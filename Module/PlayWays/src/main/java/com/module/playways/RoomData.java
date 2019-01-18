package com.module.playways;

import com.common.core.account.UserAccountManager;
import com.common.core.userinfo.model.UserInfoModel;
import com.common.log.MyLog;
import com.module.playways.rank.prepare.model.OnlineInfoModel;
import com.module.playways.rank.prepare.model.PlayerInfoModel;
import com.module.playways.rank.prepare.model.RoundInfoModel;
import com.module.playways.rank.room.event.RoundInfoChangeEvent;
import com.module.playways.rank.room.model.RankDataUtils;
import com.module.playways.rank.song.model.SongModel;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;


/**
 * 房间内所有数据的聚合类
 * 每种模式的房间内状态信息都由其存储
 */
public class RoomData implements Serializable {
    public final static String TAG = "RoomData";

    public static final int SYSTEM_ID = 1;

    public final static String READY_GO_WEBP_URL = "http://bucket-oss-inframe.oss-cn-beijing.aliyuncs.com/ready_go4.webp";
    public final static String READY_GO_SVGA_URL = "https://bucket-oss-inframe.oss-cn-beijing.aliyuncs.com/android_resource/sige_go.svga";
    public final static String ROOM_STAGE_SVGA = "https://bucket-oss-inframe.oss-cn-beijing.aliyuncs.com/android_resource/main_stage_people.svga";
    public final static String ROOM_SPECAIL_EMOJI_DABIAN = "https://bucket-oss-inframe.oss-cn-beijing.aliyuncs.com/android_resource/emoji_bianbian.svga";
    public final static String ROOM_SPECAIL_EMOJI_AIXIN = "https://bucket-oss-inframe.oss-cn-beijing.aliyuncs.com/android_resource/emoji_love.svga";
    public static final String AUDIO_FOR_AI_PATH = "audioforai.aac";
    public static final String MATCHING_SCORE_FOR_AI_PATH = "matchingscore.json";

    private int mGameType; // 游戏类型

    private int mGameId; // 房间id

    private String mSysAvatar; // 系统头像
    /**
     * 当要拿服务器时间和本地时间比较时，请将服务器时间加上这个矫正值
     * 如
     * if(System.currentTimeMillis() > mGameStartTs + mShiftts){
     * <p>
     * }
     */
    private int mShiftTs;// 本地时间比服务器快多少毫秒，比如快1秒，mShiftTs = 1000;

    private long mGameCreateTs;// 游戏创建时间,服务器的

    private long mGameStartTs;// 游戏开始时间,服务器的

    private long mGameOverTs;// 游戏结束时间,服务器的

    private long mLastSyncTs;// 上次同步服务器状态时间,服务器的

    private SongModel mSongModel; // 歌曲信息

    private List<RoundInfoModel> mRoundInfoModelList;//所有的轮次信息

    private RoundInfoModel mExpectRoundInfo;// 按理的 期望的当前的轮次

    private RoundInfoModel mRealRoundInfo;// 实际的当前轮次信息

    private List<OnlineInfoModel> mOnlineInfoList;//所有的用户在线信息

    private List<PlayerInfoModel> mPlayerInfoList;//选手信息

    private List<SongModel> mSongModelList;//选手信息

    private volatile boolean mIsGameFinish = false;

