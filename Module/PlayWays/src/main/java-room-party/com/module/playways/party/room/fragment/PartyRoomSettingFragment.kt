package com.module.playways.party.room.fragment

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.common.base.BaseFragment
import com.common.core.view.setDebounceViewClickListener
import com.common.utils.FragmentUtils
import com.common.utils.U
import com.common.view.ex.ExTextView
import com.common.view.titlebar.CommonTitleBar
import com.module.playways.R

class PartyRoomSettingFragment : BaseFragment() {
    lateinit var titlebar: CommonTitleBar
    lateinit var roomInfoSettingTv: ExTextView
    lateinit var roomGonggaoTv: ExTextView
    lateinit var roomAdministratorTv: ExTextView

    override fun initView(): Int {
        return R.layout.party_room_setting_fragment_layout
    }

    override fun initData(savedInstanceState: Bundle?) {
        titlebar = rootView.findViewById(R.id.titlebar)
        roomInfoSettingTv = rootView.findViewById(R.id.room_info_setting_tv)
        roomGonggaoTv = rootView.findViewById(R.id.room_gonggao_tv)
        roomAdministratorTv = rootView.findViewById(R.id.room_administrator_tv)

        titlebar.leftTextView.setDebounceViewClickListener { finish() }
        roomInfoSettingTv.setDebounceViewClickListener {

        }

        roomGonggaoTv.setDebounceViewClickListener {
            U.getFragmentUtils().addFragment(
                    FragmentUtils.newAddParamsBuilder(context as FragmentActivity, GonggaoSettingFragment::class.java)
                            .setAddToBackStack(true)
                            .setHasAnimation(true)
                            .build())
        }

        roomAdministratorTv.setDebounceViewClickListener {
            U.getFragmentUtils().addFragment(
                    FragmentUtils.newAddParamsBuilder(context as FragmentActivity, AdministratorSelectFragment::class.java)
                            .setAddToBackStack(true)
                            .setHasAnimation(true)
                            .build())
        }
    }

    override fun destroy() {
        super.destroy()
        U.getSoundUtils().release(TAG)
    }

    override fun useEventBus(): Boolean {
        return false
    }
}