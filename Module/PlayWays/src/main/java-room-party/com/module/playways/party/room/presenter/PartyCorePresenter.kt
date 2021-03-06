package com.module.playways.party.room.presenter

import android.os.Handler
import android.os.Message
import com.alibaba.fastjson.JSON
import com.common.core.account.UserAccountManager
import com.common.core.myinfo.MyUserInfoManager
import com.common.core.userinfo.UserInfoManager
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
import com.component.busilib.recommend.RA
import com.engine.EngineEvent
import com.engine.Params
import com.module.ModuleServiceManager
import com.module.common.ICallback
import com.module.playways.grab.room.event.SwitchRoomEvent
import com.module.playways.party.match.model.JoinPartyRoomRspModel
import com.module.playways.party.room.PartyRoomData
import com.module.playways.party.room.PartyRoomServerApi
import com.module.playways.party.room.event.*
import com.module.playways.party.room.model.*
import com.module.playways.party.room.ui.IPartyRoomView
import com.module.playways.room.data.H
import com.module.playways.room.gift.event.GiftBrushMsgEvent
import com.module.playways.room.gift.event.UpdateCoinEvent
import com.module.playways.room.gift.event.UpdateMeiliEvent
import com.module.playways.room.msg.event.GiftPresentEvent
import com.module.playways.room.msg.filter.PushMsgFilter
import com.module.playways.room.msg.manager.PartyRoomMsgManager
import com.module.playways.room.room.comment.model.CommentModel
import com.module.playways.room.room.comment.model.CommentNoticeModel
import com.module.playways.room.room.comment.model.CommentSysModel
import com.module.playways.room.room.comment.model.CommentTextModel
import com.module.playways.room.room.event.PretendCommentMsgEvent
import com.zq.live.proto.Common.EClubMemberRoleType
import com.zq.live.proto.Common.EPUserRole
import com.zq.live.proto.PartyRoom.*
import com.zq.live.proto.broadcast.PartyDiamondbox
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


class PartyCorePresenter(var mRoomData: PartyRoomData, var roomView: IPartyRoomView) : RxLifeCyclePresenter() {

    companion object {
        internal val MSG_ENSURE_IN_RC_ROOM = 9// ??????????????????????????????????????????????????????
        internal val MSG_GET_QUICK_ANSWER_RESULT = 21// ??????????????????
    }

    internal var mAbsenTimes = 0

    internal var mRoomServerApi = ApiManager.getInstance().createService(PartyRoomServerApi::class.java)

    internal var mDestroyed = false

    //???????????????????????????
    var voteDialogTs = 0L

