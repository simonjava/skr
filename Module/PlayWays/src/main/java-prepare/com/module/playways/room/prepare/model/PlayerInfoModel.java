package com.module.playways.room.prepare.model;

import com.common.core.userinfo.model.UserInfoModel;
import com.module.playways.grab.room.event.SomeOneOnlineChangeEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.Objects;

public abstract class PlayerInfoModel implements Serializable {
    protected boolean isOnline = true;//是否在线
    protected boolean isSkrer;//是否是机器人
    protected int userID;
    protected UserInfoModel userInfo;

    /**
     * 以下是只在排位赛才会用到的
     **/

    public void setSkrer(boolean skrer) {
        isSkrer = skrer;
    }

    public int getUserID() {
        if (userID == 0) {
            if (userInfo != null) {
                return userInfo.getUserId();
            } else {
                return 0;
            }
        } else {
            return userID;
        }
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    public boolean isSkrer() {
        return isSkrer;
    }

    public UserInfoModel getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoModel userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        if (this.isOnline != online) {
            this.isOnline = online;
            EventBus.getDefault().post(new SomeOneOnlineChangeEvent(this));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerInfoModel that = (PlayerInfoModel) o;

        if (userID != that.userID) return false;
        return Objects.equals(userInfo, that.userInfo);
    }

    @Override
    public int hashCode() {
        int result = userID;
        result = 31 * result + (userInfo != null ? userInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "userInfo=" + userInfo +
                ", isSkrer=" + isSkrer +
                '}';
    }


}
