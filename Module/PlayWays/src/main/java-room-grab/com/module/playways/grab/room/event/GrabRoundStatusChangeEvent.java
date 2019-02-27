package com.module.playways.grab.room.event;

import com.module.playways.grab.room.model.GrabRoundInfoModel;

public class GrabRoundStatusChangeEvent {
    public int oldStatus;
    public GrabRoundInfoModel roundInfo;

    public GrabRoundStatusChangeEvent(GrabRoundInfoModel roundInfo, int oldStatus) {
        this.roundInfo = roundInfo;
        this.oldStatus = oldStatus;
    }

    @Override
    public String toString() {
        return "GrabRoundStatusChangeEvent{" +
                "oldStatus=" + oldStatus +
                ", roundInfo=" + roundInfo +
                '}';
    }
}
