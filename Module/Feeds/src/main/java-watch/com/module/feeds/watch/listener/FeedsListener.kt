package com.module.feeds.watch.listener

import com.module.feeds.watch.model.FeedRecommendTagModel
import com.module.feeds.watch.model.FeedsWatchModel

interface FeedsListener {
    fun onClickMoreListener(position: Int, watchModel: FeedsWatchModel?)

    fun onClickLikeListener(position: Int, watchModel: FeedsWatchModel?)

    fun onClickHitListener(watchModel: FeedsWatchModel?)

    fun onClickDetailListener(position: Int, watchModel: FeedsWatchModel?)

    fun onClickCDListener(position: Int, watchModel: FeedsWatchModel?)

    fun onclickRankListener(watchModel: FeedsWatchModel?)

    fun onClickAvatarListener(watchModel: FeedsWatchModel?)

    fun onClickShareListener(position: Int, watchModel: FeedsWatchModel?)

    fun onClickCollectListener(position: Int, watchModel: FeedsWatchModel?)

    fun onClickRecommendTag(model: FeedRecommendTagModel)

    fun onClickRecommendTagMore()

    fun onClickNameListener(userID: Int)
}