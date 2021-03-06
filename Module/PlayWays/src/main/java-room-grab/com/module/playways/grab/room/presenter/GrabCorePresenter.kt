package com.module.playways.grab.room.presenter

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.alibaba.fastjson.JSON
import com.common.base.BaseActivity
import com.common.core.account.UserAccountManager
import com.common.core.myinfo.MyUserInfoManager
import com.common.core.userinfo.UserInfoManager
import com.common.core.userinfo.model.UserInfoModel
import com.common.engine.ScoreConfig
import com.common.jiguang.JiGuangPush
import com.common.log.DebugLogView
import com.common.log.MyLog
import com.common.mvp.RxLifeCyclePresenter
import com.common.player.AndroidMediaPlayer
import com.common.player.ExoPlayer
import com.common.player.IPlayer
import com.common.player.PlayerCallbackAdapter
import com.common.rxretrofit.*
import com.common.statistics.StatisticsAdapter
import com.common.upload.UploadCallback
import com.common.upload.UploadParams
import com.common.utils.ActivityUtils
import com.common.utils.HandlerTaskTimer
import com.common.utils.SpanUtils
import com.common.utils.U
import com.common.videocache.MediaCacheManager
import com.common.view.AnimateClickListener
import com.component.busilib.constans.GameModeType
import com.component.busilib.constans.GrabRoomType
import com.component.busilib.recommend.RA
import com.component.lyrics.LyricAndAccMatchManager
import com.component.lyrics.utils.SongResUtils
import com.component.lyrics.utils.ZipUrlResourceManager
import com.dialog.view.TipsDialogView
import com.engine.EngineEvent
import com.engine.Params
import com.engine.arccloud.RecognizeConfig
import com.module.ModuleServiceManager
import com.module.common.ICallback
import com.module.msg.CustomMsgType
import com.module.playways.BuildConfig
import com.module.playways.RoomDataUtils
import com.module.playways.event.GrabChangeRoomEvent
import com.module.playways.grab.room.GrabResultData
import com.module.playways.grab.room.GrabRoomData
import com.module.playways.grab.room.GrabRoomServerApi
import com.module.playways.grab.room.event.*
import com.module.playways.grab.room.inter.IGrabRoomView
import com.module.playways.grab.room.model.*
import com.module.playways.pretendHeadSetSystemMsg
import com.module.playways.race.room.model.LevelResultModel
import com.module.playways.room.gift.event.GiftBrushMsgEvent
import com.module.playways.room.gift.event.ShowHalfRechargeFragmentEvent
import com.module.playways.room.gift.event.UpdateCoinEvent
import com.module.playways.room.gift.event.UpdateMeiliEvent
import com.module.playways.room.msg.event.*
import com.module.playways.room.msg.filter.PushMsgFilter
import com.module.playways.room.msg.manager.GrabRoomMsgManager
import com.module.playways.room.prepare.model.JoinGrabRoomRspModel
import com.module.playways.room.room.SwapStatusType
import com.module.playways.room.room.comment.model.CommentLightModel
import com.module.playways.room.room.comment.model.CommentModel
import com.module.playways.room.room.comment.model.CommentSysModel
import com.module.playways.room.room.comment.model.CommentTextModel
import com.module.playways.room.room.event.PretendCommentMsgEvent
import com.module.playways.room.room.score.MachineScoreItem
import com.module.playways.room.room.score.RobotScoreHelper
import com.module.playways.room.song.model.SongModel
import com.module.playways.songmanager.event.MuteAllVoiceEvent
import com.module.playways.songmanager.event.RoomNameChangeEvent
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import com.zq.live.proto.Common.ESex
import com.zq.live.proto.Common.StandPlayType
import com.zq.live.proto.Common.UserInfo
import com.zq.live.proto.GrabRoom.*
import com.zq.mediaengine.kit.ZqEngineKit
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.greendao.annotation.NotNull
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class GrabCorePresenter(@param:NotNull internal var mIGrabView: IGrabRoomView, @param:NotNull internal var mRoomData: GrabRoomData, internal var mBaseActivity: BaseActivity) : RxLifeCyclePresenter() {

    internal var mFirstKickOutTime: Long = -1 //???????????????????????????????????????????????????????????????

    internal var mAbsenTimes = 0

    internal var mRoomServerApi = ApiManager.getInstance().createService(GrabRoomServerApi::class.java)

    internal var mSyncGameStateTask: HandlerTaskTimer? = null

    internal var mRobotScoreHelper: RobotScoreHelper? = null

    internal var mDestroyed = false

    internal var mExoPlayer: IPlayer? = null

    internal var mSwitchRooming = false

    internal var mGrabRedPkgPresenter: GrabRedPkgPresenter? = null

    internal var mZipUrlResourceManager: ZipUrlResourceManager? = null

    internal var mEngineParamsTemp: EngineParamsTemp? = null

    internal var mDialogPlus: DialogPlus? = null

    internal var mAccUrl: String = ""

    internal var mUiHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_ENSURE_IN_RC_ROOM -> {
                    MyLog.d(TAG, "handleMessage ??????????????????push?????????????????????????????????")
                    ModuleServiceManager.getInstance().msgService.leaveChatRoom(mRoomData.gameId.toString() + "")
                    joinRcRoom(0)
                    ensureInRcRoom()
                }
                MSG_ROBOT_SING_BEGIN -> robotSingBegin()
                MSG_ENSURE_SWITCH_BROADCAST_SUCCESS -> onChangeBroadcastSuccess()
                MSG_RECOVER_VOLUME -> {
                    if (mEngineParamsTemp != null) {
                        ZqEngineKit.getInstance().adjustAudioMixingPlayoutVolume(mEngineParamsTemp!!.audioVolume, false)
                        ZqEngineKit.getInstance().adjustRecordingSignalVolume(mEngineParamsTemp!!.recordVolume, false)

                        if (ZqEngineKit.getInstance().params.isAnchor) {
                            val audioVolume = ZqEngineKit.getInstance().params.audioMixingPlayoutVolume
                            val recordVolume = ZqEngineKit.getInstance().params.recordingSignalVolume
                            ZqEngineKit.getInstance().adjustAudioMixingPlayoutVolume(audioVolume, false)
                            ZqEngineKit.getInstance().adjustRecordingSignalVolume(recordVolume, false)
                        } else {
                            MyLog.d(TAG, "????????????????????????")
                        }
                        mEngineParamsTemp = null
                    }
                    if (mExoPlayer != null) {
                        val valueAnimator = ValueAnimator.ofFloat(0f, mExoPlayer!!.volume)
                        valueAnimator.addUpdateListener { animation ->
                            val v = animation.animatedValue as Float
                            if (mExoPlayer != null) {
                                mExoPlayer!!.setVolume(v, false)
                            }
                        }
                        valueAnimator.duration = 1000
                        valueAnimator.start()
                    }
                }
                MSG_ENSURE_EXIT -> if (mIGrabView != null) {
                    mIGrabView.onGetGameResult(false)
                }
            }
        }
    }

    internal var mPushMsgFilter: PushMsgFilter<*> = PushMsgFilter<RoomMsg> { msg ->
        msg != null && msg.roomID == mRoomData.gameId
    }

    init {
        GrabRoomMsgManager.getInstance().addFilter(mPushMsgFilter)
        joinRoomAndInit(true)
        U.getFileUtils().deleteAllFiles(U.getAppInfoUtils().getSubDirPath("grab_save"))
        startSyncGameStateTask(sSyncStateTaskInterval * 2)
    }

    fun setGrabRedPkgPresenter(grabRedPkgPresenter: GrabRedPkgPresenter) {
        mGrabRedPkgPresenter = grabRedPkgPresenter
    }

    /**
     * ??????????????????
     * ??????????????????
     * ??????????????????
     */
    private fun joinRoomAndInit(first: Boolean) {
        MyLog.w(TAG, "joinRoomAndInit" + " first=" + first + ", gameId is " + mRoomData.gameId)
        mFirstKickOutTime = -1
        mAbsenTimes = 0

        if (mRoomData.gameId > 0) {
            var reInit = false
            if (first) {
                reInit = true
            }
            if (!reInit && ZqEngineKit.getInstance().params.isEnableVideo != mRoomData.isVideoRoom) {
                MyLog.d(TAG, "???????????????????????????")
                mIGrabView.changeRoomMode(mRoomData.isVideoRoom)
                // ????????????
                reInit = true
            }
            if (reInit) {
                val params = Params.getFromPref()
                //            params.setStyleEnum(Params.AudioEffect.none);
                params.scene = Params.Scene.grab
                params.isEnableAudio = true
                params.isEnableVideo = mRoomData.isVideoRoom
                ZqEngineKit.getInstance().init("grabroom", params)
            }
            ZqEngineKit.getInstance().joinRoom(mRoomData.gameId.toString(), UserAccountManager.uuidAsLong.toInt(), false, mRoomData.agoraToken)
            // ?????????????????????, ??????????????????????????????
            ZqEngineKit.getInstance().muteLocalAudioStream(true)
            if (mRoomData.isVideoRoom) {
                ZqEngineKit.getInstance().unbindAllRemoteVideo()
            }
        }
        joinRcRoom(-1)
        if (mRoomData.gameId > 0) {
            for (playerInfoModel in mRoomData.getPlayerAndWaiterInfoList()) {
                if (!playerInfoModel.isOnline) {
                    continue
                }
                pretendEnterRoom(playerInfoModel)
            }
            if (mRoomData.roomType == GrabRoomType.ROOM_TYPE_PLAYBOOK) {
                pretendRoomNameSystemMsg(mRoomData.roomName, CommentSysModel.TYPE_ENTER_ROOM_PLAYBOOK)
            } else {
                pretendRoomNameSystemMsg(mRoomData.roomName, CommentSysModel.TYPE_ENTER_ROOM)
            }
            pretendHeadSetSystemMsg(mRoomData.gameType)
        }
        if (mRoomData.hasGameBegin()) {
            startSyncGameStateTask(sSyncStateTaskInterval)
        } else {
            cancelSyncGameStateTask()
        }
    }

    private fun joinRcRoom(deep: Int) {
        if (deep > 4) {
            MyLog.d(TAG, "???????????????????????????5????????????????????????")
            return
        }
        if (mRoomData.gameId > 0) {
            ModuleServiceManager.getInstance().msgService.joinChatRoom(mRoomData.gameId.toString(), -1, object : ICallback {
                override fun onSucess(obj: Any?) {
                    MyLog.d(TAG, "????????????????????????")
                }

                override fun onFailed(obj: Any?, errcode: Int, message: String?) {
                    MyLog.d(TAG, "??????????????????????????? msg is $message, errcode is $errcode")
                    joinRcRoom(deep + 1)
                }
            })
            if (deep == -1) {
                /**
                 * ???????????????????????????????????????????????????????????????????????????????????????tag?????????
                 */
                JiGuangPush.joinSkrRoomId(mRoomData.gameId.toString())
            }
        }
    }

    private fun ensureInRcRoom() {
        mUiHandler.removeMessages(MSG_ENSURE_IN_RC_ROOM)
        mUiHandler.sendEmptyMessageDelayed(MSG_ENSURE_IN_RC_ROOM, (30 * 1000).toLong())
    }

    private fun pretendSystemMsg(text: String) {
        val commentSysModel = CommentSysModel(mRoomData.gameType, text)
        EventBus.getDefault().post(PretendCommentMsgEvent(commentSysModel))
    }

    private fun pretendRoomNameSystemMsg(roomName: String?, type: Int) {
        val commentSysModel = CommentSysModel(roomName ?: "", type)
        EventBus.getDefault().post(PretendCommentMsgEvent(commentSysModel))
    }

