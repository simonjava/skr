package com.module.home.persenter;

import com.alibaba.fastjson.JSONArray;
import com.common.core.account.UserAccountManager;
import com.common.log.MyLog;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.module.home.MainPageSlideApi;
import com.module.home.model.RedPkgTaskModel;
import com.module.home.view.IRedPkgView;
import java.util.List;

public class RedPkgPresenter extends RxLifeCyclePresenter {
    public final static String TAG = "RedPkgPresenter";
    public final static String RED_PKG_TASK_ID = "1";

    MainPageSlideApi mMainPageSlideApi;
    IRedPkgView view;

    boolean mIsHasShow = false;

    public RedPkgPresenter(IRedPkgView view) {
        this.view = view;
        mMainPageSlideApi = ApiManager.getInstance().createService(MainPageSlideApi.class);
    }

    public void checkRedPkg() {
        if (mIsHasShow) {
            MyLog.d(TAG, "has checkRedPkg");
            return;
        }

        if (!UserAccountManager.getInstance().hasAccount()) {
            MyLog.w(TAG, "no account");
            return;
        }

        ApiMethods.subscribe(mMainPageSlideApi.checkRedPkg(), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                MyLog.d(TAG, "process" + " result=" + result.getErrno());
                if (result.getErrno() == 0) {
                    mIsHasShow = true;
                    List<RedPkgTaskModel> redPkgTaskModelList = JSONArray.parseArray(result.getData().getString("tasks"), RedPkgTaskModel.class);
                    if (redPkgTaskModelList != null) {
                        for (RedPkgTaskModel model :
                                redPkgTaskModelList) {
                            if (RED_PKG_TASK_ID.equals(model.getTaskID()) && !model.isDone()) {
                                view.showGetCashView(Float.parseFloat(model.getRedbagExtra().getCash()), model.getDeepLink());
                            }
                        }
                    } else {
                        MyLog.d(TAG, "checkRedPkg redPkgTaskModelList is null");
                    }
                } else {
                    MyLog.d(TAG, "checkRedPkg failed, " + " result=" + result.getTraceId());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.e(TAG, e);
            }
        }, this);
    }
}
