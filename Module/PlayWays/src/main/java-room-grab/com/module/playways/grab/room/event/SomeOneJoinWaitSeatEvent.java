package com.module.playways.grab.room.event;

import com.module.playways.grab.room.model.GrabPlayerInfoModel;

/**
 * 加入观众席
 */
public class SomeOneJoinWaitSeatEvent {
    GrabPlayerInfoModel mPlayerInfoModel;

    public SomeOneJoinWaitSeatEvent(GrabPlayerInfoModel playerInfoModel) {
        this.mPlayerInfoModel = playerInfoModel;
    }

    public GrabPlayerInfoModel getPlayerInfoModel() {
        return mPlayerInfoModel;
    }
}
