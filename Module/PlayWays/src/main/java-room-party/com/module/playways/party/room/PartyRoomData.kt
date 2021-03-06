package com.module.playways.party.room

import com.common.core.myinfo.MyUserInfo
import com.common.core.myinfo.MyUserInfoManager
import com.common.core.userinfo.model.ClubInfo
import com.common.log.MyLog
import com.component.busilib.constans.GameModeType
import com.module.playways.BaseRoomData
import com.module.playways.party.match.model.JoinPartyRoomRspModel
import com.module.playways.party.room.event.*
import com.module.playways.party.room.model.*
import com.module.playways.room.data.H
import com.module.playways.room.prepare.model.PlayerInfoModel
import com.zq.live.proto.PartyRoom.EMicStatus
import com.zq.live.proto.PartyRoom.EPRoundStatus
import com.zq.live.proto.Common.EPUserRole
import com.zq.live.proto.Common.PBeginDiamondbox
import com.zq.live.proto.PartyRoom.ESeatStatus
import org.greenrobot.eventbus.EventBus


class PartyRoomData : BaseRoomData<PartyRoundInfoModel>() {

    var clubInfo: ClubInfo? = null   // 家族信息

    var partyDiamondboxModel:PartyDiamondboxModel? = null //宝箱信息

    var gameMode = 0 //  ERM_SING_PK = 1 : K歌 模式 - ERM_GMAE_PK = 2 : 游戏PK 模式 - ERM_MAKE_FRIEND = 3 : 相亲交友 模式string

    var getSeatMode = 0 // 0 需要申请上麦，1不需要申请上麦
        private set

    fun setGetSeatModeKt(getSeatMode:Int, notice:Boolean){
        this.getSeatMode = getSeatMode
        if(notice){
            EventBus.getDefault().post(PartyRoomSeatModeChangeEvent())
        }
    }

    var quickAnswerTag: String? = null // 当前抢答的标识
    var bgmPlayingPath: String? = null // 背景音乐的播放路径

    var joinSrc: Int = 0  //标记来源  列表 JRS_LIST = 1; 邀请 JRS_INVITE = 2; 断线重连 JRS_RECONNECT  = 3; 快速进入 JRS_QUICK_JOIN = 4;

    var config = PartyConfigModel()

    var isAllMute = false // 是否设置了全员禁麦
        //房间名称
        set(value) {
            if (value != field) {
                field = value
                EventBus.getDefault().post(PartyRoomAllMuteEvent())
            }
        }

    //房间名称
    var roomName = ""
        private set

    fun setRoomNameKt(roomName:String, notice:Boolean){
        this.roomName = roomName
        if(notice){
            EventBus.getDefault().post(PartyRoomNameChangeEvent())
        }
    }

    //房间主题
    var topicName = ""
        private set

    fun setTopicNameKt(topicName:String, notice:Boolean){
        this.topicName = topicName
        if(notice){
            EventBus.getDefault().post(PartyTopicNameChangeEvent())
        }
    }

    // 房间公告
    var notice = ""
        private set

    fun setNoticeKt(notice:String, notify:Boolean){
        this.notice = notice
        if(notify){
            EventBus.getDefault().post(PartyNoticeChangeEvent())
        }
    }

    var onlineUserCnt = 0 //在线人数
        set(value) {
            if (value != field) {
                field = value
                EventBus.getDefault().post(PartyOnlineUserCntChangeEvent())
            }
        }
    var applyUserCnt = 0 //申请人数
        set(value) {
            if (value != field) {
                field = value
                EventBus.getDefault().post(PartyApplyUserCntChangeEvent())
            }
        }

    var enterPermission = 2 // 2都可以进入  1 只有邀请能进
        private set

    fun setEnterPermissionKt(enterPermission:Int, notify:Boolean){
        this.enterPermission = enterPermission
        if(notify){
            EventBus.getDefault().post(PartyEnterPermissionChangeEvent())
        }
    }


    var roomType: Int? = 0 // 1是个人房 2是家族房

