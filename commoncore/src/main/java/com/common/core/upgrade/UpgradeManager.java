package com.common.core.upgrade;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.Gravity;

import com.alibaba.fastjson.JSON;
import com.common.core.R;
import com.common.core.global.event.ShowDialogInHomeEvent;
import com.common.core.login.LoginActivity;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.log.MyLog;
import com.common.miLianYun.MiLianYunManager;
import com.common.provideer.MyFileProvider;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.permission.PermissionUtils;
import com.common.utils.HttpUtils;
import com.common.utils.U;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.xiaomi.market.sdk.UpdateResponse;
import com.xiaomi.market.sdk.UpdateStatus;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;
import com.xiaomi.market.sdk.XiaomiUpdateListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;


public class UpgradeManager {
    public final String TAG = "UpgradeManager";

    public static final int MSG_UPDATE_PROGRESS = 1;
    //    public static final int MSG_INSTALL = 2;
    private static final int MSG_RESET_INSTALL_FLAG = 3;

    private static final int MSG_ENSURE_DOWNLOADMANAGER_WORK = 4;

    UpgradeData mUpgradeData = new UpgradeData();

    DownloadManager mDownloadManager;

    ForceUpgradeView mForceUpgradeView;
    DialogPlus mForceUpgradeDialog;

    NormalUpgradeView mNormalUpgradeView;
    DialogPlus mNormalUpgradeDialog;

    FinishReceiver mFinishReceiver;
    DownloadChangeObserver mDownloadChangeObserver;


    private static class UpgradeManagerHolder {
        private static final UpgradeManager INSTANCE = new UpgradeManager();
    }

    public static final UpgradeManager getInstance() {
        return UpgradeManagerHolder.INSTANCE;
    }

    public UpgradeManager() {
        mDownloadManager = (DownloadManager) U.app().getSystemService(Context.DOWNLOAD_SERVICE);
        if(MiLianYunManager.INSTANCE.lianYunOpen()){
            XiaomiUpdateAgent.setUpdateAutoPopup(false);
            XiaomiUpdateAgent.setUpdateListener(new XiaomiUpdateListener() {
                @Override
                public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
                    // DEBUG ?????????????????? ???????????????????????????
                    MyLog.w(TAG,"updateStatus="+updateStatus+" updateResponse="+updateResponse.versionCode);
                    if(updateStatus == UpdateStatus.STATUS_UPDATE){
                        MiLianYunManager.INSTANCE.setHasNewVersion(true);
                    }
                }
            });
        }
    }

    public Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_PROGRESS:
                    DS ds = (DS) msg.obj;
                    if (mForceUpgradeView != null) {
                        mForceUpgradeView.updateProgress(ds.getProgress());
                    }
                    if (mNormalUpgradeView != null) {
                        mNormalUpgradeView.updateProgress(ds.getProgress());
                    }
                    if (mUpgradeData.getStatus() == UpgradeData.STATUS_DOWNLOWNED && !mUpgradeData.isMute()) {
                        install();
                        // ??????????????????????????????????????????
                    }
                    break;
