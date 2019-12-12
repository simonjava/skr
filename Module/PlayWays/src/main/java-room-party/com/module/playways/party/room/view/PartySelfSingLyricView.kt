package com.module.playways.party.room.view

import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.View
import android.view.ViewStub
import com.common.core.myinfo.MyUserInfoManager
import com.common.log.MyLog
import com.common.utils.SpanUtils
import com.common.view.ExViewStub
import com.component.lyrics.LyricAndAccMatchManager
import com.component.lyrics.LyricsReader
import com.component.lyrics.utils.SongResUtils
import com.component.lyrics.widget.ManyLyricsView
import com.module.playways.R
import com.module.playways.grab.room.view.SingCountDownView2
import com.module.playways.party.room.PartyRoomData
import com.module.playways.party.room.model.PartyRoundInfoModel
import com.module.playways.room.song.model.SongModel
import com.zq.mediaengine.kit.ZqEngineKit
import io.reactivex.disposables.Disposable

class PartySelfSingLyricView(viewStub: ViewStub, protected var mRoomData: PartyRoomData?) : ExViewStub(viewStub) {
    val TAG = "RaceSelfSingLyricView"

    protected lateinit var mManyLyricsView: ManyLyricsView
    internal lateinit var mSingCountDownView2: SingCountDownView2

    internal var mDisposable: Disposable? = null
    internal var mSongModel: SongModel? = null
    internal var mLyricAndAccMatchManager: LyricAndAccMatchManager? = LyricAndAccMatchManager()

    override fun init(parentView: View) {
        parentView?.let {
            mManyLyricsView = it.findViewById(R.id.many_lyrics_view)
            mSingCountDownView2 = it.findViewById(R.id.sing_count_down_view)
        }
    }

    override fun layoutDesc(): Int {
        return R.layout.party_self_sing_lyric_layout
    }

    private fun initLyric() {
        mManyLyricsView?.visibility = View.GONE
        mManyLyricsView?.initLrcData()
    }

    fun startFly(isSelf: Boolean, call: (() -> Unit)?) {
        tryInflate()
        val infoModel = mRoomData?.realRoundInfo
        val totalMs = infoModel?.endMs ?: 0 - (infoModel?.beginMs ?: 0)
        mSingCountDownView2.startPlay(0, totalMs, true)
        mSingCountDownView2.setListener {
            call?.invoke()
        }

        playWithAcc(isSelf, infoModel, totalMs)
    }

    private fun playWithAcc(isSelf: Boolean, infoModel: PartyRoundInfoModel?, totalTs: Int) {
        if (infoModel == null) {
            MyLog.w(TAG, "playWithAcc infoModel = null totalTs=$totalTs")
            return
        }

        initLyric()
        mSongModel = infoModel.sceneInfo?.ktv?.music
        var curSong = mSongModel
        if (curSong == null) {
            MyLog.w(TAG, "playWithAcc curSong = null totalTs=$totalTs")
            return
        }
        mManyLyricsView.setEnableVerbatim(isSelf)
        val configParams = LyricAndAccMatchManager.ConfigParams()
        configParams.manyLyricsView = mManyLyricsView
        configParams.lyricUrl = curSong.lyric
        configParams.lyricBeginTs = curSong.standLrcBeginT
        configParams.lyricEndTs = curSong.standLrcBeginT + totalTs
        configParams.accBeginTs = curSong.beginMs
        configParams.accEndTs = curSong.beginMs + totalTs
        configParams.authorName = curSong.uploaderName
        mLyricAndAccMatchManager!!.setArgs(configParams)
        val finalCurSong = curSong
        mLyricAndAccMatchManager!!.start(object : LyricAndAccMatchManager.Listener {

            override fun onLyricParseSuccess(reader: LyricsReader) {
//                mSvlyric.visibility = View.GONE
            }

            override fun onLyricParseFailed() {
//                playWithNoAcc(finalCurSong)
            }

            override fun onLyricEventPost(lineNum: Int) {
                //                mRoomData.setSongLineNum(lineNum);
            }

        })

        // 开始开始混伴奏，开始解除引擎mute
        val accFile = SongResUtils.getAccFileByUrl(mSongModel?.acc)
        // midi不需要在这下，只要下好，native就会解析，打分就能恢复
        val midiFile = SongResUtils.getMIDIFileByUrl(mSongModel?.midi)

        val songBeginTs = mSongModel?.beginMs ?: 0
        if (accFile != null && accFile.exists()) {
            // 伴奏文件存在
            ZqEngineKit.getInstance().startAudioMixing(MyUserInfoManager.uid.toInt(), accFile.absolutePath, midiFile.absolutePath, songBeginTs.toLong(), false, false, 1)
        } else {
            ZqEngineKit.getInstance().startAudioMixing(MyUserInfoManager.uid.toInt(), mSongModel?.acc, midiFile.absolutePath, songBeginTs.toLong(), false, false, 1)
        }

        ZqEngineKit.getInstance().setRecognizeListener { result, list, targetSongInfo, lineNo -> mLyricAndAccMatchManager!!.onAcrResult(result, list, targetSongInfo, lineNo) }
    }

    protected fun createLyricSpan(lyric: String, songModel: SongModel?): SpannableStringBuilder? {
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
        if (mLyricAndAccMatchManager != null) {
            mLyricAndAccMatchManager!!.stop()
        }
    }

    fun destroy() {
        if (mManyLyricsView != null) {
            mManyLyricsView!!.release()
        }
    }

    fun reset() {
        if (mParentView != null) {
            MyLog.d(TAG, "reset")
            mManyLyricsView?.lyricsReader = null
            mLyricAndAccMatchManager?.stop()
            mSingCountDownView2?.reset()
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE) {
            reset()
        }
    }
}
