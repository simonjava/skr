package com.module.playways.battle.room.presenter

import android.os.Handler
import android.os.Message
import com.alibaba.fastjson.JSON
import com.common.core.account.UserAccountManager
import com.common.core.myinfo.MyUserInfoManager
import com.common.jiguang.JiGuangPush
import com.common.log.DebugLogView
import com.common.log.MyLog
import com.common.mvp.RxLifeCyclePresenter
import com.common.player.SinglePlayer
import com.common.rxretrofit.ApiManager
import com.common.rxretrofit.ControlType
import com.common.rxretrofit.RequestControl
import com.common.rxretrofit.subscribe
import com.common.statistics.StatisticsAdapter
import com.common.utils.ActivityUtils
import com.common.utils.SpanUtils
import com.common.utils.U
import com.common.videocache.MediaCacheManager
import com.component.lyrics.LyricAndAccMatchManager
import com.component.lyrics.utils.SongResUtils
import com.engine.EngineEvent
import com.engine.Params
import com.engine.arccloud.RecognizeConfig
import com.module.ModuleServiceManager
import com.module.common.ICallback
import com.module.msg.CustomMsgType
import com.module.playways.battle.room.BattleRoomData
import com.module.playways.battle.room.BattleRoomServerApi
import com.module.playways.battle.room.event.BattleRoundChangeEvent
import com.module.playways.battle.room.event.BattleRoundStatusChangeEvent
import com.module.playways.battle.room.model.BattlePlayerInfoModel
import com.module.playways.battle.room.model.BattleRoundInfoModel
import com.module.playways.battle.room.ui.IBattleRoomView
import com.module.playways.room.data.H
import com.module.playways.room.gift.event.GiftBrushMsgEvent
import com.module.playways.room.gift.event.UpdateCoinEvent
import com.module.playways.room.gift.event.UpdateMeiliEvent
import com.module.playways.room.msg.event.GiftPresentEvent
import com.module.playways.room.msg.event.MachineScoreEvent
import com.module.playways.room.msg.filter.PushMsgFilter
import com.module.playways.room.msg.manager.BattleRoomMsgManager
import com.module.playways.room.room.comment.model.CommentModel
import com.module.playways.room.room.comment.model.CommentNoticeModel
import com.module.playways.room.room.comment.model.CommentSysModel
import com.module.playways.room.room.comment.model.CommentTextModel
import com.module.playways.room.room.event.PretendCommentMsgEvent
import com.module.playways.room.room.score.MachineScoreItem
import com.zq.live.proto.BattleRoom.*
import com.zq.live.proto.Common.ESex
import com.zq.live.proto.Common.UserInfo
import com.zq.live.proto.GrabRoom.EMsgPosType
import com.zq.live.proto.GrabRoom.ERoomMsgType
import com.zq.live.proto.GrabRoom.MachineScore
import com.zq.live.proto.GrabRoom.RoomMsg
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


class BattleCorePresenter(var mRoomData: BattleRoomData, var roomView: IBattleRoomView) : RxLifeCyclePresenter() {

    companion object {
        internal val MSG_ENSURE_IN_RC_ROOM = 9// ??????????????????????????????????????????????????????
    }

    internal var mAbsentTimes = 0

    internal var mRoomServerApi = ApiManager.getInstance().createService(BattleRoomServerApi::class.java)

