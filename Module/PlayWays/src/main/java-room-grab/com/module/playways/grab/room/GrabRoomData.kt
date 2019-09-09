package com.module.playways.grab.room

import com.common.core.myinfo.MyUserInfoManager
import com.common.core.userinfo.model.UserInfoModel
import com.common.log.MyLog
import com.common.utils.DeviceUtils
import com.common.utils.U
import com.component.busilib.constans.GameModeType
import com.component.busilib.constans.GrabRoomType
import com.component.busilib.friends.SpecialModel
import com.module.playways.BaseRoomData
import com.module.playways.RoomDataUtils
import com.module.playways.grab.room.event.GrabGameOverEvent
import com.module.playways.grab.room.event.GrabRoundChangeEvent
import com.module.playways.grab.room.model.GrabConfigModel
import com.module.playways.grab.room.model.GrabPlayerInfoModel
import com.module.playways.grab.room.model.GrabRoundInfoModel
import com.module.playways.grab.room.model.WorksUploadModel
import com.module.playways.room.prepare.model.JoinGrabRoomRspModel
import com.zq.live.proto.Room.EQRoundStatus
import org.greenrobot.eventbus.EventBus
import java.util.*

class GrabRoomData : BaseRoomData<GrabRoundInfoModel>() {
    //    public static final int ACC_OFFSET_BY_LYRIC = 5000;// 伴奏是比歌词提前 5 秒的
    var tagId: Int = 0//一场到底歌曲分类
    var grabConfigModel = GrabConfigModel()// 一唱到底配置
    var isHasExitGame = false// 是否已经正常退出房间
    private var mIsAccEnable = false// 是否开启伴奏,只代表设置里伴奏开关
    var songLineNum: Int = 0// 歌词总行数
    var roomType: Int = GrabRoomType.ROOM_TYPE_COMMON// 一唱到底房间类型，公开，好友，私密，普通 5为歌单房间
    var ownerId: Int = 0// 房主id
    private var hasGameBegin = true// 游戏是否已经开始
    var specialModel: SpecialModel?=null

    var grabResultData: GrabResultData?=null   // 游戏结果
    var isSpeaking: Boolean = false // 是否正在抢麦说话，一般用于主播控场
    var isChallengeAvailable: Boolean = false
    var roomName: String = ""  // 房间名称
    var ownerKickTimes = 100  // 房主剩余踢人的次数,默认为100

    var isNewUser = false   // 是否是新手引导房间
    private var mOpenRecording = -1 // 是否开启高光时刻

    private val mWorksUploadList = ArrayList<WorksUploadModel>()// 作品时刻本地录音文件路径

    var isVideoRoom = false // 是否是个音频房间


    val isInPlayerList: Boolean
        get() {
            if (expectRoundInfo == null || expectRoundInfo!!.playUsers == null) {
                return false
            }

            val getPlayerInfoList = expectRoundInfo!!.playUsers
            for (model in getPlayerInfoList) {
                if (model.userID.toLong() == MyUserInfoManager.getInstance().uid) {
                    return true
                }
            }

            return false
        }

    override val gameType: Int
        get() = GameModeType.GAME_MODE_GRAB

    var isAccEnable: Boolean
        get() = mIsAccEnable
        set(accEnable) {
            mIsAccEnable = accEnable
            U.getPreferenceUtils().setSettingBoolean("grab_acc_enable1", mIsAccEnable)
        }

    /**
     * 是不是房主
     *
     * @return
     */
    val isOwner: Boolean
        get() = this.ownerId.toLong() == MyUserInfoManager.getInstance().uid

    val worksUploadModel: List<WorksUploadModel>
        get() = mWorksUploadList

    init {
        mIsAccEnable = U.getPreferenceUtils().getSettingBoolean("grab_acc_enable1", false)
    }


    /**
     * 一般不用直接拿来用
     * @return
     */
    override fun getPlayerInfoList(): List<GrabPlayerInfoModel> {
        val l = ArrayList<GrabPlayerInfoModel>()
        if (expectRoundInfo != null) {
            l.addAll(expectRoundInfo!!.playUsers)
            l.addAll(expectRoundInfo!!.waitUsers)
        } else {
            val p = GrabPlayerInfoModel()
            p.isSkrer = false
            p.isOnline = true
            p.role = GrabPlayerInfoModel.ROLE_PLAY
            p.userID = MyUserInfoManager.getInstance().uid.toInt()
            val userInfoModel = UserInfoModel()
            userInfoModel.userId = MyUserInfoManager.getInstance().uid.toInt()
            userInfoModel.avatar = MyUserInfoManager.getInstance().avatar
            userInfoModel.nickname = MyUserInfoManager.getInstance().nickName
            p.userInfo = userInfoModel
            l.add(p)
        }
        return l
    }

    override fun getInSeatPlayerInfoList(): List<GrabPlayerInfoModel> {
        val l = ArrayList<GrabPlayerInfoModel>()
        if (expectRoundInfo != null) {
            l.addAll(expectRoundInfo!!.playUsers)
        }
        return l
    }

