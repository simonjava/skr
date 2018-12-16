package com.module.rankingmode.room.presenter;

import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.common.core.account.UserAccountManager;
import com.common.log.MyLog;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.utils.HandlerTaskTimer;
import com.common.utils.SongResUtils;
import com.common.utils.U;
import com.engine.EngineEvent;
import com.engine.EngineManager;
import com.engine.Params;
import com.module.rankingmode.msg.event.AppSwapEvent;
import com.module.rankingmode.msg.event.QuitGameEvent;
import com.module.rankingmode.msg.event.RoundAndGameOverEvent;
import com.module.rankingmode.msg.event.RoundOverEvent;
import com.module.rankingmode.msg.event.SyncStatusEvent;
import com.module.rankingmode.prepare.model.OnLineInfoModel;
import com.module.rankingmode.prepare.model.RoundInfoModel;
import com.module.rankingmode.room.RoomServerApi;
import com.module.rankingmode.room.event.RoundInfoChangeEvent;
import com.module.rankingmode.room.model.RoomData;
import com.module.rankingmode.room.model.RoomDataUtils;
import com.module.rankingmode.room.view.IGameRuleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RankingCorePresenter extends RxLifeCyclePresenter {
    String TAG = "RankingCorePresenter";
    private static long heartBeatTaskInterval = 2000;
    private static long checkStateTaskDelay = 10000;
    private static long syncStateTaskInterval = 12000;

    RoomData mRoomData;

    RoomServerApi mRoomServerApi = ApiManager.getInstance().createService(RoomServerApi.class);

    HandlerTaskTimer mHeartBeatTask;

    HandlerTaskTimer mSyncGameStateTask;

    IGameRuleView mIGameRuleView;

    Handler mUiHanlder = new Handler();

    public RankingCorePresenter(@NotNull IGameRuleView iGameRuleView, @NotNull RoomData roomData) {
        TAG = "RankingCorePresenter_" + mRoomData.getGameId();
        mIGameRuleView = iGameRuleView;
        mRoomData = roomData;
        Params params = Params.getFromPref();
        EngineManager.getInstance().init(params);
        EngineManager.getInstance().joinRoom(String.valueOf(mRoomData.getGameId()), (int) UserAccountManager.getInstance().getUuidAsLong(), true);
        // 不发送本地音频
        EngineManager.getInstance().muteLocalAudioStream(true);
    }

    @Override
    public void start() {
        super.start();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mRoomData.checkRound();
        startSyncGameStateTask(3000);
    }

    @Override
    public void destroy() {
        super.destroy();
        cancelHeartBeatTask();
        cancelSyncGameStateTask();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        EngineManager.getInstance().destroy();
        mUiHanlder.removeCallbacksAndMessages(null);
    }

    /**
     * 上报轮次结束信息
     */
    public void sendRoundOverInfo() {
        MyLog.d(TAG, "上报我的演唱结束");
        HashMap<String, Object> map = new HashMap<>();
        map.put("gameID", mRoomData.getGameId());

        // 提前获取roundSeq，如果在result里在获取，可能是下下一个了，如果提前收到轮次变化的push
        int roundSeq = -1;
        if (mRoomData.getRealRoundInfo() != null && mRoomData.getRealRoundInfo().getUserID() == UserAccountManager.getInstance().getUuidAsLong()) {
            roundSeq = mRoomData.getRealRoundInfo().getRoundSeq();
        }
        int finalRoundSeq = roundSeq;

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSOIN), JSON.toJSONString(map));
        ApiMethods.subscribe(mRoomServerApi.sendRoundOver(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    MyLog.d(TAG, "我的演唱结束上报成功");
                    // 尝试自己切换到下一个轮次
                    if (finalRoundSeq >= 0) {
                        RoundInfoModel roundInfoModel = RoomDataUtils.findRoundInfoBySeq(mRoomData.getRoundInfoModelList(), finalRoundSeq + 1);
                        mRoomData.setExpectRoundInfo(roundInfoModel);
                        mRoomData.checkRound();
                    }
                } else {
                    MyLog.d(TAG, "sendRoundOverInfo" + " result=" + result);
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.d(TAG, "sendRoundOverInfo" + " error " + e);
            }
        }, this);
    }

    /**
     * 退出游戏
     */
    public void exitGame(int gameID) {
        MyLog.d(TAG, "exitGame" + " gameID=" + gameID);
        HashMap<String, Object> map = new HashMap<>();
        map.put("gameID", gameID);

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSOIN), JSON.toJSONString(map));
        ApiMethods.subscribe(mRoomServerApi.exitGame(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    U.getToastUtil().showShort("退出游戏成功");
                } else {
                    MyLog.e(TAG, "exitGame result errno is " + result.getErrmsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.e(TAG, "exitGame error " + e);
            }
        }, this);
    }

    /**
     * 心跳相关
     */
    public void startHeartBeatTask() {
        cancelHeartBeatTask();
        mHeartBeatTask = HandlerTaskTimer.newBuilder()
                .interval(heartBeatTaskInterval)
                .take(-1)
                .start(new HandlerTaskTimer.ObserverW() {
                    @Override
                    public void onNext(Integer integer) {
                        sendHeartBeat();
                    }
                });
    }

    public void cancelHeartBeatTask() {
        if (mHeartBeatTask != null) {
            mHeartBeatTask.dispose();
        }
    }

    // 上报心跳，只有当前演唱者上报 2s一次
    public void sendHeartBeat() {
        MyLog.d(TAG, "sendHeartBeat" + " gameID=" + mRoomData.getGameId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("gameID", mRoomData.getGameId());
        map.put("userID", UserAccountManager.getInstance().getUuid());

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSOIN), JSON.toJSONString(map));
        ApiMethods.subscribe(mRoomServerApi.sendHeartBeat(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    // TODO: 2018/12/13  当前postman返回的为空 待补充
                } else {
                    MyLog.e(TAG, "sendHeartBeat " + result.getErrmsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.e(TAG, "sendHeartBeat errer " + e);
            }
        }, this);
    }

    /**
     * 轮询同步状态task
     */
    public void startSyncGameStateTask(long delayTime) {
        MyLog.d(TAG, "startSyncGameStateTask start time = " + System.currentTimeMillis());
        cancelSyncGameStateTask();
        mSyncGameStateTask = HandlerTaskTimer.newBuilder()
                .delay(delayTime)
                .interval(syncStateTaskInterval)
                .take(-1)
                .start(new HandlerTaskTimer.ObserverW() {
                    @Override
                    public void onNext(Integer integer) {
                        MyLog.d(TAG, "startSyncGameStateTask" + " integer=" + integer + " exec time = " + System.currentTimeMillis());
                        syncGameStatus(mRoomData.getGameId());
                    }
                });
    }

    public void cancelSyncGameStateTask() {
        if (mSyncGameStateTask != null) {
            mSyncGameStateTask.dispose();
        }
    }


    // 同步游戏详情状态(检测不到长连接调用)
    public void syncGameStatus(int gameID) {
        ApiMethods.subscribe(mRoomServerApi.syncGameStatus(gameID), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    long syncStatusTimes = result.getData().getLong("syncStatusTimeMs");  //状态同步时的毫秒时间戳
                    long gameOverTimeMs = result.getData().getLong("gameOverTimeMs");  //游戏结束时间
                    List<OnLineInfoModel> onlineInfos = JSON.parseArray(result.getData().getString("onlineInfo"), OnLineInfoModel.class); //在线状态
                    RoundInfoModel currentInfo = JSON.parseObject(result.getData().getString("currentRound"), RoundInfoModel.class); //当前轮次信息
                    RoundInfoModel nextInfo = JSON.parseObject(result.getData().getString("nextRound"), RoundInfoModel.class); //下个轮次信息
                    updatePlayerState(gameOverTimeMs, syncStatusTimes, onlineInfos, currentInfo, nextInfo);
                } else {
                    MyLog.e(TAG, "syncGameStatus " + result.getErrmsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.e(TAG, "syncGameStatus error " + e);
            }
        }, this);
    }

    /**
     * 根据时间戳更新选手状态,目前就只有两个入口，SyncStatusEvent push了sycn，不写更多入口
     */
    private synchronized void updatePlayerState(long gameOverTimeMs, long syncStatusTimes, List<OnLineInfoModel> onlineInfos, RoundInfoModel currentInfo, RoundInfoModel nextInfo) {
        MyLog.d(TAG, "updatePlayerState" + " gameOverTimeMs=" + gameOverTimeMs + " syncStatusTimes=" + syncStatusTimes + " onlineInfos=" + onlineInfos + " currentInfo=" + currentInfo + " nextInfo=" + nextInfo);
        if (syncStatusTimes > mRoomData.getLastSyncTs()) {
            mRoomData.setLastSyncTs(syncStatusTimes);
            mRoomData.setOnlineInfoList(onlineInfos);
            mIGameRuleView.updateUserState(onlineInfos);
        }
        if (gameOverTimeMs > mRoomData.getGameStartTs()) {
            // 游戏结束了
            onGameOver(gameOverTimeMs);
        }
        // 服务下发的轮次已经大于当前轮次了，说明本地信息已经不对了，更新
        if (RoomDataUtils.roundSeqLarger(currentInfo, mRoomData.getExpectRoundInfo())) {
            MyLog.d(TAG, "updatePlayerState" + " sync发现本地轮次信息滞后，更新");
            // 轮次确实比当前的高，可以切换
            mRoomData.setExpectRoundInfo(currentInfo);
            mRoomData.checkRound();
        }
    }


    /**
     * 轮次信息有更新
     * 核心事件
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(RoundInfoChangeEvent event) {
        MyLog.d(TAG, "RoundInfoChangeEvent  轮次变更事件发生 " + " myturn=" + event.myturn);
        if (event.myturn) {
            // 轮到我唱了
            // 开始发心跳
            startHeartBeatTask();
            // 开始倒计时 3 2 1
            mIGameRuleView.startSelfCountdown(new Runnable() {
                @Override
                public void run() {
                    // 开始开始混伴奏，开始解除引擎mute
                    File accFile = SongResUtils.getAccFileByUrl(mRoomData.getSongModel().getAcc());
                    if (accFile != null && accFile.exists()) {
                        EngineManager.getInstance().muteLocalAudioStream(false);
                        EngineManager.getInstance().startAudioMixing(accFile.getAbsolutePath(), false, false, 1);
                        EngineManager.getInstance().setAudioMixingPosition(mRoomData.getSongModel().getBeginMs());
                        // 还应开始播放歌词
                        mIGameRuleView.playLyric(mRoomData.getSongModel().getItemID());
                    }
                }
            });
        } else {
            MyLog.d(TAG, "不是我的轮次，停止发心跳，停止混音，闭麦");
            cancelHeartBeatTask();
            EngineManager.getInstance().stopAudioMixing();
            EngineManager.getInstance().muteLocalAudioStream(true);
            // 收到其他的人onMute消息 开始播放其他人的歌的歌词，应该提前下载好
            if (mRoomData.getRealRoundInfo() != null) {
                // 其他人演唱
                mUiHanlder.post(new Runnable() {
                    @Override
                    public void run() {
                        int uid = RoomDataUtils.getUidOfRoundInfo(mRoomData.getRealRoundInfo());
                        mIGameRuleView.startRivalCountdown(uid);
                    }
                });
            } else if (mRoomData.getRealRoundInfo() == null) {
                if (mRoomData.getGameOverTs() > mRoomData.getGameStartTs()) {
                    // 游戏结束了,处理相应的ui逻辑
                    mUiHanlder.post(new Runnable() {
                        @Override
                        public void run() {
                            mIGameRuleView.gameFinish();
                        }
                    });
                } else {
                    MyLog.d(TAG, "结束时间比开始时间小，不应该吧");
                }
            }
        }
    }

    private void onGameOver(long gameOverTs) {
        MyLog.d(TAG, "游戏结束" + " gameOverTs=" + gameOverTs);
        if (gameOverTs > mRoomData.getGameStartTs() && mRoomData.getGameOverTs() == 0) {
            mRoomData.setGameOverTs(gameOverTs);
            mRoomData.setExpectRoundInfo(null);
            mRoomData.checkRound();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(EngineEvent event) {
        if (event.getType() == EngineEvent.TYPE_MUSIC_PLAY_TIME_FLY_LISTENER) {
            EngineEvent.MixMusicTimeInfo timeInfo = (EngineEvent.MixMusicTimeInfo) event.getObj();
            if (timeInfo.getCurrent() >= mRoomData.getSongModel().getEndMs()) {
                //可以发结束轮次的通知了
                sendRoundOverInfo();
            }
        } else if (event.getType() == EngineEvent.TYPE_USER_MUTE_AUDIO) {
            int muteUserId = event.getUserStatus().getUserId();
            RoundInfoModel infoModel = RoomDataUtils.getRoundInfoByUserId(mRoomData.getRoundInfoModelList(), muteUserId);
            if (!event.getUserStatus().isAudioMute()) {
                MyLog.w(TAG, "EngineEvent muteUserId=" + muteUserId + "解麦了");
                /**
                 * 用户开始解开mute了，说明某个用户自己认为轮到自己唱了
                 * 这里考虑下要不要加个判断，如果当前轮次是这个用户，才播放他的歌词
                 * 就是是自己状态对，还是别人状态对的问题，这里先认为自己状态对.
                 * 状态依赖服务器
                 */
                if (infoModel != null) {
                    if (RoomDataUtils.roundInfoEqual(infoModel, mRoomData.getRealRoundInfo())) {
                        //正好相等，没问题,放歌词
                        MyLog.w(TAG, "是当前轮次，没问题,放歌词");
                        mUiHanlder.post(new Runnable() {
                            @Override
                            public void run() {
                                mIGameRuleView.playLyric(infoModel.getPlaybookID());
                            }
                        });
                    } else if (RoomDataUtils.roundSeqLarger(infoModel, mRoomData.getExpectRoundInfo())) {
                        // 假设演唱的轮次在当前轮次后面，说明本地滞后了
                        MyLog.w(TAG, "演唱的轮次在当前轮次后面，说明本地滞后了,矫正并放歌词");
                        mRoomData.setExpectRoundInfo(infoModel);
                        mRoomData.checkRound();
                        mUiHanlder.post(new Runnable() {
                            @Override
                            public void run() {
                                mIGameRuleView.playLyric(infoModel.getPlaybookID());
                            }
                        });
                    }
                }else{
                    MyLog.w(TAG, "找不到该人的轮次信息？？？为什么？？？");
                }
            } else {
                /**
                 * 有人闭麦了，可以考虑加个逻辑，如果闭麦的人是当前演唱的人
                 * 说明此人演唱结束，可以考虑进入下一轮
                 */
                MyLog.w(TAG, "EngineEvent muteUserId=" + muteUserId + "闭麦了");
            }
        }
    }

    // 游戏轮次结束的通知消息（在某人向服务器短连接成功后推送)
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventMainThread(RoundOverEvent roundOverEvent) {
        MyLog.d(TAG, "onRoundOverEvent 轮次结束push通知:" + roundOverEvent.currenRound + " nextRound:" + roundOverEvent.nextRound);
        if (RoomDataUtils.roundInfoEqual(roundOverEvent.currenRound, mRoomData.getRealRoundInfo())) {
            // 确实等于当前轮次
            if (mRoomData.getRealRoundInfo() != null) {
                mRoomData.getRealRoundInfo().setEndTs(roundOverEvent.roundOverTimeMs);
            }
        }
        // 游戏轮次结束
        if (RoomDataUtils.roundSeqLarger(roundOverEvent.nextRound, mRoomData.getExpectRoundInfo())) {
            // 轮次确实比当前的高，可以切换
            mRoomData.setExpectRoundInfo(roundOverEvent.nextRound);
            mRoomData.checkRound();
        }
    }

    //轮次和游戏结束通知，除了已经结束状态，别的任何状态都要变成
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventMainThread(RoundAndGameOverEvent roundAndGameOverEvent) {
        MyLog.d(TAG, "onEventMainThread 游戏结束push通知");
        onGameOver(roundAndGameOverEvent.roundOverTimeMs);
    }

    // 退出游戏的通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(QuitGameEvent quitGameEvent) {
        MyLog.d(TAG, "onEventMainThread syncStatusEvent");
        U.getToastUtil().showShort("某一个人退出了");
    }

    // 应用进程切到后台通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AppSwapEvent appSwapEvent) {
        MyLog.d(TAG, "onEventMainThread syncStatusEvent");
    }

    // 长连接 状态同步信令， 以11秒为单位检测
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SyncStatusEvent syncStatusEvent) {
        MyLog.d(TAG, "onEventMainThread receive syncStatusEvent");
        //从新开始12秒后拉取
        startSyncGameStateTask(checkStateTaskDelay);
        updatePlayerState(syncStatusEvent.gameOverTimeMs, syncStatusEvent.syncStatusTimes, syncStatusEvent.onlineInfos, syncStatusEvent.currentInfo, syncStatusEvent.nextInfo);
    }
}
