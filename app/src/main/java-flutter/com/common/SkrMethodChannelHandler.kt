package com.common

import com.alibaba.fastjson.JSON
import com.common.core.myinfo.MyUserInfoManager
import com.common.core.userinfo.UserInfoManager
import com.common.flutter.plugin.MethodHandler
import com.common.log.MyLog
import com.common.rxretrofit.httpGet
import com.common.rxretrofit.httpPost
import com.common.rxretrofit.httpPut
import com.common.statistics.StatisticsAdapter
import com.common.utils.U
import com.module.ModuleServiceManager
import com.module.playways.party.bgmusic.getLocalMusicInfo
import com.module.playways.room.data.H
import com.zq.mediaengine.kit.ZqEngineKit
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class SkrMethodChannelHandler : MethodHandler("SkrMethodChannelHandler") {
    override fun handle(call: MethodCall, result: MethodChannel.Result): Boolean {
        when {
            call.method == "httpGet" -> {
                var url = call.argument<String>("url")
                var params = call.argument<HashMap<String, Any?>>("params")
                httpGet(url!!, params) { r ->
                    result.success(r)
                }
                return true
            }
            call.method == "httpPost" -> {
                var url = call.argument<String>("url")
                var params = call.argument<HashMap<String, Any?>>("params")
                httpPost(url!!, params) { r ->
                    result.success(r)
                }
                return true
            }
            call.method == "httpPut" -> {
                var url = call.argument<String>("url")
                var params = call.argument<HashMap<String, Any?>>("params")
                httpPut(url!!, params) { r ->
                    result.success(r)
                }
                return true
            }
            call.method == "getDeviceID" -> {
                result.success(U.getDeviceUtils().deviceID)
                return true
            }
            call.method == "getChannel" -> {
                result.success(U.getChannelUtils().channel)
                return true
            }
            call.method == "loadLocalBGM" -> {
                var l = getLocalMusicInfo()
                result.success(JSON.toJSONString(l))
                return true
            }
            call.method == "getEngineMusicPath" -> {
                var l = ZqEngineKit.getInstance().params.mixMusicFilePath
                result.success(l)
                return true
            }
            call.method == "startAudioMixing" -> {
                var filePath = call.argument<String>("filePath")
                var cycle = call.argument<Int>("cycle")
                ZqEngineKit.getInstance().startAudioMixing(MyUserInfoManager.uid.toInt(), filePath, null, 0, cycle
                        ?: 1)
                var from = call.argument<String>("from")
                if (from == "party_bgm") {
                    H.partyRoomData?.bgmPlayingPath = filePath
                }
                result.success(null)
                return true
            }
            call.method == "stopAudioMixing" -> {
                ZqEngineKit.getInstance().stopAudioMixing()
                var from = call.argument<String>("from")
                if (from == "party_bgm") {
                    H.partyRoomData?.bgmPlayingPath = null
                }
                result.success(null)
                return true
            }
            call.method == "setMusicPublishVolume" -> {
                var volume = call.argument<Int>("volume")
                var from = call.argument<String>("from")
                ZqEngineKit.getInstance().adjustAudioMixingPublishVolume(volume ?: 80, true)
                result.success(null)
                return true
            }
            call.method == "getMusicPublishVolume" -> {
                var volume = ZqEngineKit.getInstance().params.audioMixingPublishVolume
                result.success(volume)
                return true
            }
            call.method == "setMusicLocalVolume" -> {
                var volume = call.argument<Int>("volume")
                var from = call.argument<String>("from")
                ZqEngineKit.getInstance().adjustAudioMixingPlayoutVolume(volume ?: 80, true)
                result.success(null)
                return true
            }
            call.method == "showToast" -> {
                var short = call.argument<Boolean>("short") ?: true
                var content = call.argument<String>("content") ?: ""
                if (short) {
                    U.getToastUtil().showShort(content)
                } else {
                    U.getToastUtil().showLong(content)
                }
                result.success(null)
                return true
            }
            call.method == "syncMyInfo" -> {
                result.success(hashMapOf("uid" to MyUserInfoManager.uid,
                        "avatar" to MyUserInfoManager.avatar,
                        "userNickname" to MyUserInfoManager.nickName)
                )
                return true
            }
            call.method == "statisticCount" -> {
                var category = call.argument<String>("category")
                var key = call.argument<String>("key")
                var params = call.argument<java.util.HashMap<String, String>>("params")
                StatisticsAdapter.recordCountEvent(category, key, params)
                result.success(null)
                return true
            }
            call.method == "sendRelationApplyMsg" -> {
                var userID = call.argument<String>("userID")
                var uniqID = call.argument<String>("uniqID")
                var content = call.argument<String>("content")
                var expireAt = call.argument<Int>("expireAt")
                var dayAfter = (System.currentTimeMillis() + 24 * 60 * 60 * 1000) / 1000
                ModuleServiceManager.getInstance().msgService.sendRelationInviteMsg(userID, uniqID, content, expireAt?.toLong()
                        ?: dayAfter)
                result.success(null)
                return true
            }
            call.method == "getRemarkName" -> {
                var userID = call.argument<Int>("userID")
                var defaultNickName = call.argument<String>("defaultNickName")
                var remarkName = UserInfoManager.getInstance().getRemarkName(userID!!, defaultNickName)
                result.success(remarkName)
                return true
            }
            call.method == "getCanPlayBgMusic" -> {
                val gameKTVSceneModel = H.partyRoomData?.realRoundInfo?.sceneInfo?.ktv
                MyLog.d("SkrMethodChannelHandler", "handle call = $call, gameKTVSceneModel = $gameKTVSceneModel")
                if (gameKTVSceneModel != null && gameKTVSceneModel.userID === MyUserInfoManager.uid.toInt()) {
                    result.success(false)
                } else {
                    result.success(true)
                }
                return true
            }
            else -> {
                return false
            }
        }
    }

}