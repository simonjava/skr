/*
 * Copyright 2018 JessYan
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
package com.common.core;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.common.base.ConfigModule;
import com.common.base.GlobalParams;
import com.common.base.InitManager;
import com.common.base.delegate.AppLifecycles;
import com.common.core.account.UserAccountManager;
import com.common.core.crash.SkrCrashHandler;
import com.common.core.scheme.SchemeUtils;
import com.common.core.scheme.processor.SkrSchemeProcessor;
import com.common.flutter.boost.FlutterBoostController;
import com.common.notification.NotificationMsgProcess;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.interceptor.CoreInfoInterceptor;
import com.common.utils.U;
import com.doraemon.DoraemonManager;
import com.module.ModuleServiceManager;
import com.module.msg.IMsgService;

import java.util.List;


/**
 * ================================================
 * CommonSDK 的 GlobalConfiguration 含有有每个组件都可公用的配置信息, 每个组件的 AndroidManifest 都应该声明此 ConfigModule
 *
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki#3.3">ConfigModule wiki 官方文档</a>
 * Created by JessYan on 30/03/2018 17:16
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class CoreConfiguration implements ConfigModule {
    public final String TAG = "CoreConfiguration";

    @Override
    public void applyOptions(GlobalParams.Builder builder) {

    }

    @Override
    public void injectAppLifecycle(List<AppLifecycles> lifecycles) {
        // AppDelegate.Lifecycle 的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        lifecycles.add(new AppLifecycles() {

            @Override
            public void attachBaseContext(@NonNull Context base) {

            }

            @Override
            public void onMainProcessCreate(@NonNull Application application) {
                Log.d(TAG, "application onCreate");
                // todo 服务器暂时无人对接，先屏蔽
                ApiManager.getInstance().addInterceptor(new CoreInfoInterceptor());
                IMsgService msgService = ModuleServiceManager.getInstance().getMsgService();
                if (msgService != null) {
                    msgService.initRongIM(U.app());
                    msgService.addMsgProcessor(new NotificationMsgProcess());
                }
                UserAccountManager.INSTANCE.init();
                DoraemonManager.init();
                InitManager.initMainThread(() -> SkrCrashHandler.getInstance().register(), 20000);
                FlutterBoostController.INSTANCE.setOpenContainerListener((context, url, params, requestCode, extra) -> {
                    String newUrl = SchemeUtils.join(url, params, requestCode);
                    SkrSchemeProcessor.INSTANCE.process(Uri.parse(newUrl), context, false);
                    return null;
                });
            }

            @Override
            public void onOtherProcessCreate(@NonNull Application application) {

            }

            @Override
            public void onTerminate(@NonNull Application application) {

            }
        });
    }

    @Override
    public void injectActivityLifecycle(List<Application.ActivityLifecycleCallbacks> lifecycles) {
    }

    @Override
    public void injectFragmentLifecycle(List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
    }
}
