package com.common.core.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.common.base.BaseActivity;
import com.common.core.R;
import com.common.core.login.fragment.LoginFragment;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.userinfo.noremind.NoRemindManager;
import com.common.log.MyLog;
import com.common.rxretrofit.ApiResult;
import com.common.utils.FragmentUtils;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.core.policy.PrivacyPolicyDialog;
import com.dialog.view.TipsDialogView;
import com.module.RouterConstants;
import com.module.home.IHomeService;
import com.umeng.socialize.UMShareAPI;

@Route(path = RouterConstants.ACTIVITY_LOGIN)
public class LoginActivity extends BaseActivity {

    public static final String KEY_SHOW_TOAST = "key_show_toast";
    public static final String KEY_ORIGIN_PATH = "key_origin_path";
    public static final String KEY_REASON = "key_reason"; // 退出的原因
    public static final int REASON_NORMAL = 0; // 因为没有账号到这个页面
    public static final int REASON_LOGOFF = 1; // 因为退出登录所以要到登录页
    int mReason = REASON_NORMAL;
    Handler mUiHandler = new Handler(Looper.getMainLooper());
    TipsDialogView mTipsDialogView;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.core_login_activity_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra(KEY_SHOW_TOAST, false)) {
            U.getToastUtil().showShort("请先登录");
        }
        if (intent != null) {
            mReason = intent.getIntExtra(KEY_REASON, 0);
        }
        MyLog.d(getTAG(), "initData" + " mReason=" + mReason);
        U.getFragmentUtils().addFragment(FragmentUtils
                .newAddParamsBuilder(LoginActivity.this, LoginFragment.class)
                .setAddToBackStack(false)
                .setHasAnimation(false)
                .build());
        if (mReason == REASON_LOGOFF) {
            // 因为是因为退出登录 或者 被踢 才到这个登录页面的，所以要清除除了 LoginActivity 外的所有 Activity
            for (Activity activity : U.getActivityUtils().getActivityList()) {
                if (activity != this) {
                    activity.finish();
                }
            }
        }

        new PrivacyPolicyDialog(this).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public boolean onBackPressedForActivity() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            moveTaskToBack(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(U.app()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void destroy() {
        super.destroy();
        UMShareAPI.get(U.app()).release();
        mUiHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(U.app()).onSaveInstanceState(outState);
    }

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    @Override
    public boolean canSlide() {
        return false;
    }

    @Override
    public boolean resizeLayoutSelfWhenKeyboardShow() {
        // 自己处理有键盘时的整体布局
        return true;
    }

    /**
     * @param from      1代表 qq wx 登陆 2 代表手机号登陆
     * @param apiResult
     */
    public void onLoginResult(int from, ApiResult apiResult) {
        if (from == 1) {
            if (apiResult.getErrno() != 0) {
                final String errmsg = apiResult.getErrmsg();
                if (!TextUtils.isEmpty(errmsg)) {
                    if (mTipsDialogView != null) {
                        mTipsDialogView.dismiss();
                    }
                    mTipsDialogView = new TipsDialogView.Builder(LoginActivity.this)
                            .setMessageTip(errmsg)
                            .setOkBtnTip("确认")
                            .setOkBtnClickListener(new DebounceViewClickListener() {
                                @Override
                                public void clickValid(View v) {
                                    if (mTipsDialogView != null) {
                                        mTipsDialogView.dismiss();
                                    }
                                }
                            })
                            .build();
                    mTipsDialogView.showByDialog();
                    return;
                }
            }
        }
        if (apiResult.getErrno() == 0) {
            if (MyUserInfoManager.INSTANCE.hasMyUserInfo() && MyUserInfoManager.INSTANCE.isUserInfoFromServer()) {
                // 如果有账号了
                if (MyUserInfoManager.INSTANCE.isNeedCompleteInfo()) {
                    boolean isUpAc = U.getActivityUtils().getTopActivity().getClass().getSimpleName().equals("UploadAccountInfoActivity");
                    if (!isUpAc) {
                        IHomeService channelService = (IHomeService) ARouter.getInstance().build(RouterConstants.SERVICE_HOME).navigation();
                        if (channelService != null) {
                            channelService.goUploadAccountInfoActivity(this);
                            /**
                             * 前去完善资料
                             */
                            finish();
                            return;
                        }
                        // 顶层的不是这个activity 不用这个跳转，这因为慢，会导致露桌面
//                        ARouter.getInstance().build(RouterConstants.ACTIVITY_UPLOAD)
//                                .greenChannel().navigation();
                    } else {
                        MyLog.d(getTAG(), "顶部已经是UploadAccountInfoActivity");
                    }
                }
            }
            // 如果不需要完善资料,则跳到主页或者来源页
            Intent intent = getIntent();
            String originPath = intent.getStringExtra(KEY_ORIGIN_PATH);
            MyLog.d(getTAG(), "登录成功，跳回原页面 originPath:" + originPath);

            NoRemindManager.INSTANCE.refreshNoRemindCache((int) MyUserInfoManager.INSTANCE.getUid());

            if (!TextUtils.isEmpty(originPath)) {
                // 登录成功后，跳回原页面
                ARouter.getInstance().build(originPath)
                        .with(intent.getExtras())
                        .navigation();
            } else {
                // 必须放在这，防止当前栈中没有activity导致底部露出
                if (!U.getActivityUtils().isHomeActivityExist()) {
                    IHomeService channelService = (IHomeService) ARouter.getInstance().build(RouterConstants.SERVICE_HOME).navigation();
                    if (channelService != null) {
                        channelService.goHomeActivity(this);
                        /**
                         * 前去完善资料
                         */
                        finish();
                        return;
                    }
                } else {
                }
            }
            finish();
        }
    }
}
