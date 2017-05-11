package com.mi.liveassistant.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table RELATION.
 */
public class Relation implements java.io.Serializable {

    private Long userId;
    private Long avatar;
    private String userNickname;
    private String signature;
    private Integer gender;
    private Integer level;
    private Integer mTicketNum;
    private Integer certificationType;
    private Boolean isFollowing;
    private Boolean isBothway;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Relation() {
    }

    public Relation(Long userId) {
        this.userId = userId;
    }

    public Relation(Long userId, Long avatar, String userNickname, String signature, Integer gender, Integer level, Integer mTicketNum, Integer certificationType, Boolean isFollowing, Boolean isBothway) {
        this.userId = userId;
        this.avatar = avatar;
        this.userNickname = userNickname;
        this.signature = signature;
        this.gender = gender;
        this.level = level;
        this.mTicketNum = mTicketNum;
        this.certificationType = certificationType;
        this.isFollowing = isFollowing;
        this.isBothway = isBothway;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAvatar() {
        return avatar;
    }

    public void setAvatar(Long avatar) {
        this.avatar = avatar;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getMTicketNum() {
        return mTicketNum;
    }

    public void setMTicketNum(Integer mTicketNum) {
        this.mTicketNum = mTicketNum;
    }

    public Integer getCertificationType() {
        return certificationType;
    }

    public void setCertificationType(Integer certificationType) {
        this.certificationType = certificationType;
    }

    public Boolean getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(Boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public Boolean getIsBothway() {
        return isBothway;
    }

    public void setIsBothway(Boolean isBothway) {
        this.isBothway = isBothway;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
