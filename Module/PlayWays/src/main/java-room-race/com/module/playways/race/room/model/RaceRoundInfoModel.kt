package com.module.playways.race.room.model

import android.util.ArrayMap
import com.common.log.MyLog
import com.module.playways.race.room.event.*
import com.module.playways.room.prepare.model.BaseRoundInfoModel
import com.zq.live.proto.RaceRoom.ERUserRole
import com.zq.live.proto.RaceRoom.ERaceRoundStatus
import com.zq.live.proto.RaceRoom.RaceRoundInfo
import org.greenrobot.eventbus.EventBus

class RaceRoundInfoModel : BaseRoundInfoModel() {

    //    protected int overReason; // 结束的原因
    //  protected int roundSeq;// 本局轮次
    var status = ERaceRoundStatus.ERRS_UNKNOWN.value // 轮次状态在擂台赛中使用
    var scores = ArrayList<RaceScore>()
    var subRoundSeq = 0 // 子轮次为1 代表第一轮A演唱 2 为第二轮B演唱
    var subRoundInfo = ArrayList<RaceSubRoundInfo>()
    var games = ArrayList<RaceGameInfo>()
    var playUsers = ArrayList<RacePlayerInfoModel>()
    var waitUsers = ArrayList<RacePlayerInfoModel>()
    val gamesChoiceMap = ArrayMap<Int, ArrayList<Int>>()

    override fun getType(): Int {
        return TYPE_RACE
    }

    /**
     * 有人进入房间
     */
    fun joinUser(racePlayerInfoModel: RacePlayerInfoModel) {
        if (racePlayerInfoModel.role == ERUserRole.ERUR_PLAY_USER.value) {
            if (!playUsers?.contains(racePlayerInfoModel)) {
                playUsers?.add(racePlayerInfoModel)
                EventBus.getDefault().post(RacePlaySeatUpdateEvent(playUsers))
            }
        } else if (racePlayerInfoModel.role == ERUserRole.ERUR_WAIT_USER.value) {
            if (!waitUsers?.contains(racePlayerInfoModel)) {
                waitUsers?.add(racePlayerInfoModel)
                EventBus.getDefault().post(RaceWaitSeatUpdateEvent(playUsers))
            }
        }
    }

    /**
     * 有人离开房间
     */
    fun exitUser(userId: Int) {
        kotlin.run {
            var i = 0
            for (p in playUsers) {
                if (p.userInfo.userId == userId) {
                    playUsers.removeAt(i)
                    EventBus.getDefault().post(RacePlaySeatUpdateEvent(playUsers))
                    return
                }
                i++
            }
        }
        kotlin.run {
            var i = 0
            for (p in waitUsers) {
                if (p.userInfo.userId == userId) {
                    waitUsers.removeAt(i)
                    EventBus.getDefault().post(RaceWaitSeatUpdateEvent(playUsers))
                    break
                }
                i++
            }
        }

    }

    fun updatePlayUsers(l: List<RacePlayerInfoModel>?) {
        playUsers?.clear()
        l?.let {
            playUsers?.addAll(it)
        }
        EventBus.getDefault().post(RacePlaySeatUpdateEvent(playUsers))
    }

    fun updateWaitUsers(l: List<RacePlayerInfoModel>?) {
        waitUsers?.clear()
        l?.let {
            waitUsers?.addAll(it)
        }
        EventBus.getDefault().post(RacePlaySeatUpdateEvent(waitUsers))
    }

    /**
     * wantSing 增加人
     */
    fun addWantSingChange(choiceID: Int, userID: Int?) {
        var list = gamesChoiceMap[choiceID]
        if (list == null) {
            list = ArrayList()
            gamesChoiceMap[choiceID] = list
        }
        if (!list.contains(userID)) {
            userID?.let {
                list.add(it)
                EventBus.getDefault().post(RaceWantSingChanceEvent(choiceID, it))
            }
        }
    }

    fun addBLightUser(notify: Boolean, userID: Int, subRoundSeq: Int, bLightCnt: Int) {
        scores.getOrNull(subRoundSeq - 1)?.addBLightUser(notify, userID, bLightCnt)
    }

