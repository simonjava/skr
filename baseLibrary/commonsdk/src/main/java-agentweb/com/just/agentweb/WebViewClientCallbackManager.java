package com.just.agentweb;

import android.graphics.Bitmap;

import com.tencent.smtt.sdk.WebView;


/**
 * Created by cenxiaozhong on 2017/5/28.
 * source code https://github.com/Justson/AgentWebX5
 */

public class WebViewClientCallbackManager {


    private PageLifeCycleCallback mPageLifeCycleCallback;

    public PageLifeCycleCallback getPageLifeCycleCallback() {
        return mPageLifeCycleCallback;
    }

    public void setPageLifeCycleCallback(PageLifeCycleCallback pageLifeCycleCallback) {
        mPageLifeCycleCallback = pageLifeCycleCallback;
    }

    public interface PageLifeCycleCallback {

        void onPageStarted(WebView view, String url, Bitmap favicon);
        void onPageFinished(WebView view, String url);

    }
}
