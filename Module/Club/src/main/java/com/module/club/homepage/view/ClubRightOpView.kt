package com.module.club.homepage.view

import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewStub
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.common.core.userinfo.model.ClubMemberInfo
import com.common.core.view.setDebounceViewClickListener
import com.common.log.MyLog
import com.common.notification.event.RongClubMsgEvent
import com.common.rxretrofit.ApiManager
import com.common.rxretrofit.ControlType
import com.common.rxretrofit.RequestControl
import com.common.rxretrofit.subscribe
import com.common.utils.U
import com.common.view.ExViewStub
import com.common.view.ex.ExImageView
import com.common.view.ex.ExTextView
import com.module.ModuleServiceManager
import com.module.RouterConstants
import com.module.club.ClubServerApi
import com.module.club.R
import com.module.common.ICallback
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 家族首页底部操作按钮
 */
class ClubRightOpView(viewStub: ViewStub) : ExViewStub(viewStub), ICallback {

    private val SP_KEY_APPLY_WATER_LEVEL = "sp_key_apply_water_level"               // 申请水位
    private val SP_KEY_APPLY_WATER_LEVEL_CLUBID = "sp_key_apply_water_level_clubid" // 申请水位对应的clubID
    private var applyTimeMs: Long = 0
    private var applyRedCount: Int = 0

    private val TAG = "ClubRightOpView"
    private var mApplyTv: ExTextView? = null
    private var mApplyCountTv: ExTextView? = null

    private var mConversationTv: ExTextView? = null
    private var mPostView: LinearLayout? = null
    private var mPostImg: ExImageView? = null
    private var mPostTv: ExTextView? = null
    private var mConversationUnreadNumTv: ExTextView? = null
    private var mClubMemberInfo: ClubMemberInfo? = null
    private var mClubPostPanel: LinearLayout? = null
    private var mClubOtherActionsPanel: ConstraintLayout? = null
    private var mClubPostActionTv: ExTextView? = null
    private var mClubPostPhotoTv: ExTextView? = null
    private var mClubPostOpusTv: ExTextView? = null
    private var applyRedIv: View? = null

    private var mPostPanelShowAni: Animation? = null
    private var mPostPanelHideAni: Animation? = null

    override fun init(parentView: View) {
        mApplyTv = parentView.findViewById(R.id.club_right_apply_tv)
        mApplyCountTv = parentView.findViewById(R.id.club_right_apply_num_tv)
        mConversationTv = parentView.findViewById(R.id.club_right_conversation_tv)
        mPostView = parentView.findViewById(R.id.club_right_post)
        mPostImg = parentView.findViewById(R.id.club_right_post_img)
        mPostTv = parentView.findViewById(R.id.club_right_post_tv)
        mConversationUnreadNumTv = parentView.findViewById(R.id.club_conversation_unread_num_tv)
        mClubPostPanel = parentView.findViewById(R.id.club_right_post_panel)
        mClubOtherActionsPanel = parentView.findViewById(R.id.club_right_other_actions_panel)
        mClubPostActionTv = parentView.findViewById(R.id.club_right_post_action)
        mClubPostPhotoTv = parentView.findViewById(R.id.club_right_post_photo)
        mClubPostOpusTv = parentView.findViewById(R.id.club_right_post_opus_tv)
        applyRedIv = parentView.findViewById(R.id.apply_red_iv)
        initAnimations()
    }

