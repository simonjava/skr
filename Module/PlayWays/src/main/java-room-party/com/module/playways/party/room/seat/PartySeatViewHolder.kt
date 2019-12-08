package com.module.playways.party.room.seat

import android.graphics.Color
import android.support.constraint.Group
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.common.core.avatar.AvatarUtils
import com.common.core.view.setDebounceViewClickListener
import com.common.utils.U
import com.common.utils.dp
import com.common.view.ex.ExImageView
import com.common.view.ex.ExTextView
import com.facebook.drawee.view.SimpleDraweeView
import com.module.playways.R
import com.module.playways.party.room.model.PartyActorInfoModel
import com.zq.live.proto.PartyRoom.EMicStatus
import com.zq.live.proto.PartyRoom.ESeatStatus

// 正常位置
class SeatViewHolder(item: View, var listener: PartySeatAdapter.Listener?) : RecyclerView.ViewHolder(item) {

    private val avatarSdv: SimpleDraweeView = item.findViewById(R.id.avatar_sdv)
    private val hotTv: ExTextView = item.findViewById(R.id.hot_tv)
    private val nameTv: TextView = item.findViewById(R.id.name_tv)
    private val muteArea: Group = item.findViewById(R.id.mute_area)
    private val muteBg: ExImageView = item.findViewById(R.id.mute_bg)
    private val muteIv: ImageView = item.findViewById(R.id.mute_iv)
    private val emojiSdv: SimpleDraweeView = item.findViewById(R.id.emoji_sdv)

    var mModel: PartyActorInfoModel? = null
    var mPos: Int = -1

    init {
        item.setDebounceViewClickListener {
            listener?.onClickItem(mPos, mModel)
        }
    }

    fun bindData(position: Int, model: PartyActorInfoModel?) {
        this.mModel = model
        this.mPos = position


        AvatarUtils.loadAvatarByUrl(avatarSdv,
                AvatarUtils.newParamsBuilder(model?.player?.userInfo?.avatar)
                        .setCircle(true)
                        .setBorderWidth(1.dp().toFloat())
                        .setBorderColor(Color.WHITE)
                        .build())
        nameTv.text = "${model?.player?.userInfo?.nicknameRemark}"
        refreshHot()
        refreshMute()
    }

    fun refreshMute() {
        if (mModel?.seat?.micStatus == EMicStatus.MS_CLOSE.value) {
            muteArea.visibility = View.VISIBLE
        } else {
            muteArea.visibility = View.GONE
        }
    }

    fun refreshHot() {
        hotTv.text = "{mModel?.player?.popularity?:0}"
    }
}


// 空座位
class EmptySeatViewHolder(item: View, var listener: PartySeatAdapter.Listener?) : RecyclerView.ViewHolder(item) {

    private val emptyBg: ExImageView = item.findViewById(R.id.empty_bg)
    private val emptyTv: TextView = item.findViewById(R.id.empty_tv)

    var mPos: Int = -1
    var mModel: PartyActorInfoModel? = null

    init {
        item.setDebounceViewClickListener {
            listener?.onClickItem(mPos, mModel)
        }
    }

    fun bindData(position: Int, model: PartyActorInfoModel?) {
        this.mPos = position
        this.mModel = model

        if (model?.seat?.seatStatus == ESeatStatus.SS_CLOSE.value) {
            // 关闭席位的UI
            emptyTv.text = "已关闭"
            emptyTv.setTextColor(U.getColor(R.color.white_trans_50))
            emptyTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 14.dp().toFloat())
        } else {
            emptyTv.text = "${position + 1}"
            emptyTv.setTextColor(U.getColor(R.color.white_trans_80))
            emptyTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 24.dp().toFloat())
        }
    }
}