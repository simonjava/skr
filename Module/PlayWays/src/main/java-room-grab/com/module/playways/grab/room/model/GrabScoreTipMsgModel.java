package com.module.playways.grab.room.model;

import com.zq.live.proto.GrabRoom.QScoreTipMsg;

import java.io.Serializable;

public class GrabScoreTipMsgModel implements Serializable {
    int tipType;
    String tipDesc;
    int fromScore;
    int toScore;

    public static GrabScoreTipMsgModel parse(QScoreTipMsg scoreTipMsg) {
        GrabScoreTipMsgModel grabScoreTipMsgModel = new GrabScoreTipMsgModel();
        grabScoreTipMsgModel.setFromScore(scoreTipMsg.getFromScore());
        grabScoreTipMsgModel.setToScore(scoreTipMsg.getToScore());
        grabScoreTipMsgModel.setTipDesc(scoreTipMsg.getTipDesc());
        grabScoreTipMsgModel.setTipType(scoreTipMsg.getTipType().getValue());
        return grabScoreTipMsgModel;
    }


    public int getTipType() {
        return tipType;
    }

    public void setTipType(int tipType) {
        this.tipType = tipType;
    }

    public String getTipDesc() {
        return tipDesc;
    }

    public void setTipDesc(String tipDesc) {
        this.tipDesc = tipDesc;
    }

    public int getFromScore() {
        return fromScore;
    }

    public void setFromScore(int fromScore) {
        this.fromScore = fromScore;
    }

    public int getToScore() {
        return toScore;
    }

    public void setToScore(int toScore) {
        this.toScore = toScore;
    }

}
