package com.module.home.updateinfo.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.common.base.BaseFragment;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.myinfo.MyUserInfoServerApi;
import com.common.log.MyLog;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExImageView;
import com.common.view.ex.ExTextView;
import com.common.view.ex.NoLeakEditText;
import com.jakewharton.rxbinding2.view.RxView;
import com.module.RouterConstants;
import com.module.home.R;
import com.module.home.updateinfo.UploadAccountInfoActivity;
import com.zq.live.proto.Common.ESex;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


public class UploadAccountInfoFragment extends BaseFragment {

    boolean isUpload = false; //当前是否是完善个人资料

    RelativeLayout mMainActContainer;
    ExImageView mBackIv;
    NoLeakEditText mNicknameEt;
    ExTextView mNicknameHintTv;

    ExImageView mMale;
    ExImageView mFemale;

    ExTextView mCompleteTv;

    int sex = 0;// 未知、非法参数
    String mNickName = "";
    String mLastName = "";   //最后一次检查的昵称

    CompositeDisposable mCompositeDisposable;
    PublishSubject<String> mPublishSubject = PublishSubject.create();
    DisposableObserver<ApiResult> mDisposableObserver;

    @Override
    public int initView() {
        return R.layout.upload_account_info_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mMainActContainer = (RelativeLayout) mRootView.findViewById(R.id.main_act_container);
        mBackIv = (ExImageView) mRootView.findViewById(R.id.back_iv);
        mNicknameEt = (NoLeakEditText) mRootView.findViewById(R.id.nickname_et);
        mNicknameHintTv = (ExTextView) mRootView.findViewById(R.id.nickname_hint_tv);
        mMale = (ExImageView) mRootView.findViewById(R.id.male);
        mFemale = (ExImageView) mRootView.findViewById(R.id.female);
        mCompleteTv = (ExTextView) mRootView.findViewById(R.id.complete_tv);

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpload = bundle.getBoolean(UploadAccountInfoActivity.BUNDLE_IS_UPLOAD);
        }

        mNicknameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                int length = U.getStringUtils().getStringLength(str);
                if (length > 14) {
                    mNicknameHintTv.setVisibility(View.VISIBLE);
                    mNicknameHintTv.setTextColor(Color.parseColor("#EF5E85"));
                    mNicknameHintTv.setText("昵称不能超过7个汉字或14个英文");
                    setCompleteTv(false);
                } else if (length == 0) {
                    mNicknameHintTv.setVisibility(View.VISIBLE);
                    mNicknameHintTv.setTextColor(Color.parseColor("#EF5E85"));
                    mNicknameHintTv.setText("昵称不能为空哦～");
                    setCompleteTv(false);
                } else {
                    mNicknameHintTv.setVisibility(View.GONE);
                    if (mPublishSubject != null) {
                        mPublishSubject.onNext(str);
                    }
                }
            }
        });

        mBackIv.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
                ARouter.getInstance().build(RouterConstants.ACTIVITY_LOGIN)
                        .greenChannel().navigation();
            }
        });

        mCompleteTv.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                mNickName = mNicknameEt.getText().toString().trim();
                MyUserInfoManager.getInstance().updateInfo(MyUserInfoManager.newMyInfoUpdateParamsBuilder()
                        .setNickName(mNickName).setSex(sex)
                        .build(), false, new MyUserInfoManager.ServerCallback() {
                    @Override
                    public void onSucess() {
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        });

        mMale.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                selectSex(true);
            }
        });

        mFemale.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                selectSex(false);
            }
        });

        if (!TextUtils.isEmpty(MyUserInfoManager.getInstance().getNickName())) {
            mNicknameEt.setText(MyUserInfoManager.getInstance().getNickName());
            setCompleteTv(true);
        } else {
            setCompleteTv(false);
        }

        if (MyUserInfoManager.getInstance().getSex() == ESex.SX_MALE.getValue()) {
            selectSex(true);
        } else {
            selectSex(false);
        }

        initPublishSubject();
    }

    private void setCompleteTv(boolean isClick) {
        if (isClick) {
            mCompleteTv.setClickable(true);
            mCompleteTv.setTextColor(Color.parseColor("#0C2275"));
            mCompleteTv.setBackgroundResource(com.common.core.R.drawable.img_btn_bg_yellow);
        } else {
            mCompleteTv.setClickable(false);
            mCompleteTv.setTextColor(Color.parseColor("#660C2275"));
            mCompleteTv.setBackgroundResource(com.common.core.R.drawable.img_btn_bg_gray);
        }
    }

    // 选一个，另一个需要缩放动画
    private void selectSex(boolean isMale) {
        this.sex = isMale ? ESex.SX_MALE.getValue() : ESex.SX_FEMALE.getValue();
        mMale.setBackground(isMale ? getResources().getDrawable(R.drawable.head_man_xuanzhong) : getResources().getDrawable(R.drawable.head_man_weixuanzhong));
        mFemale.setBackground(isMale ? getResources().getDrawable(R.drawable.head_woman_weixuanzhong) : getResources().getDrawable(R.drawable.head_women_xuanzhong));
        // 放大动画
        ObjectAnimator a1 = ObjectAnimator.ofFloat(isMale ? mMale : mFemale, "scaleX", 1f, 1.2f);
        ObjectAnimator a2 = ObjectAnimator.ofFloat(isMale ? mMale : mFemale, "scaleY", 1f, 1.2f);
        // 缩小动画
        ObjectAnimator s1 = ObjectAnimator.ofFloat(isMale ? mFemale : mMale, "scaleX", 1.2f, 1f);
        ObjectAnimator s2 = ObjectAnimator.ofFloat(isMale ? mFemale : mMale, "scaleY", 1.2f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(80);
        set.playTogether(a1, a2, s1, s2);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mMale.setClickable(isMale ? false : true);
                mFemale.setClickable(isMale ? true : false);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                onAnimationEnd(animator);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        set.start();
    }

    private void initPublishSubject() {
        mDisposableObserver = new DisposableObserver<ApiResult>() {
            @Override
            public void onNext(ApiResult result) {
                if (result.getErrno() == 0) {
                    boolean isValid = result.getData().getBoolean("isValid");
                    String unValidReason = result.getData().getString("unValidReason");
                    mNickName = mNicknameEt.getText().toString().trim();
                    if (!TextUtils.isEmpty(mLastName) && mLastName.equals(mNickName)) {
                        if (isValid) {
                            // 昵称可用
                            mNicknameHintTv.setVisibility(View.VISIBLE);
                            mNicknameHintTv.setTextColor(Color.parseColor("#7ED321"));
                            mNicknameHintTv.setText("昵称可用");
                            setCompleteTv(true);
                        } else {
                            // 昵称不可用
                            mNicknameHintTv.setVisibility(View.VISIBLE);
                            mNicknameHintTv.setTextColor(Color.parseColor("#EF5E85"));
                            mNicknameHintTv.setText(unValidReason);
                            setCompleteTv(false);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        mPublishSubject.debounce(500, TimeUnit.MILLISECONDS).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return s.length() > 0;
            }
        }).switchMap(new Function<String, ObservableSource<ApiResult>>() {
            @Override
            public ObservableSource<ApiResult> apply(String string) throws Exception {
                mLastName = string;
                MyUserInfoServerApi myUserInfoServerApi = ApiManager.getInstance().createService(MyUserInfoServerApi.class);
                return myUserInfoServerApi.checkNickName(string).subscribeOn(Schedulers.io());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(mDisposableObserver);
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(mDisposableObserver);
    }


    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }

    }
}
