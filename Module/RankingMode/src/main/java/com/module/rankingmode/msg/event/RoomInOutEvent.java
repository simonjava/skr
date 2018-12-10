package com.module.rankingmode.msg.event;

import com.module.rankingmode.msg.BasePushInfo;

public class RoomInOutEvent {

    boolean isEnter;
    BasePushInfo info;

    public RoomInOutEvent(BasePushInfo info, boolean isEnter){
        this.info = info;
        this.isEnter = isEnter;
    }
}
