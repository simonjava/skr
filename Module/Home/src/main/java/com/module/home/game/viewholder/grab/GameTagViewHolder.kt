package com.module.home.game.viewholder.grab

import android.support.v7.widget.RecyclerView
import android.view.View
import com.common.image.fresco.FrescoWorker
import com.common.image.model.BaseImage
import com.common.image.model.ImageFactory
import com.common.utils.U
import com.common.view.AnimateClickListener
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.SimpleDraweeView
import com.module.home.R
import com.module.home.game.model.GrabSpecialModel

class GameTagViewHolder(itemView: View, onClickTagListener: ((model: GrabSpecialModel?) -> Unit)?) : RecyclerView.ViewHolder(itemView) {

    var model: GrabSpecialModel? = null
    var pos: Int = 0

    val mImageSdv: SimpleDraweeView = itemView.findViewById(R.id.image_sdv)

    init {
        itemView.setOnClickListener(object : AnimateClickListener() {
            override fun click(view: View?) {
                onClickTagListener?.invoke(model)
            }
        })
    }

    fun bind(position: Int, model: GrabSpecialModel, type: Int) {
        this.model = model
        this.pos = position

        model.model?.biggest?.let {
            if (type == 1) {
                var lp = mImageSdv.layoutParams
                // 左边12 右边6
                lp.width = U.getDisplayUtils().screenWidth / 2 - U.getDisplayUtils().dip2px(18f)
                lp.height = (it.h) * lp.width / (it.w)
                mImageSdv.layoutParams = lp

                FrescoWorker.loadImage(mImageSdv, ImageFactory.newPathImage(it.url)
                        .setScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .build<BaseImage>())
            } else {
                var lp = mImageSdv.layoutParams
                // 左边12 右边6
                lp.width = U.getDisplayUtils().screenWidth / 2 - U.getDisplayUtils().dip2px(16f)
                lp.height = (it.h) * lp.width / (it.w)
                mImageSdv.layoutParams = lp

                FrescoWorker.loadImage(mImageSdv, ImageFactory.newPathImage(it.url)
                        .setScaleType(ScalingUtils.ScaleType.FIT_XY)
                        .build<BaseImage>())
            }

        }
    }
}
