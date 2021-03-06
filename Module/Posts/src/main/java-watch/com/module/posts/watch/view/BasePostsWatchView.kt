package com.module.posts.watch.view

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AbsListView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSON
import com.common.callback.Callback
import com.common.core.myinfo.MyUserInfoManager
import com.common.log.MyLog
import com.common.player.SinglePlayer
import com.common.player.SinglePlayerCallbackAdapter
import com.common.rxretrofit.ApiManager
import com.common.rxretrofit.ControlType
import com.common.rxretrofit.RequestControl
import com.common.rxretrofit.subscribe
import com.common.statistics.StatisticsAdapter
import com.common.utils.U
import com.common.view.DebounceViewClickListener
import com.component.busilib.callback.EmptyCallback
import com.component.person.event.ChildViewPlayAudioEvent
import com.dialog.view.TipsDialogView
import com.imagebrowse.ImageBrowseView
import com.imagebrowse.big.BigImageBrowseActivity
import com.imagebrowse.big.BigImageBrowseFragment
import com.imagebrowse.big.DefaultImageBrowserLoader
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.module.RouterConstants
import com.module.posts.R
import com.module.posts.detail.activity.PostsDetailActivity
import com.module.posts.detail.event.PostsDetailEvent
import com.module.posts.more.PostsMoreDialogView
import com.module.posts.redpkg.PostsRedPkgDialogView
import com.module.posts.statistics.PostsStatistics
import com.module.posts.watch.PostsWatchServerApi
import com.module.posts.watch.UNSELECT_REASON_TO_OTHER_ACTIVITY
import com.module.posts.watch.adapter.PostsWatchListener
import com.module.posts.watch.adapter.PostsWatchViewAdapter
import com.module.posts.watch.model.PostsWatchModel
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

abstract class BasePostsWatchView(val activity: FragmentActivity, val type: Int) : ConstraintLayout(activity), CoroutineScope by MainScope() {

    val TAG = when (type) {
        TYPE_POST_FOLLOW -> "FollowPostsWatchView"
        TYPE_POST_RECOMMEND -> "RecommendPostsWatchView"
        TYPE_POST_LAST -> "LastPostsWatchView"
        TYPE_POST_PERSON -> "PersonPostsWatchView"
        TYPE_POST_TOPIC -> "TopicPostsWatchView"
        TYPE_POST_DYNAMIC -> "DynamicPostsWatchView"
        else -> "BasePostsWatchView"
    }

    companion object {
        const val TYPE_POST_FOLLOW = 1     // ??????
        const val TYPE_POST_RECOMMEND = 2  // ??????
        const val TYPE_POST_LAST = 3       // ??????
        const val TYPE_POST_PERSON = 4     // ????????????
        const val TYPE_POST_TOPIC = 5      // ??????
        const val TYPE_POST_DYNAMIC = 6    // ??????
    }

    val postsWatchServerApi = ApiManager.getInstance().createService(PostsWatchServerApi::class.java)

    val playerTag = TAG + hashCode()
    private val playCallback: SinglePlayerCallbackAdapter

    var isSeleted = false  // ????????????
    var mHasInitData = false  //???????????????????????????????????????
    var hasMore = true // ????????????????????????
    var mOffset = 0   //?????????
    val mCNT = 20  // ?????????????????????

    private val refreshLayout: SmartRefreshLayout
    private val classicsHeader: ClassicsHeader
    private val recyclerView: RecyclerView
    private var layoutManager: LinearLayoutManager

    var adapter: PostsWatchViewAdapter? = null

    var postsMoreDialogView: PostsMoreDialogView? = null
    var postsRedPkgDialogView: PostsRedPkgDialogView? = null
    var tipsDialogView: TipsDialogView? = null

    var mLoadService: LoadService<*>? = null

    fun dismissDialog() {
        tipsDialogView?.dismiss(false)
        postsMoreDialogView?.dismiss(false)
        postsRedPkgDialogView?.dismiss(false)
    }

    fun autoRefresh() {
        refreshLayout.autoRefresh()
    }

