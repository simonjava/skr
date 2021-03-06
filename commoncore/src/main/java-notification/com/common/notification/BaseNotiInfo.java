package com.common.notification;

import com.zq.live.proto.Common.UserInfo;
import com.zq.live.proto.Notification.EMsgPosType;
import com.zq.live.proto.Notification.NotificationMsg;

import java.io.Serializable;

public class BaseNotiInfo implements Serializable {

    private long timeMs;          //房间消息产生时间，单位毫秒
    private int roomID;           //房间ID
    private long no;              //房间内的消息序号，每个房间有自己的消息序号,不存在则系统生成,一般情况下调用方不必设置
    private EMsgPosType posType;  //消息显示位置类型
    private UserInfo sender;      //发送者简要信息

    public static BaseNotiInfo parse(NotificationMsg msg) {
        BaseNotiInfo info = new BaseNotiInfo();
        info.setTimeMs(msg.getTimeMs());
        info.setRoomID(msg.getRoomID());
        info.setNo(msg.getNo());
        info.setPosType(msg.getPosType());
        info.setSender(msg.getSender());
        return info;
    }


    public long getTimeMs() {
        return timeMs;
    }

    public void setTimeMs(long timeMs) {
        this.timeMs = timeMs;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public EMsgPosType getPosType() {
        return posType;
    }

    public void setPosType(EMsgPosType posType) {
        this.posType = posType;
    }

    public UserInfo getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender = sender;
    }

}
