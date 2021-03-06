package com.common.loadsir.callback;

import android.content.Context;
import android.view.View;

import com.common.base.R;
import com.kingja.loadsir.callback.Callback;


/**
 * Description:TODO
 * Create Time:2017/9/4 10:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

public class LoadingCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }

    @Override
    public boolean getSuccessVisible() {
        return super.getSuccessVisible();
    }

    /**
     * 会吃掉 ReloadEvent的点击事件
     * @param context
     * @param view
     * @return
     */
    @Override
    protected boolean onReloadEvent(Context context, View view) {
        return true;
    }
}