    private fun initAnimations() {
        mPostPanelShowAni = RotateAnimation(0f, 45f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        mPostPanelShowAni?.fillAfter = true
        mPostPanelShowAni?.duration = 300
        mPostPanelShowAni?.interpolator = AccelerateInterpolator()

        mPostPanelHideAni = RotateAnimation(45f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        mPostPanelHideAni?.fillAfter = true
        mPostPanelHideAni?.duration = 300
        mPostPanelHideAni?.interpolator = AccelerateInterpolator()
    }

    fun bindData(clubMemberInfo: ClubMemberInfo?) {
        mClubMemberInfo = clubMemberInfo

        mApplyTv?.setDebounceViewClickListener {
            U.getPreferenceUtils().setSettingLong(SP_KEY_APPLY_WATER_LEVEL, applyTimeMs)
            applyRedIv?.visibility = View.GONE
            ARouter.getInstance().build(RouterConstants.ACTIVITY_LIST_APPLY_CLUB)
                    .withSerializable("clubMemberInfo", clubMemberInfo)
                    .navigation()
        }

        mClubPostActionTv?.setDebounceViewClickListener {
            ARouter.getInstance()
                    .build(RouterConstants.ACTIVITY_POSTS_PUBLISH)
                    .withInt("from", 2)
                    .withInt("familyID", mClubMemberInfo?.club?.clubID
                            ?: return@setDebounceViewClickListener)
                    .navigation()
        }

        mClubPostOpusTv?.setDebounceViewClickListener {
            ARouter.getInstance().build(RouterConstants.ACTIVITY_FEEDS_SONG_MANAGE)
                    .withInt("from", 10)
                    .withInt("familyID", clubMemberInfo?.club?.clubID
                            ?: return@setDebounceViewClickListener)
                    .navigation()
        }
        mConversationTv?.setDebounceViewClickListener { view ->
            ModuleServiceManager.getInstance().msgService.startClubChat(view?.context, clubMemberInfo?.club?.clubID.toString(), clubMemberInfo?.club?.name)
        }
        mPostView?.setDebounceViewClickListener { view ->
            togglePostPanel()
        }

        updateUnreadMsgCount()
        updateApplyCount()
        if (mClubMemberInfo?.isFounder() == true || mClubMemberInfo?.isCoFounder() == true) {
            mApplyTv?.visibility = View.VISIBLE
            checkApplyRed()
        } else {
            mApplyTv?.visibility = View.GONE
            applyRedIv?.visibility = View.GONE
        }

    }

    /**
     * 打开或关闭发布面板
     */
    private fun togglePostPanel() {

        mPostPanelShowAni?.reset()
        mPostPanelHideAni?.reset()

        if (mClubPostPanel?.visibility == View.GONE) {
            mClubPostPanel?.visibility = View.VISIBLE
            mClubOtherActionsPanel?.visibility = View.GONE
            mPostTv?.visibility = View.INVISIBLE

            mPostImg?.startAnimation(mPostPanelShowAni)

        } else {
            mClubPostPanel?.visibility = View.GONE
            mClubOtherActionsPanel?.visibility = View.VISIBLE
            mPostTv?.visibility = View.VISIBLE

            mPostImg?.startAnimation(mPostPanelHideAni)
        }
    }

    fun setPhotoClickListener(click: (view: View?) -> Unit) {
        mClubPostPhotoTv?.setDebounceViewClickListener(click)
    }

    override fun layoutDesc(): Int {
        return R.layout.club_home_page_right_op_view_layout
    }

    fun show() {
        setVisibility(View.VISIBLE)
    }

    fun hide() {
        setVisibility(View.GONE)
    }

    fun updateUnreadMsgCount() {
        //获取未读消息数
        mClubMemberInfo?.club?.let {
            ModuleServiceManager.getInstance().msgService.getClubUnReadCount(this, it.clubID.toString())
        }
    }

    private fun updateApplyCount() {
        //获取未读消息数
        mClubMemberInfo?.club?.let {
            ModuleServiceManager.getInstance().clubService.getClubApplyCount(it.clubID, object : ICallback {
                override fun onSucess(obj: Any?) {
                    obj?.toString()?.toInt()?.takeIf { it > 0 }?.let {
                        mApplyCountTv?.text = it.toString()
                        mApplyCountTv?.visibility = View.VISIBLE
                    } ?: mApplyCountTv?.apply {
                        mApplyCountTv?.visibility = View.GONE
                    }
                }

                override fun onFailed(obj: Any?, errcode: Int, message: String?) {

                }

            })
        }
    }

    /**
     * 获取并显示未读消息数量
     */
    override fun onSucess(obj: Any?) {
        obj?.let { it as? Int }?.let {
            if (it > 0) {
                mConversationUnreadNumTv?.text = it.toString()
                mConversationUnreadNumTv?.visibility = View.VISIBLE
            } else {
                mConversationUnreadNumTv?.text = ""
                mConversationUnreadNumTv?.visibility = View.GONE
            }
        }
    }

    override fun onFailed(obj: Any?, errcode: Int, message: String?) {
        MyLog.e(TAG, "获取未读消息数失败 errode: " + obj.toString())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RongClubMsgEvent) {
        updateUnreadMsgCount()
    }

    override fun onViewAttachedToWindow(v: View) {
        super.onViewAttachedToWindow(v)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onViewDetachedFromWindow(v: View) {
        super.onViewDetachedFromWindow(v)
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    fun resume() {
        updateApplyCount()
        updateUnreadMsgCount()
    }

    private fun checkApplyRed() {
        if (U.getPreferenceUtils().getSettingInt(SP_KEY_APPLY_WATER_LEVEL_CLUBID, 0) != mClubMemberInfo?.club?.clubID) {
            // clubID变了，重置一下数据
            mClubMemberInfo?.club?.clubID?.let {
                U.getPreferenceUtils().setSettingInt(SP_KEY_APPLY_WATER_LEVEL_CLUBID, it)
            }
            U.getPreferenceUtils().setSettingLong(SP_KEY_APPLY_WATER_LEVEL, 0)
        }

        launch {
            val lastTimeMs = U.getPreferenceUtils().getSettingLong(SP_KEY_APPLY_WATER_LEVEL, 0)
            val result = subscribe(RequestControl("getCountMemberApply", ControlType.CancelThis)) {
                ApiManager.getInstance().createService(ClubServerApi::class.java).getCountMemberApply(mClubMemberInfo?.club?.clubID!!, lastTimeMs)
            }
            if (result.errno == 0) {
                applyTimeMs = result.data.getLongValue("timeMs")
                applyRedCount = result.data.getIntValue("total")

                // 是否显示红点
                if (applyRedCount > 0) {
                    applyRedIv?.visibility = View.VISIBLE
                } else {
                    applyRedIv?.visibility = View.GONE
                }
            }
        }
    }
}