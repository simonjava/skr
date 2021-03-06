package com.common.view;

import android.view.View;

import com.common.log.MyLog;
import com.common.utils.CommonUtils;
import com.common.utils.U;

public abstract class DebounceViewClickListener implements View.OnClickListener {

    public final String TAGDEB = "DebounceViewClickListener";

    int debounceTime = CommonUtils.FAST_DOUBLE_CLICK_INTERVAL;

    public DebounceViewClickListener() {
    }

    public DebounceViewClickListener(int debounceTime) {
        this.debounceTime = debounceTime;
    }

    @Override
    public void onClick(View v) {
        /**
         * 防止快速点击 或者 同时按多个按钮的问题
         */
        if (U.getCommonUtils().isFastDoubleClick(debounceTime)) {
            MyLog.d(TAGDEB, "点击太快了，取消本次 v=" + v);
            return;
        }
        clickValid(v);
    }

    public abstract void clickValid(View v);
}
