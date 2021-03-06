package com.module.playways.audition.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.component.voice.control.VoiceControlPanelView
import com.kyleduo.switchbutton.SwitchButton
import com.module.playways.R
import com.zq.mediaengine.kit.ZqEngineKit

const val LATENCY_TEST_MODE = false

class AudienceVoiceControlPanelView(context: Context, attrs: AttributeSet) : VoiceControlPanelView(context, attrs) {
    protected var mMixTv: TextView? = null
    protected var mMixSb: SwitchButton? = null
    protected var mLowLatencyTv: TextView? = null
    protected var mLowLatencySb: SwitchButton? = null

    override fun getLayout(): Int {
        return R.layout.audition_voice_control_panel_layout
    }

    override fun init(context: Context?) {
        super.init(context)
        mMixTv = this.findViewById(com.component.busilib.R.id.mix_tv)
        mMixSb = this.findViewById(com.component.busilib.R.id.mix_sb)
        mLowLatencyTv = this.findViewById(com.component.busilib.R.id.low_latency_tv)
        mLowLatencySb = this.findViewById(com.component.busilib.R.id.low_latency_sb)
        if (LATENCY_TEST_MODE) {
            mLowLatencySb?.setOnCheckedChangeListener { buttonView, isChecked ->
                ZqEngineKit.getInstance().setEnableAudioLowLatency(isChecked)
            }
            mEarSb?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    mMixSb?.setCheckedNoEvent(false)
                }
                ZqEngineKit.getInstance().setEnableAudioPreviewLatencyTest(isChecked)
            }
            mMixSb?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    mEarSb?.setCheckedNoEvent(false)
                }
                ZqEngineKit.getInstance().setEnableAudioMixLatencyTest(isChecked)
            }
        }
    }

    override fun bindData() {
        super.bindData()
        if (ZqEngineKit.getInstance().params != null) {
            if (LATENCY_TEST_MODE) {
                mEarSb?.setCheckedNoEvent(ZqEngineKit.getInstance().params.isEnableAudioPreviewLatencyTest)
                mMixSb?.setCheckedNoEvent(ZqEngineKit.getInstance().params.isEnableAudioMixLatencyTest)
                mLowLatencySb?.setCheckedNoEvent(ZqEngineKit.getInstance().params.isEnableAudioLowLatency)
            } else {
                mMixTv?.visibility = View.INVISIBLE
                mMixSb?.visibility = View.INVISIBLE
                mLowLatencyTv?.visibility = View.INVISIBLE
                mLowLatencySb?.visibility = View.INVISIBLE
            }
        }
    }
}
