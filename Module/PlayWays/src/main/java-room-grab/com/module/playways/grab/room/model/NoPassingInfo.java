package com.module.playways.grab.room.model;

import com.zq.live.proto.Room.NoPassSingInfo;

public class NoPassingInfo {
    int userID;
    long timeMs;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public long getTimeMs() {
        return timeMs;
    }

    public void setTimeMs(long timeMs) {
        this.timeMs = timeMs;
    }

    @Override
    public String toString() {
        return "NoPassingInfo{" +
                "userID=" + userID +
                ", timeMs=" + timeMs +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NoPassingInfo that = (NoPassingInfo) object;
        return userID == that.userID;
    }

    @Override
    public int hashCode() {
        return userID;
    }

    public static NoPassingInfo parse(NoPassSingInfo pb) {
        NoPassingInfo noPassingInfo = new NoPassingInfo();
        noPassingInfo.setUserID(pb.getUserID());
        noPassingInfo.setTimeMs(pb.getTimeMs());
        return noPassingInfo;
    }
}