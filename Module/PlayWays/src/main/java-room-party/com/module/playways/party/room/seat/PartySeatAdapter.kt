package com.module.playways.party.room.seat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.common.log.MyLog
import com.module.playways.R
import com.module.playways.party.room.model.PartyActorInfoModel
import com.module.playways.party.room.model.PartyEmojiInfoModel
import com.module.playways.party.room.model.PartySeatInfoModel
import com.zq.live.proto.PartyRoom.ESeatStatus

class PartySeatAdapter(var listener: Listener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mTag = "PartySeatAdapter"

    private val VIEW_TYPE_NORMAL = 1  // 有人的席位
    private val VIEW_TYPE_EMPTY = 2  // 空席位 (分为空的和被关闭的两种)

    var mDataList = HashMap<Int, PartyActorInfoModel>()  // 席位对应的嘉宾(席位从1开始)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_NORMAL) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.party_seat_item_view_layout, parent, false)
            SeatViewHolder(view, listener)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.party_seat_empty_item_layout, parent, false)
            EmptySeatViewHolder(view, listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        var d = mDataList[position]
        if(d==null || (d.seat?.userID?:0)<=0){
            // 位置为空 或者为 关闭状态
            return VIEW_TYPE_EMPTY
        }else{
            return VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (holder is SeatViewHolder) {
            if (payloads.isEmpty()) {
                holder.bindData(position, mDataList[position + 1])
            } else {
                payloads.forEach { refreshType ->
                    if (refreshType is Int) {
                        when (refreshType) {
                            REFRESH_MUTE -> {
                                holder.refreshMute()
                            }
                            REFRESH_HOT -> {
                                holder.refreshHot()
                            }
                        }
                    } else if (refreshType is PartyEmojiInfoModel) {
                        // 刷个表情
                    }
                }
            }
        } else if (holder is EmptySeatViewHolder) {
            holder.bindData(position, mDataList[position + 1])
        }
    }

    fun showUserEmoji(seatInfoModel: PartySeatInfoModel, model: PartyEmojiInfoModel) {
        if (mDataList[seatInfoModel.seatSeq]?.seat?.userID == seatInfoModel.userID) {
            // 和mDataList中数据一致
            notifyItemChanged((seatInfoModel.seatSeq - 1), model)
        } else {
            MyLog.d(mTag, "showUserEmoji seatInfoModel = $seatInfoModel, model = $model")
        }
    }

    // 全量的局部刷新
    fun refreshAllWithType(refreshType: Int) {
        notifyItemRangeChanged(0, itemCount, refreshType)
    }

    companion object {
        // 表情可以直接用表情的model来局部刷新
        const val REFRESH_MUTE = 1   // 局部刷新，静音
        const val REFRESH_HOT = 2     // 局部刷新，热度
    }

    interface Listener {
        fun onClickItem(position: Int, model: PartyActorInfoModel?)
    }
}