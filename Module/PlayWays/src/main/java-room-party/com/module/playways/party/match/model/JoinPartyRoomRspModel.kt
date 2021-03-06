package com.module.playways.party.match.model

import com.alibaba.fastjson.annotation.JSONField
import com.common.core.userinfo.model.ClubInfo
import com.module.playways.party.room.model.PartyConfigModel
import com.module.playways.party.room.model.PartyPlayerInfoModel
import com.module.playways.party.room.model.PartyRoundInfoModel
import com.module.playways.party.room.model.PartySeatInfoModel
import java.io.Serializable

class JoinPartyRoomRspModel : Serializable {

    @JSONField(name = "gameMode")
    var gameMode: Int = 0

    @JSONField(name = "getSeatMode")
    var getSeatMode: Int = 0

    @JSONField(name = "roomID")
    var roomID = 0

    @JSONField(name = "agoraToken")
    var agoraToken: String? = null

    @JSONField(name = "clubInfo")
    var clubInfo: ClubInfo? = null

    @JSONField(name = "applyUserCnt")
    var applyUserCnt: Int? = null

    @JSONField(name = "currentRound")
    var currentRound: PartyRoundInfoModel? = null

    @JSONField(name = "gameStartTimeMs")
    var gameStartTimeMs: Long? = null

    @JSONField(name = "notice")
    var notice: String? = null

    @JSONField(name = "onlineUserCnt")
    var onlineUserCnt: Int? = null

    @JSONField(name = "roomName")
    var roomName: String? = null

    @JSONField(name = "seats")
    var seats: ArrayList<PartySeatInfoModel>? = null

    @JSONField(name = "topicName")
    var topicName: String? = null

    @JSONField(name = "users")
    var users: ArrayList<PartyPlayerInfoModel>? = null

    @JSONField(name = "enterPermission")
    var enterPermission = 2

    @JSONField(name = "elapsedTimeMs")
    var elapsedTimeMs: Int? = 0

    //[ RT_UNKNOWN, RT_PERSONAL, RT_FAMILY ]
    @JSONField(name = "roomType")
    var roomType: Int? = 0

    @JSONField(name = "config")
    var config: PartyConfigModel = PartyConfigModel()

    @JSONField(name = "joinSrc")
    var joinSrc: Int = 0

    companion object {
        const val JRS_LIST = 1  //列表
        const val JRS_INVITE = 2  //邀请
        const val JRS_RECONNECT = 3 //断线重连
        const val JRS_QUICK_JOIN = 4 // 快速进入
        const val JRS_CHANGE_ROOM = 5 // 换房
    }
}