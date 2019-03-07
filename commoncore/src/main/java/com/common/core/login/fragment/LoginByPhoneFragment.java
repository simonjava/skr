package com.common.core.login.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.base.BaseFragment;
import com.common.core.R;
import com.common.core.account.UserAccountManager;
import com.common.core.account.UserAccountServerApi;
import com.common.core.account.event.VerifyCodeErrorEvent;
import com.common.core.permission.SkrBasePermission;
import com.common.core.permission.SkrPhoneStatePermission;
import com.common.log.MyLog;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.utils.FragmentUtils;
import com.common.utils.HandlerTaskTimer;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExImageView;
import com.common.view.ex.ExTextView;
import com.common.view.ex.NoLeakEditText;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 手机方式登陆界面
 */
public class LoginByPhoneFragment extends BaseFragment {

    public static final String PREF_KEY_PHONE_NUM = "pref_key_phone_num";

    RelativeLayout mMainActContainer;
    ExImageView mIvBack;
    NoLeakEditText mPhoneInputTv;
    NoLeakEditText mCodeInputTv;
    ExTextView mGetCodeTv;
    ExTextView mLoginTv;
    ExTextView mErrorHint;

    String mPhoneNumber; //发送验证码的电话号码
    String mCode; //验证码

    HandlerTaskTimer mTaskTimer; // 倒计时验证码

    SkrBasePermission mSkrPermission = new SkrPhoneStatePermission();

