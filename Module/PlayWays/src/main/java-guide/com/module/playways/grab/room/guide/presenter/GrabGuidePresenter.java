package com.module.playways.grab.room.guide.presenter;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import com.common.core.account.UserAccountManager;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.userinfo.model.UserInfoModel;
import com.common.engine.ScoreConfig;
import com.common.log.MyLog;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.player.IPlayer;
import com.common.player.VideoPlayerAdapter;
import com.common.player.exoplayer.ExoPlayer;
import com.common.player.mediaplayer.AndroidMediaPlayer;
import com.common.utils.ActivityUtils;
import com.common.utils.SpanUtils;
import com.common.utils.U;
import com.component.busilib.SkrConfig;
import com.component.busilib.constans.GameModeType;
import com.engine.EngineEvent;
import com.engine.Params;
import com.engine.arccloud.RecognizeConfig;
import com.module.ModuleServiceManager;
import com.module.msg.CustomMsgType;
import com.module.msg.IMsgService;
import com.module.playways.BuildConfig;
import com.module.playways.RoomDataUtils;
import com.module.playways.grab.room.GrabRoomData;
import com.module.playways.grab.room.event.GrabGameOverEvent;
import com.module.playways.grab.room.event.GrabPlaySeatUpdateEvent;
import com.module.playways.grab.room.event.GrabRoundChangeEvent;
import com.module.playways.grab.room.event.GrabRoundStatusChangeEvent;
import com.module.playways.grab.room.event.GrabSomeOneLightBurstEvent;
import com.module.playways.grab.room.event.GrabSomeOneLightOffEvent;
import com.module.playways.grab.room.event.GrabWaitSeatUpdateEvent;
import com.module.playways.grab.room.event.SomeOneJoinWaitSeatEvent;
import com.module.playways.grab.room.event.SomeOneLeavePlaySeatEvent;
import com.module.playways.grab.room.guide.IGrabGuideView;
import com.module.playways.grab.room.model.BLightInfoModel;
import com.module.playways.grab.room.model.ChorusRoundInfoModel;
import com.module.playways.grab.room.model.GrabPlayerInfoModel;
import com.module.playways.grab.room.model.GrabRoundInfoModel;
import com.module.playways.grab.room.model.GrabSkrResourceModel;
import com.module.playways.grab.room.model.MLightInfoModel;
import com.module.playways.grab.room.model.WantSingerInfo;
import com.module.playways.others.LyricAndAccMatchManager;
import com.module.playways.room.gift.event.GiftBrushMsgEvent;
import com.module.playways.room.msg.event.GiftPresentEvent;
import com.module.playways.room.msg.event.MachineScoreEvent;
import com.module.playways.room.msg.event.QChoGiveUpEvent;
import com.module.playways.room.msg.event.QCoinChangeEvent;
import com.module.playways.room.msg.event.QExitGameMsgEvent;
import com.module.playways.room.msg.event.QGetSingChanceMsgEvent;
import com.module.playways.room.msg.event.QJoinNoticeEvent;
import com.module.playways.room.msg.event.QLightBurstMsgEvent;
import com.module.playways.room.msg.event.QLightOffMsgEvent;
import com.module.playways.room.msg.event.QPkInnerRoundOverEvent;
import com.module.playways.room.msg.event.QRoundAndGameOverMsgEvent;
import com.module.playways.room.msg.event.QRoundOverMsgEvent;
import com.module.playways.room.msg.event.QWantSingChanceMsgEvent;
import com.module.playways.room.msg.filter.PushMsgFilter;
import com.module.playways.room.msg.manager.ChatRoomMsgManager;
import com.module.playways.room.prepare.model.BaseRoundInfoModel;
import com.module.playways.room.prepare.model.PlayerInfoModel;
import com.module.playways.room.room.comment.model.CommentLightModel;
import com.module.playways.room.room.comment.model.CommentSysModel;
import com.module.playways.room.room.comment.model.CommentTextModel;
import com.module.playways.room.room.event.PretendCommentMsgEvent;
import com.module.playways.room.room.score.MachineScoreItem;
import com.module.playways.room.room.score.RobotScoreHelper;
import com.module.playways.room.song.model.SongModel;
import com.zq.live.proto.Common.ESex;
import com.zq.live.proto.Common.UserInfo;
import com.zq.live.proto.GrabRoom.EMsgPosType;
import com.zq.live.proto.GrabRoom.EQGameOverReason;
import com.zq.live.proto.GrabRoom.EQRoundOverReason;
import com.zq.live.proto.GrabRoom.EQRoundResultType;
import com.zq.live.proto.GrabRoom.EQRoundStatus;
import com.zq.live.proto.GrabRoom.ERoomMsgType;
import com.zq.live.proto.GrabRoom.EWantSingType;
import com.zq.live.proto.GrabRoom.MachineScore;
import com.zq.live.proto.GrabRoom.RoomMsg;
import com.component.lyrics.utils.SongResUtils;
import com.component.lyrics.utils.ZipUrlResourceManager;
import com.component.mediaengine.kit.ZqEngineKit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.File;
import java.util.List;

import io.agora.rtc.Constants;

public class GrabGuidePresenter extends RxLifeCyclePresenter {
    public String TAG = "GrabGuidePresenter";

    static final int MSG_ROBOT_SING_BEGIN = 10;

    static final int MSG_ENSURE_SWITCH_BROADCAST_SUCCESS = 21; // ??????????????????????????????????????????????????????????????????

    long mFirstKickOutTime = -1; //???????????????????????????????????????????????????????????????

    int mAbsenTimes = 0;

    GrabRoomData mRoomData;

    IGrabGuideView mIGrabView;

    RobotScoreHelper mRobotScoreHelper;

    boolean mDestroyed = false;

    IPlayer mExoPlayer;

