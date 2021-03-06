package com.common.core.login.interceptor;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.common.core.permission.SkrAudioPermission;
import com.module.RouterConstants;
import com.common.core.account.UserAccountManager;
import com.common.core.login.LoginActivity;
import com.common.core.login.execption.UnloginException;
import com.common.log.MyLog;

@Interceptor(priority = 1)
public class SkrArouterInterceptor implements IInterceptor {
    public final String TAG = "SkrArouterInterceptor";

    /**
     * 我们经常需要在目标页面中配置一些属性，比方说"是否需要登陆"之类的
     * 可以通过 Route 注解中的 extras 属性进行扩展，这个属性是一个 int值，换句话说，单个int有4字节，也就是32位，可以配置32个开关
     * 剩下的可以自行发挥，通过字节操作可以标识32个开关，通过开关标记目标页面的一些属性，在拦截器中可以拿到这个标记进行业务逻辑判断
     *
     * @Route(path = "/test/activity", extras = Consts.XXXX)
     */
    public static final int NO_NEED_LOGIN = 0x0001;
//    SkrAudioPermission skrAudioPermission = new SkrAudioPermission();

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        MyLog.d(TAG, "process" + " postcard=" + postcard + " path:" + postcard.getPath() + " callback=" + callback);
        if (RouterConstants.ACTIVITY_LOGIN.equals(postcard.getPath())) {
            callback.onContinue(postcard);
            return;
        }

        if (UserAccountManager.INSTANCE.hasAccount()) {
//            if(postcard.getPath().equals(RouterConstants.ACTIVITY_AUDITION_ROOM)){
//                skrAudioPermission.ensurePermission({
//                        callback.onContinue(postcard);
//                },true);
//            }else{
                callback.onContinue(postcard);
//            }
        } else {
            // 不需要登陆
            boolean noNeedLogin = (postcard.getExtra() & NO_NEED_LOGIN) > 0;

            if (noNeedLogin) {
                callback.onContinue(postcard);
            } else {
                callback.onInterrupt(new UnloginException());

                String path = postcard.getPath();
                Bundle bundle = postcard.getExtras();

                // 登录成功后，应该继续处理这个 callback 才最好啊
                ARouter.getInstance().build(RouterConstants.ACTIVITY_LOGIN)
                        .with(bundle)
                        .withString(LoginActivity.KEY_ORIGIN_PATH, path)
                        .withBoolean(LoginActivity.KEY_SHOW_TOAST, true)
                        .greenChannel().navigation();
            }
        }
    }

    @Override
    public void init(Context context) {

    }
}
