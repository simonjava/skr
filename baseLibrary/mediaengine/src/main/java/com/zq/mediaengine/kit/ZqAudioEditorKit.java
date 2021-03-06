package com.zq.mediaengine.kit;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.engine.Params;
import com.zq.mediaengine.capture.AudioFileCapture;
import com.zq.mediaengine.encoder.MediaCodecAudioEncoder;
import com.zq.mediaengine.filter.audio.AudioFilterBase;
import com.zq.mediaengine.filter.audio.AudioFilterMgt;
import com.zq.mediaengine.filter.audio.AudioLevelMeterFilter;
import com.zq.mediaengine.filter.audio.AudioMixer;
import com.zq.mediaengine.filter.audio.AudioPreview;
import com.zq.mediaengine.filter.audio.AudioResampleFilter;
import com.zq.mediaengine.filter.audio.IPcmPlayer;
import com.zq.mediaengine.framework.AVConst;
import com.zq.mediaengine.framework.AudioBufFormat;
import com.zq.mediaengine.framework.AudioBufFrame;
import com.zq.mediaengine.framework.AudioCodecFormat;
import com.zq.mediaengine.framework.SrcPin;
import com.zq.mediaengine.kit.filter.CbAudioEffectFilter;
import com.zq.mediaengine.kit.filter.TbAudioEffectFilter;
import com.zq.mediaengine.publisher.MediaMuxerPublisher;
import com.zq.mediaengine.publisher.Publisher;

/**
 * 音频编辑、合成实现类。
 *
 * 实现人声和伴奏的对齐，以及音效、音量的调整。
 */

public class ZqAudioEditorKit {
    public static final String TAG = "ZqAudioEditorKit";

    private static final int MAX_CHN = 8;

    public static final int STATE_IDLE = 0;
    public static final int STATE_PREVIEW_PREPARING = 1;
    public static final int STATE_PREVIEW_STARTED = 2;
    public static final int STATE_PREVIEW_PAUSED = 3;
    public static final int STATE_COMPOSING = 4;

    /**
     * The constant ERROR_UNKNOWN.
     */
    public static final int ERROR_UNKNOWN = -1;
    /**
     * The constant ERROR_IO.
     */
    public static final int ERROR_IO = -2;
    /**
     * The constant ERROR_UNSUPPORTED.
     */
    public static final int ERROR_UNSUPPORTED = -3;

    private Context mContext;
    private AudioMixer mAudioMixer;
    private IPcmPlayer mPcmPlayer;
    private AudioPreview mAudioPreview;
    private MediaCodecAudioEncoder mAudioEncoder;
    private MediaMuxerPublisher mPublisher;

    private int mState;
    private AudioSource[] mAudioSource;
    private int mLoopCount;
    private int mLoopedCount;
    private String mComposePath;

    // 电平归一化
    private boolean mEnableAutoComposeLevel;
    private float mAutoComposeLevelDB;
    private boolean mIsAudioLevelPassing;
    private long mPassingStartTime;
    private long mComposeStartTime;
    private AudioLevelMeterFilter mAudioLevelMeterFilter;

    private OnPreviewInfoListener mOnPreviewInfoListener;
    private OnComposeInfoListener mOnComposeInfoListener;
    private OnErrorListener mOnErrorListener;

    /**
     * 音频合成预览的信息回调接口
     */
    public interface OnPreviewInfoListener {
        /**
         * 音频预览开始
         */
        void onStarted();

        /**
         * 音频预览结束
         */
        void onCompletion();

        /**
         * 音频预览循环播放模式下，每次重新播放会触发该回调
         *
         * @param count 当前是第几次loop，从1开始
         */
        void onLoopCount(int count);
    }

    /**
     * 音频合成处理的信息回调接口
     */
    public interface OnComposeInfoListener {
        /**
         * 当前的合成进度回调
         *
         * @param progress 当前的进度，取值在[0, 1]范围内
         */
        void onProgress(float progress, boolean isAudioLevelPassing);

        /**
         * 如果开启了合成时的电平归一化，在评估完成后触发该回调
         */
        void onAudioLevelPassCompletion();

        /**
         * 音频合成完成回调
         */
        void onCompletion();
    }

