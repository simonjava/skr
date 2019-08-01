package com.module.feeds.rank.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.fastjson.JSON
import com.common.base.BaseActivity
import com.common.core.avatar.AvatarUtils
import com.common.core.myinfo.MyUserInfoManager
import com.common.core.userinfo.UserInfoManager
import com.common.player.PlayerCallbackAdapter
import com.common.player.SinglePlayer
import com.common.rxretrofit.ApiManager
import com.common.view.DebounceViewClickListener
import com.common.view.titlebar.CommonTitleBar
import com.facebook.drawee.view.SimpleDraweeView
import com.module.RouterConstants
import com.module.feeds.R
import com.module.feeds.make.openFeedsMakeActivity
import com.module.feeds.rank.FeedsRankServerApi
import com.module.feeds.rank.adapter.FeedDetailAdapter
import com.module.feeds.watch.model.FeedsWatchModel
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.coroutines.launch

/**
 * 具体神曲的榜单
 */
@Route(path = RouterConstants.ACTIVITY_FEEDS_RANK_DETAIL)
class FeedsDetailRankActivity : BaseActivity() {

    lateinit var mTitlebar: CommonTitleBar
    lateinit var mRefreshLayout: SmartRefreshLayout
    lateinit var mRecyclerView: RecyclerView
    lateinit var mRecordCover: SimpleDraweeView
    lateinit var mNameTv: TextView
    lateinit var mLikeNumTv: TextView
    lateinit var mHitIv: ImageView

    private val mAdapter: FeedDetailAdapter = FeedDetailAdapter()

    var title = ""
    var challengeID = 0L

    var offset = 0
    val mCNT = 30
    private val mFeedRankServerApi: FeedsRankServerApi = ApiManager.getInstance().createService(FeedsRankServerApi::class.java)

    private val playerTag = TAG + hashCode()

    private val playCallback = object : PlayerCallbackAdapter() {
        override fun onCompletion() {
            super.onCompletion()
            // 重复播放一次
            mAdapter.mCurrentPlayModel?.let {
                play(it)
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.feeds_rank_detail_activity_layout
    }

    override fun initData(savedInstanceState: Bundle?) {
        title = intent.getStringExtra("rankTitle")
        challengeID = intent.getLongExtra("challengeID", 0L)

        mTitlebar = findViewById(R.id.titlebar)

        mRecordCover = findViewById(R.id.record_cover)
        mNameTv = findViewById(R.id.name_tv)
        mLikeNumTv = findViewById(R.id.like_num_tv)
        mRefreshLayout = findViewById(R.id.refreshLayout)
        mRecyclerView = findViewById(R.id.recycler_view)
        mHitIv = findViewById(R.id.hit_iv);


        mTitlebar.centerTextView.text = title
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

        mRefreshLayout.setEnableRefresh(true)
        mRefreshLayout.setEnableLoadMore(true)
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false)
        mRefreshLayout.setEnableOverScrollDrag(false)

        mRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                loadMoreData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                initLoadData()
            }
        })

        mTitlebar.leftTextView.setOnClickListener(object : DebounceViewClickListener() {
            override fun clickValid(v: View?) {
                finish()
            }
        })

        mHitIv.setOnClickListener(object : DebounceViewClickListener() {
            override fun clickValid(v: View?) {
                // 打榜去
                openFeedsMakeActivity(challengeID)
            }
        })

        mAdapter.onClickPlayListener = { model, _ ->
            model?.let {
                if (mAdapter.mCurrentPlayModel == it) {
                    // 暂停播放
                    stop(it)
                } else {
                    // 开始播放
                    play(it)
                }
            }
        }
        initLoadData()
        SinglePlayer.addCallback(playerTag, playCallback)
    }

    private fun play(model: FeedsWatchModel) {
        mAdapter.mCurrentPlayModel = model
        mAdapter.notifyDataSetChanged()
        model.song?.playURL?.let {
            SinglePlayer.startPlay(playerTag, it)
        }
    }

    private fun stop(model: FeedsWatchModel) {
        mAdapter.mCurrentPlayModel = null
        mAdapter.notifyDataSetChanged()
        SinglePlayer.stop(playerTag)
    }


    private fun initLoadData() {
        loadData(0)
    }

    private fun loadMoreData() {
        loadData(offset)
    }

    private fun loadData(off: Int) {
        launch {
            val result = mFeedRankServerApi.getFeedRankDetailList(off, mCNT, MyUserInfoManager.getInstance().uid.toInt(), challengeID)
            if (result.errno == 0) {
                val list = JSON.parseArray(result.data.getString("rankInfos"), FeedsWatchModel::class.java)
                offset = result.data.getIntValue("offset")
                showDetailInfo(list, off == 0)
            }
        }
    }

    private fun showDetailInfo(list: List<FeedsWatchModel>?, isClean: Boolean) {
        mRefreshLayout.finishLoadMore()
        mRefreshLayout.finishRefresh()
        if (isClean) {
            mAdapter.mDataList.clear()
            mRefreshLayout.setEnableLoadMore(true)
        }

        if (list.isNullOrEmpty()) {
            // 后面都没有数据了
            mRefreshLayout.setEnableLoadMore(false)
        }

        list?.let { mAdapter.mDataList.addAll(it) }
        mAdapter.notifyDataSetChanged()
        if (isClean && mAdapter.mDataList.isNotEmpty()) {
            bindTopData(mAdapter.mDataList[0])
        }
    }

    private fun bindTopData(feedsWatchModel: FeedsWatchModel) {
        AvatarUtils.loadAvatarByUrl(mRecordCover, AvatarUtils.newParamsBuilder(feedsWatchModel.user?.avatar).setCircle(true).build())
        mNameTv.text = UserInfoManager.getInstance().getRemarkName(feedsWatchModel.user?.userID
                ?: 0, feedsWatchModel.user?.nickname)
        mLikeNumTv.text = feedsWatchModel.starCnt.toString()

    }

    override fun useEventBus(): Boolean {
        return false
    }

    override fun destroy() {
        super.destroy()
        SinglePlayer.removeCallback(playerTag)
    }
}