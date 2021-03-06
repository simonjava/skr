package com.component.busilib.friends;

import java.io.Serializable;

public class SpecialModel implements Serializable {

    public static final int TYPE_AUDIO = 1;
    public static final int TYPE_VIDEO = 2;

    /**
     * tagID : 8
     * tagName : 疯狂00后
     * introduction : 请开始你的表演
     * bgColor : #9B6C43
     */

    private int tagID;
    private String tagName;
    private String introduction;
    private String bgColor;
    private String bgImage1;
    private String bgImage2;
    private String bgImage3;
    private int tagType;
    private TagImageModel biggest;
    private TagImageModel longer;

    public int getTagID() {
        return tagID;
    }

    public void setTagID(int tagID) {
        this.tagID = tagID;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getBgImage3() {
        return bgImage3;
    }

    public void setBgImage3(String bgImage3) {
        this.bgImage3 = bgImage3;
    }

    public String getBgImage1() {
        return bgImage1;
    }

    public void setBgImage1(String bgImage1) {
        this.bgImage1 = bgImage1;
    }

    public String getBgImage2() {
        return bgImage2;
    }

    public void setBgImage2(String bgImage2) {
        this.bgImage2 = bgImage2;
    }


    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public TagImageModel getBiggest() {
        return biggest;
    }

    public void setBiggest(TagImageModel biggest) {
        this.biggest = biggest;
    }

    public TagImageModel getLonger() {
        return longer;
    }

    public void setLonger(TagImageModel longer) {
        this.longer = longer;
    }

    @Override
    public String toString() {
        return "SpecialModel{" +
                "tagID=" + tagID +
                ", tagName='" + tagName + '\'' +
                ", introduction='" + introduction + '\'' +
                ", bgColor='" + bgColor + '\'' +
                ", bgImage1='" + bgImage1 + '\'' +
                ", bgImage2='" + bgImage2 + '\'' +
                ", bgImage3='" + bgImage3 + '\'' +
                ", tagType=" + tagType +
                '}';
    }
}
