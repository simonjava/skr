package com.module.playways.grab.room.view.chorus

import android.os.Handler
import android.view.View
import android.view.ViewStub
import com.common.core.view.setDebounceViewClickListener
import com.module.playways.R
import com.module.playways.room.data.H


/**
 * 合唱的歌唱者看到的板子
 */
class ChorusSelfSingCardView(viewStub: ViewStub) : BaseChorusSelfCardView(viewStub) {

//    internal var mSingCountDownView: SingCountDownView2? = null

    override val isForVideo: Boolean
        get() = false

    var hander = Handler()
    override fun init(parentView: View) {
        super.init(parentView)
//        mSingCountDownView = mParentView!!.findViewById(R.id.sing_count_down_view)
//        mSingCountDownView?.setListener(mOverListener)
        parentView.findViewById<View>(R.id.relative_container).setDebounceViewClickListener { }
    }

    override fun layoutDesc(): Int {
        return R.layout.grab_chorus_self_sing_card_stub_layout
    }

    public override fun playLyric(): Boolean {
        if (super.playLyric() == true) {
            if (H.isGrabRoom()) {
                hander.postDelayed({mOverListener?.invoke()}, H.grabRoomData?.realRoundInfo?.singTotalMs?.toLong() ?: 0L)
//                mSingCountDownView?.startPlay(0, H.grabRoomData!!.realRoundInfo!!.singTotalMs, true)
            } else if (H.isMicRoom()) {
                hander.postDelayed({mOverListener?.invoke()}, H.micRoomData?.realRoundInfo?.singTotalMs?.toLong() ?: 0L)
//                mSingCountDownView?.startPlay(0, H.micRoomData!!.realRoundInfo!!.singTotalMs, true)
            }
            return true
        } else {
            return false
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE) {
//            mSingCountDownView?.reset()
            hander.removeCallbacksAndMessages(null)
        }
    }
}
