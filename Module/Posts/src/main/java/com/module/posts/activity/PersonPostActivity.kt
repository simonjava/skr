package com.module.posts.activity

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.common.base.BaseActivity
import com.common.core.myinfo.MyUserInfoManager
import com.common.core.userinfo.model.UserInfoModel
import com.common.core.view.setDebounceViewClickListener
import com.common.log.MyLog
import com.module.RouterConstants
import com.module.posts.R
import com.module.posts.watch.view.PersonPostsWatchView

// 个人中心的帖子
@Route(path = RouterConstants.ACTIVITY_PERSON_POST)
class PersonPostActivity : BaseActivity() {

    lateinit var backIv: ImageView
    lateinit var create: TextView
    lateinit var divider: View
    lateinit var content: RelativeLayout

    var userInfoModel: UserInfoModel? = null
    var watchView: PersonPostsWatchView? = null

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.person_post_activity_layout
    }

    override fun initData(savedInstanceState: Bundle?) {
        userInfoModel = intent.getSerializableExtra("userInfoModel") as UserInfoModel?
        if (userInfoModel == null) {
            MyLog.w(TAG, "PersonPostActivity userInfoModel = null")
            finish()
            return
        }

        backIv = findViewById(R.id.back_iv)
        create = findViewById(R.id.create)
        divider = findViewById(R.id.divider)
        content = findViewById(R.id.content)

        backIv.setDebounceViewClickListener {
            finish()
        }

        if (userInfoModel?.userId == MyUserInfoManager.uid.toInt()) {
            create.visibility = View.VISIBLE
        } else {
            create.visibility = View.GONE
        }
        create.setDebounceViewClickListener {
            ARouter.getInstance()
                    .build(RouterConstants.ACTIVITY_POSTS_PUBLISH)
                    .navigation()
        }

        if (watchView == null) {
            watchView = PersonPostsWatchView(this@PersonPostActivity, userInfoModel!!, null)
        }
        watchView?.layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        content.addView(watchView)

        watchView?.getPosts(false)
    }

    override fun destroy() {
        super.destroy()
        watchView?.destory()
    }

    override fun useEventBus(): Boolean {
        return false
    }

    override fun canSlide(): Boolean {
        return false
    }
}