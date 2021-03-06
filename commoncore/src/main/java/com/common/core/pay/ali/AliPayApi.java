package com.common.core.pay.ali;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.common.core.pay.EPayPlatform;
import com.common.core.pay.IPayApi;
import com.common.core.pay.PayBaseReq;
import com.common.core.pay.event.PayResultEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.Map;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AliPayApi implements IPayApi {
    Activity mActivity;

    Disposable disposable;

    public AliPayApi(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void pay(final PayBaseReq payBaseResp) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        final AliPayReq aliPayReq = (AliPayReq) payBaseResp;
        io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) {
                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(aliPayReq.getOrderInfo(), true);

                PayResult payResult = new PayResult(result);
                JSONObject jsonObject = JSON.parseObject(payResult.getResult());
                AlipayResponse alipayResponse = JSON.parseObject(jsonObject.getString("alipay_trade_app_pay_response"), AlipayResponse.class);
                //AliPay 成功到返回值为10000
                EventBus.getDefault().post(new PayResultEvent(EPayPlatform.ALI_PAY, alipayResponse.getSub_msg(), alipayResponse.getCode() == 10000 ? 0 : alipayResponse.getCode()));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        EventBus.getDefault().post(new PayResultEvent(EPayPlatform.ALI_PAY, e.getMessage(), -1));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static class AlipayResponse implements Serializable {
        int code;

        String msg;

        String sub_code;

        String sub_msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getSub_code() {
            return sub_code;
        }

        public void setSub_code(String sub_code) {
            this.sub_code = sub_code;
        }

        public String getSub_msg() {
            return sub_msg;
        }

        public void setSub_msg(String sub_msg) {
            this.sub_msg = sub_msg;
        }
    }

    @Override
    public void release() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
