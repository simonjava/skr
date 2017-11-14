package com.wali.live.livesdk.live.window.presenter;

import com.base.image.fresco.image.BaseImage;
import com.base.image.fresco.image.ImageFactory;
import com.base.log.MyLog;
import com.thornbirds.component.IParams;
import com.wali.live.component.presenter.BaseSdkRxPresenter;
import com.wali.live.dao.Gift;
import com.wali.live.livesdk.live.window.event.FloatGiftEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wangmengjie on 17-9-11.
 */

public class GameFloatIconPresenter extends BaseSdkRxPresenter<IGameFloatIcon> {

    private final LinkedList<Gift> mGiftQueue = new LinkedList<>();
    private Subscription mGiftSubscription;

    public GameFloatIconPresenter() {
        super(null);
    }

    @Override
    protected final String getTAG() {
        return "GameFloatIconPresenter";
    }

    @Override
    public void startPresenter() {
        super.startPresenter();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void stopPresenter() {
        super.stopPresenter();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FloatGiftEvent event) {
        boolean mIsAdd = false;
        if (event != null && event.barrageMsg != null) {
            Gift gift = event.barrageMsg;
            MyLog.d(TAG, "gift = " + gift.getPicture());
            mIsAdd = mGiftQueue.add(gift);
        }
        if (mIsAdd) {
            processGift();
        }
    }

    private void processGift() {
        Gift gift = mGiftQueue.poll();
        if (gift != null) {
            if (mGiftSubscription != null && !mGiftSubscription.isUnsubscribed()) {
                mGiftSubscription.unsubscribe();
            }
            BaseImage image = ImageFactory.newHttpImage(gift.getPicture()).build();
            mView.startGiftAnimator(false, image);
        }
    }

    public void giftShowTimer(long delayTime) {
        if (mGiftSubscription != null && !mGiftSubscription.isUnsubscribed()) {
            mGiftSubscription.unsubscribe();
        }
        mGiftSubscription = Observable.timer(delayTime, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(PresenterEvent.STOP))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mView.startGiftAnimator(mGiftQueue.isEmpty(), null);
                        if (!mGiftQueue.isEmpty()) {
                            processGift();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        MyLog.e(TAG, "giftShowTimer failed, throwable=" + throwable);
                    }
                });
    }

    @Override
    public boolean onEvent(int event, IParams params) {
        return false;
    }
}