    /**
     * 错误回调接口。
     */
    public interface OnErrorListener {
        void onError(int what, int msg1, int msg2);
    }

    public ZqAudioEditorKit(Context context) {
        mState = STATE_IDLE;
        mContext = context;
        mAudioSource = new AudioSource[MAX_CHN];

        mAudioMixer = new AudioMixer();
        mAudioMixer.setBlockingMode(true);
//        mPcmPlayer = new AudioTrackPlayer();
        // TODO: 时间戳不准，用mPcmPlayer代替
        mAudioPreview = new AudioPreview(context);
        mAudioPreview.setBlockingMode(true);
        mAudioMixer.getSrcPin().connect(mAudioPreview.getSinkPin());

        // 默认开启合成时的自动增益控制
        mEnableAutoComposeLevel = false;
        mAutoComposeLevelDB = -3;
        mAudioLevelMeterFilter = new AudioLevelMeterFilter();
        mAudioLevelMeterFilter.setTag("compose");
        mAudioMixer.getSrcPin().connect(mAudioLevelMeterFilter.getSinkPin());

        // 合成模块
        mAudioEncoder = new MediaCodecAudioEncoder();
        mAudioEncoder.setAutoWork(true);
        mAudioEncoder.setUseSyncMode(true);
        mPublisher = new MediaMuxerPublisher();
        mPublisher.setAudioOnly(true);
        mPublisher.setPubListener(mPubListener);
        // 开始合成时再连接encoder
        mAudioEncoder.getSrcPin().connect(mPublisher.getAudioSink());
    }

    public void setOnPreviewInfoListener(OnPreviewInfoListener listener) {
        Log.d(TAG, "setOnPreviewInfoListener: " + listener);
        mOnPreviewInfoListener = listener;
    }

    public void setOnComposeInfoListener(OnComposeInfoListener listener) {
        Log.d(TAG, "setOnComposeInfoListener: " + listener);
        mOnComposeInfoListener = listener;
    }

    public void setOnErrorListener(OnErrorListener listener) {
        Log.d(TAG, "setOnComposeInfoListener: " + listener);
        mOnErrorListener = listener;
    }

    /**
     * 重置当前实例，调用后当前实例的状态恢复到刚创建时的样子，不过设置的回调会保留。
     *
     * 如果在合成过程中，则合成操作立即中断，中间文件不会被删除，需要应用层自行处理。
     */
    public synchronized void reset() {
        for (int i = 0; i < MAX_CHN; i++) {
            if (mAudioSource[i] != null) {
                mAudioSource[i].capture.getSrcPin().disconnect(false);
                mAudioSource[i].release();
                mAudioSource[i] = null;
            }
        }
        mState = STATE_IDLE;
    }

    /**
     * 设置需要编辑合成的音频文件路径，并配置相应音频文件的实际区域。
     *
     * idx为0的音频文件会作为基准，其有效长度会作为输出文件的长度，一般设置为伴奏的地址。
     * 注意均需要设置为本地文件地址，网络地址目前不能很好的支持。
     *
     * @param idx       音频文件索引，最大支持8路
     * @param path      音频文件的绝对路径
     * @param offset    当前音频的实际开始位置，单位为ms, 小于0时代表前面补充对应时长的静音数据，大于音频长度时无效。
     * @param end       当前音频的实际结束位置，单位为ms, 大于音频长度，或者小于0时，按实际长度计算。
     */
    public synchronized void setDataSource(int idx, String path, long offset, long end) {
        Log.d(TAG, "setDataSource idx: " + idx + " path: " + path + " off: " + offset + " end: " + end);
        if (idx < 0 || idx >= MAX_CHN) {
            return;
        }
        if (TextUtils.isEmpty(path)) {
            return;
        }
        mAudioSource[idx] = new AudioSource(mContext, path, offset, end);
        mAudioSource[idx].capture.setOnPreparedListener(mOnCapturePreparedListener);
        mAudioSource[idx].capture.setOnSeekCompletionListener(mOnCaptureSeekCompletionListener);
        mAudioSource[idx].capture.setOnPositionUpdateListener(mOnCapturePositionUpdateListener, 100);
        mAudioSource[idx].capture.setOnCompletionListener(mOnCaptureCompletionListener);
        mAudioSource[idx].capture.setOnErrorListener(mOnCaptureErrorListener);
        mAudioSource[idx].getSrcPin().connect(mAudioMixer.getSinkPin(idx));
        if (offset < 0) {
            Log.d(TAG, "preset idx " + idx + " delay: " + offset);
            mAudioMixer.setDelay(idx, offset);
        }
    }

