package com.wali.live.modulechannel.presenter;

import android.support.annotation.NonNull;

import com.common.log.MyLog;
import com.common.mvp.PresenterEvent;
import com.common.mvp.RxLifeCyclePresenter;
import com.wali.live.modulechannel.api.ChannelDataStore;
import com.wali.live.modulechannel.model.viewmodel.BaseViewModel;
import com.wali.live.modulechannel.model.viewmodel.ChannelModelFactory;
import com.wali.live.modulechannel.model.viewmodel.ChannelUiType;
import com.wali.live.modulechannel.model.viewmodel.ChannelViewModel;
import com.wali.live.proto.CommonChannel.ChannelItem;
import com.wali.live.proto.HotChannel.GetRecommendListRsp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhujianning on 18-10-17.
 */

public class ChannelPresenter extends RxLifeCyclePresenter implements IChannelPresenter {
    public static final String TAG = ChannelPresenter.class.getSimpleName();
    public static final String LOG_FORMAT = "<channel id:%s> %s";

    private ChannelDataStore mDataStore;
    private IChannelView mView;

    private long mChannelId;

    private Disposable mDisposable;
    private Disposable mGetDataDisposable;

    public ChannelPresenter(IChannelView view) {
        addToLifeCycle();
        mDataStore = new ChannelDataStore();
        mView = view;
    }

    @Override
    public void setChannelId(long channelId) {
        mChannelId = channelId;
    }

    @Override
    public void start() {
        super.start();
        MyLog.d(TAG, formatLog("start"));
        if (mDisposable != null
                && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        //  因为开始可能会拉不到数据，用个定时器不停拉
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(10)
                .compose(this.bindUntilEvent(PresenterEvent.DESTROY))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {

                        if(mGetDataDisposable != null && !mGetDataDisposable.isDisposed()) {
                            return;
                        }

                        getDataFromServer();
                    }

                    @Override
                    public void onError(Throwable e) {
                        MyLog.d(TAG, e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void getDataFromServer() {

        if(mGetDataDisposable != null
                && !mGetDataDisposable.isDisposed()) {
            return;
        }
        MyLog.d(TAG, formatLog("getDataFromServer"));

        mDataStore.getHotChannelObservable(mChannelId)
                .subscribeOn(Schedulers.io())
                .map(new io.reactivex.functions.Function<GetRecommendListRsp, List<? extends BaseViewModel>>() {
                    @Override
                    public List<? extends BaseViewModel> apply(GetRecommendListRsp getRecommendListRsp) throws Exception {
                        if(getRecommendListRsp != null) {
                            return processRsp(getRecommendListRsp);
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindUntilEvent(PresenterEvent.DESTROY))
                .subscribe(new Observer<List<? extends BaseViewModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mGetDataDisposable = d;
                    }

                    @Override
                    public void onNext(List<? extends BaseViewModel> list) {
                        MyLog.d(TAG, formatLog("getChannelObservable onNext"));
                        if (list != null && list.size() != 0) {
                            mView.updateView(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MyLog.d(TAG, e);
                        MyLog.d(TAG, formatLog("getChannelObservable onError=" + e.getMessage()));
                        mView.finishRefresh();
                    }

                    @Override
                    public void onComplete() {
                        mView.finishRefresh();
                        if(mDisposable != null && !mDisposable.isDisposed()) {
                            mDisposable.dispose();
                        }
                    }
                });
    }

    private List<? extends BaseViewModel> processRsp(@NonNull GetRecommendListRsp rsp) {
        MyLog.d(TAG, formatLog("processRsp"));
        List<ChannelViewModel> models = new ArrayList();

        for (ChannelItem protoItem : rsp.getItemsList()) {
            ChannelViewModel viewModel = ChannelModelFactory.getChannelViewModel(protoItem);
            if (viewModel == null) {
                continue;
            }

            int uiType = viewModel.getUiType();
            if (uiType == ChannelUiType.TYPE_TWO_CARD || uiType == ChannelUiType.TYPE_THREE_CARD) {
                models.add(viewModel);
            }
        }
        return models;
    }

    private String formatLog(String method) {
        return String.format(LOG_FORMAT, mChannelId, method);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
