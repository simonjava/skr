package com.module.playways.party.room.model

import com.common.core.userinfo.model.UserInfoModel
import com.module.playways.room.prepare.model.PlayerInfoModel
import com.zq.live.proto.Common.EPUserRole
import com.zq.live.proto.Common.POnlineInfo
import java.util.*
import kotlin.collections.ArrayList
import java.lang.StringBuilder

class PartyPlayerInfoModel : PlayerInfoModel() {
    //    enum EPUserRole {
//        EPUR_UNKNOWN  = 0; //未知角色
//        EPUR_HOST     = 1; //主持人
//        EPUR_ADMIN    = 2; //管理员
//        EPUR_GUEST    = 3; //嘉宾
//        EPUR_AUDIENCE = 4; //观众
//    }
    var role = ArrayList<Int>() // 角色
        set(value) {
            field = value
            field.sortWith(Comparator { o1, o2 ->
                o1 - o2
            })
        }
    
    var popularity = 0 // 人气

    fun getRoleDesc(): String? {
        var stringBuilder = StringBuilder()
        if (isHost()) {
            stringBuilder.append("主持人")
        }
        if (isAdmin()) {
            if (isHost()) {
                stringBuilder.append("/")
            }
            stringBuilder.append("管理员")
        }
        if (isGuest()) {
            if (isHost() || isAdmin()) {
                stringBuilder.append("/")
            }
            stringBuilder.append("嘉宾")
        }
        return stringBuilder.toString()
    }

    /**
     * 是否是指定的某些角色
     */
    fun isRole(vararg roles: Int): Boolean {
        for (r in role) {
            for (r2 in roles) {
                if (r == r2) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 不仅仅是观众
     */
    fun isNotOnlyAudience(): Boolean {
        return isRole(EPUserRole.EPUR_ADMIN.value, EPUserRole.EPUR_HOST.value, EPUserRole.EPUR_GUEST.value)
    }

    /**
     * 是不是管理员
     */
    fun isAdmin(): Boolean {
        for (r in role) {
            if (r == EPUserRole.EPUR_ADMIN.value) {
                return true
            }
        }
        return false
    }

    /**
     * 是不是主持人
     */
    fun isHost(): Boolean {
        for (r in role) {
            if (r == EPUserRole.EPUR_HOST.value) {
                return true
            }
        }
        return false
    }

    /**
     * 是不是嘉宾
     */
    fun isGuest(): Boolean {
        for (r in role) {
            if (r == EPUserRole.EPUR_GUEST.value) {
                return true
            }
        }
        return false
    }

    override fun toString(): String {
        return "PartyPlayerInfoModel(userInfo=${userInfo.toSimpleString()}, role=$role)"
    }

    /**
     * 判断两个model信息是否相同
     */
    fun same(playerInfoModel: PartyPlayerInfoModel): Boolean {
        if (this.popularity != playerInfoModel.popularity) {
            return false
        }
        if (this.role.size != playerInfoModel.role.size) {
            return false
        }
        for ((index, r) in this.role.withIndex()) {
            if (r != playerInfoModel.role[index]) {
                return false
            }
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as PartyPlayerInfoModel

        if (this.role.size != other.role.size) {
            return false
        }
        for ((index, r) in this.role.withIndex()) {
            if (r != other.role[index]) {
                return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + role.hashCode()
        return result
    }

    // 又一个较为完整的user 更新一个 不完整的user ，但又不是完全覆盖
    fun tryUpdate(info: PartyPlayerInfoModel) {
        if(!info.role.isEmpty()){
            this.role = info.role
        }
        this.popularity = info.popularity
        this.userInfo?.tryUpdate(info.userInfo)
    }

    companion object {
        fun parseFromPb(pb: POnlineInfo): PartyPlayerInfoModel {
            var info = PartyPlayerInfoModel()
            info.popularity = pb.popularity
            for (r in pb.roleList) {
                info.role.add(r.value)
            }
            info.role.sortWith(Comparator { o1, o2 ->
                o1 - o2
            })
            info.userInfo = UserInfoModel.parseFromPB(pb.userInfo)
            return info
        }

        fun parseFromPb(pbs: List<POnlineInfo>): ArrayList<PartyPlayerInfoModel> {
            var infos = ArrayList<PartyPlayerInfoModel>()
            for (pb in pbs) {
                infos.add(parseFromPb(pb))
            }
            return infos
        }
    }
}
