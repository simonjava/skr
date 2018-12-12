package com.module.rankingmode.prepare.model;

import com.common.log.MyLog;
import com.zq.live.proto.Room.JoinInfo;
import com.zq.live.proto.Room.JoinNoticeMsg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JsonGameInfo implements Serializable {
    /**
     * jsonJoinInfo : [{"userID":30,"joinSeq":1,"joinTimeMs":1544439278416},{"userID":20,"joinSeq":2,"joinTimeMs":1544439286547},{"userID":10,"joinSeq":3,"joinTimeMs":1544441838343}]
     * hasJoinedUserCnt : 3
     * readyClockResMs : -7986350
     */

    private int hasJoinedUserCnt;
    private int readyClockResMs;
    private List<JsonJoinInfo> jsonJoinInfo;

    public int getHasJoinedUserCnt() {
        return hasJoinedUserCnt;
    }

    public void setHasJoinedUserCnt(int hasJoinedUserCnt) {
        this.hasJoinedUserCnt = hasJoinedUserCnt;
    }

    public int getReadyClockResMs() {
        return readyClockResMs;
    }

    public void setReadyClockResMs(int readyClockResMs) {
        this.readyClockResMs = readyClockResMs;
    }

    public List<JsonJoinInfo> getJsonJoinInfo() {
        return jsonJoinInfo;
    }

    public void setJsonJoinInfo(List<JsonJoinInfo> jsonJoinInfo) {
        this.jsonJoinInfo = jsonJoinInfo;
    }

    public void parse(JoinNoticeMsg joinNoticeMsg) {
        if (joinNoticeMsg == null) {
            MyLog.e("JsonGameInfo joinNoticeMsg == null");
            return;
        }
        this.setHasJoinedUserCnt(joinNoticeMsg.getHasJoinedUserCnt());
        this.setReadyClockResMs(joinNoticeMsg.getReadyClockResMs());

        List<JsonJoinInfo> jsonJoinInfos = new ArrayList<>();
        for (JoinInfo info : joinNoticeMsg.getJoinInfoList()) {
            JsonJoinInfo jsonJoinInfo = new JsonJoinInfo();
            jsonJoinInfo.parse(info);
            jsonJoinInfos.add(jsonJoinInfo);
        }
        this.setJsonJoinInfo(jsonJoinInfos);

        return;
    }
}
