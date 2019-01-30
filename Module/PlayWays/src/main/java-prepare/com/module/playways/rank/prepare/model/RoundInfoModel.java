package com.module.playways.rank.prepare.model;

import com.common.log.MyLog;
import com.module.playways.grab.room.event.GrabRoundStatusChangeEvent;
import com.module.playways.grab.room.event.SomeOneGrabEvent;
import com.module.playways.grab.room.event.SomeOneLightOffEvent;
import com.module.playways.rank.song.model.SongModel;
import com.zq.live.proto.Room.NoPassSingInfo;
import com.zq.live.proto.Room.QRoundInfo;
import com.zq.live.proto.Room.RoundInfo;
import com.zq.live.proto.Room.WantSingInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RoundInfoModel implements Serializable {
    public static final int TYPE_RANK = 1;
    public static final int TYPE_GRAB = 2;

    public static final int STATUS_INIT = 1;
    public static final int STATUS_GRAB = 2;
    public static final int STATUS_SING = 3;
    public static final int STATUS_OVER = 4;
    /**
     * userID : 7
     * playbookID : 1
     * roundSeq : 1
     * singBeginMs : 3000
     * singEndMs : 341000
     */
    private int type = TYPE_RANK;
    private int userID;
    private int playbookID;   //songModelId
    private SongModel songModel;//本轮次要唱的歌儿的详细信息
    private int roundSeq;
    private int singBeginMs;
    private int singEndMs;
    private long startTs;// 开始时间，服务器的
    private long endTs;// 结束时间，服务器的
    private int sysScore;//本轮系统打分，先搞个默认60分
    private boolean hasSing = false;

    private int status = STATUS_INIT;// 轮次状态，在一唱到底中使用

    /**
     * 0未知
     * 1上个轮次结束
     * 2没人抢唱
     * 3当前玩家退出
     * 4多人灭灯
     */
    private int overReason; // 结束的原因

    //0未知
    //1有种优秀叫一唱到底（全部唱完）
    //2有种结束叫刚刚开始（t<30%）
    //3有份悲伤叫都没及格(30%<=t <60%)
    //4有种遗憾叫明明可以（60%<=t<90%）
    //5有种可惜叫我觉得你行（90%<=t<=100%)
    private int resultType; // 结果类型

    private Set<Integer> hasGrabUserSet = new HashSet<>(); //已经抢了的人

    private Set<Integer> hasLightOffUserSet = new HashSet<>();//已经灭灯的人

    public RoundInfoModel() {

    }


    public RoundInfoModel(int type) {
        this.type = type;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOverReason() {
        return overReason;
    }

    public void setOverReason(int overReason) {
        this.overReason = overReason;
    }

    public SongModel getSongModel() {
        return songModel;
    }

    public void setSongModel(SongModel songModel) {
        this.songModel = songModel;
    }

    public Set<Integer> getHasGrabUserSet() {
        return hasGrabUserSet;
    }

    public Set<Integer> getHasLightOffUserSet() {
        return hasLightOffUserSet;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPlaybookID() {
        return playbookID;
    }

    public void setPlaybookID(int playbookID) {
        this.playbookID = playbookID;
    }

    public int getRoundSeq() {
        return roundSeq;
    }

    public void setRoundSeq(int roundSeq) {
        this.roundSeq = roundSeq;
    }

    public int getSingBeginMs() {
        return singBeginMs;
    }

    public void setSingBeginMs(int singBeginMs) {
        this.singBeginMs = singBeginMs;
    }

    public int getSingEndMs() {
        return singEndMs;
    }

    public void setSingEndMs(int singEndMs) {
        this.singEndMs = singEndMs;
    }

    public long getStartTs() {
        return startTs;
    }

    public void setStartTs(long startTs) {
        this.startTs = startTs;
    }

    public long getEndTs() {
        return endTs;
    }

    public void setEndTs(long endTs) {
        this.endTs = endTs;
    }

    public int getSysScore() {
        return sysScore;
    }

    public void setSysScore(int sysScore) {
        this.sysScore = sysScore;
    }

    public boolean isHasSing() {
        return hasSing;
    }

    public void setHasSing(boolean hasSing) {
        this.hasSing = hasSing;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public static RoundInfoModel parseFromRoundInfo(RoundInfo roundInfo) {
        RoundInfoModel roundInfoModel = new RoundInfoModel(TYPE_RANK);
        roundInfoModel.setUserID(roundInfo.getUserID());
        roundInfoModel.setPlaybookID(roundInfo.getPlaybookID());
        roundInfoModel.setRoundSeq(roundInfo.getRoundSeq());
        roundInfoModel.setSingBeginMs(roundInfo.getSingBeginMs());
        roundInfoModel.setSingEndMs(roundInfo.getSingEndMs());
        return roundInfoModel;
    }

    public static RoundInfoModel parseFromRoundInfo(QRoundInfo roundInfo) {
        RoundInfoModel roundInfoModel = new RoundInfoModel(TYPE_GRAB);
        roundInfoModel.setUserID(roundInfo.getUserID());
        roundInfoModel.setPlaybookID(roundInfo.getPlaybookID());
        roundInfoModel.setRoundSeq(roundInfo.getRoundSeq());
        roundInfoModel.setSingBeginMs(roundInfo.getSingBeginMs());
        roundInfoModel.setSingEndMs(roundInfo.getSingEndMs());
        roundInfoModel.setStatus(roundInfo.getStatus().getValue());
        for (WantSingInfo wantSingInfo : roundInfo.getWantSingInfosList()) {
            roundInfoModel.addGrabUid(false, wantSingInfo.getUserID());
        }
        for (NoPassSingInfo noPassSingInfo : roundInfo.getNoPassSingInfosList()) {
            roundInfoModel.addLightOffUid(false, noPassSingInfo.getUserID());
        }
        roundInfoModel.setOverReason(roundInfo.getOverReason().getValue());
        roundInfoModel.setResultType(roundInfo.getResultType().getValue());
        return roundInfoModel;
    }

    public void tryUpdateByRoundInfoModel(RoundInfoModel roundInfo, boolean notify) {
        if (roundInfo == null) {
            MyLog.e("JsonRoundInfo RoundInfo == null");
            return;
        }
        this.setUserID(roundInfo.getUserID());
        this.setPlaybookID(roundInfo.getPlaybookID());
        this.setRoundSeq(roundInfo.getRoundSeq());
        this.setSingBeginMs(roundInfo.getSingBeginMs());
        this.setSingEndMs(roundInfo.getSingEndMs());
        //TODO 抢 灭 结束原因 补全
        for (int uid : roundInfo.getHasGrabUserSet()) {
            addGrabUid(notify, uid);
        }
        for (int uid : roundInfo.getHasLightOffUserSet()) {
            addLightOffUid(notify, uid);
        }
        if (roundInfo.getOverReason() > 0) {
            this.setOverReason(roundInfo.getOverReason());
        }
        if (roundInfo.getResultType() > 0) {
            this.setResultType(roundInfo.getResultType());
        }
        updateStatus(notify, roundInfo.getStatus());
        return;
    }

    public void addGrabUid(boolean notify, int userID) {
        if (!hasGrabUserSet.contains(userID)) {
            hasGrabUserSet.add(userID);
            if (notify) {
                SomeOneGrabEvent event = new SomeOneGrabEvent(userID, this);
                EventBus.getDefault().post(event);
            }
        }
    }

    public void addLightOffUid(boolean notify, Integer userID) {
        if (!hasLightOffUserSet.contains(userID)) {
            hasLightOffUserSet.add(userID);
            if (notify) {
                SomeOneLightOffEvent event = new SomeOneLightOffEvent(userID, this);
                EventBus.getDefault().post(event);
            }
        }
    }

    public void updateStatus(boolean notify, int statusGrab) {
        if (status < statusGrab) {
            int old = status;
            status = statusGrab;
            if (notify) {
                EventBus.getDefault().post(new GrabRoundStatusChangeEvent(this, old));
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        RoundInfoModel that = (RoundInfoModel) o;
        if (that == null) {
            return false;
        }

        if (this.type != that.type) {
            return false;
        }
        if (roundSeq != that.roundSeq) {
            return false;
        }
        if (playbookID != that.playbookID) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + roundSeq;
        result = 31 * result + playbookID;
        return result;
    }

    @Override
    public String toString() {
        return "RoundInfoModel{" +
                "type=" + type +
                ", userID=" + userID +
                ", playbookID=" + playbookID +
//                ", songModel=" + songModel.getItemName() +
                ", roundSeq=" + roundSeq +
                ", singBeginMs=" + singBeginMs +
                ", singEndMs=" + singEndMs +
                ", startTs=" + startTs +
                ", endTs=" + endTs +
                ", sysScore=" + sysScore +
                ", hasSing=" + hasSing +
                ", status=" + status +
                ", overReason=" + overReason +
                ", resultType=" + resultType +
                ", hasGrabUserSet=" + hasGrabUserSet +
                ", hasLightOffUserSet=" + hasLightOffUserSet +
                '}';
    }

}