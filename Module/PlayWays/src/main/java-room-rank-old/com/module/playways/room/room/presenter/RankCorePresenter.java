package com.module.playways.room.room.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;

import com.alibaba.fastjson.JSON;
import com.common.core.account.UserAccountManager;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.userinfo.model.UserInfoModel;
import com.common.engine.ScoreConfig;
import com.common.log.MyLog;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.player.ExoPlayer;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.upload.UploadCallback;
import com.common.upload.UploadParams;
import com.common.utils.ActivityUtils;
import com.common.utils.SpanUtils;
import com.common.utils.U;
import com.component.busilib.SkrConfig;
import com.component.busilib.constans.GameModeType;
import com.engine.EngineEvent;
import com.engine.Params;
import com.engine.arccloud.RecognizeConfig;
import com.module.ModuleServiceManager;
import com.module.common.ICallback;
import com.module.msg.CustomMsgType;
import com.module.msg.IMsgService;
import com.module.playways.RoomDataUtils;
import com.component.lyrics.LyricAndAccMatchManager;
import com.module.playways.room.msg.event.AccBeginEvent;
import com.module.playways.room.msg.event.AppSwapEvent;
import com.module.playways.room.msg.event.ExitGameEvent;
import com.module.playways.room.msg.event.MachineScoreEvent;
import com.module.playways.room.msg.event.PkBurstLightMsgEvent;
import com.module.playways.room.msg.event.PkLightOffMsgEvent;
import com.module.playways.room.msg.event.RoundAndGameOverEvent;
import com.module.playways.room.msg.event.RoundOverEvent;
import com.module.playways.room.msg.event.SyncStatusEvent;
import com.module.playways.room.msg.filter.PushMsgFilter;
import com.module.playways.room.msg.manager.ChatRoomMsgManager;
import com.module.playways.room.prepare.model.BaseRoundInfoModel;
import com.module.playways.room.prepare.model.OnlineInfoModel;
import com.module.playways.room.prepare.model.PlayerInfoModel;
import com.module.playways.room.room.RankRoomData;
import com.module.playways.room.room.RankRoomServerApi;
import com.module.playways.room.room.SwapStatusType;
import com.module.playways.room.room.comment.model.CommentAiModel;
import com.module.playways.room.room.comment.model.CommentModel;
import com.module.playways.room.room.comment.model.CommentSysModel;
import com.module.playways.room.room.comment.model.CommentTextModel;
import com.module.playways.room.room.event.PkSomeOneOnlineChangeEvent;
import com.module.playways.room.room.event.PretendCommentMsgEvent;
import com.module.playways.room.room.event.RankRoundInfoChangeEvent;
import com.module.playways.room.room.model.BLightInfoModel;
import com.module.playways.room.room.model.MLightInfoModel;
import com.module.playways.room.room.model.RankPlayerInfoModel;
import com.module.playways.room.room.model.RankRoundInfoModel;
import com.module.playways.room.room.model.RecordData;
import com.module.playways.room.room.score.MachineScoreItem;
import com.module.playways.room.room.score.RobotScoreHelper;
import com.module.playways.room.room.view.IGameRuleView;
import com.zq.live.proto.Common.ESex;
import com.zq.live.proto.Common.UserInfo;
import com.zq.live.proto.GrabRoom.EMsgPosType;
import com.zq.live.proto.GrabRoom.ERoomMsgType;
import com.zq.live.proto.GrabRoom.MachineScore;
import com.zq.live.proto.GrabRoom.RoomMsg;
import com.component.lyrics.event.LrcEvent;
import com.component.lyrics.utils.SongResUtils;
import com.zq.mediaengine.kit.ZqEngineKit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.module.playways.room.msg.event.ExitGameEvent.EXIT_GAME_AFTER_PLAY;
import static com.module.playways.room.msg.event.ExitGameEvent.EXIT_GAME_OUT_ROUND;

public class RankCorePresenter extends RxLifeCyclePresenter {
    String TAG = "RankCorePresenter";

    static final int MSG_ENSURE_IN_RC_ROOM = 9;// ??????????????????????????????????????????????????????

    static final int MSG_ROBOT_SING_BEGIN = 10;

    static final int MSG_ENSURE_SWITCH_BROADCAST_SUCCESS = 21; // ??????????????????????????????????????????????????????????????????

    static final int MSG_START_LAST_TWO_SECONDS_TASK = 30;

    private static long sHeartBeatTaskInterval = 3000;
    private static long sSyncStateTaskInterval = 12000;

    RankRoomData mRoomData;

    RankRoomServerApi mRoomServerApi = ApiManager.getInstance().createService(RankRoomServerApi.class);

    Disposable mHeartBeatTask;

    Disposable mSyncGameStateTask;

    IGameRuleView mIGameRuleView;

    RobotScoreHelper mRobotScoreHelper;

    ExoPlayer mExoPlayer;

