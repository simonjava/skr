package com.module.playways.grab.room.view.video;

import android.view.View;
import android.view.ViewStub;

import com.common.log.MyLog;
import com.module.playways.R;
import com.module.playways.grab.room.view.chorus.DoubleChorusSelfSingCardView;
import com.module.playways.grab.room.view.control.SelfSingCardView;
import com.module.playways.grab.room.view.minigame.DoubleMiniGameSelfSingCardView;
import com.module.playways.grab.room.view.normal.view.DoubleNormalSelfSingCardView;
import com.module.playways.room.song.model.SongModel;
import com.zq.live.proto.Common.StandPlayType;

public class DoubleSelfSingCardView {
    public final static String TAG = "DoubleSelfSingCardView";
    DoubleChorusSelfSingCardView mDoubleChorusSelfSingCardView;
    DoubleNormalSelfSingCardView mDoubleNormalSelfSingCardView;
    DoubleMiniGameSelfSingCardView mDoubleMiniGameSelfSingCardView;

    SongModel mSongModel;

    public DoubleSelfSingCardView(View rootView) {
        {
            ViewStub viewStub = rootView.findViewById(R.id.double_normal_lyric_view_stub);
            mDoubleNormalSelfSingCardView = new DoubleNormalSelfSingCardView(viewStub, null);
        }
        {
            ViewStub viewStub = rootView.findViewById(R.id.grab_video_chorus_lyric_view_stub);
            mDoubleChorusSelfSingCardView = new DoubleChorusSelfSingCardView(viewStub, null);
        }
        {
            ViewStub viewStub = rootView.findViewById(R.id.grab_video_mini_game_lyric_view_stub);
            mDoubleMiniGameSelfSingCardView = new DoubleMiniGameSelfSingCardView(viewStub, null);
        }

    }

    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            mDoubleNormalSelfSingCardView.setVisibility(View.GONE);
            mDoubleChorusSelfSingCardView.setVisibility(View.GONE);
            mDoubleMiniGameSelfSingCardView.setVisibility(View.GONE);
        } else if (visibility == View.VISIBLE) {
            mDoubleNormalSelfSingCardView.setVisibility(View.GONE);
            mDoubleChorusSelfSingCardView.setVisibility(View.GONE);
            mDoubleMiniGameSelfSingCardView.setVisibility(View.GONE);
            if (mSongModel.getPlayType() == StandPlayType.PT_CHO_TYPE.getValue()) {
                mDoubleChorusSelfSingCardView.setVisibility(View.VISIBLE);
            } else if (mSongModel.getPlayType() == StandPlayType.PT_MINI_GAME_TYPE.getValue()) {
                mDoubleMiniGameSelfSingCardView.setVisibility(View.VISIBLE);
            } else {
                mDoubleNormalSelfSingCardView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void playLyric(SongModel songModel) {
        if (songModel == null) {
            MyLog.w(TAG, "playLyric" + " songModel is null");
            return;
        }

        mSongModel = songModel;

        if (songModel.getPlayType() == StandPlayType.PT_CHO_TYPE.getValue()) {
//            mDoubleChorusSelfSingCardView.playLyric();
        } else if (songModel.getPlayType() == StandPlayType.PT_MINI_GAME_TYPE.getValue()) {
//            mDoubleMiniGameSelfSingCardView.playLyric();
        } else {
//            mDoubleNormalSelfSingCardView.playLyric();
        }
    }

    public void destroy() {
        mDoubleChorusSelfSingCardView.destroy();
        mDoubleNormalSelfSingCardView.destroy();
        mDoubleMiniGameSelfSingCardView.destroy();
    }

    public void setListener(SelfSingCardView.Listener listener) {
        mDoubleChorusSelfSingCardView.setListener(listener);
        mDoubleNormalSelfSingCardView.setListener(listener);
        mDoubleMiniGameSelfSingCardView.setListener(listener);
    }

//    public View getRealView() {
//        if(mDoubleNormalSelfSingCardView.getVisibility()==View.VISIBLE){
//            return mDoubleNormalSelfSingCardView.getRealView();
//        }
//        if(mVideoChorusSelfSingCardView.getVisibility()==View.VISIBLE){
//            return mVideoChorusSelfSingCardView.getRealView();
//        }
//        return null;
//    }
}
