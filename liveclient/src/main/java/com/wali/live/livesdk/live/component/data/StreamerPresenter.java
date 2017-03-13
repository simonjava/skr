package com.wali.live.livesdk.live.component.data;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.base.log.MyLog;
import com.mi.live.engine.base.GalileoConstants;
import com.mi.live.engine.streamer.IStreamer;
import com.wali.live.component.presenter.ComponentPresenter;

/**
 * Created by yangli on 2017/03/08.
 * <p>
 * Generated using create_component_data.py
 *
 * @module 推流器数据
 */
public class StreamerPresenter extends ComponentPresenter {
    private static final String TAG = "StreamerPresenter";

    private int mMusicVolume = 50; // 音乐音量
    private int mVoiceVolume = 50; // 人声音量
    private boolean mMirrorImage = true; // 是否开启镜像
    private boolean mBackCamera = false; // 是否为后置摄像头
    private boolean mFlashLight = false; // 是否开启闪光灯
    private boolean mHifi = false; // 是否开启高保真
    private int mReverb = GalileoConstants.TYPE_ORIGINAL; // 混响类型

    private IStreamer mStreamer;

    public void setStreamer(IStreamer streamer) {
        mStreamer = streamer;
    }

    public StreamerPresenter(
            @NonNull IComponentController componentController) {
        super(componentController);
    }

    @Override
    public void destroy() {
        super.destroy();
        mStreamer = null;
    }

    @Nullable
    @Override
    protected IAction createAction() {
        return new Action();
    }

    // 设置人声音量
    public void setVoiceVolume(int volume) {
        if (mStreamer != null && mVoiceVolume != volume) {
            mVoiceVolume = volume;
            mStreamer.setVoiceVolume(mVoiceVolume / 50.0f);
        }
    }

    public int getVoiceVolume() {
        return mVoiceVolume;
    }

    // 设置混响
    public void setReverb(int type) {
        if (mStreamer != null && mReverb != type) {
            mReverb = type;
            mStreamer.setReverbLevel(mReverb);
        }
    }

    public int getReverb() {
        return mReverb;
    }

    // 开启高保真
    public void enableHifi(boolean enable) {
        if (mStreamer != null && mHifi != enable) {
            mHifi = enable;
            mStreamer.setAudioType(mHifi ? 1 : 0);
        }
    }

    public boolean isHifi() {
        return false;
    }

    // 切换前后置摄像头
    public void switchCamera() {
        if (mStreamer != null) {
            MyLog.w(TAG, "switchCamera");
            mBackCamera = !mBackCamera;
            mStreamer.switchCamera();
            mStreamer.setMirrorMode(mBackCamera ? false : mMirrorImage);
        }
    }

    // 当前是否为后置摄像头
    public boolean isBackCamera() {
        return mBackCamera;
    }

    // 开启镜像
    public void enableMirrorImage(boolean enable) {
        if (mStreamer != null) {
            mMirrorImage = enable;
            if (!mBackCamera) {
                mStreamer.setMirrorMode(mMirrorImage);
            }
        }
    }

    public boolean isMirrorImage() {
        return mMirrorImage;
    }

    // 开启闪光灯
    public void enableFlashLight(boolean enable) {
        if (mStreamer != null) {
            mFlashLight = enable;
            mStreamer.toggleTorch(mFlashLight);
        }
    }

    public boolean isFlashLight() {
        return mFlashLight;
    }

    public class Action implements IAction {
        @Override
        public boolean onAction(int source, @Nullable Params params) {
            switch (source) {
                default:
                    break;
            }
            return false;
        }
    }
}
