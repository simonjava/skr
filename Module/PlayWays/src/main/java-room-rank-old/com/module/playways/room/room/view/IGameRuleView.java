package com.module.playways.room.room.view;

import com.module.playways.room.prepare.model.OnlineInfoModel;
import com.module.playways.room.room.model.RankRoundInfoModel;
import com.module.playways.room.song.model.SongModel;

import java.util.List;

public interface IGameRuleView {
    void startSelfCountdown(RankRoundInfoModel lastRoundInfo, Runnable countDownOver);

    void startRivalCountdown(RankRoundInfoModel lastRoundInfo,int uid,String avatar);

    void userExit();

    void gameFinish();

//    void showVoteView();

//    /**
//     * 战绩界面
//     */
//    void showRecordView(RecordData recordData);

    void updateUserState(List<OnlineInfoModel> jsonOnLineInfoList);

    //先显示，然后再播放
    void playLyric(SongModel songModel);

    void updateScrollBarProgress(int score,int curTotalScore,int lineNum);

    // 显示演唱剩余时间倒计时
    void showLeftTime(long wholeTile);

    // 主舞台离开（开始主舞台消失动画）
    void hideMainStage();

    void finishActivity();

    void onOtherStartSing(SongModel songModel);

}
