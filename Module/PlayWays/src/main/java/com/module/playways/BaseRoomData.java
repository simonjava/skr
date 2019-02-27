package com.module.playways;

import com.common.core.account.UserAccountManager;
import com.common.core.userinfo.model.UserInfoModel;
import com.common.log.MyLog;
import com.module.playways.grab.room.event.GrabGameOverEvent;
import com.module.playways.grab.room.event.GrabRoundChangeEvent;
import com.module.playways.grab.room.model.GrabResultInfoModel;
import com.module.playways.rank.prepare.model.GameConfigModel;
import com.module.playways.rank.prepare.model.GrabRoundInfoModel;
import com.module.playways.rank.prepare.model.PlayerInfoModel;
import com.module.playways.rank.prepare.model.BaseRoundInfoModel;
import com.module.playways.rank.room.event.PkMyBurstSuccessEvent;
import com.module.playways.rank.room.event.PkMyLightOffSuccessEvent;
import com.module.playways.rank.room.event.RoundInfoChangeEvent;
import com.module.playways.rank.room.model.RecordData;
import com.module.playways.rank.song.model.SongModel;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;


/**
 * 房间内所有数据的聚合类
 * 每种模式的房间内状态信息都由其存储
 */
public abstract class BaseRoomData<T extends BaseRoundInfoModel> implements Serializable {
    public final static String TAG = "RoomData";

    public static final int SYSTEM_ID = 1;
    public static final String SYSTEM_AVATAR ="http://bucket-oss-inframe.oss-cn-beijing.aliyuncs.com/common/system_default.png"; //系统头像

    public final static String PK_MAIN_STAGE_WEBP = "http://res-static.inframe.mobi/app/pk_main_stage.webp";
    public final static String READY_GO_SVGA_URL = "http://res-static.inframe.mobi/app/sige_go.svga";
    public final static String ROOM_STAGE_SVGA = "http://res-static.inframe.mobi/app/main_stage_people.svga";
    public final static String ROOM_SPECAIL_EMOJI_DABIAN = "http://res-static.inframe.mobi/app/emoji_bianbian.svga";
    public final static String ROOM_SPECAIL_EMOJI_AIXIN = "http://res-static.inframe.mobi/app/emoji_love.svga";
    public static final String AUDIO_FOR_AI_PATH = "audioforai.aac";
    public static final String MATCHING_SCORE_FOR_AI_PATH = "matchingscore.json";



    protected int mGameId; // 房间id

    protected String mSysAvatar; // 系统头像

    /**
     * 当要拿服务器时间和本地时间比较时，请将服务器时间加上这个矫正值
     * 如
     * if(System.currentTimeMillis() > mGameStartTs + mShiftts){
     * <p>
     * }
     */
    protected int mShiftTs;// 本地时间比服务器快多少毫秒，比如快1秒，mShiftTs = 1000;

    protected long mGameCreateTs;// 游戏创建时间,服务器的

    protected long mGameStartTs;// 游戏开始时间,服务器的

    protected long mGameOverTs;// 游戏结束时间,服务器的

    protected long mLastSyncTs;// 上次同步服务器状态时间,服务器的

    protected SongModel mSongModel; // 歌曲信息

    protected List<T> mRoundInfoModelList;//所有的轮次信息

    protected T mExpectRoundInfo;// 按理的 期望的当前的轮次

    protected T mRealRoundInfo;// 实际的当前轮次信息

    protected List<PlayerInfoModel> mPlayerInfoList;//选手信息

    protected boolean mIsGameFinish = false; // 游戏开始了

    protected boolean mMute = false;//是否mute


    public abstract int getGameType();

    public abstract void checkRoundInEachMode();

    public void setIsGameFinish(boolean isGameFinish) {
        this.mIsGameFinish = isGameFinish;
    }

    public boolean isIsGameFinish() {
        return mIsGameFinish;
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

    public List<T> getRoundInfoModelList() {
        return mRoundInfoModelList;
    }

    public void setRoundInfoModelList(List<T> roundInfoModelList) {
        mRoundInfoModelList = roundInfoModelList;
    }

    public T getExpectRoundInfo() {
        return mExpectRoundInfo;
    }

    public void setExpectRoundInfo(T expectRoundInfo) {
        mExpectRoundInfo = expectRoundInfo;
    }

    public <T extends BaseRoundInfoModel> T getRealRoundInfo() {
        return (T) mRealRoundInfo;
    }

    public void setRealRoundInfo(T realRoundInfo) {
        mRealRoundInfo = realRoundInfo;
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

    public PlayerInfoModel getPlayerInfoModel(int userID) {
        if (userID == 0) {
            return null;
        }
        if (mPlayerInfoList == null) {
            return null;
        }
        for (PlayerInfoModel playerInfo : mPlayerInfoList) {
            if (playerInfo.getUserInfo().getUserId() == userID) {
                return playerInfo;
            }
        }

        return null;
    }

    public int getRealRoundSeq() {
        if (mRealRoundInfo != null) {
            return mRealRoundInfo.getRoundSeq();
        }

        return -1;
    }

    public boolean isMute() {
        return mMute;
    }

    public void setMute(boolean mute) {
        mMute = mute;
    }

    public void setOnline(int userID, boolean online) {
        if (mPlayerInfoList != null) {
            for (PlayerInfoModel playerInfo : mPlayerInfoList) {
                if (playerInfo.getUserInfo().getUserId() == userID) {
                    playerInfo.setOnline(online);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "RoomData{" +
                "mGameType=" + getGameType() +
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
                ", mPlayerInfoList=" + mPlayerInfoList +
                ", mIsGameFinish=" + mIsGameFinish +
                ", mMute=" + mMute +
                '}';
    }
}
