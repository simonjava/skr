package com.component.busilib.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.common.core.avatar.AvatarUtils
import com.common.core.myinfo.MyUserInfoManager
import com.common.core.userinfo.model.UserInfoModel
import com.common.utils.U
import com.component.busilib.R
import com.facebook.drawee.view.SimpleDraweeView
import com.zq.live.proto.Common.EVIPType

// 所有的头像
class AvatarView : ConstraintLayout {
    val mTag = "AvatarView"

    private val avatarIv: SimpleDraweeView
    private val vipIv: ImageView

    private var isCircle = true
    private var borderWidth = 0
    private var borderColor = Color.WHITE

    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    init {
        View.inflate(context, R.layout.avatar_view_layout, this)
        avatarIv = findViewById(R.id.avatar)
        vipIv = findViewById(R.id.vip_iv)
    }

    private fun initView(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.avatarView)
        isCircle = typedArray.getBoolean(R.styleable.avatarView_isCircle, true)
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.avatarView_borderWidth, 0)
        borderColor = typedArray.getColor(R.styleable.avatarView_borderColor, Color.WHITE)
        typedArray.recycle()
    }

    fun bindData(model: UserInfoModel?) {
        AvatarUtils.loadAvatarByUrl(avatarIv, AvatarUtils.newParamsBuilder(model?.avatar)
                .setBorderColor(borderColor)
                .setBorderWidth(borderWidth.toFloat())
                .setCircle(isCircle)
                .build())

        when {
            model?.vipInfo?.vipType == EVIPType.EVT_RED_V.value -> {
                vipIv.visibility = View.VISIBLE
                vipIv.setImageResource(R.drawable.vip_red_icon)
            }
            model?.vipInfo?.vipType == EVIPType.EVT_GOLDEN_V.value -> {
                vipIv.visibility = View.VISIBLE
                vipIv.setImageResource(R.drawable.vip_gold_icon)
            }
            else -> {
                vipIv.visibility = View.GONE
            }
        }
    }

    fun bindData(model: UserInfoModel?, isOnline: Boolean) {
        AvatarUtils.loadAvatarByUrl(avatarIv, AvatarUtils.newParamsBuilder(model?.avatar)
                .setBorderColor(borderColor)
                .setGray(isOnline)
                .setBorderWidth(borderWidth.toFloat())
                .setCircle(isCircle)
                .build())
        when {
            model?.vipInfo?.vipType == EVIPType.EVT_RED_V.value -> {
                vipIv.visibility = View.VISIBLE
                vipIv.setImageResource(R.drawable.vip_red_icon)
            }
            model?.vipInfo?.vipType == EVIPType.EVT_GOLDEN_V.value -> {
                vipIv.visibility = View.VISIBLE
                vipIv.setImageResource(R.drawable.vip_gold_icon)
            }
            else -> {
                vipIv.visibility = View.GONE
            }
        }
    }

    fun setImageDrawable(drawble: Drawable) {
        avatarIv.setImageDrawable(drawble)
        vipIv.visibility = View.GONE
    }
}