package com.module.posts.detail.view


import android.view.View
import android.view.ViewStub
import android.widget.TextView
import com.common.core.permission.SkrAudioPermission
import com.common.core.view.setDebounceViewClickListener
import com.common.player.SinglePlayer
import com.common.recorder.MyMediaRecorder
import com.common.utils.U
import com.common.view.DebounceViewClickListener
import com.common.view.DiffuseView
import com.common.view.ExViewStub
import com.component.busilib.view.CircleCountDownView
import com.common.view.ex.ExImageView
import com.common.view.ex.ExTextView
import com.module.posts.R
import com.module.posts.publish.PostsPublishModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * 帖子声音录制
 */
class PostsVoiceRecordView(viewStub: ViewStub) : ExViewStub(viewStub) {

    val TAG = "PostsVoiceRecordView"

    val playTag = TAG + hashCode()

    companion object {
        val STATUS_IDLE = 1
        val STATUS_RECORDING = 2
        val STATUS_RECORD_OK = 3
        val STATUS_RECORD_PLAYING = 4
    }

    var status = STATUS_IDLE
    var myMediaRecorder: MyMediaRecorder? = null

    var mResetCall: (() -> Unit)? = null

    lateinit var playBtn: ExImageView
    lateinit var playTipsTv: ExTextView
    lateinit var countDownTv: TextView
    lateinit var abandonIv: ExImageView
    lateinit var abandonTv: ExTextView
    lateinit var okIv: ExImageView
    lateinit var okTv: ExTextView
    lateinit var recordDiffuseView: DiffuseView
    lateinit var circleCountDownView: CircleCountDownView
    var startRecordTs = 0L
    var recordJob: Job? = null
    var recordOkListener: ((String?,Int) -> Unit)? = null
    var okClickListener: ((String?,Int) -> Unit)? = null
    var okToStopPlayListener: (() -> Unit)? = null

    private val skrAudioPermission = SkrAudioPermission()

    override fun init(parentView: View) {
        playBtn = parentView.findViewById(R.id.play_btn)
        playTipsTv = parentView.findViewById(R.id.play_tips_tv)
        countDownTv = parentView.findViewById(R.id.count_down_tv)
        abandonIv = parentView.findViewById(R.id.abandon_iv)
        abandonTv = parentView.findViewById(R.id.abandon_tv)
        okIv = parentView.findViewById(R.id.ok_iv)
        okTv = parentView.findViewById(R.id.ok_tv)
        recordDiffuseView = parentView.findViewById(R.id.record_diffuse_view)
        circleCountDownView = parentView.findViewById(R.id.circle_count_down_view)

        playBtn.setOnClickListener {
            if (status == STATUS_IDLE) {
                skrAudioPermission.ensurePermission({
                    startRecord()
                }, true)
            } else if (status == STATUS_RECORDING) {
                if ((System.currentTimeMillis() - startRecordTs) < 2 * 1000) {
                    U.getToastUtil().showShort("太短了，多录制几句吧")
                    return@setOnClickListener
                }
                stopRecord()
            } else if (status == STATUS_RECORD_OK) {
                // 播放
                play()
            } else if (status == STATUS_RECORD_PLAYING) {
                stop()
            }
        }
        abandonIv.setOnClickListener {
            reset()
        }
        okIv.setOnClickListener(object : DebounceViewClickListener() {
            override fun clickValid(v: View?) {
                okClickListener?.invoke(PostsPublishModel.POSTS_PUBLISH_AUDIO_FILE_PATH,myMediaRecorder?.mDuration?:0)
            }
        })
        parentView.setDebounceViewClickListener {

        }
    }

    override fun layoutDesc(): Int {
        return R.layout.posts_detail_voice_record_view_stub_layout
    }

    private fun startRecord() {
        okToStopPlayListener?.invoke()
        status = STATUS_RECORDING
        playTipsTv.text = "点击停止"
        abandonIv.visibility = View.GONE
        abandonTv.visibility = View.GONE
        okIv.visibility = View.GONE
        okTv.visibility = View.GONE
        countDownTv.visibility = View.VISIBLE
        countDownTv.text = "0s"
        playBtn.setImageResource(R.drawable.yuyin_weikaishi)

        recordJob = launch {
            for (i in 0 until 60) {
                recordDiffuseView.start(2000)
                delay(1000)
                countDownTv.text = "${i + 1}s"
            }
            stopRecord()
        }
        if (myMediaRecorder == null) {
            myMediaRecorder = MyMediaRecorder.newBuilder().build()
        }
        myMediaRecorder?.start(PostsPublishModel.POSTS_PUBLISH_AUDIO_FILE_PATH, null)
        startRecordTs = System.currentTimeMillis()
    }

    private fun stopRecord() {
        status = STATUS_RECORD_OK
        recordDiffuseView.stop()
        circleCountDownView.visibility = View.GONE
        myMediaRecorder?.stop()
        recordJob?.cancel()
        playBtn.setImageResource(R.drawable.yuyin_zanting)
        playTipsTv.text = "播放"
        abandonIv.visibility = View.VISIBLE
        abandonTv.visibility = View.VISIBLE
        okIv.visibility = View.VISIBLE
        okTv.visibility = View.VISIBLE
        recordOkListener?.invoke(PostsPublishModel.POSTS_PUBLISH_AUDIO_FILE_PATH,myMediaRecorder?.mDuration?:0)
    }

    private fun play() {
        okToStopPlayListener?.invoke()
        status = STATUS_RECORD_PLAYING
        playBtn.setImageResource(R.drawable.yuyin_bofang)
        circleCountDownView.visibility = View.VISIBLE
        recordDiffuseView.visibility = View.GONE
        circleCountDownView.go(0, myMediaRecorder?.mDuration ?: 0) {
            stop()
        }
        playTipsTv.text = "暂停"
        recordJob = launch {
            for (i in 0 until 60) {
                delay(1000)
                countDownTv.text = "${i + 1}s"
            }
            stop()
        }
        SinglePlayer.startPlay(playTag, PostsPublishModel.POSTS_PUBLISH_AUDIO_FILE_PATH)
    }

    fun stop() {
        SinglePlayer.stop(playTag)
        stopRecord()
    }

    fun reset() {
        if(mParentView==null){
            return
        }
        stop()
        mResetCall?.invoke()
        status = STATUS_IDLE
        playBtn.setImageResource(R.drawable.yuyin_weikaishi)
        playTipsTv.text = "点击录音"
        abandonIv.visibility = View.GONE
        abandonTv.visibility = View.GONE
        okIv.visibility = View.GONE
        okTv.visibility = View.GONE
        countDownTv.visibility = View.GONE
    }

}

