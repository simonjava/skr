package com.wali.live.watchsdk.component.presenter;

import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.base.log.MyLog;
import com.thornbirds.component.IEventController;
import com.thornbirds.component.presenter.ComponentPresenter;
import com.thornbirds.component.view.IOrientationListener;
import com.wali.live.component.view.panel.BaseBottomPanel;

import java.lang.ref.WeakReference;

/**
 * Created by yangli on 2017/03/13.
 *
 * @module 面板表现基类
 */
public abstract class BaseContainerPresenter<VIEW_GROUP extends ViewGroup>
        extends ComponentPresenter<VIEW_GROUP> implements IOrientationListener {

    protected boolean mIsLandscape = false;

    protected BaseBottomPanel<? extends View, VIEW_GROUP> mCurrPanel;

    @CheckResult
    protected final <T> T deRef(WeakReference<T> reference) {
        return reference != null ? reference.get() : null;
    }

    public BaseContainerPresenter(@NonNull IEventController controller) {
        super(controller);
    }

    protected final boolean hidePanel(boolean useAnimation) {
        if (mCurrPanel != null) {
            mCurrPanel.hideSelf(useAnimation);
            mCurrPanel = null;
            return true;
        } else {
            return false;
        }
    }

    protected final void showPanel(@Nullable BaseBottomPanel panel, boolean useAnimation) {
        if (mCurrPanel != null && mCurrPanel == panel) {
            return;
        }
        if (mCurrPanel != null && mCurrPanel != panel) {
            mCurrPanel.hideSelf(useAnimation);
        }
        mCurrPanel = panel;
        if (mCurrPanel != null) {
            mCurrPanel.showSelf(useAnimation, mIsLandscape);
        }
    }

    @Override
    @CallSuper
    public void onOrientation(boolean isLandscape) {
        if (mIsLandscape == isLandscape) {
            return;
        }
        MyLog.w(TAG, "onOrientation isLandscape=" + isLandscape);
        mIsLandscape = isLandscape;
        if (mCurrPanel != null) {
            mCurrPanel.onOrientation(mIsLandscape);
        }
    }

}
