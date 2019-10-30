package com.engine.statistics;


import com.engine.statistics.datastruct.SAgora;
import com.engine.statistics.datastruct.SAgoraUserEvent;
import com.engine.statistics.datastruct.Skr;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.IRtcEngineEventHandler;



public class SAgoraDataHolder
{

    private String mLinePrefix = "";
    private List<SAgora.SRTCStats>  mRtcStatsList;
    private List<SAgora.SLocalVideoStats> mLocalVList;
    private List<SAgora.SRemoteAudioStats> mRemoteAList;
    private List<SAgora.SRemoteVideoStats> mRemoteVList;

    private List<SAgora.SNetworkQuality> mNetQualityList;
    private List<SAgora.SRemoteAudioTransportStats>   mRemoteATransList;
    private List<SAgora.SRemoteVideoTransportStat>    mRemoteVTransList;
    private List<SAgora.SAudioSamplingInfo> mAudioSamplingInfoList;
    private List<SAgora.SPlayerInfo> mPlayerInfo;
    private List<SAgoraUserEvent>  mUserEvenList;

    private List<Skr.PingInfo> mPingInfoList;
    private List<Skr.NetworkInfo> mNetworkInfoList;

    public SAgoraDataHolder() {
        mRtcStatsList = new ArrayList<SAgora.SRTCStats>();
        mLocalVList  = new ArrayList<SAgora.SLocalVideoStats>();
        mRemoteAList = new ArrayList<SAgora.SRemoteAudioStats>();
        mRemoteVList = new ArrayList<SAgora.SRemoteVideoStats>();
        mNetQualityList= new ArrayList<SAgora.SNetworkQuality>();
        mRemoteATransList= new ArrayList<SAgora.SRemoteAudioTransportStats>();
        mRemoteVTransList= new ArrayList<SAgora.SRemoteVideoTransportStat>();

        mAudioSamplingInfoList = new ArrayList<SAgora.SAudioSamplingInfo>();
        mPlayerInfo = new ArrayList<SAgora.SPlayerInfo>();
        mUserEvenList = new ArrayList<SAgoraUserEvent>();

        mPingInfoList = new ArrayList<Skr.PingInfo>();
        mNetworkInfoList = new ArrayList<Skr.NetworkInfo>();
    }

    public SAgoraDataHolder setLinePrefix(String prefix) {
        if (null != prefix && 0 != prefix.length())
            mLinePrefix = prefix;

        return this;
    }





    public synchronized void addRtcStats(IRtcEngineEventHandler.RtcStats s ) {
        SAgora.SRTCStats n = new SAgora.SRTCStats();
        n.timeStamp     = System.currentTimeMillis();

        n.cpuAppUsage   = s.cpuAppUsage;
        n.cpuTotalUsage = s.cpuTotalUsage;
        n.totalDuration = s.totalDuration;
        n.txBytes = s.txBytes;
        n.rxBytes = s.rxBytes;
        n.txKBitRate = s.txKBitRate;
        n.rxKBitRate = s.rxKBitRate;
        n.txAudioKBitRate = s.txAudioKBitRate;
        n.rxAudioKBitRate = s.rxAudioKBitRate;
        n.users = s.users;
        n.lastmileDelay = s.lastmileDelay;
        n.txPacketLossRate = s.txPacketLossRate;
        n.rxPacketLossRate = s.rxPacketLossRate;

        mRtcStatsList.add(n);
        return;
    }

