package com.common

import com.alibaba.fastjson.JSON
import com.common.core.myinfo.MyUserInfoManager
import com.common.flutter.plugin.MethodHandler
import com.common.rxretrofit.httpGet
import com.common.rxretrofit.httpPost
import com.common.rxretrofit.httpPut
import com.common.utils.U
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
        }
        return false
    }

}