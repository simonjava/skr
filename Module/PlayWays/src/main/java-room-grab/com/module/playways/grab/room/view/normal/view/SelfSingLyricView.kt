package com.module.playways.grab.room.view.normal.view

import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import com.common.log.MyLog
import com.common.utils.SpanUtils
import com.common.view.ExViewStub
import com.component.lyrics.LyricAndAccMatchManager
import com.component.lyrics.LyricsManager
import com.component.lyrics.LyricsReader
import com.component.lyrics.widget.ManyLyricsView
import com.component.lyrics.widget.VoiceScaleView
import com.module.playways.R
import com.module.playways.grab.room.model.GrabRoundInfoModel
import com.module.playways.room.data.H
import com.module.playways.room.song.model.SongModel
import com.zq.live.proto.GrabRoom.EWantSingType
import com.zq.mediaengine.kit.ZqEngineKit
import io.reactivex.disposables.Disposable

open class SelfSingLyricView(viewStub: ViewStub) : ExViewStub(viewStub) {
    val TAG = "SelfSingLyricView"

    protected var mSvlyric: ScrollView? = null
    protected var mTvLyric: TextView? = null
    protected var mManyLyricsView: ManyLyricsView? = null

    internal var mDisposable: Disposable? = null
    internal var mVoiceScaleView: VoiceScaleView? = null

    internal var mIvChallengeIcon: ImageView? = null

    internal var mLyricAndAccMatchManager: LyricAndAccMatchManager? = LyricAndAccMatchManager()

    override fun init(parentView: View) {
        mSvlyric = mParentView!!.findViewById(R.id.sv_lyric)
        mTvLyric = mParentView!!.findViewById(R.id.tv_lyric)
        mManyLyricsView = mParentView!!.findViewById(R.id.many_lyrics_view)
        mVoiceScaleView = mParentView!!.findViewById(R.id.voice_scale_view)
        mIvChallengeIcon = mParentView!!.findViewById(R.id.iv_challenge_icon)
    }

    override fun layoutDesc(): Int {
        return R.layout.grab_self_sing_lyric_layout
    }

    private fun initLyric() {
        mSvlyric?.visibility = View.VISIBLE
        mManyLyricsView?.visibility = View.GONE
        mManyLyricsView?.initLrcData()


        if (mIvChallengeIcon != null) {
            var infoModel: GrabRoundInfoModel? = null
            if (H.isGrabRoom()) {
                infoModel = H.grabRoomData!!.realRoundInfo
            }
            if (infoModel != null && (infoModel.wantSingType == EWantSingType.EWST_COMMON_OVER_TIME.value || infoModel.wantSingType == EWantSingType.EWST_ACCOMPANY_OVER_TIME.value)) {
                mIvChallengeIcon!!.visibility = View.VISIBLE
            } else {
                mIvChallengeIcon!!.visibility = View.GONE
            }
        }
        mVoiceScaleView?.visibility = View.GONE
    }

    fun playWithAcc(songModel: SongModel?,totalTs: Int) {
        tryInflate()
        initLyric()
        if (songModel == null) {
            MyLog.w(TAG, "playWithAcc curSong = null totalTs=$totalTs")
            return
        }
        val configParams = LyricAndAccMatchManager.ConfigParams()
        configParams.manyLyricsView = mManyLyricsView
        configParams.voiceScaleView = mVoiceScaleView
        configParams.lyricUrl = songModel.lyric
        configParams.lyricBeginTs = songModel.standLrcBeginT
        configParams.lyricEndTs = songModel.standLrcBeginT + totalTs
        configParams.accBeginTs = songModel.beginMs
        configParams.accEndTs = songModel.beginMs + totalTs
        configParams.authorName = songModel.uploaderName
        mLyricAndAccMatchManager!!.setArgs(configParams)
        val finalCurSong = songModel
        mLyricAndAccMatchManager!!.start(object : LyricAndAccMatchManager.Listener {

            override fun onLyricParseSuccess(reader: LyricsReader) {
                mSvlyric?.visibility = View.GONE
            }

            override fun onLyricParseFailed() {
                playWithNoAcc(finalCurSong)
            }

            override fun onLyricEventPost(lineNum: Int) {
                if (H.isGrabRoom()) {
                    H.grabRoomData?.songLineNum = lineNum
                }
            }

        })
        ZqEngineKit.getInstance().setRecognizeListener { result, list, targetSongInfo, lineNo -> mLyricAndAccMatchManager!!.onAcrResult(result, list, targetSongInfo, lineNo) }
    }

    fun playWithNoAcc(songModel: SongModel?) {
        if (songModel == null) {
            return
        }
        tryInflate()
        initLyric()
        mManyLyricsView!!.visibility = View.GONE
        mSvlyric?.visibility = View.VISIBLE
        mSvlyric?.scrollTo(0, 0)
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
        mDisposable = LyricsManager
                .loadGrabPlainLyric(songModel.standLrc)
                .subscribe({ s ->
                    val ssb = createLyricSpan(s, songModel)
                    if (ssb == null) {
                        mTvLyric?.text = s
                    } else {
                        mTvLyric?.text = ssb
                    }
                }, { throwable -> MyLog.e(TAG, "accept throwable=$throwable") })
        mLyricAndAccMatchManager!!.stop()
    }

    protected open fun createLyricSpan(lyric: String, songModel: SongModel?): SpannableStringBuilder? {
        return if (songModel != null && !TextUtils.isEmpty(songModel.uploaderName)) {
            SpanUtils()
                    .append(lyric)
                    .append("\n")
                    .append("上传者:" + songModel.uploaderName).setFontSize(12, true)
                    .create()
        } else null
    }

    override fun onViewDetachedFromWindow(v: View) {
        super.onViewDetachedFromWindow(v)
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
        mLyricAndAccMatchManager?.stop()
    }

    fun destroy() {
        mManyLyricsView?.release()
    }

    fun reset() {
        mManyLyricsView?.lyricsReader = null
        mLyricAndAccMatchManager?.stop()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE) {
            reset()
        }
    }
}
