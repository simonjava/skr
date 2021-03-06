package com.module.playways.race.room.model

import com.module.playways.race.room.event.RaceScoreChangeEvent
import com.zq.live.proto.RaceRoom.RoundScoreInfo
import org.greenrobot.eventbus.EventBus
import java.io.Serializable

class RaceScore : Serializable {

    var bLightCnt = 0
    var isEscape = false
    var winType = 0

    fun addBLightUser(notify: Boolean, userID: Int, bLightCnt: Int) {
        if (this.bLightCnt < bLightCnt) {
            this.bLightCnt = bLightCnt
            if (notify) {
                EventBus.getDefault().post(RaceScoreChangeEvent())
            }
        }
    }

    override fun toString(): String {
        return "RaceScore(bLightCnt=$bLightCnt, isEscape=$isEscape, winType=$winType)"
    }

    fun tryUpdateInfoModel(model: RaceScore?) {
        model?.let {
            if (it.bLightCnt > this.bLightCnt) {
                this.bLightCnt = it.bLightCnt
            }
            if (it.isEscape) {
                this.isEscape = true
            }
            if (it.winType > 0) {
                this.winType = it.winType
            }
        }
    }

}

internal fun parseFromRoundScoreInfoPB(pb: RoundScoreInfo): RaceScore {
    val model = RaceScore()
    model.bLightCnt = pb.bLightCnt
    model.isEscape = pb.isEscape
    model.winType = pb.winType.value
    return model
}