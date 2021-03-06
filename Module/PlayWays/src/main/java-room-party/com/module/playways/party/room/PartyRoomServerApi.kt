package com.module.playways.party.room

import com.common.rxretrofit.ApiManager
import com.common.rxretrofit.ApiResult
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface PartyRoomServerApi {

    /**
     * 主题房推荐标签
     *
     */
    @PUT("http://dev.game.inframe.mobi/v1/partyroom/tag-list")
    fun getPartyRoomTag(@Body body: RequestBody): Call<ApiResult>


    /**
     * 首页Party房间列表
     */
    @GET("http://dev.game.inframe.mobi/v2/partyroom/roomlist")
    fun getPartyRoomList(@Query("offset") offset: Int, @Query("limit") limit: Int, @Query("gameMode") gameMode: Int): Call<ApiResult>

    /**
     * 搜索主题房
     *
     */
    @PUT("http://dev.game.inframe.mobi/v1/partyroom/search")
    fun searchPartyRoom(@Body body: RequestBody): Observable<ApiResult>

    /**
     * 快速加入房间 { ERM_SING_PK = 1 : K歌 模式 - ERM_GMAE_PK = 2 : 游戏PK 模式 - ERM_MAKE_FRIEND = 3 : 相亲交友 模式string}
     *
     * @param body
     * @return
     */
    @PUT("http://dev.game.inframe.mobi/v1/partyroom/quick-join-room")
    fun quickJoinRoom(@Body body: RequestBody): Call<ApiResult>

    /**
     * 检查是否有创建权限 { null}
     *
     * @param body
     * @return
     */
    @PUT("http://dev.game.inframe.mobi/v1/partyroom/has-create-permission")
    fun hasCreatePermission(@Body body: RequestBody): Call<ApiResult>

    /**
     * 首页列出推荐家族
     */
    @GET("http://dev.api.inframe.mobi/v1/club/list-recommend-club")
    fun getRecommendClubList(@Query("offset") offset: Int, @Query("cnt") cnt: Int): Call<ApiResult>

    /**
     * 获取投票结果
     */
    @GET("http://dev.game.inframe.mobi/v1/partygame/result-vote")
    fun getVoteResult(@Query("roomID") roomID: Int, @Query("voteTag") voteTag: String): Call<ApiResult>

    /**
     * 进入房间 {"joinSrc": "JRS_UNKNOWN","platform": "PF_UNKNOWN","roomID": 0}
     */
    @PUT("http://dev.game.inframe.mobi/v1/partyroom/join-room")
    fun joinRoom(@Body body: RequestBody): Observable<ApiResult>

    /**
     * 创建房间
     */
    @PUT("http://dev.game.inframe.mobi/v1/partyroom/create-room")
    fun createRoom(@Body body: RequestBody): Call<ApiResult>

    @Headers(ApiManager.ALWAYS_LOG_TAG)
    @PUT("http://dev.game.inframe.mobi/v1/partyroom/exit-room")
    fun exitRoom(@Body body: RequestBody): Call<ApiResult>

    @PUT("http://dev.game.inframe.mobi/v1/partygame/heartbeat")
    fun heartbeat(@Body body: RequestBody): Call<ApiResult>

    @GET("http://dev.game.inframe.mobi/v1/partygame/sync-status")
    fun syncStatus(@Query("roomID") roomID: Long, @Query("roomType") roomType: Int): Call<ApiResult>

    /**
     * {
    "adminUserID": 0,
    "roomID": 0,
    "setType": "SAT_UNKNOWN"  SAT_UNKNOWN, SAT_ADD, SAT_DEL
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/set-admin")
    fun setAdmin(@Body body: RequestBody): Call<ApiResult>

    /**
     * {
    "notice": "string",
    "roomID": 0
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partyroom/set-notice")
    fun setNotice(@Body body: RequestBody): Call<ApiResult>

    /**
     * 获取申请列表 {"roomID": 0}
     */
    @GET("http://dev.game.inframe.mobi/v1/partygame/apply-for-list")
    fun getApplyList(@Query("roomID") roomID: Int, @Query("offset") offset: Int, @Query("cnt") cnt: Int): Call<ApiResult>

    /**
     * 申请或取消申请成为嘉宾 {"roomID": 0 , "cancel" : true}
     *  cancel为true，取消申请 cancel为false，为申请嘉宾
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/apply-for-guest")
    fun applyForGuest(@Body body: RequestBody): Call<ApiResult>

    /**
     * 自己上麦
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/self-get-seat")
    fun selfGetSeat(@Body body: RequestBody): Call<ApiResult>

    /**
     * 响应申请嘉宾，上麦 {"applyUserID":0，"roomID": 0}
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/get-seat")
    fun allowGetSeat(@Body body: RequestBody): Call<ApiResult>

    /**
     * 响应申请嘉宾，拒绝
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/del-apply-for-guest")
    fun refuseGetSeat(@Body body: RequestBody): Call<ApiResult>

    /**
     * 还回席位，下麦 {"roomID": 0, "seatSeq": 0,"seatUserID":0}
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/back-seat")
    fun backSeat(@Body body: RequestBody): Call<ApiResult>

    /**
     * 设置席位状态：关闭席位、打开席位
     *   {"roomID": 0, "micStatus": 0, "seatSeq": 0}
     *   MS_OPEN = 1 : 开麦 - MS_CLOSE = 2 : 闭麦
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/set-seat-status")
    fun setSeatStatus(@Body body: RequestBody): Call<ApiResult>

    /**
     * 全员禁麦，开麦 {"roomID": 0, "micStatus": 0}
     * MS_OPEN = 1 : 开麦 - MS_CLOSE = 2 : 闭麦
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/set-all-member-mic")
    fun setAllMicStatus(@Body body: RequestBody): Call<ApiResult>

    /**
     * 对指定用户禁麦，开麦 {"roomID": 0, "micStatus": 0, "seatSeq": 0, "seatUserID",0}
     * MS_OPEN = 1 : 开麦 - MS_CLOSE = 2 : 闭麦
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/set-user-mic")
    fun setUserMicStatus(@Body body: RequestBody): Call<ApiResult>

    /**
     * 对指定用户禁麦，开麦 {"roomID": 0, "micStatus": 0, "seatSeq": 0, "seatUserID",0}
     * MS_OPEN = 1 : 开麦 - MS_CLOSE = 2 : 闭麦
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/set-user-mic")
    fun setUserMicStatus2(@Body body: RequestBody): Observable<ApiResult>

    /**
     * 主持人禁麦，解除禁麦 {"roomID": 0, "micStatus": 0}
     * MS_OPEN = 1 : 开麦 - MS_CLOSE = 2 : 闭麦
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/set-host-mic")
    fun setHostMicStatus(@Body body: RequestBody): Observable<ApiResult>


    /**
     * 获取表情列表
     */
    @GET("http://dev.game.inframe.mobi/v1/partygame/list-emoji")
    fun getEmojiList(): Call<ApiResult>

    /**
     * 发送表情 {"id": 0, "roomID": 0 }
     */

    @PUT("http://dev.game.inframe.mobi/v1/partygame/send-emoji")
    fun sendEmoji(@Body body: RequestBody): Call<ApiResult>

    /**
     * 拉取房间可邀请的段位
     */
    @GET("http://dev.game.inframe.mobi/v1/partygame/online-user-list")
    fun getOnlineUserList(@Query("roomID") roomID: Int, @Query("offset") offset: Int, @Query("cnt") cnt: Int): Call<ApiResult>

    /**
     * 拉取房间可邀请的段位
     */
    @GET("http://dev.game.inframe.mobi/v1/partygame/club-could-be-host-list")
    fun getCouldBeHostList(@Query("roomID") roomID: Int, @Query("offset") offset: Int, @Query("cnt") cnt: Int): Call<ApiResult>

    @GET("http://dev.game.inframe.mobi/v1/partygame/game-rule-list")
    fun getPartyGameRuleList(@Query("roomID") roomID: Int, @Query("offset") offset: Int, @Query("cnt") cnt: Int): Call<ApiResult>

    @GET("http://dev.game.inframe.mobi/v1/partygame/game-play-list")
    fun getPartyGamePlayList(@Query("roomID") roomID: Int, @Query("ruleID") ruleID: Int, @Query("offset") offset: Int, @Query("cnt") cnt: Int): Call<ApiResult>

    @PUT("http://dev.game.inframe.mobi/v1/partyroom/invite")
    fun invite(@Body body: RequestBody): Observable<ApiResult>

    /**
     * {
    "roomID": 0,
    "ruleID": "string"
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/add-game")
    fun addGame(@Body body: RequestBody): Call<ApiResult>

    /**
     * {
    "playID": "string",
    "roomID": 0
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/add-play")
    fun addPlay(@Body body: RequestBody): Call<ApiResult>

    /**
     * {
    "roomID": 0,
    "sceneTag": "string"
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/del-game")
    fun delGame(@Body body: RequestBody): Call<ApiResult>

    /**
     * 已点游戏
     */
    @GET("http://dev.game.inframe.mobi/v1/partygame/list-game")
    fun getListGame(@Query("roomID") roomID: Int, @Query("offset") offset: Int, @Query("limit") limit: Int): Call<ApiResult>

    /**
     * {
    "roomID": 0,
    "sceneTag": "string"
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/up-game")
    fun upGame(@Body body: RequestBody): Call<ApiResult>

    /**
     * {
    "enterPermission": "EPT_UNKNOWNS",
    "roomID": 0,
    "topicName": "string"
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partyroom/change-roominfo")
    fun changeRoomInfo(@Body body: RequestBody): Call<ApiResult>

    /**
     * {
    "roomID": 0,
    "roundSeq": 0
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/end-question")
    fun endQuestion(@Body body: RequestBody): Call<ApiResult>

    /**
     * {
    "roomID": 0,
    "roundSeq": 0
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/end-round")
    fun endRound(@Body body: RequestBody): Call<ApiResult>

    /**
     * 踢人
     * {"roomID": 0,"kickoutUserID": 0}
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/kickout")
    fun kickout(@Body body: RequestBody): Call<ApiResult>

    /**
     * {
    "roomID": 0,
    "roundSeq": 0
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/end-ktv-music")
    fun endMusic(@Body body: RequestBody): Call<ApiResult>

    /**
     * {
    "roomID": 0,
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/club-become-host")
    fun becomeClubHost(@Body body: RequestBody): Call<ApiResult>

    /**
     * {
    "curHostUserID": 0,
    "roomID": 0
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/club-take-host")
    fun takeClubHost(@Body body: RequestBody): Call<ApiResult>

    /**
     * {
    "curHostUserID": 0,
    "roomID": 0
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/club-give-host")
    fun giveClubHost(@Body body: RequestBody): Call<ApiResult>

    @PUT("http://dev.game.inframe.mobi/v1/relaygame/room-send-invite-user")
    fun relayRoominvite(@Body body: RequestBody): Observable<ApiResult>

    /**主持人下发投票
     * {
    "beVotedUserIDs": [
    0
    ],
    "roomID": 0,
    "scope": "EVS_UNKNOWN"  title: - EVS_UNKNOWN = 0 : 未知 - EVS_HOST_GUEST = 1 : 除被投票用户以外，仅主持人、嘉宾可以投票 - EVS_ALL = 2 : 除被投票用户以外，房间其他所有成员均可以
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/begin-vote")
    fun beginVote(@Body body: RequestBody): Call<ApiResult>

    /**进行投票
     * {
    {
    "beVotedUserID": 0,
    "roomID": 0,
    "voteTag": "string"
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/response-vote")
    fun vote(@Body body: RequestBody): Call<ApiResult>

    /**开始抢
     * {
     * "roomID": 0
     * }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/begin-quick-answer")
    fun beginQuickAnswer(@Body body: RequestBody): Call<ApiResult>

    /**回应开始抢
     * {
     *  "quickAnswerTag": "string",
     * "roomID": 0
     * }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/response-quick-answer")
    fun responseQuickAnswer(@Body body: RequestBody): Call<ApiResult>

    /**得到抢答结果
     * {
     *  "quickAnswerTag": "string",
     * "roomID": 0
     * }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/result-quick-answer")
    fun getQuickAnswerResult(@Body body: RequestBody): Call<ApiResult>

    /**获取推荐游戏列表
     *{
     *"cnt": 0
     *}
     */
    @PUT("http://dev.game.inframe.mobi/v1/partyroom/recommend-game-list")
    fun getRecommendGameList(@Body body: RequestBody): Call<ApiResult>

    /**
     * 换房间
     */
    @PUT("http://dev.game.inframe.mobi/v1/partyroom/change-room")
    fun changeRoom(@Body body: RequestBody): Call<ApiResult>

    /**
     * 按下摇杆获得题目
     * {
    "punishType": "EPUT_UNKNOWN",
    "roomID": 0
    }
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/begin-punish")
    fun beginPunish(@Body body: RequestBody): Call<ApiResult>

    /**
     * 获取题目列表
     */
    @GET("http://dev.game.inframe.mobi/v1/partygame/list-punish")
    fun getPunishList(@Query("roomID") roomID: Int, @Query("punishType") punishType: Int): Call<ApiResult>

    /**
     * 获取派对榜单 EPlT_GX = 1 : 贡献榜单  EPlT_CA = 2 : 宠爱榜单
     */
    @GET("http://dev.game.inframe.mobi/v1/game/party-rank-list")
    fun getPartyRankList(@Query("offset") offset: Int, @Query("cnt") cnt: Int, @Query("userID") userID: Int,
                         @Query("roomID") roomID: Int, @Query("tagType") tagType: Int): Call<ApiResult>

    /**
     * 发宝箱
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/begin-diamondbox")
    fun beginDiamondBox(@Body body: RequestBody):Call<ApiResult>

    /**
     * 获取可以发送的宝箱配置
     */
    @GET("http://dev.game.inframe.mobi/v1/partygame/list-diamondbox")
    fun getDiamondBoxList(@Query("roomID") roomID: Int,
                          @Query("userID") userID: Long,
                          @Query("nickName") nickName:String
                          ):Call<ApiResult>

    /**
     * 打开宝箱，抢钻石
     */
    @PUT("http://dev.game.inframe.mobi/v1/partygame/response-diamondbox")
    fun grabDiamondBox(@Body body: RequestBody):Call<ApiResult>

    @GET("http://dev.game.inframe.mobi/v1/partygame/result-diamondbox")
    fun checkGrabDiamondBoxResult(@Query("roomID") roomID: Int,
                                  @Query("diamondboxTag") diamondboxTag: String):Call<ApiResult>

}