    PushMsgFilter mPushMsgFilter = new PushMsgFilter<RoomMsg>() {
        @Override
        public boolean doFilter(RoomMsg msg) {
            if (msg.getRoomID() == mRoomData.getGameId()) {
                return true;
            }
            return false;
        }
    };

    Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ENSURE_IN_RC_ROOM:
                    MyLog.d(TAG, "handleMessage ??????????????????push?????????????????????????????????");
                    ModuleServiceManager.getInstance().getMsgService().leaveChatRoom(mRoomData.getGameId() + "");
                    joinRcRoom(0);
                    break;
                case MSG_ROBOT_SING_BEGIN:
                    robotSingBegin((RankPlayerInfoModel) msg.obj);
                    break;
                case MSG_ENSURE_SWITCH_BROADCAST_SUCCESS:
                    onChangeBroadcastSuccess();
                    break;
//                case MSG_ROBOT_SING_END:
//                    break;
                case MSG_START_LAST_TWO_SECONDS_TASK:
                    BaseRoundInfoModel roundInfoModel = (BaseRoundInfoModel) msg.obj;
                    if (roundInfoModel != null && roundInfoModel == mRoomData.getRealRoundInfo()) {
                        mIGameRuleView.hideMainStage();
                    }
                    break;
            }

        }
    };

    public RankCorePresenter(@NotNull IGameRuleView iGameRuleView, @NotNull RankRoomData roomData) {
        mIGameRuleView = iGameRuleView;
        mRoomData = roomData;
        TAG += hashCode();

        MyLog.w(TAG, "player info is " + mRoomData.toString());

        if (mRoomData.getGameId() > 0) {
            Params params = Params.getFromPref();
            params.setScene(Params.Scene.rank);
            ZqEngineKit.getInstance().init("rankingroom", params);
            boolean isAnchor = false;
//            if(RoomDataUtils.isMyRound(mRoomData.getRealRoundInfo())){
//                isAnchor = true;
//            }
            ZqEngineKit.getInstance().joinRoom(String.valueOf(mRoomData.getGameId()), (int) UserAccountManager.getInstance().getUuidAsLong(), isAnchor, mRoomData.getAgoraToken());
            // ?????????????????????
//            ZqEngineKit.getInstance().muteLocalAudioStream(true);
            // ????????????
            pretenSystemMsg();
            // ??????AI??????
            pretenAiJudgeMsg();
            // ??????????????????
            for (int i = 0; i < mRoomData.getRoundInfoModelList().size(); i++) {
                RankRoundInfoModel roundInfoModel = mRoomData.getRoundInfoModelList().get(i);
                PlayerInfoModel playerInfoModel = RoomDataUtils.getPlayerInfoById(mRoomData, roundInfoModel.getUserID());
                pretenEnterRoom(playerInfoModel, i);
            }
//            IMsgService msgService = ModuleServiceManager.getInstance().getMsgService();
//            if (msgService != null) {
//                msgService.syncHistoryFromChatRoom(String.valueOf(mRoomData.getGameId()), 10, true, null);
//            }
            ChatRoomMsgManager.getInstance().addFilter(mPushMsgFilter);

            if (ScoreConfig.isAcrEnable()) {
                ZqEngineKit.getInstance().startRecognize(RecognizeConfig.newBuilder()
                        .setMode(RecognizeConfig.MODE_MANUAL)
                        .setSongName(mRoomData.getSongModel().getItemName())
                        .setArtist(mRoomData.getSongModel().getOwner())
                        .build()
                );
            }
        }
    }

    private void joinRcRoom(int deep) {
        if (deep > 4) {
            MyLog.d(TAG, "???????????????????????????5????????????????????????");
            return;
        }
        if (mRoomData.getGameId() > 0) {
            ModuleServiceManager.getInstance().getMsgService().joinChatRoom(String.valueOf(mRoomData.getGameId()), 10, new ICallback() {
                @Override
                public void onSucess(Object obj) {
                    MyLog.d(TAG, "????????????????????????");
                }

                @Override
                public void onFailed(Object obj, int errcode, String message) {
                    MyLog.d(TAG, "????????????????????????");
                    joinRcRoom(deep + 1);
                }
            });
        }
    }

    private void ensureInRcRoom() {
        mUiHandler.removeMessages(MSG_ENSURE_IN_RC_ROOM);
        mUiHandler.sendEmptyMessageDelayed(MSG_ENSURE_IN_RC_ROOM, 30 * 1000);
    }

    private void pretenSystemMsg() {
        CommentSysModel commentSysModel = new CommentSysModel(mRoomData.getGameType(), "????????????????????????????????????????????????????????????????????????????????????????????????");
        EventBus.getDefault().post(new PretendCommentMsgEvent(commentSysModel));
    }

    private void pretenAiJudgeMsg() {
        PlayerInfoModel ai = mRoomData.getAiJudgeInfo();
        if (ai != null) {
            CommentAiModel commentAiModel = new CommentAiModel(ai, "?????????,?????????????????????????????????????????????");
            EventBus.getDefault().post(new PretendCommentMsgEvent(commentAiModel));
        }
    }

    private void pretenEnterRoom(PlayerInfoModel playerInfoModel, int index) {
        CommentTextModel commentTextModel = new CommentTextModel();
        commentTextModel.setUserInfo(playerInfoModel.getUserInfo());
        commentTextModel.setAvatarColor(CommentModel.AVATAR_COLOR);
        SpannableStringBuilder ssb = new SpanUtils()
                .append(playerInfoModel.getUserInfo().getNicknameRemark() + " ").setForegroundColor(CommentModel.RANK_NAME_COLOR)
                .append(String.format("???%s??????", index + 1)).setForegroundColor(CommentModel.RANK_TEXT_COLOR)
                .create();
        commentTextModel.setStringBuilder(ssb);
        EventBus.getDefault().post(new PretendCommentMsgEvent(commentTextModel));
    }

    @Override
    public void start() {
        super.start();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mRoomData.checkRoundInEachMode();
        startSyncGameStateTask(sSyncStateTaskInterval);
        ensureInRcRoom();
    }

    @Override
    public void destroy() {
        super.destroy();
        cancelHeartBeatTask("destroy");
        cancelSyncGameStateTask();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mUiHandler.removeCallbacksAndMessages(null);
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
        ChatRoomMsgManager.getInstance().removeFilter(mPushMsgFilter);
        if (!mRoomData.hasGoVoiceRoom()) {
            exitGame();
            ModuleServiceManager.getInstance().getMsgService().leaveChatRoom(String.valueOf(mRoomData.getGameId()));
            ZqEngineKit.getInstance().destroy("rankingroom");
        } else {
            MyLog.w(TAG, "???????????????????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????
     */
    public void sendRoundOverInfo(String reason) {
        MyLog.w(TAG, "???????????????????????? reason:" + reason);
        estimateOverTsThisRound();

        // TODO: 2018/12/27 ??????????????????????????????90???
        long timeMs = System.currentTimeMillis();
        int sysScore = 0;
        if (mRobotScoreHelper != null) {
            sysScore = mRobotScoreHelper.getAverageScore();
        }
        String sign = U.getMD5Utils().MD5_32("skrer|" +
                String.valueOf(mRoomData.getGameId()) + "|" +
                String.valueOf(sysScore) + "|" +
                String.valueOf(timeMs));

        RankRoundInfoModel infoModel = RoomDataUtils.getRoundInfoByUserId(mRoomData, (int) MyUserInfoManager.getInstance().getUid());
        if (infoModel == null) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("gameID", mRoomData.getGameId());
        map.put("sysScore", sysScore);
        map.put("timeMs", timeMs);
        map.put("sign", sign);
        // ????????????roundSeq????????????result???????????????????????????????????????????????????????????????????????????push
        map.put("roundSeq", infoModel.getRoundSeq());

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        ApiMethods.subscribe(mRoomServerApi.sendRoundOver(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    MyLog.w(TAG, "???????????????????????? traceid is " + result.getTraceId());
                    // TODO: 2019/1/22  ???????????????????????????????????????????????????
                    // ????????????????????????????????????
//                    if (finalRoundSeq >= 0) {
//                        RoundInfoModel roundInfoModel = RoomDataUtils.findRoundInfoBySeq(mRoomData.getRoundInfoModelList(), finalRoundSeq + 1);
//                        mRoomData.setExpectRoundInfo(roundInfoModel);
//                        mRoomData.checkRound();
//                    }
                } else {
                    MyLog.w(TAG, "???????????????????????? traceid is " + result.getTraceId());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.w(TAG, "sendRoundOverInfo" + " error " + e);
            }
        }, this);
    }

    /**
     * ??????????????????????????????
     */
    public void sendRoundScoreInfo(int myRoundSeq) {
        MyLog.w(TAG, "????????????????????????");
        estimateOverTsThisRound();
        long timeMs = System.currentTimeMillis();
        int sysScore = 0;
        if (mRobotScoreHelper != null) {
            sysScore = mRobotScoreHelper.getAverageScore();
        }
        String sign = U.getMD5Utils().MD5_32("skrer|" +
                String.valueOf(mRoomData.getGameId()) + "|" +
                String.valueOf(sysScore) + "|" +
                String.valueOf(timeMs));

        HashMap<String, Object> map = new HashMap<>();
        map.put("gameID", mRoomData.getGameId());
        map.put("sysScore", sysScore);
        map.put("timeMs", timeMs);
        map.put("sign", sign);
        // ????????????roundSeq????????????result???????????????????????????????????????????????????????????????????????????push
        map.put("roundSeq", myRoundSeq);

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        ApiMethods.subscribe(mRoomServerApi.sendRoundScore(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    MyLog.w(TAG, "?????????????????????????????? traceid is " + result.getTraceId());
                    // TODO: 2019/1/22  ???????????????????????????????????????????????????
                    // ????????????????????????????????????
//                    if (finalRoundSeq >= 0) {
//                        RoundInfoModel roundInfoModel = RoomDataUtils.findRoundInfoBySeq(mRoomData.getRoundInfoModelList(), finalRoundSeq + 1);
//                        mRoomData.setExpectRoundInfo(roundInfoModel);
//                        mRoomData.checkRound();
//                    }
                } else {
                    MyLog.w(TAG, "?????????????????????????????? traceid is " + result.getTraceId());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.w(TAG, "sendRoundScoreInfo error " + e);
            }
        }, this);
    }

    /**
     * ????????????
     */
    public void exitGame() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("gameID", mRoomData.getGameId());

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        ApiMethods.subscribe(mRoomServerApi.exitGame(body), null);
    }

    /**
     * ???????????????????????????
     *
     * @param out ?????????
     * @param in  ?????????
     */
    public void swapGame(boolean out, boolean in) {
        MyLog.w(TAG, "swapGame" + " out=" + out + " in=" + in);
        HashMap<String, Object> map = new HashMap<>();
        map.put("gameID", mRoomData.getGameId());
        if (out) {
            map.put("status", SwapStatusType.SS_SWAP_OUT);
        } else if (in) {
            map.put("status", SwapStatusType.SS_SWAP_IN);
        }


        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        ApiMethods.subscribe(mRoomServerApi.swap(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
//                    U.getToastUtil().showShort("????????????????????????");
                } else {
                    MyLog.e(TAG, "swapGame result errno is " + result.getErrmsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.e(TAG, "swapGame error " + e);
            }
        }, this);
    }

    /**
     * ????????????
     */
    public void startHeartBeatTask() {
        cancelHeartBeatTask("startHeartBeatTask");
        mHeartBeatTask = Observable
                .interval(0, sHeartBeatTaskInterval, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        sendHeartBeat();
                    }
                });
    }

    public void cancelHeartBeatTask(String from) {
        MyLog.w(TAG, "????????????????????? " + from);
        if (mHeartBeatTask != null && !mHeartBeatTask.isDisposed()) {
            mHeartBeatTask.dispose();
        }
    }

    // ?????????????????????????????????????????? 2s??????
    public void sendHeartBeat() {
        MyLog.w(TAG, "sendHeartBeat");
        HashMap<String, Object> map = new HashMap<>();
        map.put("gameID", mRoomData.getGameId());
        map.put("userID", MyUserInfoManager.getInstance().getUid());

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        ApiMethods.subscribe(mRoomServerApi.sendHeartBeat(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    // TODO: 2018/12/13  ??????postman??????????????? ?????????
                    MyLog.w(TAG, "??????ok, traceid is " + result.getTraceId());
                } else {
                    MyLog.w(TAG, "???????????? traceid is " + result.getTraceId());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.w(TAG, "????????????" + e);
            }
        }, this);
    }

    /**
     * ??????????????????task
     */
    public void startSyncGameStateTask(long delayTime) {
        cancelSyncGameStateTask();

        if (mRoomData.isIsGameFinish()) {
            MyLog.w(TAG, "???????????????????????????Sync");
            return;
        }

        mSyncGameStateTask = Observable
                .interval(delayTime, sSyncStateTaskInterval, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        MyLog.w(TAG, "12????????? syncGameTask ??????????????????");
                        syncGameStatus(mRoomData.getGameId());
                    }
                }, throwable -> MyLog.w(TAG, throwable + ""));
    }

    public void cancelSyncGameStateTask() {
        if (mSyncGameStateTask != null && !mSyncGameStateTask.isDisposed()) {
            mSyncGameStateTask.dispose();
        }
    }

    // ????????????????????????(???????????????????????????)
    public void syncGameStatus(int gameID) {
        ApiMethods.subscribe(mRoomServerApi.syncGameStatus(gameID), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {

                    long syncStatusTimes = result.getData().getLongValue("syncStatusTimeMs");  //?????????????????????????????????
                    long gameOverTimeMs = result.getData().getLongValue("gameOverTimeMs");  //??????????????????
                    List<OnlineInfoModel> onlineInfos = JSON.parseArray(result.getData().getString("onlineInfo"), OnlineInfoModel.class); //????????????

                    RankRoundInfoModel currentInfo = JSON.parseObject(result.getData().getString("currentRound"), RankRoundInfoModel.class); //??????????????????
                    RankRoundInfoModel nextInfo = JSON.parseObject(result.getData().getString("nextRound"), RankRoundInfoModel.class); //??????????????????

                    String msg = "";
                    if (currentInfo != null) {
                        msg = "syncGameStatus?????????, currentRound ??? " + currentInfo.getUserID();
                    } else {
                        msg = "syncGameStatus?????????, currentRound ??? null";
                    }

                    if (nextInfo != null) {
                        msg = msg + ", nextRound ??? " + nextInfo.getUserID();
                    } else {
                        msg = msg + ", nextRound ??? null";
                    }

                    msg = msg + ",traceid is " + result.getTraceId();
                    MyLog.w(TAG, msg);

                    if (currentInfo == null && nextInfo == null) {
                        cancelSyncGameStateTask();
                        recvGameOverFromServer("syncGameStatus", gameOverTimeMs);
                        return;
                    }
                    updatePlayerState(gameOverTimeMs, syncStatusTimes, onlineInfos, currentInfo, nextInfo);
                } else {
                    MyLog.w(TAG, "syncGameStatus?????? traceid is " + result.getTraceId());
                    estimateOverTsThisRound();
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.w(TAG, "syncGameStatus????????????errno???" + e);
            }
        }, this);
    }

    /**
     * pk?????????
     */
    public void pklightOff() {
        MyLog.d(TAG, "pklightOff");
        HashMap<String, Object> map = new HashMap<>();
        map.put("gameID", mRoomData.getGameId());
        map.put("roundSeq", mRoomData.getRealRoundSeq());

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        ApiMethods.subscribe(mRoomServerApi.pklightOff(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    // TODO: 2019/2/21 ????????????
                    MyLog.w(TAG, "??????ok, traceid is " + result.getTraceId());
                } else {
                    MyLog.w(TAG, "????????????, traceid is " + result.getTraceId());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.w(TAG, "????????????, errno is " + e);
            }
        }, this);
    }

    public void pkburst() {
        MyLog.d(TAG, "pkburst");
        HashMap<String, Object> map = new HashMap<>();
        map.put("gameID", mRoomData.getGameId());
        map.put("roundSeq", mRoomData.getRealRoundSeq());

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        ApiMethods.subscribe(mRoomServerApi.pkburst(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    // TODO: 2019/2/21 ????????????
                    MyLog.w(TAG, "??????ok, traceid is " + result.getTraceId());
                } else {
                    MyLog.w(TAG, "????????????, traceid is " + result.getTraceId());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.w(TAG, "????????????, errno is " + e);
            }
        }, this);
    }

    /**
     * ?????????????????????????????????,??????????????????????????????SyncStatusEvent push???sycn?????????????????????
     */
    private synchronized void updatePlayerState(long gameOverTimeMs, long syncStatusTimes, List<OnlineInfoModel> onlineInfos, RankRoundInfoModel currentInfo, RankRoundInfoModel nextInfo) {
        MyLog.w(TAG, "updatePlayerState" + " gameOverTimeMs=" + gameOverTimeMs + " syncStatusTimes=" + syncStatusTimes + " onlineInfos=" + onlineInfos + " currentInfo=" + currentInfo + " nextInfo=" + nextInfo);
        if (syncStatusTimes > mRoomData.getLastSyncTs()) {
            mRoomData.setLastSyncTs(syncStatusTimes);
            if (onlineInfos != null) {
                for (OnlineInfoModel onlineInfoModel : onlineInfos) {
                    mRoomData.setOnline(onlineInfoModel.getUserID(), onlineInfoModel.isIsOnline());
                }
            }
            mIGameRuleView.updateUserState(onlineInfos);
        }
        if (gameOverTimeMs != 0) {
            if (gameOverTimeMs > mRoomData.getGameStartTs()) {
                MyLog.w(TAG, "gameOverTimeMs ???= 0 ?????????????????????");
                // ???????????????
                recvGameOverFromServer("sync", gameOverTimeMs);
            } else {
                MyLog.w(TAG, "?????????????????????????????? startTs:" + mRoomData.getGameStartTs() + " overTs:" + gameOverTimeMs);
            }
        } else {
            // ????????? current ????????????null
            if (currentInfo != null) {
                // ?????????????????????????????????????????????????????????????????????????????????????????????
                if (RoomDataUtils.roundSeqLarger(currentInfo, mRoomData.getRealRoundInfo())) {
                    MyLog.w(TAG, "updatePlayerState" + " sync???????????????????????????????????????");
                    // ??????????????????????????????????????????
                    mRoomData.setExpectRoundInfo(RoomDataUtils.getRoundInfoFromRoundInfoListInRankMode(mRoomData, currentInfo));
                    mRoomData.checkRoundInEachMode();
                } else if (RoomDataUtils.roundInfoEqual(currentInfo, mRoomData.getRealRoundInfo())) {
                    // TODO: 2019/2/21 ????????????round?????????
                    if (mRoomData.getRealRoundInfo() != null) {
                        mRoomData.getRealRoundInfo().tryUpdateRoundInfoModel(currentInfo, true);
                    }
                }
            } else {
                MyLog.w(TAG, "?????????????????????????????? currentInfo=null");
            }
        }
    }

    /**
     * ?????????????????????
     * ????????????
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(RankRoundInfoChangeEvent event) {
        MyLog.w(TAG, "?????????????????? myturn=" + event.myturn);
        estimateOverTsThisRound();
        //????????????
        tryStopRobotPlay();
        mUiHandler.removeMessages(MSG_START_LAST_TWO_SECONDS_TASK);
        mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS);
        if (event.myturn) {
            // ???????????????
            // ???????????????
            if (U.getActivityUtils().isAppForeground()) {
                startHeartBeatTask();
            }

            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    // ??????????????? 3 2 1
                    mIGameRuleView.startSelfCountdown(event.getLastRoundInfoModel(), new Runnable() {
                        @Override
                        public void run() {
                            //????????????
                            if (mRoomData.getRealRoundInfo() != null && mRoomData.getRealRoundInfo().getUserID() == MyUserInfoManager.getInstance().getUid()) {
                                ZqEngineKit.getInstance().setClientRole(true);
                                // ??????????????????????????????
                                Message msg = mUiHandler.obtainMessage(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS);
                                mUiHandler.sendMessageDelayed(msg, 2000);
                            }
                        }
                    });
                    if (mRoomData.getRealRoundInfo() != null) {
                        MyLog.w(TAG, "?????????????????????" + U.getDateTimeUtils().formatTimeStringForDate(mRoomData.getGameStartTs() + mRoomData.getRealRoundInfo().getSingBeginMs(), "HH:mm:ss:SSS")
                                + "--" + U.getDateTimeUtils().formatTimeStringForDate(mRoomData.getGameStartTs() + mRoomData.getRealRoundInfo().getSingEndMs(), "HH:mm:ss:SSS"));
                    }
                }
            });
        } else {
            MyLog.w(TAG, "????????????????????????????????????????????????????????????");

            cancelHeartBeatTask("????????????");

            if (RoomDataUtils.isMyRound(event.getLastRoundInfoModel())) {
                if (SkrConfig.getInstance().isNeedUploadAudioForAI(GameModeType.GAME_MODE_CLASSIC_RANK)) {
                    //???????????????????????????????????????
                    // ???????????????????????????????????????
                    ZqEngineKit.getInstance().stopAudioRecording();
                    RankRoundInfoModel myRoundInfoModel = event.getLastRoundInfoModel();
                    if (mRobotScoreHelper != null && mRobotScoreHelper.isScoreEnough()) {
                        myRoundInfoModel.setSysScore(mRobotScoreHelper.getAverageScore());
                        uploadRes1ForAi(myRoundInfoModel);
                    }
                }
                // ????????????
                sendRoundScoreInfo(event.getLastRoundInfoModel().getRoundSeq());
            }

            ZqEngineKit.getInstance().stopAudioMixing();
            ZqEngineKit.getInstance().setClientRole(false);
//            ZqEngineKit.getInstance().muteLocalAudioStream(true);
            // ??????????????????onMute?????? ????????????????????????????????????????????????????????????
            if (mRoomData.getRealRoundInfo() != null) {
                // ???????????????
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        int uid =0;
                        if(mRoomData.getRealRoundInfo()!=null){
                            uid = mRoomData.getRealRoundInfo().getUserID();
                        }
                        PlayerInfoModel playerInfoModel = RoomDataUtils.getPlayerInfoById(mRoomData, uid);
                        String avatar = "";
                        if (playerInfoModel != null) {
                            avatar = playerInfoModel.getUserInfo().getAvatar();
                        }
                        mIGameRuleView.startRivalCountdown(event.getLastRoundInfoModel(), uid, avatar);
                        checkMachineUser(uid);
                        if (mRoomData.getRealRoundInfo() != null) {
                            MyLog.w(TAG, uid + "???????????????????????????,?????????????????????" + U.getDateTimeUtils().formatTimeStringForDate(mRoomData.getGameStartTs() + mRoomData.getRealRoundInfo().getSingBeginMs(), "HH:mm:ss:SSS")
                                    + "--" + U.getDateTimeUtils().formatTimeStringForDate(mRoomData.getGameStartTs() + (mRoomData.getRealRoundInfo() == null ? 0 : mRoomData.getRealRoundInfo().getSingEndMs()), "HH:mm:ss:SSS"));
                        } else {
                            MyLog.w(TAG, "mRoomData.getRealRoundInfo() ?????????????????????");
                        }
                        //mIGameRuleView.playLyric(RoomDataUtils.getPlayerSongInfoUserId(mRoomData.getPlayerInfoList(), uid), false);
                    }
                });
            } else if (mRoomData.getRealRoundInfo() == null) {
                if (mRoomData.getGameOverTs() > mRoomData.getGameStartTs()) {
                    // ????????????
                    cancelSyncGameStateTask();
                    // ???????????????,???????????????ui??????
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            gameIsFinish();
                            mIGameRuleView.gameFinish();
                        }
                    });
                } else {
                    MyLog.w(TAG, "????????????????????????????????????????????? startTs:" + mRoomData.getGameStartTs() + " overTs:" + mRoomData.getGameOverTs());
                }
            }
        }
    }

    private void robotSingBegin(RankPlayerInfoModel playerInfo) {
        String skrerUrl = playerInfo.getResourceInfoList().get(0).getAudioURL();
        String midiUrl = playerInfo.getResourceInfoList().get(0).getMidiURL();
        if (mRobotScoreHelper == null) {
            mRobotScoreHelper = new RobotScoreHelper();
        }
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) {
                mRobotScoreHelper.loadDataFromUrl(midiUrl, 0);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();

        if (mExoPlayer == null) {
            mExoPlayer = new ExoPlayer();
        }
        mExoPlayer.startPlay(skrerUrl);
        if (!ZqEngineKit.getInstance().getParams().isAllRemoteAudioStreamsMute()) {
            mExoPlayer.setVolume(1);
        } else {
            mExoPlayer.setVolume(0);
        }
        //??????????????????
        othersBeginSinging();
    }

    private void tryStopRobotPlay() {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mExoPlayer != null) {
                    mExoPlayer.reset();
                }
            }
        });
    }

    public void sendBurst(int seq) {
        BaseRoundInfoModel roundInfoModel = mRoomData.getRealRoundInfo();
        if (roundInfoModel != null && roundInfoModel.getRoundSeq() == seq) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("gameID", mRoomData.getGameId());
            map.put("roundSeq", roundInfoModel.getRoundSeq());
            RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
            ApiMethods.subscribe(mRoomServerApi.pkburst(body), new ApiObserver<ApiResult>() {
                @Override
                public void process(ApiResult result) {
                    if (result.getErrno() == 0) {
                        MyLog.w(TAG, "???????????? traceid is " + result.getTraceId() + "???seq???" + roundInfoModel.getRoundSeq());
                        mRoomData.consumeBurstLightTimes(roundInfoModel);
                    } else {
                        MyLog.w(TAG, "?????????????????? traceid is " + result.getTraceId() + "???seq???" + roundInfoModel.getRoundSeq());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    MyLog.w(TAG, "burst" + " error " + e);
                }

                @Override
                public void onNetworkError(ErrorType errorType) {
                    MyLog.w(TAG, "burst" + " errorType " + errorType);
                }
            }, this);
        } else {
            MyLog.d(TAG, "??????????????????????????????" + "seq=" + seq);
        }
    }

    public void sendLightOff(int seq) {
        BaseRoundInfoModel roundInfoModel = mRoomData.getRealRoundInfo();
        if (roundInfoModel != null && roundInfoModel.getRoundSeq() == seq) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("gameID", mRoomData.getGameId());
            map.put("roundSeq", roundInfoModel.getRoundSeq());
            RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
            ApiMethods.subscribe(mRoomServerApi.pklightOff(body), new ApiObserver<ApiResult>() {

                @Override
                public void process(ApiResult result) {
                    if (result.getErrno() == 0) {
                        MyLog.w(TAG, "???????????? traceid is " + result.getTraceId() + "???seq???" + roundInfoModel.getRoundSeq());
                        mRoomData.consumeLightOffTimes(roundInfoModel);
                    } else {
                        MyLog.w(TAG, "?????????????????? traceid is " + result.getTraceId() + "???seq???" + roundInfoModel.getRoundSeq());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    MyLog.w(TAG, "lightOff" + " error " + e);
                }

                @Override
                public void onNetworkError(ErrorType errorType) {
                    MyLog.w(TAG, "lightOff" + " errorType " + errorType);
                }
            }, this);
        } else {
            MyLog.d(TAG, "??????????????????????????????" + "seq=" + seq);
        }
    }

    private void checkMachineUser(int uid) {
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
            //??????????????????????????????????????????????????????????????????4000?????????????????????????????????7000
            BaseRoundInfoModel roundInfoModel = mRoomData.getRealRoundInfo();
            long delayTime = 4000l;
            //???????????????????????????????????????deley6???
            if (roundInfoModel.getRoundSeq() == 1) {
                delayTime = 6000l;
            }
            //??????????????????????????????????????????
            mUiHandler.removeMessages(MSG_ROBOT_SING_BEGIN);
            Message message = mUiHandler.obtainMessage(MSG_ROBOT_SING_BEGIN);
            message.obj = playerInfo;
            mUiHandler.sendMessageDelayed(message, delayTime);
        }
    }

    /**
     * ?????????????????????,????????????????????????Sync?????????Sync push???????????????RoundAndGameOver push
     */
    private void gameIsFinish() {
        mRoomData.setIsGameFinish(true);
        cancelHeartBeatTask("gameIsFinish");
        cancelSyncGameStateTask();
        // ????????????????????????????????????????????????
        ZqEngineKit.getInstance().destroy("rankingroom");
        EventBus.getDefault().isRegistered(this);
    }

    private void recvGameOverFromServer(String from, long gameOverTs) {
        MyLog.w(TAG, "???????????? gameOverTs=" + gameOverTs + " from:" + from);
        if (gameOverTs > mRoomData.getGameStartTs() && gameOverTs > mRoomData.getGameOverTs()) {
            mRoomData.setGameOverTs(gameOverTs);
            mRoomData.setExpectRoundInfo(null);
            mRoomData.checkRoundInEachMode();
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
     * ?????????????????????????????????
     *
     * @param roundInfoModel
     */
    private void uploadRes1ForAi(RankRoundInfoModel roundInfoModel) {
        if (mRobotScoreHelper != null && mRobotScoreHelper.vilid()) {
            UploadParams.newBuilder(RoomDataUtils.getSaveAudioForAiFilePath())
                    .setFileType(UploadParams.FileType.audioAi)
                    .startUploadAsync(new UploadCallback() {
                        @Override
                        public void onProgressNotInUiThread(long currentSize, long totalSize) {

                        }

                        @Override
                        public void onSuccessNotInUiThread(String url) {
                            uploadRes2ForAi(roundInfoModel, url);
                        }

                        @Override
                        public void onFailureNotInUiThread(String msg) {

                        }
                    });
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param roundInfoModel
     * @param audioUrl
     */
    private void uploadRes2ForAi(RankRoundInfoModel roundInfoModel, String audioUrl) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                String path = RoomDataUtils.getSaveMatchingSocreForAiFilePath();
                if (mRobotScoreHelper != null) {
                    mRobotScoreHelper.save(path);
                    UploadParams.newBuilder(path)
                            .setFileType(UploadParams.FileType.midiAi)
                            .startUploadAsync(new UploadCallback() {
                                @Override
                                public void onProgressNotInUiThread(long currentSize, long totalSize) {

                                }

                                @Override
                                public void onSuccessNotInUiThread(String url) {
                                    sendUploadRequest(roundInfoModel, audioUrl, url);
                                }

                                @Override
                                public void onFailureNotInUiThread(String msg) {

                                }
                            });
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param roundInfoModel
     * @param audioUrl
     * @param midiUrl
     */
    private void sendUploadRequest(RankRoundInfoModel roundInfoModel, String audioUrl, String midiUrl) {
        long timeMs = System.currentTimeMillis();
        HashMap<String, Object> map = new HashMap<>();
        map.put("gameID", mRoomData.getGameId());
        map.put("itemID", roundInfoModel.getPlaybookID());
        map.put("sysScore", roundInfoModel.getSysScore());
        map.put("audioURL", audioUrl);
        map.put("midiURL", midiUrl);
        map.put("timeMs", timeMs);
        StringBuilder sb = new StringBuilder();
        sb.append("skrer")
                .append("|").append(mRoomData.getGameId())
                .append("|").append(roundInfoModel.getPlaybookID())
                .append("|").append(roundInfoModel.getSysScore())
                .append("|").append(audioUrl)
                .append("|").append(midiUrl)
                .append("|").append(timeMs);
        String sign = U.getMD5Utils().MD5_32(sb.toString());
        map.put("sign", sign);
        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        ApiMethods.subscribe(mRoomServerApi.putGameResource(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    MyLog.e(TAG, "sendAiUploadRequest success");
                } else {
                    MyLog.e(TAG, "sendAiUploadRequest failed??? errno is " + result.getErrmsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.e(TAG, "sendUploadRequest error " + e);
            }
        }, this);
    }

    private int estimateOverTsThisRound() {
        int pt = RoomDataUtils.estimateTs2End(mRoomData, mRoomData.getRealRoundInfo());
        MyLog.w(TAG, "?????????????????????????????????" + pt + "ms");
        return pt;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(AccBeginEvent event) {
        onUserSpeakFromEngine("AccBeginEvent", event.userId);
    }

    //?????????push??????????????????
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(PkBurstLightMsgEvent event) {
        if (RoomDataUtils.isCurrentRunningRound(event.getpKBLightMsg().getRoundSeq(), mRoomData)) {
            MyLog.w(TAG, "??????????????????userID " + event.getpKBLightMsg().getUserID() + ", seq " + event.getpKBLightMsg().getRoundSeq());
            RankRoundInfoModel roundInfoModel = (RankRoundInfoModel) mRoomData.getRealRoundInfo();

            BLightInfoModel bLightInfoModel = new BLightInfoModel();
            bLightInfoModel.setUserID(event.getpKBLightMsg().getUserID());
            bLightInfoModel.setTimeMs((int) event.getInfo().getTimeMs());
            bLightInfoModel.setSeq(event.getpKBLightMsg().getRoundSeq());

            roundInfoModel.addBrustLightUid(true, bLightInfoModel);
        } else {
            BaseRoundInfoModel roundInfoModel = mRoomData.getRealRoundInfo();
            if (roundInfoModel != null && event.getpKBLightMsg().getRoundSeq() > roundInfoModel.getRoundSeq()) {
                // TODO: 2019/2/20  ?????????????????????round??????????????????????????????????????????round??????sync
            }
            MyLog.w(TAG, "???????????????,???????????????????????????userID " + event.getpKBLightMsg().getUserID() + ", seq " + event.getpKBLightMsg().getRoundSeq() + "?????????????????? " + mRoomData.getRealRoundSeq());
        }
    }

    //?????????push??????????????????
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(PkLightOffMsgEvent event) {
        if (RoomDataUtils.isCurrentRunningRound(event.getPKMLightMsg().getRoundSeq(), mRoomData)) {
            MyLog.w(TAG, "??????????????????userID " + event.getPKMLightMsg().getUserID() + ", seq " + event.getPKMLightMsg().getRoundSeq());
            RankRoundInfoModel roundInfoModel = (RankRoundInfoModel) mRoomData.getRealRoundInfo();

            MLightInfoModel mLightInfoModel = new MLightInfoModel();
            mLightInfoModel.setUserID(event.getPKMLightMsg().getUserID());
            mLightInfoModel.setTimeMs((int) event.getInfo().getTimeMs());
            mLightInfoModel.setSeq(event.getPKMLightMsg().getRoundSeq());

            roundInfoModel.addPkLightOffUid(true, mLightInfoModel);
        } else {
            BaseRoundInfoModel roundInfoModel = mRoomData.getRealRoundInfo();
            if (roundInfoModel != null && event.getPKMLightMsg().getRoundSeq() > roundInfoModel.getRoundSeq()) {
                // TODO: 2019/2/20  ?????????????????????round??????????????????????????????????????????round??????sync
            }
            MyLog.w(TAG, "???????????????,???????????????????????????userID " + event.getPKMLightMsg().getUserID() + ", seq " + event.getPKMLightMsg().getRoundSeq() + "?????????????????? " + mRoomData.getRealRoundSeq());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(EngineEvent event) {
        if (event.getType() == EngineEvent.TYPE_USER_JOIN) {
            int userId = event.getUserStatus().getUserId();
            onUserSpeakFromEngine("TYPE_USER_JOIN", userId);
        } else if (event.getType() == EngineEvent.TYPE_USER_ROLE_CHANGE) {
            EngineEvent.RoleChangeInfo roleChangeInfo = event.getObj();
            if (roleChangeInfo.getNewRole() == 1) {
                onChangeBroadcastSuccess();
            }
        } else if (event.getType() == EngineEvent.TYPE_USER_AUDIO_VOLUME_INDICATION) {
//            if (RoomDataUtils.isMyRound(mRoomData.getRealRoundInfo())) {
//                if (event.getObj() != null) {
//                    List<EngineEvent.UserVolumeInfo> list = (List<EngineEvent.UserVolumeInfo>) event.getObj();
//                    for (EngineEvent.UserVolumeInfo info : list) {
//
//                        if (info.getUid() == UserAccountManager.getInstance().getUuidAsLong()
//                                || info.getUid() == 0) {
//
//                            break;
//                        }
//                    }
//                }
//            }
        } else if (event.getType() == EngineEvent.TYPE_MUSIC_PLAY_FINISH) {
            //???????????????????????????????????????????????????
            sendRoundOverInfo("TYPE_MUSIC_PLAY_FINISH");
        } else if (event.getType() == EngineEvent.TYPE_MUSIC_PLAY_TIME_FLY_LISTENER) {
            EngineEvent.MixMusicTimeInfo timeInfo = (EngineEvent.MixMusicTimeInfo) event.getObj();
            //????????????????????????????????????????????????
            if (timeInfo.getCurrent() >= mRoomData.getSongModel().getEndMs() - mRoomData.getSongModel().getBeginMs()) {
                //?????????????????????????????????
                sendRoundOverInfo("TYPE_MUSIC_PLAY_TIME_FLY_LISTENER");
            }
        } else if (event.getType() == EngineEvent.TYPE_USER_MUTE_AUDIO) {
            int muteUserId = event.getUserStatus().getUserId();
            if (!event.getUserStatus().isAudioMute()) {
                MyLog.w(TAG, "EngineEvent muteUserId=" + muteUserId + "?????????");
                onUserSpeakFromEngine("TYPE_USER_MUTE_AUDIO", muteUserId);
            } else {
                /**
                 * ????????????????????????????????????????????????????????????????????????????????????
                 * ??????????????????????????????????????????????????????
                 */
                MyLog.w(TAG, "???????????????????????????????????????id???" + muteUserId);
            }
        }
    }

    /**
     * ?????????????????????
     */
    private void onChangeBroadcastSuccess() {
        mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS);
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                RankRoundInfoModel infoModel = mRoomData.getRealRoundInfo();
                if (infoModel == null || infoModel.getUserID() != MyUserInfoManager.getInstance().getUid()) {
                    MyLog.d(TAG, "onChangeBroadcastSuccess,?????????????????????????????????cancel");
                    return;
                }
                // ??????????????????????????????????????????mute
                File accFile = SongResUtils.getAccFileByUrl(mRoomData.getSongModel().getAcc());
                File midiFile = SongResUtils.getMIDIFileByUrl(mRoomData.getSongModel().getMidi());

                if (accFile != null && accFile.exists()) {
//                                    ZqEngineKit.getInstance().muteLocalAudioStream(false);
                    ZqEngineKit.getInstance().startAudioMixing((int) MyUserInfoManager.getInstance().getUid(), accFile.getAbsolutePath()
                            , midiFile == null ? "" : midiFile.getAbsolutePath(), mRoomData.getSongModel().getBeginMs(), false, false, 1);
                    /**
                     * ?????????????????????????????????getSingBeginMs???getSingEndMs??????????????????????????????0??????????????????
                     */
                    ZqEngineKit.getInstance().setAudioMixingPosition(0);
                    // ????????????????????????
                    mIGameRuleView.playLyric(mRoomData.getSongModel());
                    mIGameRuleView.showLeftTime(infoModel.getSingEndMs() - infoModel.getSingBeginMs());
                    MyLog.w(TAG, "??????????????????????????????????????????");
                    mRoomData.setSingBeginTs(System.currentTimeMillis());
                    //??????????????????
                    if (SkrConfig.getInstance().isNeedUploadAudioForAI(GameModeType.GAME_MODE_CLASSIC_RANK)) {
                        // ????????????????????????????????????
                        ZqEngineKit.getInstance().startAudioRecording(RoomDataUtils.getSaveAudioForAiFilePath(), false);
                    }
                    mRobotScoreHelper = new RobotScoreHelper();
                    //??????????????????????????????
                    sendUserSpeakEventToOthers();
                } else {
                    MyLog.e(TAG, "onChangeBroadcastSuccess acc ?????????????????????????????????");
                }
            }
        });
    }

    private void sendUserSpeakEventToOthers() {
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

            RoomMsg roomMsg = new RoomMsg.Builder()
                    .setTimeMs(ts)
                    .setMsgType(ERoomMsgType.RM_ROUND_ACC_BEGIN)
                    .setRoomID(mRoomData.getGameId())
                    .setNo(ts)
                    .setPosType(EMsgPosType.EPT_UNKNOWN)
                    .setSender(senderInfo)
                    .build();

            String contnet = U.getBase64Utils().encode(roomMsg.toByteArray());
            msgService.sendChatRoomMessage(String.valueOf(mRoomData.getGameId()), CustomMsgType.MSG_TYPE_ROOM, contnet, null);
        }
    }

    private void onUserSpeakFromEngine(String from, int muteUserId) {
        MyLog.w(TAG, "onUserSpeakFromEngine muteUserId=" + muteUserId + "?????????,from:" + from);
        RankRoundInfoModel infoModel = RoomDataUtils.getRoundInfoByUserId(mRoomData, muteUserId);
        if (infoModel != null && infoModel.getUserID() == MyUserInfoManager.getInstance().getUid()) {
            MyLog.d(TAG, "onUserSpeakFromEngine" + " ???????????????????????????");
            return;
        }
        /**
         * ??????????????????mute??????????????????????????????????????????????????????
         * ????????????????????????????????????????????????????????????????????????????????????????????????
         * ??????????????????????????????????????????????????????????????????????????????????????????.
         * ?????????????????????
         */
        if (infoModel != null && infoModel.getUserID() != MyUserInfoManager.getInstance().getUid()) {
            if (RoomDataUtils.roundInfoEqual(infoModel, mRoomData.getRealRoundInfo())) {
                //????????????????????????,?????????
                MyLog.w(TAG, "???????????????????????????,?????????");
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyLog.d(TAG, "???????????????????????????????????????????????????????????????????????? ????????????id???" + muteUserId);
                        othersBeginSinging();
                    }
                });
            } else if (RoomDataUtils.roundSeqLarger(infoModel, mRoomData.getExpectRoundInfo())) {
                // ??????????????????????????????????????????????????????????????????
                MyLog.w(TAG, "????????????????????????????????????????????????????????????,??????????????????");
                // ?????????????????????????????????????????????????????????????????????????????????????????????
                mRoomData.setExpectRoundInfo(infoModel);
                mRoomData.checkRoundInEachMode();
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyLog.w(TAG, "????????????????????????????????????????????????????????????????????????????????????????????????,??????????????????  ????????????id???" + muteUserId);
                        othersBeginSinging();
                    }
                });
            }
        } else {
            MyLog.w(TAG, "???????????????????????????????????? ?????????????????????????????????????????????????????????");
        }
    }

    /**
     * ?????????????????????????????????
     */
    private void othersBeginSinging() {
        RankRoundInfoModel infoModel = mRoomData.getRealRoundInfo();
        if (infoModel != null && !infoModel.isHasSing()) {
            infoModel.setHasSing(true);
            mIGameRuleView.showLeftTime(infoModel.getSingEndMs() - infoModel.getSingBeginMs());
            //mIGameRuleView.playLyric(RoomDataUtils.getPlayerSongInfoUserId(mRoomData.getPlayerInfoList(), infoModel.getUserID()), true);
            mIGameRuleView.onOtherStartSing(RoomDataUtils.getPlayerSongInfoUserId(mRoomData.getPlayerAndWaiterInfoList(), infoModel.getUserID()));
            startLastTwoSecondTask();
        }
    }

    private void startLastTwoSecondTask() {
        final RankRoundInfoModel roundInfoModel = mRoomData.getRealRoundInfo();
        if (roundInfoModel != null) {
            mUiHandler.removeMessages(MSG_START_LAST_TWO_SECONDS_TASK);
            Message message = mUiHandler.obtainMessage(MSG_START_LAST_TWO_SECONDS_TASK);
            message.obj = roundInfoModel;
            mUiHandler.sendMessageDelayed(message, roundInfoModel.getSingEndMs() - roundInfoModel.getSingBeginMs() - 2000);
        } else {
            MyLog.e(TAG, "startLastTwoSecondTask roundInfoModel ??? null, ?????????????????????");
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     *
     * @param lineNum
     */
    public void sendTotalScoreToOthers(int lineNum) {
        MachineScoreItem machineScoreItem = new MachineScoreItem();
        machineScoreItem.setScore(999);// ???ios?????????????????????????????????999??????????????????????????????????????????
        long ts = ZqEngineKit.getInstance().getAudioMixingCurrentPosition();
        machineScoreItem.setTs(ts);
        machineScoreItem.setNo(lineNum);
        sendScoreToOthers(machineScoreItem);
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
                            .setItemID(mRoomData.getSongModel().getItemID())
                            .setLineNum(mRoomData.getSongLineNum())
                            .build()
                    )
                    .build();
            String contnet = U.getBase64Utils().encode(roomMsg.toByteArray());
            msgService.sendChatRoomMessage(String.valueOf(mRoomData.getGameId()), CustomMsgType.MSG_TYPE_ROOM, contnet, null);
        }
    }

    // ?????????????????????????????????????????????????????????????????????????????????)
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventMainThread(RoundOverEvent event) {
        ensureInRcRoom();
        MyLog.w(TAG, "?????????????????????????????????????????????push???id??? " + event.currenRound.getUserID()
                + ", exitUserID ??? " + event.exitUserID + " timets ???" + event.info.getTimeMs());
        if (mRoomData.getLastSyncTs() > event.info.getTimeMs()) {
            MyLog.w(TAG, "??????????????????");
            return;
        }

        if (event.exitUserID != 0) {
            mRoomData.setOnline(event.exitUserID, false);
        }
        if (RoomDataUtils.isCurrentRunningRound(event.currenRound.getRoundSeq(), mRoomData)) {
            // ?????????????????????
            mRoomData.getRealRoundInfo().tryUpdateRoundInfoModel(event.currenRound, true);
        }
        // ??????????????????
        if (RoomDataUtils.roundSeqLarger(event.nextRound, mRoomData.getRealRoundInfo())) {
            // ??????????????????????????????????????????
            MyLog.w(TAG, "??????????????????????????????????????????");
            mRoomData.setExpectRoundInfo(RoomDataUtils.getRoundInfoFromRoundInfoListInRankMode(mRoomData, event.nextRound));
            mRoomData.checkRoundInEachMode();
        }
    }

    //???????????????????????????????????????????????????????????????????????????????????????
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventMainThread(RoundAndGameOverEvent event) {
        MyLog.w(TAG, "?????????????????????????????????push timets ??? " + event.info.getTimeMs());

        MyLog.d(TAG, "onEventMainThread" + " roundAndGameOverEvent mVoteInfoModels =" + event.mVoteInfoModels);
        MyLog.d(TAG, "onEventMainThread" + " roundAndGameOverEvent mScoreResultModel =" + event.mScoreResultModel);
        MyLog.d(TAG, "onEventMainThread" + " roundAndGameOverEvent mUserGameResultModels =" + event.mUserGameResultModels);
        if (event.mExitUserID != 0) {
            mRoomData.setOnline(event.mExitUserID, false);
        }
        if (RoomDataUtils.isCurrentRunningRound(event.mRankRoundInfoModel.getRoundSeq(), mRoomData)) {
            mRoomData.getRealRoundInfo().tryUpdateRoundInfoModel(event.mRankRoundInfoModel, true);
        }

        RecordData recordData = new RecordData(event.mVoteInfoModels,
                event.mScoreResultModel,
                event.mUserGameResultModels);
        mRoomData.setRecordData(recordData);

        recvGameOverFromServer("push", event.roundOverTimeMs);
        cancelSyncGameStateTask();
    }

    // ??????????????????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AppSwapEvent appSwapEvent) {
        MyLog.w(TAG, "onEventMainThread syncStatusEvent");
    }

    // ????????? ????????????????????? ???11??????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SyncStatusEvent syncStatusEvent) {
        ensureInRcRoom();
        String msg = "??????????????????????????????push???, currentRound ??? ";
        msg = msg + (syncStatusEvent.currentInfo == null ? "null" : syncStatusEvent.currentInfo.getUserID() + "");
        msg = msg + ", nextInfo ??? " + (syncStatusEvent.nextInfo == null ? "null" : syncStatusEvent.nextInfo.getUserID() + "");
        msg = msg + ", timeMs" + syncStatusEvent.info.getTimeMs();
        MyLog.w(TAG, msg);
