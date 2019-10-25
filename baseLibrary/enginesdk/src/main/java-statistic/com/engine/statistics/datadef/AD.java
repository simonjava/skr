package com.engine.statistics.datadef;


import com.engine.statistics.SUtils;

import io.agora.rtc.IRtcEngineEventHandler;



public class AD //means AgoraData
{


    //将数值转回为相应的语义字符串
    protected static String transNetQuality(int quality) {
        if (0 == quality) return "质量未知value(0)";
        else if (1 == quality) return "质量极好";
        else if (2 == quality) return "用户主观感觉和极好差不多，但码率可能略低于极好";
        else if (3 == quality) return "用户主观感受有瑕疵但不影响沟通";
        else if (4 == quality) return "勉强能沟通但不顺畅";
        else if (5 == quality) return "网络质量非常差，基本不能沟通";
        else if (6 == quality) return "网络连接断开，完全无法沟通";
        else if (8 == quality) return "中间态：检测网络中value(8)";
        else return "声网没有定义的质量值value("+quality+")";
    }


    public static class SAgoraRTCStats extends IRtcEngineEventHandler.RtcStats {
        public long timeStamp;

        public String toString() {
            return SUtils.transTime(timeStamp)+" AD.SAgoraRTCStats: totalDuration="+totalDuration+
                    ", txBytes=" + txBytes +
                    ", rxBytes=" + rxBytes +
                    ", txKBitRate=" + txKBitRate +
                    ", rxKBitRate=" + rxKBitRate +
                    ", txAudioKBitRate=" + txAudioKBitRate +
                    ", rxAudioKBitRate=" + rxAudioKBitRate +
                    ", txVideoKBitRate=" + txVideoKBitRate +
                    ", rxVideoKBitRate=" + rxVideoKBitRate +
                    ", users=" + users +
                    ", lastmileDelay=" + lastmileDelay +
                    ", txPacketLossRate=" + txPacketLossRate +
                    ", rxPacketLossRate=" + rxPacketLossRate +
                    ", cpuTotalUsage=" + cpuTotalUsage +
                    ", cpuAppUsage=" + cpuAppUsage + "\n";

        }
    }


    public static class SAgoraLocalVideoStats extends IRtcEngineEventHandler.LocalVideoStats {
        public long timeStamp;
        public String toString() {
            return SUtils.transTime(timeStamp)+" AD.SAgoraLocalVideoStats: sentBitrate=" + sentBitrate +
                    ", sentFrameRate=" + sentFrameRate+
                    ", encoderOutputFrameRate=" + encoderOutputFrameRate+
                    ", rendererOutputFrameRate=" + rendererOutputFrameRate+
                    ", targetBitrate=" + targetBitrate+
                    ", targetFrameRate=" + targetFrameRate+
                    ", qualityAdaptIndication=" + qualityAdaptIndication+"\n";
        }
    }

    public static class SAgoraRemoteAudioStats extends IRtcEngineEventHandler.RemoteAudioStats {
        public long timeStamp;
//        public String strQuality; //replace base class's "quality" for explicitly meanning

        public String toString() {

            return SUtils.transTime(timeStamp)+" AD.SAgoraRemoteAudioStats: uid=" + uid +
                    ", quality=" + transNetQuality(quality) +
                    ", networkTransportDelay=" + networkTransportDelay+
                    ", jitterBufferDelay=" + jitterBufferDelay+
                    ", audioLossRate=" + audioLossRate+"\n";
        }
    }

    public static class SAgoraRemoteVideoStats extends IRtcEngineEventHandler.RemoteVideoStats {
        public long timeStamp;
        public String toString() {
            return SUtils.transTime(timeStamp)+" AD.SAgoraRemoteVideoStats: uid="+ uid +
                    ", width=" +width+
                    ", height=" +height+
                    ", receivedBitrate=" +receivedBitrate+
                    ", decoderOutputFrameRate=" +decoderOutputFrameRate+
                    ", rendererOutputFrameRate=" +rendererOutputFrameRate+
                    ", rxStreamType=" +rxStreamType+"\n";
        }
    }


    public static class SAgoraRemoteVideoTransportStat{
        public long timeStamp;
        public int 	uid;
        public int 	delay;
        public int 	lost;
        public int 	rxKBitRate;

        public String toString() {
            return SUtils.transTime(timeStamp)+" AD.SAgoraRemoteVideoTransportStat: uid="+uid+"， delay="+delay+"， lost="+lost+"， rxKBitRate="+rxKBitRate + "\n";
        }
    }


    public static class SAgoraRemoteAudioTransportStats{
        public long timeStamp;
        public int 	uid;
        public int 	delay;
        public int 	lost;
        public int 	rxKBitRate;

        public String toString(){
            return SUtils.transTime(timeStamp)+" AD.SAgoraRemoteAudioTransportStats: uid="+uid+"， delay="+delay+"， lost="+lost+"， rxKBitRate="+rxKBitRate + "\n";
        }
    }

    public static class SAgoraNetworkQuality {
        public long timeStamp;
        public int  uid = -1;
        public int 	txQuality=0;
        public int 	rxQuality=0;



        public String toString(){

            return SUtils.transTime(timeStamp)+" AD.SAgoraNetworkQuality: uid="+uid+", txQuality="+transNetQuality(txQuality)+", rxQuality="+transNetQuality(rxQuality) + "\n";
        }
    } //end of class AD.SAgoraNetworkQuality






}
