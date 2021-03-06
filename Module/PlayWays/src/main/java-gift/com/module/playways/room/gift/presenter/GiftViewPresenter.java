package com.module.playways.room.gift.presenter;

import com.common.log.MyLog;
import com.common.mvp.PresenterEvent;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.rxretrofit.ApiManager;
import com.module.playways.room.gift.GiftManager;
import com.module.playways.room.gift.GiftServerApi;
import com.module.playways.room.gift.event.GiftReadyEvent;
import com.module.playways.room.gift.inter.IGiftDisplayView;
import com.module.playways.room.gift.model.BaseGift;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GiftViewPresenter extends RxLifeCyclePresenter {
    IGiftDisplayView mIGiftView;

    Disposable mGiftDisposable;

    GiftServerApi mGiftServerApi;

    public GiftViewPresenter(IGiftDisplayView iGiftView) {
        mIGiftView = iGiftView;
        mGiftServerApi = ApiManager.getInstance().createService(GiftServerApi.class);
        addToLifeCycle();
        EventBus.getDefault().register(this);
    }

    public void loadData() {
        GiftManager.getInstance().loadGift();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GiftReadyEvent giftReadyEvent) {
        MyLog.w(getTAG(), "onEvent" + " giftReadyEvent=" + giftReadyEvent);
        if (giftReadyEvent.isGiftLoadSuccess()) {
            formatGiftData(GiftManager.getInstance().getBaseGiftList());
        } else {
            mIGiftView.getGiftListFaild();
        }
    }

    public void formatGiftData(List<BaseGift> giftList) {
        ArrayList<List<BaseGift>> arrayList = new ArrayList<>();

        Observable.fromIterable(giftList).buffer(8)
                .subscribeOn(Schedulers.io())
                .compose(bindUntilEvent(PresenterEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BaseGift>>() {
                    @Override
                    public void accept(List<BaseGift> baseGifts) throws Exception {
                        arrayList.add(baseGifts);
                    }
                }, throwable -> {
                    MyLog.d(getTAG(), "throwable" + throwable);
                }, () -> {
                    HashMap<Integer, List<BaseGift>> giftHashMap = new HashMap<>();
                    int index = 0;
                    for (List<BaseGift> baseGiftList : arrayList) {
                        giftHashMap.put(index++, baseGiftList);
                    }

                    mIGiftView.showGift(giftHashMap);
                });
    }


    @Override
    public void destroy() {
        super.destroy();
        EventBus.getDefault().unregister(this);
    }
}
