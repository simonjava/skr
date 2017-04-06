package com.wali.live.channel.viewmodel;

import com.base.utils.display.DisplayUtils;
import com.google.protobuf.GeneratedMessage;

/**
 * Created by lan on 16/6/28.
 *
 * @module 频道
 * @description 频道数据模型抽象基类，提供title的数据处理
 */
public abstract class ChannelViewModel<GM extends GeneratedMessage> extends BaseViewModel {
    protected int mUiType;
    protected boolean mFullColumn;

    protected int mImageWidth;
    protected int mImageHeight;
    protected float mRatio = 1f;
    protected int mFrameHeight;

    /**
     * 注意：此构造函数用于构造测试数据
     */
    protected ChannelViewModel() {
    }

    protected ChannelViewModel(GM protoItem) throws Exception {
        parse(protoItem);
    }

    protected void parse(GM protoItem) throws Exception {
        parseTemplate(protoItem);
    }

    protected abstract void parseTemplate(GM protoItem) throws Exception;

    public int getUiType() {
        return mUiType;
    }

    public boolean isFullColumn() {
        return mFullColumn;
    }

    public void setImageSize(int width, int height) {
        if (mImageWidth == width && mImageHeight == height) {
            return;
        }
        mImageWidth = width;
        mImageHeight = height;

        if (mImageWidth != 0) {
            mRatio = 1f * mImageHeight / mImageWidth;
            mFrameHeight = (int) (mRatio * DisplayUtils.getScreenWidth());
        } else {
            mFrameHeight = 0;
        }
    }

    public float getRatio() {
        return mRatio;
    }

    public int getFrameHeight() {
        return mFrameHeight;
    }
}
