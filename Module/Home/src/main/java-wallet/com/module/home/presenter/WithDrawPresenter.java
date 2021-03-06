package com.module.home.presenter;

import com.alibaba.fastjson.JSON;
import com.common.log.MyLog;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.utils.U;
import com.module.home.WalletServerApi;
import com.module.home.inter.IWithDrawView;
import com.module.home.model.WithDrawInfoModel;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.module.home.fragment.WithdrawFragment.WX_CHANNEL;

public class WithDrawPresenter extends RxLifeCyclePresenter {
    IWithDrawView mWithDrawView;
    WalletServerApi mWalletServerApi;
    WithDrawInfoModel mWithDrawInfoModel;

    public WithDrawPresenter(IWithDrawView withDrawView) {
        mWithDrawView = withDrawView;
        mWalletServerApi = ApiManager.getInstance().createService(WalletServerApi.class);
    }


    public void getWithDrawInfo(int deep) {
        if (deep < 10) {
            ApiMethods.subscribe(mWalletServerApi.getWithdrawInfo(), new ApiObserver<ApiResult>() {
                @Override
                public void process(ApiResult result) {
                    if (result.getErrno() == 0) {
                        mWithDrawInfoModel = JSON.parseObject(result.getData().toString(), WithDrawInfoModel.class);
                        mWithDrawView.showWithDrawInfo(mWithDrawInfoModel);
                    } else {
                        U.getToastUtil().showShort(result.getErrmsg());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    getWithDrawInfo(deep+1);
                }

                @Override
                public void onNetworkError(ErrorType errorType) {
                    getWithDrawInfo(deep+1);
                }
            }, this);
        } else {
            MyLog.e(getTAG(), "10次都没拉到数据");
            U.getToastUtil().showShort("您网络异常，请退出重进");
        }
    }

    public void bindWxChannel(String openId, String accessToken) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("openID", openId);
        map.put("accessToken", accessToken);
        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));

        ApiMethods.subscribe(mWalletServerApi.authBindWX(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    U.getToastUtil().showShort("绑定微信成功");
                    mWithDrawInfoModel.getByChannel(WX_CHANNEL).setIsBind(true);
                    mWithDrawView.bindWxResult(true);
                } else {
                    U.getToastUtil().showShort(result.getErrmsg());
                    mWithDrawView.bindWxResult(false);
                }
            }

            @Override
            public void onNetworkError(ErrorType errorType) {
                U.getToastUtil().showShort("网络延迟，请重试");
            }

            @Override
            public void onError(Throwable e) {
                U.getToastUtil().showShort("绑定微信失败，请重试");
            }
        }, this);
    }

    /**
     * 提现
     *
     * @param amount
     * @param action
     */
    public void withDraw(long amount, int action) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("amount", amount);
        map.put("action", action);
        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));

        ApiMethods.subscribe(mWalletServerApi.decr(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    mWithDrawView.withDraw(true);
                } else {
                    U.getToastUtil().showShort(result.getErrmsg());
                    mWithDrawView.withDraw(false);
                }
            }

            @Override
            public void onNetworkError(ErrorType errorType) {
                U.getToastUtil().showShort("网络延迟，请重试");
            }

            @Override
            public void onError(Throwable e) {
                U.getToastUtil().showShort("网络延迟，请重试");
            }
        }, this);
    }
}