    private var users = ArrayList<PartyPlayerInfoModel>() // 当前的用户信息 包括 主持人管理员 以及 嘉宾
    private var usersMap = HashMap<Int, PartyPlayerInfoModel>()  // 根据id找人

    private var seats = ArrayList<PartySeatInfoModel>() // 座位信息
    private var seatsSeatIdMap = HashMap<Int, PartySeatInfoModel>() // 根据座位id找座位
    private var seatsUserIdMap = HashMap<Int, PartySeatInfoModel>() // 根据用户id找座位

    // 题目信息在轮次信息里 轮次信息在父类的 realRoundInfo 中
    var hostId = 0 //主持人id
        private set(value) {
            if (field != value) {
                field = value
                EventBus.getDefault().post(PartyHostChangeEvent(field))
                if (field == MyUserInfoManager.uid.toInt()) {
                    EventBus.getDefault().post(PartyMyUserInfoChangeEvent(null))
                }
                if (field == 0) {
                    isAllMute = false
                }
            }
        }
    /**
     * 本人的用户信息
     */
    var myUserInfo: PartyPlayerInfoModel? = null
        private set(value) {
            if (field == null && value == null) {

            } else {
                if (field != null && value != null) {
                    if (field!!.same(value)) {

                    } else {
                        val oldUser = field
                        field = value
                        EventBus.getDefault().post(PartyMyUserInfoChangeEvent(oldUser))
                    }
                } else {
                    val oldUser = field
                    field = value
                    EventBus.getDefault().post(PartyMyUserInfoChangeEvent(oldUser))
                }
            }
        }

    /**
     * 本人的座位信息
     */
    var mySeatInfo: PartySeatInfoModel? = null
        private set(value) {
//            MyLog.w("chengsimin", "value=$value field=$field")
            if (field == null && value == null) {

            } else {
                if (field != null && value != null) {
                    if (field!!.same(value)) {
//                        MyLog.w("chengsimin", "same")
                    } else {
//                        MyLog.w("chengsimin", "not same")
                        field = value
                        EventBus.getDefault().post(PartyMySeatInfoChangeEvent())
                    }
                } else {
//                    MyLog.w("chengsimin", "not same")
                    field = value
                    EventBus.getDefault().post(PartyMySeatInfoChangeEvent())
                }
            }
        }

    override fun getPlayerAndWaiterInfoList(): List<PlayerInfoModel> {
        return users
    }

    override fun getInSeatPlayerInfoList(): List<PlayerInfoModel> {
        return users
    }

    override fun getCanGiveGiftList(): List<PlayerInfoModel> {
        var list = ArrayList<PlayerInfoModel>()
        if (H.partyRoomData?.hostId!! > 0) {
            getPlayerInfoById(H.partyRoomData?.hostId!!)?.let { model ->
                list.add(model)
            }
        }

        this.seats?.let {
            for (info in it) {
                getPlayerInfoById(info.userID)?.let { model ->
                    list.add(model)
                }
            }
        }

        return list
    }

    override val gameType: Int
        get() = GameModeType.GAME_MODE_PARTY


    init {
        syncServerTs()
    }

    /**
     * 根据 userId 找 用户信息
     * 找不到观众的
     */
    fun getPlayerInfoById(userId: Int): PartyPlayerInfoModel? {
        return usersMap[userId]
    }

    /**
     * 根据 座位编号 找 座位信息
     */
    fun getSeatInfoBySeq(seatSeq: Int): PartySeatInfoModel? {
        return seatsSeatIdMap[seatSeq]
    }

    fun isPersonalHome(): Boolean {
        return roomType == 1
    }

    fun isClubHome(): Boolean {
        return roomType == 2
    }

    /**
     * 根据 userId 找 座位信息
     */
    fun getSeatInfoByUserId(userId: Int): PartySeatInfoModel? {
        return seatsUserIdMap[userId]
    }

