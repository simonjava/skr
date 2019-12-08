package com.module.playways.room.msg.manager

import com.common.log.MyLog
import com.zq.live.proto.PartyRoom.EPartyRoomMsgType
import com.zq.live.proto.PartyRoom.PartyRoomMsg
import org.greenrobot.eventbus.EventBus

/**
 * 处理所有的RaceRoomMsg
 */
object PartyRoomMsgManager : BaseMsgManager<EPartyRoomMsgType, PartyRoomMsg>() {

    /**
     * 处理消息分发
     *
     * @param msg
     */
    override fun processRoomMsg(msg: PartyRoomMsg) {
        var canGo = true  //是否放行的flag
        for (filter in mPushMsgFilterList) {
            canGo = filter.doFilter(msg)
            if (!canGo) {
                MyLog.d("MicRoomMsgManager", "processRoomMsg " + msg + "被拦截")
                return
            }
        }
        processRoomMsg(msg.msgType, msg)
    }

    private fun processRoomMsg(messageType: EPartyRoomMsgType, msg: PartyRoomMsg?) {
        if (msg == null) {
            return
        }
        if (msg != null) {
            MyLog.d(TAG, "processRoomMsg" + " messageType=" + messageType + " 信令 msg.ts=" + msg.timeMs)
        }
        when {
            msg.msgType == EPartyRoomMsgType.PRT_JOIN_NOTICE -> EventBus.getDefault().post(msg.pJoinNoticeMsg)
            msg.msgType == EPartyRoomMsgType.PRT_FIX_ROOM_NOTICE -> EventBus.getDefault().post(msg.pFixRoomNoticeMsg)
            msg.msgType == EPartyRoomMsgType.PRT_SET_ROOM_ADMIN -> EventBus.getDefault().post(msg.pSetRoomAdminMsg)
            msg.msgType == EPartyRoomMsgType.PRT_SET_ALL_MEMBER_MIC -> EventBus.getDefault().post(msg.pSetAllMemberMicMsg)
            msg.msgType == EPartyRoomMsgType.PRT_SET_USER_MIC -> EventBus.getDefault().post(msg.pSetUserMicMsg)
            msg.msgType == EPartyRoomMsgType.PRT_SET_SEAT_STATUS -> EventBus.getDefault().post(msg.pSetSeatStatusMsg)
            msg.msgType == EPartyRoomMsgType.PRT_APPLY_FOR_GUEST -> EventBus.getDefault().post(msg.pApplyForGuest)
            msg.msgType == EPartyRoomMsgType.PRT_GET_SEAT -> EventBus.getDefault().post(msg.pGetSeatMsg)
            msg.msgType == EPartyRoomMsgType.PRT_BACK_SEAT -> EventBus.getDefault().post(msg.pBackSeatMsg)
            msg.msgType == EPartyRoomMsgType.PRT_INVITE_USER -> EventBus.getDefault().post(msg.pInviteUserMsg)
            msg.msgType == EPartyRoomMsgType.PRT_CHANGE_SEAT -> EventBus.getDefault().post(msg.pChangeSeatMsg)
            msg.msgType == EPartyRoomMsgType.PRT_KICK_OUT_USER -> EventBus.getDefault().post(msg.pKickoutUserMsg)
            msg.msgType == EPartyRoomMsgType.PRT_NEXT_ROUND -> EventBus.getDefault().post(msg.pNextRoundMsg)
            msg.msgType == EPartyRoomMsgType.PRT_EXIT_GAME -> EventBus.getDefault().post(msg.ppExitGameMsg)
            msg.msgType == EPartyRoomMsgType.PRT_SYNC -> EventBus.getDefault().post(msg.pSyncMsg)
            msg.msgType == EPartyRoomMsgType.PRT_DYNAMIC_EMOJI -> EventBus.getDefault().post(msg.pDynamicEmojiMsg)
        }
    }
}