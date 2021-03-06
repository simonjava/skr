package com.module.home.presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.common.base.BaseActivity;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.pay.EPayPlatform;
import com.common.core.pay.IPayCallBack;
import com.common.core.pay.PayApi;
import com.common.core.pay.PayBaseReq;
import com.common.core.pay.ali.AliPayReq;
import com.common.core.pay.event.PayResultEvent;
import com.common.core.pay.wx.WxPayReq;
import com.common.log.MyLog;
import com.common.miLianYun.MiLianYunManager;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.utils.U;
import com.module.home.WalletServerApi;
import com.module.home.inter.IBallanceView;
import com.module.home.model.RechargeItemModel;

import java.util.HashMap;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import okhttp3.MediaType;
import okhttp3.RequestBody;

//同时只能有一个订单
public class BallancePresenter extends RxLifeCyclePresenter {
    public final String TAG = "BallencePresenter";

    public static final int IDLE = 0;
    public static final int SEND_ORDER = 1;
    public int mPayState = IDLE;

    PayApi mPayApi;

    IBallanceView mIBallanceView;

    WalletServerApi mWalletServerApi;

    String orderID;

    PayBaseReq mPayBaseReq;

    BaseActivity mBaseActivity;

    public static final int MSG_CHECK_ORDER = 100;

    Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MyLog.d(TAG, "handleMessage" + " msg=" + msg.what);
            if (msg.what == MSG_CHECK_ORDER && mPayState == SEND_ORDER) {
                checkOrder();
            }
        }
    };

    public BallancePresenter(Activity activity, IBallanceView iBallanceView) {
        mIBallanceView = iBallanceView;
        mBaseActivity = (BaseActivity) activity;
        mWalletServerApi = ApiManager.getInstance().createService(WalletServerApi.class);
        mPayApi = new PayApi(mBaseActivity, new IPayCallBack() {
            @Override
            public void onFailed(PayResultEvent event) {
                MyLog.w(TAG, "pay onFailed, errorCode is " + event.getErrorCode());
                if (event.getErrorCode() == PayApi.PAY_FAILD) {
                    mIBallanceView.rechargeFailed("支付失败");
                } else {
                    mIBallanceView.rechargeFailed("取消支付");
                }
                clearOrderState();
            }

            @Override
            public void onSuccess() {
                MyLog.w(TAG, "pay onSuccess");
                if(mPayBaseReq.mEPayPlatform != EPayPlatform.ALI_PAY) {
                    mIBallanceView.rechargeSuccess();
                    clearOrderState();
                }else if(mPayState == IDLE){ //阿里支付在回调成功时，IDLE 说明成功回调前过早检查了订单状态，需要重新检查下
                    mIBallanceView.rechargeSuccess();
                }
            }

            @Override
            public void payStart(PayBaseReq payBaseResp) {
                mIBallanceView.sendOrder(payBaseResp);
                mPayBaseReq = payBaseResp;
                mPayState = SEND_ORDER;
            }
        });
    }

    @Override
    public void resume() {
        super.resume();
        if (mPayState == SEND_ORDER) {
            mUiHandler.removeMessages(MSG_CHECK_ORDER);
            mUiHandler.sendMessageDelayed(mUiHandler.obtainMessage(MSG_CHECK_ORDER), 3000);
        }
    }

    //空闲状态
    private void clearOrderState() {
        mUiHandler.removeMessages(MSG_CHECK_ORDER);
        mPayState = IDLE;
        mPayBaseReq = null;
    }

    public void getGoodsList() {
        //20代表安卓平台
        ApiMethods.subscribe(mWalletServerApi.getGoodsList("20"), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult obj) {
                MyLog.w(TAG, "getGoodsList process" + " obj=" + obj);
                if (obj.getErrno() == 0) {
                    List<RechargeItemModel> list = JSON.parseArray(obj.getData().getString("list"), RechargeItemModel.class);
                    mIBallanceView.showRechargeList(list);
                }
            }
        }, this);
    }

    public void getZSBalance() {
        ApiMethods.subscribe(mWalletServerApi.getZSBalance(), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult obj) {
                MyLog.w(TAG, "getZSBalance process" + " obj=" + obj);
                if (obj.getErrno() == 0) {
                    String amount = obj.getData().getString("totalAmountStr");
                    mIBallanceView.showBalance(amount);
                }
            }
        }, this);
    }

    public void rechargeAliPay(String goodsID) {
        HashMap<String, Object> map = new HashMap<>();
        String ts = System.currentTimeMillis() + "";
        map.put("goodsID", goodsID);
        map.put("timeMs", ts);
        HashMap<String, Object> signMap = new HashMap<>();
        signMap.put("goodsID", goodsID);
        signMap.put("skrer", "skrer");
        signMap.put("userID", MyUserInfoManager.INSTANCE.getUid());
        signMap.put("timeMs", ts);
        signMap.put("appSecret", "dbf555fe9347eef8c74c5ff6b9f047dd");

        String sign = U.getMD5Utils().signReq(signMap);
        map.put("signV2", sign);

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));

        ApiMethods.subscribe(mWalletServerApi.aliOrder(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult obj) {
                MyLog.w(TAG, "rechargeAliPay process" + " obj=" + obj);
                if (obj.getErrno() == 0) {
                    orderID = obj.getData().getString("orderID");
                    String orderInfo = obj.getData().getString("paySign");
                    mPayApi.pay(new AliPayReq(orderInfo, orderID));
                } else {
                    U.getToastUtil().showShort(obj.getErrmsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.e(TAG, "rechargeAliPay onError" + " e=" + e);
                U.getToastUtil().showShort("获取订单失败，请重试");
            }

            @Override
            public void onNetworkError(ErrorType errorType) {
                MyLog.e(TAG, "rechargeAliPay net onError");
                U.getToastUtil().showShort("网络超时，请重试");
            }
        }, this);
    }

    public void rechargeWxPay(String goodsID) {
        HashMap<String, Object> map = new HashMap<>();
        String ts = System.currentTimeMillis() + "";
        map.put("goodsID", goodsID);
        map.put("timeMs", ts);

        HashMap<String, Object> signMap = new HashMap<>(map);
        signMap.put("userID", MyUserInfoManager.INSTANCE.getUid());
        signMap.put("skrer", "skrer");
        signMap.put("appSecret", "dbf555fe9347eef8c74c5ff6b9f047dd");
        String sign = U.getMD5Utils().signReq(signMap);

        map.put("signV2", sign);

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));

        ApiMethods.subscribe(mWalletServerApi.wxOrder(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult obj) {
                MyLog.w(TAG, "rechargeWxPay process" + " obj=" + obj);
                if (obj.getErrno() == 0) {
                    orderID = obj.getData().getString("orderID");
                    String prepayID = obj.getData().getString("prepayID");
                    mPayApi.pay(new WxPayReq(prepayID, sign, orderID));
                } else {
                    U.getToastUtil().showShort(obj.getErrmsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.e(TAG, "rechargeAliPay onError" + " e=" + e);
                U.getToastUtil().showShort("获取订单失败，请重试");
            }

            @Override
            public void onNetworkError(ErrorType errorType) {
                MyLog.e(TAG, "rechargeAliPay net onError");
                U.getToastUtil().showShort("网络超时，请重试");
            }
        }, this);
    }


    public void rechargeMiPay(String goodsID) {
        HashMap<String, Object> map = new HashMap<>();
//        String ts = System.currentTimeMillis() + "";
        map.put("goodsID", goodsID);
//        map.put("timeMs", ts);
//
//        HashMap<String, Object> signMap = new HashMap<>(map);
//        signMap.put("userID", MyUserInfoManager.INSTANCE.getUid());
//        signMap.put("skrer", "skrer");
//        signMap.put("appSecret", "dbf555fe9347eef8c74c5ff6b9f047dd");
//        String sign = U.getMD5Utils().signReq(signMap);
//
//        map.put("signV2", sign);

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));

        ApiMethods.subscribe(mWalletServerApi.xmOrder(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult obj) {
                MyLog.w(TAG, "rechargeXmPay process" + " obj=" + obj);
                if (obj.getErrno() == 0) {
                    orderID = obj.getData().getString("cpOrderID");
                    int feeValue = obj.getData().getIntValue("feeValue");
                    String cpUserInfo = obj.getData().getString("cpUserInfo");
                    MiLianYunManager.INSTANCE.pay(orderID, feeValue,cpUserInfo, new Function2<Integer, String, Unit>() {
                        @Override
                        public Unit invoke(Integer code, String msg) {
                            if (code == 0) {
                                mIBallanceView.rechargeSuccess();
                            } else {
                                mIBallanceView.rechargeFailed("支付失败 code=" + code + " msg=" + msg);
                            }
                            clearOrderState();
                            return null;
                        }
                    });
                } else {
                    U.getToastUtil().showShort(obj.getErrmsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.e(TAG, "rechargeXmPay onError" + " e=" + e);
                U.getToastUtil().showShort("获取订单失败，请重试");
            }

            @Override
            public void onNetworkError(ErrorType errorType) {
                MyLog.e(TAG, "rechargeXmPay net onError");
                U.getToastUtil().showShort("网络超时，请重试");
            }
        }, this);
    }

    public void checkOrder() {
        MyLog.d(TAG, "checkOrder");
        if (mPayBaseReq == null) {
            MyLog.d(TAG, "checkOrder mPayBaseReq is null");
            return;
        }

        if (mPayBaseReq.getEPayPlatform() == EPayPlatform.WX_PAY) {
            checkWxOrder(((WxPayReq) mPayBaseReq).getOrderID());
        }else if(mPayBaseReq.getEPayPlatform() == EPayPlatform.ALI_PAY){
            checkAliOrder(((AliPayReq)mPayBaseReq).getOrderID());
        }
        clearOrderState();
    }

    private void checkWxOrder(String orderId) {
        MyLog.d(TAG, "checkWxOrder" + " orderId=" + orderId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderID", orderId);

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));

        ApiMethods.subscribe(mWalletServerApi.wxOrderCheck(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult obj) {
                MyLog.d(TAG, "checkWxOrder process" + " obj=" + obj);
                if (obj.getErrno() == 0) {
                    mIBallanceView.rechargeSuccess();
                } else {
                    mIBallanceView.rechargeFailed(obj.getErrmsg());
                }

            }

            @Override
            public void onNetworkError(ErrorType errorType) {

            }

            @Override
            public void onError(Throwable e) {

            }
        }, this);
    }

    private void checkAliOrder(String orderID) {
        HashMap<String, Object> map = new HashMap<>();
        String ts = System.currentTimeMillis() + "";
        map.put("orderID", orderID);
        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));

        ApiMethods.subscribe(mWalletServerApi.aliOrderCheck(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult obj) {
                MyLog.d(TAG, "checkAliOrder process" + " obj=" + obj);
                if (obj.getErrno() == 0) {
                    mIBallanceView.rechargeSuccess();
                } else {
                    mIBallanceView.rechargeFailed(obj.getErrmsg());
                }

            }
        }, this);
    }

    @Override
    public void destroy() {
        super.destroy();
        mPayApi.release();
        mUiHandler.removeCallbacksAndMessages(null);
    }


}
