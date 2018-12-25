package com.common.core.myinfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.common.core.userinfo.UserInfo;


/**
 * 除了个人基本信息外
 * 还有许多额外信息，存在 UserInfo 的ext中？
 */
public class MyUserInfo {
    private UserInfo mUserInfo;

    private Boolean certificationChanged; // 认证改变
    private String viewUrl;                                       //直播地址
    private String roomId = null;                                 //正在直播的id, 房间号
    private String tvRoomId = null;                               //正在播放的电视台房间id
    private Integer roomType;
    private Boolean online;  // 是否在线，这个只有在添加管理员的时候使用了。
    private Integer appType; // app类型 4代表一直播
    private Boolean redName; // 是否被社区红名了
    private String norbleMedal = null;
    //aaa
    private Boolean isNeedBindPhone;//实名：手机绑定
    private String phoneNum;
    private String ext;

    public MyUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = new UserInfo();
        }
    }

    public Long getUid() {
        return this.mUserInfo.getUserId();
    }

    public void setUid(Long uid) {
        this.mUserInfo.setUserId(uid);
    }

    public String getAvatar() {
        return this.mUserInfo.getAvatar();
    }

    public void setAvatar(String avatar) {
        this.mUserInfo.setAvatar(avatar);
    }

    public String getNickName() {
        return this.mUserInfo.getUserNickname();
    }

    public void setNickName(String nickName) {
        this.mUserInfo.setUserNickname(nickName);
    }

    public void setBirthday(String birthday) {
        this.mUserInfo.setBirthday(birthday);
    }

    public String getBirthday() {
        return this.mUserInfo.getBirthday();
    }

    public void setSex(int sex) {
        this.mUserInfo.setSex(sex);
    }

    public int getSex() {
        return this.mUserInfo.getSex();
    }

    public String getSignature() {
        return this.mUserInfo.getSignature();
    }

    public void setSignature(String signature) {
        this.mUserInfo.setSignature(signature);
    }

    public Boolean getCertificationChanged() {
        return this.certificationChanged;
    }

    public void setCertificationChanged(Boolean certificationChanged) {
        this.certificationChanged = certificationChanged;
    }


    public String getViewUrl() {
        return this.viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTvRoomId() {
        return this.tvRoomId;
    }

    public void setTvRoomId(String tvRoomId) {
        this.tvRoomId = tvRoomId;
    }

    public Integer getRoomType() {
        return this.roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }

    public Boolean getOnline() {
        return this.online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Integer getAppType() {
        return this.appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public Boolean getRedName() {
        return this.redName;
    }

    public void setRedName(Boolean redName) {
        this.redName = redName;
    }

    public String getNorbleMedal() {
        return this.norbleMedal;
    }

    public void setNorbleMedal(String norbleMedal) {
        this.norbleMedal = norbleMedal;
    }


    public Boolean getIsNeedBindPhone() {
        return this.isNeedBindPhone;
    }

    public void setIsNeedBindPhone(Boolean isNeedBindPhone) {
        this.isNeedBindPhone = isNeedBindPhone;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getExt() {
        return this.ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        parseFromJson(JSON.parseObject(userInfo.getExt()));
    }


    public JSONObject packetToJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("certificationChanged", certificationChanged);
            jsonObject.put("viewUrl", viewUrl);
            jsonObject.put("roomId", roomId);
            jsonObject.put("tvRoomId", tvRoomId);
            jsonObject.put("roomType", roomType);
            jsonObject.put("online", online);
            jsonObject.put("appType", appType);
            jsonObject.put("redName", redName);
            jsonObject.put("norbleMedal", norbleMedal);
            jsonObject.put("isNeedBindPhone", isNeedBindPhone);
            jsonObject.put("phoneNum", phoneNum);
            jsonObject.put("ext", ext);
        } catch (JSONException e) {

        }

        return jsonObject;
    }

    public void parseFromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        certificationChanged = jsonObject.getBoolean("certificationChanged");
        viewUrl = jsonObject.getString("viewUrl");
        roomId = jsonObject.getString("roomId");
        tvRoomId = jsonObject.getString("tvRoomId");
        roomType = jsonObject.getIntValue("roomType");
        online = jsonObject.getBoolean("online");
        appType = jsonObject.getIntValue("appType");
        redName = jsonObject.getBoolean("redName");
        norbleMedal = jsonObject.getString("norbleMedal");
        isNeedBindPhone = jsonObject.getBoolean("isNeedBindPhone");
        phoneNum = jsonObject.getString("phoneNum");
        ext = jsonObject.getString("ext");
    }

}
