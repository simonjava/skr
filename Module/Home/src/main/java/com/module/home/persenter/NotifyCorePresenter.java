package com.module.home.persenter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.common.anim.ObjectPlayControlTemplate;
import com.common.core.global.event.ShowDialogInHomeEvent;
import com.common.core.permission.SkrAudioPermission;
import com.common.core.permission.SkrCameraPermission;
import com.common.core.scheme.SchemeSdkActivity;
import com.common.core.scheme.event.BothRelationFromSchemeEvent;
import com.common.core.scheme.event.DoubleInviteFromSchemeEvent;
import com.common.core.scheme.event.GrabInviteFromSchemeEvent;
import com.common.core.userinfo.ResultCallback;
import com.common.core.userinfo.UserInfoManager;
import com.common.core.userinfo.model.UserInfoModel;
import com.common.floatwindow.FloatWindow;
import com.common.floatwindow.MoveType;
import com.common.floatwindow.Screen;
import com.common.floatwindow.ViewStateListenerAdapter;
import com.common.log.MyLog;
import com.common.mvp.PresenterEvent;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.notification.event.CRInviteInCreateRoomNotifyEvent;
import com.common.notification.event.CRRefuseInviteNotifyEvent;
import com.common.notification.event.CRSendInviteUserNotifyEvent;
import com.common.notification.event.CRSyncInviteUserNotifyEvent;
import com.common.notification.event.FeedCommentAddNotifyEvent;
import com.common.notification.event.FeedCommentLikeNotifyEvent;
import com.common.notification.event.FeedLikeNotifyEvent;
import com.common.notification.event.FollowNotifyEvent;
import com.common.notification.event.GrabInviteNotifyEvent;
import com.common.notification.event.SysWarnNotifyEvent;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.statistics.StatisticsAdapter;
import com.common.utils.ActivityUtils;
import com.common.utils.SpanUtils;
import com.common.utils.U;
import com.common.view.AnimateClickListener;
import com.component.busilib.manager.WeakRedDotManager;
import com.component.busilib.verify.SkrVerifyUtils;
import com.component.dialog.ConfirmDialog;
import com.component.dialog.NotifyDialogView;
import com.component.notification.DoubleInviteNotifyView;
import com.component.notification.FollowNotifyView;
import com.component.notification.GrabInviteNotifyView;
import com.dialog.view.TipsDialogView;
import com.module.RouterConstants;
import com.module.home.MainPageSlideApi;
import com.module.home.R;
import com.module.home.view.INotifyView;
import com.module.playways.IPlaywaysModeService;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.zq.live.proto.Common.EMsgRoomMediaType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.component.busilib.beauty.JumpBeautyFromKt.FROM_FRIEND_RECOMMEND;

public class NotifyCorePresenter extends RxLifeCyclePresenter {

    static final String TAG_INVITE_FOALT_WINDOW = "TAG_INVITE_FOALT_WINDOW";
    static final String TAG_RELATION_FOALT_WINDOW = "TAG_RELATION_FOALT_WINDOW";
    static final String TAG_DOUBLE_INVITE_FOALT_WINDOW = "TAG_DOUBLE_INVITE_FOALT_WINDOW";
    static final String TAG_DOUBLE_ROOM_INVITE_FOALT_WINDOW = "TAG_DOUBLE_ROOM_INVITE_FOALT_WINDOW";
    static final int MSG_DISMISS_INVITE_FLOAT_WINDOW = 2;
    static final int MSG_DISMISS_RELATION_FLOAT_WINDOW = 3;
    static final int MSG_DISMISS_DOUBLE_INVITE_FOALT_WINDOW = 4;      // 普通邀请
    static final int MSG_DISMISS_DOUBLE_ROOM_INVITE_FOALT_WINDOW = 5; // 邀请好友，在双人房中的邀请

    DialogPlus mBeFriendDialog;
    DialogPlus mSysWarnDialogPlus;

    INotifyView mINotifyView;

    MainPageSlideApi mMainPageSlideApi = ApiManager.getInstance().createService(MainPageSlideApi.class);

