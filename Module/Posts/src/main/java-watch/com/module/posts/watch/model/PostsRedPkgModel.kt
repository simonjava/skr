package com.module.posts.watch.model

import com.alibaba.fastjson.annotation.JSONField
import java.io.Serializable

// 帖子红包
class PostsRedPkgModel : Serializable {

    companion object {
        const val ROS_UN_OPEN = 1    //未打开
        const val ROS_HAS_OPEN = 2   // 已打开

        const val RS_UN_AUDIT = 1 //1未审核
        const val RS_ONGING = 2   //2有效期内
        const val RS_GET_PART = 3 // 3已过期未领取完
        const val RS_GET_ALL = 4  // 4被领取完
    }

    @JSONField(name = "openStatus")
    var openStatus: Int = 0    // 红包的状态
    @JSONField(name = "redpacketID")
    var redpacketID: Long = 0L
    @JSONField(name = "redpacketDesc")
    var redpacketDesc: String = ""  // 金额描述
    @JSONField(name = "redpacketNum")
    var redpacketNum: Int = 0
    @JSONField(name = "redpacketType")
    var redpacketType: Int = 0    // 0未知 1金币红包 2钻石红包
    @JSONField(name = "resTimeMs")
    var resTimeMs: Long = 0    // 剩余时间
    @JSONField(name = "status")
    var status: Int = 0   // 0未知

    override fun toString(): String {
        return "PostsRedPkgModel(openStatus=$openStatus, redpacketID=$redpacketID, redpacketDesc='$redpacketDesc', redpacketNum=$redpacketNum, redpacketType=$redpacketType, resTimeMs=$resTimeMs, status=$status)"
    }

}