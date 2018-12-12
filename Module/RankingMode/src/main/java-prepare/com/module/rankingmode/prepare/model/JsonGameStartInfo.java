package com.module.rankingmode.prepare.model;

import com.common.log.MyLog;
import com.zq.live.proto.Room.GameStartInfo;

import java.io.Serializable;

public class JsonGameStartInfo implements Serializable {
    /**
     * startTimeMs : 1544586876239
     * startPassedMs : 3119
     */

    private long startTimeMs;
    private long startPassedMs;

    public long getStartTimeMs() {
        return startTimeMs;
    }

    public void setStartTimeMs(long startTimeMs) {
        this.startTimeMs = startTimeMs;
    }

    public long getStartPassedMs() {
        return startPassedMs;
    }

    public void setStartPassedMs(long startPassedMs) {
        this.startPassedMs = startPassedMs;
    }

    public void parse(GameStartInfo gameStartInfo) {
        if (gameStartInfo == null) {
            MyLog.e("JsonGameStartInfo GameStartInfo == null");
            return;
        }

        this.setStartTimeMs(gameStartInfo.getStartTimeMs());
        this.setStartPassedMs(gameStartInfo.getStartPassedMs());
        return;
    }
}