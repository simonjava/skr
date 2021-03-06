package com.module.home.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

public class GameKConfigModel implements Serializable {
    /**
     * homepage-site-first : {"enable":true,"pic":"http://res-static.inframe.mobi/app/skr-redpacket-20190304.png","schema":"inframeskr://web/fullScreen?url=https://www.baidu.com&showShare=1"}
     * pk-tags : ["K歌·3人赛"]
     * stand-tags : ["抢麦·挑战30s"]
     */
    @JSONField(name = "agora-token")
    private boolean agoraTokenEnable;
    @JSONField(name = "homepage-site-first")
    private HomepagesitefirstBean homepagesitefirst;
    @JSONField(name = "pk-tags")
    private List<String> pktags;
    @JSONField(name = "stand-tags")
    private List<String> standtags;
    @JSONField(name = "homepage-ticker-interval")
    private int homepagetickerinterval; // 老接口，用秒吧
    @JSONField(name = "relay-ticker-interval-ms")
    private long relaytickerinterval;   // 毫秒

    public HomepagesitefirstBean getHomepagesitefirst() {
        return homepagesitefirst;
    }

    public void setHomepagesitefirst(HomepagesitefirstBean homepagesitefirst) {
        this.homepagesitefirst = homepagesitefirst;
    }

    public List<String> getPktags() {
        return pktags;
    }

    public void setPktags(List<String> pktags) {
        this.pktags = pktags;
    }

    public List<String> getStandtags() {
        return standtags;
    }

    public void setStandtags(List<String> standtags) {
        this.standtags = standtags;
    }

    public boolean isAgoraTokenEnable() {
        return agoraTokenEnable;
    }

    public void setAgoraTokenEnable(boolean agoraTokenEnable) {
        this.agoraTokenEnable = agoraTokenEnable;
    }

    public int getHomepagetickerinterval() {
        return homepagetickerinterval;
    }

    public void setHomepagetickerinterval(int homepagetickerinterval) {
        this.homepagetickerinterval = homepagetickerinterval;
    }

    public long getRelaytickerinterval() {
        return relaytickerinterval;
    }

    public void setRelaytickerinterval(long relaytickerinterval) {
        this.relaytickerinterval = relaytickerinterval;
    }

    public static class HomepagesitefirstBean implements Serializable{
        /**
         * enable : true
         * pic : http://res-static.inframe.mobi/app/skr-redpacket-20190304.png
         * schema : inframeskr://web/fullScreen?url=https://www.baidu.com&showShare=1
         */

        private boolean enable;
        private String pic;
        private String schema;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }
    }

    @Override
    public String toString() {
        return "GameKConfigModel{" +
                "agoraTokenEnable=" + agoraTokenEnable +
                ", homepagesitefirst=" + homepagesitefirst +
                ", pktags=" + pktags +
                ", standtags=" + standtags +
                ", homepagetickerinterval=" + homepagetickerinterval +
                '}';
    }
}
