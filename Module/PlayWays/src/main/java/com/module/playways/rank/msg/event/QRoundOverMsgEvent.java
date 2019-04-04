// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Room.proto
package com.module.playways.rank.msg.event;

import com.common.core.myinfo.MyUserInfoManager;
import com.module.playways.rank.msg.BasePushInfo;
import com.module.playways.grab.room.model.GrabRoundInfoModel;
import com.zq.live.proto.Room.QRoundOverMsg;
import com.zq.live.proto.Room.QUserCoin;

public final class QRoundOverMsgEvent {
    public BasePushInfo info;
    /**
     * 本轮次结束的毫秒时间戳
     */
    public Long roundOverTimeMs;

    /**
     * 当前轮次信息
     */
    public GrabRoundInfoModel currentRound;

    /**
     * 下个轮次信息
     */
    public GrabRoundInfoModel nextRound;

    public int myCoin = -1;

    public int totalRoundNum;

    public QRoundOverMsgEvent(BasePushInfo info, QRoundOverMsg qRoundOverMsg) {
        this.info = info;
        this.roundOverTimeMs = qRoundOverMsg.getRoundOverTimeMs();
        this.currentRound = GrabRoundInfoModel.parseFromRoundInfo(qRoundOverMsg.getCurrentRound());
        this.nextRound = GrabRoundInfoModel.parseFromRoundInfo(qRoundOverMsg.getNextRound());
        for(QUserCoin c :qRoundOverMsg.getQUserCoinList()){
            if(c.getUserID()== MyUserInfoManager.getInstance().getUid()){
                long a = c.getCoin();
                myCoin = (int) a;
            }
        }
        this.totalRoundNum = qRoundOverMsg.getTotalGameRoundSeq();
    }

    public BasePushInfo getInfo() {
        return info;
    }

    public Long getRoundOverTimeMs() {
        return roundOverTimeMs;
    }

    public GrabRoundInfoModel getCurrentRound() {
        return currentRound;
    }

    public GrabRoundInfoModel getNextRound() {
        return nextRound;
    }

    @Override
    public String toString() {
        return "QRoundOverMsgEvent{" +
                "roundOverTimeMs=" + roundOverTimeMs +
                ", currentRoundSeq=" + currentRound.getRoundSeq() +
                ", currentRoundOverReason=" + currentRound.getOverReason() +
                ", nextRound=" + nextRound +
                '}';
    }
}
