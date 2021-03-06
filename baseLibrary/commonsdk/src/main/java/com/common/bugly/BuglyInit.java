package com.common.bugly;

import com.common.log.MyLog;
import com.common.utils.U;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.LinkedHashMap;
import java.util.Map;

public class BuglyInit {
    public static void init(boolean coreProess) {
        if (!coreProess) {
            return;
        }
        CrashReport.setIsDevelopmentDevice(U.app(), MyLog.isDebugLogOpen());
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(U.app());
        strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
            @Override
            public synchronized Map<String, String> onCrashHandleStart(int i, String s, String s1, String s2) {
//                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//　　　　　　String x5CrashInfo = Webview.getCrashExtraMessage(appContext);
//　　　　　　map.put("x5crashInfo", x5CrashInfo);
                return super.onCrashHandleStart(i, s, s1, s2);
            }

            @Override
            public synchronized byte[] onCrashHandleStart2GetExtraDatas(int i, String s, String s1, String s2) {
                return super.onCrashHandleStart2GetExtraDatas(i, s, s1, s2);
            }
        });
        strategy.setAppChannel(U.getChannelUtils().getChannel());  //设置渠道
//        strategy.setAppVersion(U.getAppInfoUtils().getVersionName());      //App的版本
//        strategy.setAppPackageName(U.getAppInfoUtils().getVersionName());  //App的包名
        strategy.setUploadProcess(coreProess);
        strategy.setEnableANRCrashMonitor(true);
        strategy.setEnableNativeCrashMonitor(true);
        CrashReport.initCrashReport(U.app(), "75917797f3", MyLog.isDebugLogOpen());
    }

    public static void setUserId(String uuid) {
        CrashReport.setUserId(uuid);
    }
}
