package com.engine;

import android.support.annotation.NonNull;

public class EngineEvent {
    public static final int TYPE_USER_SELF_JOIN_SUCCESS = 21;
    public static final int TYPE_USER_JOIN = 1;
    public static final int TYPE_USER_LEAVE = 2;
    public static final int TYPE_FIRST_REMOTE_VIDEO_DECODED = 3;
    public static final int TYPE_USER_MUTE_AUDIO = 4;
    public static final int TYPE_USER_MUTE_VIDEO = 5;
    public static final int TYPE_USER_REJOIN = 6;
    public static final int TYPE_USER_ROLE_CHANGE = 7;
    public static final int TYPE_USER_VIDEO_ENABLE = 8;
    public static final int TYPE_USER_AUDIO_VOLUME_INDICATION = 9;
    public static final int TYPE_USER_NETWORK_QUALITY_INDICATION = 22;

    public static final int TYPE_MUSIC_PLAY_START = 10;// 伴奏开始
    public static final int TYPE_MUSIC_PLAY_PAUSE = 11;// 伴奏暂停
    public static final int TYPE_MUSIC_PLAY_STOP = 12;// 伴奏停止
    public static final int TYPE_MUSIC_PLAY_FINISH = 13;// 伴奏结束
    public static final int TYPE_MUSIC_PLAY_TIME_FLY_LISTENER = 14;// 伴奏时间流逝
    public static final int TYPE_MUSIC_PLAY_FIRST_PKT = 15; // 伴奏首帧开始播放，仅在自采集模式下生效
    public static final int TYPE_MUSIC_PLAY_ERROR = 16; // 伴奏播放出错
    public static final int TYPE_MUSIC_PLAY_STATE_CHANGE = 17; // 伴奏播放状态变化

    public static final int TYPE_ENGINE_INITED = 100;   // 引擎初始化完毕
    public static final int TYPE_ENGINE_DESTROY = 99;   // 引擎销毁完毕
    public static final int TYPE_ENGINE_ERROR = 101;    // 引擎出现不可恢复错误

    public static final int TYPE_RECORD_FINISHED = 110; // 录制完成
    public static final int TYPE_RECORD_ERROR = 111;    // 录制出错
    public static final int TYPE_RECORD_START = 112;    // 录制开始
    public static final int TYPE_RECORD_AUDIO_FIRST_PKT = 113; // 录制首帧获取，仅在自采集模式下生效

    public static final int TYPE_CAMERA_OPENED = 200;// 相机打开
    public static final int TYPE_CAMERA_FIRST_FRAME_RENDERED = 201;//相机首帧渲染
    public static final int TYPE_CAMERA_FACING_CHANGED = 202;// 摄像头切换
    public static final int TYPE_CAMERA_ERROR = 203;// 摄像头出错

    public int type;
    public UserStatus userStatus;
    public Object obj;

    public EngineEvent(int type) {
        this.type = type;
    }

