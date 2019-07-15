package com.common.recorder

import android.media.MediaRecorder
import android.os.Handler
import android.os.Looper
import android.os.Message

import com.common.log.MyLog

import java.util.function.Function

class MyMediaRecorder {

    //    String filePath;
    val MSG_UPDATE_VOLUME = 81

    var audioSource = MediaRecorder.AudioSource.MIC
    var outputFormat = MediaRecorder.OutputFormat.MPEG_4
    var audioEncoder = MediaRecorder.AudioEncoder.AAC
    var audioChannel = 1
    var audioSamplingRate = 44100
    var audioEncodingBitRate = 192000
    private var mStartRecordingTs: Long = 0
    /**
     * 单位ms
     *
     * @return
     */
    var duration = 0
        internal set
    private var mMediaRecorder: MediaRecorder? = null
    private var mRecording = false
    private var mUiHandler: Handler? = null
    private var mVolumeListener: ((Int) -> Unit)? = null

    private fun config(filePath: String) {
        if (mMediaRecorder == null) {
            mMediaRecorder = MediaRecorder()
        }
        mMediaRecorder?.setAudioSource(audioSource)
        mMediaRecorder?.setOutputFormat(outputFormat)
        mMediaRecorder?.setOutputFile(filePath)
        mMediaRecorder?.setAudioEncoder(audioEncoder)
        mMediaRecorder?.setAudioChannels(audioChannel)
        mMediaRecorder?.setAudioSamplingRate(audioSamplingRate)
        mMediaRecorder?.setAudioEncodingBitRate(audioEncodingBitRate)
    }

    fun start(filePath: String, callback: ((Int) -> Unit)?) {
        config(filePath)
        try {
            if (mRecording) {
                mMediaRecorder?.reset()
            }
            mMediaRecorder?.prepare()
            mMediaRecorder?.start()
            mStartRecordingTs = System.currentTimeMillis()
            mRecording = true
            mVolumeListener = callback
            if (callback != null) {
                updateVolume()
            }
        } catch (e: Exception) {
            MyLog.e(TAG, e)
        }
    }

    fun stop() {
        if (mRecording) {
            duration = (System.currentTimeMillis() - mStartRecordingTs).toInt()
        }
        mRecording = false
        mMediaRecorder?.reset()
        mUiHandler?.removeCallbacksAndMessages(null)
    }

    fun destroy() {
        mRecording = false
        mMediaRecorder?.reset()
        mMediaRecorder?.release()
        mMediaRecorder = null
        mUiHandler?.removeCallbacksAndMessages(null)
        mUiHandler = null
    }

    private fun updateVolume() {
        if (mMediaRecorder != null) {
            val ratio = mMediaRecorder!!.maxAmplitude / 600
            var db = 0// 分贝
            if (ratio > 1) {
                db = (20 * Math.log10(ratio.toDouble())).toInt()
                if (mVolumeListener != null) {
                    mVolumeListener?.invoke(db)
                    getHandler()?.removeMessages(MSG_UPDATE_VOLUME)
                    getHandler()?.sendEmptyMessageDelayed(MSG_UPDATE_VOLUME, 1000)
                }
            }
        }
    }

    private fun getHandler(): Handler? {
        if (mUiHandler == null) {
            mUiHandler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {}
            }
        }
        return mUiHandler;
    }

    class Builder internal constructor() {
        internal var mParams = MyMediaRecorder()

        fun setAudioSource(audioSource: Int): Builder {
            mParams.audioSource = audioSource
            return this
        }

        fun setOutputFormat(outputFormat: Int): Builder {
            mParams.outputFormat = outputFormat
            return this
        }

        fun setAudioEncoder(audioEncoder: Int): Builder {
            mParams.audioEncoder = audioEncoder
            return this
        }

        fun setAudioChannel(audioChannel: Int): Builder {
            mParams.audioChannel = audioChannel
            return this
        }

        fun setAudioSamplingRate(audioSamplingRate: Int): Builder {
            mParams.audioSamplingRate = audioSamplingRate
            return this
        }

        fun setAudioEncodingBitRate(audioEncodingBitRate: Int): Builder {
            mParams.audioEncodingBitRate = audioEncodingBitRate
            return this
        }

        fun build(): MyMediaRecorder {
            return mParams
        }

    }

    companion object {
        val TAG = "MyMediaRecorder"

        fun newBuilder(): Builder {
            return Builder()
        }
    }

}
