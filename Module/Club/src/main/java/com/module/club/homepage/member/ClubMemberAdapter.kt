package com.module.club.homepage.member

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.common.view.ex.ExTextView
import com.component.busilib.view.AvatarView
import com.module.club.R

class ClubMemberAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mDataList = ArrayList<ClubMemberInfoModel>()
    var mTotal = 0

    private val ITEM_TYPE_NORMAL = 1
    private val ITEM_TYPE_LAST = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_NORMAL) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.club_member_view_item_layout, parent, false)
            ClubMemberViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.club_member_view_last_layout, parent, false)
            LastViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return if (mDataList.size > 5) {
            6  // 最多就6个
        } else {
            mDataList.size + 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == (itemCount - 1)) {
            ITEM_TYPE_LAST
        } else {
            ITEM_TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ClubMemberViewHolder) {
            holder.bindData(position, mDataList[position])
        } else if (holder is LastViewHolder) {
            holder.bindData(position, mTotal)
        }
    }

    inner class ClubMemberViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        private val avatarView: AvatarView = item.findViewById(R.id.avatar_view)
        private val roleTagTv: TextView = item.findViewById(R.id.role_tag_tv)

        var mPos = -1
        var mModel: ClubMemberInfoModel? = null

        fun bindData(position: Int, model: ClubMemberInfoModel) {
            this.mPos = position
            this.mModel = model

            // todo
        }
    }

    inner class LastViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val totalNumTv: ExTextView = item.findViewById(R.id.total_num_tv)
        val moreIv: ImageView = item.findViewById(R.id.more_iv)

        fun bindData(position: Int, total: Int) {
            totalNumTv.text = "${total}人"
        }
    }
}