    /**
     * 开始或恢复音频合成预览。
     *
     * 调用该接口前，需要设置好合成预览的各路音频文件路径。
     * idx为0的路径必须设置，不然该接口不会工作。
     *
     * @param loopCount 循环次数，<0表示无限循环。
     */
    public synchronized void startPreview(int loopCount) {
        Log.d(TAG, "startPreview loopCount: " + loopCount);
        if (loopCount == 0) {
            return;
        }
        if (mAudioSource[0] == null) {
            Log.e(TAG, "idx 0 audio data source not set, return");
            return;
        }
        if (mState != STATE_IDLE) {
            return;
        }
        mState = STATE_PREVIEW_PREPARING;
        // 预览时断开encoder的连接
        mAudioMixer.getSrcPin().disconnect(mAudioEncoder.getSinkPin(), false);

        mLoopCount = loopCount;
        mLoopedCount = 0;
        doStart();
    }

    private void doStart() {
        Log.d(TAG, "doStart");
        // 在各路音轨均prepared的情况下，同时开始
        for (int i = 0; i < MAX_CHN; i++) {
            if (mAudioSource[i] != null) {
                mAudioSource[i].isPrepared = false;
            }
        }
        for (int i = 0; i < MAX_CHN; i++) {
            if (mAudioSource[i] != null) {
                mAudioSource[i].capture.prepareAsync();
            }
        }
    }

    /**
     * 暂停音频合成预览。
     */
    public synchronized void pausePreview() {
        Log.d(TAG, "pausePreview");
        if (mAudioSource[0] == null) {
            Log.e(TAG, "idx 0 audio data source not set, return");
            return;
        }
        if (mState != STATE_PREVIEW_STARTED && mState != STATE_PREVIEW_PREPARING) {
            return;
        }
        for (int i = 0; i < MAX_CHN; i++) {
            if (mAudioSource[i] != null) {
                Runnable runnable = (i == 0) ? new Runnable() {
                    @Override
                    public void run() {
                        mAudioPreview.pause();
                    }
                } : null;
                mAudioSource[i].capture.pause(runnable);
            }
        }
        mState = STATE_PREVIEW_PAUSED;
    }

    /**
     * 恢复音频合成预览。
     */
    public synchronized void resumePreview() {
        Log.d(TAG, "resumePreview");
        if (mAudioSource[0] == null) {
            Log.e(TAG, "idx 0 audio data source not set, return");
            return;
        }
        if (mState != STATE_PREVIEW_PAUSED) {
            return;
        }
        for (int i = 0; i < MAX_CHN; i++) {
            if (mAudioSource[i] != null) {
                Runnable runnable = (i == 0) ? new Runnable() {
                    @Override
                    public void run() {
                        mAudioPreview.resume();
                    }
                } : null;
                mAudioSource[i].capture.start(runnable);
            }
        }
        mState = STATE_PREVIEW_STARTED;
    }

    /**
     * 停止音频合成预览。
     */
    public synchronized void stopPreview() {
        Log.d(TAG, "stopPreview");
        if (mAudioSource[0] == null) {
            Log.e(TAG, "idx 0 audio data source not set, return");
            return;
        }
        if (mState != STATE_PREVIEW_STARTED &&
                mState != STATE_PREVIEW_PAUSED &&
                mState != STATE_PREVIEW_PREPARING) {
            return;
        }
        // 主音轨最后stop
        for (int i = 1; i < MAX_CHN; i++) {
            if (mAudioSource[i] != null) {
                mAudioSource[i].capture.stop();
            }
        }
        if (mAudioSource[0] != null) {
            mAudioSource[0].capture.stop();
        }
        mAudioPreview.stop();
        mState = STATE_IDLE;
    }

