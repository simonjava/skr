package com.mi.liveassistant.room.user;

import android.text.TextUtils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mi.liveassistant.common.api.ErrorCode;
import com.mi.liveassistant.common.log.MyLog;
import com.mi.liveassistant.data.model.User;
import com.mi.liveassistant.proto.LiveCommonProto;
import com.mi.liveassistant.proto.UserProto;
import com.mi.liveassistant.room.user.callback.IUserCallback;
import com.mi.liveassistant.room.user.request.HomepageRequest;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class UserInfoManager {
    private static final String TAG = UserInfoManager.class.getSimpleName();

    public UserInfoManager() {
    }

    /**
     * 获取一个用户的信息
     */
    public void getUserByUuid(final long uuid, final IUserCallback callback) {
        MyLog.d(TAG, "getUserByUuid uuid=" + uuid);
        Observable.just(0)
                .map(new Func1<Integer, UserProto.GetHomepageRsp>() {
                    @Override
                    public UserProto.GetHomepageRsp call(Integer integer) {
                        UserProto.GetHomepageRsp rsp = new HomepageRequest(uuid).syncRsp();
                        return rsp;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserProto.GetHomepageRsp>() {
                    @Override
                    public void call(UserProto.GetHomepageRsp rsp) {
                        int errCode = ErrorCode.CODE_ERROR_NORMAL;
                        if (rsp != null && (errCode = rsp.getRetCode()) == ErrorCode.CODE_SUCCESS) {
                            callback.notifySuccess(parse(rsp));
                        } else {
                            callback.notifyFail(errCode);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        MyLog.e(throwable);
                    }
                });
    }

    private User parse(UserProto.GetHomepageRsp rsp) {
        com.mi.liveassistant.data.confuse.User user = new com.mi.liveassistant.data.confuse.User();
        user.parse(rsp.getPersonalInfo());
        user.parse(rsp.getPersonalData());
        try {
            user.parse(LiveCommonProto.UserRoomInfo.parseFrom(rsp.getRoomInfo()));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (rsp.getRankTopThreeListList() != null) {
            user.setRankTopThreeList(rsp.getRankTopThreeListList());
        }
        if (!TextUtils.isEmpty(rsp.getViewUrl())) {
            user.setViewUrl(rsp.getViewUrl());
        }
        if (!TextUtils.isEmpty(rsp.getRoomId())) {
            user.setRoomId(rsp.getRoomId());
        }
        return user.toLite();
    }
}