package com.module.home.updateinfo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.common.base.BaseFragment;
import com.common.callback.Callback;
import com.common.core.account.UserAccountManager;
import com.common.core.avatar.AvatarUtils;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.myinfo.MyUserInfoServerApi;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.statistics.StatisticsAdapter;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExImageView;
import com.common.view.ex.ExTextView;
import com.common.view.ex.NoLeakEditText;
import com.common.view.titlebar.CommonTitleBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.module.RouterConstants;
import com.module.home.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


public class UploadAccountInfoFragment extends BaseFragment {

    ExImageView mIvBack;
    SimpleDraweeView mAvatarIv;
    NoLeakEditText mNicknameEt;
    ExTextView mNicknameHintTv;

    ExTextView mNextIv;

    String mNickName = "";
    String mLastName = "";   //最后一次检查的昵称

    PublishSubject<String> mPublishSubject = PublishSubject.create();
    DisposableObserver<ApiResult> mDisposableObserver;

    @Override
    public int initView() {
        return R.layout.upload_account_info_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mIvBack = getRootView().findViewById(R.id.iv_back);

        mAvatarIv = getRootView().findViewById(R.id.avatar_iv);
        mNicknameEt = getRootView().findViewById(R.id.nickname_et);
        mNicknameHintTv = getRootView().findViewById(R.id.nickname_hint_tv);


        mNextIv = getRootView().findViewById(R.id.next_iv);

        mIvBack.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                // HomeAcitivy的
                UserAccountManager.INSTANCE.logoff(1, new Callback() {
                    @Override
                    public void onCallback(int r, Object obj) {
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                });

            }
        });

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
                    setNicknameHintText("昵称不能超过7个汉字或14个英文", true);
                    setCompleteTv(false);
                } else if (length == 0) {
                    setNicknameHintText("昵称不能为空哦～", true);
                    setCompleteTv(false);
                } else {
                    mNicknameHintTv.setVisibility(View.GONE);
                    if (mPublishSubject != null) {
                        mPublishSubject.onNext(str);
                    }
                }
            }
        });

        mNextIv.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                mNickName = mNicknameEt.getText().toString().trim();
                if (TextUtils.isEmpty(mNickName)) {
                    setNicknameHintText("昵称不能为空哦～", true);
                    setCompleteTv(false);
                } else {
                    verifyName(mNickName);
                }
            }
        });

        AvatarUtils.loadAvatarByUrl(mAvatarIv,
                AvatarUtils.newParamsBuilder(MyUserInfoManager.INSTANCE.getAvatar())
                        .setCircle(true)
                        .build());

        if (!TextUtils.isEmpty(MyUserInfoManager.INSTANCE.getNickName())) {
            mNicknameEt.setText(MyUserInfoManager.INSTANCE.getNickName());
            mNicknameEt.setSelection(MyUserInfoManager.INSTANCE.getNickName().length());
            setCompleteTv(true);
        } else {
            setCompleteTv(false);
        }

        initPublishSubject();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (TextUtils.isEmpty(MyUserInfoManager.INSTANCE.getNickName())) {
            mNicknameEt.requestFocus();
            U.getKeyBoardUtils().showSoftInputKeyBoard(getActivity());
        } else {
            mNicknameEt.clearFocus();
        }
    }


    private void verifyName(String nickName) {
        if (nickName.equals(MyUserInfoManager.INSTANCE.getNickName())) {
            U.getKeyBoardUtils().hideSoftInputKeyBoard(getActivity());
            goAgeTagUpload();
            return;
        }

        MyUserInfoServerApi myUserInfoServerApi = ApiManager.getInstance().createService(MyUserInfoServerApi.class);
        ApiMethods.subscribe(myUserInfoServerApi.verifyName(nickName), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    boolean isValid = result.getData().getBooleanValue("isValid");
                    String unValidReason = result.getData().getString("unValidReason");
                    if (isValid) {
                        // 昵称可用
                        U.getKeyBoardUtils().hideSoftInputKeyBoard(getActivity());
                        // 进入下一页
                        goAgeTagUpload();
                    } else {
                        // 昵称不可用
                        setNicknameHintText(unValidReason, true);
                    }
                }
            }
        });
    }

    public void goAgeTagUpload() {
        ARouter.getInstance().build(RouterConstants.ACTIVITY_UPLOAD_SEX_AGE)
                .withString("nickname", mNickName)
                .greenChannel()
                .navigation();
    }

    private void setNicknameHintText(String text, boolean isError) {
        if (isError) {
            mNicknameHintTv.setTextColor(Color.parseColor("#EDADC5"));
        } else {
            mNicknameHintTv.setTextColor(Color.WHITE);
        }
        mNicknameHintTv.setVisibility(View.VISIBLE);
        mNicknameHintTv.setText(text);
    }

    private void setCompleteTv(boolean isClick) {
        if (isClick && !TextUtils.isEmpty(mNicknameEt.getText().toString().trim())) {
            mNextIv.setClickable(true);
            mNextIv.setAlpha(1f);
        } else {
            mNextIv.setClickable(false);
            mNextIv.setAlpha(0.5f);
        }
    }

    private void initPublishSubject() {
        mDisposableObserver = new DisposableObserver<ApiResult>() {
            @Override
            public void onNext(ApiResult result) {
                if (result.getErrno() == 0) {
                    boolean isValid = result.getData().getBooleanValue("isValid");
                    String unValidReason = result.getData().getString("unValidReason");
                    mNickName = mNicknameEt.getText().toString().trim();
                    if (!TextUtils.isEmpty(mLastName) && mLastName.equals(mNickName)) {
                        if (isValid) {
                            // 昵称可用
                            setNicknameHintText("昵称可用", false);
                            setCompleteTv(true);
                        } else {
                            // 昵称不可用
                            setNicknameHintText(unValidReason, true);
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
        mPublishSubject.debounce(300, TimeUnit.MILLISECONDS).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return s.length() > 0;
            }
        }).switchMap(new Function<String, ObservableSource<ApiResult>>() {
            @Override
            public ObservableSource<ApiResult> apply(String string) throws Exception {
                mLastName = string;
                MyUserInfoServerApi myUserInfoServerApi = ApiManager.getInstance().createService(MyUserInfoServerApi.class);
                return myUserInfoServerApi.verifyName(string).subscribeOn(Schedulers.io());
            }
        }).retry(100).observeOn(AndroidSchedulers.mainThread()).subscribe(mDisposableObserver);
    }


    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        StatisticsAdapter.recordCountEvent("signup", "namesex_expose2", null, true);
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mPublishSubject != null) {
            mPublishSubject.onComplete();
        }
        if (mDisposableObserver != null) {
            mDisposableObserver.dispose();
        }

    }
}
