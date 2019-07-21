package com.module.feeds.watch

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.common.base.BaseFragment
import com.common.log.MyLog
import com.common.utils.U
import com.common.view.DebounceViewClickListener
import com.module.feeds.R
import com.module.feeds.watch.view.FeedsCollectView
import com.module.feeds.watch.view.FeedsWatchView
import com.common.view.viewpager.NestViewPager
import com.common.view.viewpager.SlidingTabLayout
import com.common.view.ex.ExTextView

class FeedsWatchFragment : BaseFragment() {

    private lateinit var mNavigationBgIv: ImageView
    private lateinit var mFeedChallengeTv: ExTextView
    private lateinit var mFeedTab: SlidingTabLayout
    private lateinit var mFeedVp: NestViewPager
    private lateinit var mTabPagerAdapter: PagerAdapter

    val mRecommendFeedsView: FeedsWatchView by lazy { FeedsWatchView(this) }   //推荐
    val mFollowFeesView: FeedsWatchView by lazy { FeedsWatchView(this) }       //关注
    val mFeedsCollectView: FeedsCollectView by lazy { FeedsCollectView(this) } //喜欢

    override fun initView(): Int {
        return R.layout.feeds_watch_fragment_layout
    }

    override fun initData(savedInstanceState: Bundle?) {
        mNavigationBgIv = mRootView.findViewById(R.id.navigation_bg_iv)
        mFeedTab = mRootView.findViewById(R.id.feed_tab)
        mFeedChallengeTv = mRootView.findViewById(R.id.feed_challenge_tv)
        mFeedVp = mRootView.findViewById(R.id.feed_vp)

        mFeedChallengeTv.setOnClickListener(object : DebounceViewClickListener() {
            override fun clickValid(v: View?) {
                // 发起挑战
            }
        })

        mFeedTab.setCustomTabView(R.layout.feed_tab_view_layout, R.id.tab_tv)
        mFeedTab.setSelectedIndicatorColors(U.getColor(R.color.black_trans_80))
        mFeedTab.setDistributeMode(SlidingTabLayout.DISTRIBUTE_MODE_NONE)
        mFeedTab.setIndicatorAnimationMode(SlidingTabLayout.ANI_MODE_NORMAL)
        mFeedTab.setTitleSize(14f)
        mFeedTab.setSelectedTitleSize(24f)
        mFeedTab.setIndicatorWidth(U.getDisplayUtils().dip2px(16f))
        mFeedTab.setSelectedIndicatorThickness(U.getDisplayUtils().dip2px(4f).toFloat())
        mFeedTab.setIndicatorCornorRadius(U.getDisplayUtils().dip2px(2f).toFloat())

        mTabPagerAdapter = object : PagerAdapter() {

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                MyLog.d(TAG, "destroyItem container=$container position=$position object=$`object`")
                container.removeView(`object` as View)
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                MyLog.d(TAG, "instantiateItem container=$container position=$position")
                var view: View? = when (position) {
                    0 -> mRecommendFeedsView
                    1 -> mFollowFeesView
                    2 -> mFeedsCollectView
                    else -> null
                }
                if (container.indexOfChild(view) == -1) {
                    container.addView(view)
                }
                return view!!
            }

            override fun getItemPosition(`object`: Any): Int {
                return PagerAdapter.POSITION_NONE
            }

            override fun getCount(): Int {
                return 3
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when (position) {
                    0 -> "推荐"
                    1 -> "关注"
                    2 -> "喜欢"
                    else -> super.getPageTitle(position)
                }
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }
        }

        mFeedTab.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                mFeedTab.notifyDataChange()
                val drawable = mNavigationBgIv.getBackground() as ColorDrawable
                val color: Int = drawable.color
                when (position) {
                    0 -> {
                    }
                    1 -> {
                    }
                    2 -> {
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        mFeedVp.offscreenPageLimit = 2
        mFeedVp.adapter = mTabPagerAdapter
        mFeedTab.setViewPager(mFeedVp)
        mTabPagerAdapter.notifyDataSetChanged()
        mFeedVp.setCurrentItem(1, false)
    }

    override fun useEventBus(): Boolean {
        return false
    }

    override fun isInViewPager(): Boolean {
        return true
    }
}
