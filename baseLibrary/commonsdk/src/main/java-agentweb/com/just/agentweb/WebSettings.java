package com.just.agentweb;


import com.tencent.smtt.sdk.WebView;


public interface WebSettings <T extends com.tencent.smtt.sdk.WebSettings>{

    WebSettings toSetting(WebView webView);


    T getWebSettings();






}
