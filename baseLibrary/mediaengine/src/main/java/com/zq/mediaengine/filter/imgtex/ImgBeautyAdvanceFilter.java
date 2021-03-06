package com.zq.mediaengine.filter.imgtex;

import android.content.Context;
import android.util.Log;

import com.zq.mediaengine.framework.ImgTexFrame;
import com.zq.mediaengine.framework.SinkPin;
import com.zq.mediaengine.framework.SrcPin;
import com.zq.mediaengine.util.gles.GLRender;

/**
 * @hide
 */

public class ImgBeautyAdvanceFilter extends ImgFilterBase {
    private static final String TAG = "ImgBeautyAdvanceFilter";

    private ImgBeautyGrindAdvanceFilter mGrindAdvanceFilter;
    private ImgBeautySpecialEffectsFilter mSpecialEffectsFilter;
    private ImgBeautyAdjustSkinColorFilter mAdjustSkinColorFilter;

    public ImgBeautyAdvanceFilter(GLRender glRender, Context context) {
        mGrindAdvanceFilter = new ImgBeautyGrindAdvanceFilter(glRender);
        try {
            mSpecialEffectsFilter = new ImgBeautySpecialEffectsFilter(glRender,
                    context, "13_nature.png");
        } catch (Exception e) {
            Log.e(TAG, "KSYResource missing!");
        }
        if (mSpecialEffectsFilter != null) {
            mGrindAdvanceFilter.getSrcPin().connect(mSpecialEffectsFilter.getSinkPin());
        }

        String pinkUri = "assets://KSYResource/0_pink.png";
        String ruddyUri = "assets://KSYResource/0_ruddy2.png";
        try {
            mAdjustSkinColorFilter = new ImgBeautyAdjustSkinColorFilter(glRender, context,
                    pinkUri, ruddyUri);
        } catch (Exception e) {
            Log.e(TAG, "KSYResource missing!");
        }
        if (mAdjustSkinColorFilter != null) {
            if (mSpecialEffectsFilter != null) {
                mSpecialEffectsFilter.getSrcPin().connect(mAdjustSkinColorFilter.getSinkPin());
            } else {
                mGrindAdvanceFilter.getSrcPin().connect(mAdjustSkinColorFilter.getSinkPin());
            }
        }
        setGrindRatio(0.3f);
        setRuddyRatio(0.0f);
    }

    @Override
    public void setOnErrorListener(OnErrorListener listener) {
        super.setOnErrorListener(listener);
        mGrindAdvanceFilter.setOnErrorListener(mErrorListener);
        if (mSpecialEffectsFilter != null) {
            mSpecialEffectsFilter.setOnErrorListener(mErrorListener);
        }
        if (mAdjustSkinColorFilter != null) {
            mAdjustSkinColorFilter.setOnErrorListener(mErrorListener);
        }
    }

    @Override
    public boolean isGrindRatioSupported() {
        return true;
    }

    @Override
    public boolean isWhitenRatioSupported() {
        return mSpecialEffectsFilter != null;
    }

    @Override
    public boolean isRuddyRatioSupported() {
        return mAdjustSkinColorFilter != null;
    }

    @Override
    public void setGrindRatio(float ratio) {
        super.setGrindRatio(ratio);
        mGrindAdvanceFilter.setGrindRatio(ratio);
    }

    @Override
    public void setWhitenRatio(float ratio) {
        super.setWhitenRatio(ratio);
        if (mSpecialEffectsFilter != null) {
            mSpecialEffectsFilter.setIntensity(ratio);
        }
    }

    /**
     * Set the ruddy ratio.
     *
     * @param ratio the ratio between -1.0f~1.0f
     */
    @Override
    public void setRuddyRatio(float ratio) {
        super.setRuddyRatio(ratio);
        if (mAdjustSkinColorFilter != null) {
            mAdjustSkinColorFilter.setRuddyRatio(ratio);
        }
    }

    @Override
    public int getSinkPinNum() {
        return 1;
    }


    @Override
    public SinkPin<ImgTexFrame> getSinkPin(int idx) {
        return mGrindAdvanceFilter.getSinkPin();
    }

    @Override
    public SrcPin<ImgTexFrame> getSrcPin() {
        if (mAdjustSkinColorFilter != null) {
            return mAdjustSkinColorFilter.getSrcPin();
        } else if (mSpecialEffectsFilter != null) {
            return mSpecialEffectsFilter.getSrcPin();
        } else {
            return mGrindAdvanceFilter.getSrcPin();
        }
    }

    public void setGLRender(GLRender glRender) {
        mGrindAdvanceFilter.setGLRender(glRender);
        if (mAdjustSkinColorFilter != null) {
            mAdjustSkinColorFilter.setGLRender(glRender);
        }
        if (mSpecialEffectsFilter != null) {
            mSpecialEffectsFilter.setGLRender(glRender);
        }
    }
}
