package com.common.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.common.base.BaseActivity;
import com.common.log.MyLog;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;


/**
 * 这个类我一定好好改改，flag
 * <p>
 * 完美解决键盘 虚拟按键 沉浸式 等一系列涉及到布局的问题
 */
public class AndroidBug5497WorkaroundSupportingTranslucentStatus {
    public final static String TAG = "AndroidBug5497WorkaroundSupportingTranslucentStatus";
    private WeakReference<BaseActivity> mBaseActivityRef;
    private String mFrom = "";
    // 我们页面的根布局以及layoutparams
    private View mChildOfContent;
    private FrameLayout.LayoutParams frameLayoutParams;

    // 我们视图view先前的高度
    private int usableHeightPrevious;


    ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            /**
             * 这里要特别注意，如果又两个Activity A B，B在A之上，在B中弹出键盘，A中的  onGlobalLayout 也会发生回调
             */
            // 根布局需要重新计算layout时就会触发
            possiblyResizeChildOfContent();
        }
    };

    public static AndroidBug5497WorkaroundSupportingTranslucentStatus assistActivity(BaseActivity activity) {
        return new AndroidBug5497WorkaroundSupportingTranslucentStatus(activity);
    }

    private AndroidBug5497WorkaroundSupportingTranslucentStatus(BaseActivity activity) {
        mBaseActivityRef = new WeakReference<>(activity);
        mFrom = activity.getClass().getName();
        /**
         * 这里优化下
         * 1. 为了支持fragment，支持自己传入根view
         * 2. 希望只有view在前台可见时响应这个事件
         * 3. 兼容activity一开始没setContentView，后面又addFragment这种情况
         */
        // 拿到页面根布局
        FrameLayout content = activity.findViewById(android.R.id.content);
        // 拿到activity页面的 rootView
        mChildOfContent = content.getChildAt(0);
        /**
         * 所以这里就有一个bug
         */
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    /**
     * 以下数据，以米8手机，虚拟按键开启为例子，让情况尽可能复杂
     */
    private void possiblyResizeChildOfContent() {
        MyLog.d(TAG, "possiblyResizeChildOfContent mFrom:" + mFrom);
        Activity curActivity = U.getActivityUtils().getCurrentActivity();
        if (curActivity != mBaseActivityRef.get()) {
            MyLog.d(TAG, "not curActivity ,return");
            /**
             * 如果不是当前acitivity发出的就忽略吧。
             */
            return;
        }

        /**
         * 米8手机为例，弹出键盘时
         * r.left=0
         * r.right=1080
         * r.top=110
         * r.bottom=1280
         *
         * 未弹出键盘时
         *  r.top=110
         *  r.bottom=2118
         */
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);

        int usableHeightNow = r.bottom;
        MyLog.d(TAG, "possiblyResizeChildOfContent r.top:" + r.top + " r.bottom:" + r.bottom
                + " usableHeightNow:" + usableHeightNow
                + " screenHeight:" + U.getDisplayUtils().getScreenHeight()
                + " phoneHeight:" + U.getDisplayUtils().getPhoneHeight()
        );
        if (usableHeightNow != usableHeightPrevious) {
            /**
             * usableHeightSansKeyboard 这里为 2248 手机高度。
             * getSoftButtonsBarHeight = 130。 这里会算上虚拟按键的高度
             */
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            if (U.getKeyBoardUtils().hasNavigationBar()) {
                usableHeightSansKeyboard -= U.getKeyBoardUtils().getSoftButtonsBarHeight();
            }
            /**
             * heightDifference = 2188-1280 = 908 可以认为是键盘高度
             * heightDifference 并不一定就是软键盘的高度，在有虚拟按键的手机上，还加上了虚拟按键的高度
             * 想拿键盘高度，参考{@link KeyBoardUtils}
             *
             * 已经减去了虚拟按键的高度，已经就是键盘高度了
             */
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            /**
             * 是否自己控制布局，当有键盘事件时
             */
            boolean resizeSelf = mBaseActivityRef.get().resizeLayoutSelfWhenKeybordShow();
            MyLog.d(TAG, "possiblyResizeChildOfContent usableHeightSansKeyboard=" + usableHeightSansKeyboard
                    + " usableHeightNow=" + usableHeightNow
                    + " heightDifference=" + heightDifference
                    + " resizeSelf=" + resizeSelf);

            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                MyLog.d(TAG, "键盘变为可见");
                /**
                 * 键盘变为可见,这里有几种操作
                 * 1.改动根布局高度
                 * 2.不改变根布局高度，发event告知改 Activiy 键盘出现，让业务自己处理，
                 *  比如修改 PlaceHolderView 的高度
                 */
                if (resizeSelf) {
                    KeyboardEvent keyboardEvent = new KeyboardEvent(mFrom, KeyboardEvent.EVENT_TYPE_KEYBOARD_VISIBLE, heightDifference);
                    EventBus.getDefault().post(keyboardEvent);
                } else {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                }
            } else {
                MyLog.d(TAG, "键盘变为不可见");
                // 键盘变为不可见
                if (resizeSelf) {
                    KeyboardEvent keyboardEvent = new KeyboardEvent(mFrom, KeyboardEvent.EVENT_TYPE_KEYBOARD_HIDDEN, 0);
                    EventBus.getDefault().post(keyboardEvent);
                }
                frameLayoutParams.height = usableHeightSansKeyboard;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    public void destroy() {
        mChildOfContent.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }
}