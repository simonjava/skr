package com.wali.live.componentwrapper;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.thornbirds.component.ComponentView;
import com.thornbirds.component.IEventObserver;

import java.util.List;

import static com.wali.live.componentwrapper.BaseSdkController.MSG_BACKGROUND_CLICK;
import static com.wali.live.componentwrapper.BaseSdkController.MSG_INPUT_VIEW_HIDDEN;
import static com.wali.live.componentwrapper.BaseSdkController.MSG_INPUT_VIEW_SHOWED;
import static com.wali.live.componentwrapper.BaseSdkController.MSG_ON_ORIENT_LANDSCAPE;
import static com.wali.live.componentwrapper.BaseSdkController.MSG_ON_ORIENT_PORTRAIT;

/**
 * Created by yangli on 2017/8/2.
 *
 * @author YangLi
 * @mail yanglijd@gmail.com
 */
public abstract class BaseSdkView<VIEW extends View, CONTROLLER extends BaseSdkController>
        extends ComponentView<VIEW, CONTROLLER> implements IEventObserver {

    protected Activity mActivity;

    protected final void addViewToSet(int[] idSet, List<View>... listSet) {
        if (idSet == null || listSet == null) {
            return;
        }
        for (int id : idSet) {
            View view = $(id);
            for (List<View> viewSet : listSet) {
                viewSet.add(view);
            }
        }
    }

    // add view to mContentView
    protected final <T extends View> void addViewAboveAnchor(
            @NonNull T view,
            @NonNull ViewGroup.LayoutParams params,
            View anchorView) {
        ViewGroup rootView = (ViewGroup) mContentView;
        int pos = anchorView != null ? rootView.indexOfChild(anchorView) : -1;
        if (pos >= 0) {
            rootView.addView(view, pos + 1, params);
        } else {
            rootView.addView(view, params);
        }
    }

    // add view to mContentView
    protected final <T extends View> void addViewUnderAnchor(
            @NonNull T view,
            @NonNull ViewGroup.LayoutParams params,
            View anchorView) {
        ViewGroup rootView = (ViewGroup) mContentView;
        int pos = anchorView != null ? rootView.indexOfChild(anchorView) : -1;
        if (pos >= 0) {
            rootView.addView(view, pos, params);
        } else {
            rootView.addView(view, 0, params);
        }
    }

    protected final void registerAction(int event) {
        mController.registerObserverForEvent(event, this);
    }

    protected final void unregisterAction(int event) {
        mController.unregisterObserverForEvent(event, this);
    }

    public BaseSdkView(
            @NonNull Activity activity,
            @NonNull ViewGroup parentView,
            @NonNull CONTROLLER controller) {
        super(parentView, controller);
        mActivity = activity;
    }

    @Override
    @CallSuper
    public void startView() {
        super.startView();
    }

    @Override
    @CallSuper
    public void stopView() {
        super.stopView();
        mController.unregisterObserver(this);
    }

    @Override
    @CallSuper
    public void release() {
        super.release();
    }
}
