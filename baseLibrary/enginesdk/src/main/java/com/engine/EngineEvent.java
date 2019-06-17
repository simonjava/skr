package com.engine;

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

    public static final int TYPE_MUSIC_PLAY_START = 10;// 伴奏开始
    public static final int TYPE_MUSIC_PLAY_PAUSE = 11;// 伴奏暂停
    public static final int TYPE_MUSIC_PLAY_STOP = 12;// 伴奏停止
    public static final int TYPE_MUSIC_PLAY_FINISH = 13;// 伴奏结束

    public static final int TYPE_MUSIC_PLAY_TIME_FLY_LISTENER = 14;// 伴奏时间流逝

    public static final int TYPE_ENGINE_DESTROY = 99;

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
        return (T)obj;
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
        int newRole;

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

    String getTypeDesc(int type){
        switch (type){
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
            case TYPE_MUSIC_PLAY_START:
                return "MUSIC_PLAY_START";
            case TYPE_MUSIC_PLAY_PAUSE:
                return "MUSIC_PLAY_PAUSE";
            case TYPE_MUSIC_PLAY_STOP:
                return "MUSIC_PLAY_STOP";
            case TYPE_MUSIC_PLAY_FINISH:
                return "USIC_PLAY_FINISH";
            case TYPE_ENGINE_DESTROY:
                return "ENGINE_DESTROY";
        }
        return type+"";
    }

    @Override
    public String toString() {
        return "EngineEvent{" +
                "type=" + getTypeDesc(type) +
                " user="+userStatus+
                '}';
    }
}
