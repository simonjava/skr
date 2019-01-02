package com.common.core.userinfo;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.core.myinfo.Location;

import java.io.Serializable;

// TODO: 2019/1/2 该类会作为json来解析，不要改变量名 
public class UserInfoModel implements Serializable {

    /**
     * userID : 11
     * nickname : 不可抗力好吧
     * sex : 1
     * birthday : 1993-05-06
     * avatar : http://bucket-oss-inframe.oss-cn-beijing.aliyuncs.com/f488412aad91c18e.jpg
     * signature : 怎么的了哈哈
     * location : {"province":"","city":"","district":""}
     */

    private int userId;
    private String nickname;
    private int sex;
    private String birthday;
    private String avatar;
    private String signature;
    private Location location;
    private String letter;
    private int mIsSystem;
    private boolean isFriend;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String userNickname) {
        this.nickname = userNickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public void setIsSystem(int isSystem) {
        mIsSystem = isSystem;
    }

    public int getIsSystem() {
        return mIsSystem;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public static UserInfoDB toUserInfoDB(UserInfoModel userInfModel) {
        UserInfoDB userInfoDB = new UserInfoDB();
        if (userInfModel != null) {
            userInfoDB.setUserId(userInfModel.getUserId());
            userInfoDB.setUserNickname(userInfModel.getNickname());
            userInfoDB.setSex(userInfModel.getSex());
            userInfoDB.setBirthday(userInfModel.getBirthday());
            userInfoDB.setAvatar(userInfModel.getAvatar());
            userInfoDB.setSignature(userInfModel.getSignature());
            userInfoDB.setLetter(userInfModel.getLetter());
            userInfoDB.setIsSystem(userInfModel.getIsSystem());

            JSONObject jsonObject = new JSONObject();
            Location location = userInfModel.getLocation();
            jsonObject.put("location", location);
            userInfoDB.setExt(jsonObject.toJSONString());
        }
        return userInfoDB;
    }

    public static UserInfoModel parseFromDB(UserInfoDB userInDB) {
        UserInfoModel userInfoModel = new UserInfoModel();
        if (userInDB != null) {
            userInfoModel.setUserId((int) userInDB.getUserId());
            userInfoModel.setNickname(userInDB.getUserNickname());
            userInfoModel.setSex(userInDB.getSex());
            userInfoModel.setBirthday(userInDB.getBirthday());
            userInfoModel.setAvatar(userInDB.getAvatar());
            userInfoModel.setSignature(userInDB.getSignature());
            userInfoModel.setLetter(userInDB.getLetter());
            userInfoModel.setIsSystem(userInDB.getIsSystem());
            String extJSon = userInDB.getExt();
            if (!TextUtils.isEmpty(extJSon)) {
                JSONObject jsonObject = JSON.parseObject(extJSon, JSONObject.class);
                Location location = jsonObject.getObject("location", Location.class);
                userInfoModel.setLocation(location);
            }
        }
        return userInfoModel;
    }


}
