package com.module.feeds.detail.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.common.base.BaseActivity
import com.common.utils.FragmentUtils
import com.common.utils.U
import com.module.RouterConstants
import com.module.feeds.R
import com.module.feeds.detail.fragment.FeedsDetailFragment
import com.module.feeds.detail.manager.AbsPlayModeManager
import com.module.feeds.detail.manager.FeedSongPlayModeManager
import com.module.feeds.statistics.FeedPage

@Route(path = RouterConstants.ACTIVITY_FEEDS_DETAIL)
class FeedsDetailActivity : BaseActivity() {

    companion object {
        // 标记来源
        val TYPE_SWITCH = 1     //只支持上下首切换
        val TYPE_SWITCH_MODE = 2  //即支持换模式又支持上下首切换
        val TYPE_NO = 3  //上面两个都不支持
        var MANAGER: AbsPlayModeManager? = null

        fun openActivity(from: FeedPage, activity: Activity, feedID: Int, type: Int, playType: FeedSongPlayModeManager.PlayMode?, playModeManager: AbsPlayModeManager?) {
            MANAGER = playModeManager
            val intent = Intent(activity, FeedsDetailActivity::class.java)
            intent.putExtra("feed_ID", feedID)

            type?.let {
                intent.putExtra("type", type)
            }

            from?.let {
                intent.putExtra("from", it)
            }

            playType?.let {
                intent.putExtra("playType", playType)
            }
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 避免不停地进主页又从神曲进主页
        var num = 0
        for (i in U.getActivityUtils().activityList.size - 1 downTo 0) {
            val ac = U.getActivityUtils().activityList[i]
            if (ac is FeedsDetailActivity) {
                num++
                if (num >= 2) {
                    ac.finish()
                }
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.feeds_detail_activity_layout
    }

    override fun initData(savedInstanceState: Bundle?) {
        val feedID = intent.getIntExtra("feed_ID", -1)
        val type = intent.getIntExtra("type", TYPE_SWITCH)
        var from: FeedPage? = null
        intent.getSerializableExtra("from")?.let {
            from = it as FeedPage
        }

        val playType = intent.getSerializableExtra("playType") as FeedSongPlayModeManager.PlayMode?
        U.getFragmentUtils().addFragment(FragmentUtils.newAddParamsBuilder(this@FeedsDetailActivity, FeedsDetailFragment::class.java)
                .setAddToBackStack(false)
                .setHasAnimation(false)
                .addDataBeforeAdd(0, feedID)
                .addDataBeforeAdd(1, type)
                .addDataBeforeAdd(2, playType)
                .addDataBeforeAdd(3, MANAGER)
                .addDataBeforeAdd(4, from)
                .build())
        MANAGER = null
    }

    override fun resizeLayoutSelfWhenKeyboardShow(): Boolean {
        return true
    }

    override fun useEventBus(): Boolean {
        return false
    }
}
