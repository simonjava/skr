package com.module.feeds.detail.adapter

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.common.core.avatar.AvatarUtils
import com.common.core.userinfo.UserInfoManager
import com.common.image.fresco.BaseImageView
import com.common.utils.U
import com.common.view.DebounceViewClickListener
import com.common.view.ex.ExTextView
import com.common.view.recyclerview.DiffAdapter
import com.component.busilib.view.AvatarView
import com.module.RouterConstants
import com.module.feeds.R
import com.module.feeds.detail.model.FeedLikeModel


class LikeWorkAdapter(val listener: FeedLikeListener) : DiffAdapter<FeedLikeModel, LikeWorkAdapter.LikeWorkHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeWorkHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.like_work_item_layout, parent, false)
        return LikeWorkHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: LikeWorkHolder, position: Int) {
        holder.bindData(mDataList[position])
    }

    inner class LikeWorkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mAvatarIv: AvatarView
        val mTitleTv: ExTextView
        val mSubTitleTv: ExTextView
        val mTimeTv: ExTextView
        var model: FeedLikeModel? = null

        init {
            mAvatarIv = itemView.findViewById(R.id.avatar_iv)
            mTitleTv = itemView.findViewById(R.id.title_tv)
            mSubTitleTv = itemView.findViewById(R.id.sub_title_tv)
            mTimeTv = itemView.findViewById(R.id.time_tv)

            itemView.setOnClickListener(object : DebounceViewClickListener() {
                override fun clickValid(v: View?) {
                    listener.onClickItme(model)
                }
            })
        }

        fun bindData(model: FeedLikeModel) {
            this.model = model
            mAvatarIv.bindData(model.toUserInfoModel())
            mTitleTv.text = UserInfoManager.getInstance().getRemarkName(model.userID, model.nickname)
            mSubTitleTv.text = model.actionDesc
            mTimeTv.text = U.getDateTimeUtils().formatHumanableDateForSkrFeed(model.timeMs, System.currentTimeMillis())
            mAvatarIv?.setOnClickListener(object : DebounceViewClickListener() {
                override fun clickValid(v: View?) {
                    val bundle = Bundle()
                    bundle.putInt("bundle_user_id", model?.userID)
                    ARouter.getInstance()
                            .build(RouterConstants.ACTIVITY_OTHER_PERSON)
                            .with(bundle)
                            .navigation()
                }
            })
        }
    }
}

interface FeedLikeListener {
    fun onClickItme(model: FeedLikeModel?)
}