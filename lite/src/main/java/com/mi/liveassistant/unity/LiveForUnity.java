package com.mi.liveassistant.unity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.mi.liveassistant.common.api.ErrorCode;
import com.mi.liveassistant.common.log.MyLog;
import com.mi.liveassistant.room.manager.live.GameLiveManager;
import com.mi.liveassistant.room.manager.live.callback.ILiveCallback;
import com.mi.liveassistant.room.manager.live.callback.ILiveListener;

/**
 * Created by yangli on 2017/5/4.
 *
 * @module Unity游戏直播辅助类
 */
public class LiveForUnity extends UnitySdk<Activity, IUnityLiveListener> {
    private static final String TAG = "LiveForUnity";

    private GameLiveManager mLiveManager;
    private boolean mIsBegin;

    private String mTitle;
    private String mCoverUrl;

    @Override
    protected String getTAG() {
        return TAG;
    }

    public LiveForUnity(Activity activity, IUnityLiveListener listener) {
        super(activity, listener);
        MyLog.w(TAG, "LiveForUnity");
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLiveManager = new GameLiveManager(new ILiveListener() {
                    @Override
                    public void onEndUnexpected() {
                        MyLog.w(TAG, "onEndUnexpected");
                        if (mUnityListener != null) {
                            mUnityListener.onEndUnexpected();
                        }
                    }
                });
                if (mActivity instanceof MiLiveActivity) {
                    ((MiLiveActivity) mActivity).setLiveForUnity(LiveForUnity.this);
                }
            }
        });
    }

    public void startLive(@NonNull Intent intent) {
        startLive(intent, mTitle, mCoverUrl);
    }

    public void startLive(final String title, final String coverUrl) {
        mActivity.runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    if (mUnityListener != null) {
                        mUnityListener.onBeginLiveFailed(ErrorCode.CODE_ERROR_TOO_LOW_SDK);
                    }
                    return;
                }
                MyLog.w(TAG, "startLive");
                if (mActivity instanceof MiLiveActivity) {
                    mTitle = title;
                    mCoverUrl = coverUrl;
                    ((MiLiveActivity) mActivity).queryScreenRecordIntent();
                } else if (mUnityListener != null) {
                    mUnityListener.onBeginLiveFailed(ErrorCode.CODE_ERROR_MISSING_PROJECTION);
                }
            }
        });
    }

    public void startLive(final @NonNull Intent intent, final String title, final String coverUrl) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    if (mUnityListener != null) {
                        mUnityListener.onBeginLiveFailed(ErrorCode.CODE_ERROR_TOO_LOW_SDK);
                    }
                    return;
                }
                MyLog.w(TAG, "startLive mIsBegin=" + mIsBegin);
                if (mIsBegin) {
                    return;
                }
                mLiveManager.setCaptureIntent(intent);
                mLiveManager.beginLive(null, title, coverUrl, new ILiveCallback() {
                    @Override
                    public void notifyFail(int errCode) {
                        MyLog.w(TAG, "startLive failed, errCode=" + errCode);
                        if (mUnityListener != null) {
                            mUnityListener.onBeginLiveFailed(errCode);
                        }
                    }

                    @Override
                    public void notifySuccess(long playerId, String liveId) {
                        mIsBegin = true;
                        MyLog.w(TAG, "startLive success");
                        if (mUnityListener != null) {
                            mUnityListener.onBeginLiveSuccess(playerId, liveId);
                        }
                    }
                });
            }
        });
    }

    public void stopLive() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyLog.w(TAG, "stopLive mIsBegin=" + mIsBegin);
                if (!mIsBegin) {
                    return;
                }
                mLiveManager.endLive(new ILiveCallback() {
                    @Override
                    public void notifyFail(int errCode) {
                        MyLog.w(TAG, "stopLive failed");
                        if (mUnityListener != null) {
                            mUnityListener.onEndLiveFailed(errCode);
                        }
                    }

                    @Override
                    public void notifySuccess(long playerId, String liveId) {
                        mIsBegin = false;
                        MyLog.w(TAG, "stopLive success");
                        if (mUnityListener != null) {
                            mUnityListener.onEndLiveSuccess(playerId, liveId);
                        }
                    }
                });
            }
        });
    }

    public void resume() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLiveManager.resume();
            }
        });
    }

    public void pause() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLiveManager.pause();
            }
        });
    }

    public void destroy() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLiveManager.destroy();
                mLiveManager = null;
            }
        });
    }
}
