/*
 * Copyright (C) 2005-2017 Qihoo 360 Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.qihoo360.loader2;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.qihoo360.i.Factory;
import com.qihoo360.i.IModule;
import com.qihoo360.i.IPlugin;
import com.qihoo360.mobilesafe.core.BuildConfig;
import com.qihoo360.mobilesafe.parser.manifest.ManifestParser;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.base.IPC;
import com.qihoo360.replugin.component.ComponentList;
import com.qihoo360.replugin.component.process.PluginProcessHost;
import com.qihoo360.replugin.component.receiver.PluginReceiverProxy;
import com.qihoo360.replugin.helper.LogDebug;
import com.qihoo360.replugin.helper.LogRelease;
import com.qihoo360.replugin.model.PluginInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.qihoo360.replugin.helper.LogDebug.LOADER_TAG;
import static com.qihoo360.replugin.helper.LogDebug.LOG;
import static com.qihoo360.replugin.helper.LogDebug.PLUGIN_TAG;
import static com.qihoo360.replugin.helper.LogRelease.LOGR;

/**
 * @author RePlugin Team
 */
class Loader {

    private final Context mContext;

    private final String mPluginName;

    final String mPath;

    final Plugin mPluginObj;

    PackageInfo mPackageInfo;

    Resources mPkgResources;

    Context mPkgContext;

    ClassLoader mClassLoader;

    /**
     * ?????????????????????Component??????
     */
    ComponentList mComponents;

    Method mCreateMethod;

    Method mCreateMethod2;

    IPlugin mPlugin;

    IPluginHost mPluginHost;

    ProxyPlugin mBinderPlugin;

    /**
     * layout??????????????????
     */
    HashSet<String> mIgnores = new HashSet<String>();

    /**
     * layout?????????????????????
     */
    HashMap<String, Constructor<?>> mConstructors = new HashMap<String, Constructor<?>>();

    static class ProxyPlugin implements IPlugin {

        com.qihoo360.loader2.IPlugin mPlugin;

        ProxyPlugin(IBinder plugin) {
            mPlugin = com.qihoo360.loader2.IPlugin.Stub.asInterface(plugin);
        }

        @Override
        public IModule query(Class<? extends IModule> c) {
            IBinder b = null;
            try {
                b = mPlugin.query(c.getName());
            } catch (Throwable e) {
                if (LOGR) {
                    LogRelease.e(PLUGIN_TAG, "query(" + c + ") exception: " + e.getMessage(), e);
                }
            }
            // TODO: return IModule
            return null;
        }
    }

    /**
     * ?????????Loader??????
     *
     * @param p Plugin????????????
     *          ?????????????????????plugin???????????????plugin.mInfo????????????????????????
     *          ??????plugin???????????????????????????mInfo???????????????????????????
     *          FIXME ????????????????????????????????????????????????
     */
    Loader(Context context, String name, String path, Plugin p) {
        mContext = context;
        mPluginName = name;
        mPath = path;
        mPluginObj = p;
    }

    final boolean isPackageInfoLoaded() {
        return mPackageInfo != null;
    }

    final boolean isResourcesLoaded() {
        return isPackageInfoLoaded() && mPkgResources != null;
    }

    final boolean isDexLoaded() {
        return isResourcesLoaded() && mClassLoader != null;
    }

    final boolean isAppLoaded() {
        return mPlugin != null;
    }

    final Context createBaseContext(Context newBase) {
        return new PluginContext(newBase, android.R.style.Theme, mClassLoader, mPkgResources, mPluginName, this);
    }

