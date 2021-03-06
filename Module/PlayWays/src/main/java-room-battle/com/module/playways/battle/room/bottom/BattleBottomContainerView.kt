package com.module.playways.battle.room.bottom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.common.rxretrofit.ApiManager
import com.common.view.DebounceViewClickListener
import com.module.playways.BaseRoomData
import com.module.playways.R
import com.module.playways.battle.room.BattleRoomData
import com.module.playways.battle.room.BattleRoomServerApi
import com.module.playways.room.room.view.BottomContainerView
import com.zq.mediaengine.kit.ZqEngineKit
import org.greenrobot.eventbus.EventBus


class BattleBottomContainerView : BottomContainerView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var roomData: BattleRoomData? = null

    internal var mRoomServerApi = ApiManager.getInstance().createService(BattleRoomServerApi::class.java)

    override fun getLayout(): Int {
        return R.layout.battle_bottom_container_view_layout
    }

    override fun init() {
        super.init()

        mInputBtn?.setOnClickListener(object : DebounceViewClickListener() {
            override fun clickValid(v: View) {
                if (roomData?.isMute == true) {
                    roomData?.isMute = false
                    mInputBtn?.setBackgroundResource(R.drawable.relay_new_unmute)
                    ZqEngineKit.getInstance().adjustRecordingSignalVolume(ZqEngineKit.getInstance().params.recordingSignalVolume, false)
                } else {
                    roomData?.isMute = true
                    mInputBtn?.setBackgroundResource(R.drawable.relay_new_mute)
                    ZqEngineKit.getInstance().adjustRecordingSignalVolume(0, false)
                }
            }
        })
    }

    fun unMuteBtn(){
        roomData?.isMute = false
        mInputBtn?.setBackgroundResource(R.drawable.relay_new_unmute)
        ZqEngineKit.getInstance().adjustRecordingSignalVolume(ZqEngineKit.getInstance().params.recordingSignalVolume, false)
    }

    override fun setRoomData(roomData: BaseRoomData<*>) {
        super.setRoomData(roomData)
        this.roomData = roomData as BattleRoomData
    }

    override fun dismissPopWindow() {
        super.dismissPopWindow()
    }

}
