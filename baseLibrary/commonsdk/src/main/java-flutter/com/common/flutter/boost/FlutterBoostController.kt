package com.common.flutter.boost

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.alibaba.android.arouter.launcher.ARouter
import com.common.flutter.plugin.CommonFlutterPlugin
import com.common.log.MyLog
import com.common.utils.U
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import com.idlefish.flutterboost.interfaces.INativeRouter
import io.flutter.embedding.android.FlutterView
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugins.GeneratedPluginRegistrant

object FlutterBoostController  {

    val TAG = "FlutterBoostController"

    private var inited = false

    var openContainerListener: ((context: Context?, url: String?, urlParams: MutableMap<String, Any>?, requestCode: Int, exts: MutableMap<String, Any>?) -> Unit)? = null

    /**
     *
     */
    fun openFlutterPage(context: Context, pageRouterName: String, params: MutableMap<String, Any>?, requestCode: Int = 0) {
        init()
        // 打开一个flutter page 页面
        val intent = BoostFlutterActivity.withNewEngine().url(pageRouterName)
                .params(params ?: mapOf<String, Any>())
                .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque)
                .build(context)
        if (context is Activity) {
            context.startActivityForResult(intent, requestCode)
        } else {
            context.startActivity(intent)
        }
    }

    fun openFlutterPage(context: Context, pageRouterName: String, params: MutableMap<String, Any>?) {
        openFlutterPage(context,pageRouterName,params,0)
    }

    private fun init() {
        if (inited) {
            return
        }
        val router = INativeRouter { context, url, urlParams, requestCode, exts ->
            MyLog.d("FlutterBoost", "openContainer context=$context url=$url urlParams=$urlParams requestCode=$requestCode exts=$exts")
            openContainerListener?.invoke(context, url, urlParams, requestCode, exts)
        }

        val pluginsRegister = FlutterBoost.BoostPluginsRegister { mRegistry ->
            MyLog.d("FlutterBoost", "registerPlugins mRegistry=$mRegistry")
            GeneratedPluginRegistrant.registerWith(mRegistry)
            //用户自定义的插件也在这里注册
            TextPlatformViewPlugin.register(mRegistry.registrarFor("TextPlatformViewPlugin"))
            CommonFlutterPlugin.registerWith(mRegistry.registrarFor("com.commonsdk.SkrFlutterPlugin"))
        }

        val platform = FlutterBoost.ConfigBuilder(U.app(), router)
                .isDebug(true)
                .whenEngineStart(FlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
                .renderMode(FlutterView.RenderMode.texture)
                .pluginsRegister(pluginsRegister)
                .whenEngineStart(FlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
                .lifecycleListener(object : FlutterBoost.BoostLifecycleListener{
                    override fun onEngineCreated() {
                        MyLog.d(TAG, "onEngineCreated")
                    }

                    override fun onPluginsRegistered() {
                        MyLog.d(TAG, "onPluginsRegistered")
                    }

                    override fun onEngineDestroy() {
                        MyLog.w(TAG,"onEngineDestroy")
                    }

                })
                .build()
        FlutterBoost.instance().init(platform)
    }
}