    final boolean loadDex(ClassLoader parent, int load) {
        /**
         * ?????? mPath = /data/user/0/com.mi.liveassistant2/app_p_a/229159006.jar
         */
        try {
            PackageManager pm = mContext.getPackageManager();

            mPackageInfo = Plugin.queryCachedPackageInfo(mPath);
            if (mPackageInfo == null) {
                // PackageInfo
                mPackageInfo = pm.getPackageArchiveInfo(mPath,
                        PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES | PackageManager.GET_PROVIDERS | PackageManager.GET_RECEIVERS | PackageManager.GET_META_DATA);
                if (mPackageInfo == null || mPackageInfo.applicationInfo == null) {
                    if (LOG) {
                        LogDebug.d(PLUGIN_TAG, "get package archive info null");
                    }
                    mPackageInfo = null;
                    return false;
                }
                if (LOG) {
                    LogDebug.d(PLUGIN_TAG, "get package archive info, pi=" + mPackageInfo);
                }
                mPackageInfo.applicationInfo.sourceDir = mPath;
                mPackageInfo.applicationInfo.publicSourceDir = mPath;

                if (TextUtils.isEmpty(mPackageInfo.applicationInfo.processName)) {
                    mPackageInfo.applicationInfo.processName = mPackageInfo.applicationInfo.packageName;
                }

                // ????????????SO????????????
                // ?????????????????????ApplicationLoaders.getClassLoader????????????PathClassLoader??????????????????
                // ??????findLibrary????????????????????????????????????SO?????????
                // Added by Jiongxuan Zhang
                PluginInfo pi = mPluginObj.mInfo;
                File ld = pi.getNativeLibsDir();
                /**
                 * ?????? so ???????????????
                 */
                mPackageInfo.applicationInfo.nativeLibraryDir = ld.getAbsolutePath();

//                // ???PluginInfo.getFrameworkVersion???FRAMEWORK_VERSION_UNKNOWN???p-n?????????????????????????????????????????????
//                if (pi.getFrameworkVersion() == PluginInfo.FRAMEWORK_VERSION_UNKNOWN) {
//                    pi.setFrameworkVersionByMeta(mPackageInfo.applicationInfo.metaData);
//                }

                // ?????????: pkgName -> pluginName
                synchronized (Plugin.PKG_NAME_2_PLUGIN_NAME) {
                    Plugin.PKG_NAME_2_PLUGIN_NAME.put(mPackageInfo.packageName, mPluginName);
                }

                // ?????????: pluginName -> fileName
                synchronized (Plugin.PLUGIN_NAME_2_FILENAME) {
                    Plugin.PLUGIN_NAME_2_FILENAME.put(mPluginName, mPath);
                }

                // ?????????: fileName -> PackageInfo
                synchronized (Plugin.FILENAME_2_PACKAGE_INFO) {
                    Plugin.FILENAME_2_PACKAGE_INFO.put(mPath, new WeakReference<PackageInfo>(mPackageInfo));
                }
            }

            // TODO preload?????????????????????????????????(???pluginInfo?????????MP.getPlugin(name, true)??????clone?????????)???????????????PluginInfo?????????????????????
            // TODO ??????preload??????????????????????????????????????????cache????????????????????????2.0???????????????????????????
            // TODO ????????????????????????????????????????????????????????????????????????????????????preload??????????????????
            // ???PluginInfo.getFrameworkVersion???FRAMEWORK_VERSION_UNKNOWN???p-n?????????????????????????????????????????????
            if (mPluginObj.mInfo.getFrameworkVersion() == PluginInfo.FRAMEWORK_VERSION_UNKNOWN) {
                mPluginObj.mInfo.setFrameworkVersionByMeta(mPackageInfo.applicationInfo.metaData);
                // ?????????P-n????????????????????????????????????????????????APK??????????????????
                // PluginInfoList.save();
            }

            // ???????????????ComponentList???
            // Added by Jiongxuan Zhang
            mComponents = Plugin.queryCachedComponentList(mPath);
            if (mComponents == null) {
                // ComponentList
                /**
                 * ????????????
                 */
                mComponents = new ComponentList(mPackageInfo, mPath, mPluginObj.mInfo);

                // ?????????????????????????????? receiver
                regReceivers();

                // ????????????ComponentList
                synchronized (Plugin.FILENAME_2_COMPONENT_LIST) {
                    Plugin.FILENAME_2_COMPONENT_LIST.put(mPath, new WeakReference<>(mComponents));
                }

                /* ??????????????? */
                // ????????????????????????????????????
                adjustPluginProcess(mPackageInfo.applicationInfo);

                // ??????????????? Activity ??? TaskAffinity
                adjustPluginTaskAffinity(mPluginName, mPackageInfo.applicationInfo);
            }

            if (load == Plugin.LOAD_INFO) {
                return isPackageInfoLoaded();
            }

            /**
             * resources ??????
             */
            mPkgResources = Plugin.queryCachedResources(mPath);
            // LOAD_RESOURCES???LOAD_ALL????????????????????????LOAD_INFO???????????????????????????PackageInfo???
            if (mPkgResources == null) {
                // Resources
                try {
                    if (BuildConfig.DEBUG) {
                        // ?????????Debug????????????????????????Instant Run?????????????????????New??????
                        Resources r = pm.getResourcesForApplication(mPackageInfo.applicationInfo);
                        mPkgResources = new Resources(r.getAssets(), r.getDisplayMetrics(), r.getConfiguration());
                    } else {
                        mPkgResources = pm.getResourcesForApplication(mPackageInfo.applicationInfo);
                    }
                } catch (NameNotFoundException e) {
                    if (LOG) {
                        LogDebug.d(PLUGIN_TAG, e.getMessage(), e);
                    }
                    return false;
                }
                if (mPkgResources == null) {
                    if (LOG) {
                        LogDebug.d(PLUGIN_TAG, "get resources null");
                    }
                    return false;
                }
                if (LOG) {
                    LogDebug.d(PLUGIN_TAG, "get resources for app, r=" + mPkgResources);
                }

                // ?????????: Resources
                synchronized (Plugin.FILENAME_2_RESOURCES) {
                    Plugin.FILENAME_2_RESOURCES.put(mPath, new WeakReference<>(mPkgResources));
                }
            }
            if (load == Plugin.LOAD_RESOURCES) {
                return isResourcesLoaded();
            }

            mClassLoader = Plugin.queryCachedClassLoader(mPath);
            if (mClassLoader == null) {
                // ClassLoader
                String out = mPluginObj.mInfo.getDexParentDir().getPath();
                //changeDexMode(out);

                //
                Log.i("dex", "load " + mPath + " ...");
                if (BuildConfig.DEBUG) {
                    // ??????Instant Run?????????parent???IncrementalClassLoader????????????DEBUG?????????
                    // ???????????????BootClassLoader??????
                    // Added by yangchao-xy & Jiongxuan Zhang
                    parent = ClassLoader.getSystemClassLoader();
                } else {
                    // ????????????????????????
                    parent = getClass().getClassLoader().getParent(); // TODO: ??????????????????????????????
                }
                String soDir = mPackageInfo.applicationInfo.nativeLibraryDir;

                long begin = 0;
                boolean isDexExist = false;

                if (LOG) {
                    begin = System.currentTimeMillis();
                    /**
                     * dexFile = /data/user/0/com.mi.liveassistant2/app_p_od/229159006.dex
                     */
                    File dexFile = mPluginObj.mInfo.getDexFile();
                    if (dexFile.exists() && dexFile.length() > 0) {
                        isDexExist = true;
                    }
                }

                /**
                 * ????????? classloader
                 *  mPath = /data/user/0/com.mi.liveassistant2/app_p_a/229159006.jar  //?????????????????? dexPath???????????? apk ?????????
                 *  out = /data/user/0/com.mi.liveassistant2/app_p_od
                 *  sodir = /data/user/0/com.mi.liveassistant2/app_p_n/229159006
                 *
                 */
                mClassLoader = RePlugin.getConfig().getCallbacks().createPluginClassLoader(mPluginObj.mInfo, mPath, out, soDir, parent);
                Log.i("dex", "load " + mPath + " = " + mClassLoader);

                if (mClassLoader == null) {
                    if (LOG) {
                        LogDebug.d(PLUGIN_TAG, "get dex null");
                    }
                    return false;
                }

                if (LOG) {
                    if (!isDexExist) {
                        Log.d(LOADER_TAG, " --??????DEX, " + "(plugin=" + mPluginName + ", version=" + mPluginObj.mInfo.getVersion() + ")"
                                + ", use:" + (System.currentTimeMillis() - begin)
                                + ", process:" + IPC.getCurrentProcessName());
                    } else {
                        Log.d(LOADER_TAG, " --????????????DEX, " + "(plugin=" + mPluginName + ", version=" + mPluginObj.mInfo.getVersion() + ")"
                                + ", use:" + (System.currentTimeMillis() - begin)
                                + ", process:" + IPC.getCurrentProcessName());
                    }
                }

                // ????????????ClassLoader
                synchronized (Plugin.FILENAME_2_DEX) {
                    Plugin.FILENAME_2_DEX.put(mPath, new WeakReference<>(mClassLoader));
                }
            }
            if (load == Plugin.LOAD_DEX) {
                return isDexLoaded();
            }

            /**
             * ?????????????????? ??????
             */
            mPkgContext = new PluginContext(mContext, android.R.style.Theme, mClassLoader, mPkgResources, mPluginName, this);
            if (LOG) {
                LogDebug.d(PLUGIN_TAG, "pkg context=" + mPkgContext);
            }

        } catch (Throwable e) {
            if (LOGR) {
                LogRelease.e(PLUGIN_TAG, "p=" + mPath + " m=" + e.getMessage(), e);
            }
            return false;
        }

        return true;
    }

