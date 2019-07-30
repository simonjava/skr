package com.module.feeds.watch.view

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AbsListView
import com.alibaba.android.arouter.launcher.ARouter
import com.common.base.BaseFragment
import com.common.core.userinfo.UserInfoManager
import com.common.player.SinglePlayer
import com.common.player.VideoPlayerAdapter
import com.common.player.event.PlayerEvent
import com.common.view.DebounceViewClickListener
import com.component.dialog.FeedsMoreDialogView
import com.module.feeds.watch.presenter.FeedWatchViewPresenter
import com.module.feeds.watch.adapter.FeedsWatchViewAdapter
import com.module.feeds.watch.listener.FeedsListener
import com.module.feeds.watch.model.FeedsWatchModel
import com.module.RouterConstants
import com.module.feeds.R
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class FeedsWatchView(fragment: BaseFragment, type: Int) : ConstraintLayout(fragment.context), IFeedsWatchView {
    val TAG = "FeedsWatchView"

    companion object {
        const val TYPE_RECOMMEND = 1
        const val TYPE_FOLLOW = 2
    }

    private val mRefreshLayout: SmartRefreshLayout
    private val mClassicsHeader: ClassicsHeader
    private val mLayoutManager: LinearLayoutManager
    private val mRecyclerView: RecyclerView

    private val mAdapter: FeedsWatchViewAdapter
    private val mPersenter: FeedWatchViewPresenter

    var mFeedsMoreDialogView: FeedsMoreDialogView? = null
    var mCurFocusPostion = -1 // 记录当前操作的焦点pos
    val playerTag = TAG + hashCode()
    val playCallback = object:VideoPlayerAdapter.PlayerCallbackAdapter(){

    }

    init {
        View.inflate(context, R.layout.feed_watch_view_layout, this)

        mRefreshLayout = findViewById(R.id.refreshLayout)
        mClassicsHeader = findViewById(R.id.classics_header)
        mRecyclerView = findViewById(R.id.recycler_view)

        mPersenter = FeedWatchViewPresenter(this, type)
        mAdapter = FeedsWatchViewAdapter(object : FeedsListener {
            override fun onclickRankListener(watchModel: FeedsWatchModel?) {
                // 排行榜详情
                watchModel?.let {
                    ARouter.getInstance().build(RouterConstants.ACTIVITY_FEEDS_RANK_DETAIL)
                            .withString("rankTitle", it.rank?.rankTitle)
                            .withLong("challengeID", it.song?.challengeID ?: 0L)
                            .navigation()
                }
            }

            override fun onClickLikeListener(position: Int, watchModel: FeedsWatchModel?) {
                // 喜欢
                watchModel?.let { mPersenter.feedLike(position, it) }
            }

            override fun onClickCommentListener(watchModel: FeedsWatchModel?) {
                // 评论
                ARouter.getInstance().build(RouterConstants.ACTIVITY_FEEDS_DETAIL)
                        .withSerializable("feed_model", watchModel)
                        .navigation()
            }

            override fun onClickHitListener(watchModel: FeedsWatchModel?) {
                // 打榜
                watchModel?.song?.let {
                    ARouter.getInstance().build(RouterConstants.ACTIVITY_FEEDS_MAKE)
                            .withSerializable("song_model", it)
                            .navigation()
                }
            }

            override fun onClickDetailListener(watchModel: FeedsWatchModel?) {
                // 详情  声音要连贯
                ARouter.getInstance().build(RouterConstants.ACTIVITY_FEEDS_DETAIL)
                        .withSerializable("feed_model", watchModel)
                        .navigation()
            }

            override fun onClickCDListener(position: Int, watchModel: FeedsWatchModel?) {
                // 播放
                watchModel?.let { model -> controlPlay(position, model, false) }
            }

            override fun onClickMoreListener(watchModel: FeedsWatchModel?) {
                // 更多
                watchModel?.let {
                    if (fragment.activity != null) {
                        mFeedsMoreDialogView = FeedsMoreDialogView(fragment.activity!!, FeedsMoreDialogView.FROM_FEED,
                                it.user?.userID ?: 0,
                                it.song?.songID ?: 0,
                                0)
                                .apply {
                                    setFollow(it.hasFollow == true)
                                    mFuncationTv.setOnClickListener(object : DebounceViewClickListener() {
                                        override fun clickValid(v: View?) {
                                            dismiss()
                                            UserInfoManager.getInstance().mateRelation(it.user?.userID
                                                    ?: 0, UserInfoManager.RA_BUILD, false)
                                        }
                                    })
                                }
                        mFeedsMoreDialogView?.showByDialog()
                    }

                }
            }

        })

        mRefreshLayout.apply {
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setEnableLoadMoreWhenContentNotFull(false)
            setEnableOverScrollDrag(true)
            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    mPersenter.initWatchList(true)
                }

                override fun onRefresh(refreshLayout: RefreshLayout) {
                    mPersenter.loadMoreWatchList()
                }
            })
        }

        mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        (mRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var maxPercent = 0f
            var model: FeedsWatchModel? = null
            var isFound = false

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
                        var firstCompleteItem = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                        var postion = 0
                        if (firstCompleteItem != RecyclerView.NO_POSITION) {
                            // 找到了
                            model = mAdapter.mDataList[firstCompleteItem]
                            postion = firstCompleteItem
                        } else {
                            // 找不到位置，取其中百分比最大的
                            var firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()
                            var lastVisibleItem = mLayoutManager.findLastVisibleItemPosition()
                            if (firstVisibleItem != RecyclerView.NO_POSITION && lastVisibleItem != RecyclerView.NO_POSITION) {
                                val percents = FloatArray(lastVisibleItem - firstVisibleItem + 1)
                                var i = firstVisibleItem
                                isFound = false
                                maxPercent = 0f
                                model = null
                                while (i <= lastVisibleItem && !isFound) {
                                    val itemView = mRecyclerView.findViewHolderForAdapterPosition(i).itemView
                                    val location1 = IntArray(2)
                                    val location2 = IntArray(2)
                                    itemView.getLocationOnScreen(location1)
                                    mRecyclerView.getLocationOnScreen(location2)
                                    val top = location1[1] - location2[1]
                                    when {
                                        top < 0 -> percents[i - firstVisibleItem] = (itemView.height + top).toFloat() * 100 / itemView.height
                                        (top + itemView.height) < mRecyclerView.height -> percents[i - firstVisibleItem] = 100f
                                        else -> percents[i - firstVisibleItem] = (mRecyclerView.height - top).toFloat() * 100 / itemView.height
                                    }
                                    if (percents[i - firstVisibleItem] == 100f) {
                                        isFound = true
                                        maxPercent = 100f
                                        model = mAdapter.mDataList[i]
                                        postion = i
                                    } else {
                                        if (percents[i - firstVisibleItem] > maxPercent) {
                                            maxPercent = percents[i - firstVisibleItem]
                                            model = mAdapter.mDataList[i]
                                            postion = i
                                        }
                                    }
                                    i++
                                }
                            }
                        }

                        model?.let {
                            isFound = true
                            controlPlay(postion, it, true)
                        }
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {

                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {

                    }
                }
            }
        })
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        SinglePlayer.addCallback(playerTag, playCallback)
    }

    private fun controlPlay(pos: Int, model: FeedsWatchModel, isMustPlay: Boolean) {
        if (mCurFocusPostion != pos) {
            SinglePlayer.reset(playerTag)
            mCurFocusPostion = pos
        }
        if (isMustPlay) {
            // 播放
            startPlay(pos, model)
        } else {
            if (mAdapter.mCurrentPlayModel?.feedID == model.feedID && mAdapter.playing) {
                // 停止播放
                pausePlay()
            } else {
                // 播放
                startPlay(pos, model)
            }
        }
    }

    private fun startPlay(pos: Int, model: FeedsWatchModel) {
        mAdapter.startPlayModel(pos, model)
        model.song?.playURL?.let {
            SinglePlayer.startPlay(playerTag,it)
        }
    }

    /**
     * 继续播放
     */
    private fun resumePlay(){
        mAdapter.resumePlayModel()
        mAdapter.mCurrentPlayModel?.song?.playURL?.let {
            SinglePlayer.startPlay(playerTag,it)
        }
    }

    private fun pausePlay() {
        mAdapter.pausePlayModel()
        SinglePlayer.pause(playerTag)
    }

    override fun addWatchList(list: List<FeedsWatchModel>?, isClear: Boolean) {
        mRefreshLayout.finishRefresh()
        mRefreshLayout.finishLoadMore()
        if (isClear) {
            mAdapter.mDataList.clear()
            if (list != null && list.isNotEmpty()) {
                mAdapter.mDataList.addAll(list)
            }
            if (mAdapter.mDataList.isNotEmpty()) {
                controlPlay(0, mAdapter.mDataList[0], true)
            }
            mAdapter.notifyDataSetChanged()
        } else {
            if (list != null && list.isNotEmpty()) {
                mAdapter.mDataList.addAll(list)
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun requestError() {
        mRefreshLayout.finishRefresh()
        mRefreshLayout.finishLoadMore()
    }

    override fun feedLikeResult(position: Int, model: FeedsWatchModel, isLike: Boolean) {
        model.isLiked = isLike
        if (isLike) {
            model.starCnt = model.starCnt?.plus(1)
        } else {
            model.starCnt = model.starCnt?.minus(1)
        }
        mAdapter.update(position, model, FeedsWatchViewAdapter.REFRESH_TYPE_LIKE)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        SinglePlayer.addCallback(playerTag, playCallback)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        destory()
    }

    @Subscribe
    fun onEvent(event: PlayerEvent.TimeFly) {
        mAdapter.updatePlayProgress(event.curPostion, event.totalDuration)
    }

    fun destory() {
        mPersenter.destroy()
        SinglePlayer.removeCallback(playerTag)
        EventBus.getDefault().unregister(this)
    }

    fun unselected() {
        pausePlay()
    }

    fun selected() {
        // 该页面选中以及从详情页返回都会回调这个方法
        if(!mPersenter.initWatchList(false)){
            // 如果因为时间短没请求，继续往前播放
            resumePlay()
        }
    }
}