    public EngineEvent(int type, UserStatus userStatus) {
        this.type = type;
        this.userStatus = userStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public <T> T getObj() {
        return (T) obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public static class UserVolumeInfo {
        int uid;
        int volume;

        public UserVolumeInfo(int uid, int volume) {
            this.uid = uid;
            this.volume = volume;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        @Override
        public String toString() {
            return "UserVolumeInfo{" +
                    "uid=" + uid +
                    ", volume=" + volume +
                    '}';
        }
    }

    public static class MixMusicTimeInfo {
        int current;
        int duration;

        public MixMusicTimeInfo(int current, int duration) {
            this.current = current;
            this.duration = duration;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }

    public static class RoleChangeInfo {
        int oldRole;
        int newRole;//1 主播 2观众

        public RoleChangeInfo(int oldRole, int newRole) {
            this.oldRole = oldRole;
            this.newRole = newRole;
        }

        public int getOldRole() {
            return oldRole;
        }

        public void setOldRole(int oldRole) {
            this.oldRole = oldRole;
        }

        public int getNewRole() {
            return newRole;
        }

        public void setNewRole(int newRole) {
            this.newRole = newRole;
        }
    }

    /**
     * state	状态码：
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_PLAY(710)：音乐文件正常播放
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_PAUSED(711)：音乐文件暂停播放
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_STOPPED(713)：音乐文件停止播放
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_ERROR(714)：音乐文件报错。SDK 会在 errorCode 参数中返回具体的报错原因
     * errorCode	错误码：
     * MEDIA_ENGINE_AUDIO_ERROR_MIXING_OPEN(701)：音乐文件打开出错
     * MEDIA_ENGINE_AUDIO_ERROR_MIXING_TOO_FREQUENT(702)：音乐文件打开太频繁
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_INTERRUPTED_EOF(703)：音乐文件播放异常中断
     */
    public static class MusicStateChange{
        int state;
        int errorCode;

        public MusicStateChange(int state, int errorCode) {
            this.state = state;
            this.errorCode = errorCode;
        }

        public boolean isPlayOk() {
            if (state == 710) {
                return true;
            }
            return false;
        }

    }

    public static class NetworkQualityInfo {
        int uid;
        // >=4 时提醒用户
        int txQuality;
        int rxQuality;

        public NetworkQualityInfo(int uid, int txQuality, int rxQuality) {
            this.uid = uid;
            this.txQuality = txQuality;
            this.rxQuality = rxQuality;
        }

        @NonNull
        @Override
        public String toString() {
            return "NetworkQualityInfo uid: " + uid + " tx: " + txQuality + " rx: " + rxQuality;
        }
    }

    String getTypeDesc(int type) {
        switch (type) {
            case TYPE_USER_SELF_JOIN_SUCCESS:
                return "SELF_JOIN_SUCCESS";
            case TYPE_USER_JOIN:
                return "USER_JOIN";
            case TYPE_USER_LEAVE:
                return "USER_LEAVE";
            case TYPE_FIRST_REMOTE_VIDEO_DECODED:
                return "FIRST_REMOTE_VIDEO_DECODED";
            case TYPE_USER_MUTE_AUDIO:
                return "MUTE_AUDIO";
            case TYPE_USER_MUTE_VIDEO:
                return "MUTE_VIDEO";
            case TYPE_USER_REJOIN:
                return "REJOIN";
            case TYPE_USER_ROLE_CHANGE:
                return "ROLE_CHANGE";
            case TYPE_USER_VIDEO_ENABLE:
                return "VIDEO_ENABLE";
            case TYPE_USER_AUDIO_VOLUME_INDICATION:
                return "AUDIO_VOLUME_INDICATION";
            case TYPE_USER_NETWORK_QUALITY_INDICATION:
                return "NETWORK_QUALITY_INDICATION";
            case TYPE_MUSIC_PLAY_START:
                return "MUSIC_PLAY_START";
            case TYPE_MUSIC_PLAY_PAUSE:
                return "MUSIC_PLAY_PAUSE";
            case TYPE_MUSIC_PLAY_STOP:
                return "MUSIC_PLAY_STOP";
            case TYPE_MUSIC_PLAY_FINISH:
                return "MUSIC_PLAY_FINISH";
            case TYPE_MUSIC_PLAY_FIRST_PKT:
                return "MUSIC_PLAY_FIRST_PKT";
            case TYPE_MUSIC_PLAY_ERROR:
                return "MUSIC_PLAY_ERROR";
            case TYPE_ENGINE_DESTROY:
                return "ENGINE_DESTROY";
            case TYPE_ENGINE_INITED:
                return "ENGINE_INITED";
            case TYPE_ENGINE_ERROR:
                return "ENGINE_ERROR";
            case TYPE_RECORD_FINISHED:
                return "RECORD_FINISHED";
            case TYPE_RECORD_ERROR:
                return "RECORD_ERROR";
            case TYPE_RECORD_START:
                return "RECORD_START";
            case TYPE_RECORD_AUDIO_FIRST_PKT:
                return "RECORD_AUDIO_FIRST_PKT";
            case TYPE_CAMERA_OPENED:
                return "CAMERA_OPENED";
            case TYPE_CAMERA_FIRST_FRAME_RENDERED:
                return "CAMERA_FIRST_FRAME_RENDERED";
            case TYPE_CAMERA_FACING_CHANGED:
                return "CAMERA_FACING_CHANGED";
            case TYPE_CAMERA_ERROR:
                return "CAMERA_ERROR";
            case TYPE_MUSIC_PLAY_STATE_CHANGE:
                return "TYPE_MUSIC_PLAY_STATE_CHANGE";
        }
        return type + "";
    }

    @Override
    public String toString() {
        return "EngineEvent{" +
                "type=" + getTypeDesc(type) +
                " user=" + userStatus +
                '}';
    }
}