    /**
     * ???????????????????????????????????? receiver ???????????????
     *
     * @throws android.os.RemoteException
     */
    private void regReceivers() throws android.os.RemoteException {
        String plugin = mPluginObj.mInfo.getName();

        if (mPluginHost == null) {
            mPluginHost = getPluginHost();
        }

        if (mPluginHost != null) {
            mPluginHost.regReceiver(plugin, ManifestParser.INS.getReceiverFilterMap(plugin));
        }
    }

    /**
     * ?????? IPluginHost Binder ??????
     */
    private IPluginHost getPluginHost() {
        IBinder binder = PluginProviderStub.proxyFetchHostBinder(mContext);
        if (binder == null) {
            if (LOG) {
                LogDebug.e(PluginReceiverProxy.TAG, "p.p fhb fail");
            }
            return null;
        } else {
            return IPluginHost.Stub.asInterface(binder);
        }
    }

    final boolean loadEntryMethod(boolean log) {
        //
        try {
            String className = Factory.PLUGIN_ENTRY_PACKAGE_PREFIX + "." + mPluginName + "." + Factory.PLUGIN_ENTRY_CLASS_NAME;
            Class<?> c = mClassLoader.loadClass(className);
            if (LOG) {
                LogDebug.d(PLUGIN_TAG, "found entry: className=" + className + ", loader=" + c.getClassLoader());
            }
            mCreateMethod = c.getDeclaredMethod(Factory.PLUGIN_ENTRY_EXPORT_METHOD_NAME, Factory.PLUGIN_ENTRY_EXPORT_METHOD_PARAMS);
        } catch (Throwable e) {
            if (log) {
                if (LOGR) {
                    LogRelease.e(PLUGIN_TAG, e.getMessage(), e);
                }
            } else {
                if (LOG) {
                    LogDebug.d(PLUGIN_TAG, "loadEntryMethod exception");
                }
            }
        }
        return mCreateMethod != null;
    }