    internal var mDestroyed = false


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
            }
        }
    }


    internal var mPushMsgFilter: PushMsgFilter<*> = PushMsgFilter<BattleRoomMsg> { msg ->
        msg != null && msg.roomID == mRoomData.gameId
    }

    init {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        BattleRoomMsgManager.addFilter(mPushMsgFilter)
        joinRoomAndInit(true)
        startSyncGameStatus()
    }

    /**
     * ??????????????????
     * ??????????????????
     * ??????????????????
     */
    private fun joinRoomAndInit(first: Boolean) {
        MyLog.w(TAG, "joinRoomAndInit" + " first=" + first + ", gameId is " + mRoomData.gameId)
        mAbsentTimes = 0

        if (mRoomData.gameId > 0) {
            var reInit = false
            if (first) {
                reInit = true
            }
            if (reInit) {
                val params = Params.getFromPref()
                //            params.setStyleEnum(Params.AudioEffect.none);
                params.scene = Params.Scene.doubleChat
                params.isEnableAudio = true
                ZqEngineKit.getInstance().init("battleroom", params)
            }
            // ???????????????????????????????????? ???token???????????? ???????????????
            ZqEngineKit.getInstance().joinRoom(mRoomData.gameId.toString(), UserAccountManager.uuidAsLong.toInt(), false, mRoomData.agoraToken)
            ZqEngineKit.getInstance().setClientRole(true)
        } else {
            MyLog.e(TAG, "?????????????????? mRoomData.gameId=" + mRoomData.gameId)
        }
        joinRcRoom(-1)

        if (mRoomData.gameId > 0) {
            var roundInfoModel = mRoomData.realRoundInfo
            if (roundInfoModel == null) {
                roundInfoModel = mRoomData.expectRoundInfo
            }
        }

        pretendSystemMsg("????????????????????????????????????24?????????????????????????????????????????????????????????????????????????????????")
        pretendSystemMsg("??????????????????????????????????????????????????????????????????")


        pretendTeammateMsg("????????????????????????${mRoomData?.getFirstTeammate()?.userInfo?.nicknameRemark}????????????????????????????????????~")

        startHeartbeat()
        startSyncGameStatus()
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

    private fun pretendNoticeMsg(title: String, content: String) {
        val noticeModel = CommentNoticeModel(title, content)
        EventBus.getDefault().post(PretendCommentMsgEvent(noticeModel))
    }

    private fun pretendSystemMsg(text: String) {
        val commentSysModel = CommentSysModel(mRoomData.gameType, text)
        EventBus.getDefault().post(PretendCommentMsgEvent(commentSysModel))
    }

    private fun pretendTeammateMsg(text: String) {
        val commentTextModel = CommentTextModel()
        commentTextModel.userInfo = mRoomData.getFirstTeammate()?.userInfo
        commentTextModel.stringBuilder = SpanUtils()
                .append(text).setForegroundColor(CommentModel.GRAB_SYSTEM_COLOR)
                .create()
        EventBus.getDefault().post(PretendCommentMsgEvent(commentTextModel))
    }


    /**
     * ???ui?????????
     * ??????????????????
     */
    fun onOpeningAnimationOver() {
        // ??????????????????????????????
        mRoomData.checkRoundInEachMode()
        ensureInRcRoom()
    }

    /**
     * ???????????????????????????,????????????????????????
     */
    private fun preOpWhenSelfRound() {

    }

    private fun preOpWhenOtherRound() {
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
        ZqEngineKit.getInstance().destroy("battleroom")
        mUiHandler.removeCallbacksAndMessages(null)
        BattleRoomMsgManager.removeFilter(mPushMsgFilter)
        ModuleServiceManager.getInstance().msgService.leaveChatRoom(mRoomData.gameId.toString())
        JiGuangPush.exitSkrRoomId(mRoomData.gameId.toString())
        MyLog.d(TAG, "destroy over")
        stopGuideMusic()
    }

    /**
     * ????????????????????????
     */
    fun sendRoundOverInfo() {
        if (mRoomData?.realRoundInfo?.hasSendRoundOverInfo == false) {
            MyLog.w(TAG, "????????????????????????")
            mRoomData?.realRoundInfo?.hasSendRoundOverInfo = true
            val map = HashMap<String, Any>()
            map["roomID"] = mRoomData.gameId
            map["roundSeq"] = mRoomData.realRoundInfo?.roundSeq ?: 0

            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
            launch {
                var result = subscribe { mRoomServerApi.sendRoundOver(body) }
                if (result.errno == 0) {
                    MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
                } else {
                    MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
                }
            }
        } else {
            MyLog.w(TAG, "???????????????????????????")
        }
    }


    /**
     * ??????????????????
     */
    fun giveUpSing(okCallback: (() -> Unit)?) {
        MyLog.w(TAG, "???????????????")
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = mRoomData.realRoundInfo?.roundSeq ?: 0
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("giveUpSing", ControlType.CancelThis)) {
                mRoomServerApi.giveUpSing(body)
            }
            if (result.errno == 0) {
                //closeEngine()
                okCallback?.invoke()
                MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
            } else {
                MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
            }
        }
    }

    /**
     * ??????????????????
     */
    fun overSing() {
        MyLog.w(TAG, "overSing")
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = mRoomData.realRoundInfo?.roundSeq ?: 0
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("overSing", ControlType.CancelThis)) {
                mRoomServerApi.overSing(body)
            }
            if (result.errno == 0) {
                //closeEngine()
                MyLog.w(TAG, "overSing ???????????? traceid is " + result.traceId)
            } else {
                MyLog.w(TAG, "overSing ???????????? traceid is " + result.traceId)
            }
        }
    }

    /**
     * ??????????????????
     */
    fun overWait() {
        MyLog.w(TAG, "overWait")
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = mRoomData.realRoundInfo?.roundSeq ?: 0
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("overWait", ControlType.CancelThis)) {
                mRoomServerApi.overWait(body)
            }
            if (result.errno == 0) {
                //closeEngine()
                MyLog.w(TAG, "overWait ???????????? traceid is " + result.traceId)
            } else {
                MyLog.w(TAG, "overWait ???????????? traceid is " + result.traceId)
            }
        }
    }

    /**
     * ???????????????
     */
    fun reqHelpSing() {
        MyLog.w(TAG, "reqHelpSing")
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = mRoomData.realRoundInfo?.roundSeq ?: 0
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("reqHelpSing", ControlType.CancelThis)) {
                mRoomServerApi.reqHelpSing(body)
            }
            if (result.errno == 0) {
                var cnt = result.data.getIntValue("resHelpCardCnt")
                mRoomData.config.helpCardCnt = cnt
                //closeEngine()
                MyLog.w(TAG, "reqHelpSing ?????? traceid is " + result.traceId)
            } else {
                U.getToastUtil().showShort(result.errmsg)
                MyLog.w(TAG, "reqHelpSing ?????? traceid is " + result.traceId)
            }
        }
    }

    /**
     * ???????????????
     */
    fun reqSwitchSing() {
        MyLog.w(TAG, "reqSwitchSing")
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = mRoomData.realRoundInfo?.roundSeq ?: 0
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("reqSwitchSing", ControlType.CancelThis)) {
                mRoomServerApi.reqSwitchSing(body)
            }
            if (result.errno == 0) {
                var cnt = result.data.getIntValue("resSwitchCardCnt")
                mRoomData.config.switchCardCnt = cnt
                //closeEngine()
                MyLog.w(TAG, "reqSwitchSing ?????? traceid is " + result.traceId)
            } else {
                U.getToastUtil().showShort(result.errmsg)
                MyLog.w(TAG, "reqSwitchSing ?????? traceid is " + result.traceId)
            }
        }
    }

    /**
     * ??????
     */
    fun grabSing() {
        MyLog.w(TAG, "grabSing")
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = mRoomData.realRoundInfo?.roundSeq ?: 0
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("grabSing", ControlType.CancelThis)) {
                mRoomServerApi.grabSing(body)
            }
            if (result.errno == 0) {
                //closeEngine()
                MyLog.w(TAG, "grabSing ?????? traceid is " + result.traceId)
            } else {
                U.getToastUtil().showShort(result.errmsg)
                MyLog.w(TAG, "grabSing ?????? traceid is " + result.traceId)
            }
        }
    }

    fun rspHelpSing(roundSeq: Int, op: Int) {
        MyLog.w(TAG, "rspHelpSing")
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId
        map["roundSeq"] = roundSeq
        map["helpSingType"] = op
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("rspHelpSing", ControlType.CancelThis)) {
                mRoomServerApi.rspHelpSing(body)
            }
            if (result.errno == 0) {
                //closeEngine()
                MyLog.w(TAG, "rspHelpSing ?????? traceid is " + result.traceId)
            } else {
                U.getToastUtil().showShort(result.errmsg)
                MyLog.w(TAG, "rspHelpSing ?????? traceid is " + result.traceId)
            }
        }
    }

    /**
     * ????????????
     */
    fun exitRoom(from: String) {
        MyLog.w(TAG, "exitRoom from=$from")
        if (mRoomData.isHasExitGame == false) {
            val map = HashMap<String, Any>()
            map["roomID"] = mRoomData.gameId
            map["roundSeq"] = mRoomData.realRoundInfo?.roundSeq ?: 0
            mRoomData.isHasExitGame = true
            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
            // ?????? destroy ????????????
            GlobalScope.launch {
                var result = subscribe { mRoomServerApi.userExit(body) }
                if (result.errno == 0) {

                }
            }
        }
    }

    private fun playGuideMusic() {
        val musicUrl = mRoomData.realRoundInfo?.music?.acc
        SinglePlayer.startPlay(TAG, musicUrl ?: "")
    }

    private fun stopGuideMusic() {
        SinglePlayer.stop(TAG)
    }

    var heartbeatJob: Job? = null

    /**
     * ???????????????
     */
    private fun startHeartbeat() {
        heartbeatJob?.cancel()
//        heartbeatJob = launch {
//            while (true) {
//                if (mRoomData?.getMyUserInfoInBattle()?.isHost()) {
//                    val map = mutableMapOf(
//                            "roomID" to mRoomData.gameId,
//                            "hostUserID" to MyUserInfoManager.uid
//                    )
//                    val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
//                    val result = subscribe { mRoomServerApi.heartbeat(body) }
//                    if (result.errno == 0) {
//
//                    } else {
//
//                    }
//                    delay(60 * 1000)
//                } else {
//                    heartbeatJob?.cancel()
//                    break
//                }
//            }
//        }
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
                delay(12 * 1000)
                syncGameStatusInner()
            }
        }
    }

    private fun syncGameStatusInner() {
        launch {
            val result = subscribe {
                mRoomServerApi.syncStatus(mRoomData.gameId.toLong())
            }
            if (result.errno == 0) {
                val syncStatusTimeMs = result.data.getLongValue("serverSendTimeMs")
                if (syncStatusTimeMs > mRoomData.lastSyncTs) {
                    mRoomData.lastSyncTs = syncStatusTimeMs
                    val thisRound = JSON.parseObject(result.data.getString("currentRound"), BattleRoundInfoModel::class.java)
                    val gameOverTimeMs = result.data.getLongValue("gameOverTimeMs")
                    // ??????10???sync ???????????????sync ?????? 5??? sync ??????
                    processSyncResult(false, gameOverTimeMs, thisRound)
                }
            } else {

            }
        }
    }


    /**
     * ??????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: BattleRoundChangeEvent) {
        MyLog.d(TAG, "RelayRoundChangeEvent = $event")
        processStatusChange(1, event.lastRound, event.newRound)
    }

    /**
     * ?????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: BattleRoundStatusChangeEvent) {
        MyLog.d(TAG, "RelayRoundStatusChangeEvent =$event")
        processStatusChange(2, null, event.roundInfo)
    }

    private fun processStatusChange(from: Int, lastRound: BattleRoundInfoModel?, thisRound: BattleRoundInfoModel?) {
        DebugLogView.println(TAG, "processStatusChange from=$from roundSeq=${thisRound?.roundSeq} statusNow=${thisRound?.status} lastOverReason=${lastRound?.overReason}")
        if (thisRound == null) {
            // ???????????????
            roomView.gameOver("thisRound == null")
            return
        }
        // ???????????????????????????
        closeEngine()
        for (u in mRoomData.opTeamInfo) {
            ZqEngineKit.getInstance().muteRemoteAudioStream(u.userID, true)
        }

        if (thisRound.status == EBRoundStatus.BRS_INTRO.value) {
            var r = {
                if (thisRound.userID == MyUserInfoManager.uid.toInt()) {
                    tryDownloadAccIfSelfSing()
                }
                roomView.showIntro()
                playGuideMusic()
            }
            // ????????????
            if (lastRound == null) {
                // ???????????????????????????????????????
                if (thisRound.musicSeq == 1) {
                    // ????????????????????????tips
                    roomView.showBeginTips {
                        r.invoke()
                    }
                } else {
                    r.invoke()
                }
            } else {
                // ??????????????????????????????
                if (lastRound.overReason == EBRoundOverReason.BROR_UNKNOWN.value
                        || lastRound.overReason == EBRoundOverReason.BROR_REQ_HELP_SING.value
                        || lastRound.overReason == EBRoundOverReason.BROR_REQ_SWITCH_SING.value) {
                    // ????????????????????????????????? ??????????????? ??????????????? ????????????????????????
                    if (lastRound.overReason == EBRoundOverReason.BROR_REQ_SWITCH_SING.value) {
                        if (lastRound.userID == MyUserInfoManager.uid.toInt()) {
                            pretendSystemMsg("???????????????????????????")
                        } else {
                            var helperNickName = mRoomData.getPlayerInfoById(lastRound.userID)?.userInfo?.nicknameRemark
                            pretendSystemMsg("${helperNickName}????????????????????????")
                        }
                    }
                    r.invoke()
                } else {
                    roomView.showRoundOver(lastRound) {
                        r.invoke()
                    }
                }
            }
        } else if (thisRound.status == EBRoundStatus.BRS_HELP.value) {
            if (thisRound.userID == MyUserInfoManager.uid.toInt()) {
                tryDownloadAccIfSelfSing()
            }
            if (thisRound.getHelpUserId() == MyUserInfoManager.uid.toInt()) {
                pretendSystemMsg("???????????????????????????")
            } else {
                var helperNickName = mRoomData.getPlayerInfoById(thisRound.getHelpUserId())?.userInfo?.nicknameRemark
                if (thisRound.getHelpUserId() == mRoomData.getFirstTeammate()?.userInfo?.userId) {
                    pretendSystemMsg("??????${helperNickName}????????????????????????")
                    pretendSystemMsg("??????${helperNickName}??????????????????")
                } else {
                    pretendSystemMsg("??????${helperNickName}????????????????????????")
                }
            }
            // ??????????????????
            roomView.useHelpSing()
        } else if (thisRound.status == EBRoundStatus.BRS_SING.value) {
            stopGuideMusic()
            // ??????????????????
            if (thisRound.userID == MyUserInfoManager.uid.toInt()) {
                // ??????????????????
                ZqEngineKit.getInstance().muteLocalAudioStream(false)
                roomView.showSelfSing()
                onChangeBroadcastSuccess()
            } else {
                roomView.showOtherSing()
                // ??????????????????????????????????????????
                ZqEngineKit.getInstance().muteRemoteAudioStream(thisRound.userID, false)
            }
        }
    }


    private fun tryDownloadAccIfSelfSing() {
        /**
         * ????????????????????????????????????????????????????????????????????????????????????????????????
         */
        mRoomData.realRoundInfo?.music?.let { songModel ->
            MediaCacheManager.preCache(songModel.acc)
            val midiFile = SongResUtils.getMIDIFileByUrl(songModel?.midi)
            // ??????midi
            if (midiFile != null && !midiFile.exists()) {
                MyLog.d(TAG, "onChangeBroadcastSuccess ??????midi?????? url=${songModel.midi} => local=${midiFile.path}")
                U.getHttpUtils().downloadFileAsync(songModel.midi, midiFile, true, null)
            }
        }
    }

    /**
     * ?????????????????????  ???????????????
     */
    private fun onChangeBroadcastSuccess() {
        var songModel = mRoomData?.realRoundInfo?.music
        if (songModel == null) {
            return
        }
        // ??????????????????????????????????????????mute
//            val accFile = SongResUtils.getAccFileByUrl(songModel.acc)
        // midi????????????????????????????????????native?????????????????????????????????
        val midiFile = SongResUtils.getMIDIFileByUrl(songModel?.midi)
        // ??????midi
        if (midiFile != null && !midiFile.exists()) {
            if (U.getHttpUtils().isDownloading(songModel.midi)) {

            } else {
                MyLog.d(TAG, "onChangeBroadcastSuccess ??????midi?????? url=${songModel.midi} => local=${midiFile.path}")
                U.getHttpUtils().downloadFileAsync(songModel.midi, midiFile, true, null)
            }
        }

        //  ????????????
        val songBeginTs = songModel.beginMs
        ZqEngineKit.getInstance().startAudioMixing(MyUserInfoManager.uid.toInt(), songModel.accWithCdnInfosJson, midiFile.absolutePath, songBeginTs.toLong(), 1)
        // ??????acr????????????
        //????????????????????????acr????????????
        ZqEngineKit.getInstance().startRecognize(RecognizeConfig.newBuilder()
                .setSongName(songModel.itemName)
                .setArtist(songModel.owner)
                .setMode(RecognizeConfig.MODE_MANUAL)
                .build())
    }

    private fun closeEngine() {
        if (mRoomData.gameId > 0) {
            ZqEngineKit.getInstance().stopAudioMixing()
//            mUiHandler.removeMessages(MSG_TURN_CHANGE)
//            mUiHandler.removeMessages(MSG_LAUNER_MUSIC)
//            ZqEngineKit.getInstance().stopAudioRecording()
//            if (ZqEngineKit.getInstance().params.isAnchor) {
//                ZqEngineKit.getInstance().setClientRole(false)
//            }
        }
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun onEvent(event: EngineEvent) {
        if (event.getType() == EngineEvent.TYPE_MUSIC_PLAY_STATE_CHANGE) {
//            var state = event.obj as EngineEvent.MusicStateChange
//            if (state.isPlayOk && mRoomData.realRoundInfo?.accLoadingOk == false) {
//                mRoomData.realRoundInfo?.accLoadingOk = true
//                var progress = mRoomData.getSingCurPosition()
//                DebugLogView.println(TAG, "????????????ok progress=${progress}")
//                if (progress != Long.MAX_VALUE) {
//                    if (progress > 0) {
//                        DebugLogView.println(TAG, "EngineEvent ???????????????")
//                        ZqEngineKit.getInstance().setAudioMixingPosition(progress.toInt())
//                        mUiHandler.post {
//                            realSingBegin()
//                        }
//                    } else {
//                        DebugLogView.println(TAG, "EngineEvent ????????? ${-progress}ms??? resume")
//                        ZqEngineKit.getInstance().pauseAudioMixing()
//                        mUiHandler.removeMessages(MSG_LAUNER_MUSIC)
//                        mUiHandler.sendEmptyMessageDelayed(MSG_LAUNER_MUSIC, -progress)
//                    }
//                } else {
//                    MyLog.e(TAG, "????????????????????????2")
//                }
//            }
        } else if (event.getType() == EngineEvent.TYPE_MUSIC_PLAY_FINISH) {
//            DebugLogView.println(TAG, "??????????????????")
//            sendRoundOverInfo()
        } else if (event.getType() == EngineEvent.TYPE_MUSIC_PLAY_TIME_FLY_LISTENER) {
            //DebugLogView.println(TAG, "??????????????????")
//            val timeInfo = event.getObj() as EngineEvent.MixMusicTimeInfo
//            //????????????????????????????????????????????????
//            var progress = mRoomData.getSingCurPosition()
//            val shift = progress -timeInfo.current
//            //DebugLogView.println(TAG, "???????????????????????????????????????${shift}")
//            if (abs(shift) >1000 && progress>=0) {
//                DebugLogView.println(TAG, "?????????????????????????????????????????? ???${shift}")
//                // ???????????????????????????????????????
//                ZqEngineKit.getInstance().setAudioMixingPosition(mRoomData.getSingCurPosition().toInt())
//                launcherNextTurn()
//            }
//            if(mRoomData.hasOverThisRound()){
//                sendRoundOverInfo()
//            }
        } else if (event.getType() == EngineEvent.TYPE_USER_ROLE_CHANGE) {
//            val roleChangeInfo = event.getObj<EngineEvent.RoleChangeInfo>()
//            if (roleChangeInfo.newRole == 1) {
//                val roundInfoModel = mRoomData.realRoundInfo
//                if (roundInfoModel != null && roundInfoModel.singBySelf()) {
//                    MyLog.d(TAG, "??????????????????????????????")
//                    onChangeBroadcastSuccess()
//                }
//            }
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


    private fun pretendEnterRoomMsg(playerInfoModel: BattlePlayerInfoModel) {
        val commentModel = CommentTextModel()
        commentModel.userInfo = playerInfoModel.userInfo
        commentModel.avatarColor = CommentModel.AVATAR_COLOR
        val nameBuilder =
                SpanUtils().append(playerInfoModel.userInfo.nicknameRemark + " ").setForegroundColor(CommentModel.GRAB_NAME_COLOR)
                        .create()
        commentModel.nameBuilder = nameBuilder
        val stringBuilder = SpanUtils()
                .append("???????????????").setForegroundColor(CommentModel.GRAB_TEXT_COLOR)
                .create()
        commentModel.stringBuilder = stringBuilder
        EventBus.getDefault().post(PretendCommentMsgEvent(commentModel))
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: PExitGameMsg) {
//        MyLog.d(TAG, "onEvent event = $event")
//        mRoomData.applyUserCnt = event.applyUserCnt
//        mRoomData.onlineUserCnt = event.onlineUserCnt
//        var u = BattlePlayerInfoModel.parseFromPb(event.user)
//        mRoomData.updateUser(u, null)
//    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: BSingRoundMsg) {
        MyLog.d(TAG, "onEvent event = $event")
//        event.teamResultList?.forEach {
//            it.
//        }
        val curRoundInfo = BattleRoundInfoModel.parseFromRoundInfo(event.currentRound)
        if (curRoundInfo.roundSeq == mRoomData.realRoundSeq) {
            mRoomData.realRoundInfo?.tryUpdateRoundInfoModel(curRoundInfo, true)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: BGameOverMsg) {
        MyLog.d(TAG, "onEvent event = $event")
//        event.teamResultList?.forEach {
//            it.
//        }
        mRoomData.expectRoundInfo = null
        mRoomData.checkRoundInEachMode()
    }

    /**
     * ????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: BNextRoundMsg) {
        MyLog.w(TAG, "?????????????????????????????????????????????push currentRound:${event.currentRound}")
        MyLog.w(TAG, "?????????????????????????????????????????????push nextRound:${event.nextRound}")
        ensureInRcRoom()
//        roomView.showSongCount(event.musicCnt)
        var currentRound = BattleRoundInfoModel.parseFromRoundInfo(event.currentRound)
        if (mRoomData.realRoundSeq == currentRound.roundSeq) {
            currentRound.result?.teamScore?.forEach {
                if (mRoomData?.myTeamTag == it.teamTag) {
                    // ???????????????????????????
                    mRoomData?.myTeamScore = it.teamScore
                } else {
                    // ???????????????????????????
                    mRoomData?.opTeamScore = it.teamScore
                }
            }
            mRoomData.realRoundInfo?.tryUpdateRoundInfoModel(currentRound, false)
        }

        var nextRound = BattleRoundInfoModel.parseFromRoundInfo(event.nextRound)
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

    /**
     * ??????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: BSyncMsg) {
        ensureInRcRoom()
        MyLog.w(TAG, "??????????????? sync push???????????? ,event=$event")
        if (event.syncStatusTimeMs > mRoomData.lastSyncTs) {
            mRoomData.lastSyncTs = event.syncStatusTimeMs
            var thisRound = BattleRoundInfoModel.parseFromRoundInfo(event.currentRound)
            // ??????10???sync ???????????????sync ?????? 5??? sync ??????
            startSyncGameStatus()
            processSyncResult(true, 0, thisRound)
        }
    }

    /**
     * ????????????????????????
     */
    private fun processSyncResult(fromPush: Boolean, gameOverTimeMs: Long, thisRound: BattleRoundInfoModel?) {
        mRoomData.gameOverTs = gameOverTimeMs
        if (gameOverTimeMs > 0) {
            DebugLogView.println(TAG, "gameOverTimeMs=${gameOverTimeMs} ??????????????????>0,????????????")
            mRoomData.gameOverTs = gameOverTimeMs
            // ???????????????????????????
            mRoomData.expectRoundInfo = null
            mRoomData.checkRoundInEachMode()
        } else {
            if (thisRound?.roundSeq == mRoomData.realRoundSeq) {
                mRoomData.realRoundInfo?.tryUpdateRoundInfoModel(thisRound, true)
            } else if ((thisRound?.roundSeq ?: 0) > mRoomData.realRoundSeq) {
                MyLog.w(TAG, "sync ?????????????????????????????? roundInfo ???")
                // ???????????????
                mRoomData.expectRoundInfo = thisRound
                mRoomData.checkRoundInEachMode()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ActivityUtils.ForeOrBackgroundChange) {
        MyLog.w(TAG, if (event.foreground) "???????????????" else "???????????????")
//        if (event.foreground) {
//            muteAllRemoteAudioStreams(mRoomData.isMute, false)
//        } else {
//            muteAllRemoteAudioStreams(true, false)
//        }
    }

//    /**
//     * ????????????????????????????????????????????????
//     *
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: MuteAllVoiceEvent) {
//        MyLog.d(TAG, "onEvent event=$event")
//        if (event.begin) {
//            muteAllRemoteAudioStreams(true, false)
//        } else {
//            muteAllRemoteAudioStreams(mRoomData.isMute, false)
//        }
//    }


    /*????????????*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MachineScoreEvent) {
        //???????????????????????????????????????????????????????????????????????????????????????????????????
        if (mRoomData?.realRoundInfo?.userID == event.userId) {
            roomView.receiveScoreEvent(event.score, event.lineNum)
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
        roomView.receiveScoreEvent(score, line)
        //?????????????????????
        val now = mRoomData.realRoundInfo
        if (now != null) {
            sendScoreToServer(score, line)
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
        val map = java.util.HashMap<String, Any>()
        val infoModel = mRoomData.realRoundInfo ?: return
        map["userID"] = MyUserInfoManager.uid

        var itemID = 0
        if (infoModel.music != null) {
            itemID = infoModel?.music?.itemID ?: 0
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
        map["teamTag"] = mRoomData.myTeamTag
//        map["segmentCnt"] = (mRoomData.realRoundInfo?.music?.relaySegments?.size ?: 0) + 1
        map["sentenceCnt"] = mRoomData.sentenceCnt

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
                StatisticsAdapter.recordCountEvent("party", "game_getflower", null)
            } else {
                StatisticsAdapter.recordCountEvent("party", "game_getgift", null)
            }
        }
    }

}