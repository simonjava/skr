package com.module.playways.grab.room.inter;

import com.module.playways.rank.prepare.model.OnlineInfoModel;
import com.module.playways.rank.prepare.model.RoundInfoModel;
import com.module.playways.rank.room.model.RecordData;
import com.module.playways.rank.song.model.SongModel;
import com.zq.live.proto.Room.EQRoundResultType;

import java.util.List;

public interface IGrabView {
    /**
     * 抢唱阶段开始
     * 展示要唱的歌儿,《下一首》《东西》
     *
     * @param seq       当前轮次的序号
     * @param songModel 要唱的歌信息
     */
    void grabBegin(int seq, SongModel songModel);

    /**
     * 自己抢到了
     * 演唱阶段开始
     */
    void singBySelf();

    /**
     * 别人抢到了
     * 演唱阶段开始
     */
    void singByOthers(long uid);

    /**
     * 轮次结束
     *
     * @param reason               原因
     * @param playNextSongInfoCard 是否播放下一场抢唱的歌曲卡片
     */
    void roundOver(int reason, int reasonType, boolean playNextSongInfoCard, RoundInfoModel now);

    void updateUserState(List<OnlineInfoModel> jsonOnLineInfoList);

    /**
     * 中途跑了
     */
    void exitInRound();


    void gameFinish();

    /**
     * 战绩界面
     */
    void showRecordView(RecordData recordData);

    // 主舞台离开（开始主舞台消失动画）
    void hideMainStage();

}
