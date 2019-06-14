package com.module.playways.grab.room.view.chorus;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.module.playways.R;
import com.module.playways.grab.room.model.ChorusRoundInfoModel;
import com.module.playways.grab.room.model.GrabRoundInfoModel;
import com.module.playways.grab.room.view.SingCountDownView2;
import com.module.playways.grab.room.view.control.SelfSingCardView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * 合唱的歌唱者看到的板子
 */
public class ChorusSelfSingCardView extends BaseChorusSelfCardView {

    public final static String TAG = "ChorusSelfSingCardView";

    SingCountDownView2 mSingCountDownView;

    public ChorusSelfSingCardView(Context context) {
        super(context);
    }

    public ChorusSelfSingCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChorusSelfSingCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void init() {
        inflate(getContext(), R.layout.grab_chorus_self_sing_card_layout, this);
        mLyricRecycleView = findViewById(R.id.lyric_recycle_view);
        mSingCountDownView = findViewById(R.id.sing_count_down_view);
        mLyricRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mChorusSelfLyricAdapter = new ChorusSelfLyricAdapter(mLeft, mRight);
        mLyricRecycleView.setAdapter(mChorusSelfLyricAdapter);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void playLyric() {
        if (mRoomData == null) {
            return;
        }
        mLeft.reset();
        mRight.reset();
        GrabRoundInfoModel infoModel = mRoomData.getRealRoundInfo();
        if (infoModel != null) {
            List<ChorusRoundInfoModel> chorusRoundInfoModelList = infoModel.getChorusRoundInfoModels();
            if (chorusRoundInfoModelList != null && chorusRoundInfoModelList.size() >= 2) {
                int uid1 = chorusRoundInfoModelList.get(0).getUserID();
                int uid2 = chorusRoundInfoModelList.get(1).getUserID();
                mLeft.mUserInfoModel = mRoomData.getUserInfo(uid1);
                mLeft.mChorusRoundInfoModel = chorusRoundInfoModelList.get(0);
                mRight.mUserInfoModel = mRoomData.getUserInfo(uid2);
                mRight.mChorusRoundInfoModel = chorusRoundInfoModelList.get(1);
            }
            mSongModel = infoModel.getMusic();
            playWithNoAcc();
            mSingCountDownView.startPlay(0, infoModel.getSingTotalMs(), true);
        }
    }

    public void setListener(SelfSingCardView.Listener listener) {
        super.setListener(listener);
        mSingCountDownView.setListener(listener);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE) {
            mSingCountDownView.reset();
        }
    }
}