    @Override
    public int initView() {
        return R.layout.core_phone_login_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mMainActContainer = (RelativeLayout) mRootView.findViewById(R.id.main_act_container);
        mIvBack = (ExImageView) mRootView.findViewById(R.id.iv_back);
        mPhoneInputTv = (NoLeakEditText) mRootView.findViewById(R.id.phone_input_tv);
        mCodeInputTv = (NoLeakEditText) mRootView.findViewById(R.id.code_input_tv);
        mGetCodeTv = (ExTextView) mRootView.findViewById(R.id.get_code_tv);
        mErrorHint = (ExTextView) mRootView.findViewById(R.id.error_hint);
        mLoginTv = (ExTextView) mRootView.findViewById(R.id.login_tv);

        mPhoneInputTv.setText(U.getPreferenceUtils().getSettingString(PREF_KEY_PHONE_NUM, ""));

        if (TextUtils.isEmpty(mPhoneInputTv.getText().toString().trim())) {
            mPhoneInputTv.requestFocus();
        } else {
            mCodeInputTv.requestFocus();
        }
        U.getKeyBoardUtils().showSoftInputKeyBoard(getActivity());

        mGetCodeTv.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                mPhoneNumber = mPhoneInputTv.getText().toString().trim();
                if (checkPhoneNumber(mPhoneNumber)) {
                    sendSmsVerifyCode(mPhoneNumber);
                }
            }
        });

        mLoginTv.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                U.getKeyBoardUtils().hideSoftInputKeyBoard(getActivity());
                mPhoneNumber = mPhoneInputTv.getText().toString().trim();
                mCode = mCodeInputTv.getText().toString().trim();
                if (checkPhoneNumber(mPhoneNumber)) {
                    if (!TextUtils.isEmpty(mCode)) {
                        if (!U.getNetworkUtils().hasNetwork()) {
                            setHintText("网络异常，请检查网络之后重试！", true);
                        } else {
                            if (U.getChannelUtils().getChannel().startsWith("MI_SHOP_mimusic")) {
                                // 小米商店渠道，需要获取读取imei权限
                                mSkrPermission.ensurePermission(new Runnable() {
                                    @Override
                                    public void run() {
                                        UserAccountManager.getInstance().loginByPhoneNum(mPhoneNumber, mCode);
                                    }
                                }, true);
                            } else {
                                UserAccountManager.getInstance().loginByPhoneNum(mPhoneNumber, mCode);
                            }
                        }
                    } else {
                        setHintText("验证码为空", true);
                    }
                }
            }
        });

        mIvBack.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                U.getSoundUtils().play(TAG, R.raw.normal_back, 500);
                U.getKeyBoardUtils().hideSoftInputKeyBoard(getActivity());
                stopTimeTask();
                U.getFragmentUtils().popFragment(new FragmentUtils.PopParams.Builder()
                        .setPopFragment(LoginByPhoneFragment.this)
                        .setPopAbove(false)
                        .setHasAnimation(true)
                        .setNotifyShowFragment(LoginFragment.class)
                        .build());
            }
        });

        mLoginTv.setClickable(false);
        mLoginTv.setTextColor(Color.parseColor("#660C2275"));

        U.getSoundUtils().preLoad(TAG, R.raw.normal_back);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSkrPermission.onBackFromPermisionManagerMaybe();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VerifyCodeErrorEvent event) {
        MyLog.d(TAG, "onEventMainThread" + " event=" + event);
        setHintText(event.getErrmsg(), true);
    }

    private void setHintText(String text, boolean isError) {
        if (isError) {
            mErrorHint.setTextColor(Color.parseColor("#EF5E85"));
        } else {
            mErrorHint.setTextColor(Color.parseColor("#398A26"));
        }
        mErrorHint.setText(text);
    }

    private void sendSmsVerifyCode(final String phoneNumber) {
        MyLog.d(TAG, "sendSmsVerifyCode" + " phoneNumber=" + phoneNumber);
        if (!U.getNetworkUtils().hasNetwork()) {
            setHintText("网络异常，请检查网络后重试!", true);
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            return;
        }

        UserAccountServerApi userAccountServerApi = ApiManager.getInstance().createService(UserAccountServerApi.class);

        long timeMs = System.currentTimeMillis();
        String sign = U.getMD5Utils().MD5_32("skrer|sms|" +
                String.valueOf(phoneNumber) + "|" +
                String.valueOf(timeMs));

        ApiMethods.subscribe(userAccountServerApi.sendSmsVerifyCode(phoneNumber, timeMs, sign), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    // 发送验证码成功
                    setHintText("验证码发送成功", false);
                    U.getPreferenceUtils().setSettingString(PREF_KEY_PHONE_NUM, phoneNumber);
                    mGetCodeTv.setSelected(true);
                    mGetCodeTv.setClickable(false);
                    mCodeInputTv.setFocusable(true);
                    mCodeInputTv.setFocusableInTouchMode(true);
                    mCodeInputTv.requestFocus();
                    mLoginTv.setClickable(true);
                    mLoginTv.setTextColor(Color.parseColor("#0C2275"));
                    mLoginTv.setBackgroundResource(R.drawable.img_btn_bg_yellow);
                    startTimeTask();
                } else {
                    setHintText(result.getErrmsg(), true);
                }
            }
        }, this);
    }


    /**
     * 更新准备时间倒计时
     */
    public void startTimeTask() {
        mTaskTimer = HandlerTaskTimer.newBuilder()
                .interval(1000)
                .take(60)
                .start(new HandlerTaskTimer.ObserverW() {
                    @Override
                    public void onNext(Integer integer) {
                        mGetCodeTv.setText("倒计时:" + (60 - integer) + "s");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mGetCodeTv.setText("获取验证码");
                        mGetCodeTv.setSelected(false);
                        mGetCodeTv.setClickable(true);
                    }
                });
    }

    public void stopTimeTask() {
        if (mTaskTimer != null) {
            mTaskTimer.dispose();
        }
    }

    /**
     * 检查手机号是否正确
     *
     * @return
     */
    private boolean checkPhoneNumber(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber) && TextUtils.isDigitsOnly(phoneNumber)
                && phoneNumber.length() == 11 && phoneNumber.startsWith("1")) {
            return true;
        } else if (TextUtils.isEmpty(phoneNumber)) {
            setHintText("手机号为空", true);
        } else {
            setHintText("手机号有误", true);
        }

        return false;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        stopTimeTask();
        U.getSoundUtils().release(TAG);
    }

    @Override
    protected boolean onBackPressed() {
        stopTimeTask();
        U.getFragmentUtils().popFragment(new FragmentUtils.PopParams.Builder()
                .setPopFragment(LoginByPhoneFragment.this)
                .setPopAbove(false)
                .setHasAnimation(true)
                .setNotifyShowFragment(LoginFragment.class)
                .build());
        return true;
    }
}
