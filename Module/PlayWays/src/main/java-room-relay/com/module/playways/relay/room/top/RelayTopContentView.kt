package com.module.playways.relay.room.top

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.common.core.avatar.AvatarUtils
import com.common.core.myinfo.MyUserInfoManager
import com.common.core.view.setDebounceViewClickListener
import com.common.utils.U
import com.common.utils.dp
import com.common.view.ex.ExConstraintLayout
import com.common.view.ex.ExTextView
import com.component.person.event.ShowPersonCardEvent
import com.facebook.drawee.view.SimpleDraweeView
import com.module.playways.BaseRoomData
import com.module.playways.R
import com.module.playways.relay.room.RelayRoomData
import com.module.playways.relay.room.event.RelayLockChangeEvent
import com.zq.live.proto.RelayRoom.RMuteMsg
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

// 顶部头像栏
class RelayTopContentView : ExConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val mTag = "RelayTopContentView"

    private val arrowIv: ImageView
    private val unlimitIv: ImageView
    private val leftAvatarSdv: SimpleDraweeView
    private val leftMuteIv: ImageView
    private val rightMuteIv: ImageView

    private val rightAvatarSdv: SimpleDraweeView
    private val rightAvatarBg: ExTextView
    private val countTimeTv: TextView
    private var anim: ObjectAnimator? = null
    private var countTimeScaleAnimator: ObjectAnimator? = null

    var listener: Listener? = null
    var mIsOpen = true
    var countDownJob: Job? = null

    var roomData: RelayRoomData? = null


    init {
        View.inflate(context, R.layout.relay_top_content_view_layout, this)

        U.getSoundUtils().preLoad(mTag, R.raw.double_chat_cd)
        arrowIv = this.findViewById(R.id.arrow_iv)
        leftAvatarSdv = this.findViewById(R.id.left_avatar_sdv)
        leftMuteIv = this.findViewById(R.id.left_mute_iv)
        rightMuteIv = this.findViewById(R.id.right_mute_iv)

        rightAvatarSdv = this.findViewById(R.id.right_avatar_sdv)
        rightAvatarBg = this.findViewById(R.id.right_avatar_bg)
        countTimeTv = this.findViewById(R.id.count_time_tv)
        unlimitIv = this.findViewById(R.id.unlimit_iv)

//        loveBg.setDebounceViewClickListener {
//            listener?.clickLove()
//        }

        arrowIv.setDebounceViewClickListener {
            listener?.clickArrow(!mIsOpen)
        }
        leftMuteIv.visibility = View.GONE
        rightMuteIv.visibility = View.GONE

        leftAvatarSdv.setDebounceViewClickListener {
            if (roomData?.leftSeat == true) {
                EventBus.getDefault().post(ShowPersonCardEvent(MyUserInfoManager.uid.toInt()))
            } else {
                EventBus.getDefault().post(ShowPersonCardEvent(roomData?.peerUser?.userID ?: 0))
            }
        }

        rightAvatarSdv.setDebounceViewClickListener {
            if (roomData?.leftSeat == true) {
                EventBus.getDefault().post(ShowPersonCardEvent(roomData?.peerUser?.userID ?: 0))
            } else {
                EventBus.getDefault().post(ShowPersonCardEvent(MyUserInfoManager.uid.toInt()))
            }
        }

        rightAvatarBg.setDebounceViewClickListener {
            listener?.clickInvite()
        }
    }

    fun setArrowIcon(open: Boolean) {
        if (open) {
            // 展开状态
            mIsOpen = true
            arrowIv.setImageResource(R.drawable.race_expand_icon)
        } else {
            // 折叠状态
            mIsOpen = false
            arrowIv.setImageResource(R.drawable.race_shrink_icon)
        }
    }

    fun bindData() {
        if (roomData?.leftSeat == true) {
            AvatarUtils.loadAvatarByUrl(leftAvatarSdv, AvatarUtils.newParamsBuilder(MyUserInfoManager.avatar)
                    .setCircle(true)
                    .setBorderColor(Color.parseColor("#ffd8d8d8"))
                    .setBorderWidth(0.dp().toFloat())
                    .build())

            //创建房间的人显示在昨天
            if (roomData?.isPersonArrive() == true) {
                rightAvatarSdv.visibility = View.VISIBLE
                rightAvatarSdv.isEnabled = true

                AvatarUtils.loadAvatarByUrl(rightAvatarSdv, AvatarUtils.newParamsBuilder(roomData?.peerUser?.userInfo?.avatar)
                        .setCircle(true)
                        .setBorderColor(Color.parseColor("#ffd8d8d8"))
                        .setBorderWidth(0.dp().toFloat())
                        .build())
            } else {
                rightAvatarSdv.visibility = View.INVISIBLE
                rightAvatarSdv.isEnabled = false
            }
        } else {
            AvatarUtils.loadAvatarByUrl(rightAvatarSdv, AvatarUtils.newParamsBuilder(MyUserInfoManager.avatar)
                    .setCircle(true)
                    .setBorderColor(Color.parseColor("#ffd8d8d8"))
                    .setBorderWidth(0.dp().toFloat())
                    .build())
            AvatarUtils.loadAvatarByUrl(leftAvatarSdv, AvatarUtils.newParamsBuilder(roomData?.peerUser?.userInfo?.avatar)
                    .setCircle(true)
                    .setBorderColor(Color.parseColor("#ffd8d8d8"))
                    .setBorderWidth(0.dp().toFloat())
                    .build())
        }

        if (roomData?.hasTimeLimit() == true) {
            countTimeTv.visibility = View.GONE
            unlimitIv.visibility = View.VISIBLE
        } else {
            countTimeTv.text = U.getDateTimeUtils().formatVideoTime(5 * 60 * 1000)
            unlimitIv.visibility = View.GONE
        }
    }

    fun launchCountDown() {
        if (countTimeTv.visibility == View.VISIBLE) {
            var music = roomData?.realRoundInfo?.music
            countDownJob = launch {
                var t = roomData?.configModel?.durationTimeMs ?: 0
                var i = 0
                var offsetTs = (System.currentTimeMillis() - BaseRoomData.shiftTsForRelay) - (roomData?.gameCreateTs
                        ?: 0)
                if (offsetTs < 1000) {
                    offsetTs = 0
                }

                while (i <= t) {
                    i++
                    var leftTs = t - (1000L * i) - offsetTs
                    if (leftTs < 0) {
                        leftTs = 0
                    }
                    countTimeTv.text = U.getDateTimeUtils().formatVideoTime(leftTs)
                    if (leftTs <= 10 * 1000L) {
                        U.getSoundUtils().play(mTag, R.raw.double_chat_cd)
                        countTimeTv?.setTextColor(Color.parseColor("#FFC15B"))
                        countTimeScaleAnimator?.cancel()
                        if (countTimeScaleAnimator == null) {
                            countTimeScaleAnimator = ObjectAnimator.ofPropertyValuesHolder(countTimeTv,
                                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1.5f, 1.0f),
                                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.5f, 1.0f))
                            countTimeScaleAnimator?.duration = 1000
                        }
                        countTimeScaleAnimator?.start()
                    }
                    if (leftTs == 0L) {
                        countTimeScaleAnimator?.cancel()
                        break
                    }
                    delay(1000)
                }
                listener?.countDownOver()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RelayLockChangeEvent) {
        if (roomData?.unLockMe == true && roomData?.unLockPeer == true) {
            unlimitIv.visibility = View.VISIBLE
            countTimeTv.visibility = View.GONE
            countDownJob?.cancel()
        } else if (roomData?.unLockMe == false && roomData?.unLockPeer == false) {
//            loveBg.isClickable = true
//            loveBg.setImageResource(R.drawable.normal_love_icon)
        } else if (roomData?.unLockMe == true) {
//            loveBg.isClickable = false
//            tipsIv.visibility = View.GONE
//            if (roomData?.leftSeat == true) {
//                loveBg.setImageResource(R.drawable.light_left_love_icon)
//            } else {
//                loveBg.setImageResource(R.drawable.light_right_love_icon)
//            }
        } else if (roomData?.unLockPeer == true) {
//            loveBg.isClickable = true
//            tipsIv.visibility = View.VISIBLE
//            if (roomData?.leftSeat == true) {
//                loveBg.setImageResource(R.drawable.light_right_love_icon)
//            } else {
//                loveBg.setImageResource(R.drawable.light_left_love_icon)
//            }
        }
    }


    fun getViewLeft(userID: Int): Int {
        var userSeatLeft = true  // 默认这个id在左边的位置上
        userSeatLeft = if (userID == MyUserInfoManager.uid.toInt()) {
            roomData?.leftSeat ?: true
        } else {
            !(roomData?.leftSeat ?: true)
        }

        return if (userSeatLeft) {
            // 左边的位置
            U.getDisplayUtils().screenWidth / 2 - 85.dp() + 21.dp() - 8.dp()
        } else {
            // 右边的位置
            U.getDisplayUtils().screenWidth / 2 + 85.dp() - 21.dp() - 8.dp()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RMuteMsg) {
        if (event.userID == MyUserInfoManager.uid.toInt()) {
            if (roomData?.leftSeat == true) {
                if (event.isMute) {
                    leftMuteIv.visibility = View.VISIBLE
                } else {
                    leftMuteIv.visibility = View.GONE
                }
            } else {
                if (event.isMute) {
                    rightMuteIv.visibility = View.VISIBLE
                } else {
                    rightMuteIv.visibility = View.GONE
                }
            }
        } else if (event.userID == roomData?.peerUser?.userID) {
            if (roomData?.leftSeat == true) {
                if (event.isMute) {
                    rightMuteIv.visibility = View.VISIBLE
                } else {
                    rightMuteIv.visibility = View.GONE
                }
            } else {
                if (event.isMute) {
                    leftMuteIv.visibility = View.VISIBLE
                } else {
                    leftMuteIv.visibility = View.GONE
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        U.getSoundUtils().release(mTag)
        countTimeScaleAnimator?.cancel()
        anim?.cancel()
    }

    interface Listener {
        fun clickArrow(open: Boolean)
        fun clickLove()
        fun countDownOver()
        fun clickInvite()
    }
}