package com.component.lyrics.event;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.common.log.MyLog;
import com.component.lyrics.LyricsReader;
import com.component.lyrics.model.LyricsLineInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.Map;

public class LyricEventLauncher {
    public final String TAG = "LyricEventLauncher";
    //模拟机器人打分事件
    static final int MSG_LYRIC_LINE_END_EVENT = 11;//某行结束
    static final int MSG_LYRIC_LINE_START_EVENT = 12;//某行开始
    static final int MSG_LYRIC_START_EVENT = 13;//倒计时结束，开始走动的事件

    Handler mUiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_LYRIC_START_EVENT) {
                LrcEvent.LyricStartEvent event = new LrcEvent.LyricStartEvent();
                event.lineNum = msg.arg1;
                event.extra = msg.obj;
                EventBus.getDefault().post(event);
            } else if (msg.what == MSG_LYRIC_LINE_START_EVENT) {
                LrcEvent.LyricLineStartEvent event = new LrcEvent.LyricLineStartEvent();
                event.lineNum = msg.arg1;
                event.extra = msg.obj;
                EventBus.getDefault().post(event);
            } else if (msg.what == MSG_LYRIC_LINE_END_EVENT) {
                LrcEvent.LineLineEndEvent event = new LrcEvent.LineLineEndEvent();
                event.lineNum = msg.arg1;
                event.extra = msg.obj;
                EventBus.getDefault().post(event);
            }
        }
    };

    public void destroy() {
        mUiHandler.removeCallbacksAndMessages(null);
    }

    public int postLyricEvent(LyricsReader lyricsReader, int accBeginTs, int accEndTs, Object extra) {
        MyLog.d(TAG, "postLyricEvent accBeginTs=" + accBeginTs + " accEndTs=" + accEndTs + " extra=" + extra);
        if (lyricsReader == null) {
            MyLog.d(TAG, "postLyricEvent lyricsReader is null");
            return 0;
        }

        Map<Integer, LyricsLineInfo> lyricsLineInfos = lyricsReader.getLrcLineInfos();
        Iterator<Map.Entry<Integer, LyricsLineInfo>> it = lyricsLineInfos.entrySet().iterator();
        mUiHandler.removeCallbacksAndMessages(null);
        int eventNum = 0;
        boolean hasSendLast = false;
        while (it.hasNext()) {
            Map.Entry<Integer, LyricsLineInfo> entry = it.next();
            int lineNum = entry.getKey();
            {
                if (lineNum == 0) {
                    Message msg = mUiHandler.obtainMessage(MSG_LYRIC_START_EVENT);
                    msg.arg1 = lineNum;
                    msg.obj = extra;
                    int t = entry.getValue().getStartTime() - accBeginTs;
                    if (t > 0) {
                        mUiHandler.sendMessageDelayed(msg, t);
                    } else {
                        mUiHandler.sendMessage(msg);
                    }
                }
            }
            {
                Message msg = mUiHandler.obtainMessage(MSG_LYRIC_LINE_START_EVENT);
                msg.arg1 = lineNum;
                msg.obj = extra;
                int t = entry.getValue().getStartTime() - accBeginTs;
                if (t > 0) {
                    mUiHandler.sendMessageDelayed(msg, t);
                } else {
//                    mUiHandler.sendMessage(msg);
                }
            }
            {
                Message msg = mUiHandler.obtainMessage(MSG_LYRIC_LINE_END_EVENT);
                msg.arg1 = lineNum;
                msg.obj = extra;
                //
                if (entry.getValue().getEndTime() > accEndTs) {
                    //dev 环境会一下把所有都发出来
                    // 这个 只要发一句就行了，保证最后一句歌词能打出分来
                    if (!hasSendLast) {
                        mUiHandler.sendMessageDelayed(msg, accEndTs - accBeginTs);
                        hasSendLast = true;
                    }
                } else {
                    int t = entry.getValue().getEndTime() - accBeginTs + 250;
                    if (t > 0) {
                        mUiHandler.sendMessageDelayed(msg, t);
                    } else {
                        if(entry.getValue().getEndTime()==0){
                            msg.arg1 = lineNum-1;
                            msg.obj = null;
                            mUiHandler.sendMessageDelayed(msg, accEndTs - accBeginTs);
                        }
                    }
                }
            }
            eventNum++;
        }
        MyLog.d(TAG, "postLyricEvent 一共" + eventNum + "行");
        return eventNum;
    }

    public static class LyricInfo {
        String lyricString;
        Object extra;
    }

}
