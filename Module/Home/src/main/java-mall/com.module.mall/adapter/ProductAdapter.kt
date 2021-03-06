package com.module.mall.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.core.avatar.AvatarUtils
import com.common.core.view.setDebounceViewClickListener
import com.common.image.fresco.BaseImageView
import com.common.utils.ImageUtils
import com.common.utils.U
import com.common.view.ex.ExImageView
import com.common.view.ex.ExTextView
import com.common.view.recyclerview.DiffAdapter
import com.module.home.R
import com.module.mall.model.ProductModel

class ProductAdapter(val getIndexMethod: (() -> Int)) : DiffAdapter<ProductModel, ProductAdapter.ProductHolder>() {

    var clickItemMethod: ((ProductModel) -> Unit)? = null
    var selectItemMethod: ((ProductModel, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mall_product_item_layout, parent, false)
        return ProductHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bindData(mDataList[position], position)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            holder.bindData(mDataList[position], position)
        } else {
            // 局部刷新
            payloads.forEach {
                if (it is Int) {
                    refreshHolder(holder, position, it)
                }
            }
        }
    }

    fun refreshHolder(holder: ProductHolder, position: Int, refreshType: Int) {
        holder.updateText(mDataList[position], position)
    }

    inner class ProductHolder : RecyclerView.ViewHolder {
        var bg: ExImageView
        var productName: ExTextView
        var btnIv: ExTextView
        var hasBuyIv: ExTextView
        var strokeIv: ExImageView
        var effectIv: BaseImageView

        var model: ProductModel? = null
        var index: Int = -1

        var coinDrawable = U.getDrawable(R.drawable.grab_coin_icon).apply {
            setBounds(0, 0, getIntrinsicWidth(), getIntrinsicHeight())
        }

        var diamondDrawable = U.getDrawable(R.drawable.mall_diamond).apply {
            setBounds(0, 0, getIntrinsicWidth(), getIntrinsicHeight())
        }

        constructor(itemView: View) : super(itemView) {
            bg = itemView.findViewById(R.id.bg)
            productName = itemView.findViewById(R.id.product_name)
            btnIv = itemView.findViewById(R.id.btn_iv)
            strokeIv = itemView.findViewById(R.id.stroke_iv)
            effectIv = itemView.findViewById(R.id.effect_iv)
            hasBuyIv = itemView.findViewById(R.id.has_buy_iv)

            btnIv.setDebounceViewClickListener {
                clickItemMethod?.invoke(model!!)
            }

            hasBuyIv.setDebounceViewClickListener {
                clickItemMethod?.invoke(model!!)
            }

            effectIv.setDebounceViewClickListener {
                selectItemMethod?.invoke(model!!, index)
            }
        }

        fun bindData(model: ProductModel, index: Int) {
            this.model = model
            this.index = index
            AvatarUtils.loadAvatarByUrl(effectIv,
                    AvatarUtils.newParamsBuilder(model.goodsURL)
                            .setSizeType(ImageUtils.SIZE.SIZE_160)
                            .setCornerRadius(U.getDisplayUtils().dip2px(8f).toFloat())
                            .build())
            productName.text = model.goodsName

            updateText(model, index)
        }

        fun updateText(model: ProductModel, index: Int) {
            this.model = model
            this.index = index

            if (model.isBuy) {
                hasBuyIv.visibility = View.VISIBLE
                btnIv.visibility = View.GONE
            } else {
                hasBuyIv.visibility = View.GONE
                btnIv.visibility = View.VISIBLE

                if (model.price?.size > 0) {
                    btnIv.text = "X${model.price[0].realPrice}"
                    if (model.price[0].priceType == 1) {
                        //PT_Coin
                        btnIv.setCompoundDrawables(coinDrawable, null, null, null)
                    } else {
                        //PT_Zuan
                        btnIv.setCompoundDrawables(diamondDrawable, null, null, null)
                    }
                }
            }

            if (getIndexMethod.invoke() == index) {
                strokeIv.visibility = View.VISIBLE
            } else {
                strokeIv.visibility = View.GONE
            }
        }
    }
}

