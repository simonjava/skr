package com.module.playways.room.prepare.model;

import java.io.Serializable;

public class MatchIconModel implements Serializable {
    int sex;
    String avatarURL;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }
}
