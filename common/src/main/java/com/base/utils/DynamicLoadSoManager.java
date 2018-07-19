package com.base.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.base.common.R;
import com.base.global.GlobalData;
import com.base.log.MyLog;
import com.base.utils.toast.ToastUtils;
import com.base.utils.version.VersionCheckManager;
import com.base.version.http.HttpUtils;
import com.base.version.http.SimpleRequest;
import com.base.version.http.bean.BasicNameValuePair;
import com.base.version.http.bean.NameValuePair;
import com.mi.milink.sdk.base.Global;
import com.mi.milink.sdk.base.debug.CustomLogcat;
import com.mi.milink.sdk.base.os.info.StorageDash;
import com.mi.milink.sdk.util.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class DynamicLoadSoManager {

    public final static String TAG = "DynamicLoadSoManager";

    private static final String DEFAULT_LIB_DIR_NAME = "milib";

    static HashMap<String, LibInfo> sLibInfoMap = new HashMap();

    String libName;// 如broadcast

    public DynamicLoadSoManager(String libName) {
        this.libName = libName;
    }

    static class LibInfo {
        public String name;
        public boolean hasLoad = false;
        public String downloadUrl;
        public boolean tryAllWay = false;
        public boolean downloadIng = false;
        public boolean hasCheck = false;

        public LibInfo(String name) {
            this.name = name;
        }
    }


    public static boolean isLibraryExist(String libName) {
        LibInfo libInfo = sLibInfoMap.get(libName);
        if (libInfo == null) {
            libInfo = new LibInfo(libName);
            sLibInfoMap.put(libName, libInfo);
        }
        if (libInfo.hasLoad) {
            return true;
        }
        if (libInfo.hasCheck) {
            return libInfo.hasLoad;
        }
        // 首先尝试直接load
        try {
            System.loadLibrary(libName);
            // 加载成功
            libInfo.hasLoad = true;
            return true;
        } catch (UnsatisfiedLinkError e) {
            CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                    e);
        } catch (Exception e) {
            CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                    e);
        } catch (Error e) {
            CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                    e);
        }
        // 系统库里没有加载成功，看看包路径下是否有so
        String soFileName = "lib" + libName + ".so";
        final File soFile = new File(getLibDir(), soFileName);

        if (soFile.exists()) {
            try {
                String soFilePath = soFile.getAbsolutePath();
                CustomLogcat.d(TAG, "try to load library: " + soFilePath
                        + " from qzlib");
                System.load(soFilePath);
                libInfo.hasLoad = true;
                return true;
            } catch (UnsatisfiedLinkError e) {
                CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                        e);
            } catch (Exception e) {
                CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                        e);
            } catch (Error e) {
                CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                        e);
            }
        }
        libInfo.hasCheck = true;
        return false;
    }

    public void loadLibrary(final CallBack callBack) {
        final LibInfo libInfo = getLibInfo();
        if (libInfo.hasLoad) {
            if (callBack != null) {
                callBack.loadLibrarySuccess();
            }
            return;
        }
        if (libInfo.downloadIng) {
            ToastUtils.showToast("直播组件正在更新中,可在通知栏查看进度");
            return;
        }
        // 首先尝试直接load
        try {
            System.loadLibrary(libName);
            // 加载成功
            libInfo.hasLoad = true;
            if (callBack != null) {
                callBack.loadLibrarySuccess();
            }
            return;
        } catch (UnsatisfiedLinkError e) {
            CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                    e);
        } catch (Exception e) {
            CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                    e);
        } catch (Error e) {
            CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                    e);
        }
        // 系统库里没有加载成功，看看包路径下是否有so
        String soFileName = "lib" + libName + ".so";
        final File soFile = new File(getLibDir(), soFileName);

        if (soFile.exists()) {
            try {
                String soFilePath = soFile.getAbsolutePath();
                CustomLogcat.d(TAG, "try to load library: " + soFilePath
                        + " from qzlib");
                System.load(soFilePath);
                libInfo.hasLoad = true;
                if (callBack != null) {
                    callBack.loadLibrarySuccess();
                }
                return;
            } catch (UnsatisfiedLinkError e) {
                CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                        e);
            } catch (Exception e) {
                CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                        e);
            } catch (Error e) {
                CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                        e);
            }
        }

        if (!libInfo.tryAllWay) {
            // 开线程下载
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    File zipFile = downloadSo();
                    if (zipFile != null && zipFile.exists()) {
                        // 下载完成,尝试安装
                        // 尝试解压Native库文件
                        boolean resu = FileUtils.unzip(zipFile, getLibDir());

                        CustomLogcat.w(TAG, "Install Native Libs => " + resu);

                        if (zipFile.exists()) {
                            zipFile.delete();
                        }

                        // 3.文件存在了，尝试加载自己目录下的so
                        if (soFile.exists()) {
                            try {
                                String soFilePath = soFile.getAbsolutePath();
                                CustomLogcat.d(TAG, "try to load library: " + soFilePath
                                        + " from qzlib");
                                System.load(soFilePath);
                                libInfo.hasLoad = true;
                                ToastUtils.showToast("组件加载成功");
                                if (callBack != null) {
                                    callBack.loadLibrarySuccess();
                                }
                            } catch (UnsatisfiedLinkError e) {
                                CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                                        e);
                            } catch (Exception e) {
                                CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                                        e);
                            } catch (Error e) {
                                CustomLogcat.e(TAG, "cannot load library " + libName + " from system lib",
                                        e);
                            }
                            libInfo.tryAllWay = true;
                        }
                    }
                    if (!libInfo.hasLoad) {
                        downloadApk();
                    }
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io())
                    .subscribe();

        } else {
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    downloadApk();
                    subscriber.onCompleted();
                }
            })
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        }
        return;
    }

    File downloadSo() {
        LibInfo libInfo = getLibInfo();
        // 文件不存在尝试下载，
        if (TextUtils.isEmpty(libInfo.downloadUrl)) {
            // 拿url
            checkNewVersion();
        }
        // 拿到下载的url
        if (!TextUtils.isEmpty(libInfo.downloadUrl)) {
            File root = StorageDash.hasExternal() ? new File(
                    Environment.getExternalStorageDirectory(), "Xiaomi"
                    + File.separator + "WALI_LIVE_SDK") : Global.getCacheDir();
            // 后续加上给用户的提示
            final File zipFile = new File(root, libName + ".zip");
            if (!zipFile.getParentFile().exists()) {
                zipFile.getParentFile().mkdirs();
            }
            // 尝试删除临时文件
            if (zipFile.exists()) {
                zipFile.delete();
            }
            ToastUtils.showToast("下载直播所需要的组件");
            libInfo.downloadIng = true;
            boolean r = HttpUtils.downloadFile(libInfo.downloadUrl, zipFile, new HttpUtils.OnDownloadProgress() {
                @Override
                public void onDownloaded(long downloaded, long totalLength) {
                    int p = (int) (downloaded * 100 / totalLength);
                    String tips = String.format("已下载%d%%", p);
                    VersionCheckManager.getInstance().showDownloadNotification(tips);
                }

                @Override
                public void onCompleted(String localPath) {
                    VersionCheckManager.getInstance().removeNotification();
                }

                @Override
                public void onCanceled() {

                }

                @Override
                public void onFailed() {

                }
            });
            libInfo.downloadIng = false;
            return zipFile;
        }
        return null;
    }

    void downloadApk() {
        // 尝试升级直播助手

        VersionCheckManager.getInstance().startDownload(new VersionCheckManager.IUpdateListener() {
            @Override
            public void onRepeatedRequest() {
                MyLog.d(TAG, "onRepeatedRequest");
                ToastUtils.showToast("直播组件正在更新中,可在通知栏查看进度");
            }

            @Override
            public void onDownloadStart() {
                MyLog.d(TAG, "onDownloadStart");
                ToastUtils.showToast("更新直播助手");
            }

            @Override
            public void onDownloadProgress(int progress) {
                MyLog.d(TAG, "onDownloadProgress" + " progress=" + progress);
                String tips = String.format("已下载%d%%", progress);
                VersionCheckManager.getInstance().showDownloadNotification(tips);
            }

            @Override
            public void onDownloadSuccess(String path) {
                MyLog.d(TAG, "onDownloadSuccess" + " path=" + path);
                VersionCheckManager.getInstance().removeNotification();
                VersionCheckManager.getInstance().installLocalPackageN("com.wali.live.watchsdk.editinfo.fileprovider",path);
            }

            @Override
            public void onDownloadFailed(int errCode) {
                MyLog.d(TAG, "onDownloadFailed" + " errCode=" + errCode);

            }
        });
    }


    public static File getLibDir() {
        String libDirPath = getInstallPath();

        return new File(libDirPath + File.separator + DEFAULT_LIB_DIR_NAME);
    }

    @SuppressLint("SdCardPath")
    private static String getInstallPath() {
        File dirFile = Global.getFilesDir();
        if (dirFile == null) {
            dirFile = Global.getCacheDir();
        }
        if (dirFile != null) {
            return dirFile.getParent();
        } else {
            return "/data/data/" + Global.getPackageName();
        }
    }

    int checkNewVersion() {
        SimpleRequest.StringContent result = getStringContent();
        return parseResult(result);
    }

    SimpleRequest.StringContent getStringContent() {
        String miId = "0";
        String url = String.format("http://api.chat.xiaomi.net/v2/user/%s/grayupgarde", miId);
        List<NameValuePair> postBody = new ArrayList();
        postBody.add(new BasicNameValuePair("uuid", miId));
        // 注意这里为了测试，增加了STAGING_NAME
        postBody.add(new BasicNameValuePair("app", "lib" + libName));
        postBody.add(new BasicNameValuePair("platform", "android"));
        postBody.add(new BasicNameValuePair("system", String.valueOf(Build.VERSION.SDK_INT)));
        postBody.add(new BasicNameValuePair("channel", "DEFAULT")); // 渠道号
        postBody.add(new BasicNameValuePair("device", Build.MODEL));

        int version = 0;
        version = (version == 0 ? 1 : version);
        postBody.add(new BasicNameValuePair("currentVersion", String.valueOf(version)));
        postBody.add(new BasicNameValuePair("language", "zh_CN"));
        postBody.add(new BasicNameValuePair("updateId", "0"));
        postBody.add(new BasicNameValuePair("unique", miId));

        // 发送当前包的hash值
        postBody.add(new BasicNameValuePair("hash", "5fcc13f203341157dae7469f10b3121a9cb67721")); // 缺个hash会没法下载，随便写个
        SimpleRequest.StringContent result = null;
        try {
            result = HttpUtils.doV2Get(url, postBody);
        } catch (Exception e) {
        }
        return result;
    }

    private int parseResult(SimpleRequest.StringContent result) {
        if (result == null) {
            return -1;
        }
        String jsonString = result.getBody();
        if (TextUtils.isEmpty(jsonString)) {
            return -1;
        }
        try {
            JSONObject resultObj = new JSONObject(jsonString);
            if (!resultObj.has("result") || !"ok".equalsIgnoreCase(resultObj.getString("result"))) {
                return -1;
            }
            JSONObject dataObj = resultObj.getJSONObject("data");
            boolean shouldUpdate = dataObj.getBoolean("newUpdate");
            if (!shouldUpdate) {
                return 1;
            }
            String downloadUrl = dataObj.getString("downloadUrl");
            getLibInfo().downloadUrl = downloadUrl;
        } catch (JSONException e) {
            return -1;
        }
        return 0;
    }


    LibInfo getLibInfo() {
        LibInfo libInfo = sLibInfoMap.get(libName);
        if (libInfo == null) {
            libInfo = new LibInfo(libName);
            sLibInfoMap.put(libName, libInfo);
        }
        return libInfo;
    }


    public interface CallBack {
        void loadLibrarySuccess();
    }

}