//        startSyncGameStateTask(sSyncStateTaskInterval);

        updatePlayerState(syncStatusEvent.gameOverTimeMs, syncStatusEvent.syncStatusTimes, syncStatusEvent.onlineInfos, syncStatusEvent.currentInfo, syncStatusEvent.nextInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ExitGameEvent event) {
        MyLog.w(TAG, "????????????????????????push??????type???" + event.type + ",timeMs???" + event.info.getTimeMs());
        if (event.type == EXIT_GAME_AFTER_PLAY) {   //?????????????????????????????????
//            U.getToastUtil().showShort("???????????????????????????????????????");
        } else if (event.type == EXIT_GAME_OUT_ROUND) {   //?????????????????????????????????
//            U.getToastUtil().showShort("?????????????????????????????????");
        }
        mRoomData.setOnline(event.exitUserID, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PkSomeOneOnlineChangeEvent event) {
        UserInfoModel userInfo = mRoomData.getPlayerOrWaiterInfo(event.model.getUserID());
        CommentSysModel commentSysModel = new CommentSysModel(userInfo.getNicknameRemark(), "???????????????");
        EventBus.getDefault().post(new PretendCommentMsgEvent(commentSysModel));

        if (event.model.getUserID() == MyUserInfoManager.getInstance().getUid()) {
            MyLog.d(TAG, "??????????????????????????????????????????,id???" + event.model.getUserID());
            U.getToastUtil().showShort("?????????????????????????????????????????????????????????");
            mIGameRuleView.finishActivity();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActivityUtils.ForeOrBackgroundChange event) {
        MyLog.w(TAG, event.foreground ? "???????????????" : "???????????????");
        swapGame(!event.foreground, event.foreground);
        if (mRoomData.getRealRoundInfo() != null
                && mRoomData.getRealRoundInfo().getUserID() == MyUserInfoManager.getInstance().getUid()) {
            if (event.foreground) {
                startHeartBeatTask();
            } else {
                cancelHeartBeatTask("???????????????");
            }
        }

        if (event.foreground) {
            muteAllRemoteAudioStreams(mRoomData.isMute(), false);
        } else {
            muteAllRemoteAudioStreams(true, false);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(LrcEvent.LineLineEndEvent event) {
        MyLog.d(TAG, "onEvent LineEndEvent lineno=" + event.lineNum);
        /**
         * ??????????????????
         */
        if (RoomDataUtils.isRobotRound(mRoomData.getRealRoundInfo(), mRoomData.getPlayerAndWaiterInfoList())) {
            // ?????????????????????????????????
            if (mRobotScoreHelper != null) {
                int score = mRobotScoreHelper.tryGetScoreByLine(event.lineNum);
                if (score >= 0) {
//                        U.getToastUtil().showShort("score:" + score);
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mIGameRuleView.updateScrollBarProgress(score, mRobotScoreHelper.tryGetTotalScoreByLine(event.lineNum), mRobotScoreHelper.tryGetScoreLineNum());
                        }
                    });
                }
            }
        } else {
            // ?????????????????????????????????
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LyricAndAccMatchManager.ScoreResultEvent event) {
        int line = event.getLine();
        int acrScore = event.getAcrScore();
        int melpScore = event.getMelpScore();
        String from = event.getFrom();
        if (melpScore > acrScore) {
            processScore(from, melpScore, line);
        } else {
            processScore(from, acrScore, line);
        }
    }

    void processScore(String from, int score, int line) {
        MyLog.d(TAG, "processScore" + " from=" + from + " score=" + score + " line=" + line);
        if (score < 0) {
            return;
        }
        MyLog.d(TAG, "onEvent" + " ??????=" + score);
        MachineScoreItem machineScoreItem = new MachineScoreItem();
        machineScoreItem.setScore(score);
        long ts = ZqEngineKit.getInstance().getAudioMixingCurrentPosition();
        machineScoreItem.setTs(ts);
        machineScoreItem.setNo(line);
        mRoomData.setCurSongTotalScore(mRoomData.getCurSongTotalScore() + score);
        // ??????????????????????????????
        sendScoreToOthers(machineScoreItem);
        if (mRobotScoreHelper != null) {
            mRobotScoreHelper.add(machineScoreItem);
        }
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mIGameRuleView.updateScrollBarProgress(score, mRoomData.getCurSongTotalScore(), mRoomData.getSongLineNum());
            }
        });
        //?????????????????????
        sendScoreToServer(score, line);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MachineScoreEvent event) {
        ensureInRcRoom();
        //???????????????????????????????????????????????????????????????????????????????????????????????????
        if (RoomDataUtils.isThisUserRound(mRoomData.getRealRoundInfo(), event.userId)) {
            mIGameRuleView.updateScrollBarProgress(event.score, event.totalScore, event.lineNum);
        } else {
            MyLog.d(TAG, "????????????????????????????????????????????????event.userId=" + event.userId);
            if (mRoomData.getRealRoundInfo() != null) {
                MyLog.d(TAG, "??????????????? ??????????????????????????? ?????????:" + mRoomData.getRealRoundInfo().getUserID());
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param score
     * @param line
     */
    public void sendScoreToServer(int score, int line) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("gameID", mRoomData.getGameId());
        RankRoundInfoModel infoModel = RoomDataUtils.getRoundInfoByUserId(mRoomData, (int) MyUserInfoManager.getInstance().getUid());
        if (infoModel == null) {
            MyLog.d(TAG, "sendScoreToServer ????????????????????????");
            return;
        }
        int itemID = infoModel.getPlaybookID();
        map.put("itemID", itemID);
        int mainLevel = 0;
        PlayerInfoModel playerInfoModel = RoomDataUtils.getPlayerInfoById(mRoomData, (int) MyUserInfoManager.getInstance().getUid());
        if (playerInfoModel != null) {
            mainLevel = playerInfoModel.getUserInfo().getRanking().getMainRanking();
        }
        map.put("mainLevel", mainLevel);
        map.put("no", line);
        int roundSeq = infoModel.getRoundSeq();
        map.put("roundSeq", roundSeq);
        map.put("score", score);
        long nowTs = System.currentTimeMillis();
        int singSecond = (int) ((nowTs - mRoomData.getSingBeginTs()) / 1000);
        map.put("singSecond", singSecond);
        map.put("timeMs", nowTs);
        map.put("userID", MyUserInfoManager.getInstance().getUid());
        StringBuilder sb = new StringBuilder();
        sb.append("skrer")
                .append("|").append(MyUserInfoManager.getInstance().getUid())
                .append("|").append(itemID)
                .append("|").append(score)
                .append("|").append(line)
                .append("|").append(mRoomData.getGameId())
                .append("|").append(mainLevel)
                .append("|").append(singSecond)
                .append("|").append(roundSeq)
                .append("|").append(nowTs);
        map.put("sign", U.getMD5Utils().MD5_32(sb.toString()));
        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        ApiMethods.subscribe(mRoomServerApi.sendPkPerSegmentResult(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    // TODO: 2018/12/13  ??????postman??????????????? ?????????
                    MyLog.w(TAG, "????????????????????????");
                } else {
                    MyLog.w(TAG, "????????????????????????" + result.getErrno());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.e(e);
            }
        }, this);
    }
}
