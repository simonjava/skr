package com.module.playways.doubleplay.event;

import com.module.playways.doubleplay.pbLocalModel.LocalCombineRoomMusic;

public class StartDoubleGameEvent {
    LocalCombineRoomMusic mMusic;
    String nextDec;
    boolean hasNext = false;

    public StartDoubleGameEvent(LocalCombineRoomMusic music, String nextDec, boolean hasNext) {
        this.mMusic = music;
        this.nextDec = nextDec;
        this.hasNext = hasNext;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public LocalCombineRoomMusic getMusic() {
        return mMusic;
    }

    public String getNextDec() {
        return nextDec;
    }
}
