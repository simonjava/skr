package com.module.playways.relay.room

import com.common.core.myinfo.MyUserInfoManager
import com.common.core.userinfo.model.UserInfoModel
import com.common.log.MyLog
import com.component.busilib.constans.GameModeType
import com.module.playways.BaseRoomData
import com.module.playways.RoomDataUtils
import com.module.playways.relay.match.model.JoinRelayRoomRspModel
import com.module.playways.relay.room.event.RelayLockChangeEvent
import com.module.playways.relay.room.event.RelayRoundChangeEvent
import com.module.playways.relay.room.model.RelayConfigModel
import com.module.playways.relay.room.model.RelayRoundInfoModel
import com.module.playways.relay.room.model.ReplayPlayerInfoModel
import com.module.playways.room.prepare.model.PlayerInfoModel
import com.zq.live.proto.RelayRoom.ERRoundStatus
import org.greenrobot.eventbus.EventBus


class RelayRoomData : BaseRoomData<RelayRoundInfoModel>() {
    companion object {
        var MUSIC_PUBLISH_VOLUME = 85
    }

    override fun getPlayerAndWaiterInfoList(): List<PlayerInfoModel> {
        return null!!
    }

    override fun getInSeatPlayerInfoList(): List<PlayerInfoModel> {
        return null!!
    }

    /**
     * 本地时间比服务器时间快多少，使用专门的校时接口校对 t1+(t2-t1)/2 - s1
     * t1为本地发包时间 t2为本地收包时间  s1 为服务器收包时间
     *
     * createTs + beginTs 代表服务器期望的两端商量好的演唱开始时间
     * System.currentTimeMillis() - shiftTsForRelay 与 createTs + beginTs 进行比较能得到演唱应该的进度
     */
    var shiftTsForRelay = 0
    var configModel = RelayConfigModel()// 一唱到底配置
    var peerUser: ReplayPlayerInfoModel? = null

    var unLockMe = false // 我是否解锁
        set(value) {
            if (value != field) {
                field = value
                EventBus.getDefault().post(RelayLockChangeEvent())
            }
        }
    var unLockPeer = false // 对方是否解锁
        set(value) {
            if (value != field) {
                field = value
                EventBus.getDefault().post(RelayLockChangeEvent())
            }
        }
    var leftSeat = true;// 我的未知是否在左边
    var isHasExitGame = false

    override val gameType: Int
        get() = GameModeType.GAME_MODE_RELAY

    fun hasTimeLimit(): Boolean {
        return unLockMe && unLockPeer
    }

    init {

    }

    /**
     * 返回当前歌曲应该到达的进度
     * 如果是负数则说明还在准备阶段 未开始播放
     */
    fun getSingCurPosition(): Long {
        realRoundInfo?.singBeginMs?.let {
            if (it > 0) {
                return (System.currentTimeMillis() - shiftTsForRelay) - (gameCreateTs + it)
            }
        }
        return Long.MAX_VALUE
    }

    /**
     * 返回当前正在演唱的选手id
     * 如果还未演唱，返回的即将演唱的选手id
     */
    fun getSingerIdNow(): Int {
        var now = getSingCurPosition()
        if (now != Long.MAX_VALUE) {
            // 拿到歌曲分段信息
            realRoundInfo?.music?.relaySegments?.let {
                var index = 0
                for (s in it) {
                    if (now < s) {
                        break
                    }
                    index++
                }
                if (index % 2 == 0) {
                    // 第一个人演唱阶段 看发起人
                    return realRoundInfo?.originId!!
                } else {
                    // 第二个人演唱阶段
                    if (realRoundInfo?.originId!! == MyUserInfoManager.uid.toInt()) {
                        return peerUser?.userID!!
                    } else {
                        return MyUserInfoManager.uid.toInt()
                    }
                }
            }
        }
        return 0
    }

    /**
     * 算出下一次轮次切换的时间 如果没有轮次切换了 返回-1
     */
    fun getNextTurnChangeTs(): Long {
        var now = getSingCurPosition()
        if (now != Long.MAX_VALUE) {
            // 拿到歌曲分段信息
            realRoundInfo?.music?.relaySegments?.let {
                var index = 0
                for (s in it) {
                    if (now < s) {
                        return s - now;
                    }
                    index++
                }
            }
        }
        return -1
    }

    /**
     * 当前是否是我唱
     */
    fun isSingByMeNow(): Boolean {
        // 第二个人演唱阶段
        return getSingerIdNow() == MyUserInfoManager.uid.toInt()
    }

    /**
     * 我是否第一个唱
     */
    fun isFirstSingByMe(): Boolean {
        // 第二个人演唱阶段
        return MyUserInfoManager.uid.toInt() == realRoundInfo?.originId
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
                lastRoundInfoModel?.updateStatus(false, ERRoundStatus.RRS_END.value)
                realRoundInfo = null
                EventBus.getDefault().post(RelayRoundChangeEvent(lastRoundInfoModel, null))
            }
            return
        }
        MyLog.d(TAG, "尝试切换轮次 checkRoundInEachMode mExpectRoundInfo.roundSeq=" + expectRoundInfo!!.roundSeq)
        if (RoomDataUtils.roundSeqLarger<RelayRoundInfoModel>(expectRoundInfo, realRoundInfo) || realRoundInfo == null) {
            // 轮次大于，才切换
            val lastRoundInfoModel = realRoundInfo
            lastRoundInfoModel?.updateStatus(false, ERRoundStatus.RRS_END.value)
            realRoundInfo = expectRoundInfo
            EventBus.getDefault().post(RelayRoundChangeEvent(lastRoundInfoModel, realRoundInfo))
        }
    }

    fun loadFromRsp(rsp: JoinRelayRoomRspModel) {
        this.gameId = rsp.roomID
        this.gameCreateTs = rsp.createTimeMs
        rsp.tokens?.forEach {
            if (it.userID == MyUserInfoManager.uid.toInt()) {
                this.agoraToken = it.token
                return@forEach
            }
        }
        this.configModel = rsp.config ?: RelayConfigModel()
        this.peerUser = ReplayPlayerInfoModel()
        rsp.users?.forEach {
            if (it.userId != MyUserInfoManager.uid.toInt()) {
                this.peerUser?.userInfo = it
                return@forEach
            }
        }
        this.peerUser?.isOnline = true
        this.expectRoundInfo = rsp.currentRound

    }


}
