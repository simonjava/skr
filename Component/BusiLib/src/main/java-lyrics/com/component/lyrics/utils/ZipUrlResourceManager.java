package com.component.lyrics.utils;

import com.common.log.MyLog;
import com.common.utils.HttpUtils;
import com.common.utils.U;
import com.component.lyrics.model.UrlRes;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 用于下载多个文件一个进度条下载，主要场景是下载音乐资源文件，歌词，mid，mp3，原唱等等
 */
public class ZipUrlResourceManager {
    public static final String TAG = "ZipUrlResourceManager";

    List<UrlRes> taskQueue;

    volatile UrlRes currentTask;

    HttpUtils.OnDownloadProgress onDownloadProgress;

    Disposable checkTotlaSizeTask;

    //所有url对应的资源文件的大小
    volatile long totalLength = 0;
    volatile long downloadLength = 0;

    boolean mIsFinished = false;

    volatile boolean mIsCancel = false;

    int mFiledTime = 0;

    public ZipUrlResourceManager(List<UrlRes> songResArrayList, HttpUtils.OnDownloadProgress onDownloadProgress) {
        this.taskQueue = new LinkedList<>(songResArrayList);
        this.onDownloadProgress = onDownloadProgress;
    }


    /**
     * 开始下载
     */
    public void go() {
        if (taskQueue == null) {
            if (onDownloadProgress != null) {
                onDownloadProgress.onFailed();
            }
            MyLog.e(TAG, "queue is null");
            return;
        }

        LinkedList<UrlRes> songResList = new LinkedList<>(taskQueue);
        taskQueue.clear();

        Observable.fromIterable(songResList)
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<UrlRes>() {
                    @Override
                    public boolean test(UrlRes songRes) throws Exception {
                        return !songRes.isExist();
                    }
                })
                .subscribe(new Observer<UrlRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        checkTotlaSizeTask = d;
                    }

                    @Override
                    public void onNext(UrlRes songRes) {
                        MyLog.d(TAG, "onNext" + " songRes=" + songRes);
                        long length = U.getHttpUtils().getFileLength(songRes.downloadUrl);
                        if (length > 0) {
                            totalLength = totalLength + length;
                            songRes.length = length;
                            taskQueue.add(songRes);
                            MyLog.d(TAG, "onNext" + " totalLength =" + totalLength);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MyLog.e(TAG, "what the fuck");
                        if (onDownloadProgress != null) {
                            onDownloadProgress.onFailed();
                        }
                    }

                    @Override
                    public void onComplete() {
                        startQueue();
                    }
                });
    }
    
    public boolean containUrl(String url){
        if(taskQueue == null){
            return false;
        }

        for (UrlRes urlRes : taskQueue) {
            if(urlRes.getResUrl().equals(url)){
                return true;
            }
        }

        return false;
    }

    private void startQueue() {
        if(mIsCancel){
            return;
        }

        UrlRes res = null;
        if (taskQueue.size() > 0) {
            res = taskQueue.remove(0);
        }

        final UrlRes songRes = res;

        //下载完了
        if (songRes == null) {
            if (onDownloadProgress != null) {
                MyLog.d(TAG, "startQueue 已经下载完所有资源");
                onDownloadProgress.onCompleted("");
            }

            mIsFinished = true;
            currentTask = null;
            return;
        }

        if (songRes.length <= 0) {
            MyLog.d(TAG, "startQueue songRes length is <= 0");
            startQueue();
            return;
        }


        currentTask = songRes;

        //先用临时文件路径下载
        U.getHttpUtils().downloadFileSync(songRes.getResUrl(), songRes.outFile,true, new HttpUtils.OnDownloadProgress() {
            @Override
            public void onDownloaded(long downloaded, long length) {
                if (onDownloadProgress != null) {
                    //这个totalLength所有文件的大小
                    long l = downloadLength + downloaded;
                    onDownloadProgress.onDownloaded(l, totalLength);
//                    MyLog.d(TAG, "onDownloaded" + " downloaded=" + l + " total length=" + totalLength);
                }
            }

            @Override
            public void onCompleted(String localPath) {
                downloadLength = downloadLength + songRes.length;
                MyLog.d(TAG, "onCompleted" + " localPath=" + localPath);
                MyLog.d(TAG, "onCompleted" + " songRes.length=" + songRes.length);

                startQueue();
            }

            @Override
            public void onCanceled() {
                MyLog.w(TAG, "onCanceled");
                if (onDownloadProgress != null) {
                    onDownloadProgress.onCanceled();
                }
            }

            @Override
            public void onFailed() {
                MyLog.w(TAG, "onFailed");
                if(++mFiledTime < 10){
                    taskQueue.add(songRes);
                    startQueue();
                }

                if (onDownloadProgress != null) {
                    onDownloadProgress.onFailed();
                }
            }
        });
    }

    /**
     * 取消当前以及之后的所有task
     */
    public void cancelAllTask() {
        mIsCancel = true;
        if(mIsFinished){
            MyLog.d(TAG, "cancel when tasklist is finished");
            onDownloadProgress = null;
            return;
        }

        if(onDownloadProgress != null){
            onDownloadProgress.onCanceled();
            onDownloadProgress = null;
        }

        if (checkTotlaSizeTask != null && !checkTotlaSizeTask.isDisposed()) {
            checkTotlaSizeTask.dispose();
        }

        if (currentTask != null) {
            U.getHttpUtils().cancelDownload(currentTask.downloadUrl);
        }

        taskQueue.clear();
    }
}
