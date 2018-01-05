package com.wali.live.common.gift.bean;

import com.wali.live.common.gift.presenter.GiftMallPresenter;

import java.util.Iterator;
import java.util.List;

/**
 * Created by lan on 2017/12/21.
 */
public class GiftMallBean {
    //竖屏正常礼物
    private List<List<GiftMallPresenter.GiftWithCard>> mNormalGiftPortraitList;
    //竖屏包裹礼物
    private List<List<GiftMallPresenter.GiftWithCard>> mPktGiftPortraitList;
    //横屏正常礼物
    private List<GiftMallPresenter.GiftWithCard> mNormalGiftLandscapeList;
    //横屏包裹礼物
    private List<GiftMallPresenter.GiftWithCard> mPktGiftLandscapeList;

    public void remove(GiftMallPresenter.GiftWithCard giftWithCard) {
        if (mPktGiftLandscapeList != null) {
            removeGift(mPktGiftLandscapeList, giftWithCard);
        }
        if (mPktGiftPortraitList != null) {
            for (List<GiftMallPresenter.GiftWithCard> list : mPktGiftPortraitList) {
                if (list != null) {
                    if (removeGift(list, giftWithCard)) {
                        break;
                    }
                }
            }
        }
    }

    private boolean removeGift(List<GiftMallPresenter.GiftWithCard> list, GiftMallPresenter.GiftWithCard giftWithCard) {
        Iterator<GiftMallPresenter.GiftWithCard> iterator = list.iterator();
        while (iterator.hasNext()) {
            GiftMallPresenter.GiftWithCard giftWithCard1 = iterator.next();
            if (giftWithCard1.gift.getGiftId() == giftWithCard.gift.getGiftId()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public List<List<GiftMallPresenter.GiftWithCard>> getGiftPortraitList(boolean isMallGift) {
        return isMallGift ? mNormalGiftPortraitList : mPktGiftPortraitList;
    }

    public List<GiftMallPresenter.GiftWithCard> getGiftLandscapeList(boolean isMallGift) {
        return isMallGift ? mNormalGiftLandscapeList : mPktGiftLandscapeList;
    }

    public void setNormalGiftPortraitList(List<List<GiftMallPresenter.GiftWithCard>> normalGiftPortraitList) {
        mNormalGiftPortraitList = normalGiftPortraitList;
    }

    public void setPktGiftPortraitList(List<List<GiftMallPresenter.GiftWithCard>> pktGiftPortraitList) {
        mPktGiftPortraitList = pktGiftPortraitList;
    }

    public void setNormalGiftLandscapeList(List<GiftMallPresenter.GiftWithCard> normalGiftLandscapeList) {
        mNormalGiftLandscapeList = normalGiftLandscapeList;
    }

    public void setPktGiftLandscapeList(List<GiftMallPresenter.GiftWithCard> pktGiftLandscapeList) {
        mPktGiftLandscapeList = pktGiftLandscapeList;
    }
}
