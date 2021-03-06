//package com.module.feeds.watch.view
//
//import android.media.MediaPlayer
//import android.os.Bundle
//import android.support.constraint.ConstraintLayout
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.view.View
//import android.widget.AbsListView
//import com.alibaba.android.arouter.launcher.ARouter
//import com.common.base.BaseFragment
//import com.common.core.myinfo.MyUserInfoManager
//import com.common.core.share.SharePanel
//import com.common.core.share.ShareType
//import com.common.core.userinfo.event.RelationChangeEvent
//import com.common.core.userinfo.model.UserInfoModel
//import com.common.log.MyLog
//import com.common.player.PlayerCallbackAdapter
//import com.common.player.SinglePlayer
//import com.common.utils.U
//import com.common.utils.dp
//import com.common.videocache.MediaCacheManager
//import com.common.view.AnimateClickListener
//import com.common.view.DebounceViewClickListener
//import com.component.busilib.callback.EmptyCallback
//import com.component.busilib.event.FeedPublishSucessEvent
//import com.component.person.view.RequestCallBack
//import com.dialog.view.TipsDialogView
//import com.kingja.loadsir.callback.Callback
//import com.kingja.loadsir.core.LoadService
//import com.kingja.loadsir.core.LoadSir
//import com.module.RouterConstants
//import com.module.feeds.IPersonFeedsWall
//import com.module.feeds.R
//import com.module.feeds.detail.activity.FeedsDetailActivity
//import com.module.feeds.detail.manager.AbsPlayModeManager
//import com.module.feeds.detail.manager.FeedSongPlayModeManager
//import com.module.feeds.event.FeedDetailChangeEvent
//import com.module.feeds.event.FeedsCollectChangeEvent
//import com.module.feeds.make.make.openFeedsMakeActivityFromChallenge
//import com.module.feeds.statistics.FeedsPlayStatistics
//import com.module.feeds.watch.adapter.FeedsWatchViewAdapter
//import com.module.feeds.watch.listener.FeedsListener
//import com.component.busilib.model.FeedSongModel
//import com.module.feeds.watch.model.FeedsWatchModel
//import com.module.feeds.watch.presenter.FeedWatchViewPresenter
//import com.module.feeds.watch.viewholder.FeedViewHolder
//import com.scwang.smartrefresh.layout.SmartRefreshLayout
//import com.scwang.smartrefresh.layout.api.RefreshLayout
//import com.scwang.smartrefresh.layout.header.ClassicsHeader
//import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
//import com.umeng.socialize.UMShareListener
//import com.umeng.socialize.bean.SHARE_MEDIA
//import kotlinx.coroutines.*
//import org.greenrobot.eventbus.EventBus
//import org.greenrobot.eventbus.Subscribe
//import org.greenrobot.eventbus.ThreadMode
//import java.lang.Exception
//
//
//class FeedsWatchView(val fragment: BaseFragment, val type: Int) : ConstraintLayout(fragment.context!!), IFeedsWatchView, IPersonFeedsWall, CoroutineScope by MainScope() {
//    val TAG = "FeedsWatchView"
//
//    constructor(fragment: BaseFragment, type: Int, userInfoModel: UserInfoModel, callBack: RequestCallBack?) : this(fragment, type) {
//        this.mUserInfo = userInfoModel
//        this.mCallBack = callBack
//        mPersenter.mUserInfo = userInfoModel
//    }
//
//    companion object {
//        const val TYPE_RECOMMEND = 1  // ??????
//        const val TYPE_FOLLOW = 2   // ??????
//        const val TYPE_PERSON = 3   // ????????????
//    }
//
//    private val mRefreshLayout: SmartRefreshLayout
//    private val mClassicsHeader: ClassicsHeader
//    private val mLayoutManager: LinearLayoutManager
//    private val mRecyclerView: RecyclerView
//
//    private var mLoadService: LoadService<*>? = null
//
//    private val mAdapter: FeedsWatchViewAdapter
//    private val mPersenter: FeedWatchViewPresenter = FeedWatchViewPresenter(this, type)
//
//    var mFeedsMoreDialogView: FeedsMoreDialogView? = null
//    var mSharePanel: SharePanel? = null
//    val playerTag = TAG + hashCode()
//
//    val playCallback: PlayerCallbackAdapter
//
//    var hasMore = true // ????????????????????????
//    var isSeleted = false  // ????????????
//
//    private var mUserInfo: UserInfoModel? = null
//    private var mCallBack: RequestCallBack? = null
//    private var mTipsDialogView: TipsDialogView? = null
//
//    fun isHomePage(): Boolean {
//        if (type == TYPE_RECOMMEND || type == TYPE_FOLLOW) {
//            return true
//        }
//        return false
//    }
//
//    override fun isHasMore(): Boolean {
//        return hasMore
//    }
//
//    init {
//        View.inflate(context, R.layout.feed_watch_view_layout, this)
//
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this)
//        }
//
//        mRefreshLayout = findViewById(R.id.refreshLayout)
//        mClassicsHeader = findViewById(R.id.classics_header)
//        mRecyclerView = findViewById(R.id.recycler_view)
//
//        mAdapter = FeedsWatchViewAdapter(object : FeedsListener {
//            override fun onClickCollectListener(position: Int, watchModel: FeedsWatchModel?) {
//                // ??????
//                watchModel?.let {
//                    mPersenter.collectOrUnCollectFeed(position, it)
//                }
//            }
//
//            override fun onClickShareListener(position: Int, watchModel: FeedsWatchModel?) {
//                // ??????
//                watchModel?.let {
//                    mSharePanel = SharePanel(fragment.activity)
//                            .apply {
//                                mTitle = it.song?.workName
//                                mDes = it.user?.nickname
//                                mPlayMusicUrl = it.song?.playURL
//                                mUrl = String.format("http://www.skrer.mobi/feed/song?songID=%d&userID=%d",
//                                        it.song?.songID, it.user?.userID)
//                            }
//                    mSharePanel?.show(ShareType.MUSIC)
//                    mSharePanel?.setUMShareListener(object : UMShareListener {
//                        override fun onResult(p0: SHARE_MEDIA?) {
//
//                        }
//
//                        override fun onCancel(p0: SHARE_MEDIA?) {
//
//                        }
//
//                        override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
//
//                        }
//
//                        override fun onStart(p0: SHARE_MEDIA?) {
//                            mPersenter.addShareCount(it)
//                        }
//                    })
//                }
//            }
//
//            override fun onClickAvatarListener(watchModel: FeedsWatchModel?) {
//                watchModel?.user?.let {
//                    val bundle = Bundle()
//                    bundle.putInt("bundle_user_id", it.userID)
//                    ARouter.getInstance()
//                            .build(RouterConstants.ACTIVITY_OTHER_PERSON)
//                            .with(bundle)
//                            .navigation()
//                }
//            }
//
//            override fun onclickRankListener(watchModel: FeedsWatchModel?) {
//                // ???????????????
//                watchModel?.let {
//                    ARouter.getInstance().build(RouterConstants.ACTIVITY_FEEDS_RANK_DETAIL)
//                            .withString("rankTitle", it.rank?.rankTitle)
//                            .withLong("challengeID", it.song?.challengeID ?: 0L)
//                            .withLong("challengeCnt", it.challengeCnt.toLong())
//                            .navigation()
//                }
//            }
//
//            override fun onClickLikeListener(position: Int, watchModel: FeedsWatchModel?) {
//                // ??????
//                watchModel?.let { mPersenter.feedLike(position, it) }
//            }
//
//            override fun onClickHitListener(watchModel: FeedsWatchModel?) {
//                SinglePlayer.reset(playerTag)
//                openFeedsMakeActivityFromChallenge(watchModel?.song?.challengeID)
//            }
//
//            override fun onClickDetailListener(position: Int, watchModel: FeedsWatchModel?) {
//                // ??????  ???????????????
//                // ?????????????????? resume ???
//                if (watchModel != null && watchModel.status == 2) {
//                    startPlay(position, watchModel)
////                    ARouter.getInstance().build(RouterConstants.ACTIVITY_FEEDS_DETAIL)
////                            .withInt("feed_ID", watchModel.feedID)
////                            .withInt("from", 1)
////                            .navigation()
//                    // ?????????????????????????????????
//                    fragment.activity?.let { fragmentActivity ->
//                        FeedsDetailActivity.openActivity(fragmentActivity, watchModel.feedID, 1, FeedSongPlayModeManager.PlayMode.ORDER, object : AbsPlayModeManager() {
//                            override fun getNextSong(userAction: Boolean): FeedSongModel? {
//                                return findNextSong(userAction)
//                            }
//
//                            override fun getPreSong(userAction: Boolean): FeedSongModel? {
//                                return findPresong(userAction)
//                            }
//                        })
//                    }
//                }
//
//            }
//
//            override fun onClickCDListener(position: Int, watchModel: FeedsWatchModel?) {
//                // ??????
//                watchModel?.let { model -> controlPlay(position, model, false) }
//            }
//
//            override fun onClickMoreListener(position: Int, watchModel: FeedsWatchModel?) {
//                // ??????
//                watchModel?.let {
//                    if (fragment.activity != null) {
//                        if (isHomePage()) {
//                            homePagerMore(it)
//                        } else {
//                            personPageMore(position, it)
//                        }
//                    }
//
//                }
//            }
//
//        }, type)
//
//        mRefreshLayout.apply {
//            setEnableRefresh(isHomePage())
//            setEnableLoadMore(isHomePage())
//            setEnableLoadMoreWhenContentNotFull(false)
//            setEnableOverScrollDrag(isHomePage())
//            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
//                override fun onLoadMore(refreshLayout: RefreshLayout) {
//                    getMoreFeeds()
//                }
//
//                override fun onRefresh(refreshLayout: RefreshLayout) {
//                    getFeeds(true)
//                }
//            })
//        }
//
//        mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        mRecyclerView.layoutManager = mLayoutManager
//        mRecyclerView.adapter = mAdapter
//        mAdapter.notifyDataSetChanged()
//
//        if (isHomePage()) {
//            addOnScrollListenerToRv()
//            val mLoadSir = LoadSir.Builder()
//                    .addCallback(EmptyCallback(R.drawable.home_list_empty_icon, "??????????????????", "#802F2F30"))
//                    .build()
//            mLoadService = mLoadSir.register(mRefreshLayout, Callback.OnReloadListener {
//                getFeeds(true)
//            })
//        }
//        playCallback = object : PlayerCallbackAdapter() {
//            override fun onCompletion() {
//                super.onCompletion()
//                MyLog.w(TAG, "PlayerCallbackAdapter onCompletion")
//                // ????????????
//                SinglePlayer.reset(playerTag)
//                // ???????????????????????????????????????
//                mAdapter.mCurrentPlayModel?.let { model ->
//                    mAdapter.mCurrentPlayPosition?.let {
//                        if (isHomePage()) {
//                            // ??????,????????????????????????
//                            launch {
//                                model.isCollected = mPersenter.getCollectedStatus(model)
//                                mAdapter.playComplete()
//                            }
//                        } else {
//                            // ????????????
//                            mAdapter.pausePlayModel()
//                        }
//
//                    }
//                }
//            }
//
//            override fun onPrepared() {
//                super.onPrepared()
//                mAdapter.resumeWhenBufferingEnd()
//                /**
//                 * ?????????
//                 */
//                mAdapter.mCurrentPlayPosition?.let {
//                    if (it + 1 < mAdapter!!.mDataList.size) {
//                        mAdapter.mDataList.get(it + 1)?.song?.playURL?.let { it2 ->
//                            MediaCacheManager.preCache(it2)
//                        }
//                    }
//                }
//            }
//
//            override fun openTimeFlyMonitor(): Boolean {
//                return true
//            }
//
//            override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
//                MyLog.d(TAG, "onBufferingUpdate percent=$percent")
//                if (percent == 100) {
//                    if (SinglePlayer.isPlaying) {
//                        mAdapter.resumeWhenBufferingEnd()
//                    }
//                } else {
//                    mAdapter.pauseWhenBuffering()
//                }
//            }
//
//            override fun onTimeFlyMonitor(pos: Long, duration: Long) {
//                if (mAdapter.playing == true) {
//                    mAdapter.updatePlayProgress(pos, duration)
//                    mAdapter.mCurrentPlayPosition?.let { position ->
//                        try {
//                            val holder = mRecyclerView.findViewHolderForAdapterPosition(position)
//                            if (holder is FeedViewHolder?) {
//                                mAdapter.mCurrentPlayModel?.let { model ->
//                                    holder?.playLyric()
//                                }
//                            }
//                        } catch (e: Exception) {
//                            MyLog.e(e)
//                        }
//                    }
//
//                    FeedsPlayStatistics.updateCurProgress(pos, duration)
//                } else {
//                    if (MyLog.isDebugLogOpen()) {
//                        U.getToastUtil().showShort("FeedsWatchView???bug,??????????????????")
//                    }
//                    pausePlay()
//                }
//
//            }
//        }
//        SinglePlayer.addCallback(playerTag, playCallback)
//    }
//
//    private fun findPresong(userAction: Boolean): FeedSongModel? {
//        if (type == TYPE_RECOMMEND) {
//            // todo ????????????
//            if (mAdapter.mCurrentPlayPosition == 1 || mAdapter.mCurrentPlayPosition == 0) {
//                return null
//            }
//            mAdapter.mCurrentPlayPosition?.let {
//                if (!mAdapter.mDataList.isNullOrEmpty()) {
//                    mAdapter.mCurrentPlayPosition = it - 1
//                    mAdapter.mCurrentPlayModel = mAdapter.mDataList[(mAdapter.mCurrentPlayPosition
//                            ?: 0) - 1]
//                    return if (mAdapter.mCurrentPlayModel?.status != 2) {
//                        // ???????????????
//                        findPresong(userAction)
//                    } else {
//                        mAdapter.mCurrentPlayModel?.song
//                    }
//                }
//            }
//
//        } else {
//            if (mAdapter.mCurrentPlayPosition == 0) {
//                return null
//            }
//
//            mAdapter.mCurrentPlayPosition?.let {
//                if (!mAdapter.mDataList.isNullOrEmpty()) {
//                    mAdapter.mCurrentPlayPosition = it - 1
//                    mAdapter.mCurrentPlayModel = mAdapter.mDataList[mAdapter.mCurrentPlayPosition
//                            ?: 0]
//                    return if (mAdapter.mCurrentPlayModel?.status != 2) {
//                        // ???????????????
//                        findPresong(userAction)
//                    } else {
//                        mAdapter.mCurrentPlayModel?.song
//                    }
//                }
//            }
//        }
//
//        return null
//    }
//
//    private fun findNextSong(userAction: Boolean): FeedSongModel? {
//        if (type == TYPE_RECOMMEND) {
//            // ????????????
//            if (mAdapter.mCurrentPlayPosition == mAdapter.mDataList.size - 1) {
//                getMoreFeeds()
//            }
//            if (mAdapter.mCurrentPlayPosition != null && mAdapter.mCurrentPlayPosition!! < mAdapter.mDataList.size - 1) {
//                // ??????????????????
//                mAdapter.mCurrentPlayPosition = mAdapter.mCurrentPlayPosition!! + 1
//                mAdapter.mCurrentPlayModel = mAdapter.mDataList[mAdapter.mCurrentPlayPosition!! - 1]
//                return if (mAdapter.mCurrentPlayModel?.status != 2) {
//                    // ??????????????????
//                    findNextSong(userAction)
//                } else {
//                    mAdapter.mCurrentPlayModel?.song
//                }
//            }
//        } else {
//            if (mAdapter.mCurrentPlayPosition == mAdapter.mDataList.size - 2) {
//                // ?????????????????????????????????????????????
//                getMoreFeeds()
//            }
//
//            if (mAdapter.mCurrentPlayPosition != null && mAdapter.mCurrentPlayPosition!! < mAdapter.mDataList.size - 1) {
//                // ??????????????????
//                mAdapter.mCurrentPlayPosition = mAdapter.mCurrentPlayPosition!! + 1
//                mAdapter.mCurrentPlayModel = mAdapter.mDataList[mAdapter.mCurrentPlayPosition!!]
//                return if (mAdapter.mCurrentPlayModel?.status != 2) {
//                    // ??????????????????
//                    findNextSong(userAction)
//                } else {
//                    mAdapter.mCurrentPlayModel?.song
//                }
//            }
//        }
//        return null
//    }
//
//    private fun addOnScrollListenerToRv() {
//        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            var maxPercent = 0f
//            var model: FeedsWatchModel? = null
//            var isFound = false
//
//            val cdHeight = 168.dp()   // ????????????
//            val bottomHeight = 50.dp()  // ????????????
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                when (newState) {
//                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
//                        var postion = -1
//                        // ??????????????????????????????????????????????????????
//                        val firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()
//                        val lastVisibleItem = mLayoutManager.findLastVisibleItemPosition()
//                        if (firstVisibleItem != RecyclerView.NO_POSITION && lastVisibleItem != RecyclerView.NO_POSITION) {
//                            val percents = FloatArray(lastVisibleItem - firstVisibleItem + 1)
//                            var i = firstVisibleItem
//                            isFound = false
//                            maxPercent = 0f
//                            model = null
//                            while (i <= lastVisibleItem && !isFound) {
//                                if (mRecyclerView.findViewHolderForAdapterPosition(i) != null) {
//                                    val itemView = mRecyclerView.findViewHolderForAdapterPosition(i).itemView
//                                    val location1 = IntArray(2)
//                                    val location2 = IntArray(2)
//                                    itemView.getLocationOnScreen(location1)
//                                    mRecyclerView.getLocationOnScreen(location2)
//                                    val top = location1[1] - location2[1]
//                                    when {
//                                        top < 0 -> {
//                                            // ?????????
//                                            if ((itemView.height + top) >= (cdHeight + bottomHeight)) {
//                                                percents[i - firstVisibleItem] = 100f
//                                            } else {
//                                                percents[i - firstVisibleItem] = (itemView.height + top - bottomHeight).toFloat() / cdHeight.toFloat()
//                                            }
//                                        }
//                                        (top + itemView.height) < mRecyclerView.height -> {
//                                            percents[i - firstVisibleItem] = 100f
//                                        }
//                                        else -> {
//                                            // ?????????
//                                            if ((mRecyclerView.height - top) >= (itemView.height - bottomHeight)) {
//                                                percents[i - firstVisibleItem] = 100f
//                                            } else {
//                                                percents[i - firstVisibleItem] = (itemView.height - (mRecyclerView.height - top) - bottomHeight).toFloat() / cdHeight.toFloat()
//                                            }
//                                        }
//                                    }
//                                    if (percents[i - firstVisibleItem] == 100f) {
//                                        isFound = true
//                                        maxPercent = 100f
//                                        postion = i
//                                    } else {
//                                        if (percents[i - firstVisibleItem] > maxPercent) {
//                                            maxPercent = percents[i - firstVisibleItem]
//                                            postion = i
//                                        }
//                                    }
//                                }
//                                i++
//                            }
//                        }
//                        if (postion >= 0) {
//                            isFound = true
//                            model = if (type == TYPE_RECOMMEND) {
//                                // ????????????????????????????????????
//                                if (postion == 0) postion = 1
//                                if (!mAdapter.mDataList.isNullOrEmpty() && postion - 1 >= 0 && postion - 1 < mAdapter.mDataList.size) {
//                                    mAdapter.mDataList[postion - 1]
//                                } else {
//                                    null
//                                }
//                            } else {
//                                mAdapter.mDataList[postion]
//                            }
//                            controlPlay(postion, model, true)
//                        }
//                    }
//                    RecyclerView.SCROLL_STATE_DRAGGING -> {
//                    }
//                    RecyclerView.SCROLL_STATE_SETTLING -> {
//                    }
//                }
//            }
//        })
//    }
//
//    private fun controlPlay(pos: Int, model: FeedsWatchModel?, isMustPlay: Boolean) {
//        MyLog.d(TAG, "controlPlay isSeleted = $isSeleted")
//        if (model != null && model != mAdapter.mCurrentPlayModel) {
//            SinglePlayer.reset(playerTag)
//        }
//        if (isMustPlay) {
//            // ??????
//            startPlay(pos, model)
//        } else {
//            if (mAdapter.mCurrentPlayModel == model && mAdapter.playing) {
//                // ????????????
//                pausePlay()
//            } else {
//                // ??????
//                startPlay(pos, model)
//            }
//        }
//    }
//
//    private fun startPlay(pos: Int, model: FeedsWatchModel?) {
//        MyLog.d(TAG, "startPlayModel isSeleted = $isSeleted pos=$pos mCurrentPlayPosition=${mAdapter.mCurrentPlayPosition}")
//        mAdapter.startPlayModel(pos, model)
//        // ??????????????????????????????????????????????????????????????????
//        if (!isSeleted) {
//            mAdapter.pausePlayModel()
//            return
//        }
//        model?.song?.playURL?.let {
//            FeedsPlayStatistics.setCurPlayMode(model.feedID)
//            SinglePlayer.startPlay(playerTag, it)
//        }
//    }
//
//    /**
//     * ????????????
//     */
//    private fun resumePlay() {
//        MyLog.d(TAG, "resumePlay isSeleted= $isSeleted")
//        if (!isSeleted) {
//            mAdapter.pausePlayModel()
//            return
//        }
//        mAdapter.resumePlayModel()
//        if (isHomePage()) {
//            srollPositionToTop(mAdapter.mCurrentPlayPosition ?: 0)
//        }
//        mAdapter.mCurrentPlayModel?.song?.playURL?.let {
//            FeedsPlayStatistics.setCurPlayMode(mAdapter.mCurrentPlayModel?.feedID ?: 0)
//            SinglePlayer.startPlay(playerTag, it)
//        }
//    }
//
//    private fun srollPositionToTop(position: Int) {
//        // ????????????
//        val firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()
//        val lastVisibleItem = mLayoutManager.findLastVisibleItemPosition()
//        if (position < firstVisibleItem || position > lastVisibleItem) {
//            mLayoutManager.scrollToPositionWithOffset(position, 0)
//        }
//    }
//
//    private fun pausePlay() {
//        MyLog.d(TAG, "pausePlay")
//        mAdapter.pausePlayModel()
//        SinglePlayer.pause(playerTag)
//    }
//
//    override fun getFeeds(flag: Boolean) {
//        if (mAdapter.mDataList.isNullOrEmpty()) {
//            // ???????????????????????????????????????
//            mPersenter.initWatchList(true)
//        } else {
//            mPersenter.initWatchList(flag)
//        }
//    }
//
//    override fun getMoreFeeds() {
//        mPersenter.loadMoreWatchList()
//    }
//
//    override fun setUserInfoModel(userInfoModel: Any?) {
//        mUserInfo = userInfoModel as UserInfoModel
//        mPersenter.mUserInfo = mUserInfo
//    }
//
//    override fun addWatchList(list: List<FeedsWatchModel>?, isClear: Boolean, hasMore: Boolean) {
//        this.hasMore = hasMore
//        mRefreshLayout.finishRefresh()
//        mRefreshLayout.finishLoadMore()
//        mRefreshLayout.setEnableLoadMore(hasMore)
//
//        mCallBack?.onRequestSucess(hasMore)
//        if (isClear) {
//            mAdapter.mDataList.clear()
//            if (list != null && list.isNotEmpty()) {
//                mAdapter.mDataList.addAll(list)
//            }
//            mAdapter.notifyDataSetChanged()
//            mRecyclerView.smoothScrollToPosition(0)
//            if (isHomePage()) {
//                if (mAdapter.mDataList.isNotEmpty() == true) {
//                    // delay ?????????2???notify??????
//                    launch {
//                        delay(200)
//                        if (type == TYPE_RECOMMEND) {
//                            controlPlay(1, mAdapter.mDataList.get(0), true)
//                        } else {
//                            controlPlay(0, mAdapter.mDataList.get(0), true)
//                        }
//                    }
//                } else {
//                    // ????????????????????????
//                    pausePlay()
//                    mAdapter.mCurrentPlayModel = null
//                }
//            }
//
//        } else {
//            if (list != null && list.isNotEmpty()) {
//                mAdapter.mDataList.addAll(list)
//                mAdapter.notifyDataSetChanged()
//            }
//        }
//
//        if (mAdapter.mDataList != null && mAdapter.mDataList.isNotEmpty()) {
//            mLoadService?.showSuccess()
//        } else {
//            mLoadService?.showCallback(EmptyCallback::class.java)
//        }
//    }
//
//    override fun requestError() {
//        mCallBack?.onRequestSucess(true)
//        mRefreshLayout.finishRefresh()
//        mRefreshLayout.finishLoadMore()
//    }
//
//    override fun feedLikeResult(position: Int, model: FeedsWatchModel, isLike: Boolean) {
//        model.isLiked = isLike
//        if (isLike) {
//            model.starCnt = model.starCnt.plus(1)
//        } else {
//            model.starCnt = model.starCnt.minus(1)
//        }
//        mAdapter.update(position, model, FeedsWatchViewAdapter.REFRESH_TYPE_LIKE)
//    }
//
//    override fun feedDeleteResult(position: Int, model: FeedsWatchModel) {
//        if (mAdapter.mCurrentPlayModel == model) {
//            SinglePlayer.pause(playerTag)
//            mAdapter.mCurrentPlayModel = null
//            mAdapter.mCurrentPlayPosition = null
//        }
//        mAdapter.delete(model)
//    }
//
//    override fun showCollect(position: Int, model: FeedsWatchModel) {
//        mAdapter.update(position, model, FeedsWatchViewAdapter.REFRESH_TYPE_COLLECT)
//    }
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//    }
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        mFeedsMoreDialogView?.dismiss(false)
//    }
//
//    override fun destroy() {
//        cancel()
//        mPersenter.destroy()
//        mFeedsMoreDialogView?.dismiss(false)
//        mSharePanel?.dismiss(false)
//        SinglePlayer.reset(playerTag)
//        SinglePlayer.removeCallback(playerTag)
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this)
//        }
//    }
//
//    override fun unselected() {
//        MyLog.d(TAG, "unselected")
//        isSeleted = false
//        pausePlay()
//    }
//
//    override fun selected() {
//        MyLog.d(TAG, "selected")
//        isSeleted = true
//        // ???????????????????????????????????????????????????????????????
//        if (!mPersenter.initWatchList(false)) {
//            // ???????????????????????????????????????????????????,?????????????????????
//            if (isHomePage()) {
//                resumePlay()
//            }
//        }
//    }
//
//    fun autoRefresh() {
//        mRefreshLayout.autoRefresh()
//    }
//
//    private fun homePagerMore(it: FeedsWatchModel) {
//        mFeedsMoreDialogView?.dismiss(false)
//        if (it.user?.userID == MyUserInfoManager.getInstance().uid.toInt()) {
//            mFeedsMoreDialogView = FeedsMoreDialogView(fragment.activity!!, FeedsMoreDialogView.FROM_FEED_HOME, it, true)
//            mFeedsMoreDialogView?.showByDialog()
//        } else {
//            mFeedsMoreDialogView = FeedsMoreDialogView(fragment.activity!!, FeedsMoreDialogView.FROM_FEED_HOME, it, null)
//            mFeedsMoreDialogView?.showByDialog()
//        }
//    }
//
//    private fun personPageMore(position: Int, it: FeedsWatchModel) {
//        if ((mUserInfo?.userId
//                        ?: 0).toLong() == MyUserInfoManager.getInstance().uid) {
//            mFeedsMoreDialogView = FeedsMoreDialogView(fragment.activity!!, FeedsMoreDialogView.FROM_PERSON, it, null)
//                    .apply {
//                        mCopyReportTv.text = "??????"
//                        mCopyReportTv.setOnClickListener(object : DebounceViewClickListener() {
//                            override fun clickValid(v: View?) {
//                                dismiss(false)
//                                mTipsDialogView = TipsDialogView.Builder(fragment.activity)
//                                        .setMessageTip("???????????????????????????")
//                                        .setConfirmTip("????????????")
//                                        .setCancelTip("??????")
//                                        .setCancelBtnClickListener(object : AnimateClickListener() {
//                                            override fun click(view: View?) {
//                                                mTipsDialogView?.dismiss()
//                                            }
//                                        })
//                                        .setConfirmBtnClickListener(object : AnimateClickListener() {
//                                            override fun click(view: View?) {
//                                                mTipsDialogView?.dismiss(false)
//                                                mPersenter.deleteFeed(position, it)
//                                            }
//                                        })
//                                        .build()
//                                mTipsDialogView?.showByDialog()
//                            }
//                        })
//                    }
//        } else {
//            mFeedsMoreDialogView = FeedsMoreDialogView(fragment.activity!!, FeedsMoreDialogView.FROM_OTHER_PERSON, it, null)
//        }
//        mFeedsMoreDialogView?.showByDialog()
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: FeedDetailChangeEvent) {
//        // ??????????????????
//        event.model?.let {
//            mAdapter.updateModelFromDetail(it)
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: RelationChangeEvent) {
//        // ????????????????????????
//        if (type == TYPE_FOLLOW) {
//            mPersenter.mHasInitData = false
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: FeedsCollectChangeEvent) {
//        mAdapter.mDataList.forEachIndexed { index, feedsWatchModel ->
//            if (feedsWatchModel.feedID == event.feedID) {
//                feedsWatchModel.isCollected = event.isCollected
//                mAdapter.update(index, feedsWatchModel, FeedsWatchViewAdapter.REFRESH_TYPE_COLLECT)
//            }
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: FeedPublishSucessEvent) {
//        // ????????????,????????????????????????
//        if (type == TYPE_PERSON) {
//            mPersenter.mHasInitData = false
//        }
//    }
//}