    public synchronized int getState() {
        return mState;
    }

    /**
     * 获取主音轨的时长信息。
     *
     * 需要startPreview后，收到onPreviewStarted回调后获取方才有效。
     * 可以在onPreviewStarted回调中获取并保存该值。
     *
     * @return  音频时长或0, 单位为ms
     */
    public synchronized long getDuration() {
        if (mAudioSource[0] == null) {
            Log.e(TAG, "idx 0 audio data source not set, return");
            return 0;
        }
        return mAudioSource[0].capture.getDuration();
    }

    /**
     * 获取主音轨在预览时的位置信息。
     *
     * @return  当前播放的位置，单位为ms
     */
    public synchronized long getPosition() {
        if (mAudioSource[0] == null) {
            Log.e(TAG, "idx 0 audio data source not set, return");
            return 0;
        }
        // TODO: 不准确
        long pos = mAudioSource[0].capture.getPosition();
        Log.d(TAG, "getPosition: " + pos);
        return pos;
    }

    /**
     * 对主音轨进行seek操作，其他音轨也会自动seek。
     *
     * @param pos 需要seek的目标位置，如果配置了offset, 则实际seek位置为offset+pos, 单位ms
     */
    public synchronized void seekTo(long pos) {
        Log.d(TAG, "seekTo: " + pos);
        if (mAudioSource[0] == null) {
            Log.e(TAG, "idx 0 audio data source not set, return");
            return;
        }
        if (mState != STATE_PREVIEW_STARTED &&
                mState != STATE_PREVIEW_PAUSED &&
                mState != STATE_PREVIEW_PREPARING) {
            return;
        }
        doSeekTo(pos);
    }

    private void doSeekTo(long pos) {
        Log.d(TAG, "doSeekTo " + pos);
        for (int i = 0; i < MAX_CHN; i++) {
            if (mAudioSource[i] != null) {
                mAudioSource[i].isSeekCompletion = false;
            }
        }
        for (int i = 0; i < MAX_CHN; i++) {
            if (mAudioSource[i] != null) {
                // 暂停后seek, 保障各路音轨同时开始
                mAudioSource[i].capture.pause();
                mAudioSource[i].capture.seek(pos);
            }
        }
    }

    /**
     * 设置各路音轨的音量。
     *
     * 可以在预览开始前后设置，预览开始后设置会实时生效，合成开始后设置无效。
     *
     * @param idx   待设置的音轨索引
     * @param vol   音量大小，一般在[0, 1]之间，大于1可以放大声音，但可能会出现爆音。
     */
    public void setInputVolume(int idx, float vol) {
        Log.d(TAG, "setInputVolume idx: " + idx + " vol: " + vol);
        mAudioMixer.setInputVolume(idx, vol);
    }

    /**
     * 获取当前指定音轨的音量。
     *
     * @param idx   音轨索引
     * @return  音量大小。
     */
    public float getInputVolume(int idx) {
        return mAudioMixer.getInputVolume(idx);
    }

    /**
     * 设置最终合成输出的音频音量大小。
     *
     * @param vol   音量大小，一般在[0, 1]之间，大于1可以放大声音，但可能会出现爆音。
     */
    public void setOutputVolume(float vol) {
        Log.d(TAG, "setOutputVolume vol: " + vol);
        mAudioMixer.setOutputVolume(vol);
    }

    /**
     * 获取最终合成输出的音量大小。
     *
     * @return  当前的输出音量大小。
     */
    public float getOutputVolume() {
        return mAudioMixer.getOutputVolume();
    }

    /**
     * 设置指定音轨的延迟信息。
     *
     * 必须在setDataSource之后设置，预览开始后设置会实时生效，合成开始后设置无效。
     *
     * @param idx       待设置的音轨序号
     * @param delayInMs 延迟大小，单位为毫秒。小于0表示前移，大于0表示后移。
     *                  注意，idx为0时设置该值无效。
     */
    public void setDelay(int idx, long delayInMs) {
        Log.d(TAG, "setDelay idx: " + idx + " delay: " + delayInMs);
        if (mAudioSource[idx] == null) {
            Log.e(TAG, "Index " + idx + " has no DataSource set, return");
            return;
        }
        mAudioMixer.setDelay(idx, delayInMs + mAudioSource[idx].delayBase);
    }

