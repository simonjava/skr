package com.common.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

/**
 * 包裹懒加载view的一些常用方法
 */
public abstract class ExViewStub implements View.OnAttachStateChangeListener {
    protected ViewStub mViewStub;
    protected View mParentView;

    public ExViewStub(ViewStub viewStub) {
        mViewStub = viewStub;
    }

    public void tryInflate() {
        if (mParentView == null) {
            mParentView = mViewStub.inflate();
            mParentView.addOnAttachStateChangeListener(this);
            mParentView.setTranslationY(mViewStub.getTranslationY());
            onViewAttachedToWindow(mParentView);
            init(mParentView);
            mViewStub = null;
        }
    }

    protected abstract void init(View parentView);

    /**
     * 只要描述这个 ExViewStub 包裹的Layout到底是哪个
     * 方便查找维护，对代码逻辑不会有任何作用
     *
     * @return
     */
    protected abstract int layoutDesc();

    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            if (mParentView != null) {
                mParentView.setVisibility(View.GONE);
                mParentView.clearAnimation();
            }
        } else {
            tryInflate();
            mParentView.setVisibility(visibility);
        }
    }

    public int getVisibility() {
        if (mParentView != null) {
            return mParentView.getVisibility();
        } else {
            return View.GONE;
        }
    }

    public View getRealView() {
        if (mParentView == null) {
            return mViewStub;
        }
        return mParentView;
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {

    }

    public void setTranslateY(float ty) {
        if (mParentView == null) {
            mViewStub.setTranslationY(ty);
            return;
        }
        mParentView.setTranslationY(ty);
    }

    public boolean isShow() {
        if (mParentView != null && mParentView.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }
}