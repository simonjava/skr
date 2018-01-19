package com.base.mvp.specific;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.base.mvp.IRxView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by lan on 17/4/13.
 */
public abstract class RxRelativeLayout extends RelativeLayout implements IRxView {
    protected final String TAG = getTAG();

    protected BehaviorSubject<PresenterEvent> mBehaviorSubject = BehaviorSubject.create();

    protected String getTAG() {
        return getClass().getSimpleName();
    }

    public RxRelativeLayout(Context context) {
        super(context);
    }

    public RxRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @CallSuper
    public void destroy() {
        mBehaviorSubject.onNext(PresenterEvent.DESTROY);
    }

    @Override
    public <T> Observable.Transformer<T, T> bindUntilEvent() {
        return PresenterEvent.bindUntilEvent(mBehaviorSubject, PresenterEvent.DESTROY);
    }

    protected final <V extends View> V $(@IdRes int resId) {
        return (V) findViewById(resId);
    }

    protected final <V extends View> V $click(@IdRes int id, OnClickListener listener) {
        V v = $(id);
        if (v != null) {
            v.setOnClickListener(listener);
        }
        return v;
    }

    protected final void $click(View v, OnClickListener listener) {
        if (v != null) {
            v.setOnClickListener(listener);
        }
    }

    protected final <V extends View> V $rxClick(@IdRes int id, long milliSeconds, Action1 action) {
        V v = $(id);
        if (v != null) {
            RxView.clicks(v)
                    .throttleFirst(milliSeconds, TimeUnit.MILLISECONDS)
                    .subscribe(action);
        }
        return v;
    }
}
