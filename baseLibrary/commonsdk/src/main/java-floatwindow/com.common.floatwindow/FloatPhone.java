package com.common.floatwindow;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.common.log.MyLog;
import com.common.utils.U;

/**
 * Created by yhao on 17-11-14.
 * https://github.com/yhaolpz
 */

class FloatPhone extends FloatView {
    public final static String TAG = "FloatPhone";
    private FloatWindow.B mB;
    private WindowManager mWindowManager;
    private final WindowManager.LayoutParams mLayoutParams;
    private boolean isRemove = true;

    FloatPhone(FloatWindow.B b) {
        mB = b;

        mWindowManager = (WindowManager) mB.mApplicationContext.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mLayoutParams.windowAnimations = 0;

        mLayoutParams.gravity = mB.gravity;
        mLayoutParams.x = mB.xOffset;
        mLayoutParams.y = mB.yOffset;
        mLayoutParams.width = mB.mWidth;
        mLayoutParams.height = mB.mHeight;
    }

    @Override
    public void init() {
        initInner(mB.reqPermissionIfNeed);
    }

    void initInner(boolean reqPermissionIfNeed) {
        boolean hasFloatWindowPermission = U.getPermissionUtils().checkFloatWindow(mB.mApplicationContext);
        MyLog.d(TAG, "init hasFloatWindowPermission=" + hasFloatWindowPermission + " reqPermissionIfNeed=" + reqPermissionIfNeed);
        if (hasFloatWindowPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            addView();
        } else {
            if (reqPermissionIfNeed) {
                //TODO 这里不好加悬浮窗权限是否获得成功的回调
                U.getPermissionUtils().requestFloatWindow(U.getActivityUtils().getTopActivity());
            } else {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                mLayoutParams.packageName = U.app().getPackageName();

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                    mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                    Context topActivityOrApp = U.getActivityUtils().getTopActivity();
                    if (!(topActivityOrApp instanceof Activity)) {
                        Log.e("ToastUtils", "Couldn't get top Activity.");
                        return;
                    }
                    Activity topActivity = (Activity) topActivityOrApp;
                    if (topActivity.isFinishing() || topActivity.isDestroyed()) {
                        Log.e("ToastUtils", topActivity + " is useless");
                        return;
                    }
                    mWindowManager = topActivity.getWindowManager();
                    mLayoutParams.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;
                    //U.getActivityUtils().addOnActivityDestroyedListener(topActivity, LISTENER);
                } else {
                    //mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    mLayoutParams.type = WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW + 37;
                }

                final Configuration config = U.app().getResources().getConfiguration();
                final int gravity = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                        ? Gravity.getAbsoluteGravity(mB.gravity, config.getLayoutDirection())
                        : mB.gravity;

                mLayoutParams.y = mB.yOffset;
                mLayoutParams.height = mB.mHeight;
                mLayoutParams.width = mB.mWidth;
                mLayoutParams.format = PixelFormat.TRANSLUCENT;
                mLayoutParams.windowAnimations = android.R.style.Animation_Toast;

                mLayoutParams.setTitle("ToastWithoutNotification");
//                mLayoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
                mLayoutParams.gravity = gravity;
                if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                    mLayoutParams.horizontalWeight = 1.0f;
                }
                if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                    mLayoutParams.verticalWeight = 1.0f;
                }
                mLayoutParams.x = mB.xOffset;
                mLayoutParams.packageName = U.app().getPackageName();

                addView();
            }
        }
    }

    public void addView() {
        try {
            if (isRemove) {
                MyLog.d(TAG, "addView type=" + mLayoutParams.type);
                mWindowManager.addView(mB.mView, mLayoutParams);
                isRemove = false;
            }
        }catch (Exception e){
            MyLog.e(TAG, e);
        }
    }

    @Override
    public void dismiss() {
        MyLog.d(TAG, "dismiss isRemove=" + isRemove);
        try {
            if (!isRemove) {
                isRemove = true;
                mWindowManager.removeView(mB.mView);
            }
        }catch (Exception e){
            MyLog.e(TAG, e);
        }

    }

    @Override
    public void updateXY(int x, int y) {
        MyLog.d(TAG,"updateXY" + " x=" + x + " y=" + y);
        if (isRemove) return;
        mB.xOffset = x;
        mB.yOffset = y;
        mLayoutParams.x = x;
        mLayoutParams.y = y;
        mWindowManager.updateViewLayout(mB.mView, mLayoutParams);
    }

    @Override
    void updateX(int x) {
        if (isRemove) return;
        mB.xOffset = x;
        mLayoutParams.x = x;
        mWindowManager.updateViewLayout(mB.mView, mLayoutParams);
    }

    @Override
    void updateY(int y) {
        if (isRemove) return;
        mB.yOffset = y;
        mLayoutParams.y = y;
        mWindowManager.updateViewLayout(mB.mView, mLayoutParams);
    }

    @Override
    int getX() {
        return mB.xOffset;
    }

    @Override
    int getY() {
        return mB.yOffset;
    }


}
