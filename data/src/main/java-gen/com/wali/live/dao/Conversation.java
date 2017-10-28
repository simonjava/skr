package com.wali.live.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import android.text.TextUtils;

import com.base.log.MyLog;

import org.json.JSONObject;

/**
 * Entity mapped to table CONVERSATION.
 */
public class Conversation {

    private Long id;
    private long target;
    private Integer unreadCount;
    private Long sendTime;
    private Long receivedTime;
    private String content;
    private Long lastMsgSeq;
    private String targetName;
    private Long msgId;
    private Integer msgType;
    private Integer ignoreStatus;
    private long locaLUserId;
    private boolean isNotFocus;
    private String ext;
    private Integer certificationType;
    private int targetType;
    private String icon;
    private Integer inputMode;

    // KEEP FIELDS - put your custom fields here
    public static final int NO_SET_VALUE = -1;
    public static final int NOT_IGNORE = 0; //不忽略该对话
    public static final int IGNOE_UNSHOW_UNREAD = 1; //忽略对话(不计入总未读数)但是对话列表显示红点,不显示未读
    public static final int IGNOE_BUT_SHOW_UNREAD = 2; //忽略对话(不计入总未读数)但是对话列表显示灰色未读

    public static final int UNFOCUS_CONVERSATION_TARGET = 123;//未关注列表target
    public static final int GROUP_NOTIFY_CONVERSATION_TARGET = 125;// 群通知的target
    public static final int INTERACT_CONVERSATION_TARGET=126;   //互动通知的target
    public static final int VFANS_NOTIFY_CONVERSATION_TARGET = 127;// vfans通知的target

    public static final String EXT_SENDER = "EXT_SENDER"; //最后一条消息发送的人
    public static final String EXT_TARGET = "EXT_TARGET"; //机器人最后一条消息的发送者
    public static final String EXT_IS_BLOCK = "EXT_IS_BLOCK"; //是否把这个人加入了黑名单
    public static final String EXT_ATT_ID = "EXT_ATT_ID"; //att　id
    public static final String EXT_FOCUS_STATE = "EXT_FOCUS_STATUE";// 关注的状态
    public static final String EXT_RUBBISH_UNREAD_COUNT = "EXT_RUBBISH_UNREAD_COUNT"; //垃圾箱未读数
    public static final String EXT_HAS_SOME_BODY_AT_ME = "EXT_SOME_BODY_AT_ME"; //最后一个at me的消息seq
    public static final String EXT_IS_FOLLOW_MSG = "EXT_IS_FOLLOW_MSG"; //给ext设置的，是标记是不是关注的消息
    // KEEP FIELDS END

    public Conversation() {
    }

    public Conversation(Long id) {
        this.id = id;
    }

    public Conversation(Long id, long target, Integer unreadCount, Long sendTime, Long receivedTime, String content, Long lastMsgSeq, String targetName, Long msgId, Integer msgType, Integer ignoreStatus, long locaLUserId, boolean isNotFocus, String ext, Integer certificationType, int targetType, String icon, Integer inputMode) {
        this.id = id;
        this.target = target;
        this.unreadCount = unreadCount;
        this.sendTime = sendTime;
        this.receivedTime = receivedTime;
        this.content = content;
        this.lastMsgSeq = lastMsgSeq;
        this.targetName = targetName;
        this.msgId = msgId;
        this.msgType = msgType;
        this.ignoreStatus = ignoreStatus;
        this.locaLUserId = locaLUserId;
        this.isNotFocus = isNotFocus;
        this.ext = ext;
        this.certificationType = certificationType;
        this.targetType = targetType;
        this.icon = icon;
        this.inputMode = inputMode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(long target) {
        this.target = target;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getLastMsgSeq() {
        return lastMsgSeq;
    }

    public void setLastMsgSeq(Long lastMsgSeq) {
        this.lastMsgSeq = lastMsgSeq;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getIgnoreStatus() {
        return ignoreStatus;
    }

    public void setIgnoreStatus(Integer ignoreStatus) {
        this.ignoreStatus = ignoreStatus;
    }

    public long getLocaLUserId() {
        return locaLUserId;
    }

    public void setLocaLUserId(long locaLUserId) {
        this.locaLUserId = locaLUserId;
    }

    public boolean getIsNotFocus() {
        return isNotFocus;
    }

    public void setIsNotFocus(boolean isNotFocus) {
        this.isNotFocus = isNotFocus;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Integer getCertificationType() {
        return certificationType;
    }

    public void setCertificationType(Integer certificationType) {
        this.certificationType = certificationType;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getInputMode() {
        return inputMode;
    }

    public void setInputMode(Integer inputMode) {
        this.inputMode = inputMode;
    }

    // KEEP METHODS - put your custom methods here
    public boolean isNotFocusConversation() {
        return target == UNFOCUS_CONVERSATION_TARGET;
    }

    public boolean isBlock() {
        if (TextUtils.isEmpty(ext)) {
            return false;
        }
        try {
            JSONObject jsonObject = new JSONObject(ext);
            return jsonObject.optBoolean(EXT_IS_BLOCK, false);
        } catch (Exception e) {
            MyLog.e(e);
            return false;
        }
    }

    public void updateOrInsertExt(String key, Object value) {
        try {
            JSONObject jsonObject = null;
            if (TextUtils.isEmpty(ext)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(ext);
            }
            jsonObject.put(key, value);
            ext = jsonObject.toString();
        } catch (Exception e) {
            MyLog.e(e);
        }
    }


    public int getFocusStatue() {
        int statue = SixinMessage.MSG_STATUS_UNFOUCS;
        if (TextUtils.isEmpty(ext)) {
            return statue;
        }
        try {
            JSONObject jsonObject = new JSONObject(ext);
            statue = jsonObject.optInt(EXT_FOCUS_STATE, SixinMessage.MSG_STATUS_UNFOUCS);
        } catch (Exception e) {
            MyLog.e(e);
        }
        return statue;

    }


    public boolean hasSomeOneAtMe(){
        try {
            if(!TextUtils.isEmpty(ext)) {
                JSONObject jsonObject = new JSONObject(ext);
                return jsonObject.optBoolean(EXT_HAS_SOME_BODY_AT_ME, false);
            }
        } catch (Exception e) {
            MyLog.e(e);
        }
        return false;
    }

    public boolean hasFocusKey() {
        if (TextUtils.isEmpty(ext)) {
            return false;
        }
        try {
            JSONObject jsonObject = new JSONObject(ext);
            return jsonObject.has(EXT_FOCUS_STATE);
        } catch (Exception e) {
            MyLog.e(e);
        }
        return false;
    }
    // KEEP METHODS END

}