    final boolean invoke(PluginCommImpl manager) {
        try {
            mPlugin = (IPlugin) mCreateMethod.invoke(null, mPkgContext, manager);
            if (LOG) {
                LogDebug.d(PLUGIN_TAG, "Loader.invoke(): plugin=" + mPath + ", cl=" + (mPlugin != null ? mPlugin.getClass().getClassLoader() : "null"));
            }
        } catch (Throwable e) {
            if (LOGR) {
                LogRelease.e(PLUGIN_TAG, e.getMessage(), e);
            }
            return false;
        }
        return true;
    }

    final boolean loadEntryMethod2() {
        //
        try {
            String className = Factory.PLUGIN_ENTRY_PACKAGE_PREFIX + "." + mPluginName + "." + Factory.PLUGIN_ENTRY_CLASS_NAME;
            Class<?> c = mClassLoader.loadClass(className);
            if (LOG) {
                LogDebug.d(PLUGIN_TAG, "found entry: className=" + className + ", loader=" + c.getClassLoader());
            }
            mCreateMethod2 = c.getDeclaredMethod(Factory.PLUGIN_ENTRY_EXPORT_METHOD_NAME, Factory.PLUGIN_ENTRY_EXPORT_METHOD2_PARAMS);
        } catch (Throwable e) {
            // ????????????????????????????????????????????????????????????????????????load?????????????????????log
//            if (LOGR) {
//                LogRelease.e(PLUGIN_TAG, e.getMessage(), e);
//            }
        }
        return mCreateMethod2 != null;
    }

