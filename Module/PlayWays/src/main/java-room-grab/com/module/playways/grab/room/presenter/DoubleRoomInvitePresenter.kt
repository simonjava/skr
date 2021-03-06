package com.module.playways.grab.room.presenter

import com.common.mvp.RxLifeCyclePresenter

class DoubleRoomInvitePresenter() : RxLifeCyclePresenter() {
//    private val mTag = "DoubleRoomInvitePresenter"
//    private var mDoubleRoomServerApi = ApiManager.getInstance().createService(DoubleRoomServerApi::class.java)
//    private var handlerTaskTimer: HandlerTaskTimer? = null
//
//    /**
//     * 邀请一个人去双人房
//     */
//    fun inviteToDoubleRoom(inviteUserID: Int) {
//        sendInvite(inviteUserID)
//    }
//
//    private fun sendInvite(inviteUserID: Int) {
//        val mutableSet1 = mutableMapOf("inviteUserID" to inviteUserID)
//        val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(mutableSet1))
//        ApiMethods.subscribe(mDoubleRoomServerApi.sendInvite(body), object : ApiObserver<ApiResult>() {
//            override fun process(obj: ApiResult?) {
//                if (obj?.errno == 0) {
//                    startCheckLoop()
//                    U.getToastUtil().showShort("邀请成功")
//                } else {
//                    if (obj?.errno == 8376040) {
//                        StatisticsAdapter.recordCountEvent("cp", "invite2_outchance", null)
//                    }
//                    U.getToastUtil().showShort(obj?.errmsg)
//                }
//            }
//
//            override fun onNetworkError(errorType: ErrorType?) {
//                U.getToastUtil().showShort("网络错误")
//            }
//
//            override fun onError(e: Throwable) {
//                U.getToastUtil().showShort("请求错误")
//            }
//        }, this)
//    }
//
//    private fun startCheckLoop() {
//        handlerTaskTimer?.dispose()
//        handlerTaskTimer = HandlerTaskTimer
//                .newBuilder()
//                .interval(3000)
//                .delay(500)
//                .take(3)
//                .start(object : HandlerTaskTimer.ObserverW() {
//                    override fun onNext(t: Int) {
//                        getInviteState()
//                    }
//                })
//    }
//
//    /**
//     * 发出邀请之后轮询检查对方的进房情况，因为push有可能会丢
//     */
//    private fun getInviteState() {
//        ApiMethods.subscribe(mDoubleRoomServerApi.getInviteEnterResult(), object : ApiObserver<ApiResult>() {
//            override fun process(obj: ApiResult?) {
//                if (obj?.errno == 0 && obj.data.getBooleanValue("hasInvitedRoom")) {
//                    val doubleRoomData = DoubleRoomData.makeRoomDataFromJsonObject(obj.data)
//                    doubleRoomData.doubleRoomOri = DoubleRoomData.DoubleRoomOri.GRAB_INVITE
//                    doubleRoomData.inviterId = MyUserInfoManager.uid
//                    ARouter.getInstance().build(RouterConstants.ACTIVITY_DOUBLE_PLAY)
//                            .withSerializable("roomData", doubleRoomData)
//                            .navigation()
//                }
//            }
//        }, this)
//    }
//
//    override fun destroy() {
//        super.destroy()
//        handlerTaskTimer?.dispose()
//    }
}