    /**
     * 获取指定音轨的延迟信息。
     *
     * @param idx   音轨索引
     * @return  延迟大小，单位为毫秒。
     */
    public long getDelay(int idx) {
        if (mAudioSource[idx] == null) {
            return 0;
        }
        return mAudioMixer.getDelay(idx) - mAudioSource[idx].delayBase;
    }

    /**
     * 设置指定音轨的音效。
     * 需要在设置相应index的DataSource之后调用。
     *
     * @param idx   音轨索引
     * @param type  音效类型
     */
    public synchronized void setAudioEffect(int idx, int type) {
        Log.d(TAG, "setAudioEffect idx: " + idx + " type: " + type);
        if (mAudioSource[idx] == null) {
            Log.e(TAG, "Index " + idx + " has no DataSource set, return");
            return;
        }
        mAudioSource[idx].setAudioEffect(type);
    }

    /**
     * 获取指定音轨的音效。
     *
     * @param idx   音轨索引
     * @return  音效类型。
     */
    public synchronized int getAudioEffect(int idx) {
        if (mAudioSource[idx] == null) {
            Log.e(TAG, "Index " + idx + " has no DataSource set, return");
            return Params.AudioEffect.none.ordinal();
        }
        return mAudioSource[idx].effectType;
    }

    /**
     * 设置合成后的输出目标文件路径。
     *
     * @param path  输出路径。
     */
    public void setOutputPath(String path) {
        Log.d(TAG, "setOutputPath path: " + path);
        mComposePath = path;
    }

    /**
     * 获取合成后的输出目标文件路径。
     *
     * @return  输出路径。
     */
    public String getOutputPath() {
        return mComposePath;
    }

    /**
     * 设置是否开启合成前的自动音量功能，开启后合成后的音频电平值被调整为-3dB
     *
     * @param enable enable
     */
    public void setEnableAutoComposeLevel(boolean enable) {
        mEnableAutoComposeLevel = enable;
    }

    public boolean isEnableAutoComposeLevel() {
        return mEnableAutoComposeLevel;
    }

    public void setAutoComposeLevelDB(float db) {
        mAutoComposeLevelDB = db;
    }

    public float getAutoComposeLevelDB() {
        return mAutoComposeLevelDB;
    }

    /**
     * 开始合成。
     */
    public synchronized void startCompose() {
        Log.d(TAG, "startCompose");
        if (mAudioSource[0] == null) {
            Log.e(TAG, "idx 0 audio data source not set, return");
            return;
        }
        if (TextUtils.isEmpty(mComposePath)) {
            Log.e(TAG, "output path is empty, return");
            return;
        }
        stopPreview();
        if (mState != STATE_IDLE) {
            return;
        }

        mState = STATE_COMPOSING;

        // 开启电平归一化时，先计算当前的电平
        if (mEnableAutoComposeLevel) {
            mIsAudioLevelPassing = true;
            mAudioLevelMeterFilter.setEnableLevelMeter(0, true);
            mAudioLevelMeterFilter.updateMeter(0);
            mPassingStartTime = System.nanoTime() / 1000 / 1000;
            doStart();
        } else {
            doStartCompose(1.0f);
        }
    }

    private void doStartCompose(float outVol) {
        // AutoWork模式下，采样率和声道数会根据实际参数配置
        AudioCodecFormat audioCodecFormat =
                new AudioCodecFormat(AVConst.CODEC_ID_AAC,
                        AVConst.AV_SAMPLE_FMT_S16,
                        44100,
                        2,
                        128000);
        mAudioEncoder.configure(audioCodecFormat);
        // 开始合成时再连接encoder
        mAudioMixer.getSrcPin().connect(mAudioEncoder.getSinkPin());
        // 设置归一化音量
        mAudioMixer.setOutputVolume(outVol);

        mComposeStartTime = System.nanoTime() / 1000 / 1000;
        mPublisher.start(mComposePath);
        doStart();
    }

