package com.module.playways.party.room.model

import com.alibaba.fastjson.annotation.JSONField
import com.zq.live.proto.PartyRoom.EMicStatus
import com.zq.live.proto.PartyRoom.ESeatStatus
import com.zq.live.proto.PartyRoom.SeatInfo
import java.io.Serializable

// 座位信息
class PartySeatInfoModel : Serializable {

    @JSONField(name = "seatSeq")
    var seatSeq: Int = 1     // 服务器席位从1开始
    @JSONField(name = "seatStatus")
    var seatStatus = ESeatStatus.SS_UNKNOWN.value  // 座位的状态   SS_OPEN= 1;  //打开  SS_CLOSE   = 2; //关闭
    @JSONField(name = "userID")
    var userID: Int = 0      // 座位上人的id
    @JSONField(name = "micStatus")
    var micStatus = EMicStatus.MS_UNKNOWN.value   // 麦的状态     MS_OPEN    = 1; //开麦   MS_CLOSE   = 2; //闭麦

    override fun toString(): String {
        return "PartySeatInfoModel(seatSeq=$seatSeq, seatStatus=$seatStatus, userID=$userID, micStatus=$micStatus)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PartySeatInfoModel

        if (seatSeq != other.seatSeq) return false

        return true
    }


    fun same(other: PartySeatInfoModel): Boolean {
        if (seatSeq != other.seatSeq) return false
        if (seatStatus != other.seatStatus) return false
        if (userID != other.userID) return false
        if (micStatus != other.micStatus) return false

        return true
    }

    override fun hashCode(): Int {
        var result = seatSeq
        result = 31 * result + seatStatus
        result = 31 * result + userID
        result = 31 * result + micStatus
        return result
    }


    companion object {
        fun parseFromPb(pb: SeatInfo): PartySeatInfoModel {
            var info = PartySeatInfoModel()
            info.seatSeq = pb.seatSeq
            info.seatStatus = pb.seatStatus.value
            info.userID = pb.userID
            info.micStatus = pb.micStatus.value
            return info
        }

        fun parseFromPb(pbs: List<SeatInfo>): ArrayList<PartySeatInfoModel> {
            var infos = ArrayList<PartySeatInfoModel>()
            for (pb in pbs) {
                infos.add(parseFromPb(pb))
            }
            return infos
        }
    }
}