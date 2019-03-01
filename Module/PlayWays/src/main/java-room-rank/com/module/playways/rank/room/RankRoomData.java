package com.module.playways.rank.room;

import com.common.core.account.UserAccountManager;
import com.common.log.MyLog;
import com.component.busilib.constans.GameModeType;
import com.module.playways.BaseRoomData;
import com.module.playways.RoomDataUtils;
import com.module.playways.rank.msg.event.ExitGameEvent;
import com.module.playways.rank.prepare.model.BaseRoundInfoModel;
import com.module.playways.rank.room.event.PkSomeOneOnlineChangeEvent;
import com.module.playways.rank.room.model.RankGameConfigModel;
import com.module.playways.rank.prepare.model.PlayerInfoModel;
import com.module.playways.rank.room.model.RankPlayerInfoModel;
import com.module.playways.rank.room.model.RankRoundInfoModel;
import com.module.playways.rank.room.event.PkMyBurstSuccessEvent;
import com.module.playways.rank.room.event.PkMyLightOffSuccessEvent;
import com.module.playways.rank.room.event.RoundInfoChangeEvent;
import com.module.playways.rank.room.model.RecordData;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RankRoomData extends BaseRoomData<RankRoundInfoModel> {

    protected List<RankRoundInfoModel> mRoundInfoModelList;//所有的轮次信息

    protected RecordData mRecordData; // PK赛的结果信息

    protected List<RankPlayerInfoModel> mPlayerInfoList;//选手信息

    protected RankGameConfigModel mGameConfigModel;// 配置信息

    protected int mLeftBaoLightTimes; //剩余爆灯次数
    protected int mLeftMieLightTimes; //剩余灭灯次数
    protected int mSongLineNum;// 歌词行数
    protected int mCurSongTotalScore; // 歌曲部分演唱的累计总分
    protected long mSingBeginTs;// 本人开始演唱的时间戳

    @Override
    public int getGameType() {
        return GameModeType.GAME_MODE_CLASSIC_RANK;
    }

    /**
     * 检查轮次信息是否需要更新
     */
    @Override
    public void checkRoundInEachMode() {
        MyLog.d(TAG, "checkRound mExcpectRoundInfo=" + mExpectRoundInfo + " mRealRoundInfo=" + mRealRoundInfo);
        if (mIsGameFinish) {
            MyLog.d(TAG, "游戏结束了，不需要再check");
            return;
        }
        if (mExpectRoundInfo == null) {
            // 结束状态了
            if (mRealRoundInfo != null) {
                RankRoundInfoModel lastRoundInfoModel = mRealRoundInfo;
                mRealRoundInfo = null;
                EventBus.getDefault().post(new RoundInfoChangeEvent(false, lastRoundInfoModel));
            }
            return;
        }
        if (!RoomDataUtils.roundInfoEqual(mExpectRoundInfo, mRealRoundInfo)) {
            // 轮次需要更新了
            RankRoundInfoModel lastRoundInfoModel = mRealRoundInfo;
            mRealRoundInfo = mExpectRoundInfo;
            if (mRealRoundInfo.getUserID() == UserAccountManager.getInstance().getUuidAsLong()) {
                // 轮到自己唱了。开始发心跳，开始倒计时，3秒后 开始开始混伴奏，开始解除引擎mute，
                EventBus.getDefault().post(new RoundInfoChangeEvent(true, lastRoundInfoModel));
            } else {
                // 别人唱，本人的引擎mute，取消本人心跳。监听他人的引擎是否 unmute,开始混制歌词
                EventBus.getDefault().post(new RoundInfoChangeEvent(false, lastRoundInfoModel));
            }
        }
    }

    public void setRecordData(RecordData recordData) {
        mRecordData = recordData;
    }

    public RecordData getRecordData() {
        return mRecordData;
    }

    public int getLeftBurstLightTimes() {
        return mLeftBaoLightTimes;
    }

    public void consumeBurstLightTimes(BaseRoundInfoModel which) {
        mLeftBaoLightTimes = mLeftBaoLightTimes - 1;
        EventBus.getDefault().post(new PkMyBurstSuccessEvent(which));
    }

    public int getLeftLightOffTimes() {
        return mLeftMieLightTimes;
    }

    public void consumeLightOffTimes(BaseRoundInfoModel which) {
        mLeftMieLightTimes = mLeftMieLightTimes - 1;
        EventBus.getDefault().post(new PkMyLightOffSuccessEvent(which));
    }

    public RankGameConfigModel getGameConfigModel() {
        return mGameConfigModel;
    }

    public void setGameConfigModel(RankGameConfigModel gameConfigModel) {
        mGameConfigModel = gameConfigModel;
        mLeftMieLightTimes = mGameConfigModel.getpKMaxShowMLightTimes();
        mLeftBaoLightTimes = mGameConfigModel.getpKMaxShowBLightTimes();
    }

    public void setSongLineNum(int songLineNum) {
        mSongLineNum = songLineNum;
    }

    public int getSongLineNum() {
        return mSongLineNum;
    }

    public int getCurSongTotalScore() {
        return mCurSongTotalScore;
    }

    public void setCurSongTotalScore(int curSongTotalScore) {
        mCurSongTotalScore = curSongTotalScore;
    }

    public void setSingBeginTs(long singBeginTs) {
        mSingBeginTs = singBeginTs;
    }

    public long getSingBeginTs() {
        return mSingBeginTs;
    }

    public void setPlayerInfoList(List<RankPlayerInfoModel> playerInfoList) {
        mPlayerInfoList = playerInfoList;
    }

    public List<RankPlayerInfoModel> getPlayerInfoList() {
        return mPlayerInfoList;
    }

    public RankPlayerInfoModel getPlayerInfoModel(int userID) {
        if (userID == 0) {
            return null;
        }
        if (mPlayerInfoList == null) {
            return null;
        }
        for (RankPlayerInfoModel playerInfo : mPlayerInfoList) {
            if (playerInfo.getUserInfo().getUserId() == userID) {
                return playerInfo;
            }
        }
        return null;
    }

    public void setOnline(int userID, boolean online) {
        if (mPlayerInfoList != null) {
            for (RankPlayerInfoModel playerInfo : mPlayerInfoList) {
                if (playerInfo.getUserInfo().getUserId() == userID) {
                    if(playerInfo.isOnline() != online){
                        playerInfo.setOnline(online);
                            EventBus.getDefault().post(new PkSomeOneOnlineChangeEvent(playerInfo));
                    }
                }
            }
        }
    }

    public PlayerInfoModel getAiJudgeInfo() {
        if (getPlayerInfoList() != null && getPlayerInfoList().size() > 0) {
            for (RankPlayerInfoModel playerInfoModel : getPlayerInfoList()) {
                if (playerInfoModel.isAI()) {
                    return playerInfoModel;
                }
            }
        }
        return null;
    }

    public List<RankRoundInfoModel> getRoundInfoModelList() {
        return mRoundInfoModelList;
    }

    public void setRoundInfoModelList(List<RankRoundInfoModel> roundInfoModelList) {
        mRoundInfoModelList = roundInfoModelList;
    }
}