    /**
     * 检查轮次信息是否需要更新
     */
    override fun checkRoundInEachMode() {
        if (isIsGameFinish) {
            MyLog.d(TAG, "游戏结束了，不需要再checkRoundInEachMode")
            return
        }
        if (expectRoundInfo == null) {
            MyLog.d(TAG, "尝试切换轮次 checkRoundInEachMode mExpectRoundInfo == null")
            // 结束状态了
            if (realRoundInfo != null) {
                val lastRoundInfoModel = realRoundInfo
                lastRoundInfoModel!!.updateStatus(false, EQRoundStatus.QRS_END.value)
                realRoundInfo = null
                //                if (lastRoundInfoModel != null
                //                        && lastRoundInfoModel.getOverReason() == EQRoundOverReason.ROR_LAST_ROUND_OVER.getValue()
                //                        && lastRoundInfoModel.getResultType() == EQRoundResultType.ROT_TYPE_1.getValue()) {
                //                    // 一唱到底自动加金币
                //                    setCoin(getCoin() + 1);
                //                }
                EventBus.getDefault().post(GrabGameOverEvent(lastRoundInfoModel))
            }
            return
        }
        MyLog.d(TAG, "尝试切换轮次 checkRoundInEachMode mExpectRoundInfo.roundSeq=" + expectRoundInfo!!.roundSeq)
        if (RoomDataUtils.roundSeqLarger<GrabRoundInfoModel>(expectRoundInfo, realRoundInfo) || realRoundInfo == null) {
            // 轮次大于，才切换
            val lastRoundInfoModel = realRoundInfo
            lastRoundInfoModel?.updateStatus(false, EQRoundStatus.QRS_END.value)
            realRoundInfo = expectRoundInfo
            if (realRoundInfo != null) {
                (realRoundInfo as GrabRoundInfoModel).updateStatus(false, EQRoundStatus.QRS_INTRO.value)
            }
            // 告知切换到新的轮次了
            //            if (lastRoundInfoModel != null
            //                    && lastRoundInfoModel.getOverReason() == EQRoundOverReason.ROR_LAST_ROUND_OVER.getValue()
            //                    && lastRoundInfoModel.getResultType() == EQRoundResultType.ROT_TYPE_1.getValue()) {
            //                // 一唱到底自动加金币
            //                setCoin(getCoin() + 1);
            //            }
            EventBus.getDefault().post(GrabRoundChangeEvent(lastRoundInfoModel, realRoundInfo))
        }
    }

    fun hasGameBegin(): Boolean {
        return hasGameBegin
    }

    fun setHasGameBegin(hasGameBegin: Boolean) {
        this.hasGameBegin = hasGameBegin
    }

    fun loadFromRsp(rsp: JoinGrabRoomRspModel) {
        this.gameId = rsp.roomID
        this.setCoin(rsp.coin)
        this.setHzCount(rsp.hongZuan, 0)
        if (rsp.config != null) {
            this.grabConfigModel = rsp.config
        } else {
            MyLog.w(TAG, "JoinGrabRoomRspModel rsp==null")
        }
        val grabRoundInfoModel = rsp.currentRound
        if (grabRoundInfoModel != null) {
            if (rsp.isNewGame) {
                grabRoundInfoModel.isParticipant = true
            } else {
                grabRoundInfoModel.isParticipant = false
                grabRoundInfoModel.enterStatus = grabRoundInfoModel.status
            }
            grabRoundInfoModel.elapsedTimeMs = rsp.elapsedTimeMs
        }
        this.expectRoundInfo = grabRoundInfoModel
        this.realRoundInfo = null
        //            mRoomData.setRealRoundInfo(rsp.getCurrentRound());
        this.tagId = rsp.tagID

        isIsGameFinish = false
        isHasExitGame = false
        this.agoraToken = rsp.agoraToken
        this.roomType = rsp.roomType
        this.ownerId = rsp.ownerID

        if (this.gameCreateTs == 0L) {
            this.gameCreateTs = System.currentTimeMillis()
        }
        if (this.gameStartTs == 0L) {
            this.gameStartTs = this.gameCreateTs
        }
        // 游戏未开始
        this.setHasGameBegin(rsp.hasGameBegin())
        this.isChallengeAvailable = rsp.isChallengeAvailable
        this.roomName = rsp.roomName
        this.isVideoRoom = rsp.mediaType == 2
    }

    override fun toString(): String {
        return "GrabRoomData{" +
                ", mTagId=" + tagId +
                ", mGrabConfigModel=" + grabConfigModel +
                ", roomType=" + roomType +
                ", ownerId=" + ownerId +
                ", hasGameBegin=" + hasGameBegin +
                ", mAgoraToken=" + agoraToken +
                '}'.toString()
    }


    //    GrabGuideInfoModel mGrabGuideInfoModel;
    //
    //    public void setGrabGuideInfoModel(GrabGuideInfoModel grabGuideInfoModel) {
    //        mGrabGuideInfoModel = grabGuideInfoModel;
    //    }
    //
    //    public GrabGuideInfoModel getGrabGuideInfoModel() {
    //        return mGrabGuideInfoModel;
    //    }

    fun openAudioRecording(): Boolean {
        if (true) {
            return false
        }
        if (mOpenRecording == -1) {
            if (U.getDeviceUtils().level == DeviceUtils.LEVEL.BAD) {
                MyLog.w(TAG, "设备太差，不开启录制")
                mOpenRecording = 0
            } else {
                mOpenRecording = 1
            }
        }
        return mOpenRecording == 1
    }


    fun addWorksUploadModel(savePath: WorksUploadModel) {
        mWorksUploadList.add(savePath)
    }
}