    final boolean loadEntryMethod3() {
        //
        try {
            String className = Factory.REPLUGIN_LIBRARY_ENTRY_PACKAGE_PREFIX + "." + Factory.PLUGIN_ENTRY_CLASS_NAME;
            Class<?> c = mClassLoader.loadClass(className);
            if (LOG) {
                LogDebug.d(PLUGIN_TAG, "found entry: className=" + className + ", loader=" + c.getClassLoader());
            }
            mCreateMethod2 = c.getDeclaredMethod(Factory.PLUGIN_ENTRY_EXPORT_METHOD_NAME, Factory.PLUGIN_ENTRY_EXPORT_METHOD2_PARAMS);
        } catch (Throwable e) {
            if (LOGR) {
                LogRelease.e(PLUGIN_TAG, e.getMessage(), e);
            }
        }
        return mCreateMethod2 != null;
    }

    final boolean invoke2(PluginCommImpl x) {
        try {
            IBinder manager = null; // TODO
            IBinder b = (IBinder) mCreateMethod2.invoke(null, mPkgContext, getClass().getClassLoader(), manager);
            if (b == null) {
                if (LOGR) {
                    LogRelease.e(PLUGIN_TAG, "p.e.r.b n");
                }
                return false;
            }
            mBinderPlugin = new ProxyPlugin(b);
            mPlugin = mBinderPlugin;
            if (LOG) {
                LogDebug.d(PLUGIN_TAG, "Loader.invoke2(): plugin=" + mPath + ", plugin.binder.cl=" + b.getClass().getClassLoader());
            }
        } catch (Throwable e) {
            if (LOGR) {
                LogRelease.e(PLUGIN_TAG, e.getMessage(), e);
            }
            return false;
        }
        return true;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @return
     */
    private List<String> getHostProcessList() {
        List<String> pluginProcessList = new ArrayList<>();
        for (int i = 0; i < PluginProcessHost.PROCESS_COUNT; i++) {
            pluginProcessList.add(IPC.getPackageName() + PluginProcessHost.PROCESS_PLUGIN_SUFFIX2 + i);
        }
        return pluginProcessList;
    }

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    private List<String> getPluginProcessList() {
        Set<String> processSet = new HashSet<>();

        String pluginUIProcess = mComponents.getApplication().packageName;

        getPluginProcess(processSet, mComponents.getProviders());
        getPluginProcess(processSet, mComponents.getActivities());
        getPluginProcess(processSet, mComponents.getServices());
        getPluginProcess(processSet, mComponents.getReceivers());

        processSet.remove(pluginUIProcess);

        return Arrays.asList(processSet.toArray(new String[0]));
    }

    /**
     * ??????????????????????????????
     *
     * @param processSet
     * @param componentInfos
     */
    private void getPluginProcess(Set<String> processSet, ComponentInfo[] componentInfos) {
        if (componentInfos != null) {
            for (ComponentInfo componentInfo : componentInfos) {
                processSet.add(componentInfo.processName);
            }
        }
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @return
     */
    private HashMap<String, String> genDynamicProcessMap() {
        HashMap<String, String> processMap = new HashMap<>();

        List<String> hostProcessList = getHostProcessList();
        List<String> pluginProcessList = getPluginProcessList();

        int hostProcessCount = hostProcessList != null ? hostProcessList.size() : 0;

        if (hostProcessCount <= 0) {
            return processMap;
        }

        int pluginProcessCount = pluginProcessList != null ? pluginProcessList.size() : 0;

        for (int i = 0; i < pluginProcessCount; i++) {
            int hostProcessIndex = i % hostProcessCount;
            processMap.put(pluginProcessList.get(i), hostProcessList.get(hostProcessIndex));
        }

        return processMap;
    }

    /**
     * ????????????AndroidMainfest????????????????????????????????????meta-data???"process_map"
     *
     * @param appInfo
     * @return
     */
    private HashMap<String, String> getConfigProcessMap(ApplicationInfo appInfo) {
        HashMap<String, String> processMap = new HashMap<>();
        Bundle bdl = appInfo.metaData;
        if (bdl == null || TextUtils.isEmpty(bdl.getString("process_map"))) {
            return processMap;
        }
        try {
            String processMapStr = bdl.getString("process_map");
            JSONArray ja = new JSONArray(processMapStr);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                if (jo != null) {
                    String to = jo.getString("to").toLowerCase();
                    if (to.equals("$ui")) {
                        to = IPC.getPackageName();
                    } else {
                        // ??? UI ???????????????????????????????????????
                        if (to.contains("$" + PluginProcessHost.PROCESS_PLUGIN_SUFFIX)) {
                            to = PluginProcessHost.PROCESS_ADJUST_MAP.get(to);
                        }
                    }
                    processMap.put(jo.getString("from"), to);
                }
            }
        } catch (JSONException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return processMap;
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * ??????
     * ???????????????????????????????????? ???meta-data???process_map??? ?????????????????????????????????????????????????????????????????????
     *
     * @param appInfo
     */
    private void adjustPluginProcess(ApplicationInfo appInfo) {
        HashMap<String, String> processMap = getConfigProcessMap(appInfo);
        if (processMap == null || processMap.isEmpty()) {

            PluginInfo pi = MP.getPlugin(mPluginName, false);
            if (pi != null && pi.getFrameworkVersion() >= 4) {
                processMap = genDynamicProcessMap();
            }
        }

        if (LOG) {
            Log.d(PLUGIN_TAG, "--- ?????????????????????????????? BEGIN ---");
            for (Map.Entry<String, String> entry : processMap.entrySet()) {
                Log.d(PLUGIN_TAG, entry.getKey() + " -> " + entry.getValue());
            }
        }

        doAdjust(processMap, mComponents.getActivityMap());
        doAdjust(processMap, mComponents.getServiceMap());
        doAdjust(processMap, mComponents.getReceiverMap());
        doAdjust(processMap, mComponents.getProviderMap());

        if (LOG) {
            Log.d(PLUGIN_TAG, "--- ?????????????????????????????? END --- " + IPC.getCurrentProcessName());
        }
    }

    private void doAdjust(HashMap<String, String> processMap, HashMap<String, ? extends ComponentInfo> infos) {

        if (processMap == null || processMap.isEmpty()) {
            return;
        }

        for (HashMap.Entry<String, ? extends ComponentInfo> entry : infos.entrySet()) {
            ComponentInfo info = entry.getValue();
            if (info != null) {
                String targetProcess = processMap.get(info.processName);

                if (!TextUtils.isEmpty(targetProcess)) {
                    if (LOG) {
                        Log.d(TaskAffinityStates.TAG, String.format("--- ???????????? %s, %s -> %s", info.name, info.processName, targetProcess));
                    }

                    info.processName = targetProcess;
                }
            }
        }
    }

    /**
     * ??????????????? Activity ????????? TaskAffinity
     *
     * @param plugin ????????????
     */
    private void adjustPluginTaskAffinity(String plugin, ApplicationInfo appInfo) {
        if (appInfo == null) {
            return;
        }

        Bundle bdl = appInfo.metaData;
        if (bdl != null) {
            boolean useDefault = bdl.getBoolean("use_default_task_affinity", true);
            if (LOG) {
                LogDebug.d(TaskAffinityStates.TAG, "useDefault = " + useDefault);
            }

            if (!useDefault) {
                if (LOG) {
                    LogDebug.d(TaskAffinityStates.TAG, String.format("???????????? %s ???????????? TaskAffinity", plugin));
                }

                String defaultPluginTaskAffinity = appInfo.packageName;
                for (HashMap.Entry<String, ActivityInfo> entry : mComponents.getActivityMap().entrySet()) {
                    ActivityInfo info = entry.getValue();
                    if (LOG) {
                        if (info != null) {
                            LogDebug.d(TaskAffinityStates.TAG, String.format("%s.taskAffinity = %s ", info.name, info.taskAffinity));
                        }
                    }

                    // ??????????????? TaskAffinity
                    if (info != null && info.taskAffinity.equals(defaultPluginTaskAffinity)) {
                        info.taskAffinity = info.taskAffinity + "." + plugin;
                        if (LOG) {
                            LogDebug.d(TaskAffinityStates.TAG, String.format("?????? %s ??? TaskAffinity ??? %s", info.name, info.taskAffinity));
                        }
                    }
                }
            }
        }
    }
}
