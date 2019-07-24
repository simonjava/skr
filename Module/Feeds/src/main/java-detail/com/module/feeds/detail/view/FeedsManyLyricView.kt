package com.module.feeds.detail.view

import android.view.View
import android.view.ViewStub
import com.common.log.MyLog
import com.common.rx.RxRetryAssist
import com.common.utils.U
import com.common.view.ExViewStub
import com.component.lyrics.LyricsManager
import com.component.lyrics.widget.AbstractLrcView
import com.component.lyrics.widget.AbstractLrcView.LRCPLAYERSTATUS_PLAY
import com.module.feeds.R
import com.module.feeds.detail.view.inter.BaseFeedsLyricView
import com.component.lyrics.widget.ManyLyricsView

import com.module.feeds.watch.model.FeedSongModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


class FeedsManyLyricView(viewStub: ViewStub) : ExViewStub(viewStub), BaseFeedsLyricView {
    val TAG = "FeedsManyLyricView"
    lateinit var mManyLyricsView: ManyLyricsView
    private var mFeedSongModel: FeedSongModel? = null
    var mDisposable: Disposable? = null
    var mIsStart: Boolean = false

    override fun init(parentView: View) {
        mManyLyricsView = mParentView.findViewById(R.id.many_lyrics_view)
        mManyLyricsView.spaceLineHeight = U.getDisplayUtils().dip2px(15f).toFloat()
    }

    override fun layoutDesc(): Int {
        return R.layout.feeds_many_lyric_layout
    }

    override fun setSongModel(feedSongModel: FeedSongModel) {
        mFeedSongModel = feedSongModel
        tryInflate()
        showLyric(false)
    }

    override fun playLyric() {
        showLyric(true)
    }

    private fun showLyric(play: Boolean) {
        mFeedSongModel?.songTpl?.lrcTs?.let {
            mDisposable?.dispose()
            mDisposable = LyricsManager.getLyricsManager(U.app())
                    .fetchAndLoadLyrics(mFeedSongModel!!.songTpl!!.lrcTs)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(RxRetryAssist(3, ""))
                    .subscribe(Consumer { lyricsReader ->
                        MyLog.w(TAG, "onEventMainThread " + "play")
                        if (mManyLyricsView != null) {
                            mManyLyricsView.setVisibility(View.VISIBLE)
                            mManyLyricsView.initLrcData()
                        }

                        mManyLyricsView.setLyricsReader(lyricsReader)

                        if (play) {
                            mIsStart = true
                            mManyLyricsView.play(0)
                        } else {
                            if (mManyLyricsView.getLrcStatus() == AbstractLrcView.LRCSTATUS_LRC && mManyLyricsView.getLrcPlayerStatus() != LRCPLAYERSTATUS_PLAY) {
                                mManyLyricsView.pause()
                            }
                        }
                    })
        }
    }

    override fun seekTo(duration: Int) {
        mManyLyricsView?.seekto(duration)
    }

    override fun pause() {
        mManyLyricsView?.pause()
    }

    override fun resume() {
        mManyLyricsView?.resume()
    }

    override fun isStart(): Boolean = mIsStart

    override fun stop() {
        mManyLyricsView.seekto(0)
        mManyLyricsView?.pause()
        mIsStart = false
    }

    override fun onViewDetachedFromWindow(v: View) {
        super.onViewDetachedFromWindow(v)

    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE) {

        }
    }
}