    /**
     * 中断合成
     */
    public synchronized void abortCompose() {
        Log.d(TAG, "abortCompose");
        if (mAudioSource[0] == null) {
            Log.e(TAG, "idx 0 audio data source not set, return");
            return;
        }
        if (mState != STATE_COMPOSING) {
            Log.e(TAG, "composing not started, return");
            return;
        }
        for (int i = 0; i < MAX_CHN; i++) {
            if (mAudioSource[i] != null) {
                mAudioSource[i].capture.stop();
            }
        }
        mAudioEncoder.stop();
        mState = STATE_IDLE;
    }

    /**
     * 释放当前实例，释放后不能再访问该实例。
     */
    public synchronized void release() {
        Log.d(TAG, "release");
        mAudioMixer.getSrcPin().disconnect(false);
        mAudioEncoder.release();
        mAudioPreview.release();

        // 如果有连接Mixer, 主idx的AudioSource需要最后release
        for (int i = 1; i < MAX_CHN; i++) {
            if (mAudioSource[i] != null) {
                mAudioSource[i].release();
                mAudioSource[i] = null;
            }
        }
        if (mAudioSource[0] != null) {
            mAudioSource[0].release();
            mAudioSource[0] = null;
        }
        mState = STATE_IDLE;
    }

    private AudioFileCapture.OnPreparedListener mOnCapturePreparedListener = new AudioFileCapture.OnPreparedListener() {
        @Override
        public void onPrepared(AudioFileCapture audioFileCapture) {
            Log.d(TAG, "onPrepared: " + audioFileCapture);
            synchronized (ZqAudioEditorKit.this) {
                // 等待所有音轨都prepared之后再开始
                if (mState == STATE_PREVIEW_PREPARING || mState == STATE_COMPOSING) {
                    boolean isReady = true;
                    for (int i = 0; i < MAX_CHN; i++) {
                        if (mAudioSource[i] != null) {
                            if (mAudioSource[i].capture == audioFileCapture)  {
                                mAudioSource[i].isPrepared = true;
                                Log.d(TAG, "Player " + i + " prepared");
                            } else if (!mAudioSource[i].isPrepared) {
                                isReady = false;
                                Log.d(TAG, "Player " + i + " not ready, wait...");
                            }
                        }
                    }
                    if (isReady) {
                        Log.d(TAG, "All channel prepared, start now");
                        for (int i = 0; i < MAX_CHN; i++) {
                            if (mAudioSource[i] != null) {
                                mAudioSource[i].capture.start();
                            }
                        }
                        if (mState == STATE_PREVIEW_PREPARING) {
                            mAudioPreview.start();
                            mState = STATE_PREVIEW_STARTED;
                        }
                    }
                }
            }
        }
    };

    private void handlePreviewCompletion() {
        if (++mLoopedCount < mLoopCount || mLoopCount < 0) {
            Log.d(TAG, "try to loop, mLoopedCount: " + mLoopedCount);
            // seek到开头, loop播放
            doSeekTo(0);
            if (mOnPreviewInfoListener != null) {
                mOnPreviewInfoListener.onLoopCount(mLoopedCount);
            }
        } else {
            for (int i = 0; i < MAX_CHN; i++) {
                if (mAudioSource[i] != null) {
                    mAudioSource[i].capture.stop();
                }
            }
            mAudioPreview.stop();
            mState = STATE_IDLE;
            if (mOnPreviewInfoListener != null) {
                mOnPreviewInfoListener.onCompletion();
            }
        }
    }

    private AudioFileCapture.OnSeekCompletionListener mOnCaptureSeekCompletionListener = new AudioFileCapture.OnSeekCompletionListener() {
        @Override
        public void onSeekCompletion(AudioFileCapture audioFileCapture, long ms) {
            Log.d(TAG, "onSeekCompletion: " + audioFileCapture + " state: " + mState + " ms: " + ms);
            synchronized (ZqAudioEditorKit.this) {
                // 等待所有seek操作完成后同时开始
                if (mState == STATE_PREVIEW_STARTED || mState == STATE_PREVIEW_PAUSED) {
                    boolean isReady = true;
                    for (int i = 0; i < MAX_CHN; i++) {
                        if (mAudioSource[i] != null) {
                            if (mAudioSource[i].capture == audioFileCapture)  {
                                mAudioSource[i].isSeekCompletion = true;
                                Log.d(TAG, "Player " + i + " seek completion");
                            } else if (!mAudioSource[i].isSeekCompletion) {
                                isReady = false;
                                Log.d(TAG, "Player " + i + " seek not complete, wait...");
                            }
                        }
                    }
                    if (isReady) {
                        Log.d(TAG, "All channel seek completion, start now");
                        for (int i = 0; i < MAX_CHN; i++) {
                            if (mAudioSource[i] != null) {
                                mAudioSource[i].capture.start();
                            }
                        }
                    }
                }
            }
        }
    };

