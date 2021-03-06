package com.just.agentweb;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by cenxiaozhong on 2017/5/12.
 * source CODE  https://github.com/Justson/AgentWebX5
 */

public abstract class BaseIndicatorView extends FrameLayout implements BaseProgressSpec ,LayoutParamsOffer{
    public BaseIndicatorView(Context context) {
        super(context);
    }

    public BaseIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void reset() {

    }

    @Override
    public void setProgress(int newProgress) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }



    @Override
    public LayoutParams offerLayoutParams() {
        return null;
    }
}
