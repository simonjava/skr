package com.just.agentweb;

import android.view.KeyEvent;

import com.tencent.smtt.sdk.WebView;


/**
 * <b>@项目名：</b> agentwebX5<br>
 * <b>@包名：</b><br>
 * <b>@创建者：</b> cxz --  just<br>
 * <b>@创建时间：</b> &{DATE}<br>
 * <b>@公司：</b> <br>
 * <b>@邮箱：</b> cenxiaozhong.qqcom@qq.com<br>
 * <b>@描述</b><br>
 *
 *     source CODE  https://github.com/Justson/AgentWebX5
 */

public class EventHandlerImpl implements IEventHandler {
    private WebView mWebView;
    private EventInterceptor mEventInterceptor;

    public static final EventHandlerImpl getInstantce(WebView view,EventInterceptor eventInterceptor) {
        return new EventHandlerImpl(view, eventInterceptor);
    }

    public EventHandlerImpl(WebView webView,EventInterceptor eventInterceptor) {
        LogUtils.i("Info","EventInterceptor:"+eventInterceptor);
        this.mWebView = webView;
        this.mEventInterceptor = eventInterceptor;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return back();
        }
        return false;
    }

    @Override
    public boolean back() {
        if(this.mEventInterceptor!=null&&this.mEventInterceptor.event()){
            return true;
        }
        if(mWebView!=null&&mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return false;
    }

    public void inJectPriorityEvent(){

    }
}
