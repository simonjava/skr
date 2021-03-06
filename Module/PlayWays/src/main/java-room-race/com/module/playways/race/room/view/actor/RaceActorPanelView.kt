package com.module.playways.race.room.view.actor

import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.common.base.BaseFragment
import com.common.utils.U
import com.common.utils.dp
import com.common.view.viewpager.NestViewPager
import com.common.view.viewpager.SlidingTabLayout
import com.module.playways.R
import com.module.playways.race.room.RaceRoomData
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder

/**
 * 展示竞演者的view
 */
class RaceActorPanelView(fragment: BaseFragment, val mRoomData: RaceRoomData) : ConstraintLayout(fragment.context) {

    private val raceTitleStl: SlidingTabLayout
    private val raceVp: NestViewPager

    private val pagerAdapter: PagerAdapter

    var actorView: RaceActorView? = null   // 竞演者
    var audienceView: RaceActorView? = null  // 观众
    private var mDialogPlus: DialogPlus? = null

    init {
        View.inflate(context, R.layout.race_actor_panel_view_stub_layout, this)

        raceVp = findViewById(R.id.race_vp)
        raceTitleStl = findViewById(R.id.race_title_stl)

        raceTitleStl.setCustomTabView(R.layout.race_actor_tab_view, R.id.tab_tv)
        raceTitleStl.setSelectedIndicatorColors(U.getColor(R.color.black_trans_20))
        raceTitleStl.setDistributeMode(SlidingTabLayout.DISTRIBUTE_MODE_NONE)
        raceTitleStl.setIndicatorAnimationMode(SlidingTabLayout.ANI_MODE_NONE)
        raceTitleStl.setIndicatorWidth(64.dp())
        raceTitleStl.setIndicatorBottomMargin(10.dp())
        raceTitleStl.setSelectedIndicatorThickness(24.dp().toFloat())
        raceTitleStl.setIndicatorCornorRadius(12.dp().toFloat())

        pagerAdapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return 2
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                if (position == 0) {
                    // 竞演者
                    if (actorView == null) {
                        actorView = RaceActorView(raceVp.context, RaceActorView.TYPE_ACTOR, mRoomData)
                    }
                    if (container.indexOfChild(actorView) == -1) {
                        container.addView(actorView)
                    }
                    actorView?.initData()
                    return actorView!!
                } else if (position == 1) {
                    // 观众
                    if (audienceView == null) {
                        audienceView = RaceActorView(raceVp.context, RaceActorView.TYPE_AUDIENCE, mRoomData)
                    }
                    if (container.indexOfChild(audienceView) == -1) {
                        container.addView(audienceView)
                    }
                    return audienceView!!
                }
                return super.instantiateItem(container, position)
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            override fun getPageTitle(position: Int): CharSequence? {
                if (position == 0) {
                    return "竞演者"
                } else if (position == 1) {
                    return "观众"
                }
                return ""
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }

            override fun getItemPosition(`object`: Any): Int {
                return PagerAdapter.POSITION_NONE
            }
        }

        raceVp.adapter = pagerAdapter
        raceTitleStl.setViewPager(raceVp)
        pagerAdapter.notifyDataSetChanged()

        raceVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    // 竞演者
                    actorView?.initData()
                } else if (position == 1) {
                    // 观众
                    audienceView?.initData()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    /**
     * 以后tips dialog 不要在外部单独写 dialog 了。
     * 可以不
     */
    fun showByDialog() {
        showByDialog(true)
    }

    fun showByDialog(canCancel: Boolean) {
        mDialogPlus?.dismiss(false)
        mDialogPlus = DialogPlus.newDialog(context)
                .setContentHolder(ViewHolder(this))
                .setGravity(Gravity.BOTTOM)
                .setContentBackgroundResource(com.common.base.R.color.transparent)
                .setOverlayBackgroundResource(com.common.base.R.color.transparent)
                .setExpanded(false)
                .setCancelable(canCancel)
                .create()
        mDialogPlus?.show()
    }

    fun dismiss() {
        mDialogPlus?.dismiss()
    }

    fun dismiss(isAnimation: Boolean) {
        mDialogPlus?.dismiss(isAnimation)
    }

    fun destory() {
        audienceView?.destory()
        actorView?.destory()
    }
}