    var changing = false

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
                MSG_GET_QUICK_ANSWER_RESULT -> {
                    getQuickAnswerResult(msg.obj as String)
                }
            }
        }
    }


    internal var mPushMsgFilter: PushMsgFilter<*> = PushMsgFilter<PartyRoomMsg> { msg ->
        msg != null && msg.roomID == mRoomData.gameId
    }

    init {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        PartyRoomMsgManager.addFilter(mPushMsgFilter)
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
        mAbsenTimes = 0

        if (mRoomData.gameId > 0) {
            var reInit = false
            if (first) {
                reInit = true
            }
            if (reInit) {
                val params = Params.getFromPref()
                //            params.setStyleEnum(Params.AudioEffect.none);
                params.scene = Params.Scene.partyRoom
                params.isEnableAudio = true
                ZqEngineKit.getInstance().init("partyroom", params)
            }
            var isAnchor = mRoomData?.getMyUserInfoInParty()?.isRole(EPUserRole.EPUR_HOST.value, EPUserRole.EPUR_GUEST.value)
            DebugLogView.println(TAG, "isAnchor=$isAnchor")
            // ???????????????????????????????????? ???token???????????? ???????????????
            ZqEngineKit.getInstance().joinRoom(mRoomData.gameId.toString(), UserAccountManager.uuidAsLong.toInt(), false, mRoomData.agoraToken)
            ZqEngineKit.getInstance().setClientRole(isAnchor)
            // ?????????????????????, ??????????????????????????????
            if (mRoomData.getMyUserInfoInParty().isGuest()) {
                ZqEngineKit.getInstance().muteLocalAudioStream(mRoomData.isMute)
            }
        } else {
            MyLog.e(TAG, "?????????????????? mRoomData.gameId=" + mRoomData.gameId)
        }
        joinRcRoom(-1)


        if (mRoomData.gameId > 0) {
            var roundInfoModel = mRoomData.realRoundInfo
            if (roundInfoModel == null) {
                roundInfoModel = mRoomData.expectRoundInfo
            }
            val gameInfoModel = roundInfoModel?.sceneInfo
            gameInfoModel?.let {
                pretendNoticeMsg(it.rule?.ruleName ?: "", it.rule?.ruleDesc ?: "")
            }

            if (mRoomData.isClubHome()) {
                pretendNoticeMsg("????????????", "????????????${mRoomData.clubInfo?.name}????????????")
            } else {
                if (mRoomData.notice.isNotEmpty()) {
                    pretendNoticeMsg("????????????", mRoomData.notice)
                } else {
                    pretendNoticeMsg("????????????", "????????????${mRoomData.getPlayerInfoById(mRoomData.hostId)?.userInfo?.nicknameRemark}????????????")
                }
            }
        }

        pretendSystemMsg("????????????????????????????????????24?????????????????????????????????????????????????????????????????????????????????")
        pretendSystemMsg("??????????????????????????????????????????????????????????????????")

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

    //TODO ????????????
    fun selfGetSeat() {
        launch {
            val map = mutableMapOf(
                    "roomID" to mRoomData.gameId
            )
            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
            val result = subscribe(RequestControl("selfGetSeat", ControlType.CancelThis)) {
                mRoomServerApi.selfGetSeat(body)
            }
            if (result.errno == 0) {
                // todo ????????????????????????????????????
            } else {
                U.getToastUtil().showShort(result.errmsg)
            }
        }
    }

    private fun ensureInRcRoom() {
        mUiHandler.removeMessages(MSG_ENSURE_IN_RC_ROOM)
        mUiHandler.sendEmptyMessageDelayed(MSG_ENSURE_IN_RC_ROOM, (30 * 1000).toLong())
    }

    fun pretendNoticeMsg(title: String, content: String) {
        val noticeModel = CommentNoticeModel(title, content)
        EventBus.getDefault().post(PretendCommentMsgEvent(noticeModel))
    }

    fun pretendSystemMsg(text: String) {
        val commentSysModel = CommentSysModel(mRoomData.gameType, text)
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

    fun becomeClubHost(realVerifyCallBack: (() -> Unit)?) {
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("becomeClubHost", ControlType.CancelThis)) {
                mRoomServerApi.becomeClubHost(body)
            }
            if (result.errno == 0) {

            } else {
                if (result.errno == 8348054) {
                    realVerifyCallBack?.invoke()
                } else {
                    U.getToastUtil().showShort(result.errmsg)
                }
            }
        }
    }

    fun takeClubHost() {
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId
        map["curHostUserID"] = mRoomData.hostId

        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("takeClubHost", ControlType.CancelThis)) {
                mRoomServerApi.takeClubHost(body)
            }
            if (result.errno == 0) {

            } else {
                U.getToastUtil().showShort(result.errmsg)
            }
        }
    }

    fun giveClubHost() {
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId
        map["getHostUserID"] = 0

        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("giveClubHost", ControlType.CancelThis)) {
                mRoomServerApi.giveClubHost(body)
            }
            if (result.errno == 0) {

            } else {
                U.getToastUtil().showShort(result.errmsg)
            }
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
        ZqEngineKit.getInstance().destroy("partyroom")
        mUiHandler.removeCallbacksAndMessages(null)
        PartyRoomMsgManager.removeFilter(mPushMsgFilter)
        ModuleServiceManager.getInstance().msgService.leaveChatRoom(mRoomData.gameId.toString())
        JiGuangPush.exitSkrRoomId(mRoomData.gameId.toString())
        MyLog.d(TAG, "destroy over")
    }

    /**
     * ????????????????????????
     */
    fun sendRoundOverInfo() {
//        if(mRoomData?.realRoundInfo?.hasSendRoundOverInfo == false){
//            MyLog.w(TAG, "????????????????????????")
//            mRoomData?.realRoundInfo?.hasSendRoundOverInfo = true
//            val map = HashMap<String, Any>()
//            map["roomID"] = mRoomData.gameId
//            map["roundSeq"] = mRoomData.realRoundInfo?.roundSeq ?: 0
//
//            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
//            launch {
//                var result = subscribe { mRoomServerApi.sendRoundOver(body) }
//                if (result.errno == 0) {
//                    MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
//                } else {
//                    MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
//                }
//            }
//        }else{
//            MyLog.w(TAG, "???????????????????????????")
//        }
    }


    /**
     * ??????????????????
     */
    fun giveUpSing(okCallback: (() -> Unit)?) {
        MyLog.w(TAG, "???????????????")
//        val map = HashMap<String, Any?>()
//        map["roomID"] = mRoomData.gameId
//        map["roundSeq"] = mRoomData.realRoundInfo?.roundSeq ?: 0
//        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
//        launch {
//            var result = subscribe(RequestControl("giveUpSing", ControlType.CancelThis)) {
//                mRoomServerApi.giveUpSing(body)
//            }
//            if (result.errno == 0) {
//                //closeEngine()
//                okCallback?.invoke()
//                MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
//            } else {
//                MyLog.w(TAG, "???????????????????????? traceid is " + result.traceId)
//            }
//        }
    }

    fun beginQuickAnswer() {
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId

        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("beginQuickAnswer", ControlType.CancelThis)) {
                mRoomServerApi.beginQuickAnswer(body)
            }
            if (result.errno == 0) {
                U.getToastUtil().showShort("??????????????????")
                // 10????????????????????????
                mUiHandler.removeMessages(MSG_GET_QUICK_ANSWER_RESULT)
                val msg = mUiHandler.obtainMessage(MSG_GET_QUICK_ANSWER_RESULT)
                msg.obj = result.data.getString("quickAnswerTag")
                mUiHandler.sendMessageDelayed(msg, 10 * 1000)
            } else {
                U.getToastUtil().showShort(result.errmsg)
            }
        }
    }

    private fun getQuickAnswerResult(tag: String) {
        val map = HashMap<String, Any?>()
        map["roomID"] = mRoomData.gameId
        map["quickAnswerTag"] = tag
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        launch {
            var result = subscribe(RequestControl("getQuickAnswerResult", ControlType.CancelThis)) {
                mRoomServerApi.getQuickAnswerResult(body)
            }
            if (result.errno == 0) {
                var answers = JSON.parseArray(result.data.getString("answers"), PartyQuickAnswerResult::class.java)
                EventBus.getDefault().post(PartyQuickAnswerResultEvent(answers))
            } else {
                U.getToastUtil().showShort(result.errmsg)
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
            if (mRoomData.isClubHome() && mRoomData.hostId == MyUserInfoManager.uid.toInt()) {
                val map = HashMap<String, Any?>()
                map["roomID"] = mRoomData.gameId
                map["getHostUserID"] = 0
                val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
                subscribe { mRoomServerApi.giveClubHost(body) }
            }

            var result = subscribe { mRoomServerApi.exitRoom(body) }
            if (result.errno == 0) {

            }
        }
    }

    var heartbeatJob: Job? = null

    /**
     * ???????????????
     */
    private fun startHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = launch {
            while (true) {
                if (mRoomData?.getMyUserInfoInParty()?.isHost()) {
                    val map = mutableMapOf(
                            "roomID" to mRoomData.gameId,
                            "hostUserID" to MyUserInfoManager.uid
                    )
                    val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
                    val result = subscribe { mRoomServerApi.heartbeat(body) }
                    if (result.errno == 0) {

                    } else {

                    }
                    delay(60 * 1000)
                } else {
                    heartbeatJob?.cancel()
                    break
                }
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
                delay(mRoomData.config.internalTs.toLong())
                syncGameStatusInner()
            }
        }
    }

    private fun syncGameStatusInner() {
        launch {
            val result = subscribe {
                mRoomServerApi.syncStatus(mRoomData.gameId.toLong(), mRoomData?.roomType
                        ?: GameModeType.GAME_MODE_PARTY)
            }
            if (result.errno == 0) {
                val syncStatusTimeMs = result.data.getLongValue("syncStatusTimeMs")
                if (syncStatusTimeMs > mRoomData.lastSyncTs) {
                    mRoomData.lastSyncTs = syncStatusTimeMs
                    val thisRound = JSON.parseObject(result.data.getString("currentRound"), PartyRoundInfoModel::class.java)
                    val onlineUserCnt = result.data.getIntValue("onlineUserCnt")
                    val applyUserCnt = result.data.getIntValue("applyUserCnt")
                    val seats = JSON.parseArray(result.data.getString("seats"), PartySeatInfoModel::class.java)
                    var users = JSON.parseArray(result.data.getString("users"), PartyPlayerInfoModel::class.java)
                    val gameOverTimeMs = result.data.getLongValue("gameOverTimeMs")
                    // ??????10???sync ???????????????sync ?????? 5??? sync ??????
                    processSyncResult(false, gameOverTimeMs, onlineUserCnt, applyUserCnt, seats, users, thisRound)
                }
                if (result.data.getBooleanValue("mustExitRoom")) {
                    MyLog.i(TAG, "mustExitRoom == true ??????????????????")
                    roomView.gameOver()
                }
            } else {

            }
        }
    }


    /**
     * ????????????????????????????????????
     */
    private fun userStatisticForIntimacy() {
//        launch {
//            while (true) {
//                delay(10 * 60 * 1000)
//                val l1 = java.util.ArrayList<Int>()
//                for (m in mRoomData.getPlayerAndWaiterInfoList()) {
//                    if (m.userID != MyUserInfoManager.uid.toInt()) {
//                        if (mRoomData.preUserIDsSnapShots.contains(m.userID)) {
//                            l1.add(m.userID)
//                        }
//                    }
//                }
//                if (l1.isNotEmpty()) {
//                    val map = java.util.HashMap<String, Any>()
//                    map["gameID"] = mRoomData.gameId
//                    map["mode"] = GameModeType.GAME_MODE_GRAB
//                    val ts = System.currentTimeMillis()
//                    map["timeMs"] = ts
//                    map["sign"] = U.getMD5Utils().MD5_32("skrer|" + MyUserInfoManager.uid + "|" + ts)
//                    map["userID"] = MyUserInfoManager.uid
//                    map["preUserIDs"] = l1
//                    val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
//                    val result = subscribe { mRoomServerApi.userStatistic(body) }
//                    if (result.errno == 0) {
//
//                    }
//                } else {
//                    break
//                }
//            }
//        }
    }

    fun kickOut(userID: Int) {
        val map = HashMap<String, Any>()
        map["roomID"] = mRoomData.gameId
        map["kickoutUserID"] = userID
        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
        GlobalScope.launch {
            var result = subscribe { mRoomServerApi.kickout(body) }
            if (result.errno == 0) {
                // ????????????????????????
            }
        }
    }

    private var mSwitchRooming = false

    // ?????????
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
        map["gameMode"] = mRoomData.gameMode
        map["vars"] = RA.getVars()
        map["testList"] = RA.getTestList()

        launch {
            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
            var result = subscribe(RequestControl("changeRoom", ControlType.CancelThis)) {
                mRoomServerApi.changeRoom(body)
            }
            if (result.errno == 0) {
                val rspModel = JSON.parseObject(result.data!!.toJSONString(), JoinPartyRoomRspModel::class.java)
                onChangeRoomSuccess(rspModel)
            } else {
                roomView.onChangeRoomResult(false, result.errmsg)
            }
            mSwitchRooming = false
        }
    }

    /**
     * ????????? ?????? ??????????????? ????????????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PartyChangeRoomEvent) {
        if(mRoomData.gameId == event.mJoinGrabRoomRspModel.roomID ){
            //??????????????????????????????
            event.extra?.let { it as? PartyDiamondbox }?.let {
                mRoomData.partyDiamondboxModel = PartyDiamondboxModel.parseFromPB(it)
            }

            roomView.refreshDiamondBox()
        }else {
            mRoomData.loadFromRsp(event.mJoinGrabRoomRspModel)
            event.extra?.let { it as? PartyDiamondbox }?.let {
                mRoomData.partyDiamondboxModel = PartyDiamondboxModel.parseFromPB(it)
            }

            joinRoomAndInit(true)
            onOpeningAnimationOver()

            // ?????????????????? GameOver ????????????
            changing = false
        }
    }

    private fun onChangeRoomSuccess(rspModel: JoinPartyRoomRspModel?) {
        MyLog.d(TAG, "onChangeRoomSuccess JoinPartyRoomRspModel=$rspModel")
        if (rspModel != null) {
            EventBus.getDefault().post(SwitchRoomEvent())
            mRoomData.loadFromRsp(rspModel)
            joinRoomAndInit(false)
            onOpeningAnimationOver()
            roomView.onChangeRoomResult(true, null)
        }
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN, priority = Int.MAX_VALUE)
    fun onEvent(event: PartyMySeatInfoChangeEvent) {
        DebugLogView.println(TAG, "PartyMySeatInfoChangeEvent ??????${mRoomData.myUserInfo?.role} ?????? ${mRoomData.mySeatInfo}")
        if (mRoomData.mySeatInfo?.seatStatus == ESeatStatus.SS_OPEN.value) {
            // ?????????????????????
            if (!ZqEngineKit.getInstance().params.isAnchor) {
                ZqEngineKit.getInstance().setClientRole(true)
            }
            if (mRoomData.mySeatInfo?.micStatus == EMicStatus.MS_OPEN.value) {
                // ???????????????
                mRoomData.isMute = false
                ZqEngineKit.getInstance().muteLocalAudioStream(false)
                ZqEngineKit.getInstance().adjustRecordingSignalVolume(ZqEngineKit.getInstance().params.recordingSignalVolume, false)
            } else {
                mRoomData.isMute = true
                ZqEngineKit.getInstance().muteLocalAudioStream(true)
//                ZqEngineKit.getInstance().adjustRecordingSignalVolume(0, false)
            }
        } else {
            if (mRoomData.myUserInfo?.isHost() == true) {

            } else {
                // ???????????????
                ZqEngineKit.getInstance().setClientRole(false)
            }
        }
    }

    /**
     * ?????????????????????
     * ???????????????????????? ?????????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN, priority = Int.MAX_VALUE)
    fun onEvent(event: PartyMyUserInfoChangeEvent) {
        DebugLogView.println(TAG, "PartyMyUserInfoChangeEvent ??????${mRoomData.myUserInfo?.role} ?????? ${mRoomData.mySeatInfo}")
        if (mRoomData.myUserInfo?.isHost() == true) {
//            startHeartbeat()
        } else if (mRoomData.myUserInfo?.isGuest() == true) {
            // ??????????????? ????????????????????????????????????
        } else if (mRoomData.myUserInfo?.isAdmin() == true) {
            //??????????????????
        }
        if (mRoomData.myUserInfo?.isHost() == true || this.mRoomData.myUserInfo?.isGuest() == true) {
            if (!ZqEngineKit.getInstance().params.isAnchor) {
                ZqEngineKit.getInstance().setClientRole(true)
                if (!mRoomData.isMute) {
                    // ???????????????
                    ZqEngineKit.getInstance().adjustRecordingSignalVolume(ZqEngineKit.getInstance().params.recordingSignalVolume, false)
                } else {
                    ZqEngineKit.getInstance().adjustRecordingSignalVolume(0, false)
                }
            }
        } else {
            if (ZqEngineKit.getInstance().params.isAnchor) {
                ZqEngineKit.getInstance().setClientRole(false)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PartyHostChangeEvent) {
        if (mRoomData.myUserInfo?.isHost() == true) {
            startHeartbeat()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PBeginVote) {
        if (event.beginTimeMs > voteDialogTs) {
            voteDialogTs = event.beginTimeMs
            roomView.showVoteView(event)
            pretendSystemMsg(if (event.scope == EVoteScope.EVS_HOST_GUEST) "????????????????????????????????????????????????" else "??????????????????????????????")
        } else {
            MyLog.w(TAG, "PBeginVote?????????????????????")
        }
    }

    /**
     * ??????????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PartyRoundChangeEvent) {
        MyLog.d(TAG, "RelayRoundChangeEvent = $event")
        processStatusChange(1, event.lastRound, event.newRound)
    }

    /**
     * ?????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PartyRoundStatusChangeEvent) {
        MyLog.d(TAG, "RelayRoundStatusChangeEvent =$event")
        processStatusChange(2, null, event.roundInfo)
    }

    private fun processStatusChange(from: Int, lastRound: PartyRoundInfoModel?, thisRound: PartyRoundInfoModel?) {
        DebugLogView.println(TAG, "processStatusChange from=$from roundSeq=${thisRound?.roundSeq} statusNow=${thisRound?.status}")
        // ??????????????????????????????
//        mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS)
//        closeEngine()
//        ZqEngineKit.getInstance().stopRecognize()
        if (thisRound == null) {
            // ???????????????
            if (mRoomData.isClubHome()) {
                roomView.showRoundOver(lastRound) {
                    roomView.showWaiting()
                }
            } else {
                roomView.gameOver()
            }
            return
        }

        if (thisRound.status == EPRoundStatus.PRS_WAITING.value) {
            // ????????????
            roomView.showRoundOver(lastRound) {
                roomView.showWaiting()
            }

        } else if (thisRound.status == EPRoundStatus.PRS_PLAY_GAME.value) {

            roomView.showRoundOver(lastRound) {
                // ????????????
//                val size = U.getActivityUtils().activityList.size
//                var needTips = false
//                for (i in size - 1 downTo 0) {
//                    val activity = U.getActivityUtils().activityList[i]
//                    if (activity is RelayRoomActivity) {
//                        break
//                    } else {
//                        activity.finish()
//                        needTips = true
//                    }
//                }
//                if (needTips) {
//                    U.getToastUtil().showLong("?????????????????????")
//                }
                roomView.gameBegin(thisRound)
            }
        } else if (thisRound.status == EPRoundStatus.PRS_END.value) {

        }
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
        } else if (event.getType() == EngineEvent.TYPE_ENGINE_ERROR) {
            U.getToastUtil().showLong("????????????????????????????????????????????????????????????")
        } else {
            // ?????????????????????????????????????????? ????????????
        }
    }

//    /**
//     * ?????????????????????
//     */
//    private fun onChangeBroadcastSuccess() {
//        MyLog.d(TAG, "onChangeBroadcastSuccess ??????????????????")
//        mUiHandler.removeMessages(MSG_ENSURE_SWITCH_BROADCAST_SUCCESS)
//        mUiHandler.post(Runnable {
//            var songModel: SongModel? = mRoomData.realRoundInfo?.music
//            if (songModel == null) {
//                return@Runnable
//            }
//            // ??????????????????????????????????????????mute
//            val accFile = SongResUtils.getAccFileByUrl(songModel?.acc)
//            // midi????????????????????????????????????native?????????????????????????????????
//            val midiFile = SongResUtils.getMIDIFileByUrl(songModel?.midi)
//            MyLog.d(TAG, "onChangeBroadcastSuccess ?????????????????? info=${songModel.toSimpleString()} acc=${songModel.acc} midi=${songModel.midi} ")
//
//            // ??????midi
//            if (midiFile != null && !midiFile.exists()) {
//                MyLog.d(TAG, "onChangeBroadcastSuccess ??????midi?????? url=${songModel.midi} => local=${midiFile.path}")
//                U.getHttpUtils().downloadFileAsync(songModel.midi, midiFile, true, null)
//            }
//
//            //  ????????????
//            val songBeginTs = songModel.beginMs
//            if (accFile != null && accFile.exists()) {
//                // ??????????????????
//                ZqEngineKit.getInstance().startAudioMixing(MyUserInfoManager.uid.toInt(), accFile.absolutePath, midiFile.absolutePath, songBeginTs.toLong(), false, false, 1)
//            } else {
//                ZqEngineKit.getInstance().startAudioMixing(MyUserInfoManager.uid.toInt(), songModel.acc, midiFile.absolutePath, songBeginTs.toLong(), false, false, 1)
//            }
//
//        })
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: MAddMusicMsg) {
//        MyLog.d(TAG, "MAddMusicMsg event=$event")
//        roomView.showSongCount(event.musicCnt)
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: MDelMusicMsg) {
//        MyLog.d(TAG, "MAddMusicMsg event=$event")
//        roomView.showSongCount(event.musicCnt)
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PJoinNoticeMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        var playerInfoModel = PartyPlayerInfoModel.parseFromPb(event.user)
        var seatInfoModel: PartySeatInfoModel? = null
        if (event.hasSeat()) {
            seatInfoModel = PartySeatInfoModel.parseFromPb(event.seat)
        }
        roomView.joinNotice(playerInfoModel)
        mRoomData.updateUser(playerInfoModel, seatInfoModel)

        pretendEnterRoomMsg(playerInfoModel)
    }

    private fun pretendEnterRoomMsg(playerInfoModel: PartyPlayerInfoModel) {
        val commentModel = CommentTextModel()
        commentModel.userInfo = playerInfoModel.userInfo
        commentModel.avatarColor = CommentModel.AVATAR_COLOR
        val nameBuilder = when {
            mRoomData.isClubHome() -> {
                SpanUtils().append("${getIdentityName(playerInfoModel.userInfo.clubInfo.roleType)}${playerInfoModel.userInfo.nicknameRemark} ").setForegroundColor(CommentModel.GRAB_NAME_COLOR)
                        .create()
            }
            else -> {
                SpanUtils().append(playerInfoModel.userInfo.nicknameRemark + " ").setForegroundColor(CommentModel.GRAB_NAME_COLOR)
                        .create()
            }
        }
        commentModel.nameBuilder = nameBuilder
        val stringBuilder = SpanUtils()
                .append("???????????????").setForegroundColor(CommentModel.GRAB_TEXT_COLOR)
                .create()
        commentModel.stringBuilder = stringBuilder
        EventBus.getDefault().post(PretendCommentMsgEvent(commentModel))
    }

    private fun getIdentityName(roleType: Int): String {
        when (roleType) {
            EClubMemberRoleType.ECMRT_Invalid.value -> return ""
            EClubMemberRoleType.ECMRT_Founder.value -> return "????????????"
            EClubMemberRoleType.ECMRT_CoFounder.value -> return "???????????????"
            EClubMemberRoleType.ECMRT_Hostman.value -> return "?????????????????????"
            EClubMemberRoleType.ECMRT_Common.value -> return "????????????"
            else -> return ""
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PFixRoomNoticeMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        mRoomData.setNoticeKt(event.newRoomNotice, true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PartyNoticeChangeEvent) {
        pretendSystemMsg("???????????? ${mRoomData.notice}")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PChangeRoomTopicMsg) {
        mRoomData.setTopicNameKt(event.newTopic, true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PartyTopicNameChangeEvent) {
        pretendSystemMsg("??????????????????????????? ${mRoomData.topicName}")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PChangeRoomEnterPermissionMsg) {
        mRoomData.setEnterPermissionKt(event.permission.value, true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PChangeGetSeatMode) {
        mRoomData.setGetSeatModeKt(event.getSeatMode.value, true)
        if (mRoomData.getSeatMode == EGetSeatMode.EGSM_NO_APPLY.value) {
            pretendSystemMsg("???????????????????????????????????????????????????")
        } else if (mRoomData.getSeatMode == EGetSeatMode.EGSM_NEED_APPLY.value) {
            pretendSystemMsg("???????????????????????????????????????????????????")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PartyEnterPermissionChangeEvent) {
        val msg = if (mRoomData.enterPermission == 2) "?????????????????????" else "?????????????????????"
        pretendSystemMsg("???????????????????????????????????? ${msg}")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PSetRoomAdminMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        if (event.setType == ESetAdminType.SAT_ADD) {
            pretendSystemMsg("${event.user.userInfo.nickName} ??????????????????????????????")
        } else {
            pretendSystemMsg("${event.user.userInfo.nickName} ??????????????????????????????")
        }
        val p = PartyPlayerInfoModel.parseFromPb(event.user)
        mRoomData.updateUser(p, null)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PSetAllMemberMicMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        if (event.micStatus.value == EMicStatus.MS_CLOSE.value) {
            mRoomData.isAllMute = true
            pretendSystemMsg("??????????????????????????????")
        } else if (event.micStatus.value == EMicStatus.MS_OPEN.value) {
            mRoomData.isAllMute = false
            pretendSystemMsg("??????????????????????????????")
        }
        mRoomData.updateSeats(PartySeatInfoModel.parseFromPb(event.seatsList))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PSetUserMicMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        var partySeatInfoModel = PartySeatInfoModel()
        partySeatInfoModel.micStatus = event.micStatus.value
        partySeatInfoModel.seatSeq = event.seatSeq
        partySeatInfoModel.userID = event.userID
        partySeatInfoModel.seatStatus = ESeatStatus.SS_OPEN.value
        mRoomData.updateSeat(partySeatInfoModel)

        if (event.userID == MyUserInfoManager.uid.toInt() && event.opUser.userInfo.userID != MyUserInfoManager.uid.toInt()) {
            mRoomData.getPlayerInfoById(event.opUser.userInfo.userID)?.let {
                if (event.micStatus.value == EMicStatus.MS_OPEN.value) {
                    pretendSystemMsg("${if (it.isHost()) "?????????" else "?????????"} ?????????????????????????????????")
                } else {
                    pretendSystemMsg("${if (it.isHost()) "?????????" else "?????????"} ?????????????????????")
                }
            }
        }
    }

    private fun opCommentName(userID: Int): String {
        mRoomData.getPlayerInfoById(userID)?.let {
            return if (it.isHost()) "?????????" else "?????????"
        }

        return ""
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PUpdatePopularityMsg) {
        mRoomData.updatePopular(event.userID, event.popularity)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PSetSeatStatusMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        var partySeatInfoModel = PartySeatInfoModel()
        partySeatInfoModel.seatSeq = event.seatSeq
        partySeatInfoModel.seatStatus = event.seatStatus.value
        partySeatInfoModel.micStatus = mRoomData.getSeatInfoBySeq(event.seatSeq)?.micStatus ?: 0
        partySeatInfoModel.userID = mRoomData.getSeatInfoBySeq(event.seatSeq)?.userID ?: 0
        mRoomData.updateSeat(partySeatInfoModel)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PApplyForGuest) {
        MyLog.d(TAG, "onEvent event = $event")
//        if(mRoomData.myUserInfo?.isHost() == true || mRoomData.){
//        }
        if (event.cancel) {
//            pretendSystemMsg("${event.user.userInfo.nickName} ????????????")
        } else {
//            pretendSystemMsg("${event.user.userInfo.nickName} ????????????")
        }
        if (event.cancel && event.user.userInfo.userID == MyUserInfoManager.uid.toInt()) {
            // ?????????????????????
            val opPlayerInfoModel = PartyPlayerInfoModel.parseFromPb(event.opUser)
            if (opPlayerInfoModel.isHost()) {
                pretendSystemMsg("???????????????${opPlayerInfoModel.userInfo.nicknameRemark} ???????????????????????????")
            } else if (opPlayerInfoModel.isAdmin()) {
                pretendSystemMsg("???????????????${opPlayerInfoModel.userInfo.nicknameRemark} ???????????????????????????")
            }
        }

        mRoomData.applyUserCnt = event.applyUserCnt
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PGetSeatMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        mRoomData.updateUser(PartyPlayerInfoModel.parseFromPb(event.user), PartySeatInfoModel.parseFromPb(event.seatInfo))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PBackSeatMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        var partySeatInfoModel = PartySeatInfoModel()
        var n = mRoomData.getSeatInfoBySeq(event.seatSeq)
        partySeatInfoModel.seatSeq = event.seatSeq
        partySeatInfoModel.userID = 0
        partySeatInfoModel.micStatus = n?.micStatus ?: 0
        partySeatInfoModel.seatStatus = n?.seatStatus ?: 0
        mRoomData.updateUser(PartyPlayerInfoModel.parseFromPb(event.user), partySeatInfoModel)
        //TODO
        if (event.opUser.userInfo.userID != event.user.userInfo.userID) {
            if (event.user.userInfo.userID == MyUserInfoManager.uid.toInt()) {
                // ???????????????????????????
                mRoomData.getPlayerInfoById(event.opUser.userInfo.userID)?.let {
                    pretendSystemMsg("${if (it.isHost()) "?????????" else "?????????"} ??????????????????")
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PExitGameMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        mRoomData.applyUserCnt = event.applyUserCnt
        mRoomData.onlineUserCnt = event.onlineUserCnt
        var u = PartyPlayerInfoModel.parseFromPb(event.user)
        mRoomData.updateUser(u, null)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PGameOverMsg) {
        if(changing){
            MyLog.i("???????????????????????????????????????")
            return
        }
        MyLog.d(TAG, "onEvent event = $event")
        mRoomData.expectRoundInfo = null
        mRoomData.checkRoundInEachMode()
    }


    @Subscribe
    fun onEvent(event: RoomChangingEvent) {
        changing = true
    }


    /**
     * ????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PNextRoundMsg) {
        MyLog.w(TAG, "?????????????????????????????????????????????push currentRound:${event.currentRound}")
        MyLog.w(TAG, "?????????????????????????????????????????????push nextRound:${event.nextRound}")
        ensureInRcRoom()
//        roomView.showSongCount(event.musicCnt)
        var currentRound = PartyRoundInfoModel.parseFromRoundInfo(event.currentRound)
        var nextRound = PartyRoundInfoModel.parseFromRoundInfo(event.nextRound)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PClubBecomeHostMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        val model = PartyPlayerInfoModel.parseFromPb(event.user)
        mRoomData.updateUser(model, null)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PClubChangeHostMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        // ??????????????????
        if (event.hasToUser() && (event.toUser.userInfo?.userID ?: 0) > 0) {
            mRoomData.updateUser(PartyPlayerInfoModel.parseFromPb(event.toUser), null)
        }
        val fromModel = PartyPlayerInfoModel.parseFromPb(event.fromUser)
        mRoomData.updateUser(fromModel, null)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PClubGameStopMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        mRoomData.expectRoundInfo = null
        mRoomData.checkRoundInEachMode()
    }

    // ??????
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PRoomWarningMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        if (mRoomData.myUserInfo?.isHost() == true || mRoomData.myUserInfo?.isAdmin() == true) {
            roomView.showWarningDialog(event.warningMsg)
        }
    }

    // ??????
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PRoomLockedMsg) {
        MyLog.d(TAG, "onEvent event = $event")
        roomView.showWarningDialog(event.msg)
    }

    // ????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PBeginQuickAnswer) {
        MyLog.d(TAG, "onEvent event = $event")
        if (mRoomData.myUserInfo?.isGuest() == true || mRoomData.myUserInfo?.isHost() == true) {
            H.partyRoomData?.quickAnswerTag = event.quickAnswerTag
            roomView.beginQuickAnswer(event.beginTimeMs, event.endTimeMs)
        }
        pretendSystemMsg("??????????????????????????????????????????")
    }


    // ????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PResultQuickAnswer) {
        MyLog.d(TAG, "onEvent event = $event")
        mUiHandler.removeMessages(MSG_GET_QUICK_ANSWER_RESULT)
        val answers = ArrayList<PartyQuickAnswerResult>()
        val sb = StringBuilder()
        for (an in event.answersList) {
            val anp = PartyQuickAnswerResult.parseFromPb(an)
            answers.add(anp)
            sb.append(anp.seq).append(".").append(anp.user?.userInfo?.nicknameRemark).append("\n")
        }
        if (sb.toString().isNotEmpty()) {
            sb.insert(0, "????????????:\n")
        } else {
            sb.append("????????????:\n????????????")
        }
        pretendSystemMsg(sb.toString())
        EventBus.getDefault().post(PartyQuickAnswerResultEvent(answers))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PResultVote) {
        pretendSystemMsg("???????????????" + "\n" +
                "${UserInfoManager.getInstance().getRemarkName(event.voteInfosList[0].user.userInfo.userID, event.voteInfosList[0].user.userInfo.nickName)} ${event.voteInfosList[0].voteCnt}???" + "\n" +
                "${UserInfoManager.getInstance().getRemarkName(event.voteInfosList[1].user.userInfo.userID, event.voteInfosList[1].user.userInfo.nickName)} ${event.voteInfosList[1].voteCnt}???")
    }

    // TODO sync
    /**
     * ??????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PSyncMsg) {
        ensureInRcRoom()
        MyLog.w(TAG, "??????????????? sync push???????????? ,event=$event")
        if (event.syncStatusTimeMs > mRoomData.lastSyncTs) {
            mRoomData.lastSyncTs = event.syncStatusTimeMs
            var onlineUserCnt = event.onlineUserCnt
            var applyUserCnt = event.applyUserCnt

            var seats = PartySeatInfoModel.parseFromPb(event.seatsList)
            var users = PartyPlayerInfoModel.parseFromPb(event.usersList)
            var thisRound = PartyRoundInfoModel.parseFromRoundInfo(event.currentRound)
            // ??????10???sync ???????????????sync ?????? 5??? sync ??????
            startSyncGameStatus()
            processSyncResult(true, 0, onlineUserCnt, applyUserCnt, seats, users, thisRound)
        }
    }

    /**
     * ????????????????????????
     */
    private fun processSyncResult(fromPush: Boolean, gameOverTimeMs: Long, onlineUserCnt: Int, applyUserCnt: Int, seats: List<PartySeatInfoModel>?, users: List<PartyPlayerInfoModel>?, thisRound: PartyRoundInfoModel?) {
        mRoomData.gameOverTs = gameOverTimeMs
        mRoomData.onlineUserCnt = onlineUserCnt
        mRoomData.applyUserCnt = applyUserCnt
        mRoomData.updateUsers(users as ArrayList<PartyPlayerInfoModel>?)
        if (gameOverTimeMs > 0) {
            mRoomData.emptySeats()
            if (mRoomData.isClubHome()) {
                DebugLogView.println(TAG, "gameOverTimeMs=${gameOverTimeMs} ??????????????????>0,")
            } else {
                DebugLogView.println(TAG, "gameOverTimeMs=${gameOverTimeMs} ??????????????????>0,????????????")
            }
            mRoomData.gameOverTs = gameOverTimeMs
            // ???????????????????????????
            mRoomData.expectRoundInfo = null
            mRoomData.checkRoundInEachMode()
        } else {
            mRoomData.updateSeats(seats as ArrayList<PartySeatInfoModel>)
            if (thisRound?.roundSeq == mRoomData.realRoundSeq) {
                mRoomData.realRoundInfo?.tryUpdateRoundInfoModel(thisRound, true)
            } else if ((thisRound?.roundSeq ?: 0) > mRoomData.realRoundSeq) {
                MyLog.w(TAG, "sync ?????????????????????????????? roundInfo ???")
                // ???????????????
                if (fromPush && thisRound?.sceneInfo == null && thisRound?.status == EPRoundStatus.PRS_PLAY_GAME.value) {
                    MyLog.w(TAG, "pushSync?????????????????????,????????????sync")
                    syncGameStatusInner()
                } else {
                    launch {
                        mRoomData.expectRoundInfo = thisRound
                        mRoomData.checkRoundInEachMode()
                    }
                }
            }
        }
    }

//    @Subscribe
//    fun onEvent(event: QChangeRoomNameEvent) {
//        MyLog.d(TAG, "onEvent QChangeRoomNameEvent !!??????????????? $event")
//        if (mRoomData.gameId == event.info.roomID) {
//            pretendRoomNameSystemMsg(event.newName, CommentSysModel.TYPE_MODIFY_ROOM_NAME)
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ActivityUtils.ForeOrBackgroundChange) {
        MyLog.w(TAG, if (event.foreground) "???????????????" else "???????????????")
//        if (event.foreground) {
//            muteAllRemoteAudioStreams(mRoomData.isMute, false)
//        } else {
//            muteAllRemoteAudioStreams(true, false)
//        }
    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: RGameOverMsg) {
//        ensureInRcRoom()
//        roomView.gameOver()
//    }


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

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: MachineScoreEvent) {
//        //???????????????????????????????????????????????????????????????????????????????????????????????????
//        if (mRoomData?.realRoundInfo?.singByUserId(event.userId) == true) {
//            roomView.receiveScoreEvent(event.score)
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: LyricAndAccMatchManager.ScoreResultEvent) {
//        val line = event.line
//        val acrScore = event.acrScore
//        val melpScore = event.melpScore
//        val from = event.from
//        if (acrScore > melpScore) {
//            processScore(acrScore, line)
//        } else {
//            processScore(melpScore, line)
//        }
//    }
//
//    private fun processScore(score: Int, line: Int) {
//        if (score < 0) {
//            return
//        }
//        MyLog.d(TAG, "onEvent ??????=$score")
//        val machineScoreItem = MachineScoreItem()
//        machineScoreItem.score = score
//        // ???????????????????????????
//        //        long ts = ZqEngineKit.getInstance().getAudioMixingCurrentPosition();
//        val ts: Long = -1
//        machineScoreItem.ts = ts
//        machineScoreItem.no = line
//        // ??????????????????????????????
//        sendScoreToOthers(machineScoreItem)
//        roomView.receiveScoreEvent(score)
//        //?????????????????????
//        val now = mRoomData.realRoundInfo
//        if (now != null) {
//            /**
//             * pk ??? ?????? ?????????
//             */
//            if (now.isPKRound || now.isNormalRound) {
//                sendScoreToServer(score, line)
//            }
//        }
//    }
//
//    /**
//     * ?????????????????????????????????
//     *
//     * @param machineScoreItem
//     */
//    private fun sendScoreToOthers(machineScoreItem: MachineScoreItem) {
//        // ?????????????????????????????????????????????????????????????????????
//        val msgService = ModuleServiceManager.getInstance().msgService
//        if (msgService != null) {
//            val ts = System.currentTimeMillis()
//            val senderInfo = UserInfo.Builder()
//                    .setUserID(MyUserInfoManager.uid.toInt())
//                    .setNickName(MyUserInfoManager.nickName)
//                    .setAvatar(MyUserInfoManager.avatar)
//                    .setSex(ESex.fromValue(MyUserInfoManager.sex))
//                    .setDescription("")
//                    .setIsSystem(false)
//                    .build()
//
//            val now = mRoomData.realRoundInfo
//            if (now != null && now.music != null) {
//                val roomMsg = RoomMsg.Builder()
//                        .setTimeMs(ts)
//                        .setMsgType(ERoomMsgType.RM_ROUND_MACHINE_SCORE)
//                        .setRoomID(mRoomData.gameId)
//                        .setNo(ts)
//                        .setPosType(EMsgPosType.EPT_UNKNOWN)
//                        .setSender(senderInfo)
//                        .setMachineScore(MachineScore.Builder()
//                                .setUserID(MyUserInfoManager.uid.toInt())
//                                .setNo(machineScoreItem.no)
//                                .setScore(machineScoreItem.score)
//                                .setItemID(now?.music?.itemID)
////                                .setLineNum(mRoomData.songLineNum)
//                                .build()
//                        )
//                        .build()
//                val contnet = U.getBase64Utils().encode(roomMsg.toByteArray())
//                msgService.sendChatRoomMessage(mRoomData.gameId.toString(), CustomMsgType.MSG_TYPE_ROOM, contnet, null)
//            }
//        }
//    }
//
//
//    /**
//     * ??????????????????,??????pk????????????
//     *
//     * @param score
//     * @param line
//     */
//    private fun sendScoreToServer(score: Int, line: Int) {
//        val map = HashMap<String, Any>()
//        val infoModel = mRoomData.realRoundInfo ?: return
//        map["userID"] = MyUserInfoManager.uid
//
//        var itemID = 0
//        if (infoModel.music != null) {
//            itemID = infoModel?.music?.itemID ?: 0
//            if (infoModel.status == EMRoundStatus.MRS_SPK_SECOND_PEER_SING.value) {
//                val pkSong = infoModel?.music?.pkMusic
//                if (pkSong != null) {
//                    itemID = pkSong.itemID
//                }
//            }
//        }
//
//        map["itemID"] = itemID
//        map["score"] = score
//        map["no"] = line
//        map["gameID"] = mRoomData.gameId
//        map["mainLevel"] = 0
//        map["singSecond"] = 0
//        val roundSeq = infoModel.roundSeq
//        map["roundSeq"] = roundSeq
//        val nowTs = System.currentTimeMillis()
//        map["timeMs"] = nowTs
//
//
//        val sb = StringBuilder()
//        sb.append("skrer")
//                .append("|").append(MyUserInfoManager.uid)
//                .append("|").append(itemID)
//                .append("|").append(score)
//                .append("|").append(line)
//                .append("|").append(mRoomData.gameId)
//                .append("|").append(0)
//                .append("|").append(0)
//                .append("|").append(roundSeq)
//                .append("|").append(nowTs)
//        map["sign"] = U.getMD5Utils().MD5_32(sb.toString())
//        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))
//        launch {
//            var result = subscribe {
//                mRoomServerApi.sendPkPerSegmentResult(body)
//            }
//            if (result.errno == 0) {
//                // TODO: 2018/12/13  ??????postman??????????????? ?????????
//                MyLog.w(TAG, "????????????????????????")
//            } else {
//                MyLog.w(TAG, "????????????????????????" + result.errno)
//            }
//        }
//    }

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