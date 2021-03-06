package com.module.playways.room.prepare.model

import com.module.playways.grab.room.model.GrabConfigModel
import com.module.playways.grab.room.model.GrabPlayerInfoModel
import com.module.playways.grab.room.model.GrabRoundInfoModel

import java.io.Serializable

/*
{
  "data": {
    "coin": 0,
    "currentRound": {
      "bLightInfo": [
        {
          "roundSeq": 0,
          "userID": 0
        }
      ],
      "getSingMs": 0,
      "introBeginMs": 0,
      "introEndMs": 0,
      "mLightInfo": [
        {
          "roundSeq": 0,
          "userID": 0
        }
      ],
      "music": {
        "acc": "string",
        "beginTimeMs": 0,
        "cover": "string",
        "endTimeMs": 0,
        "isBlank": true,
        "itemID": 0,
        "itemName": "string",
        "lyric": "string",
        "midi": "string",
        "ori": "string",
        "owner": "string",
        "rankLrcBeginT": 0,
        "rankLrcEndT": 0,
        "rankUserVoice": "string",
        "standIntro": "string",
        "standIntroBeginT": 0,
        "standIntroEndT": 0,
        "standLrc": "string",
        "standLrcBeginT": 0,
        "standLrcEndT": 0,
        "totalTimeMs": 0,
        "zip": "string"
      },
      "noPassSingInfos": [
        {
          "timeMs": 0,
          "userID": 0
        }
      ],
      "overReason": "ROR_UNKNOWN",
      "playbookID": 0,
      "resultType": "ROT_UNKNOWN",
      "roundInit": {
        "userCnt": 0
      },
      "roundSeq": 0,
      "singBeginMs": 0,
      "singEndMs": 0,
      "skrResource": {
        "audioURL": "string",
        "itemID": 0,
        "midiURL": "string",
        "resourceID": 0,
        "sysScore": 0
      },
      "status": "QRS_UNKNOWN",
      "userID": 0,
      "wantSingInfos": [
        {
          "timeMs": 0,
          "userID": 0
        }
      ]
    },
    "elapsedTimeMs": 0,
    "gameOverTimeMs": 0,
    "nextRound": {
      "bLightInfo": [
        {
          "roundSeq": 0,
          "userID": 0
        }
      ],
      "getSingMs": 0,
      "introBeginMs": 0,
      "introEndMs": 0,
      "mLightInfo": [
        {
          "roundSeq": 0,
          "userID": 0
        }
      ],
      "music": {
        "acc": "string",
        "beginTimeMs": 0,
        "cover": "string",
        "endTimeMs": 0,
        "isBlank": true,
        "itemID": 0,
        "itemName": "string",
        "lyric": "string",
        "midi": "string",
        "ori": "string",
        "owner": "string",
        "rankLrcBeginT": 0,
        "rankLrcEndT": 0,
        "rankUserVoice": "string",
        "standIntro": "string",
        "standIntroBeginT": 0,
        "standIntroEndT": 0,
        "standLrc": "string",
        "standLrcBeginT": 0,
        "standLrcEndT": 0,
        "totalTimeMs": 0,
        "zip": "string"
      },
      "noPassSingInfos": [
        {
          "timeMs": 0,
          "userID": 0
        }
      ],
      "overReason": "ROR_UNKNOWN",
      "playbookID": 0,
      "resultType": "ROT_UNKNOWN",
      "roundInit": {
        "userCnt": 0
      },
      "roundSeq": 0,
      "singBeginMs": 0,
      "singEndMs": 0,
      "skrResource": {
        "audioURL": "string",
        "itemID": 0,
        "midiURL": "string",
        "resourceID": 0,
        "sysScore": 0
      },
      "status": "QRS_UNKNOWN",
      "userID": 0,
      "wantSingInfos": [
        {
          "timeMs": 0,
          "userID": 0
        }
      ]
    },
    "onlineInfo": [
      {
        "isOnline": true,
        "isSkrer": true,
        "userID": 0,
        "userInfo": {
          "avatar": "string",
          "description": "string",
          "isSystem": true,
          "mainLevel": 0,
          "nickName": "string",
          "sex": "SX_UNKNOWN",
          "userID": 0
        }
      }
    ],
    "syncStatusTimeMs": 0
  },
  "errmsg": "string",
  "errno": 0,
  "traceId": "string"
}
 */
class JoinGrabRoomRspModel : Serializable {
    var inChallenge = false
    var maxGetBLightCnt = -1
    var coin: Int = 0 // ????????????
    var config: GrabConfigModel? = null//?????????????????????
    var currentRound: GrabRoundInfoModel? = null // ????????????
    var nextRound: GrabRoundInfoModel? = null // ???????????????????????????
    var elapsedTimeMs: Int = 0//??????????????????????????????????????????????????????????????????????????????????????????
    var gameOverTimeMs: Int = 0// ????????????
    var roomID: Int = 0// ??????id
    var syncStatusTimeMs: Int = 0 // ????????????
    var tagID: Int = 0// ????????????
    var isNewGame: Boolean = false// ????????????????????????
    var agoraToken: String? = null// ??????token
    var roomType: Int = 0// ???????????????????????????????????????????????????????????? 5?????????
    var ownerID: Int = 0// ??????id
    var gameCreateTimeMs: Long = 0// ?????????????????????????????????
    var gameStartTimeMs: Long = 0// ????????????????????????????????????????????????????????????????????? gameCreateTimeMs
    var isHasGameBegin: Boolean? = null// ????????????????????????
    var isChallengeAvailable = false// ?????????????????????
    var roomName: String? = null    //????????????
    var hongZuan: Float = 0.toFloat() // ??????
    var mediaType: Int = 0// 2????????????
    var waitUsers: List<GrabPlayerInfoModel>? = null // ???????????????????????????????????????

    override fun toString(): String {
        return "JoinGrabRoomRspModel{" +
                "coin=" + coin +
                ", config=" + config +
                ", currentRound=" + currentRound +
                ", nextRound=" + nextRound +
                ", elapsedTimeMs=" + elapsedTimeMs +
                ", gameOverTimeMs=" + gameOverTimeMs +
                ", roomID=" + roomID +
                ", syncStatusTimeMs=" + syncStatusTimeMs +
                ", tagID=" + tagID +
                ", isNewGame=" + isNewGame +
                ", agoraToken='" + agoraToken + '\''.toString() +
                ", roomType=" + roomType +
                ", ownerID=" + ownerID +
                ", gameStartTimeMs=" + gameStartTimeMs +
                ", hasGameBegin=" + isHasGameBegin +
                ", challengeAvailable=" + isChallengeAvailable +
                ", roomName='" + roomName + '\''.toString() +
                '}'.toString()
    }
}
