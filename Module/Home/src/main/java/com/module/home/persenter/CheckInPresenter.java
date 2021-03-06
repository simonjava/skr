package com.module.home.persenter;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.common.base.BaseActivity;
import com.common.core.account.UserAccountManager;
import com.common.log.MyLog;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.rxretrofit.ControlType;
import com.common.rxretrofit.RequestControl;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.module.home.MainPageSlideApi;
import com.module.home.R;
import com.module.home.event.CheckInSuccessEvent;
import com.module.home.model.HomeGoldModel;
import com.module.home.view.CheckInSuccessView;
import com.module.home.view.HomeGoldCheckInView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.common.core.global.event.ShowDialogInHomeEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 签到
 */
public class CheckInPresenter extends RxLifeCyclePresenter {
    public final String TAG = "CheckInPresenter";
    public final static String RED_PKG_TASK_ID = "1";

    public static final String PREF_KEY_SHOW_CHECKIN = "checkin";

    MainPageSlideApi mMainPageSlideApi;

    DialogPlus mCheckInDialog;

    DialogPlus mCheckInSuccessDialog;

    BaseActivity mBaseActivity;

    Handler mUiHandler;

    boolean mHasShow = false;

    public CheckInPresenter(BaseActivity baseActivity) {
        mBaseActivity = baseActivity;
        mMainPageSlideApi = ApiManager.getInstance().createService(MainPageSlideApi.class);
        mUiHandler = new Handler();
    }

    public void check() {
        if (!UserAccountManager.INSTANCE.hasAccount()) {
            MyLog.w(TAG, "no account");
            return;
        }

        if (mHasShow) {
            MyLog.d(TAG, "checkin mHasShow=" + mHasShow);
            return;
        }

        long lastTs = U.getPreferenceUtils().getSettingLong(PREF_KEY_SHOW_CHECKIN, 0);
        long now = System.currentTimeMillis();
        long dayDiff = U.getDateTimeUtils().getDayDiff(lastTs, now);

        if (dayDiff == 0) {
            MyLog.d(TAG, "今天展示过了 lastTs=" + lastTs + " now=" + now);
            mHasShow = true;
            return;
        }

        ApiMethods.subscribe(mMainPageSlideApi.checkInInfo(), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                MyLog.d(TAG, "process" + " result=" + result.getErrno());
                if (result.getErrno() == 0) {
                    HomeGoldModel cur = JSON.parseObject(result.getData().getString("curr"), HomeGoldModel.class);
                    if (cur != null && cur.getState() == 2) {
                        MyLog.d(TAG, "check " + "已经签到过了");
                        return;
                    }

                    List<HomeGoldModel> homeGoldModelList = JSONArray.parseArray(result.getData().getString("items"), HomeGoldModel.class);
                    if (homeGoldModelList != null && homeGoldModelList.size() > 0) {
                        showCheckInView(homeGoldModelList);
                    } else {
                        MyLog.d(TAG, "check homeGoldModelList is null");
                    }
                } else {
                    MyLog.d(TAG, "check failed, " + " result=" + result.toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.e(TAG, e);
            }
        }, this, new RequestControl("checkInInfo", ControlType.CancelThis));
    }

    public void showCheckInView(List<HomeGoldModel> homeGoldModelList) {
        if (mCheckInDialog != null) {
            mCheckInDialog.dismiss();
        }

        HomeGoldCheckInView checkInView = new HomeGoldCheckInView(mBaseActivity);
        if (homeGoldModelList.size() == 7) {
            HomeGoldModel sevenDayModel = homeGoldModelList.remove(6);
            checkInView.setSevenDayInfo(sevenDayModel);
        }

        checkInView.setData(homeGoldModelList);
        checkInView.getIvReceive().setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                signIn();
                if (mCheckInDialog != null) {
                    mCheckInDialog.dismiss();
                }
            }
        });

        if (mCheckInDialog == null) {
            mCheckInDialog = DialogPlus.newDialog(mBaseActivity)
                    .setContentHolder(new ViewHolder(checkInView))
                    .setGravity(Gravity.CENTER)
                    .setContentBackgroundResource(R.color.transparent)
                    .setOverlayBackgroundResource(R.color.black_trans_80)
                    .setExpanded(false)
                    .setCancelable(true)
                    .setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(@NonNull DialogPlus dialog) {
                            // 不放在api里，因为到api那层不代表已经展示了
                            MyLog.d(TAG, "onDismiss" + " dialog=" + dialog);
                            U.getPreferenceUtils().setSettingLong(PREF_KEY_SHOW_CHECKIN, System.currentTimeMillis());
                            mHasShow = true;
                        }
                    })
                    .create();
        }
        EventBus.getDefault().post(new ShowDialogInHomeEvent(mCheckInDialog, 10));
    }

    public void signIn() {
        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(new HashMap<>()));
        ApiMethods.subscribe(mMainPageSlideApi.signIn(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                MyLog.d(TAG, "process" + " result=" + result.getErrno());
                if (result.getErrno() == 0) {
                    HomeGoldModel homeGoldModel = JSON.parseObject(result.getData().getString("curr"), HomeGoldModel.class);
                    showCheckInSuccessView(homeGoldModel);
                    EventBus.getDefault().post(new CheckInSuccessEvent());
                } else {
                    MyLog.w(TAG, "signIn failed, " + " result=" + result.getTraceId());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.e(TAG, e);
            }
        }, this);
    }

    public void showCheckInSuccessView(HomeGoldModel homeGoldModel) {
        CheckInSuccessView getRedPkgCashView = new CheckInSuccessView(mBaseActivity);
        getRedPkgCashView.setData(homeGoldModel);

        getRedPkgCashView.getIvConfirm().setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (mCheckInSuccessDialog != null) {
                    mCheckInSuccessDialog.dismiss();
                }
            }
        });
        if (mCheckInSuccessDialog == null) {
            mCheckInSuccessDialog = DialogPlus.newDialog(mBaseActivity)
                    .setContentHolder(new ViewHolder(getRedPkgCashView))
                    .setGravity(Gravity.CENTER)
                    .setContentBackgroundResource(R.color.transparent)
                    .setOverlayBackgroundResource(R.color.black_trans_80)
                    .setExpanded(false)
                    .setCancelable(true)
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(@NonNull DialogPlus dialog, @NonNull View view) {
                            MyLog.d(TAG, "onClick" + " dialog=" + dialog + " view=" + view);
                            mCheckInSuccessDialog.dismiss();
                        }
                    })
                    .create();
        }
        EventBus.getDefault().post(new ShowDialogInHomeEvent(mCheckInSuccessDialog, 9));
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mCheckInSuccessDialog != null) {
            mCheckInSuccessDialog.dismiss();
        }

        if (mCheckInDialog != null) {
            mCheckInDialog.dismiss();
        }

        mUiHandler.removeCallbacksAndMessages(null);
    }
}
