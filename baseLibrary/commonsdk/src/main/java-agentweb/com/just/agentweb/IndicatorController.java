package com.just.agentweb;


import com.tencent.smtt.sdk.WebView;

/**
 * <b>@项目名：</b> agentwebX5<br>
 * <b>@包名：</b>com.just.library<br>
 * <b>@创建者：</b> cxz --  just<br>
 * <b>@创建时间：</b> &{DATE}<br>
 * <b>@公司：</b> <br>
 * <b>@邮箱：</b> cenxiaozhong.qqcom@qq.com<br>
 * <b>@描述</b>source CODE  https://github.com/Justson/AgentWebX5<br>
 */

public interface IndicatorController {

    void progress(WebView v, int newProgress);

    BaseProgressSpec offerIndicator();
}