    /**
     * 根据 座位编号 找 用户信息
     */
    fun getPlayerInfoBySeq(seatSeq: Int): PartyPlayerInfoModel? {
        var seatInfo = seatsSeatIdMap[seatSeq]
        seatInfo?.userID?.let {
            return getPlayerInfoById(it)
        }
        return null
    }

    /**
     *  座位信息 key为座位序号  value 的座位状态 和该位置上的嘉宾信息
     */
    fun getSeatInfoMap(): HashMap<Int, PartyActorInfoModel> {
        var seatsMap = HashMap<Int, PartyActorInfoModel>()
        this.seats?.let {
            for (info in it) {
                var partyActorInfoModel = PartyActorInfoModel()
                partyActorInfoModel.seat = info
                if (info.userID > 0) {
                    partyActorInfoModel.player = getPlayerInfoById(info.userID)
                }
                seatsMap[info.seatSeq] = partyActorInfoModel
            }
        }
        return seatsMap
    }

    fun hasEmptySeat(): Boolean {
        this.seats?.let {
            for (info in it) {
                if (info.seatStatus == ESeatStatus.SS_OPEN.value && info.userID == 0) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 得到自己在Party中的角色等信息
     */
    fun getMyUserInfoInParty(): PartyPlayerInfoModel {
        if (myUserInfo == null) {
            // 如果找不到，则说明自己是观众
            var myinfo = PartyPlayerInfoModel()
            myinfo?.role.add(EPUserRole.EPUR_AUDIENCE.value)
            myinfo.popularity = 0
            myinfo.isOnline = true
            myinfo.userInfo = MyUserInfo.toUserInfoModel(MyUserInfoManager.myUserInfo)
            return myinfo
        } else {
            return myUserInfo!!
        }
    }

    /**
     * 得到自己在Party中的座位等信息
     */
    fun getMySeatInfoInParty(): PartySeatInfoModel? {
        return mySeatInfo
    }

    /**
     * 更新用户信息，传入一个列表
     */
    fun updateUsers(list: ArrayList<PartyPlayerInfoModel>?) {
        var hasMy = false
        var hasHost = false

        users.clear()
        if (list?.isNotEmpty() == true) {
            for (info in list) {
                var oldUser = usersMap[info.userID]
                if (oldUser == null) {
                    oldUser = info
                } else {
                    oldUser.tryUpdate(info)
                }
                users.add(oldUser)
            }
        }
        usersMap.clear()
        for (info in users) {
            usersMap[info.userID] = info

            if (info.userID == MyUserInfoManager.uid.toInt() && info.isNotOnlyAudience()) {
                myUserInfo = info
                hasMy = true
            }
            if (info.isHost()) {
                hostId = info.userID
                hasHost = true
            }
//                if (info.userID == hostId && !info.isHost()) {
//                    hostId = 0
//                }
        }
        if (!hasMy) {
            myUserInfo = null
        }
        if (!hasHost) {
            hostId = 0
        }
    }

//    /**
//     * 更新用户信息，传入一个列表
//     */
//    fun updateUsers1(list: ArrayList<PartyPlayerInfoModel>?) {
//        var hasMy = false
//        var hasHost = false
//        users.clear()
//        usersMap.clear()
//        if (list?.isNotEmpty() == true) {
//            users.addAll(list)
//            for (info in users) {
//                usersMap[info.userID] = info
//
//                if (info.userID == MyUserInfoManager.uid.toInt()) {
//                    myUserInfo = info
//                    hasMy = true
//                }
//                if (info.isHost()) {
//                    hostId = info.userID
//                    hasHost = true
//                }
////                if (info.userID == hostId && !info.isHost()) {
////                    hostId = 0
////                }
//            }
//        }
//        if (!hasMy) {
//            myUserInfo = null
//        }
//        if(!hasHost){
//            hostId = 0
//        }
//    }

    /**
     * 更新用户信息
     * 有座位传座位，没有就不传
     */
    fun updateUser(playerInfoModel: PartyPlayerInfoModel, seatInfoModel: PartySeatInfoModel?) {
        // 判断是否要更新用户
        if (playerInfoModel.isNotOnlyAudience()) {
            var uu = usersMap[playerInfoModel.userID]
            if (uu != null) {
//                if (uu.same(playerInfoModel)) {
//                } else {
//                    users.remove(uu)
//                    users.add(playerInfoModel)
//                    usersMap[playerInfoModel.userID] = playerInfoModel
                uu.tryUpdate(playerInfoModel)
//                }
            } else {
                users.add(playerInfoModel)
                usersMap[playerInfoModel.userID] = playerInfoModel
            }
        } else {
            // 是观众了 去除信息
            users.remove(playerInfoModel)
            usersMap.remove(playerInfoModel.userID)

        }
        updateSeat(seatInfoModel)
        // 座位信息有变化
        if (playerInfoModel.userID == MyUserInfoManager.uid.toInt() && playerInfoModel.isNotOnlyAudience()) {
            myUserInfo = playerInfoModel
        }
        if (playerInfoModel.isHost()) {
            hostId = playerInfoModel.userID
        }
        if (playerInfoModel.userID == hostId && !playerInfoModel.isHost()) {
            hostId = 0
        }
        if (!playerInfoModel.isNotOnlyAudience()) {
            if (playerInfoModel.userID == MyUserInfoManager.uid.toInt()) {
                myUserInfo = null
            }
            if (playerInfoModel.userID == hostId) {
                // 主持人变观众了
                hostId = 0
            }

            if (seatInfoModel == null && seatsUserIdMap.containsKey(playerInfoModel.userID)) {
                var seat = seatsUserIdMap[playerInfoModel.userID]
                seat?.userID = 0
                EventBus.getDefault().post(PartySeatInfoChangeEvent(seat?.seatSeq ?: -1))
            }
        }
    }

//    fun removeUser(playerInfoModel: PartyPlayerInfoModel) {
//        usersMap.remove(playerInfoModel.userID)
//        if (playerInfoModel.userID == MyUserInfoManager.uid.toInt()) {
//            myUserInfo = null
//        }
//        if (seatsUserIdMap.containsKey(playerInfoModel.userID)) {
//            var seat = seatsUserIdMap[playerInfoModel.userID]
//            seat?.userID = 0
//            EventBus.getDefault().post(PartySeatInfoChangeEvent(seat?.seatSeq ?: -1))
//        }
//    }

    /**
     * 更新座位信息，传入一堆座位
     */
    fun updateSeats(list: ArrayList<PartySeatInfoModel>?) {
        if (list?.isNotEmpty() == true) {
            seats.clear()
            seats.addAll(list)
            seatsSeatIdMap.clear()
            seatsUserIdMap.clear()
            var hasMy = false
            for (info in seats) {
                seatsSeatIdMap[info.seatSeq] = info
                if (info.userID > 0) {
                    seatsUserIdMap[info.userID] = info
                }
                if (info.userID == MyUserInfoManager.uid.toInt()) {
                    mySeatInfo = info
                    hasMy = true
                }
            }
            if (!hasMy) {
                mySeatInfo = null
            }
            EventBus.getDefault().post(PartySeatInfoChangeEvent(-1))
        }
    }

    /**
     * 更新单个座位信息
     */
    fun updateSeat(seatInfoModel: PartySeatInfoModel?) {
        var hasSeatChange = false
        if (seatInfoModel != null) {
            var ss = seatsSeatIdMap[seatInfoModel.seatSeq]
            if (ss != null) {
                if (ss.same(seatInfoModel)) {

                } else {
                    seats.remove(ss)
                    seats.add(seatInfoModel)
                    seatsSeatIdMap[seatInfoModel.seatSeq] = seatInfoModel
                    seatsUserIdMap[seatInfoModel.userID] = seatInfoModel
                    hasSeatChange = true
                }
            } else {
                seats.add(seatInfoModel)
                seatsSeatIdMap[seatInfoModel.seatSeq] = seatInfoModel
                seatsUserIdMap[seatInfoModel.userID] = seatInfoModel
                hasSeatChange = true
            }
        }
        if (hasSeatChange) {
            // 座位信息有变化
            EventBus.getDefault().post(PartySeatInfoChangeEvent(seatInfoModel!!.seatSeq))
            if (seatInfoModel.userID == MyUserInfoManager.uid.toInt()) {
                mySeatInfo = seatInfoModel
            }
        }
    }

    fun emptySeats() {
        seatsUserIdMap.clear()
        for (info in seats) {
            info.userID = 0
            info.seatStatus = ESeatStatus.SS_OPEN.value
            seatsSeatIdMap[info.seatSeq] = info
        }
        mySeatInfo = null
        EventBus.getDefault().post(PartySeatInfoChangeEvent(-1))
    }

    /**
     * 更新人气信息
     */
    fun updatePopular(userId: Int, popularity: Int) {
        // 只更新人气数值
        val player = getPlayerInfoById(userId)
        if (player?.popularity != popularity) {
            player?.popularity = popularity
            EventBus.getDefault().post(PartyPopularityUpdateEvent(userId))
        }
    }

    /**
     * 检查轮次信息是否需要更新
     */
    override fun checkRoundInEachMode() {
        if (isIsGameFinish) {
            MyLog.d(TAG, "游戏结束了，不需要再checkRoundInEachMode")
            return
        }
        if (expectRoundInfo == null) {
            MyLog.d(TAG, "尝试切换轮次 checkRoundInEachMode mExpectRoundInfo == null")
            // 结束状态了
            if (realRoundInfo != null) {
                val lastRoundInfoModel = realRoundInfo
                lastRoundInfoModel?.updateStatus(false, EPRoundStatus.PRS_END.value)
                realRoundInfo = null
                EventBus.getDefault().post(PartyRoundChangeEvent(lastRoundInfoModel, null))
            }
            return
        }
        MyLog.d(TAG, "尝试切换轮次 checkRoundInEachMode mExpectRoundInfo.roundSeq=" + expectRoundInfo!!.roundSeq)
        if ((expectRoundInfo?.roundSeq ?: 0) > (realRoundInfo?.roundSeq
                        ?: 0) || realRoundInfo == null) {
            // 轮次大于，才切换
            val lastRoundInfoModel = realRoundInfo
            lastRoundInfoModel?.updateStatus(false, EPRoundStatus.PRS_END.value)
            realRoundInfo = expectRoundInfo
            EventBus.getDefault().post(PartyRoundChangeEvent(lastRoundInfoModel, realRoundInfo))
        }
    }

    /**
     * 返回当前歌曲应该到达的进度
     * 如果是负数则说明还在准备阶段 未开始播放
     */
    fun getSingCurPosition(): Long {
        realRoundInfo?.roundStartTimeMs?.let {
            if (it > 0) {
                return (System.currentTimeMillis() - shiftTsForRelay) - it - 3000
            }
        }
        return Long.MAX_VALUE
    }

    fun loadFromRsp(rsp: JoinPartyRoomRspModel) {
        this.gameId = rsp.roomID
        this.agoraToken = rsp.agoraToken
        this.clubInfo = rsp.clubInfo
        this.applyUserCnt = rsp.applyUserCnt ?: 0
        this.gameStartTs = (rsp.gameStartTimeMs ?: 0L)
        this.notice = rsp.notice ?: ""
        this.onlineUserCnt = rsp.onlineUserCnt ?: 0
        this.roomName = rsp.roomName ?: ""
        this.topicName = rsp.topicName ?: ""
        updateSeats(rsp.seats)
        updateUsers(rsp.users)
        this.expectRoundInfo = rsp.currentRound
        this.realRoundInfo = null
        this.enterPermission = rsp.enterPermission
        this.roomType = rsp.roomType
        this.config = rsp.config
        this.getSeatMode = rsp.getSeatMode
        this.gameMode = rsp.gameMode
        this.joinSrc = rsp.joinSrc
        if (getMySeatInfoInParty()?.micStatus == EMicStatus.MS_CLOSE.value) {
            isMute = true
        }
    }

}