//                case MSG_INSTALL:
//                    install();
//                    break;
                case MSG_RESET_INSTALL_FLAG:
                    if (mUpgradeData.getStatus() == UpgradeData.STATUS_INSTALLING) {
                        mUpgradeData.setStatus(UpgradeData.STATUS_DOWNLOWNED);
                    }
                    break;
                case MSG_ENSURE_DOWNLOADMANAGER_WORK:
                    downloadApkInner2();
                    break;
                default:
                    break;
            }
        }
    };

    public void checkUpdate1() {
//        if (true) {
//            UpgradeInfoModel upgradeInfoModel = new UpgradeInfoModel();
//            upgradeInfoModel.setDownloadURL("https://s1.zb.mi.com/miliao/apk/miliao/7.4/11.apk");
//            upgradeInfoModel.setForceUpdate(true);
//            upgradeInfoModel.setLatestVersionCode(2037);
//            upgradeInfoModel.setPackageSize(1024 * 1024 * 24 + 1024 * 800);
//            mUpgradeData.setNeedShowDialog(true);
//            onGetUpgradeInfoModel(upgradeInfoModel);
//            return;
//        }
        if(MiLianYunManager.INSTANCE.lianYunOpen()){
            if(!MiLianYunManager.INSTANCE.getHasNewVersion()){
                XiaomiUpdateAgent.update(U.app());
            }
            return;
        }
        /**
         * ??????????????????????????????????????????????????????????????????
         */
        if (mUpgradeData.getStatus() == UpgradeData.STATUS_INIT) {
            mUpgradeData.setNeedShowDialog(false);
            loadDataFromServer();
        } else if (mUpgradeData.getStatus() == UpgradeData.STATUS_LOAD_DATA_FROM_SERVER
                || mUpgradeData.getStatus() == UpgradeData.STATUS_DOWNLOWNED) {
            UpgradeInfoModel upgradeInfoModel = mUpgradeData.getUpgradeInfoModel();
            if (upgradeInfoModel != null) {
                if (upgradeInfoModel.isForceUpdate()) {
                    if (mForceUpgradeDialog != null) {
                        if (!mForceUpgradeDialog.isShowing()) {
                            mForceUpgradeDialog.show();
                        }
                    }
                }
            }
        }
    }

    public void checkUpdate2() {
        if(MiLianYunManager.INSTANCE.lianYunOpen()){
            if(!MiLianYunManager.INSTANCE.getHasNewVersion()){
                XiaomiUpdateAgent.update(U.app());
            }
            return;
        }
        mUpgradeData.setNeedShowDialog(true);
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        UpgradeCheckApi checkApi = ApiManager.getInstance().createService(UpgradeCheckApi.class);
        ApiMethods.subscribe(checkApi.getUpdateInfo(U.getAppInfoUtils().getPackageName(), 2, 1,
                U.getAppInfoUtils().getVersionCode(), (int) MyUserInfoManager.INSTANCE.getUid()),
                new ApiObserver<ApiResult>() {
                    @Override
                    public void process(ApiResult apiResult) {
                        if (apiResult.getErrno() == 0) {
                            mUpgradeData.setStatus(UpgradeData.STATUS_LOAD_DATA_FROM_SERVER);
                            boolean needUpdate = apiResult.getData().getBoolean("needUpdate");
                            mUpgradeData.setNeedUpdate(needUpdate);
                            if (needUpdate) {
                                String updateInfo = apiResult.getData().getString("updateInfo");
                                UpgradeInfoModel updateInfoModel = JSON.parseObject(updateInfo, UpgradeInfoModel.class);
                                onGetUpgradeInfoModel(updateInfoModel);
                            } else {
                                if (mUpgradeData.isNeedShowDialog()) {
                                    U.getToastUtil().showShort("?????????????????????");
                                }
                            }
                        }
                    }
                });
    }

    private void onGetUpgradeInfoModel(UpgradeInfoModel updateInfoModel) {
        if (updateInfoModel != null) {
            mUpgradeData.setUpgradeInfoModel(updateInfoModel);
            if (updateInfoModel.getLatestVersionCode() > U.getAppInfoUtils().getVersionCode()) {
                // ????????????
                if (updateInfoModel.isForceUpdate()) {
                    // ?????????????????????,??????????????????????????????
                    showForceUpgradeDialog();
                } else {
                    // ????????????,??????
                    // ?????????????????????????????????
                    if (mUpgradeData.isNeedShowDialog()) {
                        showNormalUpgradeDialog();
                    } else {
                        if (tryGetSaveFileApkVersion() == updateInfoModel.getLatestVersionCode()) {
                            // ?????????ok???,?????????????????????
                            long ts = U.getPreferenceUtils().getSettingLong("lastUpdateTs", 0);
                            if (System.currentTimeMillis() - ts > 24 * 3600 * 1000) {
                                U.getPreferenceUtils().setSettingLong("lastUpdateTs", System.currentTimeMillis());
                                showNormalUpgradeDialog();
                            }
                        } else {
                            if (U.getNetworkUtils().isWifi()) {
                                // ????????????wifi?????????????????????
                                MyLog.d(TAG, "??????wifi?????????????????????????????????");
                                downloadApk(true);
                            } else {
                                MyLog.d(TAG, "??????wifi???????????????????????????");
                            }
                        }
                        //???????????????????????????????????????????????????
                        tryShowRedDotTips();
                    }
                }
            }
        }
    }

    /**
     * ????????????????????????????????????
     */
    private void tryShowRedDotTips() {
        UpgradeInfoModel upgradeInfoModel = mUpgradeData.getUpgradeInfoModel();
        if (upgradeInfoModel != null) {
            int verionsCodePref = U.getPreferenceUtils().getSettingInt("show_reddot_upgrade_version", 0);
            if (verionsCodePref != upgradeInfoModel.getLatestVersionCode()) {
                // ??????????????????
                U.getPreferenceUtils().setSettingInt("show_reddot_upgrade_version", upgradeInfoModel.getLatestVersionCode());
                U.getPreferenceUtils().setSettingBoolean("need_show_upgrade_reddot", true);
                mUpgradeData.setNeedShowRedDot(true);
            } else {
                boolean needShowReddot = U.getPreferenceUtils().getSettingBoolean("need_show_upgrade_reddot", false);
                mUpgradeData.setNeedShowRedDot(needShowReddot);
            }
        }
    }

    /**
     * ????????????????????????
     */
    public boolean needShowRedDotTips() {
        return mUpgradeData.isNeedShowRedDot();
    }

    /**
     * ????????????????????????
     */
    public void setNotNeedShowRedDotTips() {
        U.getPreferenceUtils().setSettingBoolean("need_show_upgrade_reddot", false);
        mUpgradeData.setNeedShowRedDot(false);
    }

    private void showNormalUpgradeDialog() {
        if (mNormalUpgradeDialog == null) {
            mNormalUpgradeView = new NormalUpgradeView(U.app());
            mNormalUpgradeView.setListener(new NormalUpgradeView.Listener() {
                @Override
                public void onUpdateBtnClick() {
                    forceDownloadBegin();
                }

                @Override
                public void onQuitBtnClick() {
                    //cancelDownload();
                    dimissDialog();
                }

                @Override
                public void onCancelBtnClick() {
                    //cancelDownload();
                    dimissDialog();
                }
            });
        }

        if (mNormalUpgradeDialog == null || !mNormalUpgradeDialog.isShowing()) {
            Activity activity = U.getActivityUtils().getTopActivity();
            if (activity != null) {
                mNormalUpgradeDialog = DialogPlus.newDialog(activity)
                        .setContentHolder(new ViewHolder(mNormalUpgradeView))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(true)
                        .setContentBackgroundResource(R.color.transparent)
                        .setOverlayBackgroundResource(R.color.black_trans_80)
                        .setExpanded(false)
                        .create();
            }
        }

        mNormalUpgradeView.bindData(mUpgradeData.getUpgradeInfoModel());
        int localVersionCode = tryGetSaveFileApkVersion();
        if (localVersionCode == mUpgradeData.getUpgradeInfoModel().getLatestVersionCode()) {
            // ?????????????????????????????????
            mUpgradeData.setStatus(UpgradeData.STATUS_DOWNLOWNED);
            mNormalUpgradeView.setAlreadyDownloadTips();
        }
        if (U.getActivityUtils().isHomeActivity(U.getActivityUtils().getTopActivity())) {
            EventBus.getDefault().post(new ShowDialogInHomeEvent(mNormalUpgradeDialog, 1));
        } else {
            mNormalUpgradeDialog.show();
        }
    }

    private void showForceUpgradeDialog() {
        if (mForceUpgradeView == null) {
            mForceUpgradeView = new ForceUpgradeView(U.app());
            mForceUpgradeView.setListener(new ForceUpgradeView.Listener() {
                @Override
                public boolean onUpdateBtnClick() {
                    return forceDownloadBegin();
                }

                @Override
                public void onQuitBtnClick() {
                    Process.killProcess(Process.myPid());
                }

                @Override
                public void onCancelBtnClick(int progress) {
                    if (progress >= 100) {
                        install();
                    } else {
                        //cancelDownload();
                    }
                }
            });
        }
        if (mForceUpgradeDialog == null || !mForceUpgradeDialog.isShowing()) {
            /**
             * ????????????????????????activity??????????????????activity?????????topactivity???????????????
             */
            Activity activity = U.getActivityUtils().getTopActivity();
            if(activity instanceof LoginActivity){

            }else {
                activity = U.getActivityUtils().getHomeActivity();
            }
            if (activity != null) {
                mForceUpgradeDialog = DialogPlus.newDialog(activity)
                        .setContentHolder(new ViewHolder(mForceUpgradeView))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(false)
                        .setContentBackgroundResource(R.color.transparent)
                        .setOverlayBackgroundResource(R.color.black_trans_80)
                        .setExpanded(false)
                        .create();
            }
        }
        mForceUpgradeView.bindData(mUpgradeData.getUpgradeInfoModel());
        int localVersionCode = tryGetSaveFileApkVersion();
        if (localVersionCode == mUpgradeData.getUpgradeInfoModel().getLatestVersionCode()) {
            // ?????????????????????????????????
            mUpgradeData.setStatus(UpgradeData.STATUS_DOWNLOWNED);
            mForceUpgradeView.setAlreadyDownloadTips();
        }
        if (U.getActivityUtils().isHomeActivity(U.getActivityUtils().getTopActivity())) {
            EventBus.getDefault().post(new ShowDialogInHomeEvent(mForceUpgradeDialog, 1));
        } else {
            mForceUpgradeDialog.show();
        }
    }

    private boolean forceDownloadBegin() {
        int localVersionCode = tryGetSaveFileApkVersion();
        if (localVersionCode == mUpgradeData.getUpgradeInfoModel().getLatestVersionCode()) {
            // ?????????????????????????????????
            mUpgradeData.setStatus(UpgradeData.STATUS_DOWNLOWNED);
            install();
            return false;
        } else {
            downloadApk(false);
            return true;
        }
    }

    private int tryGetSaveFileApkVersion() {
        File saveFile = getSaveFile();
        int version = 0;
        if (saveFile.exists()) {
            try {
                PackageManager pm = U.app().getApplicationContext().getPackageManager();
                PackageInfo packageInfo = pm.getPackageArchiveInfo(saveFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
                if (U.getAppInfoUtils().getPackageName().equals(packageInfo.packageName)) {
                    version = packageInfo.versionCode;
                }
            } catch (Exception e) {
            }
        } else {
            MyLog.d(TAG, "tryGetSaveFileApkVersion saveFile not exist");
        }
        MyLog.d(TAG, "tryGetSaveFileApkVersion saveFileApkVersion:" + version);
        return version;
    }

    // ??????apk
    private void downloadApk(final boolean mute) {
        MyLog.d(TAG, "downloadApk" + " mute=" + mute);
        if (mUpgradeData.getStatus() >= UpgradeData.STATUS_DOWNLOWNING) {
            File saveFile = getSaveFile();
            if (saveFile.exists()) {
                MyLog.d(TAG, "downloadApk ???????????????");
                return;
            } else {
                mUpgradeData.setStatus(UpgradeData.STATUS_LOAD_DATA_FROM_SERVER);
            }
        }
        mUpgradeData.setMute(mute);
        Activity topActivity = U.getActivityUtils().getTopActivity();
        boolean hasPer = U.getPermissionUtils().checkExternalStorage(topActivity);
        if (!hasPer) {
            U.getPermissionUtils().requestExternalStorage(new PermissionUtils.RequestPermission() {
                @Override
                public void onRequestPermissionSuccess() {
                    downloadApkInner1();
                }

                @Override
                public void onRequestPermissionFailure(List<String> permissions) {

                }

                @Override
                public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

                }
            }, topActivity);
        } else {
            downloadApkInner1();
        }
    }

    private void downloadApkInner1() {
        MyLog.d(TAG,"downloadApkInner1" );
        cancelDownload2();
        File saveFile = getSaveFile();
        if (saveFile.exists()) {
            saveFile.delete();
        }
        UpgradeInfoModel updateInfoModel = mUpgradeData.getUpgradeInfoModel();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(updateInfoModel.getDownloadURL()));
        request.setMimeType("application/vnd.android.package-archive");
        if (!mUpgradeData.isMute()) {
            // ????????????
            request.setTitle(U.getAppInfoUtils().getAppName());
            // ???????????????
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        } else {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        }
//        // ???????????????????????????
//        int networkFlag = DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI;
//        request.setAllowedNetworkTypes(networkFlag);
//        // ???????????????????????????????????????
//        request.setAllowedOverRoaming(false);
        // ????????????????????????
        request.setDestinationUri(Uri.fromFile(getSaveFile()));

        // ???????????????????????????????????????????????????Downloads???????????????????????????
        // ??????????????????Request?????????setVisibleInDownloadsUi?????????????????????true.
        request.setVisibleInDownloadsUi(true);
        long downloadId = mDownloadManager.enqueue(request);
        mUpgradeData.setDownloadId(downloadId);
        //?????????????????????????????????????????????????????????
        registerObserver();
        registerReceiver();
        mUiHandler.removeMessages(MSG_ENSURE_DOWNLOADMANAGER_WORK);
        mUiHandler.sendEmptyMessageDelayed(MSG_ENSURE_DOWNLOADMANAGER_WORK, 6000);
    }

    private void downloadApkInner2() {
        mUiHandler.removeMessages(MSG_ENSURE_DOWNLOADMANAGER_WORK);
        cancelDownload1();
        UpgradeInfoModel updateInfoModel = mUpgradeData.getUpgradeInfoModel();
        U.getHttpUtils().downloadFileAsync(updateInfoModel.getDownloadURL(), getSaveFile(), true, new HttpUtils.OnDownloadProgress() {
            @Override
            public void onDownloaded(long downloaded, long totalLength) {
                DS ds = new DS();
                ds.progress = (int) (downloaded * 100 / totalLength);
                ds.status = DS.STATUS_DOWNLOADING;
                if (mUpgradeData.getStatus() < UpgradeData.STATUS_DOWNLOWNING) {
                    mUpgradeData.setStatus(UpgradeData.STATUS_DOWNLOWNING);
                }
                MyLog.d(TAG, "downloadApkInner2 updateProgress " + ds);
                Message msg = mUiHandler.obtainMessage(MSG_UPDATE_PROGRESS);
                msg.obj = ds;
                mUiHandler.sendMessage(msg);

                // ????????????
            }

            @Override
            public void onCompleted(String localPath) {
                DS ds = new DS();
                ds.progress = 100;
                ds.status = DS.STATUS_SUCCESS;
                if (mUpgradeData.getStatus() < UpgradeData.STATUS_DOWNLOWNED) {
                    mUpgradeData.setStatus(UpgradeData.STATUS_DOWNLOWNED);
                }
                MyLog.d(TAG, "downloadApkInner2 updateProgress " + ds);
                Message msg = mUiHandler.obtainMessage(MSG_UPDATE_PROGRESS);
                msg.obj = ds;
                mUiHandler.sendMessage(msg);

                // ????????????
            }

            @Override
            public void onCanceled() {

            }

            @Override
            public void onFailed() {

            }
        });
    }


    private void registerObserver() {
        if (mDownloadChangeObserver == null) {
            mDownloadChangeObserver = new DownloadChangeObserver();
        }
        U.app().getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, mDownloadChangeObserver);
    }

    private void registerReceiver() {
        if (mFinishReceiver == null) {
            mFinishReceiver = new FinishReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        U.app().registerReceiver(mFinishReceiver, intentFilter);
    }

    private void unregister() {
        if (mUpgradeData.getDownloadId() > 0) {
            mDownloadManager.remove(mUpgradeData.getDownloadId());
        }
        if (mDownloadChangeObserver != null) {
            U.app().getContentResolver().unregisterContentObserver(mDownloadChangeObserver);
            mDownloadChangeObserver = null;
        }
        if (mFinishReceiver != null) {
            U.app().unregisterReceiver(mFinishReceiver);
            mFinishReceiver = null;
        }
    }

    private void cancelDownload() {
        MyLog.d(TAG,"cancelDownload" );
        cancelDownload1();
        cancelDownload2();
        mUpgradeData.setStatus(UpgradeData.STATUS_LOAD_DATA_FROM_SERVER);
    }

    private void cancelDownload1() {
        MyLog.d(TAG,"cancelDownload1" );
        unregister();
    }

    private void cancelDownload2() {
        MyLog.d(TAG,"cancelDownload2" );
        UpgradeInfoModel upgradeInfoModel = mUpgradeData.getUpgradeInfoModel();
        if (upgradeInfoModel != null) {
            U.getHttpUtils().cancelDownload(upgradeInfoModel.getDownloadURL());
        }
    }

    /**
     * ??????query???????????????????????????????????????????????????????????????????????????
     *
     * @param downloadId
     * @return
     */
    private DS getBytesAndStatus(long downloadId) {
        DS ds = new DS();
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = mDownloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //????????????????????????
                int downloaded = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //????????????????????????
                int total = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                if (total != 0) {
                    ds.progress = downloaded * 100 / total;
                }
                //????????????
                ds.status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ds;
    }

    private void updateProgress() {
        DS ds = getBytesAndStatus(mUpgradeData.getDownloadId());
        if (ds.status == DS.STATUS_DOWNLOADING) {
            if (mUpgradeData.getStatus() < UpgradeData.STATUS_DOWNLOWNING) {
                mUpgradeData.setStatus(UpgradeData.STATUS_DOWNLOWNING);
            }
        } else if (ds.status == DS.STATUS_SUCCESS) {
            if (mUpgradeData.getStatus() < UpgradeData.STATUS_DOWNLOWNED) {
                mUpgradeData.setStatus(UpgradeData.STATUS_DOWNLOWNED);
            }
        } else {
            mUpgradeData.setStatus(UpgradeData.STATUS_LOAD_DATA_FROM_SERVER);
        }
        if (ds.progress > 0) {
            mUiHandler.removeMessages(MSG_ENSURE_DOWNLOADMANAGER_WORK);
        }
        MyLog.d(TAG, "updateProgress " + ds);
        Message msg = mUiHandler.obtainMessage(MSG_UPDATE_PROGRESS);
        msg.obj = ds;
        mUiHandler.sendMessage(msg);
    }

    private File getSaveFile() {
        File file = new File(U.getAppInfoUtils().getMainDir(), "skrer.apk");
        return file;
    }

    private void dimissDialog() {
        if (mForceUpgradeDialog != null) {
            mForceUpgradeDialog.dismiss();
            mForceUpgradeDialog = null;
            mForceUpgradeView = null;
        }
        if (mNormalUpgradeDialog != null) {
            mNormalUpgradeDialog.dismiss();
            mNormalUpgradeDialog = null;
            mNormalUpgradeView = null;
        }
    }

    /**
     * ????????????
     */
    private void install() {
        MyLog.d(TAG, "install");
        File file = getSaveFile();
        if (file == null || !file.exists()) {
            MyLog.d(TAG, "??????????????????cancel");
            return;
        }
        if (mUpgradeData.getStatus() >= UpgradeData.STATUS_INSTALLING) {
            MyLog.d(TAG, "??????????????????cancel");
            return;
        }
        mUpgradeData.setStatus(UpgradeData.STATUS_INSTALLING);
        // ???????????????????????????
        mUiHandler.removeMessages(MSG_RESET_INSTALL_FLAG);
        mUiHandler.sendEmptyMessageDelayed(MSG_RESET_INSTALL_FLAG, 4000);
        if (!mUpgradeData.getUpgradeInfoModel().isForceUpdate()) {
            dimissDialog();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // ???????????????Activity???????????????Activity,?????????????????????
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //??????1 ?????????, ??????2 Provider???????????? ??????????????????????????????   ??????3  ???????????????
            Uri apkUri = MyFileProvider.getUriForFile(U.app(), U.getAppInfoUtils().getPackageName() + ".provider", file);
            //???????????????????????????????????????????????????Uri??????????????????
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            U.app().startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri,
                    "application/vnd.android.package-archive");
            U.app().startActivity(intent);
        }
    }

    private void onInstallOk() {
        MyLog.d(TAG, "onInstallOk");
        unregister();
        mUpgradeData.setStatus(UpgradeData.STATUS_FINISH);
        dimissDialog();
        U.getToastUtil().showShort("????????????");
    }

    static class DS {
        public static final int STATUS_DOWNLOADING = 2;
        public static final int STATUS_SUCCESS = 8;
        int progress;
        int status;

        public int getProgress() {
            return progress;
        }

        @Override
        public String toString() {
            return "DS{" +
                    "progress=" + progress +
                    ", status=" + status +
                    '}';
        }
    }

    /**
     * ??????????????????
     */
    private class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver() {
            super(mUiHandler);
        }


        /**
         * ???????????????Uri???????????????????????????????????????
         *
         * @param selfChange ??????????????????, ???????????????????????????false
         */
        @Override
        public void onChange(boolean selfChange) {
            updateProgress();
        }
    }

    /**
     * ??????????????????
     * ????????????
     */
    private class FinishReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MyLog.d(TAG,"onReceive" + " context=" + context + " intent=" + intent);
            if (intent.getAction().equals(DownloadManager.EXTRA_DOWNLOAD_ID)) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == mUpgradeData.getDownloadId()) {
                    mUpgradeData.setStatus(UpgradeData.STATUS_DOWNLOWNED);
                    if (!mUpgradeData.isMute()) {
                        install();
                    }
                }
            } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                if (U.getAppInfoUtils().getPackageName().equals(packageName)) {
                    onInstallOk();
                }
            } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                if (U.getAppInfoUtils().getPackageName().equals(packageName)) {
                    onInstallOk();
                }
            }
        }
    }

}