    private AudioFileCapture.OnPositionUpdateListener mOnCapturePositionUpdateListener = new AudioFileCapture.OnPositionUpdateListener() {
        @Override
        public void onPositionUpdate(AudioFileCapture audioFileCapture, long pos) {
            // Log.d(TAG, "onPositionUpdate: " + audioFileCapture + " pos: " + pos);
            synchronized (ZqAudioEditorKit.this) {
                if (mAudioSource[0] != null && mAudioSource[0].capture == audioFileCapture &&
                        mState == STATE_COMPOSING) {
                    float progress = (float) pos / audioFileCapture.getDuration();
                    // Log.d(TAG, "onProgress: " + progress);
                    progress = progress > 1.0f ? 1.0f : progress;
                    if (mOnComposeInfoListener != null) {
                        mOnComposeInfoListener.onProgress(progress, mIsAudioLevelPassing);
                    }
                }
            }
        }
    };

    private AudioFileCapture.OnCompletionListener mOnCaptureCompletionListener = new AudioFileCapture.OnCompletionListener() {
        @Override
        public void onCompletion(AudioFileCapture audioFileCapture) {
            Log.d(TAG, "onCompletion: " + audioFileCapture + " state: " + mState);
            synchronized (ZqAudioEditorKit.this) {
                if (mAudioSource[0] != null && mAudioSource[0].capture == audioFileCapture) {
                    if (mState == STATE_PREVIEW_STARTED || mState == STATE_PREVIEW_PAUSED) {
                        handlePreviewCompletion();
                    } else if (mState == STATE_COMPOSING) {
                        if (mIsAudioLevelPassing) {
                            mIsAudioLevelPassing = false;
                            handleAmpEvalCompletion();
                        }
                        // 合成完成事件依靠Publisher类发出
                    }
                }
            }
        }
    };

    private void handleAmpEvalCompletion() {
        long passingDuration = System.nanoTime() / 1000 / 1000 - mPassingStartTime;
        Log.i(TAG, "audio level passing duration: " + passingDuration);

        float maxAmp = mAudioLevelMeterFilter.getMaxAmplitude(0);
        mAudioLevelMeterFilter.setEnableLevelMeter(0, false);
        for (int i = 0; i < MAX_CHN; i++) {
            if (mAudioSource[i] != null) {
                mAudioSource[i].capture.stop();
            }
        }

        if (mOnComposeInfoListener != null) {
            mOnComposeInfoListener.onProgress(1.0f, true);
            mOnComposeInfoListener.onAudioLevelPassCompletion();
        }

        float dstAmp = getAmpFromDB(mAutoComposeLevelDB);
        float outVol = 1.0f;
        if (maxAmp < dstAmp) {
            outVol = dstAmp / maxAmp;
        }
        Log.d(TAG, "maxAmp: " + maxAmp + " dstAmp: " + dstAmp + " outVol: " + outVol);
        doStartCompose(outVol);
    }

    private float getAmpFromDB(float db) {
        return (float) Math.pow(10, db / 20);
    }

    private void handleError(int err) {
        if (mState == STATE_COMPOSING ||
                mState == STATE_PREVIEW_PREPARING ||
                mState == STATE_PREVIEW_STARTED ||
                mState == STATE_PREVIEW_PAUSED) {
            for (int i = 0; i < MAX_CHN; i++) {
                if (mAudioSource[i] != null) {
                    mAudioSource[i].capture.stop();
                }
            }
            if (mState == STATE_COMPOSING) {
                mAudioEncoder.stop();
            } else {
                mAudioPreview.stop();
            }
            mState = STATE_IDLE;

            if (mOnErrorListener != null) {
                mOnErrorListener.onError(err, 0, 0);
            }
        }
    }