//    private fun pretendHeadSetSystemMsg() {
//        val stringBuilder = SpanUtils()
//                .append(" ??????????????????????????????????????????????????????").setForegroundColor(CommentModel.RANK_SYSTEM_COLOR)
//                .create()
//        val commentSysModel = CommentSysModel(mRoomData.gameType, stringBuilder)
//        EventBus.getDefault().post(PretendCommentMsgEvent(commentSysModel))
//    }

    override fun start() {
        super.start()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    /**
     * ???ui?????????
     * ??????????????????
     */
    fun onOpeningAnimationOver() {
        // ??????????????????????????????
        if (mRoomData.hasGameBegin()) {
            mRoomData.checkRoundInEachMode()
            ensureInRcRoom()
            userStatisticForIntimacy()
        } else {
            MyLog.d(TAG, "onOpeningAnimationOver ???????????????")
        }
    }

    /**
     * ????????????
     */
    fun playGuide() {
        if (mDestroyed) {
            return
        }
        val now = mRoomData.realRoundInfo
        if (now != null) {
            if (mExoPlayer == null) {
                mExoPlayer = AndroidMediaPlayer()
                if (mRoomData.isMute || !U.getActivityUtils().isAppForeground) {
                    mExoPlayer!!.volume = 0f
                } else {
                    mExoPlayer!!.volume = 1f
                }
            }
            mExoPlayer?.setCallback(object : PlayerCallbackAdapter() {
                override fun onPrepared() {
                    super.onPrepared()
                    if (!now.isParticipant && now.enterStatus == EQRoundStatus.QRS_INTRO.value) {
                        MyLog.d(TAG, "??????????????????????????????seek")
                        mExoPlayer!!.seekTo(now.elapsedTimeMs.toLong())
                    }
                }
            })
            mExoPlayer?.startPlay(now.music.standIntro)
        }
    }

    /**
     * ??????????????????
     */
    fun stopGuide() {
        mExoPlayer?.stop()
    }

    /**
     * ???????????????????????????,????????????????????????
     */
    internal fun preOpWhenSelfRound() {
        var needAcc = false
        var needScore = false
        val now = mRoomData.realRoundInfo
        var songModel: SongModel? = null
        if (now != null) {
            songModel = now.music
            if (now.status == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.value) {
                songModel = songModel!!.pkMusic
            }
            val p = ZqEngineKit.getInstance().params
            if (p != null) {
                p.isGrabSingNoAcc = false
            }
            if (now.wantSingType == EWantSingType.EWST_SPK.value) {
                needAcc = true
                needScore = true
            } else if (now.wantSingType == EWantSingType.EWST_CHORUS.value) {
                needAcc = false
                needScore = false
            } else if (now.wantSingType == EWantSingType.EWST_MIN_GAME.value) {
                needAcc = false
                needScore = false
            } else if (mRoomData.isAccEnable && songModel != null && !TextUtils.isEmpty(songModel.acc)) {
                needAcc = true
                needScore = true
            } else {
                if (p != null) {
                    p.isGrabSingNoAcc = true
                    needScore = true
                }
            }
        }
        if (needAcc) {
            // 1. ?????????????????????????????? melp ??????
            if (songModel != null) {
                val midiFile = SongResUtils.getMIDIFileByUrl(songModel.midi)
                if (midiFile != null && !midiFile.exists()) {
                    U.getHttpUtils().downloadFileAsync(now!!.music.midi, midiFile, true, null)
                }
            }
        }
        if (!ZqEngineKit.getInstance().params.isAnchor) {
            ZqEngineKit.getInstance().setClientRole(true)
            ZqEngineKit.getInstance().muteLocalAudioStream(false)
            if (needAcc) {
                // ??????????????????????????????????????????????????????????????????
                mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS)
                mUiHandler.sendEmptyMessageDelayed(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS, 2000)
            }
        } else {
            // ???????????????,???????????? ???????????????????????????????????? ?????????????????????
            mUiHandler.postDelayed({
                ZqEngineKit.getInstance().muteLocalAudioStream(false)
                onChangeBroadcastSuccess()
            }, 500)
        }

        // ??????acr??????
        if (ScoreConfig.isAcrEnable() && now != null && now.music != null) {
            if (needAcc) {
                ZqEngineKit.getInstance().startRecognize(RecognizeConfig.newBuilder()
                        .setSongName(now.music.itemName)
                        .setArtist(now.music.owner)
                        .setMode(RecognizeConfig.MODE_MANUAL)
                        .build())
            } else {
                if (needScore) {
                    // ???????????????????????????????????? acr ??????
                    ZqEngineKit.getInstance().startRecognize(RecognizeConfig.newBuilder()
                            .setSongName(now.music.itemName)
                            .setArtist(now.music.owner)
                            .setMode(RecognizeConfig.MODE_AUTO)
                            .setAutoTimes(3)
                            .setMResultListener { result, list, targetSongInfo, lineNo ->
                                var mAcrScore = 0
                                if (targetSongInfo != null) {
                                    mAcrScore = (targetSongInfo.score * 100).toInt()
                                }
                                EventBus.getDefault().post(LyricAndAccMatchManager.ScoreResultEvent("preOpWhenSelfRound", -1, mAcrScore, 0))
                            }
                            .build())
                } else {

                }
            }
        }
    }

    private fun preOpWhenOtherRound(uid: Int) {
        val playerInfo = RoomDataUtils.getPlayerInfoById(mRoomData, uid)
        if (playerInfo == null) {
            MyLog.w(TAG, "?????????????????????PlayerInfo??????")
            return
        }
        /**
         * ?????????
         */
        if (playerInfo.isSkrer) {
            MyLog.d(TAG, "checkMachineUser uid=$uid is machine")
            //????????????????????????
            //??????????????????????????????????????????
            mUiHandler.removeMessages(MSG_ROBOT_SING_BEGIN)
            val message = mUiHandler.obtainMessage(MSG_ROBOT_SING_BEGIN)
            mUiHandler.sendMessage(message)
        }

        // ???????????????
        //        if (mRoomData.isVideoRoom()) {
        //            // ?????????????????????
        //            GrabRoundInfoModel infoModel = mRoomData.getRealRoundInfo();
        //            if (infoModel != null) {
        //                if (infoModel.isPKRound()) {
        //                    if (infoModel.getsPkRoundInfoModels().size() >= 2) {
        //                        int userId1 = infoModel.getsPkRoundInfoModels().get(0).getUserID();
        //                        int userId2 = infoModel.getsPkRoundInfoModels().get(1).getUserID();
        //                        if (MyUserInfoManager.getInstance().getUid() == userId1 ||
        //                                MyUserInfoManager.getInstance().getUid() == userId2) {
        //                            // ??????????????????????????? ?????????????????????
        //                            //join?????????????????????
        //                            if (!ZqEngineKit.getInstance().getParams().isAnchor()) {
        //                                ZqEngineKit.getInstance().setClientRole(true);
        //                            }
        //                            // ?????????
        //                            ZqEngineKit.getInstance().muteLocalAudioStream(true);
        //                        }
        //                    }
        //                }
        //            }
        //        }
    }

    var savePath = ""
    /**
     * ??????????????????????????????
     */
    fun beginSing() {
        // ???????????????????????????
        // ????????????????????????????????????
        if (mRoomData.realRoundInfo?.isNormalRound == true) {
            /**
             * ??????????????????
             */
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd_HH_mm_ss")
            val fileName = String.format(PERSON_LABEL_SAVE_PATH_FROMAT, simpleDateFormat.format(Date(System.currentTimeMillis())), mRoomData.gameId, mRoomData.realRoundInfo?.roundSeq)
            savePath = U.getAppInfoUtils().getFilePathInSubDir(ZqEngineKit.AUDIO_FEEDBACK_DIR, fileName)
            ZqEngineKit.getInstance().startAudioRecording(savePath, false)
        }
    }

    /**
     * ????????????????????????
     */
    fun ownerBeginGame() {
        MyLog.d(TAG, "ownerBeginGame")
        val map = HashMap<String, Any>()
        map["roomID"] = mRoomData.gameId

        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        ApiMethods.subscribe(mRoomServerApi.ownerBeginGame(body), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult) {
                if (result.errno == 0) {
                    val rsp = JSON.parseObject(result.data!!.toJSONString(), JoinGrabRoomRspModel::class.java)
                    // ???????????????push?????????????????????
                    val event = QGameBeginEvent()
                    event.roomID = rsp.roomID
                    event.mGrabConfigModel = rsp.config
                    event.mInfoModel = rsp.currentRound
                    onEvent(event)
                }
            }

            override fun onError(e: Throwable) {

            }
        }, this, RequestControl("ownerBeginGame", ControlType.CancelThis))
    }

    /**
     * ????????????
     */
    fun grabThisRound(seq: Int, challenge: Boolean) {
        MyLog.d(TAG, "grabThisRound" + " seq=" + seq + " challenge=" + challenge + " accenable=" + mRoomData.isAccEnable)


        val infoModel = mRoomData.realRoundInfo
        if (infoModel != null) {
            if (infoModel.wantSingInfos.contains(WantSingerInfo(MyUserInfoManager.uid.toInt()))) {
                MyLog.w(TAG, "grabThisRound cancel ??????????????????????????????")
                return
            }
        }

        val map = HashMap<String, Any>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = seq


        var songModel: SongModel? = null
        if (infoModel != null && infoModel.music != null) {
            songModel = infoModel.music
        }

        var preAccUrl = ""

        val wantSingType: Int
        // ??????????????????????????????
        if (songModel != null && songModel.playType == StandPlayType.PT_SPK_TYPE.value) {
            wantSingType = EWantSingType.EWST_SPK.value
            if (infoModel != null) {
                if (infoModel.wantSingInfos.isEmpty()) {
                    // ???????????????????????????
                    if (infoModel.music != null) {
                        preAccUrl = infoModel.music.acc
                    }
                } else {
                    //  ?????????????????????????????????
                    if (infoModel.music != null) {
                        val pkSongModel = infoModel.music.pkMusic
                        if (pkSongModel != null) {
                            preAccUrl = pkSongModel.acc
                        }
                    }
                }
            }
        } else if (songModel != null && songModel.playType == StandPlayType.PT_CHO_TYPE.value) {
            wantSingType = EWantSingType.EWST_CHORUS.value
        } else if (songModel != null && songModel.playType == StandPlayType.PT_MINI_GAME_TYPE.value) {
            wantSingType = EWantSingType.EWST_MIN_GAME.value
        } else {
            if (challenge) {
                if (mRoomData.getCoin() < 1) {
                    MyLog.w(TAG, "??????????????????,??????????????????")
                    U.getToastUtil().showShort("?????????????????????")
                    return
                }
                if (mRoomData.isAccEnable && songModel != null && !TextUtils.isEmpty(songModel.acc)) {
                    wantSingType = EWantSingType.EWST_ACCOMPANY_OVER_TIME.value
                    preAccUrl = songModel.acc
                } else {
                    wantSingType = EWantSingType.EWST_COMMON_OVER_TIME.value
                }
            } else {
                if (mRoomData.isAccEnable && songModel != null && !TextUtils.isEmpty(songModel.acc)) {
                    wantSingType = EWantSingType.EWST_ACCOMPANY.value
                    preAccUrl = songModel.acc
                } else {
                    wantSingType = EWantSingType.EWST_DEFAULT.value
                }
            }
        }

        map["wantSingType"] = wantSingType
        map["hasPassedCertify"] = MyUserInfoManager.hasGrabCertifyPassed()

        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        ApiMethods.subscribe(mRoomServerApi.wangSingChance(body), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult) {
                MyLog.w(TAG, "grabThisRound erro code is " + result.errno + ",traceid is " + result.traceId)
                if (result.errno == 0) {
                    //true??????????????????????????????????????????false????????????
                    val mHasPassedCertify = result.data!!.getBoolean("hasPassedCertify")!!

                    if (mHasPassedCertify) {
                        MyUserInfoManager.setGrabCertifyPassed(mHasPassedCertify)
                        //????????????
                        val now = mRoomData.realRoundInfo
                        if (now != null && now.roundSeq == seq) {
                            val wantSingerInfo = WantSingerInfo()
                            wantSingerInfo.wantSingType = wantSingType
                            wantSingerInfo.userID = MyUserInfoManager.uid.toInt()
                            wantSingerInfo.timeMs = System.currentTimeMillis()
                            now.addGrabUid(true, wantSingerInfo)

                            if (result.data!!.getBoolean("success")!!) {
                                val coin = result.data!!.getIntValue("coin")
                                mRoomData.setCoin(coin)
                            }
                        } else {
                            MyLog.w(TAG, "now != null && now.getRoundSeq() == seq ??????????????????" + result.traceId)
                        }
                    } else {
                        if (mDialogPlus != null) {
                            mDialogPlus!!.dismiss()
                        }

                        val tipsDialogView = TipsDialogView.Builder(mBaseActivity)
                                .setMessageTip("???????????????????????????????????????????????????")
                                .setConfirmTip("????????????")
                                .setCancelTip("????????????")
                                .setConfirmBtnClickListener(object : AnimateClickListener() {
                                    override fun click(view: View) {
                                        if (mDialogPlus != null) {
                                            mDialogPlus!!.dismiss()
                                        }
                                        mIGrabView.beginOuath()
                                    }
                                })
                                .setCancelBtnClickListener(object : AnimateClickListener() {
                                    override fun click(view: View) {
                                        if (mDialogPlus != null) {
                                            mDialogPlus!!.dismiss()
                                        }
                                    }
                                })
                                .build()

                        mDialogPlus = DialogPlus.newDialog(mBaseActivity)
                                .setContentHolder(ViewHolder(tipsDialogView))
                                .setGravity(Gravity.BOTTOM)
                                .setContentBackgroundResource(com.component.busilib.R.color.transparent)
                                .setOverlayBackgroundResource(com.component.busilib.R.color.black_trans_80)
                                .setExpanded(false)
                                .create()
                        mDialogPlus!!.show()
                    }
                } else if (result.errno == 8346158) {
                    MyLog.w(TAG, "grabThisRound failed ?????????????????? ")
                    U.getToastUtil().showShort(result.errmsg)
                    EventBus.getDefault().post(ShowHalfRechargeFragmentEvent())
                } else {
                    MyLog.w(TAG, "grabThisRound failed, " + result.traceId)
                }
            }

            override fun onError(e: Throwable) {
                MyLog.e(TAG, "grabThisRound error $e")

            }
        }, this)

        mAccUrl = preAccUrl
        if (!TextUtils.isEmpty(preAccUrl)) {
            MediaCacheManager.preCache(preAccUrl)
        }
    }

    /**
     * ??????
     */
    fun lightsOff() {
        val now = mRoomData.realRoundInfo ?: return
        if (!now.isSingStatus) {
            MyLog.d(TAG, "lightsOff ?????????????????????cancel status=" + now.status + " roundSeq=" + now.roundSeq)
            return
        }
        val roundSeq = now.roundSeq
        val map = HashMap<String, Any>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = roundSeq
        if (now.status == EQRoundStatus.QRS_SPK_FIRST_PEER_SING.value) {
            map["subRoundSeq"] = 0
        } else if (now.status == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.value) {
            map["subRoundSeq"] = 1
        }
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        ApiMethods.subscribe(mRoomServerApi.lightOff(body), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult) {
                MyLog.e(TAG, "lightsOff erro code is " + result.errno + ",traceid is " + result.traceId)
                if (result.errno == 0) {
                    val now = mRoomData.realRoundInfo
                    if (now != null && now.roundSeq == roundSeq) {
                        val noPassingInfo = MLightInfoModel()
                        noPassingInfo.userID = MyUserInfoManager.uid.toInt()
                        now.addLightOffUid(true, noPassingInfo)
                    }
                } else {

                }
            }

            override fun onError(e: Throwable) {
                MyLog.e(TAG, "lightsOff error $e")

            }
        }, this)
    }

    /**
     * ??????
     */
    fun lightsBurst() {
        val now = mRoomData.realRoundInfo ?: return
        if (!now.isSingStatus) {
            MyLog.d(TAG, "lightsBurst ?????????????????????cancel status=" + now.status + " roundSeq=" + now.roundSeq)
            return
        }
        if (RA.hasTestList()) {
            val map = HashMap<String, String>()
            map.put("testList", RA.getTestList())
            StatisticsAdapter.recordCountEvent("ra", "burst", map)
        }
        val map = HashMap<String, Any>()
        map["roomID"] = mRoomData.gameId
        val roundSeq = now.roundSeq
        map["roundSeq"] = mRoomData.realRoundSeq

        if (now.status == EQRoundStatus.QRS_SPK_FIRST_PEER_SING.value) {
            map["subRoundSeq"] = 0
        } else if (now.status == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.value) {
            map["subRoundSeq"] = 1
        }
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        ApiMethods.subscribe(mRoomServerApi.lightBurst(body), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult) {
                MyLog.e(TAG, "lightsBurst erro code is " + result.errno + ",traceid is " + result.traceId)
                if (result.errno == 0) {
                    val now = mRoomData.realRoundInfo
                    if (now != null && now.roundSeq == roundSeq) {
                        val coin = result.data!!.getIntValue("coin")
                        mRoomData.setCoin(coin)
                        if (result.data!!.getBoolean("isBLightSuccess")!!) {
                            val m = BLightInfoModel()
                            m.userID = MyUserInfoManager.uid.toInt()
                            //??????????????????????????????????????????????????????????????????
//                            now.addLightBurstUid(true, m)
                        } else {
                            val reason = result.data!!.getString("bLightFailedMsg")
                            if (!TextUtils.isEmpty(reason)) {
                                U.getToastUtil().showShort(reason)
                            }
                        }
                    }
                } else {

                }
            }

            override fun onError(e: Throwable) {
                MyLog.e(TAG, "lightsOff error $e")

            }
        }, this)
    }

    private fun robotSingBegin() {
        var skrerUrl: String? = null
        val grabRoundInfoModel = mRoomData.realRoundInfo
        if (grabRoundInfoModel != null) {
            val grabSkrResourceModel = grabRoundInfoModel.skrResource
            if (grabSkrResourceModel != null) {
                skrerUrl = grabSkrResourceModel.audioURL
            }
        }
        if (mRobotScoreHelper == null) {
            mRobotScoreHelper = RobotScoreHelper()
        }

        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayer()
        }
        mExoPlayer!!.startPlay(skrerUrl)
        mExoPlayer!!.setCallback(object : PlayerCallbackAdapter() {
            override fun onPrepared() {
                if (!grabRoundInfoModel!!.isParticipant && grabRoundInfoModel.enterStatus == EQRoundStatus.QRS_SING.value) {
                    MyLog.d(TAG, "?????????????????????????????????????????????????????????seek?????? " + grabRoundInfoModel.elapsedTimeMs)
                    mExoPlayer!!.seekTo(grabRoundInfoModel.elapsedTimeMs.toLong())
                }
            }
        })
        if (mRoomData.isMute || !U.getActivityUtils().isAppForeground) {
            mExoPlayer!!.volume = 0f
        } else {
            mExoPlayer!!.volume = 1f
        }
    }

    private fun tryStopRobotPlay() {
        if (mExoPlayer != null) {
            mExoPlayer!!.reset()
        }
    }

    fun muteAllRemoteAudioStreams(mute: Boolean, fromUser: Boolean) {
        if (fromUser) {
            mRoomData.isMute = mute
        }
        ZqEngineKit.getInstance().muteAllRemoteAudioStreams(mute)
        // ????????????????????????
        if (mute) {
            // ???????????????
            if (mExoPlayer != null) {
                mExoPlayer!!.setMuteAudio(true)
            }
        } else {
            // ??????????????????
            if (mExoPlayer != null) {
                mExoPlayer!!.setMuteAudio(false)
            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param roundInfoModel
     */
    private fun onSelfRoundOver(roundInfoModel: GrabRoundInfoModel) {
        // ?????????????????????????????????????????????
        //        if (SkrConfig.getInstance().isNeedUploadAudioForAI(GameModeType.GAME_MODE_GRAB)) {
        //            //???????????????????????????????????????
        //            // ???????????????????????????????????????
        //            if (mRoomData.getGameId() > 0) {
        //                ZqEngineKit.getInstance().stopAudioRecording();
        //            }
        //            // ????????????
        //            if (mRobotScoreHelper != null) {
        //                if (mRobotScoreHelper.isScoreEnough()) {
        //                    if (roundInfoModel.getOverReason() == EQRoundOverReason.ROR_LAST_ROUND_OVER.getValue()
        //                            && roundInfoModel.getResultType() == EQRoundResultType.ROT_TYPE_1.getValue()) {
        //                        // ???????????????????????????
        //                        roundInfoModel.setSysScore(mRobotScoreHelper.getAverageScore());
        //                        uploadRes1ForAi(roundInfoModel);
        //                    } else {
        //                        MyLog.d(TAG, "?????????????????????????????????");
        //                    }
        //                } else {
        //                    MyLog.d(TAG, "isScoreEnough false");
        //                }
        //            }
        //        }

        if (mGrabRedPkgPresenter != null && mGrabRedPkgPresenter!!.isCanReceive) {
            mGrabRedPkgPresenter!!.getRedPkg()
        }

        if (roundInfoModel.isNormalRound) {
            if (roundInfoModel.overReason == EQRoundOverReason.ROR_LAST_ROUND_OVER.value) {
                if (roundInfoModel.resultType == EQRoundResultType.ROT_TYPE_1.value) {
                    // ???????????? ????????? ???pk?????????????????????
                    var songModel = roundInfoModel.music
                    var baodengCnt = roundInfoModel.getbLightInfos().size
                    if (baodengCnt > mRoomData.maxGetBLightCnt) {
                        // ?????????????????? ?????? ??????????????????????????????????????????????????????????????????
                        uploadResForLabel(roundInfoModel)
                    }
                }
            }
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param roundInfoModel
     */
    private fun uploadResForLabel(roundInfoModel: GrabRoundInfoModel) {
        UploadParams.newBuilder(savePath)
                .setFileType(UploadParams.FileType.audioAi)
                .startUploadAsync(object : UploadCallback {
                    override fun onProgressNotInUiThread(currentSize: Long, totalSize: Long) {
                    }

                    override fun onSuccessNotInUiThread(url: String?) {
                        uploadLabelToServer(roundInfoModel, savePath, url ?: "")
                    }

                    override fun onFailureNotInUiThread(msg: String?) {
                    }
                })
    }

    private fun uploadLabelToServer(roundInfoModel: GrabRoundInfoModel, localPath: String, audioUrl: String) {
        launch {
            val map = HashMap<String, Any>()
            map["roomID"] = mRoomData.gameId
            map["audioDur"] = U.getMediaUtils().getDuration(localPath, roundInfoModel.singTotalMs)
            map["audioURL"] = audioUrl
            map["bLightCnt"] = roundInfoModel.getbLightInfos().size
            map["itemID"] = roundInfoModel.music.itemID
            map["itemName"] = roundInfoModel.music.itemName
            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
            val result = subscribe { mRoomServerApi.uploadChallengeResource(body) }
            if (result.errno == 0) {
                mRoomData.maxGetBLightCnt = roundInfoModel.getbLightInfos().size
                MyLog.d(TAG, "??????????????????????????????")
            }
            savePath = ""
        }
    }

    //    /**
    //     * ?????????????????????????????????????????????
    //     *
    //     * @param roundInfoModel
    //     * @param audioUrl
    //     */
    //    private void sendUploadRequest(BaseRoundInfoModel roundInfoModel, String audioUrl) {
    //        long timeMs = System.currentTimeMillis();
    //        HashMap<String, Object> map = new HashMap<>();
    //        map.put("roomID", mRoomData.getGameId());
    //        map.put("itemID", roundInfoModel.getPlaybookID());
    //        map.put("sysScore", roundInfoModel.getSysScore());
    //        map.put("audioURL", audioUrl);
    ////        map.put("midiURL", midiUrl);
    //        map.put("timeMs", timeMs);
    //        StringBuilder sb = new StringBuilder();
    //        sb.append("skrer")
    //                .append("|").append(mRoomData.getGameId())
    //                .append("|").append(roundInfoModel.getPlaybookID())
    //                .append("|").append(roundInfoModel.getSysScore())
    //                .append("|").append(audioUrl)
    ////                .append("|").append(midiUrl)
    //                .append("|").append(timeMs);
    //        String sign = U.getMD5Utils().MD5_32(sb.toString());
    //        map.put("sign", sign);
    //        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
    //        ApiMethods.subscribe(mRoomServerApi.saveRes(body), new ApiObserver<ApiResult>() {
    //            @Override
    //            public void process(ApiResult result) {
    //                if (result.getErrno() == 0) {
    //                    MyLog.e(TAG, "sendAiUploadRequest success");
    //                } else {
    //                    MyLog.e(TAG, "sendAiUploadRequest failed??? errno is " + result.getErrmsg());
    //                }
    //            }
    //
    //            @Override
    //            public void onError(Throwable e) {
    //                MyLog.e(TAG, "sendUploadRequest error " + e);
    //            }
    //        }, this);
    //    }

    override fun destroy() {
        MyLog.d(TAG, "destroy begin")
        super.destroy()
        mDestroyed = true
        Params.save2Pref(ZqEngineKit.getInstance().params)
        MediaCacheManager.cancelPreCache(mAccUrl)
        if (!mRoomData.isHasExitGame) {
            exitRoom("destroy")
        }
        cancelSyncGameStateTask()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        ZqEngineKit.getInstance().destroy("grabroom")
        mUiHandler.removeCallbacksAndMessages(null)
        GrabRoomMsgManager.getInstance().removeFilter(mPushMsgFilter)
        if (mExoPlayer != null) {
            mExoPlayer!!.release()
            mExoPlayer = null
        } else {
            MyLog.d(TAG, "mExoPlayer == null ")
        }

        if (mZipUrlResourceManager != null) {
            mZipUrlResourceManager!!.cancelAllTask()
        }
        ModuleServiceManager.getInstance().msgService.leaveChatRoom(mRoomData.gameId.toString())
        JiGuangPush.exitSkrRoomId(mRoomData.gameId.toString())
        MyLog.d(TAG, "destroy over")
    }

    /**
     * ????????????????????????????????????
     */
    fun sendMyGrabOver(from: String) {
        MyLog.d(TAG, "???????????????????????? from=$from")
        val roundInfoModel = mRoomData.realRoundInfo ?: return
        val map = HashMap<String, Any>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = roundInfoModel.roundSeq

        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        ApiMethods.subscribe(mRoomServerApi.sendGrapOver(body), object : ApiObserver<ApiResult>() {

            override fun process(result: ApiResult) {
                if (result.errno == 0) {
                    MyLog.w(TAG, "?????????????????????????????? traceid is " + result.traceId)
                } else {
                    MyLog.w(TAG, "?????????????????????????????? traceid is " + result.traceId)
                }
            }

            override fun onError(e: Throwable) {
                MyLog.w(TAG, "sendRoundOverInfo error $e")
            }
        }, this)
    }

    /**
     * ????????????????????????
     */
    fun sendRoundOverInfo() {
        MyLog.w(TAG, "????????????????????????")
        estimateOverTsThisRound()

        val roundInfoModel = mRoomData.realRoundInfo
        if (roundInfoModel == null || !roundInfoModel.singBySelf()) {
            return
        }
        val map = HashMap<String, Any>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = roundInfoModel.roundSeq
        if (roundInfoModel.status == EQRoundStatus.QRS_SPK_FIRST_PEER_SING.value) {
            map["subRoundSeq"] = 0
        } else if (roundInfoModel.status == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.value) {
            map["subRoundSeq"] = 1
        }

        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        ApiMethods.subscribe(mRoomServerApi.sendRoundOver(body), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult) {
                if (result.errno == 0) {
                    MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
                } else {
                    MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
                }
            }

            override fun onError(e: Throwable) {
                MyLog.w(TAG, "sendRoundOverInfo error $e")
            }
        }, this)
    }


    /**
     * ??????????????????
     */
    fun giveUpSing(ownerControl: Boolean, okListener: ((seq: Int) -> Unit)?) {
        if (ownerControl) {
            MyLog.w(TAG, "?????????????????????")
            estimateOverTsThisRound()
            val now = mRoomData.realRoundInfo
            if (now == null || !mRoomData.isOwner) {
                return
            }
            val map = HashMap<String, Any>()
            map["roomID"] = mRoomData.gameId
            map["roundSeq"] = now.roundSeq
            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
            if (now.isFreeMicRound) {
                ApiMethods.subscribe(mRoomServerApi.stopFreeMicroByOwner(body), object : ApiObserver<ApiResult>() {
                    override fun process(result: ApiResult) {
                        if (result.errno == 0) {
                            closeEngine()
                            MyLog.w(TAG, "??????????????????????????? traceid is " + result.traceId)
                            okListener?.invoke(now.roundSeq)
                        } else {
                            MyLog.w(TAG, "??????????????????????????? traceid is " + result.traceId)
                        }
                    }

                    override fun onError(e: Throwable) {
                        MyLog.w(TAG, "stopFreeMicroByOwner error $e")
                    }
                }, this)
            } else {
                ApiMethods.subscribe(mRoomServerApi.stopMiniGameByOwner(body), object : ApiObserver<ApiResult>() {
                    override fun process(result: ApiResult) {
                        if (result.errno == 0) {
                            closeEngine()
                            MyLog.w(TAG, "??????????????????????????? traceid is " + result.traceId)
                            okListener?.invoke(now.roundSeq)
                        } else {
                            MyLog.w(TAG, "??????????????????????????? traceid is " + result.traceId)
                        }
                    }

                    override fun onError(e: Throwable) {
                        MyLog.w(TAG, "stopMiniGameByOwner error $e")
                    }
                }, this)
            }

        } else {
            MyLog.w(TAG, "???????????????")
            estimateOverTsThisRound()
            val now = mRoomData.realRoundInfo
            if (now == null || !now.singBySelf()) {
                return
            }
            val map = HashMap<String, Any>()
            map["roomID"] = mRoomData.gameId
            map["roundSeq"] = now.roundSeq
            if (now.music != null) {
                map["playType"] = now.music.playType
            }
            if (now.status == EQRoundStatus.QRS_SPK_FIRST_PEER_SING.value) {
                map["subRoundSeq"] = 0
            } else if (now.status == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.value) {
                map["subRoundSeq"] = 1
            }
            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
            ApiMethods.subscribe(mRoomServerApi.giveUpSing(body), object : ApiObserver<ApiResult>() {
                override fun process(result: ApiResult) {
                    if (result.errno == 0) {
                        okListener?.invoke(now.roundSeq)
                        closeEngine()
                        MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
                    } else {
                        MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
                    }
                }

                override fun onError(e: Throwable) {
                    MyLog.w(TAG, "giveUpSing error $e")
                }
            }, this)
        }
    }

    /**
     * ????????????????????? ?????? ??????
     *
     * @param mute true ??????
     */
    fun miniOwnerMic(mute: Boolean) {
        MyLog.d(TAG, "miniOwnerMic mute=$mute")
        if (mute) {
            if (ZqEngineKit.getInstance().params.isAnchor) {
                ZqEngineKit.getInstance().setClientRole(false)
            }
            ZqEngineKit.getInstance().muteLocalAudioStream(true)
        } else {
            if (!ZqEngineKit.getInstance().params.isAnchor) {
                ZqEngineKit.getInstance().setClientRole(true)
            }
            ZqEngineKit.getInstance().muteLocalAudioStream(false)
        }

    }

    /**
     * ????????????
     *
     * @param userId ?????????id
     */
    fun reqKickUser(userId: Int) {
        val roundInfoModel = mRoomData.realRoundInfo ?: return
        val map = HashMap<String, Any>()
        map["kickUserID"] = userId
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = roundInfoModel.roundSeq

        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        ApiMethods.subscribe(mRoomServerApi.reqKickUser(body), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult) {
                if (result.errno == 0) {
                    if (mRoomData.isOwner) {
                        val kickTimes = result.data!!.getIntValue("resKickUserTimes")
                        // TODO: 2019/5/8 ??????????????????
                        mRoomData.ownerKickTimes = kickTimes
                        if (kickTimes > 0) {
                            U.getToastUtil().showShort("????????????")
                        } else {
                            U.getToastUtil().showShort("????????????????????????")
                        }
                    } else {
                        U.getToastUtil().showShort("????????????????????????")
                    }
                    val coin = result.data!!.getIntValue("coin")
                    mRoomData.setCoin(coin)
                } else {
                    U.getToastUtil().showShort("" + result.errmsg)
                }
            }

            override fun onError(e: Throwable) {

            }
        }, this)
    }


    /**
     * ??????????????????
     *
     * @param isAgree      ????????????
     * @param userId       ?????????ID
     * @param sourceUserId ?????????ID
     */
    fun voteKickUser(isAgree: Boolean, userId: Int, sourceUserId: Int) {
        val map = HashMap<String, Any>()
        map["agree"] = isAgree
        map["kickUserID"] = userId
        map["roomID"] = mRoomData.gameId
        map["sourceUserID"] = sourceUserId

        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        ApiMethods.subscribe(mRoomServerApi.rspKickUser(body), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult) {
                if (result.errno == 0) {
                    U.getToastUtil().showShort("??????????????????")
                } else {
                    U.getToastUtil().showShort(result.errmsg)
                }
            }

            override fun onError(e: Throwable) {

            }
        }, this)

    }

    /**
     * ????????????
     */
    fun exitRoom(from: String) {
        MyLog.w(TAG, "exitRoom from=$from")
        val map = HashMap<String, Any>()
        map["roomID"] = mRoomData.gameId
        mUiHandler.sendEmptyMessageDelayed(MSG_ENSURE_EXIT, 5000)
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        ApiMethods.subscribe(mRoomServerApi.exitRoom(body), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult) {
                mUiHandler.removeMessages(MSG_ENSURE_EXIT)
                if (result.errno == 0) {
                    mRoomData.isHasExitGame = true
                    val models = JSON.parseArray(result.data!!.getString("numericDetail"), NumericDetailModel::class.java)
                    val levelResultModel = JSON.parseObject(result.data!!.getString("userScoreChange"), LevelResultModel::class.java)
                    val starCnt = result.data.getInteger("starCnt")
                    if (models != null) {
                        // ????????????
                        mRoomData.grabResultData = GrabResultData(models, levelResultModel, starCnt)
                        mIGrabView.onGetGameResult(true)
                    } else {
                        mIGrabView.onGetGameResult(false)
                    }
                } else {
                    mIGrabView.onGetGameResult(false)
                }
            }

            override fun onNetworkError(errorType: ApiObserver.ErrorType) {
                mUiHandler.removeMessages(MSG_ENSURE_EXIT)
                super.onNetworkError(errorType)
                mIGrabView.onGetGameResult(false)
            }
        })
    }

    /**
     * ????????????
     */
    fun changeRoom() {
        if (mSwitchRooming) {
            U.getToastUtil().showShort("?????????")
            return
        }
        //        if(true){
        //            stopGuide();
        //            mRoomData.setRealRoundInfo(null);
        //            mIGrabView.hideAllCardView();
        //            joinRoomAndInit(false);
        //            ZqEngineKit.getInstance().unbindAllRemoteVideo();
        //            mRoomData.checkRoundInEachMode();
        //            mIGrabView.onChangeRoomResult(true, null);
        //            mIGrabView.dimissKickDialog();
        //            return;
        //        }
        mSwitchRooming = true
        val map = HashMap<String, Any>()
        map["roomID"] = mRoomData.gameId
        map["tagID"] = mRoomData.tagId
        map["vars"] = RA.getVars()
        map["testList"] = RA.getTestList()
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        ApiMethods.subscribe(mRoomServerApi.changeRoom(body), object : ApiObserver<ApiResult>() {

            override fun onNext(result: ApiResult) {
                if (result.errno == 0) {
                    val joinGrabRoomRspModel = JSON.parseObject(result.data!!.toJSONString(), JoinGrabRoomRspModel::class.java)
                    onChangeRoomSuccess(joinGrabRoomRspModel)
                } else {
                    mIGrabView.onChangeRoomResult(false, result.errmsg)
                }
                mSwitchRooming = false
            }

            override fun process(obj: ApiResult) {

            }

            override fun onNetworkError(errorType: ApiObserver.ErrorType) {
                super.onNetworkError(errorType)
                mSwitchRooming = false
                mIGrabView.onChangeRoomResult(false, "????????????")
            }
        }, this, RequestControl("changeRoom", ControlType.CancelThis))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: GrabChangeRoomEvent) {
        onChangeRoomSuccess(event.mJoinGrabRoomRspModel)
    }

    fun onChangeRoomSuccess(joinGrabRoomRspModel: JoinGrabRoomRspModel?) {
        MyLog.d(TAG, "onChangeRoomSuccess joinGrabRoomRspModel=$joinGrabRoomRspModel")
        if (joinGrabRoomRspModel != null) {
            EventBus.getDefault().post(SwitchRoomEvent())
            stopGuide()
            mRoomData.loadFromRsp(joinGrabRoomRspModel)
            joinRoomAndInit(false)
            mIGrabView.onChangeRoomResult(true, null)
            mRoomData.checkRoundInEachMode()
            mIGrabView.dimissKickDialog()
        }
    }

    /**
     * ???????????????????????????
     *
     * @param out ?????????
     * @param in  ?????????
     */
    fun swapGame(out: Boolean, `in`: Boolean) {
        MyLog.w(TAG, "swapGame out=$out in=$`in`")
        val map = HashMap<String, Any>()
        map["roomID"] = mRoomData.gameId
        if (out) {
            map["status"] = SwapStatusType.SS_SWAP_OUT
        } else if (`in`) {
            map["status"] = SwapStatusType.SS_SWAP_IN
        }
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        ApiMethods.subscribe(mRoomServerApi.swap(body), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult) {
                if (result.errno == 0) {
                    //                    U.getToastUtil().showShort("????????????????????????");
                } else {
                    MyLog.e(TAG, "swapGame result errno is " + result.errmsg)
                }
            }

            override fun onError(e: Throwable) {
                MyLog.e(TAG, "swapGame error $e")
            }
        }, this)
    }

    /**
     * ??????????????????task
     */
    fun startSyncGameStateTask(delayTime: Long) {
        cancelSyncGameStateTask()

        if (mRoomData.isIsGameFinish) {
            MyLog.w(TAG, "???????????????????????????Sync")
            return
        }

        mSyncGameStateTask = HandlerTaskTimer.newBuilder()
                .delay(delayTime)
                .interval(sSyncStateTaskInterval)
                .take(-1)
                .start(object : HandlerTaskTimer.ObserverW() {
                    override fun onNext(t: Int) {
                        MyLog.w(TAG, (sSyncStateTaskInterval / 1000).toString() + "????????? syncGameTask ??????????????????")
                        syncGameStatus(mRoomData.gameId)
                    }
                })
    }

    fun cancelSyncGameStateTask() {
        if (mSyncGameStateTask != null) {
            mSyncGameStateTask!!.dispose()
        }
    }

    // ????????????????????????(???????????????????????????)
    fun syncGameStatus(gameID: Int) {
        ApiMethods.subscribe(mRoomServerApi.syncGameStatus(gameID), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult) {
                if (result.errno == 0) {
                    if (gameID != mRoomData.gameId) {
                        MyLog.d(TAG, "syncGameStatus gameID ?????????")
                        return
                    }
                    val syncStatusTimes = result.data!!.getLongValue("syncStatusTimeMs")  //?????????????????????????????????
                    val gameOverTimeMs = result.data!!.getLongValue("gameOverTimeMs")  //??????????????????
                    val currentInfo = JSON.parseObject(result.data!!.getString("currentRound"), GrabRoundInfoModel::class.java) //??????????????????
                    val nextInfo = JSON.parseObject(result.data!!.getString("nextRound"), GrabRoundInfoModel::class.java) //??????????????????

                    var msg = if (currentInfo != null) {
                        "syncGameStatus?????????, currentRound ??? $currentInfo"
                    } else {
                        "syncGameStatus?????????, currentRound ??? null"
                    }

                    msg = msg + ",traceid is " + result.traceId
                    MyLog.w(TAG, msg)

                    if (currentInfo == null) {
                        onGameOver("syncGameStatus", gameOverTimeMs)
                        return
                    }

                    updatePlayerState(gameOverTimeMs, syncStatusTimes, currentInfo, gameID)
                    //                    fetchAcc(nextInfo);
                } else {
                    if (8346002 == result.errno) {
                        MyLog.w(TAG, "syncGameStatus??????, ???????????????")
                    } else {
                        MyLog.w(TAG, "syncGameStatus?????? traceid is " + result.traceId)
                        estimateOverTsThisRound()
                    }
                }
            }

            override fun onError(e: Throwable) {
                MyLog.w(TAG, "syncGameStatus????????????errno???$e")
            }
        }, this)
    }

    /**
     * ?????????????????????????????????,??????????????????????????????SyncStatusEvent push???sycn?????????????????????
     */
    @Synchronized
    private fun updatePlayerState(gameOverTimeMs: Long, syncStatusTimes: Long, newRoundInfo: GrabRoundInfoModel, gameId: Int) {
        MyLog.w(TAG, "updatePlayerState" + " gameOverTimeMs=" + gameOverTimeMs + " syncStatusTimes=" + syncStatusTimes + " currentInfo=" + newRoundInfo.roundSeq + ",gameId is " + gameId)
        if (!newRoundInfo.isContainInRoom) {
            MyLog.w(TAG, "updatePlayerState, ??????????????????????????? game id is $gameId")
            if (mFirstKickOutTime == -1L) {
                mFirstKickOutTime = System.currentTimeMillis()
            }
            mAbsenTimes++
            if (System.currentTimeMillis() - mFirstKickOutTime > 15000 && mAbsenTimes > 10) {
                MyLog.w(TAG, "??????15??? && ???????????????10?????????????????????")
                exitRoom("updatePlayerState")
                return
            }
        } else {
            mFirstKickOutTime = -1
            mAbsenTimes = 0
        }

        if (syncStatusTimes > mRoomData.lastSyncTs) {
            mRoomData.lastSyncTs = syncStatusTimes
        }

        if (gameOverTimeMs != 0L) {
            if (gameOverTimeMs > mRoomData.gameStartTs) {
                MyLog.w(TAG, "gameOverTimeMs ???= 0 ?????????????????????")
                // ???????????????
                onGameOver("sync", gameOverTimeMs)
            } else {
                MyLog.w(TAG, "?????????????????????????????? startTs:" + mRoomData.gameStartTs + " overTs:" + gameOverTimeMs)
            }
        } else {
            // ????????? current ????????????null
            if (newRoundInfo != null) {
                // ?????????????????????????????????????????????????????????????????????????????????????????????
                if (!mRoomData.hasGameBegin()) {
                    MyLog.w(TAG, "updatePlayerState ???????????????????????????????????????????????????")
                    // ??????????????????????????????????????????
                    mRoomData.setHasGameBegin(true)
                    mRoomData.expectRoundInfo = newRoundInfo
                    mRoomData.checkRoundInEachMode()
                } else if (RoomDataUtils.roundSeqLarger<GrabRoundInfoModel>(newRoundInfo, mRoomData.expectRoundInfo)) {
                    MyLog.w(TAG, "updatePlayerState sync ???????????????????????????????????????")
                    // ??????????????????????????????????????????
                    mRoomData.expectRoundInfo = newRoundInfo
                    mRoomData.checkRoundInEachMode()
                } else if (RoomDataUtils.isCurrentExpectingRound(newRoundInfo.roundSeq, mRoomData)) {
                    /**
                     * ???????????????????????????????????????????????????
                     */
                    if (syncStatusTimes >= mRoomData.lastSyncTs) {
                        MyLog.w(TAG, "updatePlayerState sync ??????????????????")
                        mRoomData.expectRoundInfo!!.tryUpdateRoundInfoModel(newRoundInfo, true)
                    }
                }
            } else {
                MyLog.w(TAG, "?????????????????????????????? currentInfo=null")
            }
        }
    }

    /**
     * ????????????????????????????????????
     */
    private fun userStatisticForIntimacy() {
        launch {
            while (true) {
                delay(10 * 60 * 1000)
                val l1 = java.util.ArrayList<Int>()
                for (m in mRoomData.getPlayerAndWaiterInfoList()) {
                    if (m.userID != MyUserInfoManager.uid.toInt()) {
                        if (mRoomData.preUserIDsSnapShots.contains(m.userID)) {
                            l1.add(m.userID)
                        }
                    }
                }
                if (l1.isNotEmpty()) {
                    val map = java.util.HashMap<String, Any>()
                    map["gameID"] = mRoomData.gameId
                    map["mode"] = GameModeType.GAME_MODE_GRAB
                    val ts = System.currentTimeMillis()
                    map["timeMs"] = ts
                    map["sign"] = U.getMD5Utils().MD5_32("skrer|" + MyUserInfoManager.uid + "|" + ts)
                    map["userID"] = MyUserInfoManager.uid
                    map["preUserIDs"] = l1
                    val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
                    val result = subscribe { mRoomServerApi.userStatistic(body) }
                    if (result.errno == 0) {

                    }
                } else {
                    break
                }
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING, priority = 9)
    fun onEvent(event: GrabGameOverEvent) {
        MyLog.d(TAG, "GrabGameOverEvent")
        estimateOverTsThisRound()
        tryStopRobotPlay()
        ZqEngineKit.getInstance().stopRecognize()
        mRoomData.isIsGameFinish = true
        cancelSyncGameStateTask()
        // ???????????????,???????????????ui??????
        mUiHandler.post { mIGrabView.roundOver(event.lastRoundInfo, false, null) }
        // ???????????????????????????
        ZqEngineKit.getInstance().destroy("grabroom")
        mUiHandler.postDelayed({ mIGrabView.gameFinish() }, 2000)
    }

    /**
     * ?????????????????????
     * ??????ui????????????ui????????????
     */
    @Subscribe(threadMode = ThreadMode.POSTING, priority = 9)
    fun onEvent(event: GrabRoundChangeEvent) {
        DebugLogView.println(TAG, "---??????" + event.newRoundInfo.roundSeq + "??????--- ")
        MyLog.d(TAG, "GrabRoundChangeEvent event=$event")
        // ??????????????????????????????
        estimateOverTsThisRound()
        mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS)
        closeEngine()
        tryStopRobotPlay()
        ZqEngineKit.getInstance().stopRecognize()
        val now = event.newRoundInfo

        if (now != null) {
            // ?????????????????????
            EventBus.getDefault().post(GrabPlaySeatUpdateEvent(now.playUsers))
            EventBus.getDefault().post(GrabWaitSeatUpdateEvent(now.waitUsers))
            // ???????????????
            var size = 0
            for (playerInfoModel in now.playUsers) {
                if (playerInfoModel.userID == 2) {
                    continue
                }
                size++
            }

            val finalSize = size
            if (mRoomData.roomType == GrabRoomType.ROOM_TYPE_PLAYBOOK) {

            } else {
                mUiHandler.post {
                    mIGrabView.showPracticeFlag(finalSize <= 1)
                    mIGrabView.refreshWaitTips()
                }
            }
            // ????????????????????????
            if (mRoomData.inChallenge) {
                val t = now.roundSeq - mRoomData.enterRoundSeq
                if (t == 0 || now.roundSeq == 1) {
                    mUiHandler.post {
                        mIGrabView.showChallengeStarView(0, visiable = true, justShowInChallenge = true, continueShow = true)
                    }
                }
                if (t > 1 && (t - 1) % mRoomData.grabConfigModel.challengeRoundCnt == 0) {
                    //???????????????????????????
                    launch {
                        val result = subscribe { mRoomServerApi.getChallengeStarCount(mRoomData.gameId, mRoomData.enterRoundSeq, now.roundSeq - 1) }
                        if (result.errno == 0) {
                            val cnt = result.data.getIntValue("starCnt")
//                            var continueShow = false
//                            val leftRoundCnt = mRoomData.grabConfigModel.totalGameRoundSeq - now.roundSeq
//                            if (leftRoundCnt >= mRoomData.grabConfigModel.challengeRoundCnt) {
//                                continueShow = true
//                            }
                            var continueShow = result.data.getBooleanValue("inChallenge")
                            mIGrabView.showChallengeStarView(cnt, visiable = true, justShowInChallenge = false, continueShow = continueShow)
                        }
                    }
                }
            } else {
                mUiHandler.post {
                    mIGrabView.showChallengeStarView(0, visiable = false, justShowInChallenge = false, continueShow = false)
                }
            }
        }


        if (now!!.status == EQRoundStatus.QRS_INTRO.value) {
            //??????????????????????????????
            //TODO ?????????????????????
            if (event.lastRoundInfo != null && event.lastRoundInfo.status >= EQRoundStatus.QRS_SING.value) {
                // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                mUiHandler.post { mIGrabView.roundOver(event.lastRoundInfo, true, now) }
                if (event.lastRoundInfo.singBySelf()) {
                    onSelfRoundOver(event.lastRoundInfo)
                }
            } else {
                mUiHandler.post { mIGrabView.grabBegin(now.roundSeq, now.music) }
            }
        } else if (now.isSingStatus) {
            // ????????????
            if (now.singBySelf()) {
                mUiHandler.post { mIGrabView.singBySelf() }
                preOpWhenSelfRound()
            } else {
                mUiHandler.post { mIGrabView.singByOthers() }
                preOpWhenOtherRound(now.userID)
            }
        } else if (now.status == EQRoundStatus.QRS_END.value) {
            MyLog.w(TAG, "GrabRoundChangeEvent ??????????????????????????????????????????????????????roundSeq:" + now.roundSeq)
            MyLog.w(TAG, "???????????????????????????")
        }

        mUiHandler.post { mIGrabView.hideManageTipView() }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: GrabWaitSeatUpdateEvent) {
        MyLog.d(TAG, "onEvent event=$event")
        if (event.list != null && event.list.size > 0) {
            mIGrabView.hideInviteTipView()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: SomeOneJoinWaitSeatEvent) {
        MyLog.d(TAG, "onEvent event=$event")
        mIGrabView.hideInviteTipView()
    }

    /**
     * ?????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun onEvent(event: GrabRoundStatusChangeEvent) {
        MyLog.d(TAG, "GrabRoundStatusChangeEvent event=$event")
        estimateOverTsThisRound()
        mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS)
        val now = event.roundInfo

        var needCloseEngine = true
        if (mRoomData.isVideoRoom) {
            if (now.isPKRound && now.getsPkRoundInfoModels().size >= 2) {
                if (now.status == EQRoundStatus.QRS_SPK_FIRST_PEER_SING.value) {
                    // pk????????????
                    val pkRoundInfoModel2 = now.getsPkRoundInfoModels()[1]
                    if (MyUserInfoManager.uid == pkRoundInfoModel2.userID.toLong()) {
                        // ??????????????????
                        if (pkRoundInfoModel2.overReason == EQRoundOverReason.ROR_IN_ROUND_PLAYER_EXIT.value || pkRoundInfoModel2.overReason == EQRoundOverReason.ROR_SELF_GIVE_UP.value) {
                            needCloseEngine = true
                        } else {
                            needCloseEngine = false
                        }
                    }
                } else if (now.status == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.value) {
                    // pk?????????
                    val pkRoundInfoModel1 = now.getsPkRoundInfoModels()[0]
                    if (MyUserInfoManager.uid == pkRoundInfoModel1.userID.toLong()) {
                        // ??????????????????
                        if (pkRoundInfoModel1.overReason == EQRoundOverReason.ROR_IN_ROUND_PLAYER_EXIT.value || pkRoundInfoModel1.overReason == EQRoundOverReason.ROR_SELF_GIVE_UP.value) {
                            needCloseEngine = true
                        } else {
                            needCloseEngine = false
                        }
                    }
                }
            }
        }

        if (needCloseEngine) {
            closeEngine()
        } else {
            // pk??????????????????????????????
            if (!ZqEngineKit.getInstance().params.isAnchor) {
                ZqEngineKit.getInstance().setClientRole(true)
            }
            // ?????????
            ZqEngineKit.getInstance().muteLocalAudioStream(true)
            ZqEngineKit.getInstance().stopAudioMixing()
            ZqEngineKit.getInstance().stopAudioRecording()
        }
        tryStopRobotPlay()
        if (now.status == EQRoundStatus.QRS_INTRO.value) {
            //??????????????????????????????
            mUiHandler.post { mIGrabView.grabBegin(now.roundSeq, now.music) }
        } else if (now.isSingStatus) {
            // ????????????
            if (now.singBySelf()) {
                mUiHandler.post { mIGrabView.singBySelf() }
                preOpWhenSelfRound()
            } else {
                mUiHandler.post { mIGrabView.singByOthers() }
                preOpWhenOtherRound(now.userID)
            }
        }
    }

    private fun closeEngine() {
        if (mRoomData.gameId > 0) {
            ZqEngineKit.getInstance().stopAudioMixing()
            ZqEngineKit.getInstance().stopAudioRecording()
            if (mRoomData.isSpeaking) {
                MyLog.d(TAG, "closeEngine ?????????????????????????????????")
            } else {
                //                if (mRoomData.isOwner()) {
                //                    MyLog.d(TAG, "closeEngine ????????? mute??????");
                //                    ZqEngineKit.getInstance().muteLocalAudioStream(true);
                //                } else {
                if (ZqEngineKit.getInstance().params.isAnchor) {
                    ZqEngineKit.getInstance().setClientRole(false)
                }
                //                }
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun onEvent(event: EngineEvent) {
        if (event.getType() == EngineEvent.TYPE_USER_ROLE_CHANGE) {
            val roleChangeInfo = event.getObj<EngineEvent.RoleChangeInfo>()
            if (roleChangeInfo.newRole == 1) {
                val roundInfoModel = mRoomData.realRoundInfo
                if (roundInfoModel != null && roundInfoModel.singBySelf()) {
                    MyLog.d(TAG, "??????????????????????????????")
                    onChangeBroadcastSuccess()
                } else if (mRoomData.isSpeaking) {
                    MyLog.d(TAG, "??????????????????????????????")
                }
            }
        } else if (event.getType() == EngineEvent.TYPE_USER_MUTE_AUDIO) {
            //            UserStatus userStatus = event.getUserStatus();
            //            if (userStatus != null) {
            //                MyLog.d(TAG, "??????mute?????? uid=" + userStatus.getUserId());
            //                if (userStatus.getUserId() == mRoomData.getOwnerId()) {
            //                    if (mRoomData.isOwner()) {
            //                        MyLog.d(TAG, "???????????????????????????");
            //                    } else {
            //                        if (!userStatus.isAudioMute()) {
            //                            MyLog.d(TAG, "????????????mute????????????????????????????????????????????????");
            //                            weakVolume(1000);
            //                        } else {
            //                            MyLog.d(TAG, "??????mute??????????????????");
            //                            mUiHandler.removeMessages(MSG_RECOVER_VOLUME);
            //                            mUiHandler.sendEmptyMessage(MSG_RECOVER_VOLUME);
            //                        }
            //                    }
            //                }
            //            }
        } else if (event.getType() == EngineEvent.TYPE_USER_AUDIO_VOLUME_INDICATION) {
            //            List<EngineEvent.UserVolumeInfo> list = event.getObj();
            //            for (EngineEvent.UserVolumeInfo uv : list) {
            //                //    MyLog.d(TAG, "UserVolumeInfo uv=" + uv);
            //                if (uv != null) {
            //                    int uid = uv.getUid();
            //                    if (uid == 0) {
            //                        uid = (int) MyUserInfoManager.getInstance().getUid();
            //                    }
            //                    if (mRoomData != null
            //                            && uid == mRoomData.getOwnerId()
            //                            && uv.getVolume() > 40
            //                            && !mRoomData.isOwner()) {
            //                        MyLog.d(TAG, "???????????????");
            //                        weakVolume(1000);
            //                    }
            //                }
            //            }
        }else if (event.getType() == EngineEvent.TYPE_ENGINE_ERROR) {
            U.getToastUtil().showLong("????????????????????????????????????????????????????????????")
        } else {
            // ?????????????????????????????????????????? ????????????
        }
    }

    /**
     * ????????????
     *
     * @param time
     */
    private fun weakVolume(time: Int) {
        mUiHandler.removeMessages(MSG_RECOVER_VOLUME)
        mUiHandler.sendEmptyMessageDelayed(MSG_RECOVER_VOLUME, time.toLong())
        if (ZqEngineKit.getInstance().params.isAnchor) {
            if (mEngineParamsTemp == null) {
                val audioVolume = ZqEngineKit.getInstance().params.audioMixingPlayoutVolume
                val recordVolume = ZqEngineKit.getInstance().params.recordingSignalVolume
                mEngineParamsTemp = EngineParamsTemp(audioVolume, recordVolume)
                ZqEngineKit.getInstance().adjustAudioMixingPlayoutVolume((audioVolume * 0.2).toInt(), false)
                ZqEngineKit.getInstance().adjustRecordingSignalVolume((recordVolume * 0.2).toInt(), false)
            }
        } else {
            MyLog.d(TAG, "????????????????????????")
        }
        if (mExoPlayer != null) {
            mExoPlayer!!.setVolume(mExoPlayer!!.volume * 0.0f, false)
        }
    }

    /**
     * ?????????????????????
     */
    private fun onChangeBroadcastSuccess() {
        MyLog.d(TAG, "onChangeBroadcastSuccess ??????????????????")
        mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS)
        mUiHandler.post(Runnable {
            val infoModel = mRoomData.realRoundInfo
            if (infoModel == null || !infoModel.singBySelf()) {
                MyLog.d(TAG, "onChangeBroadcastSuccess,?????????????????????????????????cancel")
                return@Runnable
            }
            var songModel: SongModel? = infoModel.music ?: return@Runnable
            if (infoModel.status == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.value) {
                songModel = songModel!!.pkMusic
            }
            if (songModel == null) {
                return@Runnable
            }
            // ??????????????????????????????????????????mute
//            val accFile = SongResUtils.getAccFileByUrl(songModel.acc)
            // midi????????????????????????????????????native?????????????????????????????????
            val midiFile = SongResUtils.getMIDIFileByUrl(songModel.midi)

            if (mRoomData.isAccEnable && infoModel.isAccRound || infoModel.isPKRound) {
                val songBeginTs = songModel.beginMs
//                if (accFile != null && accFile.exists()) {
//                    // ??????????????????
//                    ZqEngineKit.getInstance().startAudioMixing(MyUserInfoManager.uid.toInt(), accFile.absolutePath, midiFile.absolutePath, songBeginTs.toLong(), 1)
//                } else {
                    ZqEngineKit.getInstance().startAudioMixing(MyUserInfoManager.uid.toInt(), songModel.accWithCdnInfosJson, midiFile.absolutePath, songBeginTs.toLong(), 1)
//                }
            }
        })
    }

    /**
     * ????????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: QWantSingChanceMsgEvent) {
        if (RoomDataUtils.isCurrentExpectingRound(event.getRoundSeq()!!, mRoomData)) {
            MyLog.w(TAG, "???????????????userID " + event.getUserID() + ", seq " + event.getRoundSeq())
            val roundInfoModel = mRoomData.expectRoundInfo

            val wantSingerInfo = WantSingerInfo()
            wantSingerInfo.userID = event.getUserID()!!
            wantSingerInfo.timeMs = System.currentTimeMillis()
            wantSingerInfo.wantSingType = event.getWantSingType()

            if (roundInfoModel!!.status == EQRoundStatus.QRS_INTRO.value) {
                roundInfoModel.addGrabUid(true, wantSingerInfo)
            } else {
                MyLog.d(TAG, "????????????????????????????????????")
                roundInfoModel.addGrabUid(false, wantSingerInfo)
            }
        } else {
            MyLog.w(TAG, "????????????,???????????????????????????userID " + event.getUserID() + ", seq " + event.getRoundSeq() + "?????????????????? " + mRoomData.expectRoundInfo)
        }
    }

    /**
     * ????????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: QGetSingChanceMsgEvent) {
        ensureInRcRoom()
        if (RoomDataUtils.isCurrentExpectingRound(event.getRoundSeq()!!, mRoomData)) {
            MyLog.w(TAG, "??????????????????userID " + event.getUserID() + ", roundInfo" + event.currentRound)
            val roundInfoModel = mRoomData.expectRoundInfo
            roundInfoModel!!.isHasSing = true
            roundInfoModel.userID = event.getUserID()!!
            roundInfoModel.tryUpdateRoundInfoModel(event.getCurrentRound(), true)
            // ??????????????????????????????????????? updateStatus???
            //roundInfoModel.updateStatus(true, EQRoundStatus.QRS_SING.getValue());
        } else {
            MyLog.w(TAG, "???????????????,???????????????????????????userID " + event.getUserID() + ", seq " + event.getRoundSeq() + "?????????????????? " + mRoomData.expectRoundInfo)
        }
    }

    /**
     * ????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: QLightOffMsgEvent) {
        if (RoomDataUtils.isCurrentExpectingRound(event.roundSeq, mRoomData)) {
            MyLog.w(TAG, "??????????????????userID " + event.userID + ", seq " + event.roundSeq)
            val roundInfoModel = mRoomData.expectRoundInfo
            //??????????????????????????????????????????
            //roundInfoModel.updateStatus(true, EQRoundStatus.QRS_SING.getValue());
            val noPassingInfo = MLightInfoModel()
            noPassingInfo.userID = event.userID
            roundInfoModel!!.addLightOffUid(true, noPassingInfo)
        } else {
            MyLog.w(TAG, "???????????????,???????????????????????????userID " + event.userID + ", seq " + event.roundSeq + "?????????????????? " + mRoomData.expectRoundInfo)
        }
    }

    /**
     * ????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: QLightBurstMsgEvent) {
        if (RoomDataUtils.isCurrentExpectingRound(event.roundSeq, mRoomData)) {
            MyLog.w(TAG, "??????????????????userID " + event.userID + ", seq " + event.roundSeq)
            val roundInfoModel = mRoomData.expectRoundInfo
            //??????????????????????????????????????????
            //            roundInfoModel.updateStatus(true, EQRoundStatus.QRS_SING.getValue());
            val noPassingInfo = BLightInfoModel()
            noPassingInfo.userID = event.userID
            noPassingInfo.setbLightEffectModel(event.bLightEffectModel)
            roundInfoModel!!.addLightBurstUid(true, noPassingInfo)
        } else {
            MyLog.w(TAG, "???????????????,???????????????????????????userID " + event.userID + ", seq " + event.roundSeq + "?????????????????? " + mRoomData.expectRoundInfo)
        }
    }

    /**
     * ????????????????????????????????????sych????????????????????????????????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: GrabSomeOneLightOffEvent) {
        MyLog.d(TAG, "onEvent event=$event")
        if (event.roundInfo.status == EQRoundStatus.QRS_SPK_FIRST_PEER_SING.value) {
            if (event.roundInfo.getsPkRoundInfoModels().size > 0) {
                pretendLightMsgComment(event.roundInfo.getsPkRoundInfoModels()[0].userID, event.uid, false)
            }
        } else if (event.roundInfo.status == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.value) {
            if (event.roundInfo.getsPkRoundInfoModels().size > 1) {
                pretendLightMsgComment(event.roundInfo.getsPkRoundInfoModels()[1].userID, event.uid, false)
            }
        } else {
            pretendLightMsgComment(event.roundInfo.userID, event.uid, false)
        }
    }

    /**
     * ??????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: GrabSomeOneLightBurstEvent) {
        if (event.roundInfo.status == EQRoundStatus.QRS_SPK_FIRST_PEER_SING.value) {
            if (event.roundInfo.getsPkRoundInfoModels().size > 0) {
                pretendLightMsgComment(event.roundInfo.getsPkRoundInfoModels()[0].userID, event.uid, true)
            }
        } else if (event.roundInfo.status == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.value) {
            if (event.roundInfo.getsPkRoundInfoModels().size > 1) {
                pretendLightMsgComment(event.roundInfo.getsPkRoundInfoModels()[1].userID, event.uid, true)
            }
        } else {
            pretendLightMsgComment(event.roundInfo.userID, event.uid, true)
        }

        val now = mRoomData.realRoundInfo
        if (now != null) {
            if (now.singBySelf()) {
                StatisticsAdapter.recordCountEvent("grab", "game_getlike", null)
            }
        }
    }

    /**
     * ?????????????????????
     *
     * @param singerId ??????????????????
     * @param uid      ???????????????
     */
    private fun pretendLightMsgComment(singerId: Int, uid: Int, isBao: Boolean) {
        val singerModel = RoomDataUtils.getPlayerInfoById(mRoomData, singerId)
        val playerInfoModel = RoomDataUtils.getPlayerInfoById(mRoomData, uid)
        MyLog.d(TAG, "pretendLightMsgComment singerId=$singerModel uid=$playerInfoModel isBao=$isBao")
        if (singerModel != null && playerInfoModel != null) {
            var isChorus = false
            var isMiniGame = false
            val now = mRoomData.realRoundInfo
            if (now != null) {
                isMiniGame = now.isMiniGameRound
                isChorus = now.isChorusRound
            }
            val commentLightModel = CommentLightModel(mRoomData.gameType, playerInfoModel, singerModel, isBao, isChorus, isMiniGame)
            EventBus.getDefault().post(PretendCommentMsgEvent(commentLightModel))
        }
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: QJoinNoticeEvent) {
        var canAdd = false
        val playerInfoModel = event.infoModel
        MyLog.d(TAG, "??????????????????,id=" + playerInfoModel!!.userID + " name=" + playerInfoModel.userInfo.nicknameRemark + " role=" + playerInfoModel.role + " roundSeq=" + event.roundSeq)
        if (playerInfoModel != null && playerInfoModel.userID.toLong() == MyUserInfoManager.uid) {
            /**
             * ???????????????????????????
             * ??????????????????bug???
             * ???????????????A?????????????????????????????????????????????waitlist?????????A???????????????????????? A ??????????????????????????????push????????????????????????
             */
            canAdd = false
        } else if (RoomDataUtils.isCurrentExpectingRound(event.roundSeq, mRoomData)) {
            val grabRoundInfoModel = mRoomData.expectRoundInfo
            if (grabRoundInfoModel != null && grabRoundInfoModel.addUser(true, playerInfoModel)) {
                canAdd = true
            }
        } else if (!mRoomData.hasGameBegin()) {
            canAdd = true
            if (mRoomData.roomType == GrabRoomType.ROOM_TYPE_PLAYBOOK) {
                mRoomData?.playbookRoomDataWhenNotStart?.addAllUser(true, event.waitUsers)
            }
        } else {
            MyLog.w(TAG, "?????????????????????,???????????????????????????userID " + event.infoModel.userID + ", seq " + event.roundSeq + "?????????????????? " + mRoomData.expectRoundInfo)
        }
        //TODO ?????????????????????????????????????????????????????? SomeOne ??????????????????????????????????????????
        if (canAdd) {
            //  ?????????????????????
            //pretendEnterRoom(playerInfoModel);
            mIGrabView.joinNotice(event.infoModel.userInfo)
        }
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: QExitGameMsgEvent) {
        if (RoomDataUtils.isCurrentExpectingRound(event.roundSeq, mRoomData)) {
            MyLog.d(TAG, "??????????????????,id=" + event.userID)
            val grabRoundInfoModel = mRoomData.expectRoundInfo
            grabRoundInfoModel!!.removeUser(true, event.userID)
        } else {
            MyLog.w(TAG, "?????????????????????,???????????????????????????userID " + event.userID + ", seq " + event.roundSeq + "?????????????????? " + mRoomData.expectRoundInfo)
        }
    }

    /**
     * ?????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: SomeOneLeavePlaySeatEvent) {
        val grabRoundInfoModel = mRoomData.realRoundInfo
        if (grabRoundInfoModel != null) {
            for (chorusRoundInfoModel in grabRoundInfoModel.chorusRoundInfoModels) {
                if (event.mPlayerInfoModel != null) {
                    if (chorusRoundInfoModel.userID == event.mPlayerInfoModel.userID) {
                        chorusRoundInfoModel.userExit()
                        pretendGiveUp(mRoomData.getPlayerOrWaiterInfo(event.mPlayerInfoModel.userID))
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
    fun onEvent(event: QCoinChangeEvent) {
        if (event.userID.toLong() == MyUserInfoManager.uid) {
            if (event.remainCoin > 0) {
                mRoomData.setCoin(event.remainCoin)
            }
            if (event.reason.value == 1) {
                pretendSystemMsg("????????????" + event.changeCoin + "????????????")
            }
        }
    }

    /**
     * ???????????????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: QChoGiveUpEvent) {
        MyLog.d(TAG, "QChoGiveUpEvent event=$event")
        val now = mRoomData.realRoundInfo
        if (now != null) {
            if (now.roundSeq == event.roundSeq) {
                now.giveUpInChorus(event.userID)
                val list = now.chorusRoundInfoModels
                if (list != null) {
                    for (chorusRoundInfoModel in list) {
                        if (chorusRoundInfoModel.userID == event.userID) {
                            val userInfoModel = mRoomData.getPlayerOrWaiterInfo(event.userID)
                            if (event.userID.toLong() == MyUserInfoManager.uid) {
                                // ?????????????????????
                                U.getToastUtil().showShort("?????????????????????")
                            } else if (now.singBySelf()) {
                                // ????????????????????????
                                if (userInfoModel != null) {
                                    U.getToastUtil().showShort(userInfoModel.nicknameRemark + "??????????????????")
                                }
                            } else {
                                // ??????????????????????????????
                                if (userInfoModel != null) {
                                    U.getToastUtil().showShort(userInfoModel.nicknameRemark + "??????????????????")
                                }
                            }
                            pretendGiveUp(userInfoModel)
                            break
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
    fun onEvent(event: QPkInnerRoundOverEvent) {
        MyLog.d(TAG, "QPkInnerRoundOverEvent event=$event")
        if (RoomDataUtils.isCurrentRunningRound(event.mRoundInfoModel.roundSeq, mRoomData)) {
            val now = mRoomData.realRoundInfo
            if (now != null) {
                now.tryUpdateRoundInfoModel(event.mRoundInfoModel, true)
                //                // PK ????????????????????? ????????????
                if (now.getsPkRoundInfoModels().size > 0) {
                    if (now.getsPkRoundInfoModels()[0].overReason == EQRoundOverReason.ROR_SELF_GIVE_UP.value) {
                        val userInfoModel = mRoomData.getPlayerOrWaiterInfo(now.getsPkRoundInfoModels()[0].userID)
                        pretendGiveUp(userInfoModel)
                    }
                }
                if (now.getsPkRoundInfoModels().size > 1) {
                    if (now.getsPkRoundInfoModels()[1].overReason == EQRoundOverReason.ROR_SELF_GIVE_UP.value) {
                        val userInfoModel = mRoomData.getPlayerOrWaiterInfo(now.getsPkRoundInfoModels()[1].userID)
                        pretendGiveUp(userInfoModel)
                    }
                }
            }
        }
    }

    private fun pretendGiveUp(userInfoModel: UserInfoModel?) {
        if (userInfoModel != null) {
            val commentModel = CommentTextModel()
            commentModel.userInfo = userInfoModel
            commentModel.avatarColor = CommentModel.AVATAR_COLOR
            val nameBuilder = SpanUtils()
                    .append(userInfoModel.nicknameRemark + " ").setForegroundColor(CommentModel.GRAB_NAME_COLOR)
                    .create()
            commentModel.nameBuilder = nameBuilder

            val stringBuilder = SpanUtils()
                    .append("?????????").setForegroundColor(CommentModel.GRAB_TEXT_COLOR)
                    .create()
            commentModel.stringBuilder = stringBuilder
            EventBus.getDefault().post(PretendCommentMsgEvent(commentModel))
        }
    }

    private fun pretendEnterRoom(playerInfoModel: GrabPlayerInfoModel) {
        val commentModel = CommentTextModel()
        commentModel.userInfo = playerInfoModel.userInfo
        commentModel.avatarColor = CommentModel.AVATAR_COLOR
        val nameBuilder = SpanUtils()
                .append(playerInfoModel.userInfo.nicknameRemark + " ").setForegroundColor(CommentModel.GRAB_NAME_COLOR)
                .create()
        commentModel.nameBuilder = nameBuilder

        val stringBuilder = when {
            playerInfoModel.userInfo.userId != UserAccountManager.SYSTEM_GRAB_ID -> {
                val spanUtils = SpanUtils()
                        .append("???????????????").setForegroundColor(CommentModel.GRAB_TEXT_COLOR)
                if (BuildConfig.DEBUG) {
                    spanUtils.append(" ?????????" + playerInfoModel.role)
                            .append(" ???????????????" + playerInfoModel.isOnline)
                }
                spanUtils.create()
            }
            else -> SpanUtils()
                    .append("?????????????????????????????????????????????????????????????????????~").setForegroundColor(CommentModel.GRAB_TEXT_COLOR)
                    .create()
        }
        commentModel.stringBuilder = stringBuilder
        EventBus.getDefault().post(PretendCommentMsgEvent(commentModel))
    }

    /**
     * ????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: QRoundOverMsgEvent) {
        MyLog.w(TAG, "?????????????????????????????????????????????push event:$event")
        ensureInRcRoom()
        //        if (mRoomData.getLastSyncTs() >= event.getInfo().getTimeMs()) {
        //            MyLog.w(TAG, "?????????????????????");
        //            return;
        //        }
        if (RoomDataUtils.isCurrentRunningRound(event.getCurrentRound().roundSeq, mRoomData)) {
            // ?????????????????????
            mRoomData.realRoundInfo!!.tryUpdateRoundInfoModel(event.currentRound, true)
            if (event.myCoin >= 0) {
                mRoomData.setCoin(event.myCoin)
            }
            if (event.totalRoundNum > 0) {
                mRoomData.grabConfigModel.totalGameRoundSeq = event.totalRoundNum
            }

            //???PK??????????????? ????????????????????? ????????????????????????
            val infoModel = mRoomData.realRoundInfo
            if (!infoModel!!.isPKRound && !infoModel.isChorusRound) {
                if (infoModel.overReason == EQRoundOverReason.ROR_SELF_GIVE_UP.value) {
                    pretendGiveUp(mRoomData.getPlayerOrWaiterInfo(infoModel.userID))
                }
            }
        }
        if (!mRoomData.hasGameBegin()) {
            MyLog.w(TAG, "?????? QRoundOverMsgEvent???????????????????????????????????????????????????")
            mRoomData.setHasGameBegin(true)
            mRoomData.expectRoundInfo = event.nextRound
            mRoomData.checkRoundInEachMode()
        } else if (RoomDataUtils.roundSeqLarger<GrabRoundInfoModel>(event.nextRound, mRoomData.expectRoundInfo)) {
            // ??????????????????
            // ??????????????????????????????????????????
            MyLog.w(TAG, "??????????????????????????????????????????")
            mRoomData.expectRoundInfo = event.nextRound
            mRoomData.checkRoundInEachMode()
        } else {
            MyLog.w(TAG, "???????????????????????????,???????????? ????????????:" + mRoomData.expectRoundInfo!!.roundSeq
                    + " push??????:" + event.currentRound.roundSeq)
        }
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: QRoundAndGameOverMsgEvent) {
        cancelSyncGameStateTask()
        if (RoomDataUtils.isCurrentRunningRound(event.roundInfoModel.roundSeq, mRoomData)) {
            // ?????????????????????
            mRoomData.realRoundInfo!!.tryUpdateRoundInfoModel(event.roundInfoModel, true)
            if (event.myCoin >= 0) {
                mRoomData.setCoin(event.myCoin)
            }
        }
        onGameOver("QRoundAndGameOverMsgEvent", event.roundOverTimeMs)
        if (event.mOverReason == EQGameOverReason.GOR_OWNER_EXIT) {
            MyLog.w(TAG, "????????????????????????????????????")
            U.getToastUtil().showLong("????????????????????????????????????")
        }
    }

    /**
     * ??????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: QSyncStatusMsgEvent) {
        if (event.getInfo().roomID != mRoomData.gameId) {
            MyLog.w(TAG, "onEvent QSyncStatusMsgEvent??? current roomid is " + mRoomData.gameId + ", event.getInfo().getRoomID() is " + event.getInfo().roomID)
            return
        }

        ensureInRcRoom()
        MyLog.w(TAG, "??????????????? sync push????????????,event.currentRound???" + event.getCurrentRound().roundSeq + ", timeMs ???" + event.info.timeMs)
        // ??????10???sync ???????????????sync ?????? 5??? sync ??????
        startSyncGameStateTask(sSyncStateTaskInterval * 2)
        updatePlayerState(event.getGameOverTimeMs()!!, event.getSyncStatusTimeMs()!!, event.getCurrentRound(), event.getInfo().roomID)
        //        fetchAcc(event.getNextRound());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: UpdateMeiliEvent) {
        // TODO: 2019-06-05 ????????????pk??????????????????
        val grabRoundInfoModel = mRoomData.realRoundInfo
        if (grabRoundInfoModel != null && grabRoundInfoModel.isPKRound) {
            for (roundInfoModel in grabRoundInfoModel.getsPkRoundInfoModels()) {
                if (roundInfoModel.userID == event.userID) {
                    roundInfoModel.meiliTotal = event.value
                    return
                }
            }
        }
    }

    private fun onGameOver(from: String, gameOverTs: Long) {
        MyLog.w(TAG, "???????????? gameOverTs=$gameOverTs from:$from")
        if (gameOverTs > mRoomData.gameStartTs && gameOverTs > mRoomData.gameOverTs) {
            cancelSyncGameStateTask()
            mRoomData.gameOverTs = gameOverTs
            mRoomData.expectRoundInfo = null
            mRoomData.checkRoundInEachMode()
        } else {
            MyLog.w(TAG, "???????????? gameOverTs ??????????????????")
        }
    }

    /**
     * ?????? ?????????????????????
     *
     * @param event
     */
    @Subscribe
    fun onEvent(event: QGameBeginEvent) {
        MyLog.d(TAG, "onEvent QGameBeginEvent !!?????????????????????push $event")
        if (mRoomData.hasGameBegin()) {
            MyLog.d(TAG, "onEvent ?????????????????????????????????true event=$event")
            mRoomData.grabConfigModel = event.mGrabConfigModel
        } else {
            mRoomData.setHasGameBegin(true)
            mRoomData.grabConfigModel = event.mGrabConfigModel
            mRoomData.expectRoundInfo = event.mInfoModel
            mRoomData.checkRoundInEachMode()
        }
        if (mRoomData.hasGameBegin()) {
            startSyncGameStateTask(sSyncStateTaskInterval)
        } else {
            cancelSyncGameStateTask()
        }
        ensureInRcRoom()
    }

    @Subscribe
    fun onEvent(event: QChangeMusicTagEvent) {
        MyLog.d(TAG, "onEvent QChangeMusicTagEvent !!???????????? $event")
        if (mRoomData.gameId == event.info.roomID) {
            pretendSystemMsg(String.format("??????????????????????????? %s ??????", event.tagName))
        }
    }

    @Subscribe
    fun onEvent(event: QChangeRoomNameEvent) {
        MyLog.d(TAG, "onEvent QChangeRoomNameEvent !!??????????????? $event")
        if (mRoomData.gameId == event.info.roomID) {
            pretendRoomNameSystemMsg(event.newName, CommentSysModel.TYPE_MODIFY_ROOM_NAME)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ActivityUtils.ForeOrBackgroundChange) {
        MyLog.w(TAG, if (event.foreground) "???????????????" else "???????????????")
        swapGame(!event.foreground, event.foreground)
        if (event.foreground) {
            muteAllRemoteAudioStreams(mRoomData.isMute, false)
            if (mRoomData.isVideoRoom) {
                ZqEngineKit.getInstance().muteLocalVideoStream(false)
            }
        } else {
            muteAllRemoteAudioStreams(true, false)
            if (mRoomData.isVideoRoom) {
                if (ZqEngineKit.getInstance().params.isAnchor) {
                    // ????????????
                    ZqEngineKit.getInstance().muteLocalVideoStream(true)
                }
            }
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MuteAllVoiceEvent) {
        MyLog.d(TAG, "onEvent event=$event")
        if (event.begin) {
            muteAllRemoteAudioStreams(true, false)
        } else {
            muteAllRemoteAudioStreams(mRoomData.isMute, false)
        }
    }

    private fun estimateOverTsThisRound(): Int {
        //        int pt = RoomDataUtils.estimateTs2End(mRoomData, mRoomData.getRealRoundInfo());
        //        MyLog.w(TAG, "?????????????????????????????????" + pt + "ms");
        return 0
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RoomNameChangeEvent) {
        MyLog.w(TAG, "onEvent event=$event")
        mRoomData.roomName = event.mRoomName
    }


    /*????????????*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MachineScoreEvent) {
        //???????????????????????????????????????????????????????????????????????????????????????????????????
        val infoModel = mRoomData.realRoundInfo
        if (infoModel != null && infoModel.singByUserId(event.userId)) {
            mIGrabView.updateScrollBarProgress(event.score, event.lineNum)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: LyricAndAccMatchManager.ScoreResultEvent) {
        val line = event.line
        val acrScore = event.acrScore
        val melpScore = event.melpScore
        val from = event.from
        if (acrScore > melpScore) {
            processScore(acrScore, line)
        } else {
            processScore(melpScore, line)
        }
    }

    internal fun processScore(score: Int, line: Int) {
        if (score < 0) {
            return
        }
        MyLog.d(TAG, "onEvent ??????=$score")
        val machineScoreItem = MachineScoreItem()
        machineScoreItem.score = score
        // ???????????????????????????
        //        long ts = ZqEngineKit.getInstance().getAudioMixingCurrentPosition();
        val ts: Long = -1
        machineScoreItem.ts = ts
        machineScoreItem.no = line
        // ??????????????????????????????
        sendScoreToOthers(machineScoreItem)
        if (mRobotScoreHelper != null) {
            mRobotScoreHelper!!.add(machineScoreItem)
        }
        mUiHandler.post { mIGrabView.updateScrollBarProgress(score, mRoomData.songLineNum) }
        //?????????????????????
        val now = mRoomData.realRoundInfo
        if (now != null) {
            /**
             * pk ??? ?????? ?????????
             */
            if (now.isPKRound || now.isNormalRound) {
                sendScoreToServer(score, line)
            }
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param machineScoreItem
     */
    private fun sendScoreToOthers(machineScoreItem: MachineScoreItem) {
        // ?????????????????????????????????????????????????????????????????????
        val msgService = ModuleServiceManager.getInstance().msgService
        if (msgService != null) {
            val ts = System.currentTimeMillis()
            val senderInfo = UserInfo.Builder()
                    .setUserID(MyUserInfoManager.uid.toInt())
                    .setNickName(MyUserInfoManager.nickName)
                    .setAvatar(MyUserInfoManager.avatar)
                    .setSex(ESex.fromValue(MyUserInfoManager.sex))
                    .setDescription("")
                    .setIsSystem(false)
                    .build()

            val now = mRoomData.realRoundInfo
            if (now != null && now.music != null) {
                val roomMsg = RoomMsg.Builder()
                        .setTimeMs(ts)
                        .setMsgType(ERoomMsgType.RM_ROUND_MACHINE_SCORE)
                        .setRoomID(mRoomData.gameId)
                        .setNo(ts)
                        .setPosType(EMsgPosType.EPT_UNKNOWN)
                        .setSender(senderInfo)
                        .setMachineScore(MachineScore.Builder()
                                .setUserID(MyUserInfoManager.uid.toInt())
                                .setNo(machineScoreItem.no)
                                .setScore(machineScoreItem.score)
                                .setItemID(now.music.itemID)
                                .setLineNum(mRoomData.songLineNum)
                                .build()
                        )
                        .build()
                val contnet = U.getBase64Utils().encode(roomMsg.toByteArray())
                msgService.sendChatRoomMessage(mRoomData.gameId.toString(), CustomMsgType.MSG_TYPE_ROOM, contnet, null)
            }
        }
    }


    /**
     * ??????????????????,??????pk????????????
     *
     * @param score
     * @param line
     */
    fun sendScoreToServer(score: Int, line: Int) {
        //score = (int) (Math.mRandom()*100);
        val map = HashMap<String, Any>()
        val infoModel = mRoomData.realRoundInfo ?: return
        map["userID"] = MyUserInfoManager.uid

        var itemID = 0
        if (infoModel.music != null) {
            itemID = infoModel.music.itemID
            if (infoModel.status == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.value) {
                val pkSong = infoModel.music.pkMusic
                if (pkSong != null) {
                    itemID = pkSong.itemID
                }
            }
        }

        map["itemID"] = itemID
        map["score"] = score
        map["no"] = line
        map["gameID"] = mRoomData.gameId
        map["mainLevel"] = 0
        map["singSecond"] = 0
        val roundSeq = infoModel.roundSeq
        map["roundSeq"] = roundSeq
        val nowTs = System.currentTimeMillis()
        map["timeMs"] = nowTs


        val sb = StringBuilder()
        sb.append("skrer")
                .append("|").append(MyUserInfoManager.uid)
                .append("|").append(itemID)
                .append("|").append(score)
                .append("|").append(line)
                .append("|").append(mRoomData.gameId)
                .append("|").append(0)
                .append("|").append(0)
                .append("|").append(roundSeq)
                .append("|").append(nowTs)
        map["sign"] = U.getMD5Utils().MD5_32(sb.toString())
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        ApiMethods.subscribe(mRoomServerApi.sendPkPerSegmentResult(body), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult) {
                if (result.errno == 0) {
                    // TODO: 2018/12/13  ??????postman??????????????? ?????????
                    MyLog.w(TAG, "????????????????????????")
                } else {
                    MyLog.w(TAG, "????????????????????????" + result.errno)
                }
            }

            override fun onError(e: Throwable) {
                MyLog.e(e)
            }
        }, this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(qKickUserReqEvent: QKickUserReqEvent) {
        MyLog.d(TAG, "onEvent qKickUserReqEvent=$qKickUserReqEvent")
        // ???????????????
        mIGrabView.showKickVoteDialog(qKickUserReqEvent.kickUserID, qKickUserReqEvent.sourceUserID)
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun onEvent(giftPresentEvent: GiftPresentEvent) {
        MyLog.d(TAG, "onEvent giftPresentEvent=$giftPresentEvent")
        EventBus.getDefault().post(GiftBrushMsgEvent(giftPresentEvent.mGPrensentGiftMsgModel))

        if (giftPresentEvent.mGPrensentGiftMsgModel.propertyModelList != null) {
            for (property in giftPresentEvent.mGPrensentGiftMsgModel.propertyModelList) {
                if (property.userID.toLong() == MyUserInfoManager.uid) {
                    if (property.coinBalance != -1f) {
                        UpdateCoinEvent.sendEvent(property.coinBalance.toInt(), property.lastChangeMs)
                    }
                    if (property.hongZuanBalance != -1f) {
                        mRoomData.setHzCount(property.hongZuanBalance, property.lastChangeMs)
                    }
                }
                if (property.curRoundSeqMeiliTotal > 0) {
                    // ????????????????????????????????????
                    EventBus.getDefault().post(UpdateMeiliEvent(property.userID, property.curRoundSeqMeiliTotal.toInt(), property.lastChangeMs))
                }
            }
        }

        if (giftPresentEvent.mGPrensentGiftMsgModel.receiveUserInfo.userId.toLong() == MyUserInfoManager.uid) {
            if (giftPresentEvent.mGPrensentGiftMsgModel.giftInfo.price <= 0) {
                StatisticsAdapter.recordCountEvent("grab", "game_getflower", null)
            } else {
                StatisticsAdapter.recordCountEvent("grab", "game_getgift", null)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(qKickUserResultEvent: QKickUserResultEvent) {
        MyLog.d(TAG, "onEvent qKickUserResultEvent=$qKickUserResultEvent")
        // ???????????????
        if (qKickUserResultEvent.kickUserID.toLong() == MyUserInfoManager.uid) {
            // ??????????????????
            if (qKickUserResultEvent.isKickSuccess) {
                if (mRoomData.ownerId == qKickUserResultEvent.sourceUserID) {
                    mIGrabView.kickBySomeOne(true)
                } else {
                    mIGrabView.kickBySomeOne(false)
                }
            }
        } else {
            // ??????????????????
            mIGrabView.dimissKickDialog()
            pretendSystemMsg(qKickUserResultEvent.kickResultContent)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: GrabSpeakingControlEvent) {
        mRoomData.isSpeaking = event.speaking
        // ???????????????
        if (event.speaking) {
            ZqEngineKit.getInstance().muteLocalAudioStream(false)
            val v = ZqEngineKit.getInstance().params.playbackSignalVolume / 4
            ZqEngineKit.getInstance().adjustPlaybackSignalVolume(v, false)
            if (mExoPlayer != null) {
                mExoPlayer!!.setVolume(mExoPlayer!!.volume * 0.0f, false)
            }
        } else {
            // ?????????
            val infoModel = mRoomData.realRoundInfo
            if (infoModel != null && infoModel.singBySelf()) {
                MyLog.d(TAG, "??????????????????????????????")
            } else {
                ZqEngineKit.getInstance().muteLocalAudioStream(true)
            }
            val v = ZqEngineKit.getInstance().params.playbackSignalVolume
            ZqEngineKit.getInstance().adjustPlaybackSignalVolume(v, false)
            if (mExoPlayer != null) {
                mExoPlayer!!.setVolume(mExoPlayer!!.volume, false)
            }
        }
    }

    class EngineParamsTemp(internal var audioVolume: Int, internal var recordVolume: Int)

    companion object {

        private val sSyncStateTaskInterval: Long = 5000

        internal val MSG_ENSURE_IN_RC_ROOM = 9// ??????????????????????????????????????????????????????

        internal val MSG_ROBOT_SING_BEGIN = 10

        internal val MSG_ENSURE_SWITCH_BROADCAST_SUCCESS = 21 // ??????????????????????????????????????????????????????????????????

        internal val MSG_RECOVER_VOLUME = 22 // ??????????????? ????????????

        internal val MSG_ENSURE_EXIT = 8 // ??????????????? ????????????

        internal val PERSON_LABEL_SAVE_PATH_FROMAT = "person_label_%s_%s_%s.m4a"
    }
}
