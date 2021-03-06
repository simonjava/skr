package com.common.player;

import android.media.MediaPlayer;

/**
 * Created by yangli on 2017/9/21.
 */
public interface IPlayerCallback {

    void onPrepared();

    void onCompletion();

    void onSeekComplete();

    void onVideoSizeChanged(int width, int height);

    void onError(int what, int extra);

    void onInfo(int what, int extra);

    void onBufferingUpdate(MediaPlayer mp, int percent);

    boolean openTimeFlyMonitor();

    void onTimeFlyMonitor(long pos,long duration);
}