    ZipUrlResourceManager mZipUrlResourceManager;

    Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ROBOT_SING_BEGIN:
                    robotSingBegin();
                    break;
                case MSG_ENSURE_SWITCH_BROADCAST_SUCCESS:
                    onChangeBroadcastSuccess();
                    break;
            }
        }
    };

    PushMsgFilter mPushMsgFilter = new PushMsgFilter() {
        @Override
        public boolean doFilter(RoomMsg msg) {
            if (msg != null && msg.getRoomID() == mRoomData.getGameId()) {
                return true;
            }
            return false;
        }
    };


    public GrabGuidePresenter(@NotNull IGrabGuideView iGrabView, @NotNull GrabRoomData roomData) {
        mIGrabView = iGrabView;
        mRoomData = roomData;
        TAG = "GrabCorePresenter";
        ChatRoomMsgManager.getInstance().addFilter(mPushMsgFilter);
        joinRoomAndInit(true);
    }


    /**
     * ??????????????????
     * ??????????????????
     * ??????????????????
     */
    private void joinRoomAndInit(boolean first) {
        MyLog.w(TAG, "joinRoomAndInit" + " first=" + first + ", gameId is " + mRoomData.getGameId());
        mFirstKickOutTime = -1;
        mAbsenTimes = 0;

        if (mRoomData.getGameId() > 0) {
            if (first) {
                Params params = Params.getFromPref();
//            params.setStyleEnum(Params.AudioEffect.none);
                params.setScene(Params.Scene.grab);
                ZqEngineKit.getInstance().init("grabroom", params);
            }
            ZqEngineKit.getInstance().joinRoom(String.valueOf(mRoomData.getGameId()), (int) UserAccountManager.getInstance().getUuidAsLong(), mRoomData.isOwner(), mRoomData.getAgoraToken());
            // ?????????????????????, ??????????????????????????????
            ZqEngineKit.getInstance().muteLocalAudioStream(true);
        }
        if (mRoomData.getGameId() > 0) {
            for (GrabPlayerInfoModel playerInfoModel : mRoomData.getPlayerInfoList()) {
                if (!playerInfoModel.isOnline()) {
                    continue;
                }
                pretendEnterRoom(playerInfoModel);
            }
            pretendRoomNameSystemMsg(mRoomData.getRoomName(), CommentSysModel.TYPE_ENTER_ROOM);
        }
    }

    private void pretendEnterRoom(GrabPlayerInfoModel playerInfoModel) {
        CommentTextModel commentModel = new CommentTextModel();
        commentModel.setUserId(playerInfoModel.getUserInfo().getUserId());
        commentModel.setAvatar(playerInfoModel.getUserInfo().getAvatar());
        commentModel.setUserName(playerInfoModel.getUserInfo().getNicknameRemark());
        commentModel.setAvatarColor(Color.WHITE);
        SpannableStringBuilder stringBuilder;
        if (playerInfoModel.getUserInfo().getUserId() == UserAccountManager.SYSTEM_GRAB_ID) {
            stringBuilder = new SpanUtils()
                    .append(playerInfoModel.getUserInfo().getNicknameRemark() + " ").setForegroundColor(Color.parseColor("#DF7900"))
                    .append("?????????????????????????????????????????????????????????????????????~").setForegroundColor(Color.parseColor("#586D94"))
                    .create();
        } else {
            SpanUtils spanUtils = new SpanUtils()
                    .append(playerInfoModel.getUserInfo().getNicknameRemark() + " ").setForegroundColor(Color.parseColor("#DF7900"))
                    .append("???????????????").setForegroundColor(Color.parseColor("#586D94"));
            if (BuildConfig.DEBUG) {
                spanUtils.append(" ?????????" + playerInfoModel.getRole())
                        .append(" ???????????????" + playerInfoModel.isOnline());
            }
            stringBuilder = spanUtils.create();
        }
        commentModel.setStringBuilder(stringBuilder);
        EventBus.getDefault().post(new PretendCommentMsgEvent(commentModel));
    }

    private void pretendSystemMsg(String text) {
        CommentSysModel commentSysModel = new CommentSysModel(mRoomData.getGameType(), text);
        EventBus.getDefault().post(new PretendCommentMsgEvent(commentSysModel));
    }

    private void pretendRoomNameSystemMsg(String roomName, int type) {
        CommentSysModel commentSysModel = new CommentSysModel(roomName, type);
        EventBus.getDefault().post(new PretendCommentMsgEvent(commentSysModel));
    }

    @Override
    public void start() {
        super.start();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * ???ui?????????
     * ??????????????????
     */
    public void onOpeningAnimationOver() {
        // ??????????????????????????????
        if (mRoomData.hasGameBegin()) {
            mRoomData.checkRoundInEachMode();
        } else {
            MyLog.d(TAG, "onOpeningAnimationOver ???????????????");
        }
    }

    /**
     * ????????????
     */
    public void playGuide() {
        if (mDestroyed) {
            return;
        }
        GrabRoundInfoModel now = mRoomData.getRealRoundInfo();
        if (now != null) {
            if (mExoPlayer == null) {
                mExoPlayer = new AndroidMediaPlayer();
                if (mRoomData.isMute() || !U.getActivityUtils().isAppForeground()) {
                    mExoPlayer.setVolume(0);
                } else {
                    mExoPlayer.setVolume(1);
                }
            }
            mExoPlayer.setCallback(new PlayerCallbackAdapter() {
                @Override
                public void onPrepared(long duration) {
                    super.onPrepared(duration);
                    if (!now.isParticipant() && now.getEnterStatus() == EQRoundStatus.QRS_INTRO.getValue()) {
                        MyLog.d(TAG, "??????????????????????????????seek");
                        mExoPlayer.seekTo(now.getElapsedTimeMs());
                    }
                }
            });
            mExoPlayer.startPlay(now.getMusic().getStandIntro());
        }
    }

    /**
     * ??????????????????
     */
    public void stopGuide() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
        }
    }

    /**
     * ???????????????????????????,????????????????????????
     */
    void preOpWhenSelfRound() {
        GrabRoundInfoModel now = mRoomData.getRealRoundInfo();
        boolean needAcc = false;
        if (now != null) {
            if (now.getWantSingType() == EWantSingType.EWST_SPK.getValue()) {
                needAcc = true;
            } else if (now.getWantSingType() == EWantSingType.EWST_CHORUS.getValue()) {
                needAcc = false;
            } else if (mRoomData.isAccEnable()) {
                needAcc = true;
            }
        }
        if (needAcc) {
            // 1. ?????????????????????????????? melp ??????
            if (now != null) {
                String midi = now.getMusic().getMidi();
                if(!TextUtils.isEmpty(midi)){
                    File midiFile = SongResUtils.getMIDIFileByUrl(midi);
                    if (midiFile != null && !midiFile.exists()) {
                        U.getHttpUtils().downloadFileAsync(now.getMusic().getMidi(), midiFile,true, null);
                    }
                }
            }
        }
        if (!mRoomData.isOwner()) {
            ZqEngineKit.getInstance().setClientRole(true);
            ZqEngineKit.getInstance().muteLocalAudioStream(false);
            if (needAcc) {
                // ??????????????????????????????????????????????????????????????????
                mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS);
                mUiHandler.sendEmptyMessageDelayed(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS, 2000);
            }
        } else {
            // ???????????????,???????????? ???????????????????????????????????? ?????????????????????
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ZqEngineKit.getInstance().muteLocalAudioStream(false);
                    onChangeBroadcastSuccess();
                }
            }, 500);
        }

        if (needAcc) {
            if (mRoomData.isOwner()) {
                MyLog.d(TAG, "preOpWhenSelfRound ????????? ?????? onChangeBroadcastSuccess");
                onChangeBroadcastSuccess();
            } else {
                // ??????????????????????????????????????????????????????????????????
                mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS);
                mUiHandler.sendEmptyMessageDelayed(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS, 2000);
            }
        }
        // ??????acr??????
        if (ScoreConfig.isAcrEnable() && now != null && now.getMusic() != null) {
            ZqEngineKit.getInstance().startRecognize(RecognizeConfig.newBuilder()
                    .setSongName(now.getMusic().getItemName())
                    .setArtist(now.getMusic().getOwner())
                    .setMode(RecognizeConfig.MODE_MANUAL)
                    .build());
        }
    }

    /**
     * ??????????????????????????????
     */
    public void beginSing() {
        // ???????????????????????????
        BaseRoundInfoModel now = mRoomData.getRealRoundInfo();
        //??????????????????
        if (SkrConfig.getInstance().isNeedUploadAudioForAI(GameModeType.GAME_MODE_GRAB)) {
            // ????????????????????????????????????
            ZqEngineKit.getInstance().startAudioRecording(RoomDataUtils.getSaveAudioForAiFilePath(), Constants.AUDIO_RECORDING_QUALITY_HIGH);
            if (now != null) {
                if (mRobotScoreHelper == null) {
                    mRobotScoreHelper = new RobotScoreHelper();
                }
                mRobotScoreHelper.reset();
            }
        }
    }

    public void changeSong() {
        GrabRoundInfoModel now = mRoomData.getRealRoundInfo();
        if (now != null) {
            now.setMusic(mRoomData.getGrabGuideInfoModel().getNextSongModel());
        }
        tryStopRobotPlay();
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mIGrabView.changeSong();
            }
        });
    }

    /**
     * ????????????
     */
    public void grabThisRound(int userId, int seq, boolean challenge) {
        MyLog.d(TAG, "grabThisRound" + " seq=" + seq + " challenge=" + challenge + " accenable=" + mRoomData.isAccEnable());
        //????????????
        GrabRoundInfoModel now = mRoomData.getRealRoundInfo();
        if (now != null) {
            WantSingerInfo wantSingerInfo = new WantSingerInfo();
            wantSingerInfo.setWantSingType(EWantSingType.EWST_DEFAULT.getValue());
            wantSingerInfo.setUserID(userId);
            wantSingerInfo.setTimeMs(System.currentTimeMillis());
            now.addGrabUid(true, wantSingerInfo);

            // ?????????????????????????????????????????????????????????
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    now.setHasSing(true);
                    now.setUserID(userId);
                    now.updateStatus(true, EQRoundStatus.QRS_SING.getValue());
                }
            }, 1000);
        }
    }

    /**
     * ??????
     */
    public void lightsOff() {
        //TODO ??????????????????????????????????????????
        GrabRoundInfoModel now = mRoomData.getRealRoundInfo();
        if (now != null) {
            MLightInfoModel noPassingInfo = new MLightInfoModel();
            noPassingInfo.setUserID((int) MyUserInfoManager.getInstance().getUid());
            now.addLightOffUid(true, noPassingInfo);
        }
    }

    /**
     * ??????
     */
    public void lightsBurst() {
        //TODO ??????????????????????????????????????????
        GrabRoundInfoModel now = mRoomData.getRealRoundInfo();
        if (now != null) {
            mRoomData.setCoin(mRoomData.getCoin() - 2);
            BLightInfoModel m = new BLightInfoModel();
            m.setUserID((int) MyUserInfoManager.getInstance().getUid());
            now.addLightBurstUid(true, m);
        }
    }

    private void robotSingBegin() {
        String skrerUrl = null;
        GrabRoundInfoModel grabRoundInfoModel = mRoomData.getRealRoundInfo();
        if (grabRoundInfoModel != null) {
            GrabSkrResourceModel grabSkrResourceModel = grabRoundInfoModel.getSkrResource();
            if (grabSkrResourceModel != null) {
                skrerUrl = grabSkrResourceModel.getAudioURL();
            }
        }
        if (mRobotScoreHelper == null) {
            mRobotScoreHelper = new RobotScoreHelper();
        }

        if (mExoPlayer == null) {
            mExoPlayer = new ExoPlayer();
        }
        mExoPlayer.startPlay(skrerUrl);
        mExoPlayer.setCallback(new PlayerCallbackAdapter() {
            @Override
            public void onPrepared(long duration) {
                if (!grabRoundInfoModel.isParticipant() && grabRoundInfoModel.getEnterStatus() == EQRoundStatus.QRS_SING.getValue()) {
                    MyLog.d(TAG, "?????????????????????????????????????????????????????????seek?????? " + grabRoundInfoModel.getElapsedTimeMs());
                    mExoPlayer.seekTo(grabRoundInfoModel.getElapsedTimeMs());
                }
            }
        });
        if (mRoomData.isMute() || !U.getActivityUtils().isAppForeground()) {
            mExoPlayer.setVolume(0);
        } else {
            mExoPlayer.setVolume(1);
        }

        //???????????????????????????
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onGameOver("robotSingBegin", System.currentTimeMillis());
            }
        }, 23000);
    }

    private void tryStopRobotPlay() {
        if (mExoPlayer != null) {
            mExoPlayer.reset();
        }
    }

    public void muteAllRemoteAudioStreams(boolean mute, boolean fromUser) {
        if (fromUser) {
            mRoomData.setMute(mute);
        }
        ZqEngineKit.getInstance().muteAllRemoteAudioStreams(mute);
        // ????????????????????????
        if (mute) {
            // ???????????????
            if (mExoPlayer != null) {
                mExoPlayer.setVolume(0);
            }
        } else {
            // ??????????????????
            if (mExoPlayer != null) {
                mExoPlayer.setVolume(1f);
            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param roundInfoModel
     */
    private void onSelfRoundOver(GrabRoundInfoModel roundInfoModel) {
        // ?????????????????????????????????????????????
        if (SkrConfig.getInstance().isNeedUploadAudioForAI(GameModeType.GAME_MODE_GRAB)) {
            //???????????????????????????????????????
            // ???????????????????????????????????????
            if (mRoomData.getGameId() > 0) {
                ZqEngineKit.getInstance().stopAudioRecording();
            }
            // ????????????
            if (mRobotScoreHelper != null) {
                if (mRobotScoreHelper.isScoreEnough()) {
                    if (roundInfoModel.getOverReason() == EQRoundOverReason.ROR_LAST_ROUND_OVER.getValue()
                            && roundInfoModel.getResultType() == EQRoundResultType.ROT_TYPE_1.getValue()) {
                        // ???????????????????????????
                        roundInfoModel.setSysScore(mRobotScoreHelper.getAverageScore());
//                        uploadRes1ForAi(roundInfoModel);
                    } else {
                        MyLog.d(TAG, "?????????????????????????????????");
                    }
                } else {
                    MyLog.d(TAG, "isScoreEnough false");
                }
            }
        }
    }

    @Override
    public void destroy() {
        MyLog.d(TAG, "destroy begin");
        super.destroy();
        mDestroyed = true;
        Params.save2Pref(ZqEngineKit.getInstance().getParams());
        if (!mRoomData.isHasExitGame()) {
            exitRoom();
        }
        ChatRoomMsgManager.getInstance().removeFilter(mPushMsgFilter);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        ZqEngineKit.getInstance().destroy("grabroom");
        mUiHandler.removeCallbacksAndMessages(null);
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        } else {
            MyLog.d(TAG, "mExoPlayer == null ");
        }

        if (mZipUrlResourceManager != null) {
            mZipUrlResourceManager.cancelAllTask();
        }
        MyLog.d(TAG, "destroy over");
    }

    /**
     * ????????????????????????????????????
     */
    public void sendMyGrabOver() {
        //TODO ??????????????????????????????????????????

    }

    /**
     * ????????????????????????
     */
    public void sendRoundOverInfo() {
        MyLog.w(TAG, "????????????????????????");
        //TODO ???????????????????????????????????????
        GrabRoundInfoModel now = mRoomData.getRealRoundInfo();
        if (now != null) {
            now.setOverReason(EQRoundOverReason.ROR_LAST_ROUND_OVER.getValue());
        }
        GrabRoundInfoModel bRoundInfo = mRoomData.getGrabGuideInfoModel().createBRoundInfo();
        mRoomData.setExpectRoundInfo(bRoundInfo);
        mRoomData.checkRoundInEachMode();
        mRoomData.setCoin(mRoomData.getCoin() + 1);
    }

    /**
     * ??????????????????
     */
    public void giveUpSing() {
        MyLog.w(TAG, "???????????????");
        GrabRoundInfoModel now = mRoomData.getRealRoundInfo();
        if (now != null) {
            now.setOverReason(EQRoundOverReason.ROR_SELF_GIVE_UP.getValue());
        }
        GrabRoundInfoModel bRoundInfo = mRoomData.getGrabGuideInfoModel().createBRoundInfo();
        mRoomData.setExpectRoundInfo(bRoundInfo);
        mRoomData.checkRoundInEachMode();
    }


    /**
     * ????????????
     */
    public void exitRoom() {
        //TODO ????????????????????????????????????????????????
        mIGrabView.onGetGameResult(true);
    }


    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING, priority = 9)
    public void onEvent(GrabGameOverEvent event) {
        MyLog.d(TAG, "GrabGameOverEvent");
        estimateOverTsThisRound();
        tryStopRobotPlay();
        ZqEngineKit.getInstance().stopRecognize();
        mRoomData.setIsGameFinish(true);
        // ???????????????,???????????????ui??????
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mIGrabView.roundOver(event.lastRoundInfo, false, null);
            }
        });
        // ???????????????????????????
        ZqEngineKit.getInstance().destroy("grabroom");
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIGrabView.gameFinish();
            }
        }, 2000);
    }

    /**
     * ?????????????????????
     */
    @Subscribe(threadMode = ThreadMode.POSTING, priority = 9)
    public void onEvent(GrabRoundChangeEvent event) {
        MyLog.d(TAG, "GrabRoundChangeEvent" + " event=" + event);
        // ??????????????????????????????
        estimateOverTsThisRound();
        mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS);
        closeEngine();
        tryStopRobotPlay();
        ZqEngineKit.getInstance().stopRecognize();
        GrabRoundInfoModel now = event.newRoundInfo;
        if (now != null) {
            EventBus.getDefault().post(new GrabPlaySeatUpdateEvent(now.getPlayUsers()));
            EventBus.getDefault().post(new GrabWaitSeatUpdateEvent(now.getWaitUsers()));
            int size = 0;
            for (GrabPlayerInfoModel playerInfoModel : now.getPlayUsers()) {
                if (playerInfoModel.getUserID() == 2) {
                    continue;
                }
                size++;
            }
            int finalSize = size;
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mIGrabView.showPracticeFlag(finalSize <= 1);
                }
            });
        }

        if (now.getStatus() == EQRoundStatus.QRS_INTRO.getValue()) {
            //??????????????????????????????
            //TODO ?????????????????????
            if (event.lastRoundInfo != null && event.lastRoundInfo.getStatus() >= EQRoundStatus.QRS_SING.getValue()) {
                // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mIGrabView.roundOver(event.lastRoundInfo, true, now);
                    }
                });
                if (event.lastRoundInfo.singBySelf()) {
                    onSelfRoundOver(event.lastRoundInfo);
                }
            } else {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mIGrabView.grabBegin(now.getRoundSeq(), now.getMusic());
                    }
                });
            }
        } else if (now.getStatus() == EQRoundStatus.QRS_SING.getValue()
                || now.getStatus() == EQRoundStatus.QRS_CHO_SING.getValue()
                || now.getStatus() == EQRoundStatus.QRS_SPK_FIRST_PEER_SING.getValue()
                || now.getStatus() == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.getValue()) {
            // ????????????
            if (now.singBySelf()) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mIGrabView.singBySelf();
                    }
                });
                preOpWhenSelfRound();
            } else {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mIGrabView.singByOthers();
                    }
                });
                checkMachineUser(now.getUserID());
            }
        } else if (now.getStatus() == EQRoundStatus.QRS_END.getValue()) {
            MyLog.w(TAG, "GrabRoundChangeEvent ??????????????????????????????????????????????????????roundSeq:" + now.getRoundSeq());
            MyLog.w(TAG, "???????????????????????????");
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GrabWaitSeatUpdateEvent event) {
        MyLog.d(TAG, "onEvent" + " event=" + event);
        if (event.list != null && event.list.size() > 0) {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SomeOneJoinWaitSeatEvent event) {
        MyLog.d(TAG, "onEvent" + " event=" + event);
    }

    /**
     * ?????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(GrabRoundStatusChangeEvent event) {
        MyLog.d(TAG, "GrabRoundStatusChangeEvent" + " event=" + event);
        estimateOverTsThisRound();
        mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS);
        closeEngine();
        GrabRoundInfoModel now = event.roundInfo;
        tryStopRobotPlay();
        if (now.getStatus() == EQRoundStatus.QRS_INTRO.getValue()) {
            //??????????????????????????????
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mIGrabView.grabBegin(now.getRoundSeq(), now.getMusic());
                }
            });
        } else if (now.getStatus() == EQRoundStatus.QRS_SING.getValue()
                || now.getStatus() == EQRoundStatus.QRS_CHO_SING.getValue()
                || now.getStatus() == EQRoundStatus.QRS_SPK_FIRST_PEER_SING.getValue()
                || now.getStatus() == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.getValue()) {
            // ????????????
            if (now.singBySelf()) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mIGrabView.singBySelf();
                    }
                });
                preOpWhenSelfRound();
            } else {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mIGrabView.singByOthers();
                    }
                });
                checkMachineUser(now.getUserID());
            }
//            if (now.getStatus() == EQRoundStatus.QRS_CHO_SING.getValue()) {
//                pretendSystemMsg("??????????????????");
//            }
        }
    }

    private void closeEngine() {
        if (mRoomData.getGameId() > 0) {
            ZqEngineKit.getInstance().stopAudioMixing();
            if (mRoomData.isSpeaking()) {
                MyLog.d(TAG, "closeEngine ?????????????????????????????????");
            } else {
                if (mRoomData.isOwner()) {
                    MyLog.d(TAG, "closeEngine ????????? mute??????");
                    ZqEngineKit.getInstance().muteLocalAudioStream(true);
                } else {
                    if (ZqEngineKit.getInstance().getParams().isAnchor()) {
                        ZqEngineKit.getInstance().setClientRole(false);
                    }
                }
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(EngineEvent event) {
        if (event.getType() == EngineEvent.TYPE_USER_ROLE_CHANGE) {
            EngineEvent.RoleChangeInfo roleChangeInfo = event.getObj();
            if (roleChangeInfo.getNewRole() == 1) {
                GrabRoundInfoModel roundInfoModel = mRoomData.getRealRoundInfo();
                if (roundInfoModel != null && roundInfoModel.singBySelf()) {
                    MyLog.d(TAG, "??????????????????????????????");
                    onChangeBroadcastSuccess();
                } else if (mRoomData.isSpeaking()) {
                    MyLog.d(TAG, "??????????????????????????????");
                }
            }
        } else {
            // ?????????????????????????????????????????? ????????????
        }
    }

    /**
     * ?????????????????????
     */
    private void onChangeBroadcastSuccess() {
        MyLog.d(TAG, "onChangeBroadcastSuccess ??????????????????");
        mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS);
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                GrabRoundInfoModel infoModel = mRoomData.getRealRoundInfo();
                if (infoModel == null || !infoModel.singBySelf()) {
                    MyLog.d(TAG, "onChangeBroadcastSuccess,?????????????????????????????????cancel");
                    return;
                }
                SongModel songModel = infoModel.getMusic();
                if (songModel == null) {
                    return;
                }
                if (infoModel.getStatus() == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.getValue()) {
                    songModel = songModel.getPkMusic();
                }
                if (songModel == null) {
                    return;
                }
                // ??????????????????????????????????????????mute
                File accFile = SongResUtils.getAccFileByUrl(songModel.getAcc());
                // midi????????????????????????????????????native?????????????????????????????????
                File midiFile = SongResUtils.getMIDIFileByUrl(songModel.getMidi());
                if (mRoomData.isAccEnable() && infoModel.isAccRound() || infoModel.isPKRound()) {
                    int songBeginTs = songModel.getBeginMs();
                    if (accFile != null && accFile.exists()) {
                        // ??????????????????
                        ZqEngineKit.getInstance().startAudioMixing((int) MyUserInfoManager.getInstance().getUid(), accFile.getAbsolutePath()
                                , midiFile.getAbsolutePath(), songBeginTs, false, false, 1);
                    } else {
                        ZqEngineKit.getInstance().startAudioMixing((int) MyUserInfoManager.getInstance().getUid(), songModel.getAcc()
                                , midiFile.getAbsolutePath(), songBeginTs, false, false, 1);
                    }
                }
            }
        });
    }

    /**
     * ????????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QWantSingChanceMsgEvent event) {
        if (RoomDataUtils.isCurrentExpectingRound(event.getRoundSeq(), mRoomData)) {
            MyLog.w(TAG, "???????????????userID " + event.getUserID() + ", seq " + event.getRoundSeq());
            GrabRoundInfoModel roundInfoModel = mRoomData.getExpectRoundInfo();

            WantSingerInfo wantSingerInfo = new WantSingerInfo();
            wantSingerInfo.setUserID(event.getUserID());
            wantSingerInfo.setTimeMs(System.currentTimeMillis());
            wantSingerInfo.setWantSingType(event.getWantSingType());

            if (roundInfoModel.getStatus() == EQRoundStatus.QRS_INTRO.getValue()) {
                roundInfoModel.addGrabUid(true, wantSingerInfo);
            } else {
                MyLog.d(TAG, "????????????????????????????????????");
                roundInfoModel.addGrabUid(false, wantSingerInfo);
            }
        } else {
            MyLog.w(TAG, "????????????,???????????????????????????userID " + event.getUserID() + ", seq " + event.getRoundSeq() + "?????????????????? " + mRoomData.getExpectRoundInfo());
        }
    }

    /**
     * ????????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QGetSingChanceMsgEvent event) {
        if (RoomDataUtils.isCurrentExpectingRound(event.getRoundSeq(), mRoomData)) {
            MyLog.w(TAG, "??????????????????userID " + event.getUserID() + ", roundInfo" + event.currentRound);
            GrabRoundInfoModel roundInfoModel = mRoomData.getExpectRoundInfo();
            roundInfoModel.setHasSing(true);
            roundInfoModel.setUserID(event.getUserID());
            roundInfoModel.tryUpdateRoundInfoModel(event.getCurrentRound(), true);
            // ??????????????????????????????????????? updateStatus???
            //roundInfoModel.updateStatus(true, EQRoundStatus.QRS_SING.getValue());
        } else {
            MyLog.w(TAG, "???????????????,???????????????????????????userID " + event.getUserID() + ", seq " + event.getRoundSeq() + "?????????????????? " + mRoomData.getExpectRoundInfo());
        }
    }

    /**
     * ????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QLightOffMsgEvent event) {
        if (RoomDataUtils.isCurrentExpectingRound(event.roundSeq, mRoomData)) {
            MyLog.w(TAG, "??????????????????userID " + event.userID + ", seq " + event.roundSeq);
            GrabRoundInfoModel roundInfoModel = mRoomData.getExpectRoundInfo();
            //??????????????????????????????????????????
            //roundInfoModel.updateStatus(true, EQRoundStatus.QRS_SING.getValue());
            MLightInfoModel noPassingInfo = new MLightInfoModel();
            noPassingInfo.setUserID(event.userID);
            roundInfoModel.addLightOffUid(true, noPassingInfo);
        } else {
            MyLog.w(TAG, "???????????????,???????????????????????????userID " + event.userID + ", seq " + event.roundSeq + "?????????????????? " + mRoomData.getExpectRoundInfo());
        }
    }

    /**
     * ????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QLightBurstMsgEvent event) {
        if (RoomDataUtils.isCurrentExpectingRound(event.roundSeq, mRoomData)) {
            MyLog.w(TAG, "??????????????????userID " + event.userID + ", seq " + event.roundSeq);
            GrabRoundInfoModel roundInfoModel = mRoomData.getExpectRoundInfo();
            //??????????????????????????????????????????
//            roundInfoModel.updateStatus(true, EQRoundStatus.QRS_SING.getValue());
            BLightInfoModel noPassingInfo = new BLightInfoModel();
            noPassingInfo.setUserID(event.userID);
            roundInfoModel.addLightBurstUid(true, noPassingInfo);
        } else {
            MyLog.w(TAG, "???????????????,???????????????????????????userID " + event.userID + ", seq " + event.roundSeq + "?????????????????? " + mRoomData.getExpectRoundInfo());
        }
    }

    /**
     * ????????????????????????????????????sych????????????????????????????????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GrabSomeOneLightOffEvent event) {
        MyLog.d(TAG, "onEvent" + " event=" + event);
        if (event.roundInfo.getStatus() == EQRoundStatus.QRS_SPK_FIRST_PEER_SING.getValue()) {
            if (event.roundInfo.getsPkRoundInfoModels().size() > 0) {
                pretendLightMsgComment(event.roundInfo.getsPkRoundInfoModels().get(0).getUserID(), event.uid, false);
            }
        } else if (event.roundInfo.getStatus() == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.getValue()) {
            if (event.roundInfo.getsPkRoundInfoModels().size() > 1) {
                pretendLightMsgComment(event.roundInfo.getsPkRoundInfoModels().get(1).getUserID(), event.uid, false);
            }
        } else {
            pretendLightMsgComment(event.roundInfo.getUserID(), event.uid, false);
        }
    }

    /**
     * ??????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GrabSomeOneLightBurstEvent event) {
        if (event.roundInfo.getStatus() == EQRoundStatus.QRS_SPK_FIRST_PEER_SING.getValue()) {
            if (event.roundInfo.getsPkRoundInfoModels().size() > 0) {
                pretendLightMsgComment(event.roundInfo.getsPkRoundInfoModels().get(0).getUserID(), event.uid, true);
            }
        } else if (event.roundInfo.getStatus() == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.getValue()) {
            if (event.roundInfo.getsPkRoundInfoModels().size() > 1) {
                pretendLightMsgComment(event.roundInfo.getsPkRoundInfoModels().get(1).getUserID(), event.uid, true);
            }
        } else {
            pretendLightMsgComment(event.roundInfo.getUserID(), event.uid, true);
        }
    }

    /**
     * ?????????????????????
     *
     * @param singerId ??????????????????
     * @param uid      ???????????????
     */
    private void pretendLightMsgComment(int singerId, int uid, boolean isBao) {
        PlayerInfoModel singerModel = RoomDataUtils.getPlayerInfoById(mRoomData, singerId);
        PlayerInfoModel playerInfoModel = RoomDataUtils.getPlayerInfoById(mRoomData, uid);
        MyLog.d(TAG, "pretendLightMsgComment" + " singerId=" + singerModel + " uid=" + playerInfoModel + " isBao=" + isBao);
        if (singerModel != null && playerInfoModel != null) {
            boolean isChorus = false;
            GrabRoundInfoModel now = mRoomData.getRealRoundInfo();
            if (now != null) {
                isChorus = now.isChorusRound();
            }
            CommentLightModel commentLightModel = new CommentLightModel(mRoomData.getGameType(), playerInfoModel, singerModel, isBao, isChorus);
            EventBus.getDefault().post(new PretendCommentMsgEvent(commentLightModel));
        }
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QJoinNoticeEvent event) {
        boolean canAdd = false;
        GrabPlayerInfoModel playerInfoModel = event.infoModel;
        MyLog.d(TAG, "??????????????????,id=" + playerInfoModel.getUserID() + " name=" + playerInfoModel.getUserInfo().getNicknameRemark() + " role=" + playerInfoModel.getRole() + " roundSeq=" + event.roundSeq);
        if (playerInfoModel != null && playerInfoModel.getUserID() == MyUserInfoManager.getInstance().getUid()) {
            /**
             * ???????????????????????????
             * ??????????????????bug???
             * ???????????????A?????????????????????????????????????????????waitlist?????????A???????????????????????? A ??????????????????????????????push????????????????????????
             */
            canAdd = false;
        } else if (RoomDataUtils.isCurrentExpectingRound(event.roundSeq, mRoomData)) {
            GrabRoundInfoModel grabRoundInfoModel = mRoomData.getExpectRoundInfo();
            if (grabRoundInfoModel != null && grabRoundInfoModel.addUser(true, playerInfoModel)) {
                canAdd = true;
            }
        } else if (!mRoomData.hasGameBegin()) {
            canAdd = true;
        } else {
            MyLog.w(TAG, "?????????????????????,???????????????????????????userID " + event.infoModel.getUserID() + ", seq " + event.roundSeq + "?????????????????? " + mRoomData.getExpectRoundInfo());
        }
        //TODO ?????????????????????????????????????????????????????? SomeOne ??????????????????????????????????????????
        if (canAdd) {
            CommentTextModel commentModel = new CommentTextModel();
            commentModel.setUserId(playerInfoModel.getUserInfo().getUserId());
            commentModel.setAvatar(playerInfoModel.getUserInfo().getAvatar());
            commentModel.setUserName(playerInfoModel.getUserInfo().getNicknameRemark());
            commentModel.setAvatarColor(Color.WHITE);
            SpannableStringBuilder stringBuilder;
            if (playerInfoModel.getUserInfo().getUserId() == UserAccountManager.SYSTEM_GRAB_ID) {
                stringBuilder = new SpanUtils()
                        .append(playerInfoModel.getUserInfo().getNicknameRemark() + " ").setForegroundColor(Color.parseColor("#DF7900"))
                        .append("?????????????????????????????????????????????????????????????????????~").setForegroundColor(Color.parseColor("#586D94"))
                        .create();
            } else {
                SpanUtils spanUtils = new SpanUtils()
                        .append(playerInfoModel.getUserInfo().getNicknameRemark() + " ").setForegroundColor(Color.parseColor("#DF7900"))
                        .append("???????????????").setForegroundColor(Color.parseColor("#586D94"));
                if (BuildConfig.DEBUG) {
                    spanUtils.append(" ?????????" + playerInfoModel.getRole())
                            .append(" ???????????????" + playerInfoModel.isOnline());
                }
                stringBuilder = spanUtils.create();
            }
            commentModel.setStringBuilder(stringBuilder);
            EventBus.getDefault().post(new PretendCommentMsgEvent(commentModel));
        }
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QExitGameMsgEvent event) {
        if (RoomDataUtils.isCurrentExpectingRound(event.roundSeq, mRoomData)) {
            MyLog.d(TAG, "??????????????????,id=" + event.userID);
            GrabRoundInfoModel grabRoundInfoModel = mRoomData.getExpectRoundInfo();
            grabRoundInfoModel.removeUser(true, event.userID);

        } else {
            MyLog.w(TAG, "?????????????????????,???????????????????????????userID " + event.userID + ", seq " + event.roundSeq + "?????????????????? " + mRoomData.getExpectRoundInfo());
        }
    }

    /**
     * ?????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SomeOneLeavePlaySeatEvent event) {
        GrabRoundInfoModel grabRoundInfoModel = mRoomData.getRealRoundInfo();
        if (grabRoundInfoModel != null) {
            for (ChorusRoundInfoModel chorusRoundInfoModel : grabRoundInfoModel.getChorusRoundInfoModels()) {
                if (event.mPlayerInfoModel != null) {
                    if (chorusRoundInfoModel.getUserID() == event.mPlayerInfoModel.getUserID()) {
                        chorusRoundInfoModel.userExit();
                        pretendGiveUp(mRoomData.getUserInfo(event.mPlayerInfoModel.getUserID()));
                    }
                }
            }
        }
    }

    /**
     * ????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QCoinChangeEvent event) {
        if (event.userID == MyUserInfoManager.getInstance().getUid()) {
            if (event.remainCoin > 0) {
                mRoomData.setCoin(event.remainCoin);
            }
            if (event.reason.getValue() == 1) {
                pretendSystemMsg("????????????" + event.changeCoin + "????????????");
            }
        }
    }

    /**
     * ???????????????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QChoGiveUpEvent event) {
        MyLog.d(TAG, "QChoGiveUpEvent" + " event=" + event);
        GrabRoundInfoModel now = mRoomData.getRealRoundInfo();
        if (now != null) {
            if (now.getRoundSeq() == event.roundSeq) {
                now.giveUpInChorus(event.userID);
                List<ChorusRoundInfoModel> list = now.getChorusRoundInfoModels();
                if (list != null) {
                    for (ChorusRoundInfoModel chorusRoundInfoModel : list) {
                        if (chorusRoundInfoModel.getUserID() == event.userID) {
                            UserInfoModel userInfoModel = mRoomData.getUserInfo(event.userID);
                            if (event.userID == MyUserInfoManager.getInstance().getUid()) {
                                // ?????????????????????
                                U.getToastUtil().showShort("?????????????????????");
                            } else if (now.singBySelf()) {
                                // ????????????????????????
                                if (userInfoModel != null) {
                                    U.getToastUtil().showShort(userInfoModel.getNicknameRemark() + "??????????????????");
                                }
                            } else {
                                // ??????????????????????????????
                                if (userInfoModel != null) {
                                    U.getToastUtil().showShort(userInfoModel.getNicknameRemark() + "??????????????????");
                                }
                            }
                            pretendGiveUp(userInfoModel);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * ????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QPkInnerRoundOverEvent event) {
        MyLog.d(TAG, "QPkInnerRoundOverEvent" + " event=" + event);
        if (RoomDataUtils.isCurrentRunningRound(event.mRoundInfoModel.getRoundSeq(), mRoomData)) {
            GrabRoundInfoModel now = mRoomData.getRealRoundInfo();
            if (now != null) {
                now.tryUpdateRoundInfoModel(event.mRoundInfoModel, true);
//                // PK ????????????????????? ????????????
                if (now.getsPkRoundInfoModels().size() > 0) {
                    if (now.getsPkRoundInfoModels().get(0).getOverReason() == EQRoundOverReason.ROR_SELF_GIVE_UP.getValue()) {
                        UserInfoModel userInfoModel = mRoomData.getUserInfo(now.getsPkRoundInfoModels().get(0).getUserID());
                        pretendGiveUp(userInfoModel);
                    }
                }
                if (now.getsPkRoundInfoModels().size() > 1) {
                    if (now.getsPkRoundInfoModels().get(1).getOverReason() == EQRoundOverReason.ROR_SELF_GIVE_UP.getValue()) {
                        UserInfoModel userInfoModel = mRoomData.getUserInfo(now.getsPkRoundInfoModels().get(1).getUserID());
                        pretendGiveUp(userInfoModel);
                    }
                }
            }
        }
    }

    private void pretendGiveUp(UserInfoModel userInfoModel) {
        if (userInfoModel != null) {
            CommentTextModel commentModel = new CommentTextModel();
            commentModel.setUserId(userInfoModel.getUserId());
            commentModel.setAvatar(userInfoModel.getAvatar());
            commentModel.setUserName(userInfoModel.getNicknameRemark());
            commentModel.setAvatarColor(Color.WHITE);
            SpannableStringBuilder stringBuilder;
            SpanUtils spanUtils = new SpanUtils()
                    .append(userInfoModel.getNicknameRemark() + " ").setForegroundColor(Color.parseColor("#DF7900"))
                    .append("?????????").setForegroundColor(Color.parseColor("#586D94"));
            stringBuilder = spanUtils.create();
            commentModel.setStringBuilder(stringBuilder);
            EventBus.getDefault().post(new PretendCommentMsgEvent(commentModel));
        }
    }

    /**
     * ????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QRoundOverMsgEvent event) {
        MyLog.w(TAG, "?????????????????????????????????????????????push event:" + event);
//        if (mRoomData.getLastSyncTs() >= event.getInfo().getTimeMs()) {
//            MyLog.w(TAG, "?????????????????????");
//            return;
//        }
        if (RoomDataUtils.isCurrentRunningRound(event.getCurrentRound().getRoundSeq(), mRoomData)) {
            // ?????????????????????
            mRoomData.getRealRoundInfo().tryUpdateRoundInfoModel(event.currentRound, true);
            if (event.myCoin >= 0) {
                mRoomData.setCoin(event.myCoin);
            }
            if (event.totalRoundNum > 0) {
                mRoomData.getGrabConfigModel().setTotalGameRoundSeq(event.totalRoundNum);
            }
        }
        if (!mRoomData.hasGameBegin()) {
            MyLog.w(TAG, "?????? QRoundOverMsgEvent???????????????????????????????????????????????????");
            mRoomData.setHasGameBegin(true);
            mRoomData.setExpectRoundInfo(event.nextRound);
            mRoomData.checkRoundInEachMode();
        } else if (RoomDataUtils.roundSeqLarger(event.nextRound, mRoomData.getExpectRoundInfo())) {
            // ??????????????????
            // ??????????????????????????????????????????
            MyLog.w(TAG, "??????????????????????????????????????????");
            mRoomData.setExpectRoundInfo(event.nextRound);
            mRoomData.checkRoundInEachMode();
        } else {
            MyLog.w(TAG, "???????????????????????????,???????????? ????????????:" + mRoomData.getExpectRoundInfo().getRoundSeq()
                    + " push??????:" + event.currentRound.getRoundSeq());
        }
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QRoundAndGameOverMsgEvent event) {
        if (RoomDataUtils.isCurrentRunningRound(event.roundInfoModel.getRoundSeq(), mRoomData)) {
            // ?????????????????????
            mRoomData.getRealRoundInfo().tryUpdateRoundInfoModel(event.roundInfoModel, true);
            if (event.myCoin >= 0) {
                mRoomData.setCoin(event.myCoin);
            }
        }
        onGameOver("QRoundAndGameOverMsgEvent", event.roundOverTimeMs);
        if (event.mOverReason == EQGameOverReason.GOR_OWNER_EXIT) {
            MyLog.w(TAG, "????????????????????????????????????");
            U.getToastUtil().showLong("????????????????????????????????????");
        }
    }


    private void onGameOver(String from, long gameOverTs) {
        MyLog.w(TAG, "???????????? gameOverTs=" + gameOverTs + " from:" + from);
        if (gameOverTs > mRoomData.getGameStartTs() && gameOverTs > mRoomData.getGameOverTs()) {
            mRoomData.setGameOverTs(gameOverTs);
            mRoomData.setExpectRoundInfo(null);
            mRoomData.checkRoundInEachMode();
        } else {
            MyLog.w(TAG, "???????????? gameOverTs ??????????????????");
        }
    }

    public void checkMachineUser(long uid) {
        PlayerInfoModel playerInfo = RoomDataUtils.getPlayerInfoById(mRoomData, uid);
        if (playerInfo == null) {
            MyLog.w(TAG, "?????????????????????PlayerInfo??????");
            return;
        }
        /**
         * ?????????
         */
        if (playerInfo.isSkrer()) {
            MyLog.d(TAG, "checkMachineUser" + " uid=" + uid + " is machine");
            //????????????????????????
            //??????????????????????????????????????????
            mUiHandler.removeMessages(MSG_ROBOT_SING_BEGIN);
            Message message = mUiHandler.obtainMessage(MSG_ROBOT_SING_BEGIN);
            mUiHandler.sendMessage(message);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActivityUtils.ForeOrBackgroundChange event) {
        MyLog.w(TAG, event.foreground ? "???????????????" : "???????????????");
        if (event.foreground) {
            muteAllRemoteAudioStreams(mRoomData.isMute(), false);
        } else {
            muteAllRemoteAudioStreams(true, false);
        }
    }

    private int estimateOverTsThisRound() {
        int pt = RoomDataUtils.estimateTs2End(mRoomData, mRoomData.getRealRoundInfo());
        MyLog.w(TAG, "?????????????????????????????????" + pt + "ms");
        return pt;
    }


    /*????????????*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MachineScoreEvent event) {
        //???????????????????????????????????????????????????????????????????????????????????????????????????
        GrabRoundInfoModel infoModel = mRoomData.getRealRoundInfo();
        if (infoModel != null && infoModel.singByUserId(event.userId)) {
            mIGrabView.updateScrollBarProgress(event.score, event.lineNum);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LyricAndAccMatchManager.ScoreResultEvent event) {
        int line = event.line;
        int acrScore = event.acrScore;
        int melpScore = event.melpScore;
        String from = event.from;
        int score = melpScore;
        if (melpScore < 68 && acrScore > melpScore) {
            if (melpScore > 0) {
                score = 0.68 * acrScore + 0.32 * melpScore;
            } else {
                score = acrScore;
            }
        }
        processScore(score, line);
    }

    void processScore(int score, int line) {
        if (score < 0) {
            return;
        }
        MyLog.d(TAG, "onEvent" + " ??????=" + score);
        MachineScoreItem machineScoreItem = new MachineScoreItem();
        machineScoreItem.setScore(score);
        // ???????????????????????????
//        long ts = ZqEngineKit.getInstance().getAudioMixingCurrentPosition();
        long ts = -1;
        machineScoreItem.setTs(ts);
        machineScoreItem.setNo(line);
        // ??????????????????????????????
        sendScoreToOthers(machineScoreItem);
        if (mRobotScoreHelper != null) {
            mRobotScoreHelper.add(machineScoreItem);
        }
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mIGrabView.updateScrollBarProgress(score, mRoomData.getSongLineNum());
            }
        });
    }

    /**
     * ?????????????????????????????????
     *
     * @param machineScoreItem
     */
    private void sendScoreToOthers(MachineScoreItem machineScoreItem) {
        // ?????????????????????????????????????????????????????????????????????
        IMsgService msgService = ModuleServiceManager.getInstance().getMsgService();
        if (msgService != null) {
            long ts = System.currentTimeMillis();
            UserInfo senderInfo = new UserInfo.Builder()
                    .setUserID((int) MyUserInfoManager.getInstance().getUid())
                    .setNickName(MyUserInfoManager.getInstance().getNickName())
                    .setAvatar(MyUserInfoManager.getInstance().getAvatar())
                    .setSex(ESex.fromValue(MyUserInfoManager.getInstance().getSex()))
                    .setDescription("")
                    .setIsSystem(false)
                    .build();

            GrabRoundInfoModel now = mRoomData.getRealRoundInfo();
            if (now != null && now.getMusic() != null) {
                RoomMsg roomMsg = new RoomMsg.Builder()
                        .setTimeMs(ts)
                        .setMsgType(ERoomMsgType.RM_ROUND_MACHINE_SCORE)
                        .setRoomID(mRoomData.getGameId())
                        .setNo(ts)
                        .setPosType(EMsgPosType.EPT_UNKNOWN)
                        .setSender(senderInfo)
                        .setMachineScore(new MachineScore.Builder()
                                .setUserID((int) MyUserInfoManager.getInstance().getUid())
                                .setNo(machineScoreItem.getNo())
                                .setScore(machineScoreItem.getScore())
                                .setItemID(now.getMusic().getItemID())
                                .setLineNum(mRoomData.getSongLineNum())
                                .build()
                        )
                        .build();
                String contnet = U.getBase64Utils().encode(roomMsg.toByteArray());
                msgService.sendChatRoomMessage(String.valueOf(mRoomData.getGameId()), CustomMsgType.MSG_TYPE_ROOM, contnet, null);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GiftPresentEvent giftPresentEvent) {
        MyLog.d(TAG, "onEvent" + " giftPresentEvent=" + giftPresentEvent);
        if (giftPresentEvent.info.getRoomID() == mRoomData.getGameId()) {
            EventBus.getDefault().post(new GiftBrushMsgEvent(giftPresentEvent.mGPrensentGiftMsgModel));
        }
    }


}
