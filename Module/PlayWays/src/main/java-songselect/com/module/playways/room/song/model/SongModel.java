package com.module.playways.room.song.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.log.MyLog;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.component.lyrics.utils.SongResUtils;
import com.engine.CdnInfo;
import com.engine.api.EngineServerApi;
import com.zq.live.proto.Common.MusicInfo;
import com.zq.live.proto.Common.StandPlayType;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SongModel implements Serializable {

    public static final int ID_FREE_MIC = 9277;
    public static final int ID_CUSTOM_GAME = 9276;
    /**
     * itemID : 44
     * itemName : 过火
     * cover : http://online-sound-bja.oss-cn-beijing.aliyuncs.com/cover/7b61e42c07746017d7b1c87b216ea797.jpg
     * owner : 张信哲
     * lyric : http://online-sound-bja.oss-cn-beijing.aliyuncs.com/lrc/bff5ca83193e760153b616cfdacaceb7.zrce
     * ori : http://online-sound-bja.oss-cn-beijing.aliyuncs.com/mp3/60e41e6266d57555e075bde1671b009e.mp3
     * acc : http://online-sound-bja.oss-cn-beijing.aliyuncs.com/bgm/ce09989528fb7e717e8d17462cd95737.mp3
     * midi :
     * zip :
     * rankBgm : http://online-sound-bja.oss-cn-beijing.aliyuncs.com/bgm/ce09989528fb7e717e8d17462cd95737.mp3
     * beginMs : 23000
     * endMs : 121000
     * StandIntro :
     * StandIntroBeginT : 0
     * StandIntroEndT : 0
     * totalMs : 98000
     */

    private int itemID;
    private String itemName;
    private String cover;
    private String owner;
    private String lyric;   //歌词
    private String ori;     //原唱
    private String acc;     //伴奏
    private String midi;    //midi文件
    private String zip;
    private String rankBgm;
    private int beginMs;         //开始毫秒
    private int endMs;           //结束毫秒
    private int totalMs;         //共计多少毫秒
    private int rankLrcBeginT;   //匹配玩法第一句歌词开始时间,毫秒
    private String StandIntro;         //一唱到底的导唱
    private int StandIntroBeginT;      //一唱到底导唱的开始毫秒
    private int StandIntroEndT;        //一唱到底导唱的结束毫秒
    private int standLrcBeginT;        //一唱到底第一句歌词的开始毫秒
    private int standLrcEndT;          //一唱到底歌词的结束毫秒
    private boolean isblank = false;   //一唱到底是否是白板item
    private String standLrc = "";   //一唱到底歌词
    private String rankUserVoice;   //排位进入游戏前的背景音乐
    private int rankLrcEndT;   //排位进入游戏前的背景音乐
    private boolean challengeAvailable;// 挑战是否可用
    private int playType;// 玩法类型 普通 合唱 pk
    @JSONField(name = "SPKMusic")
    private List<SongModel> pkMusicList;
    private int singCount;
    private MiniGameInfoModel miniGame;
    private String writer;    //作词人
    private String composer;   //作曲人
    private String uploaderName; //上传用户名

    private ArrayList<Integer> relaySegments;

    public int getSingCount() {
        return singCount;
    }

    public void setSingCount(int singCount) {
        this.singCount = singCount;
    }

    public boolean isChallengeAvailable() {
        return challengeAvailable;
    }

    public void setChallengeAvailable(boolean challengeAvailable) {
        this.challengeAvailable = challengeAvailable;
    }

    public int getRankLrcBeginT() {
        return rankLrcBeginT;
    }

    public void setRankLrcBeginT(int rankLrcBeginT) {
        this.rankLrcBeginT = rankLrcBeginT;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getStandLrc() {
        return standLrc;
    }

    public void setStandLrc(String standLrc) {
        this.standLrc = standLrc;
    }

    public String getOri() {
        return ori;
    }

    public void setOri(String ori) {
        this.ori = ori;
    }

    public String getAcc() {
        List<CdnInfo> l = getAccWithCdnInfos(false);
        if (l.isEmpty()) {
            return acc;
        } else {
            return l.get(0).getUrl();
        }
    }

    public List<CdnInfo> getAccWithCdnInfos(boolean isUseCacheFile) {
        ArrayList<CdnInfo> l = new ArrayList<>();
        if (acc.matches("http(s)?://song-static.*")) {
            for (CdnInfo info : cdnInfos) {
                CdnInfo cdnInfo = new CdnInfo();
                cdnInfo.setCdnType(info.getCdnType());
                cdnInfo.setAppendix(info.getAppendix());
                cdnInfo.setEnableCache(info.isEnableCache());
                String url = acc.replaceFirst("song-static", "song-static" + cdnInfo.getAppendix());
                String accFilePath = null;
                if (isUseCacheFile) {
                    File accFile = SongResUtils.getAccFileByUrl(url);
                    if (accFile != null && accFile.exists()) {
                        accFilePath = accFile.getAbsolutePath();
                    }
                }
                cdnInfo.setLocalPath(accFilePath);
                cdnInfo.setUrl(url);
                l.add(cdnInfo);
            }
        }
        if (l.isEmpty()) {
            CdnInfo cdnInfo = new CdnInfo();
            cdnInfo.setCdnType("aliyun");
            cdnInfo.setAppendix("");
            cdnInfo.setEnableCache(true);
            cdnInfo.setUrl(acc);
            l.add(cdnInfo);
        }
        return l;
    }

    public String getAccWithCdnInfosJson() {
        List<CdnInfo> l = getAccWithCdnInfos(true);
        String json = JSON.toJSONString(l);
        return json;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getMidi() {
        return midi;
    }

    public void setMidi(String midi) {
        this.midi = midi;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getRankBgm() {
        return rankBgm;
    }

    public void setRankBgm(String rankBgm) {
        this.rankBgm = rankBgm;
    }

    public int getBeginMs() {
        return beginMs;
    }

    public void setBeginMs(int beginMs) {
        this.beginMs = beginMs;
    }

    public int getEndMs() {
        return endMs;
    }

    public int getRankLrcEndT() {
        return rankLrcEndT;
    }

    public void setRankLrcEndT(int rankLrcEndT) {
        this.rankLrcEndT = rankLrcEndT;
    }

    public void setEndMs(int endMs) {
        this.endMs = endMs;
    }

    public String getStandIntro() {
        return StandIntro;
    }

    public void setStandIntro(String StandIntro) {
        this.StandIntro = StandIntro;
    }

    public int getStandIntroBeginT() {
        return StandIntroBeginT;
    }

    public void setStandIntroBeginT(int StandIntroBeginT) {
        this.StandIntroBeginT = StandIntroBeginT;
    }

    public int getStandIntroEndT() {
        return StandIntroEndT;
    }

    public void setStandIntroEndT(int StandIntroEndT) {
        this.StandIntroEndT = StandIntroEndT;
    }

    public int getStandLrcBeginT() {
        return standLrcBeginT;
    }

    public void setStandLrcBeginT(int standLrcBeginT) {
        this.standLrcBeginT = standLrcBeginT;
    }

    public int getStandLrcEndT() {
        return standLrcEndT;
    }

    public void setStandLrcEndT(int standLrcEndT) {
        this.standLrcEndT = standLrcEndT;
    }

    public int getTotalMs() {
        if (totalMs <= 0) {
            MyLog.d("SongModel", "totalMs<=0 容错，返回30*1000");
            int t = standLrcEndT - standLrcBeginT;
            if (t <= 0) {
                return 30 * 1000;
            } else {
                return t;
            }
        }
        return totalMs;
    }

    public void setTotalMs(int totalMs) {
        this.totalMs = totalMs;
    }

    public boolean isIsblank() {
        return isblank;
    }

    public void setIsblank(boolean isblank) {
        this.isblank = isblank;
    }

    public String getRankUserVoice() {
        return rankUserVoice;
    }

    public void setRankUserVoice(String rankUserVoice) {
        this.rankUserVoice = rankUserVoice;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public MiniGameInfoModel getMiniGame() {
        return miniGame;
    }

    public void setMiniGame(MiniGameInfoModel miniGame) {
        this.miniGame = miniGame;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public ArrayList<Integer> getRelaySegments() {
        return relaySegments;
    }

    public void setRelaySegments(ArrayList<Integer> relaySegments) {
        this.relaySegments = relaySegments;
    }

    public void parse(MusicInfo musicInfo) {
        if (musicInfo == null) {
            MyLog.e("SongModel MusicInfo == null");
            return;
        }

        this.setItemID(musicInfo.getItemID());
        this.setItemName(musicInfo.getItemName());
        this.setCover(musicInfo.getCover());
        this.setOwner(musicInfo.getOwner());
        this.setLyric(musicInfo.getLyric());
        this.setOri(musicInfo.getOri());
        this.setAcc(musicInfo.getAcc());
        this.setMidi(musicInfo.getMidi());
        this.setZip(musicInfo.getZip());
        this.setTotalMs(musicInfo.getTotalMs());
        this.setBeginMs(musicInfo.getBeginMs());
        this.setEndMs(musicInfo.getEndMs());
        this.setRankLrcBeginT(musicInfo.getRankLrcBeginT());

        this.setStandIntro(musicInfo.getStandIntro());
        this.setStandIntroBeginT(musicInfo.getStandIntroBeginT());
        this.setStandIntroEndT(musicInfo.getStandIntroEndT());
        this.setStandLrcBeginT(musicInfo.getStandLrcBeginT());
        this.setStandLrcEndT(musicInfo.getStandLrcEndT());
        this.setIsblank(musicInfo.getIsBlank());
        this.setStandLrc(musicInfo.getStandLrc());
        this.setRankUserVoice(musicInfo.getRankUserVoice());
        this.setRankLrcEndT(musicInfo.getRankLrcEndT());
        this.setChallengeAvailable(musicInfo.getChallengeAvailable());
        this.setPlayType(musicInfo.getPlayType().getValue());
        List<MusicInfo> list = musicInfo.getSPKMusicList();
        if (list != null) {
            this.pkMusicList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                SongModel songModel = new SongModel();
                songModel.parse(list.get(i));
                this.pkMusicList.add(songModel);
            }
        }

        if (musicInfo.getMiniGame() != null) {
            this.setMiniGame(MiniGameInfoModel.parse(musicInfo.getMiniGame()));
        }
        this.setWriter(musicInfo.getWriter());
        this.setComposer(musicInfo.getComposer());
        this.setUploaderName(musicInfo.getUploaderName());
        if (musicInfo.getRelaySegmentsList() != null) {
            ArrayList<Integer> l = new ArrayList<>();
            l.addAll(musicInfo.getRelaySegmentsList());
            this.setRelaySegments(l);
        }
    }

    public SongModel getPkMusic() {
        if (pkMusicList.isEmpty()) {
            return null;
        }
        return pkMusicList.get(0);
    }

    public List<SongModel> getPkMusicList() {
        return pkMusicList;
    }

    public void setPkMusicList(List<SongModel> pkMusicList) {
        this.pkMusicList = pkMusicList;
    }


    public boolean isAllResExist() {
        File lyricFile = SongResUtils.getLyricFileByUrl(getLyric());

        if (lyricFile == null || !lyricFile.exists()) {
            return false;
        }

        File acc = SongResUtils.getAccFileByUrl(getAcc());

        if (acc == null || !acc.exists()) {
            return false;
        }

//        File ori = SongResUtils.getORIFileByUrl(getOri());
//
//        if (ori == null || !ori.exists()) {
//            return false;
//        }

        File midi = SongResUtils.getMIDIFileByUrl(getMidi());

        if (midi == null || !midi.exists()) {
            return false;
        }

        return true;
    }

    public String getDisplaySongName() {
        if (playType == StandPlayType.PT_SPK_TYPE.getValue()) {
            if (!TextUtils.isEmpty(itemName) && itemName.contains("（PK版）")) {
                return itemName.substring(0, itemName.length() - 5);
            }
        } else if (playType == StandPlayType.PT_CHO_TYPE.getValue()) {
            if (!TextUtils.isEmpty(itemName) && itemName.contains("（合唱版）")) {
                return itemName.substring(0, itemName.length() - 5);
            }
        }
        return itemName;
    }

    public String getSongDesc() {
        String desc = "";
        if (!TextUtils.isEmpty(writer)) {
            desc = "词/" + writer;
        }
        if (!TextUtils.isEmpty(desc)) {
            if (!TextUtils.isEmpty(composer)) {
                desc = desc + " 曲/" + composer;
            }
        } else {
            if (!TextUtils.isEmpty(composer)) {
                desc = "曲/" + composer;
            }
        }
        return desc;
    }

    @Override
    public String toString() {
        return "SongModel{" +
                "itemID=" + itemID +
                ", itemName='" + itemName + '\'' +
                ", cover='" + cover + '\'' +
                ", owner='" + owner + '\'' +
                ", lyric='" + lyric + '\'' +
                ", ori='" + ori + '\'' +
                ", acc='" + acc + '\'' +
                ", midi='" + midi + '\'' +
                ", zip='" + zip + '\'' +
                ", rankBgm='" + rankBgm + '\'' +
                ", beginMs=" + beginMs +
                ", endMs=" + endMs +
                ", totalMs=" + totalMs +
                ", rankLrcBeginT=" + rankLrcBeginT +
                ", StandIntro='" + StandIntro + '\'' +
                ", StandIntroBeginT=" + StandIntroBeginT +
                ", StandIntroEndT=" + StandIntroEndT +
                ", standLrcBeginT=" + standLrcBeginT +
                ", standLrcEndT=" + standLrcEndT +
                ", isblank=" + isblank +
                ", standLrc='" + standLrc + '\'' +
                ", rankUserVoice='" + rankUserVoice + '\'' +
                ", rankLrcEndT=" + rankLrcEndT +
                ", challengeAvailable=" + challengeAvailable +
                ", playType=" + playType +
                ", pkMusicList=" + pkMusicList +
                ", singCount=" + singCount +
                ", miniGame=" + miniGame +
                '}';
    }

    public String toSimpleString() {
        return "SongModel{" +
                "itemID=" + itemID +
                ", itemName='" + itemName + '\'' +
                ", acc='" + acc + '\'' +
//                ", totalMs=" + totalMs +
//                ", StandIntro='" + StandIntro + '\'' +
//                ", StandIntroBeginT=" + StandIntroBeginT +
//                ", StandIntroEndT=" + StandIntroEndT +
//                ", isblank=" + isblank +
//                ", standLrc='" + standLrc + '\'' +
                '}';
    }

    static List<CdnInfo> cdnInfos = new ArrayList<>();

    public static void syncCdnInfo() {
        EngineServerApi api = ApiManager.getInstance().createService(EngineServerApi.class);
        ApiMethods.subscribe(api.getCdnCfg((int) MyUserInfoManager.INSTANCE.getUid()), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult obj) {
                if (obj.getErrno() == 0) {
                    cdnInfos = JSON.parseArray(obj.getData().getString("cfg"), CdnInfo.class);
                    MyLog.w("SongModel", cdnInfos.toString());
                }
            }
        });
    }
}
