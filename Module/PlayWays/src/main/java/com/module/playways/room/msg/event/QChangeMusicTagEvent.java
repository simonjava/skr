package com.module.playways.room.msg.event;

import com.module.playways.room.msg.BasePushInfo;
import com.zq.live.proto.GrabRoom.QChangeMusicTag;

public class QChangeMusicTagEvent {
    public BasePushInfo info;
    int tagID; //分类标识
    String tagName = ""; //分类名称

    public QChangeMusicTagEvent(BasePushInfo info, QChangeMusicTag event) {
        this.info = info;
        this.tagID = event.getTagID();
        this.tagName = event.getTagName();
    }

    public int getTagID() {
        return tagID;
    }

    public String getTagName() {
        return tagName;
    }

    @Override
    public String toString() {
        return "QChangeMusicTagEvent{" +
                "info=" + info +
                ", tagID=" + tagID +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}