    /**
     * 检查轮次信息是否需要更新
     */
    public void checkRound() {
        MyLog.d(TAG, "checkRound mExcpectRoundInfo=" + mExpectRoundInfo + " mRealRoundInfo=" + mRealRoundInfo);
        if (mIsGameFinish) {
            MyLog.d(TAG, "游戏结束了，不需要再check");
            return;
        }
        if (mExpectRoundInfo == null) {
            // 结束状态了
            if (mRealRoundInfo != null) {
                RoundInfoModel lastRoundInfoModel = mRealRoundInfo;
                mRealRoundInfo = null;
                EventBus.getDefault().post(new RoundInfoChangeEvent(false, lastRoundInfoModel));
            }
            return;
        }
        if (!RankDataUtils.roundInfoEqual(mExpectRoundInfo, mRealRoundInfo)) {
            // 轮次需要更新了
            RoundInfoModel lastRoundInfoModel = mRealRoundInfo;
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

    public List<SongModel> getSongModelList() {
        return mSongModelList;
    }

    public void setSongModelList(List<SongModel> songModelList) {
        mSongModelList = songModelList;
    }

    public void setIsGameFinish(boolean isGameFinish) {
        this.mIsGameFinish = isGameFinish;
    }

    public boolean isIsGameFinish() {
        return mIsGameFinish;
    }

    public int getGameType() {
        return mGameType;
    }

    public void setGameType(int gameType) {
        mGameType = gameType;
    }

    public int getGameId() {
        return mGameId;
    }

    public void setGameId(int gameId) {
        mGameId = gameId;
    }

    public String getSysAvatar() {
        return mSysAvatar;
    }

    public void setSysAvatar(String sysAvatar) {
        mSysAvatar = sysAvatar;
    }

    public int getShiftTs() {
        return mShiftTs;
    }

    public void setShiftTs(int shiftTs) {
        mShiftTs = shiftTs;
    }

    public long getGameCreateTs() {
        return mGameCreateTs;
    }

    public void setGameCreateTs(long gameCreateTs) {
        mGameCreateTs = gameCreateTs;
    }

    public long getGameStartTs() {
        return mGameStartTs;
    }

    public void setGameStartTs(long gameStartTs) {
        mGameStartTs = gameStartTs;
    }

    public long getGameOverTs() {
        return mGameOverTs;
    }

    public void setGameOverTs(long gameOverTs) {
        mGameOverTs = gameOverTs;
    }

    public long getLastSyncTs() {
        return mLastSyncTs;
    }

    public void setLastSyncTs(long lastSyncTs) {
        mLastSyncTs = lastSyncTs;
    }

    public SongModel getSongModel() {
        return mSongModel;
    }

    public void setSongModel(SongModel songModel) {
        mSongModel = songModel;
    }

    public List<RoundInfoModel> getRoundInfoModelList() {
        return mRoundInfoModelList;
    }

    public void setRoundInfoModelList(List<RoundInfoModel> roundInfoModelList) {
        mRoundInfoModelList = roundInfoModelList;
    }

    public RoundInfoModel getExpectRoundInfo() {
        return mExpectRoundInfo;
    }

    public void setExpectRoundInfo(RoundInfoModel expectRoundInfo) {
        mExpectRoundInfo = expectRoundInfo;
    }

    public RoundInfoModel getRealRoundInfo() {
        return mRealRoundInfo;
    }

    public void setRealRoundInfo(RoundInfoModel realRoundInfo) {
        mRealRoundInfo = realRoundInfo;
    }

    public List<OnlineInfoModel> getOnlineInfoList() {
        return mOnlineInfoList;
    }

    public void setOnlineInfoList(List<OnlineInfoModel> onlineInfoList) {
        mOnlineInfoList = onlineInfoList;
    }

    public void setPlayerInfoList(List<PlayerInfoModel> playerInfoList) {
        mPlayerInfoList = playerInfoList;
    }

    public List<PlayerInfoModel> getPlayerInfoList() {
        return mPlayerInfoList;
    }

    public UserInfoModel getUserInfo(int userID) {
        if (userID == 0) {
            return null;
        }
        if (mPlayerInfoList == null) {
            return null;
        }
        for (PlayerInfoModel playerInfo : mPlayerInfoList) {
            if (playerInfo.getUserInfo().getUserId() == userID) {
                return playerInfo.getUserInfo();
            }
        }

        return null;
    }

    public int getRealRoundSeq(){
        if(mRealRoundInfo != null){
            return mRealRoundInfo.getRoundSeq();
        }

        return -1;
    }
    @Override
    public String toString() {
        return "RoomData{" +
                "mGameType=" + mGameType +
                ", mGameId=" + mGameId +
                ", mSysAvatar='" + mSysAvatar + '\'' +
                ", mShiftTs=" + mShiftTs +
                ", mGameCreateTs=" + mGameCreateTs +
                ", mGameStartTs=" + mGameStartTs +
                ", mGameOverTs=" + mGameOverTs +
                ", mLastSyncTs=" + mLastSyncTs +
                ", mSongModel=" + mSongModel +
                ", mRoundInfoModelList=" + mRoundInfoModelList +
                ", mExpectRoundInfo=" + mExpectRoundInfo +
                ", mRealRoundInfo=" + mRealRoundInfo +
                ", mOnlineInfoList=" + mOnlineInfoList +
                ", mPlayerInfoList=" + mPlayerInfoList +
                ", mIsGameFinish=" + mIsGameFinish +
                '}';
    }
}
