package com.module.feeds.detail.view.inter

import com.component.feeds.model.FeedSongModel

interface BaseFeedsLyricView {
    fun setSongModel(mFeedSongModel: FeedSongModel)
    fun playLyric()
    fun seekTo(duration: Int)
    fun pause()
    fun resume()
    fun stop()
    fun isStart(): Boolean
    fun destroy()
}