    SkrAudioPermission mSkrAudioPermission = new SkrAudioPermission();

    SkrCameraPermission mSkrCameraPermission = new SkrCameraPermission();

    SkrVerifyUtils mRealNameVerifyUtils = new SkrVerifyUtils();

    Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_DISMISS_INVITE_FLOAT_WINDOW:
                    FloatWindow.destroy(TAG_INVITE_FOALT_WINDOW);
                    break;
                case MSG_DISMISS_RELATION_FLOAT_WINDOW:
                    FloatWindow.destroy(TAG_RELATION_FOALT_WINDOW);
                    break;
                case MSG_DISMISS_DOUBLE_INVITE_FOALT_WINDOW:
                    FloatWindow.destroy(TAG_DOUBLE_INVITE_FOALT_WINDOW, 2);
                    break;
                case MSG_DISMISS_DOUBLE_ROOM_INVITE_FOALT_WINDOW:
                    FloatWindow.destroy(TAG_DOUBLE_ROOM_INVITE_FOALT_WINDOW, 2);
                    break;
            }
        }
    };

    ObjectPlayControlTemplate<FloatWindowData, NotifyCorePresenter> mFloatWindowDataFloatWindowObjectPlayControlTemplate = new ObjectPlayControlTemplate<FloatWindowData, NotifyCorePresenter>() {
        @Override
        protected NotifyCorePresenter accept(FloatWindowData cur) {
            if (FloatWindow.hasFollowWindowShow()) {
                return null;
            }
            if (!U.getActivityUtils().isAppForeground()) {
                MyLog.d(getTAG(), "在后台，不弹出通知");
                return null;
            }
            return NotifyCorePresenter.this;
        }

        @Override
        public void onStart(FloatWindowData floatWindowData, NotifyCorePresenter floatWindow) {
            if (floatWindowData.mType == FloatWindowData.Type.FOLLOW) {
                showFollowFloatWindow(floatWindowData);
            } else if (floatWindowData.mType == FloatWindowData.Type.GRABINVITE) {
                showGrabInviteFloatWindow(floatWindowData);
            } else if (floatWindowData.mType == FloatWindowData.Type.DOUBLE_GRAB_INVITE) {
                showDoubleInviteFloatWindow(floatWindowData);
            } else if (floatWindowData.mType == FloatWindowData.Type.DOUBLE_ROOM_INVITE) {
                showDoubleInviteFromRoomFloatWindow(floatWindowData);
            }
        }

        @Override
        protected void onEnd(FloatWindowData floatWindowData) {

        }
    };

    public NotifyCorePresenter(INotifyView iNotifyView) {
        mINotifyView = iNotifyView;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (mFloatWindowDataFloatWindowObjectPlayControlTemplate != null) {
            mFloatWindowDataFloatWindowObjectPlayControlTemplate.destroy();
        }
        if (mBeFriendDialog != null) {
            mBeFriendDialog.dismiss(false);
        }
        if (mSysWarnDialogPlus != null) {
            mSysWarnDialogPlus.dismiss(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GrabInviteFromSchemeEvent event) {
        // TODO: 2019/3/20   一场到底邀请 口令
        if (event.ask == 1) {
            // 需要再次确认弹窗
            UserInfoManager.getInstance().getUserInfoByUuid(event.ownerId, true, new ResultCallback<UserInfoModel>() {
                @Override
                public boolean onGetLocalDB(UserInfoModel o) {
                    return false;
                }

                @Override
                public boolean onGetServer(UserInfoModel userInfoModel) {
                    if (userInfoModel != null) {
                        Activity activity = U.getActivityUtils().getTopActivity();
                        if (activity instanceof SchemeSdkActivity) {
                            activity = U.getActivityUtils().getHomeActivity();
                        }
                        ConfirmDialog confirmDialog = new ConfirmDialog(activity
                                , userInfoModel, ConfirmDialog.TYPE_INVITE_CONFIRM);
                        confirmDialog.setListener(new ConfirmDialog.Listener() {
                            @Override
                            public void onClickConfirm(UserInfoModel userInfoModel) {
                                if (userInfoModel != null) {
                                    if (!userInfoModel.isFriend()) {
                                        MyLog.d(getTAG(), "同意邀请，强制成为好友" + userInfoModel);
                                        UserInfoManager.getInstance().beFriend(userInfoModel.getUserId(), null);
                                    }
                                }
                                tryGoGrabRoom(event.mediaType, event.roomId,event.tagId, 2);
                            }
                        });
                        confirmDialog.show();
                    }
                    return false;
                }
            });
        } else {
            // 不需要直接进
            tryGoGrabRoom(event.mediaType, event.roomId, event.tagId,2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DoubleInviteFromSchemeEvent event) {
        // 双人房间邀请口令
        if (event.ask == 1) {
            // 需要再次确认弹窗
            UserInfoManager.getInstance().getUserInfoByUuid(event.ownerId, true, new ResultCallback<UserInfoModel>() {
                @Override
                public boolean onGetLocalDB(UserInfoModel o) {
                    return false;
                }

                @Override
                public boolean onGetServer(UserInfoModel userInfoModel) {
                    if (userInfoModel != null) {
                        Activity activity = U.getActivityUtils().getTopActivity();
                        if (activity instanceof SchemeSdkActivity) {
                            activity = U.getActivityUtils().getHomeActivity();
                        }
                        ConfirmDialog confirmDialog = new ConfirmDialog(activity
                                , userInfoModel, ConfirmDialog.TYPE_DOUBLE_INVITE_CONFIRM);
                        confirmDialog.setListener(new ConfirmDialog.Listener() {
                            @Override
                            public void onClickConfirm(UserInfoModel userInfoModel) {
                                Observable.timer(500, TimeUnit.MILLISECONDS)
                                        .compose(NotifyCorePresenter.this.bindUntilEvent(PresenterEvent.DESTROY))
                                        .subscribe(new Consumer<Long>() {
                                            @Override
                                            public void accept(Long aLong) throws Exception {
                                                tryGoDoubleRoom(event.mediaType, event.ownerId, event.roomId, 2);
                                            }
                                        });
                            }
                        });
                        confirmDialog.show();
                    }
                    return false;
                }
            });
        } else {
            // 不需要直接进
            tryGoDoubleRoom(event.mediaType, event.ownerId, event.roomId, 2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BothRelationFromSchemeEvent event) {
        // TODO: 2019/3/25 成为好友的的口令
        MyLog.d(getTAG(), "onEvent" + " event=" + event);
        UserInfoManager.getInstance().getUserInfoByUuid(event.useId, true, new ResultCallback<UserInfoModel>() {
            @Override
            public boolean onGetLocalDB(UserInfoModel o) {
                return false;
            }

            @Override
            public boolean onGetServer(UserInfoModel userInfoModel) {
                if (userInfoModel != null) {
                    SpannableStringBuilder stringBuilder = new SpanUtils()
                            .append("是否确定与").setForegroundColor(Color.parseColor("#7F7F7F"))
                            .append("" + userInfoModel.getNicknameRemark()).setForegroundColor(Color.parseColor("#F5A623"))
                            .append("成为好友？").setForegroundColor(Color.parseColor("#7F7F7F"))
                            .create();
                    TipsDialogView tipsDialogView = new TipsDialogView.Builder(U.app())
                            .setMessageTip(stringBuilder)
                            .setConfirmTip("确定")
                            .setCancelTip("取消")
                            .setConfirmBtnClickListener(new AnimateClickListener() {
                                @Override
                                public void click(View view) {
                                    if (mBeFriendDialog != null) {
                                        mBeFriendDialog.dismiss(false);
                                    }
                                    if (userInfoModel.isFriend()) {
                                        U.getToastUtil().showShort("你们已经是好友了");
                                    } else {
                                        UserInfoManager.getInstance().beFriend(userInfoModel.getUserId(), null);
                                    }
                                }
                            })
                            .setCancelBtnClickListener(new AnimateClickListener() {
                                @Override
                                public void click(View view) {
                                    if (mBeFriendDialog != null) {
                                        mBeFriendDialog.dismiss(false);
                                    }
                                }
                            })
                            .build();

                    Activity activity = U.getActivityUtils().getTopActivity();
                    if (activity instanceof SchemeSdkActivity) {
                        activity = U.getActivityUtils().getHomeActivity();
                    }
                    mBeFriendDialog = DialogPlus.newDialog(activity)
                            .setContentHolder(new ViewHolder(tipsDialogView))
                            .setGravity(Gravity.BOTTOM)
                            .setContentBackgroundResource(R.color.transparent)
                            .setOverlayBackgroundResource(R.color.black_trans_80)
                            .setExpanded(false)
                            .create();
                    EventBus.getDefault().post(new ShowDialogInHomeEvent(mBeFriendDialog, 30));
                }
                return false;
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CRSendInviteUserNotifyEvent event) {
        FloatWindowData floatWindowData = new FloatWindowData(FloatWindowData.Type.DOUBLE_GRAB_INVITE);
        floatWindowData.setUserInfoModel(event.getUserInfoModel());
        floatWindowData.setExtra(event.msg);
        mFloatWindowDataFloatWindowObjectPlayControlTemplate.add(floatWindowData, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CRInviteInCreateRoomNotifyEvent event) {
        FloatWindowData floatWindowData = new FloatWindowData(FloatWindowData.Type.DOUBLE_ROOM_INVITE);
        floatWindowData.setMediaType(EMsgRoomMediaType.EMR_AUDIO.getValue());
        floatWindowData.setUserInfoModel(event.getUser());
        floatWindowData.setRoomID(event.getRoomID());
        floatWindowData.setExtra(event.getInviteMsg());
        mFloatWindowDataFloatWindowObjectPlayControlTemplate.add(floatWindowData, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FeedLikeNotifyEvent event) {
        WeakRedDotManager.getInstance().updateWeakRedRot(WeakRedDotManager.MESSAGE_FEED_LIKE_TYPE, 2);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FeedCommentLikeNotifyEvent event) {
        WeakRedDotManager.getInstance().updateWeakRedRot(WeakRedDotManager.MESSAGE_FEED_COMMENT_LIKE_TYPE, 2);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FeedCommentAddNotifyEvent event) {
        WeakRedDotManager.getInstance().updateWeakRedRot(WeakRedDotManager.MESSAGE_FEED_COMMENT_ADD_TYPE, 2);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CRSyncInviteUserNotifyEvent event) {
        IPlaywaysModeService iRankingModeService = (IPlaywaysModeService) ARouter.getInstance().build(RouterConstants.SERVICE_RANKINGMODE).navigation();
        if (iRankingModeService != null) {
            iRankingModeService.jumpToDoubleRoom(event);
        }
    }

    void tryGoGrabRoom(int mediaType, int roomID,int tagID, int inviteType) {
        if (mSkrAudioPermission != null) {
            mSkrAudioPermission.ensurePermission(new Runnable() {
                @Override
                public void run() {
                    if (mediaType == EMsgRoomMediaType.EMR_VIDEO.getValue()) {
                        // 视频房间
                        mSkrCameraPermission.ensurePermission(new Runnable() {
                            @Override
                            public void run() {
                                mRealNameVerifyUtils.checkJoinVideoPermission(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 进入视频预览
                                        ARouter.getInstance()
                                                .build(RouterConstants.ACTIVITY_BEAUTY_PREVIEW)
                                                .withInt("mFrom", FROM_FRIEND_RECOMMEND)
                                                .withInt("mRoomId", roomID)
                                                .withInt("mInviteType", inviteType)
                                                .navigation();

                                    }
                                });
                            }
                        }, true);
                    } else {
                        mRealNameVerifyUtils.checkJoinAudioPermission(tagID, new Runnable() {
                            @Override
                            public void run() {
                                IPlaywaysModeService iRankingModeService = (IPlaywaysModeService) ARouter.getInstance().build(RouterConstants.SERVICE_RANKINGMODE).navigation();
                                if (iRankingModeService != null) {
                                    iRankingModeService.tryGoGrabRoom(roomID, inviteType);
                                }
                            }
                        });
                    }

                }
            }, true);
        }
    }


    /**
     * @param mediaType
     * @param ownerId
     * @param roomID
     * @param inviteType 1是弹窗，2是剪切板的邀请
     */
    void tryGoDoubleRoom(int mediaType, int ownerId, int roomID, int inviteType) {
        mSkrAudioPermission.ensurePermission(new Runnable() {
            @Override
            public void run() {
                mRealNameVerifyUtils.checkJoinDoubleRoomPermission(new Runnable() {
                    @Override
                    public void run() {
                        final HashMap<String, Object> map = new HashMap<>();
                        map.put("peerUserID", ownerId);
                        map.put("roomID", roomID);
                        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
                        ApiMethods.subscribe(mMainPageSlideApi.enterInvitedDoubleFromCreateRoom(body), new ApiObserver<ApiResult>() {
                            @Override
                            public void process(ApiResult result) {
                                if (result.getErrno() == 0) {
                                    IPlaywaysModeService iRankingModeService = (IPlaywaysModeService) ARouter.getInstance().build(RouterConstants.SERVICE_RANKINGMODE).navigation();
                                    iRankingModeService.jumpToDoubleRoomFromDoubleRoomInvite(result.getData());
                                } else {
                                    U.getToastUtil().showShort(result.getErrmsg());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                MyLog.e(getTAG(), e);
                            }
                        }, NotifyCorePresenter.this);
                    }
                });
            }
        }, true);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FollowNotifyEvent event) {
        FloatWindowData floatWindowData = new FloatWindowData(FloatWindowData.Type.FOLLOW);
        floatWindowData.setUserInfoModel(event.mUserInfoModel);
        mFloatWindowDataFloatWindowObjectPlayControlTemplate.add(floatWindowData, true);

        WeakRedDotManager.getInstance().updateWeakRedRot(WeakRedDotManager.MESSAGE_FOLLOW_RED_ROD_TYPE, 2, true);
        StatisticsAdapter.recordCountEvent("social", "getfollow", null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SysWarnNotifyEvent event) {
        NotifyDialogView notifyDialogView = new NotifyDialogView(U.app(), event.getTitle(), event.getContent());
        Activity activity = U.getActivityUtils().getTopActivity();
        if (activity instanceof SchemeSdkActivity) {
            activity = U.getActivityUtils().getHomeActivity();
        }
        mSysWarnDialogPlus = DialogPlus.newDialog(activity)
                .setContentHolder(new ViewHolder(notifyDialogView))
                .setGravity(Gravity.CENTER)
                .setContentBackgroundResource(R.color.transparent)
                .setOverlayBackgroundResource(R.color.black_trans_80)
                .setExpanded(false)
                .create();
        EventBus.getDefault().post(new ShowDialogInHomeEvent(mSysWarnDialogPlus, 2));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GrabInviteNotifyEvent event) {
        // TODO: 2019/5/16 区分前台后台
        if (U.getActivityUtils().isAppForeground()) {
            FloatWindowData floatWindowData = new FloatWindowData(FloatWindowData.Type.GRABINVITE);
            floatWindowData.setUserInfoModel(event.mUserInfoModel);
            floatWindowData.setRoomID(event.roomID);
            floatWindowData.setTagID(event.tagID);
            floatWindowData.setMediaType(event.mediaType);
            mFloatWindowDataFloatWindowObjectPlayControlTemplate.add(floatWindowData, true);
        } else {
            // 展示一个通知
            mINotifyView.showNotify(event);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActivityUtils.ForeOrBackgroundChange event) {
        mFloatWindowDataFloatWindowObjectPlayControlTemplate.endCurrent(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CRRefuseInviteNotifyEvent event) {
        String remark = UserInfoManager.getInstance().getRemarkName(event.mUserInfoModel.getUserId(), event.mUserInfoModel.getNickname());
        U.getToastUtil().showShort("" + remark + event.refuseMsg);
    }

    void resendGrabInviterFloatWindowDismissMsg() {
        mUiHandler.removeMessages(MSG_DISMISS_INVITE_FLOAT_WINDOW);
        mUiHandler.sendEmptyMessageDelayed(MSG_DISMISS_INVITE_FLOAT_WINDOW, 5000);
    }

    void showGrabInviteFloatWindow(FloatWindowData floatWindowData) {
        UserInfoModel userInfoModel = floatWindowData.getUserInfoModel();

        resendGrabInviterFloatWindowDismissMsg();
        GrabInviteNotifyView grabInviteNotifyView = new GrabInviteNotifyView(U.app());
        grabInviteNotifyView.bindData(userInfoModel);
        grabInviteNotifyView.setListener(new GrabInviteNotifyView.Listener() {
            @Override
            public void onIgnore() {
                mUiHandler.removeMessages(MSG_DISMISS_INVITE_FLOAT_WINDOW);
                FloatWindow.destroy(TAG_INVITE_FOALT_WINDOW);
            }

            @Override
            public void onAgree() {
                tryGoGrabRoom(floatWindowData.mMediaType, floatWindowData.mRoomID,floatWindowData.mTagID, 1);
                mUiHandler.removeMessages(MSG_DISMISS_INVITE_FLOAT_WINDOW);
                FloatWindow.destroy(TAG_INVITE_FOALT_WINDOW);
            }
        });
        FloatWindow.with(U.app())
                .setView(grabInviteNotifyView)
                .setMoveType(MoveType.canRemove)
                .setWidth(Screen.width, 1f)                               //设置控件宽高
                .setHeight(Screen.height, 0.2f)
                .setViewStateListener(new ViewStateListenerAdapter() {
                    @Override
                    public void onDismiss(int dismissReason) {
                        mFloatWindowDataFloatWindowObjectPlayControlTemplate.endCurrent(floatWindowData);
                    }

                    @Override
                    public void onPositionUpdate(int x, int y) {
                        super.onPositionUpdate(x, y);
                        resendGrabInviterFloatWindowDismissMsg();
                    }
                })
                .setDesktopShow(false)                        //桌面显示
                .setCancelIfExist(false)
                .setReqPermissionIfNeed(false)
                .setTag(TAG_INVITE_FOALT_WINDOW)
                .build();
    }

    void resendDoubleInviterFloatWindowDismissMsg() {
        mUiHandler.removeMessages(MSG_DISMISS_DOUBLE_INVITE_FOALT_WINDOW);
        mUiHandler.sendEmptyMessageDelayed(MSG_DISMISS_DOUBLE_INVITE_FOALT_WINDOW, 5000);
    }

    void showDoubleInviteFloatWindow(FloatWindowData floatWindowData) {
        UserInfoModel userInfoModel = floatWindowData.getUserInfoModel();

        resendDoubleInviterFloatWindowDismissMsg();
        DoubleInviteNotifyView doubleInviteNotifyView = new DoubleInviteNotifyView(U.app());
        doubleInviteNotifyView.bindData(userInfoModel, floatWindowData.getExtra());
        doubleInviteNotifyView.setListener(new DoubleInviteNotifyView.Listener() {
            @Override
            public void onClickAgree() {
                mUiHandler.removeMessages(MSG_DISMISS_DOUBLE_INVITE_FOALT_WINDOW);
                FloatWindow.destroy(TAG_DOUBLE_INVITE_FOALT_WINDOW);
                mSkrAudioPermission.ensurePermission(new Runnable() {
                    @Override
                    public void run() {
                        mRealNameVerifyUtils.checkJoinDoubleRoomPermission(new Runnable() {
                            @Override
                            public void run() {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("peerUserID", userInfoModel.getUserId());
                                RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
                                ApiMethods.subscribe(mMainPageSlideApi.enterInvitedDoubleRoom(body), new ApiObserver<ApiResult>() {
                                    @Override
                                    public void process(ApiResult result) {
                                        if (result.getErrno() == 0) {
                                            IPlaywaysModeService iRankingModeService = (IPlaywaysModeService) ARouter.getInstance().build(RouterConstants.SERVICE_RANKINGMODE).navigation();
                                            iRankingModeService.jumpToDoubleRoom(result.getData());
                                        } else {
                                            U.getToastUtil().showShort(result.getErrmsg());
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        MyLog.e(getTAG(), e);
                                    }
                                }, NotifyCorePresenter.this);
                            }
                        });
                    }
                }, true);

            }
        });

        FloatWindow.with(U.app())
                .setView(doubleInviteNotifyView)
                .setMoveType(MoveType.canRemove)
                .setWidth(Screen.width, 1f)                               //设置控件宽高
                .setHeight(Screen.height, 0.2f)
                .setViewStateListener(new ViewStateListenerAdapter() {
                    @Override
                    public void onDismiss(int dismissReason) {
                        mFloatWindowDataFloatWindowObjectPlayControlTemplate.endCurrent(floatWindowData);
                        if (dismissReason != 0) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("peerUserID", userInfoModel.getUserId());
                            if (dismissReason == 1) {
                                map.put("refuseType", 1); //主动拒绝
                            } else {
                                map.put("refuseType", 2);
                            }
                            RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
                            ApiMethods.subscribe(mMainPageSlideApi.refuseInvitedDoubleRoom(body), new ApiObserver<ApiResult>() {
                                @Override
                                public void process(ApiResult result) {
                                    if (result.getErrno() == 0) {
                                        MyLog.w(getTAG(), "process" + " result=" + result);
                                    } else {
                                        MyLog.w(getTAG(), "process" + " result=" + result);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    MyLog.e(getTAG(), e);
                                }
                            }, NotifyCorePresenter.this);
                        }
                    }

                    @Override
                    public void onPositionUpdate(int x, int y) {
                        super.onPositionUpdate(x, y);
                        resendDoubleInviterFloatWindowDismissMsg();
                    }
                })
                .setDesktopShow(false)                        //桌面显示
                .setCancelIfExist(false)
                .setReqPermissionIfNeed(false)
                .setTag(TAG_DOUBLE_INVITE_FOALT_WINDOW)
                .build();
    }


    void resendDoubleRoomFloatWindowDismissMsg() {
        mUiHandler.removeMessages(MSG_DISMISS_DOUBLE_ROOM_INVITE_FOALT_WINDOW);
        mUiHandler.sendEmptyMessageDelayed(MSG_DISMISS_DOUBLE_ROOM_INVITE_FOALT_WINDOW, 5000);
    }

    void showDoubleInviteFromRoomFloatWindow(FloatWindowData floatWindowData) {
        UserInfoModel userInfoModel = floatWindowData.getUserInfoModel();

        resendDoubleRoomFloatWindowDismissMsg();
        DoubleInviteNotifyView doubleInviteNotifyView = new DoubleInviteNotifyView(U.app());
        doubleInviteNotifyView.bindData(userInfoModel, floatWindowData.getExtra());
        doubleInviteNotifyView.setListener(new DoubleInviteNotifyView.Listener() {
            @Override
            public void onClickAgree() {
                mUiHandler.removeMessages(MSG_DISMISS_DOUBLE_ROOM_INVITE_FOALT_WINDOW);
                FloatWindow.destroy(TAG_DOUBLE_ROOM_INVITE_FOALT_WINDOW);
                tryGoDoubleRoom(floatWindowData.mMediaType, userInfoModel.getUserId(), floatWindowData.getRoomID(), 1);
            }
        });

        FloatWindow.with(U.app())
                .setView(doubleInviteNotifyView)
                .setMoveType(MoveType.canRemove)
                .setWidth(Screen.width, 1f)                               //设置控件宽高
                .setHeight(Screen.height, 0.2f)
                .setViewStateListener(new ViewStateListenerAdapter() {
                    @Override
                    public void onDismiss(int dismissReason) {
                        mFloatWindowDataFloatWindowObjectPlayControlTemplate.endCurrent(floatWindowData);
                    }

                    @Override
                    public void onPositionUpdate(int x, int y) {
                        super.onPositionUpdate(x, y);
                        resendDoubleRoomFloatWindowDismissMsg();
                    }
                })
                .setDesktopShow(false)                        //桌面显示
                .setCancelIfExist(false)
                .setReqPermissionIfNeed(false)
                .setTag(TAG_DOUBLE_ROOM_INVITE_FOALT_WINDOW)
                .build();
    }

    void resendFollowFloatWindowDismissMsg() {
        mUiHandler.removeMessages(MSG_DISMISS_RELATION_FLOAT_WINDOW);
        mUiHandler.sendEmptyMessageDelayed(MSG_DISMISS_RELATION_FLOAT_WINDOW, 5000);
    }

    void showFollowFloatWindow(FloatWindowData floatWindowData) {
        UserInfoModel userInfoModel = floatWindowData.getUserInfoModel();
        resendFollowFloatWindowDismissMsg();
        FollowNotifyView relationNotifyView = new FollowNotifyView(U.app());
        relationNotifyView.bindData(userInfoModel);
        relationNotifyView.setListener(new FollowNotifyView.Listener() {
            @Override
            public void onFollowBtnClick() {
                // 不消失
//                mUiHandler.removeMessages(MSG_DISMISS_RELATION_FLOAT_WINDOW);
//                FloatWindow.destroy(TAG_RELATION_FOALT_WINDOW);
            }
        });
        FloatWindow.with(U.app())
                .setView(relationNotifyView)
                .setMoveType(MoveType.canRemove)
                .setWidth(Screen.width, 1f)                               //设置控件宽高
                .setHeight(Screen.height, 0.2f)
                .setViewStateListener(new ViewStateListenerAdapter() {
                    @Override
                    public void onDismiss(int dismissReason) {
                        mFloatWindowDataFloatWindowObjectPlayControlTemplate.endCurrent(floatWindowData);
                    }

                    @Override
                    public void onPositionUpdate(int x, int y) {
                        super.onPositionUpdate(x, y);
                        resendFollowFloatWindowDismissMsg();
                    }
                })
                .setDesktopShow(false)                        //桌面显示
                .setCancelIfExist(false)
                .setReqPermissionIfNeed(false)
                .setTag(TAG_RELATION_FOALT_WINDOW)
                .build();
    }

    public static class FloatWindowData {
        private UserInfoModel mUserInfoModel;
        private Type mType;
        private int mRoomID;
        private int mTagID;
        private int mMediaType;
        private String mExtra;

        public String getExtra() {
            return mExtra;
        }

        public void setExtra(String extra) {
            mExtra = extra;
        }

        public void setUserInfoModel(UserInfoModel userInfoModel) {
            mUserInfoModel = userInfoModel;
        }

        public UserInfoModel getUserInfoModel() {
            return mUserInfoModel;
        }

        public FloatWindowData(Type type) {
            mType = type;
        }

        public void setRoomID(int roomID) {
            mRoomID = roomID;
        }

        public int getRoomID() {
            return mRoomID;
        }

        public int getTagID() {
            return mTagID;
        }

        public void setTagID(int tagID) {
            mTagID = tagID;
        }

        public int getMediaType() {
            return mMediaType;
        }

        public void setMediaType(int mediaType) {
            mMediaType = mediaType;
        }

        @Override
        public String toString() {
            return "FloatWindowData{" +
                    "mUserInfoModel=" + mUserInfoModel +
                    ", mType=" + mType +
                    ", mRoomID=" + mRoomID +
                    '}';
        }

        /**
         * DOUBLE_GRAB_INVITE是一场到底里的邀请
         * DOUBLE_ROOM_INVITE是唱聊房里的邀请
         */
        public enum Type {
            FOLLOW, GRABINVITE, DOUBLE_GRAB_INVITE, DOUBLE_ROOM_INVITE,
        }
    }
}
