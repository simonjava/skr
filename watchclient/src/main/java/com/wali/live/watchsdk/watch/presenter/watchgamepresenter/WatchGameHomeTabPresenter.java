package com.wali.live.watchsdk.watch.presenter.watchgamepresenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.CallSuper;
import android.support.v4.content.FileProvider;

import com.base.log.MyLog;
import com.base.utils.MD5;
import com.mi.live.data.gamecenter.model.GameInfoModel;
import com.mi.live.data.room.model.RoomDataChangeEvent;
import com.thornbirds.component.IParams;
import com.wali.live.component.presenter.BaseSdkRxPresenter;
import com.wali.live.watchsdk.component.WatchComponentController;
import com.wali.live.watchsdk.editinfo.fragment.presenter.EditAvatarPresenter;
import com.wali.live.watchsdk.watch.download.CustomDownloadManager;
import com.wali.live.watchsdk.watch.view.watchgameview.WatchGameHomeTabView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * Created by vera on 2018/8/8.
 */

public class WatchGameHomeTabPresenter extends BaseSdkRxPresenter<WatchGameHomeTabView.IView>
        implements WatchGameHomeTabView.IPresenter {
    private static final String TAG = "WatchGameHomeTabPresenter";

    GameInfoModel mGameInfoModel;

    public WatchGameHomeTabPresenter(WatchComponentController controller) {
        super(controller);
        mGameInfoModel = controller.getRoomBaseDataModel().getGameInfoModel();
    }

    @Override
    protected String getTAG() {
        return TAG;
    }

    @Override
    public boolean onEvent(int event, IParams params) {
        return false;
    }

    @CallSuper
    public void startPresenter() {
        super.startPresenter();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (mView != null) {
            mView.updateUi(mGameInfoModel);
            if (mGameInfoModel != null) {
                CustomDownloadManager.getInstance().addMonitorUrl(mGameInfoModel.getPackageUrl());
            }
        }
    }

    @Override
    @CallSuper
    public void stopPresenter() {
        super.stopPresenter();
        EventBus.getDefault().unregister(this);
        if (mGameInfoModel != null) {
            CustomDownloadManager.getInstance().removeMonitorUrl(mGameInfoModel.getPackageUrl());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RoomDataChangeEvent event) {
        switch (event.type) {
            case RoomDataChangeEvent.TYPE_CHANGE_GAME_INFO: {
                if (mView != null) {
                    mView.updateUi(mGameInfoModel);
                }
            }
            break;
            default:
                break;
        }
    }

    public void tryInstallApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        String filename = mGameInfoModel.getGameId() + ".apk";
        // 由于COLUMN_LOCAL_FILENAME废弃，生成固定的下载路径
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String mDownloadFilename = Uri.withAppendedPath(Uri.fromFile(file), filename).getPath();

        Uri uri;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 待补充
            uri = FileProvider.getUriForFile(mView.getRealView().getContext(), EditAvatarPresenter.AUTHORITY, new File(mDownloadFilename));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(mDownloadFilename));
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mView.getRealView().getContext().startActivity(intent);
    }


    public void tryLaunchApk() {
        String packageName = mGameInfoModel.getPackageName();
        Intent intent = mView.getRealView().getContext().getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mView.getRealView().getContext().startActivity(intent);
        } else {
            MyLog.w(TAG, "intent launch fail, packageName=" + packageName);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CustomDownloadManager.ApkStatusEvent event) {
        String key = MD5.MD5_32(mGameInfoModel.getPackageUrl());
        if (event.downloadKey.equals(key)) {
            mView.updateDownLoadUi(event.status, event.progress, event.reason);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CustomDownloadManager.TaskEvent event) {
        String key = MD5.MD5_32(mGameInfoModel.getPackageUrl());
        if (event.downloadKey.equals(key)) {
            mView.notifyTaskRemove(event.status);
        }
    }



    @Override
    public void beginDownload() {
        CustomDownloadManager.Item item = new CustomDownloadManager.Item(mGameInfoModel.getPackageUrl(), mGameInfoModel.getGameName());
        CustomDownloadManager.getInstance().beginDownload(item);
    }

    @Override
    public void pauseDownload() {
        CustomDownloadManager.getInstance().pauseDownload(mGameInfoModel.getPackageUrl());
    }
}