    /**
     * 更新状态
     */
    fun updateStatus(notify: Boolean, statusGrab: Int) {
        if (getStatusPriority(status) < getStatusPriority(statusGrab)) {
            val old = status
            status = statusGrab
            if (notify) {
                EventBus.getDefault().post(RaceRoundStatusChangeEvent(this, old))
            }
        }
    }

    override fun tryUpdateRoundInfoModel(round: BaseRoundInfoModel, notify: Boolean) {
        if (round == null) {
            MyLog.e("JsonRoundInfo RoundInfo == null")
            return
        }
        val roundInfo = round as RaceRoundInfoModel
        // 更新双方得票
        // 观众席与玩家席更新，以最新的为准
        run {
            var needUpdate = false
            if (playUsers.size == roundInfo.playUsers.size) {
                var i = 0
                while (i < playUsers.size && i < roundInfo.playUsers.size) {
                    val infoModel1 = playUsers?.get(i)
                    val infoModel2 = roundInfo.playUsers?.get(i)
                    if (infoModel1 != infoModel2) {
                        needUpdate = true
                        break
                    }
                    i++
                }
            } else {
                needUpdate = true
            }
            if (needUpdate) {
                updatePlayUsers(roundInfo?.playUsers)
            }
        }

        run {
            var needUpdate = false
            if (waitUsers.size == roundInfo.waitUsers.size) {
                var i = 0
                while (i < waitUsers.size && i < roundInfo.waitUsers.size) {
                    val infoModel1 = waitUsers?.get(i)
                    val infoModel2 = roundInfo.waitUsers?.get(i)
                    if (infoModel1 != infoModel2) {
                        needUpdate = true
                        break
                    }
                    i++
                }
            } else {
                needUpdate = true
            }
            if (needUpdate) {
                updateWaitUsers(roundInfo?.waitUsers)
            }
        }

        if (roundInfo.overReason > 0) {
            this.overReason = roundInfo.overReason
        }
        if (roundInfo.games.size > 0) {
            //有数据
            if (this.games.isEmpty()) {
                this.games.addAll(roundInfo.games)
            } else {
                // 都有数据

            }
        }
        if (roundInfo.subRoundInfo.size > 0) {
            //有数据
            if (this.subRoundInfo.isEmpty()) {
                this.subRoundInfo.addAll(roundInfo.subRoundInfo)
            } else {
                // 都有数据

            }
        }

        if (roundInfo.scores.size > 0) {
            //有数据
            if (this.scores.isEmpty()) {
                this.scores.addAll(roundInfo.scores)
            } else {
                // 都有数据
            }
        }
        if (this.subRoundSeq != roundInfo.subRoundSeq && this.status == roundInfo.status) {
            val old = this.subRoundSeq
            this.subRoundSeq = roundInfo.subRoundSeq
            // 子轮次有切换
            EventBus.getDefault().post(RaceSubRoundChangeEvent(this, old))
        }
        // 更新 sub
        updateStatus(notify, roundInfo.status)
        return
    }

}


/**
 * 重排一下状态机的优先级
 *
 * @param status
 * @return
 */
internal fun getStatusPriority(status: Int): Int {
    return status
}

internal fun parseFromRoundInfoPB(pb: RaceRoundInfo): RaceRoundInfoModel {
    val model = RaceRoundInfoModel()
    model.roundSeq = pb.roundSeq
    model.subRoundSeq = pb.subRoundSeq
    model.status = pb.status.value
    model.overReason = pb.overReason.value
    pb.subRoundInfoList.forEach {
        model.subRoundInfo.add(parseFromSubRoundInfoPB(it))
    }
    pb.scoresList.forEach {
        model.scores.add(parseFromRoundScoreInfoPB(it))
    }
    pb.waitUsersList.forEach {
        model.waitUsers.add(parseFromROnlineInfoPB(it))
    }
    pb.playUsersList.forEach {
        model.playUsers.add(parseFromROnlineInfoPB(it))
    }
    return model
}