    fun srollPositionToTop(position: Int) {
        // ????????????
        if (position >= 0) {
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
            if (position <= firstVisibleItem || position >= lastVisibleItem) {
                layoutManager.scrollToPositionWithOffset(position, 0)
            }
        }
    }

    init {
        View.inflate(context, R.layout.posts_watch_view_layout, this)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        refreshLayout = this.findViewById(R.id.refreshLayout)
        classicsHeader = this.findViewById(R.id.classics_header)
        recyclerView = this.findViewById(R.id.recycler_view)

        adapter = PostsWatchViewAdapter(context, type, object : PostsWatchListener {
            override fun onClickPostsDetail(position: Int, model: PostsWatchModel?) {
                if (model != null && model.isAudit()) {
                    recordClick(model)
                    // ???????????????
                    var pendingPlayingUrl: String? = null
                    if (adapter?.mCurrentPlayModel == model
                            && (adapter?.playStatus == PostsWatchViewAdapter.PLAY_POSTS_AUDIO || adapter?.playStatus == PostsWatchViewAdapter.PLAY_POSTS_SONG)) {
                        if (adapter?.playStatus == PostsWatchViewAdapter.PLAY_POSTS_AUDIO) {
                            pendingPlayingUrl = adapter?.mCurrentPlayModel?.posts?.audios?.getOrNull(0)?.url
                        } else if (adapter?.playStatus == PostsWatchViewAdapter.PLAY_POSTS_SONG) {
                            pendingPlayingUrl = adapter?.mCurrentPlayModel?.posts?.song?.playURL
                        }
                    } else {
                        SinglePlayer.stop(playerTag)
                        adapter?.stopPlay()
                    }
                    ARouter.getInstance().build(RouterConstants.ACTIVITY_POSTS_DETAIL)
                            .withInt("postsID", model.posts?.postsID?.toInt() ?: 0)
                            .withString("playingUrl", pendingPlayingUrl)
                            .navigation()
                } else {
                    U.getToastUtil().showShort("???????????????????????????????????????")
                }
            }

            override fun onClickPostsAvatar(position: Int, model: PostsWatchModel?) {
                model?.user?.let {
                    recordClick(model)
                    openOtherPersonCenter(it.userId)
                }
            }

            override fun onClickPostsMore(position: Int, model: PostsWatchModel?) {
                dismissDialog()
                model?.let {
                    recordClick(model)
                    postsMoreDialogView?.dismiss(false)
                    var from = PostsMoreDialogView.FROM_POSTS_HOME
                    if (type == TYPE_POST_PERSON) {
                        from = PostsMoreDialogView.FROM_POSTS_PERSON
                    } else if (type == TYPE_POST_TOPIC) {
                        from = PostsMoreDialogView.FROM_POSTS_TOPIC
                    }
                    postsMoreDialogView = PostsMoreDialogView(activity, from, it)
                    if (it.user?.userId == MyUserInfoManager.uid.toInt()) {
                        postsMoreDialogView?.apply {
                            reportTv.text = "??????"
                            reportTv.setOnClickListener(object : DebounceViewClickListener() {
                                override fun clickValid(v: View?) {
                                    postsMoreDialogView?.dismiss(false)
                                    tipsDialogView?.dismiss(false)
                                    tipsDialogView = TipsDialogView.Builder(activity)
                                            .setMessageTip("??????????????????????")
                                            .setCancelTip("??????")
                                            .setCancelBtnClickListener {
                                                tipsDialogView?.dismiss()
                                            }
                                            .setConfirmTip("??????")
                                            .setConfirmBtnClickListener {
                                                tipsDialogView?.dismiss()
                                                deletePosts(position, model)
                                            }
                                            .build()
                                    tipsDialogView?.showByDialog()
                                }
                            })
                        }
                    }
                    postsMoreDialogView?.showByDialog(true)
                }
            }

            override fun onClickPostsAudio(position: Int, model: PostsWatchModel?) {
                StatisticsAdapter.recordCountEvent("posts", "list_voice_click", null)
                recordClick(model)
                if (adapter?.playStatus == PostsWatchViewAdapter.PLAY_POSTS_AUDIO && model == adapter?.mCurrentPlayModel) {
                    SinglePlayer.stop(playerTag)
                } else {
                    model?.posts?.audios?.let {
                        SinglePlayer.startPlay(playerTag, it[0].url)
                        postPlayEvent()
                    }
                }
                adapter?.startOrPauseAudio(position, model, PostsWatchViewAdapter.PLAY_POSTS_AUDIO)
            }

            override fun onClickPostsSong(position: Int, model: PostsWatchModel?) {
                StatisticsAdapter.recordCountEvent("posts", "list_music_click", null)
                recordClick(model)
                if (adapter?.playStatus == PostsWatchViewAdapter.PLAY_POSTS_SONG && model == adapter?.mCurrentPlayModel) {
                    SinglePlayer.stop(playerTag)
                } else {
                    model?.posts?.song?.playURL?.let {
                        SinglePlayer.startPlay(playerTag, it)
                        postPlayEvent()
                    }
                }
                adapter?.startOrPauseAudio(position, model, PostsWatchViewAdapter.PLAY_POSTS_SONG)
            }

            override fun onClickPostsImage(position: Int, model: PostsWatchModel?, index: Int, url: String?) {
                StatisticsAdapter.recordCountEvent("posts", "list_picture_click", null)
                recordClick(model)
                goBigImageBrowse(index, model?.posts?.pictures)
            }

            override fun onClickPostsRedPkg(position: Int, model: PostsWatchModel?) {
                dismissDialog()
                recordClick(model)
                model?.posts?.redpacketInfo?.let {
                    postsRedPkgDialogView?.dismiss(false)
                    postsRedPkgDialogView = PostsRedPkgDialogView(activity, it)
                    postsRedPkgDialogView?.showByDialog()
                }
            }

            override fun onClickPostsTopic(position: Int, model: PostsWatchModel?) {
                if (type == TYPE_POST_TOPIC) {
                    U.getToastUtil().showShort("????????????????????????")
                } else {
                    StatisticsAdapter.recordCountEvent("posts", "topic_content_click", null)
                    recordClick(model)
                    model?.posts?.topicInfo?.let {
                        ARouter.getInstance().build(RouterConstants.ACTIVITY_POSTS_TOPIC)
                                .withLong("topicID", it.topicID.toLong())
                                .navigation()
                    }
                }
            }

            override fun onClickPostsLike(position: Int, model: PostsWatchModel?) {
                if (model != null && model.isAudit()) {
                    StatisticsAdapter.recordCountEvent("posts", "content_like", null)
                    recordClick(model)
                    postsLikeOrUnLike(position, model)
                } else {
                    U.getToastUtil().showShort("???????????????????????????????????????")
                }
            }

            override fun onClickPostsVote(position: Int, model: PostsWatchModel?, index: Int) {
                // ????????????????????????
                if (model != null && model.isAudit()) {
                    if (model.posts?.voteInfo?.hasVoted == true) {
                        // ????????????????????????
                    } else {
                        StatisticsAdapter.recordCountEvent("posts", "list_vote_click", null)
                        recordClick(model)
                        votePosts(position, model, index)
                    }
                } else {
                    U.getToastUtil().showShort("???????????????????????????????????????")
                }
            }

            override fun onClickCommentAvatar(position: Int, model: PostsWatchModel?) {
                model?.bestComment?.user?.let {
                    recordClick(model)
                    openOtherPersonCenter(it.userId)
                }
            }

            override fun onClickCommentLike(position: Int, model: PostsWatchModel?) {
                if (model != null && model.isAudit()) {
                    StatisticsAdapter.recordCountEvent("posts", "content_like", null)
                    recordClick(model)
                    postsCommentLikeOrUnLike(position, model)
                } else {
                    U.getToastUtil().showShort("???????????????????????????????????????")
                }
            }

            override fun onClickCommentAudio(position: Int, model: PostsWatchModel?) {
                StatisticsAdapter.recordCountEvent("posts", "list_voice_click", null)
                recordClick(model)
                if (adapter?.playStatus == PostsWatchViewAdapter.PLAY_POSTS_COMMENT_AUDIO && model == adapter?.mCurrentPlayModel) {
                    SinglePlayer.stop(playerTag)
                } else {
                    model?.bestComment?.comment?.audios?.let {
                        SinglePlayer.startPlay(playerTag, it[0].url)
                        postPlayEvent()
                    }
                }
                adapter?.startOrPauseAudio(position, model, PostsWatchViewAdapter.PLAY_POSTS_COMMENT_AUDIO)
            }

            override fun onClickCommentSong(position: Int, model: PostsWatchModel?) {
                StatisticsAdapter.recordCountEvent("posts", "list_music_click", null)
                recordClick(model)
                if (adapter?.playStatus == PostsWatchViewAdapter.PLAY_POSTS_COMMENT_SONG && model == adapter?.mCurrentPlayModel) {
                    SinglePlayer.stop(playerTag)
                } else {
                    model?.bestComment?.comment?.song?.playURL?.let {
                        SinglePlayer.startPlay(playerTag, it)
                        postPlayEvent()
                    }
                }
                adapter?.startOrPauseAudio(position, model, PostsWatchViewAdapter.PLAY_POSTS_COMMENT_SONG)
            }

            override fun onClickCommentImage(position: Int, model: PostsWatchModel?, index: Int, url: String?) {
                StatisticsAdapter.recordCountEvent("posts", "list_picture_click", null)
                recordClick(model)
                goBigImageBrowse(index, model?.bestComment?.comment?.pictures)
            }
        })
        refreshLayout.apply {
            setEnableRefresh(type != TYPE_POST_TOPIC && type != TYPE_POST_DYNAMIC)
            setEnableLoadMore(type != TYPE_POST_TOPIC && type != TYPE_POST_DYNAMIC)
            setEnableLoadMoreWhenContentNotFull(false)
            setEnableOverScrollDrag(type != TYPE_POST_TOPIC)
            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    getMorePosts()
                }

                override fun onRefresh(refreshLayout: RefreshLayout) {
                    if (type == TYPE_POST_RECOMMEND) {
                        StatisticsAdapter.recordCountEvent("posts", "hot_tab_refresh", null)
                    }
                    initPostsList(true)
                }
            })
        }


        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        playCallback = object : SinglePlayerCallbackAdapter() {
            override fun onCompletion() {
                super.onCompletion()
                adapter?.stopPlay()
            }

            override fun onPlaytagChange(oldPlayerTag: String?, newPlayerTag: String?) {
                if (newPlayerTag != playerTag) {
                    adapter?.stopPlay()
                }
            }
        }
        SinglePlayer.addCallback(playerTag, playCallback)

        if (type != TYPE_POST_PERSON) {
            val mLoadSir = LoadSir.Builder()
                    .addCallback(EmptyCallback(R.drawable.home_list_empty_icon, "??????????????????", "#802F2F30"))
                    .build()
            mLoadService = mLoadSir.register(refreshLayout, com.kingja.loadsir.callback.Callback.OnReloadListener {
                initPostsList(true)
            })

            addOnScrollListenerToRv()
        }
    }

    private fun postPlayEvent() {
        if (type == TYPE_POST_PERSON) {
            EventBus.getDefault().post(ChildViewPlayAudioEvent())
        }
    }

    private fun goBigImageBrowse(index: Int, list: List<String>?) {
        BigImageBrowseFragment.open(true, context as FragmentActivity, object : DefaultImageBrowserLoader<String>() {
            override fun init() {

            }

            override fun load(imageBrowseView: ImageBrowseView, position: Int, item: String) {
                imageBrowseView.load(item)
            }

            override fun getInitCurrentItemPostion(): Int {
                return index
            }

            override fun getInitList(): List<String>? {
                return list
            }

            override fun loadMore(backward: Boolean, position: Int, data: String, callback: Callback<List<String>>?) {
                if (backward) {
                    // ????????????
                }
            }

            override fun hasMore(backward: Boolean, position: Int, data: String): Boolean {
                return if (backward) {
                    return false
                } else false
            }

            override fun hasSaveMenu(): Boolean {
                return true
            }
        })
    }

    private fun openOtherPersonCenter(userID: Int) {
        val bundle = Bundle()
        bundle.putInt("bundle_user_id", userID)
        ARouter.getInstance()
                .build(RouterConstants.ACTIVITY_OTHER_PERSON)
                .with(bundle)
                .navigation()
    }

    open fun unselected(reason: Int) {
        isSeleted = false
        if (reason == UNSELECT_REASON_TO_OTHER_ACTIVITY) {
            // ??????????????????????????????  ???????????????
            val topActivity = U.getActivityUtils().topActivity
            if (topActivity is PostsDetailActivity) {
                return
            }
            if (topActivity is BigImageBrowseActivity) {
                return
            }
        }
        SinglePlayer.stop(playerTag)
        adapter?.stopPlay()
    }

    open fun selected() {
        isSeleted = true
        if (!initPostsList(false)) {
            recordExposure("selected")
        }
    }

    fun addWatchPosts(list: List<PostsWatchModel>?, clear: Boolean) {
        if (clear) {
            // ?????????????????????????????????
            SinglePlayer.stop(playerTag)
            adapter?.stopPlay()
            adapter?.mDataList?.clear()
            if (!list.isNullOrEmpty()) {
                adapter?.mDataList?.addAll(list)
            }
            adapter?.notifyDataSetChanged()
            srollPositionToTop(0)
            recordExposure("addWatchPosts")
        } else {
            if (!list.isNullOrEmpty()) {
                val size = adapter?.mDataList?.size!!
                adapter?.mDataList?.addAll(list)
                val newSize = adapter?.mDataList?.size!!
                adapter?.notifyItemRangeInserted(size, newSize - size)
            }
        }

        if (type != TYPE_POST_PERSON) {
            if (adapter?.mDataList.isNullOrEmpty()) {
                // ????????????
                mLoadService?.showCallback(EmptyCallback::class.java)
            } else {
                mLoadService?.showSuccess()
            }
        }
    }

    fun finishRefreshOrLoadMore() {
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore()
        refreshLayout.setEnableLoadMore(hasMore)
    }

    // ????????????
    abstract fun initPostsList(flag: Boolean): Boolean

    // ??????????????????
    abstract fun getMorePosts()

    private fun addOnScrollListenerToRv() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            //??????????????????????????????????????????views
            private var isFirstVisible = true

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isFirstVisible) {
                    recordExposure("onScrolled isFirstVisible")
                    isFirstVisible = false
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
                        recordExposure("SCROLL_STATE_IDLE")
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {
                    }
                }
            }
        })
    }

    fun recordExposure(from: String) {
        // ???????????????????????????
        if (type != TYPE_POST_PERSON) {
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
            if (firstVisibleItem != RecyclerView.NO_POSITION && lastVisibleItem != RecyclerView.NO_POSITION) {
                for (i in firstVisibleItem..lastVisibleItem) {
                    if (adapter?.mDataList?.isNullOrEmpty() == false) {
                        adapter?.mDataList?.let {
                            if (i in 0 until it.size) {
                                it[i].posts?.postsID?.let { postsID ->
                                    MyLog.d(TAG, "recordExposure from = $from postsID = $postsID")
                                    PostsStatistics.addCurExpose(postsID.toInt())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun recordClick(model: PostsWatchModel?) {
        if (type != TYPE_POST_PERSON) {
            model?.posts?.postsID?.let {
                PostsStatistics.addCurClick(it.toInt())
            }
        }
    }

    // ????????????
    fun postsLikeOrUnLike(position: Int, model: PostsWatchModel) {
        launch {
            val map = HashMap<String, Any>()
            map["postsID"] = model.posts?.postsID ?: 0
            map["like"] = !model.isLiked
            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))

            val result = subscribe(RequestControl("postsLikeOrUnLike", ControlType.CancelThis)) {
                postsWatchServerApi.postsLikeOrUnLike(body)
            }
            if (result.errno == 0) {
                model.isLiked = !model.isLiked
                if (model.isLiked) {
                    // ??????
                    model.numeric?.starCnt = model.numeric?.starCnt?.plus(1)
                } else {
                    // ?????????
                    model.numeric?.starCnt = model.numeric?.starCnt?.minus(1)
                }
                adapter?.update(position, model, PostsWatchViewAdapter.REFRESH_POSTS_LIKE)
            } else {
                if (result.errno == -2) {
                    U.getToastUtil().showShort("??????????????????????????????????????????")
                }
            }
        }
    }

    // ????????????
    fun postsCommentLikeOrUnLike(position: Int, model: PostsWatchModel) {
        launch {
            val map = HashMap<String, Any>()
            map["commentID"] = model.bestComment?.comment?.commentID ?: 0
            map["postsID"] = model.posts?.postsID ?: 0
            map["like"] = (model.bestComment?.isLiked == false)
            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))

            val result = subscribe(RequestControl("postsCommentLikeOrUnLike", ControlType.CancelThis)) {
                postsWatchServerApi.postsCommentLikeOrUnLike(body)
            }
            if (result.errno == 0) {
                model.bestComment?.isLiked = (model.bestComment?.isLiked == false)
                if (model.bestComment?.isLiked == true) {
                    // ????????????
                    model.bestComment?.comment?.likedCnt = model.bestComment?.comment?.likedCnt?.plus(1)
                            ?: 0
                } else {
                    // ???????????????
                    model.bestComment?.comment?.likedCnt = model.bestComment?.comment?.likedCnt?.minus(1)
                            ?: 0
                }
                adapter?.update(position, model, PostsWatchViewAdapter.REFRESH_POSTS_COMMENT_LIKE)
            } else {
                if (result.errno == -2) {
                    U.getToastUtil().showShort("??????????????????????????????????????????")
                }
            }
        }
    }

    fun deletePosts(position: Int, model: PostsWatchModel) {
        launch {
            val map = HashMap<String, Any>()
            map["postsID"] = model.posts?.postsID ?: 0
            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))

            val result = subscribe(RequestControl("deletePosts", ControlType.CancelThis)) {
                postsWatchServerApi.deletePosts(body)
            }
            if (result.errno == 0) {
                if (model == adapter?.mCurrentPlayModel) {
                    // ?????????????????????????????????
                    adapter?.stopPlay()
                    SinglePlayer.stop(playerTag)
                }
                adapter?.deletePosts(model)
            } else {
                if (result.errno == -2) {
                    U.getToastUtil().showShort("??????????????????????????????????????????")
                } else if (result.errno == 8302558) {
                    U.getToastUtil().showShort(result.errmsg)
                }
            }
        }
    }

    // ??????
    fun votePosts(position: Int, model: PostsWatchModel, voteSeq: Int) {
        launch {
            val map = HashMap<String, Any>()
            map["postsID"] = model.posts?.postsID ?: 0
            map["voteID"] = model.posts?.voteInfo?.voteID ?: 0
            map["voteSeq"] = voteSeq
            val body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map))

            val result = subscribe(RequestControl("votePosts", ControlType.CancelThis)) {
                postsWatchServerApi.votePosts(body)
            }
            if (result.errno == 0) {
                U.getToastUtil().showShort("????????????")
                model.posts?.voteInfo?.hasVoted = true
                model.posts?.voteInfo?.voteSeq = voteSeq
                model.posts?.voteInfo?.voteList?.let {
                    if (voteSeq in 1..it.size) {
                        it[voteSeq - 1].voteCnt = it[voteSeq - 1].voteCnt + 1
                    }
                }
                adapter?.update(position, model, PostsWatchViewAdapter.REFRESH_POSTS_VOTE)

            } else {
                if (result.errno == -2) {
                    U.getToastUtil().showShort("??????????????????????????????????????????")
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: PostsDetailEvent) {
        event.model?.let {
            adapter?.updateModelFromDetail(it)
        }
    }

    open fun destory() {
        SinglePlayer.reset(playerTag)
        SinglePlayer.removeCallback(playerTag)
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        cancel()
    }
}