    public synchronized void addLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats s) {
        SAgora.SLocalVideoStats n = new SAgora.SLocalVideoStats();

        n.timeStamp = System.currentTimeMillis();
        n.sentBitrate = s.sentBitrate;
        n.sentFrameRate = s.sentFrameRate;
        n.encoderOutputFrameRate = s.encoderOutputFrameRate;
        n.rendererOutputFrameRate= s.rendererOutputFrameRate;
        n.targetBitrate = s.targetBitrate;
        n.targetFrameRate = s.targetFrameRate;
        n.qualityAdaptIndication = s.qualityAdaptIndication;

        mLocalVList.add(n);
        return;
    }

    public synchronized void addRemoteAudioStats(IRtcEngineEventHandler.RemoteAudioStats s) {
        SAgora.SRemoteAudioStats n = new SAgora.SRemoteAudioStats();

        n.timeStamp = System.currentTimeMillis();

        n.uid = s.uid;
        n.quality = s.quality;
//        n.strQuality =  transNetQuality(s.quality);
        n.networkTransportDelay = s.networkTransportDelay;
        n.jitterBufferDelay = s.jitterBufferDelay;
        n.audioLossRate = s.audioLossRate;

        mRemoteAList.add(n);
        return;
    }


    public synchronized void addRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats s) {
        SAgora.SRemoteVideoStats n = new SAgora.SRemoteVideoStats();

        n.timeStamp = System.currentTimeMillis();
        n.uid = s.uid;
        n.width = s.width;
        n.height= s.height;
        n.receivedBitrate = s.receivedBitrate;
        n.decoderOutputFrameRate = s.decoderOutputFrameRate;
        n.rendererOutputFrameRate= s.rendererOutputFrameRate;
        n.rxStreamType = s.rxStreamType;

        mRemoteVList.add(n);
        return;
    }

    public synchronized void addNetQualityStats(int uid, int txQuality,int rxQuality) {
        SAgora.SNetworkQuality n = new SAgora.SNetworkQuality();

        n.timeStamp = System.currentTimeMillis();
        n.uid = uid;
        n.txQuality = txQuality;
        n.rxQuality = rxQuality;

        mNetQualityList.add(n);
        return;
    }


    public synchronized void addRemoteAudioTransStats(int uid, int delay, int lost, int rxKBitRate) {
        SAgora.SRemoteAudioTransportStats n = new SAgora.SRemoteAudioTransportStats();

        n.timeStamp = System.currentTimeMillis();
        n.uid = uid;
        n.delay = delay;
        n.lost = lost;
        n.rxKBitRate = rxKBitRate;

        mRemoteATransList.add(n);
        return;
    }

    public synchronized void addRemoteVideoTransStata(int uid, int delay, int lost, int rxKBitRate) {
        SAgora.SRemoteVideoTransportStat n = new SAgora.SRemoteVideoTransportStat();

        n.timeStamp = System.currentTimeMillis();
        n.uid = uid;
        n.delay = delay;
        n.lost = lost;
        n.rxKBitRate = rxKBitRate;

        mRemoteVTransList.add(n);
        return;
    }

    public synchronized void addAudioSamplingInfo(SAgora.SAudioSamplingInfo o) {
        if (null == o) return;

        SAgora.SAudioSamplingInfo n = new SAgora.SAudioSamplingInfo();

        n.timeStamp = System.currentTimeMillis();


        n.meanAbsPCM = o.meanAbsPCM;
        n.maxAbsPCM = o.maxAbsPCM;
        n.smpCnt = o.smpCnt;
        n.chCnt = o.chCnt;
        n.smpRate = o.smpRate;
        n.pcmDuration = o.pcmDuration;
        n.statisticSpan = o.statisticSpan;

        mAudioSamplingInfoList.add(n);

        return;
    }


    public synchronized void addPlayerInfo(final int uid, final String filePath, final String midiPath,
                                           final long mixMusicBeginOffset, final boolean loopback,
                                           final boolean replace, final int cycle)
    {
        SAgora.SPlayerInfo n = new SAgora.SPlayerInfo();

        n.ts = System.currentTimeMillis();

        n.uid = uid;
        n.filePath = filePath;
        n.midiPath = midiPath;
        n.mixMusicBeginOffset = mixMusicBeginOffset;
        n.loopback = loopback;
        n.replace = replace;
        n.cycle = cycle;


        mPlayerInfo.add(n);
        return;
    }


    //for user event
    public synchronized void addUserEvent(SAgoraUserEvent e) {
        if (null == e) return;

        mUserEvenList.add(e);
        return;
    }

    public synchronized void addPingInfo(Skr.PingInfo e) {
        if (null == e) return;

        e.ts = System.currentTimeMillis();

        mPingInfoList.add(e);
        return;
    }

    public synchronized void addNetworkInfo(Skr.NetworkInfo e) {
        if (null == e) return;

        e.ts = System.currentTimeMillis();
        mNetworkInfoList.add(e);
        return;
    }


    private String getListString(List list) {
        String retStr = "";

        if (list.size() > 0) {
            for (Object e : list) {
                retStr += (mLinePrefix + e.toString());
            }
        }

        return retStr;
    }





    //SAgoraDataHolder's to String
    public synchronized String toString() {

        String retStr = "";

        retStr += getListString(mRtcStatsList);
        retStr += getListString(mLocalVList);
        retStr += getListString(mRemoteAList);
        retStr += getListString(mRemoteVList);
        retStr += getListString(mNetQualityList);
        retStr += getListString(mRemoteATransList);
        retStr += getListString(mRemoteVTransList);
        retStr += getListString(mAudioSamplingInfoList);
        retStr += getListString(mPlayerInfo);
        retStr += getListString(mUserEvenList);
        retStr += getListString(mPingInfoList);
        retStr += getListString(mNetworkInfoList);
        return retStr;
    }


    public synchronized SAgoraDataHolder reset() {

        mRtcStatsList.clear();
        mLocalVList.clear();
        mRemoteAList.clear();
        mRemoteVList.clear();
        mNetQualityList.clear();
        mRemoteATransList.clear();
        mRemoteVTransList.clear();

        mAudioSamplingInfoList.clear();
        mPlayerInfo.clear();
        mUserEvenList.clear();
        mPlayerInfo.clear();
        mNetworkInfoList.clear();
        return this;
    }

    //临时实验性API:
    public boolean need2Flush(){
        int recPerListLimit = 3;

        int nowTotalRecords = mRtcStatsList.size() + mLocalVList.size()+ mRemoteAList.size() +
                mRemoteVList.size() +  mNetQualityList.size() + mRemoteATransList.size() +  mRemoteVTransList.size()+
                mAudioSamplingInfoList.size();

        if (nowTotalRecords >= recPerListLimit * 11)
            return true;
        else
            return false;

    }

}