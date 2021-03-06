package com.didichuxing.doraemonkit;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;

import com.didichuxing.doraemonkit.kit.Category;
import com.didichuxing.doraemonkit.kit.IKit;
import com.didichuxing.doraemonkit.kit.alignruler.AlignRuler;
import com.didichuxing.doraemonkit.kit.blockmonitor.BlockMonitorKit;
import com.didichuxing.doraemonkit.kit.colorpick.ColorPicker;
import com.didichuxing.doraemonkit.kit.cpu.Cpu;
import com.didichuxing.doraemonkit.kit.crash.Crash;
import com.didichuxing.doraemonkit.kit.dataclean.DataClean;
import com.didichuxing.doraemonkit.kit.fileexplorer.FileExplorer;
import com.didichuxing.doraemonkit.kit.frameInfo.FrameInfo;
import com.didichuxing.doraemonkit.kit.gpsmock.GpsHookManager;
import com.didichuxing.doraemonkit.kit.gpsmock.GpsMock;
import com.didichuxing.doraemonkit.kit.logInfo.LogInfo;
import com.didichuxing.doraemonkit.kit.network.NetworkKit;
import com.didichuxing.doraemonkit.kit.ram.Ram;
import com.didichuxing.doraemonkit.kit.sysinfo.ExtraInfoProvider;
import com.didichuxing.doraemonkit.kit.sysinfo.SysInfo;
import com.didichuxing.doraemonkit.kit.temporaryclose.TemporaryClose;
import com.didichuxing.doraemonkit.kit.viewcheck.ViewChecker;
import com.didichuxing.doraemonkit.kit.webdoor.WebDoor;
import com.didichuxing.doraemonkit.kit.webdoor.WebDoorManager;
import com.didichuxing.doraemonkit.ui.FloatIconPage;
import com.didichuxing.doraemonkit.ui.UniversalActivity;
import com.didichuxing.doraemonkit.ui.base.FloatPageManager;
import com.didichuxing.doraemonkit.ui.base.PageIntent;
import com.didichuxing.doraemonkit.ui.kit.KitItem;
import com.didichuxing.doraemonkit.util.DoraemonPermissionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangweida on 2018/6/22.
 */

public class DoraemonKit {
    private static final String TAG = "DoraemonKit";

    private static SparseArray<List<IKit>> sKitMap = new SparseArray<>();

    private static boolean sHasRequestPermission;

    private static boolean sHasInit = false;

    public static void install(final Application app) {
        install(app, null);
    }

    public static void setWebDoorCallback(WebDoorManager.WebDoorCallback callback) {
        WebDoorManager.getInstance().setWebDoorCallback(callback);
        if (WebDoorManager.getInstance().isWebDoorEnable()) {
            List<IKit> tools = sKitMap.get(Category.TOOLS);
            if (tools != null) {
                tools.add(new WebDoor());
            }
        }
    }

    public static void install(final Application app, List<IKit> selfKits) {
        if (sHasInit) {
            if (selfKits != null) {
                List<IKit> biz = sKitMap.get(Category.BIZ);
                if (biz != null) {
                    biz.clear();
                    biz.addAll(selfKits);
                    for (IKit kit : biz) {
                        kit.onAppInit(app);
                    }
                }
            }
            return;
        }
        sHasInit = true;
        GpsHookManager.getInstance().init();
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            int startedActivityCounts;

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (startedActivityCounts == 0) {
                    FloatPageManager.getInstance().notifyForeground();
                }
                startedActivityCounts++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                FloatPageManager.getInstance().onActivityResumed(activity);
                tryShowFloatIcon(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                FloatPageManager.getInstance().onActivityPaused(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                startedActivityCounts--;
                if (startedActivityCounts == 0) {
                    FloatPageManager.getInstance().notifyBackground();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
        sKitMap.clear();
        List<IKit> tool = new ArrayList<>();
        List<IKit> biz = new ArrayList<>();
        List<IKit> ui = new ArrayList<>();
        List<IKit> performance = new ArrayList<>();
        List<IKit> exit = new ArrayList<>();

        tool.add(new SysInfo());
        tool.add(new FileExplorer());
        if (GpsHookManager.getInstance().isMockEnable()) {
            tool.add(new GpsMock());
        }
        if (WebDoorManager.getInstance().isWebDoorEnable()) {
            tool.add(new WebDoor());
        }
        tool.add(new Crash());
        tool.add(new LogInfo());
        tool.add(new DataClean());

        performance.add(new FrameInfo());
        performance.add(new Cpu());
        performance.add(new Ram());
        performance.add(new NetworkKit());
        performance.add(new BlockMonitorKit());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ui.add(new ColorPicker());
        }

        ui.add(new AlignRuler());
        ui.add(new ViewChecker());

        exit.add(new TemporaryClose());

        if (selfKits != null && !selfKits.isEmpty()) {
            biz.addAll(selfKits);
        }

        for (IKit kit : biz) {
            kit.onAppInit(app);
        }
        for (IKit kit : performance) {
            kit.onAppInit(app);
        }
        for (IKit kit : tool) {
            kit.onAppInit(app);
        }
        for (IKit kit : ui) {
            kit.onAppInit(app);
        }

        sKitMap.put(Category.BIZ, biz);
        sKitMap.put(Category.PERFORMANCE, performance);
        sKitMap.put(Category.TOOLS, tool);
        sKitMap.put(Category.UI, ui);
        sKitMap.put(Category.CLOSE, exit);

        FloatPageManager.getInstance().init(app);
    }

    private static void requestPermission(Context context) {
        if (!DoraemonPermissionUtil.canDrawOverlays(context) && !sHasRequestPermission) {
            Toast.makeText(context, context.getText(R.string.dk_float_permission_toast), Toast.LENGTH_LONG).show();
            DoraemonPermissionUtil.requestDrawOverlays(context);
            sHasRequestPermission = true;
        }
    }

    public static void tryShowFloatIcon(Context activity) {
        if (DoraemonPermissionUtil.canDrawOverlays(activity)) {
            showFloatIcon(activity);
        } else {
            requestPermission(activity);
        }
    }

    private static void showFloatIcon(Context context) {
        if (context instanceof UniversalActivity) {
            return;
        }
        PageIntent intent = new PageIntent(FloatIconPage.class);
        intent.mode = PageIntent.MODE_SINGLE_INSTANCE;
        FloatPageManager.getInstance().add(intent);
    }

    public static void hideFloatIcon() {
        FloatPageManager.getInstance().removeAll();
    }

    public static List<KitItem> getKitItems(int catgory) {
        if (sKitMap.get(catgory) != null) {
            List<KitItem> kitItems = new ArrayList<>();
            for (IKit kit : sKitMap.get(catgory)) {
                kitItems.add(new KitItem(kit));
            }
            return kitItems;
        } else {
            return null;
        }
    }

    private static ExtraInfoProvider sExtraInfoProvider;

    public static void setExtraInfoProvider(ExtraInfoProvider provider){
        sExtraInfoProvider = provider;
    }

    public static ExtraInfoProvider getExtraInfoProvider(){
        return sExtraInfoProvider;
    }
}
