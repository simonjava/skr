package com.module.playways.mic.room.presenter

import android.os.Handler
import android.os.Message
import com.alibaba.fastjson.JSON
import com.common.core.account.UserAccountManager
import com.common.core.myinfo.MyUserInfoManager
import com.common.core.userinfo.model.UserInfoModel
import com.common.jiguang.JiGuangPush
import com.common.log.DebugLogView
import com.common.log.MyLog
import com.common.mvp.RxLifeCyclePresenter
import com.common.rxretrofit.ApiManager
import com.common.rxretrofit.ControlType
import com.common.rxretrofit.RequestControl
import com.common.rxretrofit.subscribe
import com.common.statistics.StatisticsAdapter
import com.common.utils.ActivityUtils
import com.common.utils.SpanUtils
import com.common.utils.U
import com.component.busilib.constans.GameModeType
import com.component.lyrics.LyricAndAccMatchManager
import com.component.lyrics.utils.SongResUtils
import com.engine.EngineEvent
import com.engine.Params
import com.engine.arccloud.RecognizeConfig
import com.module.ModuleServiceManager
import com.module.common.ICallback
import com.module.msg.CustomMsgType
import com.module.playways.BuildConfig
import com.module.playways.RoomDataUtils
import com.module.playways.mic.match.model.JoinMicRoomRspModel
import com.module.playways.mic.room.MicRoomActivity
import com.module.playways.mic.room.MicRoomData
import com.module.playways.mic.room.MicRoomServerApi
import com.module.playways.mic.room.event.MicChangeRoomEvent
import com.module.playways.mic.room.event.MicRoundChangeEvent
import com.module.playways.mic.room.event.MicRoundStatusChangeEvent
import com.module.playways.mic.room.model.MicPlayerInfoModel
import com.module.playways.mic.room.model.MicRoundInfoModel
import com.module.playways.mic.room.ui.IMicRoomView
import com.module.playways.pretendHeadSetSystemMsg
import com.module.playways.room.gift.event.GiftBrushMsgEvent
import com.module.playways.room.gift.event.UpdateCoinEvent
import com.module.playways.room.gift.event.UpdateMeiliEvent
import com.module.playways.room.msg.event.GiftPresentEvent
import com.module.playways.room.msg.event.MachineScoreEvent
import com.module.playways.room.msg.event.QChangeRoomNameEvent
import com.module.playways.room.msg.filter.PushMsgFilter
import com.module.playways.room.msg.manager.MicRoomMsgManager
import com.module.playways.room.room.comment.model.CommentModel
import com.module.playways.room.room.comment.model.CommentSysModel
import com.module.playways.room.room.comment.model.CommentTextModel
import com.module.playways.room.room.event.PretendCommentMsgEvent
import com.module.playways.room.room.score.MachineScoreItem
import com.module.playways.room.song.model.SongModel
import com.module.playways.songmanager.event.MuteAllVoiceEvent
import com.module.playways.songmanager.event.RoomNameChangeEvent
import com.orhanobut.dialogplus.DialogPlus
import com.zq.live.proto.Common.ESex
import com.zq.live.proto.Common.UserInfo
import com.zq.live.proto.GrabRoom.EMsgPosType
import com.zq.live.proto.GrabRoom.ERoomMsgType
import com.zq.live.proto.GrabRoom.MachineScore
import com.zq.live.proto.GrabRoom.RoomMsg
import com.zq.live.proto.MicRoom.*
import com.zq.mediaengine.kit.ZqEngineKit
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class MicCorePresenter(var mRoomData: MicRoomData, var roomView: IMicRoomView) : RxLifeCyclePresenter() {

    internal var mFirstKickOutTime: Long = -1 //???????????????????????????????????????????????????????????????

    internal var mAbsenTimes = 0

    internal var mRoomServerApi = ApiManager.getInstance().createService(MicRoomServerApi::class.java)

    internal var mDestroyed = false

    internal var mDialogPlus: DialogPlus? = null

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
                MSG_ENSURE_SWITCH_BROADCAST_SUCCESS -> onChangeBroadcastSuccess()
            }
        }
    }

    internal var mPushMsgFilter: PushMsgFilter<*> = PushMsgFilter<MicRoomMsg> { msg ->
        msg != null && msg.roomID == mRoomData.gameId
    }

    init {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        MicRoomMsgManager.addFilter(mPushMsgFilter)
        joinRoomAndInit(true)
        U.getFileUtils().deleteAllFiles(U.getAppInfoUtils().getSubDirPath("grab_save"))
        startSyncGameStatus()
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
            if (reInit) {
                val params = Params.getFromPref()
                //            params.setStyleEnum(Params.AudioEffect.none);
                params.scene = Params.Scene.grab
                params.isEnableAudio = true
                ZqEngineKit.getInstance().init("microom", params)
            }
            ZqEngineKit.getInstance().joinRoom(mRoomData.gameId.toString(), UserAccountManager.uuidAsLong.toInt(), false, mRoomData.agoraToken)
            // ?????????????????????, ??????????????????????????????
            ZqEngineKit.getInstance().muteLocalAudioStream(true)
        }
        joinRcRoom(-1)
        if (mRoomData.gameId > 0) {
            pretendRoomNameSystemMsg(mRoomData.roomName, CommentSysModel.TYPE_MIC_ENTER_ROOM)
            pretendHeadSetSystemMsg(mRoomData.gameType)
            for (playerInfoModel in mRoomData.getPlayerAndWaiterInfoList()) {
                if (!playerInfoModel.isOnline) {
                    continue
                }
                pretendEnterRoom(playerInfoModel)
            }
        }
        startHeartbeat()
        startSyncGameStatus()
    }

    fun changeMatchState(isChecked: Boolean) {
        launch {
            val map = mutableMapOf(
                    "roomID" to mRoomData?.gameId,
                    "matchStatus" to (if (isChecked) 2 else 1)
            )

            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
            val result = subscribe(RequestControl("$TAG changeMatchState", ControlType.CancelLast)) {
                mRoomServerApi.changeMatchStatus(body)
            }

            if (result.errno == 0) {
                if (isChecked) {
//                    val commentSysModel = CommentSysModel(GameModeType.GAME_MODE_RACE, "??????????????????????????? ???????????????????????????")
//                    EventBus.getDefault().post(PretendCommentMsgEvent(commentSysModel))
                } else {
//                    val commentSysModel = CommentSysModel(GameModeType.GAME_MODE_RACE, "??????????????????????????? ????????????????????????")
//                    EventBus.getDefault().post(PretendCommentMsgEvent(commentSysModel))
                }
            } else {
                U.getToastUtil().showShort(result.errmsg)
            }
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

    /**
     * ???ui?????????
     * ??????????????????
     */
    fun onOpeningAnimationOver() {
        // ??????????????????????????????
        mRoomData.checkRoundInEachMode()
        ensureInRcRoom()
        userStatisticForIntimacy()
    }

    /**
     * ???????????????????????????,????????????????????????
     */
    internal fun preOpWhenSelfRound() {
        var needAcc = mRoomData?.realRoundInfo?.isAccRound == true

        val p = ZqEngineKit.getInstance().params
        p.isGrabSingNoAcc = !needAcc

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
    }

    fun preOpWhenOtherRound() {
    }

    /**
     * ??????????????????????????????
     */
    fun beginSing() {
        // ???????????????????????????
        // ????????????????????????????????????
//        if (mRoomData.realRoundInfo?.isNormalRound == true) {
//            /**
//             * ??????????????????
//             */
//            val fileName = String.format(PERSON_LABEL_SAVE_PATH_FROMAT, mRoomData.gameId, mRoomData.realRoundInfo?.roundSeq)
//            val savePath = U.getAppInfoUtils().getFilePathInSubDir("grab_save", fileName)
//            ZqEngineKit.getInstance().startAudioRecording(savePath, false)
//        }
    }

    fun muteAllRemoteAudioStreams(mute: Boolean, fromUser: Boolean) {
        if (fromUser) {
            mRoomData.isMute = mute
        }
        ZqEngineKit.getInstance().muteAllRemoteAudioStreams(mute)
        // ????????????????????????
        if (mute) {
            // ???????????????
//            if (mExoPlayer != null) {
//                mExoPlayer!!.setMuteAudio(true)
//            }
        } else {
            // ??????????????????
//            if (mExoPlayer != null) {
//                mExoPlayer!!.setMuteAudio(false)
//            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param roundInfoModel
     */
    private fun onSelfRoundOver(roundInfoModel: MicRoundInfoModel) {
    }

    override fun destroy() {
        MyLog.d(TAG, "destroy begin")
        super.destroy()
        mDestroyed = true
        Params.save2Pref(ZqEngineKit.getInstance().params)
        if (!mRoomData.isHasExitGame) {
            exitRoom("destroy")
        }
        cancelSyncGameStatus()
        heartbeatJob?.cancel()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        ZqEngineKit.getInstance().destroy("microom")
        mUiHandler.removeCallbacksAndMessages(null)
        MicRoomMsgManager.removeFilter(mPushMsgFilter)
        ModuleServiceManager.getInstance().msgService.leaveChatRoom(mRoomData.gameId.toString())
        JiGuangPush.exitSkrRoomId(mRoomData.gameId.toString())
        MyLog.d(TAG, "destroy over")
    }

    /**
     * ????????????????????????
     */
    fun sendRoundOverInfo() {
        MyLog.w(TAG, "????????????????????????")
        val roundInfoModel = mRoomData.realRoundInfo
        if (roundInfoModel == null || !roundInfoModel.singBySelf()) {
            return
        }
        val map = HashMap<String, Any>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = roundInfoModel.roundSeq
        if (roundInfoModel.status == EMRoundStatus.MRS_SPK_FIRST_PEER_SING.value) {
            map["subRoundSeq"] = 0
        } else if (roundInfoModel.status == EMRoundStatus.MRS_SPK_SECOND_PEER_SING.value) {
            map["subRoundSeq"] = 1
        }

        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe { mRoomServerApi.sendRoundOver(body) }
            if (result.errno == 0) {
                MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
            } else {
                MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
            }
        }

    }


    /**
     * ??????????????????
     */
    fun giveUpSing(okCallback: (() -> Unit)?) {
        MyLog.w(TAG, "???????????????")
        val now = mRoomData.realRoundInfo
        if (now == null || !now.singBySelf()) {
            return
        }
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = now.roundSeq
        if (now.music != null) {
            map["playType"] = now?.music?.playType
        }
        if (now.status == EMRoundStatus.MRS_SPK_FIRST_PEER_SING.value) {
            map["subRoundSeq"] = 0
        } else if (now.status == EMRoundStatus.MRS_SPK_SECOND_PEER_SING.value) {
            map["subRoundSeq"] = 1
        }
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("giveUpSing", ControlType.CancelThis)) {
                mRoomServerApi.giveUpSing(body)
            }
            if (result.errno == 0) {
//                        roomView.giveUpSuccess(now.roundSeq)
                closeEngine()
                okCallback?.invoke()
                MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
            } else {
                MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
            }
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
        map["kickoutUserID"] = userId
        map["roomID"] = mRoomData.gameId

        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("reqKickUser", ControlType.CancelThis)) {
                mRoomServerApi.reqKickUser(body)
            }
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
    }

    /**
     * ????????????
     */
    fun exitRoom(from: String) {
        MyLog.w(TAG, "exitRoom from=$from")
        val map = HashMap<String, Any>()
        map["roomID"] = mRoomData.gameId
        mRoomData.isHasExitGame = true
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        // ?????? destroy ????????????
        GlobalScope.launch {
            var result = subscribe { mRoomServerApi.exitRoom(body) }
            if (result.errno == 0) {

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MicChangeRoomEvent) {
        roomView.ensureActivtyTop()
        onChangeRoomSuccess(event.mJoinGrabRoomRspModel)
    }

    private fun onChangeRoomSuccess(joinGrabRoomRspModel: JoinMicRoomRspModel?) {
        MyLog.d(TAG, "onChangeRoomSuccess joinGrabRoomRspModel=$joinGrabRoomRspModel")
        if (joinGrabRoomRspModel != null) {
//            EventBus.getDefault().post(SwitchRoomEvent())
            mRoomData.loadFromRsp(joinGrabRoomRspModel)
            joinRoomAndInit(false)
            mRoomData.checkRoundInEachMode()
            roomView.dismissKickDialog()
            roomView.invitedToOtherRoom()
        }
    }

    var heartbeatJob: Job? = null

    private fun startHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = launch {
            while (true) {
                val map = mutableMapOf(
                        "roomID" to mRoomData.gameId,
                        "userID" to MyUserInfoManager.uid
                )
                val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
                val result = subscribe { mRoomServerApi.heartbeat(body) }
                if (result.errno == 0) {

                } else {

                }
                delay(60 * 1000)
            }
        }
    }

    private fun cancelSyncGameStatus() {
        syncJob?.cancel()
    }

    var syncJob: Job? = null

    private fun startSyncGameStatus() {
        if (mRoomData.isIsGameFinish) {
            MyLog.w(TAG, "???????????????????????????Sync")
            return
        }
        syncJob?.cancel()
        syncJob = launch {
            while (true) {
                delay(10 * 1000)
                val result = subscribe { mRoomServerApi.syncStatus(mRoomData.gameId.toLong()) }
                if (result.errno == 0) {
                    val gameOverTimeMs = result.data.getLong("gameOverTimeMs")
                    if (gameOverTimeMs > 0) {
                        mRoomData.gameOverTs = gameOverTimeMs
                        DebugLogView.println(TAG, "gameOverTimeMs=${gameOverTimeMs} ??????????????????>0 ??????????????????????????????")
                        // ???????????????????????????
                        mRoomData.expectRoundInfo = null
                        mRoomData.checkRoundInEachMode()
                    } else {
                        val syncStatusTimeMs = result.data.getLong("syncStatusTimeMs")
                        if (syncStatusTimeMs > mRoomData.lastSyncTs) {
                            mRoomData.lastSyncTs = syncStatusTimeMs
                            val roundInfo = JSON.parseObject(result.data.getString("currentRound"), MicRoundInfoModel::class.java)
                            processSyncResult(roundInfo)
                        }
                    }
                } else {

                }
            }
        }
    }


    private fun processSyncResult(roundInfo: MicRoundInfoModel) {
        if (roundInfo.roundSeq == mRoomData.realRoundSeq) {
            mRoomData.realRoundInfo?.tryUpdateRoundInfoModel(roundInfo, true)
        } else if (roundInfo.roundSeq > mRoomData.realRoundSeq) {
            MyLog.w(TAG, "sync ?????????????????????????????? roundInfo ???")
            // ???????????????
            launch {
                mRoomData.expectRoundInfo = roundInfo
                mRoomData.checkRoundInEachMode()
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
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MicRoundChangeEvent) {
        MyLog.d(TAG, "MicRoundChangeEvent = $event")
        processStatusChange(1, event.lastRound, event.newRound)
    }

    /**
     * ?????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MicRoundStatusChangeEvent) {
        MyLog.d(TAG, "GrabRoundStatusChangeEvent event=$event")
        processStatusChange(2, null, event.roundInfo)
    }

    private fun processStatusChange(from: Int, lastRound: MicRoundInfoModel?, thisRound: MicRoundInfoModel?) {
        DebugLogView.println(TAG, "processStatusChange from=$from roundSeq=${thisRound?.roundSeq} statusNow=${thisRound?.status}")
        // ??????????????????????????????
        mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS)
        closeEngine()
        ZqEngineKit.getInstance().stopRecognize()
        if (thisRound == null) {
            // ???????????????
            roomView.gameOver()
            return
        }
        if (thisRound.status == EMRoundStatus.MRS_INTRO.value) {
            // ????????????
            roomView.showRoundOver(lastRound) {
                roomView.showWaiting()
            }
        } else if (thisRound.isSingStatus) {
            roomView.showRoundOver(lastRound) {
                // ????????????
                if (thisRound.singBySelf()) {
                    val size = U.getActivityUtils().activityList.size
                    var needTips = false
                    for (i in size - 1 downTo 0) {
                        val activity = U.getActivityUtils().activityList[i]
                        if (activity is MicRoomActivity) {
                            break
                        } else {
                            activity.finish()
                            needTips = true
                        }
                    }
                    if (needTips) {
                        U.getToastUtil().showLong("?????????????????????")
                    }
                    roomView.singBySelf(lastRound) {
                        preOpWhenSelfRound()
                    }
                } else {
                    preOpWhenOtherRound()
                    roomView.singByOthers(lastRound)
                }
            }
        } else if (thisRound.status == EMRoundStatus.MRS_END.value) {

        }
    }

    private fun closeEngine() {
        if (mRoomData.gameId > 0) {
            ZqEngineKit.getInstance().stopAudioMixing()
            ZqEngineKit.getInstance().stopAudioRecording()
            if (ZqEngineKit.getInstance().params.isAnchor) {
                ZqEngineKit.getInstance().setClientRole(false)
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
        } else {
            // ?????????????????????????????????????????? ????????????
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
            if (infoModel.status == EMRoundStatus.MRS_SPK_SECOND_PEER_SING.value) {
                songModel = songModel!!.pkMusic
            }
            if (songModel == null) {
                return@Runnable
            }
            // ??????????????????????????????????????????mute
//            val accFile = SongResUtils.getAccFileByUrl(songModel.acc)
            // midi????????????????????????????????????native?????????????????????????????????
            val midiFile = SongResUtils.getMIDIFileByUrl(songModel.midi)
            MyLog.d(TAG, "onChangeBroadcastSuccess ?????????????????? info=${songModel.toSimpleString()} acc=${songModel.acc} midi=${songModel.midi} accRound=${mRoomData?.realRoundInfo?.isAccRound} mRoomData.isAccEnable=${mRoomData.isAccEnable}")
            val needAcc = mRoomData?.realRoundInfo?.isAccRound == true && songModel.acc.isNotEmpty()
            if (needAcc) {
                // ??????midi
                if (midiFile != null && !midiFile.exists()) {
                    MyLog.d(TAG, "onChangeBroadcastSuccess ??????midi?????? url=${songModel.midi} => local=${midiFile.path}")
                    U.getHttpUtils().downloadFileAsync(songModel.midi, midiFile, true, null)
                }

                //  ????????????
                val songBeginTs = songModel.beginMs
//                if (accFile != null && accFile.exists()) {
//                    // ??????????????????
//                    ZqEngineKit.getInstance().startAudioMixing(MyUserInfoManager.uid.toInt(), accFile.absolutePath, midiFile.absolutePath, songBeginTs.toLong(), 1)
//                } else {
                    ZqEngineKit.getInstance().startAudioMixing(MyUserInfoManager.uid.toInt(), songModel.accWithCdnInfosJson, midiFile.absolutePath, songBeginTs.toLong(), 1)
//                }
            }
            // ??????acr????????????
            if (needAcc) {
                //??????????????????????????????acc
                ZqEngineKit.getInstance().startRecognize(RecognizeConfig.newBuilder()
                        .setSongName(songModel.itemName)
                        .setArtist(songModel.owner)
                        .setMode(RecognizeConfig.MODE_MANUAL)
                        .build())
            } else {
                // ???????????????????????????????????? acr ??????
                ZqEngineKit.getInstance().startRecognize(RecognizeConfig.newBuilder()
                        .setSongName(songModel.itemName)
                        .setArtist(songModel.owner)
                        .setMode(RecognizeConfig.MODE_AUTO)
                        .setAutoTimes(4)
                        .setMResultListener { result, list, targetSongInfo, lineNo ->
                            var mAcrScore = 0
                            if (targetSongInfo != null) {
                                mAcrScore = (targetSongInfo.score * 100).toInt()
                            }
                            EventBus.getDefault().post(LyricAndAccMatchManager.ScoreResultEvent("onChangeBroadcastSuccess", -1, mAcrScore, 0))
                        }
                        .build())
            }

        })
    }


    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MJoinNoticeMsg) {
        val playerInfoModel = MicPlayerInfoModel()
        playerInfoModel.userInfo = UserInfoModel.parseFromPB(event.userInfo)
        playerInfoModel.role = event.role.value

        mRoomData.realRoundInfo?.addUser(true, playerInfoModel)
        roomView.joinNotice(playerInfoModel)
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MExitGameMsg) {
        mRoomData.realRoundInfo?.removeUser(true, event.userID)
        val grabRoundInfoModel = mRoomData.realRoundInfo
        if (grabRoundInfoModel != null) {
            for (chorusRoundInfoModel in grabRoundInfoModel.chorusRoundInfoModels) {
                if (chorusRoundInfoModel.userID == event.userID) {
                    chorusRoundInfoModel.userExit()
                    pretendGiveUp(mRoomData.getPlayerOrWaiterInfo(event.userID))
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
    fun onEvent(event: MChangeRoomOwnerMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        mRoomData.getPlayerAndWaiterInfoList().forEach {
            if (it.userID == event.userID) {
                it.role = EMUserRole.MQUR_ROOM_OWNER.value
            } else {
                it.role = EMUserRole.MRUR_PLAY_USER.value
            }
        }

        mRoomData.ownerId = event.userID
    }

    /**
     * ???????????????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MCHOGiveUpMsg) {
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
    fun onEvent(event: MSPKInnerRoundOverMsg) {
        MyLog.d(TAG, "QPkInnerRoundOverEvent event=$event")
        var roundInfo = MicRoundInfoModel.parseFromRoundInfo(event.currentRound)
        if (RoomDataUtils.isCurrentRunningRound(roundInfo.roundSeq, mRoomData)) {
            val now = mRoomData.realRoundInfo
            if (now != null) {
                now.tryUpdateRoundInfoModel(roundInfo, true)
                //                // PK ????????????????????? ????????????
                if (now.getsPkRoundInfoModels().size > 0) {
                    if (now.getsPkRoundInfoModels()[0].overReason == EMRoundOverReason.MROR_SELF_GIVE_UP.value) {
                        val userInfoModel = mRoomData.getPlayerOrWaiterInfo(now.getsPkRoundInfoModels()[0].userID)
                        pretendGiveUp(userInfoModel)
                    }
                }
                if (now.getsPkRoundInfoModels().size > 1) {
                    if (now.getsPkRoundInfoModels()[1].overReason == EMRoundOverReason.MROR_SELF_GIVE_UP.value) {
                        val userInfoModel = mRoomData.getPlayerOrWaiterInfo(now.getsPkRoundInfoModels()[1].userID)
                        pretendGiveUp(userInfoModel)
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MAddMusicMsg) {
        MyLog.d(TAG, "MAddMusicMsg event=$event")
        roomView.showSongCount(event.musicCnt)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MDelMusicMsg) {
        MyLog.d(TAG, "MAddMusicMsg event=$event")
        roomView.showSongCount(event.musicCnt)
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

    private fun pretendEnterRoom(playerInfoModel: MicPlayerInfoModel) {
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
    fun onEvent(event: MRoundOverMsg) {
        MyLog.w(TAG, "?????????????????????????????????????????????push currentRound:${event.currentRound}")
        MyLog.w(TAG, "?????????????????????????????????????????????push nextRound:${event.nextRound}")
        ensureInRcRoom()
        roomView.showSongCount(event.musicCnt)
        var currentRound = MicRoundInfoModel.parseFromRoundInfo(event.currentRound)
        var nextRound = MicRoundInfoModel.parseFromRoundInfo(event.nextRound)
        if (currentRound.roundSeq == mRoomData.realRoundInfo?.roundSeq) {
            // ?????????????????????
            mRoomData.realRoundInfo!!.tryUpdateRoundInfoModel(currentRound, false)

            //???PK??????????????? ????????????????????? ????????????????????????
            val infoModel = mRoomData.realRoundInfo
            if (!infoModel!!.isPKRound && !infoModel.isChorusRound) {
                if (infoModel.overReason == EMRoundOverReason.MROR_SELF_GIVE_UP.value) {
                    pretendGiveUp(mRoomData.getPlayerOrWaiterInfo(infoModel.userID))
                }
            }
        }
        if (nextRound.roundSeq > (mRoomData.expectRoundInfo?.roundSeq ?: 0)) {
            // ??????????????????
            // ??????????????????????????????????????????
            MyLog.w(TAG, "nextRound.roundSeq=${nextRound.roundSeq} ??????????????????????????????????????????")
            mRoomData.expectRoundInfo = nextRound
            mRoomData.checkRoundInEachMode()
        } else {
            MyLog.w(TAG, "???????????????????????????,???????????? ????????????:" + mRoomData.expectRoundInfo?.roundSeq
                    + " push??????:" + event.currentRound.roundSeq)
        }
    }


    // TODO sync
    /**
     * ??????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MSyncStatusMsg) {
        ensureInRcRoom()
        MyLog.w(TAG, "??????????????? sync push???????????? ,event=$event")
        var thisRound = MicRoundInfoModel.parseFromRoundInfo(event.currentRound)
        // ??????10???sync ???????????????sync ?????? 5??? sync ??????
        startSyncGameStatus()
        processSyncResult(thisRound)
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
        if (event.foreground) {
            muteAllRemoteAudioStreams(mRoomData.isMute, false)
        } else {
            muteAllRemoteAudioStreams(true, false)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RoomNameChangeEvent) {
        MyLog.w(TAG, "onEvent event=$event")
        mRoomData.roomName = event.mRoomName
    }


    /*????????????*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MachineScoreEvent) {
        //???????????????????????????????????????????????????????????????????????????????????????????????????
        if (mRoomData?.realRoundInfo?.singByUserId(event.userId) == true) {
            roomView.receiveScoreEvent(event.score)
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

    private fun processScore(score: Int, line: Int) {
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
        roomView.receiveScoreEvent(score)
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
                                .setItemID(now?.music?.itemID)
//                                .setLineNum(mRoomData.songLineNum)
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
    private fun sendScoreToServer(score: Int, line: Int) {
        val map = HashMap<String, Any>()
        val infoModel = mRoomData.realRoundInfo ?: return
        map["userID"] = MyUserInfoManager.uid

        var itemID = 0
        if (infoModel.music != null) {
            itemID = infoModel?.music?.itemID ?: 0
            if (infoModel.status == EMRoundStatus.MRS_SPK_SECOND_PEER_SING.value) {
                val pkSong = infoModel?.music?.pkMusic
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
        launch {
            var result = subscribe {
                mRoomServerApi.sendPkPerSegmentResult(body)
            }
            if (result.errno == 0) {
                // TODO: 2018/12/13  ??????postman??????????????? ?????????
                MyLog.w(TAG, "????????????????????????")
            } else {
                MyLog.w(TAG, "????????????????????????" + result.errno)
            }
        }
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
                StatisticsAdapter.recordCountEvent("mic", "game_getflower", null)
            } else {
                StatisticsAdapter.recordCountEvent("mic", "game_getgift", null)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MKickoutUserMsg) {
        MyLog.d(TAG, "onEvent qKickUserResultEvent=$event")
        // ???????????????
        if (event.kickUserID.toLong() == MyUserInfoManager.uid) {
            // ??????????????????
            roomView.kickBySomeOne(true)
        } else {
            // ??????????????????
            roomView.dismissKickDialog()
            pretendSystemMsg(event.kickResultContent)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MCancelMusic) {
        MyLog.d(TAG, "onEvent MCancelMusic=$event")
        pretendSystemMsg(event.cancelMusicMsg)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MMatchStatusMsg) {
        MyLog.d(TAG, "onEvent MCancelMusic=$event")
        if (event.matchStatus.value == ERoomMatchStatus.EMMS_CLOSED.value) {
            mRoomData.matchStatusOpen = false
            pretendSystemMsg("??????????????????????????? ???????????????????????????")
        } else if (event.matchStatus.value == ERoomMatchStatus.EMMS_OPEN.value) {
            mRoomData.matchStatusOpen = true
            pretendSystemMsg("??????????????????????????? ????????????????????????")
        }
    }


    companion object {

        internal val MSG_ENSURE_IN_RC_ROOM = 9// ??????????????????????????????????????????????????????

        internal val MSG_ENSURE_SWITCH_BROADCAST_SUCCESS = 21 // ??????????????????????????????????????????????????????????????????


    }

}