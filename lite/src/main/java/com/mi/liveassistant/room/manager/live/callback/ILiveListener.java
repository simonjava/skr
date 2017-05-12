package com.mi.liveassistant.room.manager.live.callback;

/**
 * Created by yangli on 2017/5/9.
 */
public interface ILiveListener {
    /**
     * 直播异常结束
     *
     * @param errCode 服务器返回的错误码
     * @param errMsg  错误描述
     */
    void onEndUnexpected(int errCode, String errMsg);
}
