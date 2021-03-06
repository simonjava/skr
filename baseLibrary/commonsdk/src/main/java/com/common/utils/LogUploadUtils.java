package com.common.utils;

import com.common.log.MyLog;
import com.common.upload.UploadCallback;
import com.common.upload.UploadParams;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LogUploadUtils {
    public static final long MAX_AUDIO_FOR_FEEDBACK = 10 * 1024 * 1024;
    public final String TAG = "LogUploadUtils";
    Disposable mUploadLogTask;

    public void upload(final long uid, Callback callback, boolean fromSelf) {

        if (mUploadLogTask != null && !mUploadLogTask.isDisposed()) {
            if (fromSelf) {
                U.getToastUtil().showShort("正在上传日志");
            }
            return;
        }

        mUploadLogTask = Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> emitter) {
                MyLog.flushLog();
                File logDir = new File(U.getAppInfoUtils().getSubDirPath("logs"));
                if (logDir == null) {
                    emitter.onError(new Throwable("没有log文件夹"));
                    return;
                }

                String zipFile = U.getAppInfoUtils().getFilePathInSubDir("logs", "logs.zip");
                File filez = new File(zipFile);
                if (filez.exists()) {
                    filez.delete();
                }

                try {
                    filez.createNewFile();
                } catch (Exception e) {
                    emitter.onError(new Throwable("文件创建失败:" + e.getMessage()));
                    return;
                }

                boolean success = false;
                try {
                    List<String> list = new ArrayList<>();
                    list.addAll(getLogsLastXXXFile(U.getAppInfoUtils().getSubDirFile("logs"), 3));
                    list.addAll(getLogsLastXXXFile(U.getAppInfoUtils().getSubDirFile("Crash_Doraemon"), 3));
                    list.addAll(getLogsLastXXXFileLimitBySize(U.getAppInfoUtils().getSubDirFile("audio_feedback"), MAX_AUDIO_FOR_FEEDBACK));
                    success = U.getZipUtils().zip(list, filez.getAbsolutePath());
                } catch (IOException e) {
                    emitter.onError(new Throwable("文件压缩失败:" + e.getMessage()));
                    return;
                }

                if (!success) {
                    emitter.onError(new Throwable("文件压缩没成功"));
                    return;
                }

                emitter.onNext(filez);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Consumer<File>() {
            @Override
            public void accept(final File file) throws Exception {
                UploadParams.newBuilder(file.getAbsolutePath())
                        .setFileType(UploadParams.FileType.log)
                        .setFileName(uid + "_" + U.getDateTimeUtils().formatTimeStringForDate(System.currentTimeMillis()) + ".zip")
                        .startUploadAsync(new UploadCallback() {
                            @Override
                            public void onProgressNotInUiThread(long currentSize, long totalSize) {

                            }

                            @Override
                            public void onSuccessNotInUiThread(String url) {
                                MyLog.w(TAG, "日志上传成功");
                                file.delete();
                                EventBus.getDefault().post(new UploadLogEvent(url, true));
                                if (callback != null) {
                                    callback.onSuccess(url);
                                }
                            }

                            @Override
                            public void onFailureNotInUiThread(String msg) {
                                MyLog.e(TAG, msg);
                                file.delete();
                                EventBus.getDefault().post(new UploadLogEvent(null, false));
                                if (callback != null) {
                                    callback.onFailed();
                                }
                            }
                        });
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                EventBus.getDefault().post(new UploadLogEvent(null, false));
                callback.onFailed();
                MyLog.e(TAG, throwable);
            }
        });
    }

    public List<String> getLogsLastXXXFile(File logDir, int lastNum) {
        ArrayList<String> lastThreeFiles = new ArrayList<>();
        if (logDir.exists() && logDir.isDirectory()) {
            File[] fileList = logDir.listFiles();
            if (fileList == null || fileList.length == 0) {
                return lastThreeFiles;
            }
            // 文件修改时间排序
            Arrays.sort(fileList, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0)
                        return 1;
                    else if (diff == 0)
                        return 0;
                    else
                        return -1;
                }
            });
            for (int i = fileList.length - 1; i >= 0; i--) {
                if (fileList[i].isFile()) {
                    if (fileList[i].getName().endsWith(".log")
                            || fileList[i].getName().endsWith(".txt")
                            || fileList[i].getName().endsWith(".xlog")
                            || fileList[i].getName().endsWith(".m4a")
                            || fileList[i].getName().endsWith(".mp4")) {
                        lastThreeFiles.add(fileList[i].getAbsolutePath());
                        if (lastThreeFiles.size() >= lastNum) {
                            break;
                        }
                    }
                }
            }
        }
        return lastThreeFiles;
    }

    public List<String> getLogsLastXXXFileLimitBySize(File logDir, long maxSize) {
        long nowSize = 0;
        ArrayList<String> lastThreeFiles = new ArrayList<>();
        if (logDir.exists() && logDir.isDirectory()) {
            File[] fileList = logDir.listFiles();
            if (fileList == null || fileList.length == 0) {
                return lastThreeFiles;
            }
            // 文件修改时间排序
            Arrays.sort(fileList, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0)
                        return 1;
                    else if (diff == 0)
                        return 0;
                    else
                        return -1;
                }
            });
            for (int i = fileList.length - 1; i >= 0; i--) {
                if (fileList[i].isFile()) {
                    if (fileList[i].getName().endsWith(".log")
                            || fileList[i].getName().endsWith(".txt")
                            || fileList[i].getName().endsWith(".xlog")
                            || fileList[i].getName().endsWith(".m4a")
                            || fileList[i].getName().endsWith(".mp4")) {
                        nowSize = fileList[i].length();
                        lastThreeFiles.add(fileList[i].getAbsolutePath());
                        if (nowSize > maxSize) {
                            break;
                        }
                    }
                }
            }
        }
        return lastThreeFiles;
    }

    public static class UploadLogEvent {
        public String mUrl;
        public boolean mIsSuccess = false;

        public UploadLogEvent(String url, boolean isSuccess) {
            this.mUrl = url;
            this.mIsSuccess = isSuccess;
        }
    }

    public static class RequestOthersUploadLogSuccess {
        public String uploaderId;
        public String uploaderName;
        public String uploaderAvatar;
        public String mLogUrl;
        public String date;
        public String extra;
    }

    public interface Callback {
        void onSuccess(String url);

        void onFailed();
    }
}
