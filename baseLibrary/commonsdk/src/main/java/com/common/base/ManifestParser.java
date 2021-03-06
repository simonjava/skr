/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.common.base;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.common.base.ConfigModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ================================================
 * 用于解析 AndroidManifest 中的 Meta 属性
 * 配合 {@link ConfigModule} 使用
 * <p>
 * Created by JessYan on 12/04/2017 14:41
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public final class ManifestParser {
    public final String TAG = "ManifestParser";
    private static final String MODULE_VALUE = "ConfigModule";

    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    static class ConfigModuleWrapForSort {
        int index = 1000;
        ConfigModule configModule;

        public ConfigModuleWrapForSort(ConfigModule configModule) {
            this.configModule = configModule;
        }
    }

    public List<ConfigModule> parse() {
        List<ConfigModuleWrapForSort> modules = new ArrayList<ConfigModuleWrapForSort>();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                Log.d(TAG, "appInfo.metaData != null");
                for (String key : appInfo.metaData.keySet()) {
                    Object valueO =  appInfo.metaData.get(key);
                    Log.w(TAG, "key:" + key+" value:"+valueO);
                    if(valueO instanceof String){
                        String value = (String)valueO;
                        if (!TextUtils.isEmpty(value)) {
                            if (value.equals(MODULE_VALUE)
                                    || value.startsWith(MODULE_VALUE + "_")) {
                                ConfigModule configModule = parseModule(key);
                                ConfigModuleWrapForSort configModuleWrapForSort = new ConfigModuleWrapForSort(configModule);
                                String t[] = value.split("_");
                                if (t.length == 2) {
                                    configModuleWrapForSort.index = Integer.parseInt(t[1]);
                                }
                                modules.add(configModuleWrapForSort);
                            }
                        }
                    }
                }
            } else {
                Log.d(TAG, "appInfo.metaData == null");
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse ConfigModule", e);
        }

        /**
         * 排个序
         */
        Collections.sort(modules, new Comparator<ConfigModuleWrapForSort>() {
            @Override
            public int compare(ConfigModuleWrapForSort t1, ConfigModuleWrapForSort t2) {
                return t1.index - t2.index;
            }
        });
        List<ConfigModule> rl = new ArrayList<>();
        for (ConfigModuleWrapForSort wrapForSort : modules) {
            rl.add(wrapForSort.configModule);
        }
        return rl;
    }

    private static ConfigModule parseModule(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find ConfigModule implementation", e);
        }

        Object module;
        try {
            module = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instantiate ConfigModule implementation for " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate ConfigModule implementation for " + clazz, e);
        }

        if (!(module instanceof ConfigModule)) {
            throw new RuntimeException("Expected instanceof ConfigModule, but found: " + module);
        }
        return (ConfigModule) module;
    }
}