    private AudioFileCapture.OnErrorListener mOnCaptureErrorListener = new AudioFileCapture.OnErrorListener() {
        @Override
        public void onError(AudioFileCapture audioFileCapture, int err, long msg) {
            Log.e(TAG, "onError: " + audioFileCapture + " err: " + err + " msg: " + msg);
            int outError;
            switch (err) {
                case AudioFileCapture.ERROR_IO:
                    outError = ERROR_IO;
                    break;
                case AudioFileCapture.ERROR_UNSUPPORTED:
                    outError = ERROR_UNSUPPORTED;
                    break;
                default:
                    outError = ERROR_UNKNOWN;
                    break;
            }
            handleError(outError);
        }
    };

    private void handleComposeCompletion() {
        long composeDuration = System.nanoTime() / 1000 / 1000 - mComposeStartTime;
        Log.i(TAG, "compose duration: " + composeDuration);

        for (int i = 0; i < MAX_CHN; i++) {
            if (mAudioSource[i] != null) {
                mAudioSource[i].capture.stop();
            }
        }
        mState = STATE_IDLE;
        if (mOnComposeInfoListener != null) {
            mOnComposeInfoListener.onProgress(1.0f, false);
            mOnComposeInfoListener.onCompletion();
        }
    }

    private Publisher.PubListener mPubListener = new Publisher.PubListener() {
        @Override
        public void onInfo(int type, long msg) {
            Log.d(TAG, "FilePubListener onInfo type: " + type + " msg: " + msg);
            if (type == Publisher.INFO_STOPPED) {
                handleComposeCompletion();
            }
        }

        @Override
        public void onError(int err, long msg) {
            Log.e(TAG, "FilePubListener onError err: " + err + " msg: " + msg);
            int outError;
            switch (err) {
                case Publisher.ERROR_IO:
                    outError = ERROR_IO;
                    break;
                default:
                    outError = ERROR_UNKNOWN;
                    break;
            }
            handleError(outError);
        }
    };

    private static class AudioSource {
        public String path;
        public long delayBase;
        public boolean isPrepared;
        public boolean isSeekCompletion;
        public AudioFileCapture capture;
        public AudioResampleFilter audioResampleFilter;
        public AudioFilterMgt filterMgt;
        public int effectType;

        AudioSource(Context context, String path, long offset, long end) {
            this.path = path;
            if (offset < 0) {
                this.delayBase = offset;
                offset = 0;
            } else {
                this.delayBase = 0;
            }
            this.isPrepared = false;
            this.isSeekCompletion = false;
            this.capture = new AudioFileCapture(context);
            this.capture.setDataSource(path, offset, end);
            // TODO: 唱吧音效不支持单声道，这里先强制转为双声道
            this.audioResampleFilter = new AudioResampleFilter();
            this.audioResampleFilter.setOutFormat(new AudioBufFormat(-1, -1, 2));
            this.filterMgt = new AudioFilterMgt();
            this.capture.getSrcPin().connect(this.audioResampleFilter.getSinkPin());
            this.audioResampleFilter.getSrcPin().connect(this.filterMgt.getSinkPin());
        }

        public SrcPin<AudioBufFrame> getSrcPin() {
            return filterMgt.getSrcPin();
        }

        public void setAudioEffect(int type) {
            this.effectType = type;
            // 添加音效
            AudioFilterBase filter = null;
            if (type == Params.AudioEffect.ktv.ordinal()) {
                filter = new TbAudioEffectFilter(2);
            } else if (type == Params.AudioEffect.rock.ordinal()) {
                filter = new TbAudioEffectFilter(1);
            } else if (type == Params.AudioEffect.liuxing.ordinal()) {
                filter = new CbAudioEffectFilter(8);
            } else if (type == Params.AudioEffect.kongling.ordinal()) {
                filter = new CbAudioEffectFilter(1);
            }
            filterMgt.setFilter(filter);
        }

        public void release() {
            capture.release();
        }
    }
}
