package com.common.milink.callback;

import com.common.log.MyLog;
import com.common.milink.event.MiLinkEvent;

import com.mi.milink.sdk.client.IEventListener;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by chengsimin on 16/7/1.
 */
public class MiLinkEventListener implements IEventListener {
    private static final java.lang.String TAG = "MiLinkEventListener";

    @Override
    public void onEventKickedByServer(int type, long l, String s) {
        MyLog.w(TAG, "onEventKickedByServer type = " + type);
        EventBus.getDefault().post(new MiLinkEvent.AccountKick(type,l,s));
    }

    @Override
    public void onEventGetServiceToken() {
        MyLog.w(TAG, "onEventGetServiceToken");
        EventBus.getDefault().post(new MiLinkEvent.AccountWantGetToken());
    }

    @Override
    public void onEventServiceTokenExpired() {
        MyLog.w(TAG, "onEventServiceTokenExpired  service token expired, passToken to get serviceToken");
        EventBus.getDefault().post(new MiLinkEvent.AccountTokenExpired());
    }

    @Override
    public void onEventShouldCheckUpdate() {
        MyLog.w(TAG, "onEventShouldCheckUpdate");
    }

    @Override
    public void onEventInvalidPacket() {
        MyLog.w(TAG, "onEventInvalidPacket invalid packet");
    }

    @Override
    public void onEventPermissionDenied() {
        MyLog.d(TAG,"onEventPermissionDenied" );
        // 移动网络开启，但是禁止app联网时会触发此提示，保证提示在app在前台时才弹出

    }
}
