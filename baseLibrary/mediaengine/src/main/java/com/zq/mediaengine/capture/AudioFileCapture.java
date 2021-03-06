package com.zq.mediaengine.capture;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.zq.mediaengine.framework.AVConst;
import com.zq.mediaengine.framework.AudioBufFormat;
import com.zq.mediaengine.framework.AudioBufFrame;
import com.zq.mediaengine.framework.SrcPin;
import com.zq.mediaengine.util.HttpMediaDataSource;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * 实现音频文件的解码输出。
 */

public class AudioFileCapture {
    private final static String TAG = "AudioFileCapture";
    private final static boolean VERBOSE = false;

    private static final boolean ENABLE_MEDIA_DATA_SOURCE = false;
    private static final long TIMEOUT_US = 10000;
    private static final int MAX_EOS_SPINS = 10;
    private static final long OFFSET_IGNORE = 20;

    /**
     * The constant STATE_IDLE.
     */
    public static final int STATE_IDLE = 0;
    /**
     * The constant STATE_PREPARING.
     */
    public static final int STATE_PREPARING = 1;
    /**
     * The constant STATE_PREPARED.
     */
    public static final int STATE_PREPARED = 2;
    /**
     * The constant STATE_STARTED.
     */
    public static final int STATE_STARTED = 3;
    /**
     * The constant STATE_STOPPING.
     */
    public static final int STATE_STOPPING = 4;

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

    private final static int CMD_PREPARE = 1;
    private final static int CMD_STOP = 2;
    private final static int CMD_SEEK = 3;
    private final static int CMD_RELEASE = 4;
    private final static int CMD_LOOP = 5;
    private final static int CMD_PAUSE = 6;

    private SrcPin<AudioBufFrame> mSrcPin;

    private Context mContext;
    private AudioBufFormat mOutFormat;
    private ByteBuffer mOutBuffer;
    private float mVolume = 1.0f;
    private String mUrl;
    private long mOffset;
    private long mEnd;

    private MediaExtractor mMediaExtractor;
    private MediaCodec mMediaCodec;
    private MediaCodec.BufferInfo mBufferInfo;
    private int mEosSpinCount = 0;
    private HandlerThread mDecodeThread;
    private Handler mDecodeHandler;
    private Handler mMainHandler;
    private volatile int mState;
    private volatile boolean mPaused;
    private volatile boolean mIsSeeking;
    private boolean mFirstFrameDecoded;
    private long mFirstPts;
    private long mDuration;
    private long mOffsetDuration;
    private long mBasePosition;
    private long mOffsetBasePosition;
    private long mCurrentPosition;
    private long mOffsetCurrentPosition;
    private long mSamplesWritten;
    private boolean mEnableAccurateSeek;
    private long mAccurateSeekPosition;

    private OnPreparedListener mOnPreparedListener;
    private OnFirstAudioFrameDecodedListener mOnFirstAudioFrameDecodedListener;
    private OnSeekCompletionListener mOnSeekCompletionListener;
    private long mPositionUpdateInterval = 100;
    private long mLastUpdateTime = 0;
    private OnPositionUpdateListener mOnPositionUpdateListener;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;

    /**
     * The interface on media prepared listener.
     */
    public interface OnPreparedListener {
        /**
         * On prepared.
         *
         * @param audioFileCapture the AudioFileCapture instance
         */
        void onPrepared(AudioFileCapture audioFileCapture);
    }

    /**
     * The interface on fist audio frame decoded listener.
     */
    public interface OnFirstAudioFrameDecodedListener {
        /**
         * On prepared.
         *
         * @param audioFileCapture the AudioFileCapture instance
         * @param time time in ms when first audio frame decoded
         */
        void onFirstAudioFrameDecoded(AudioFileCapture audioFileCapture, long time);
    }

    /**
     * The interface On seek completion listener.
     */
    public interface OnSeekCompletionListener {
        /**
         * On seek completion.
         *
         * @param audioFileCapture the AudioFileCapture instance
         * @param ms position seek in ms
         */
        void onSeekCompletion(AudioFileCapture audioFileCapture, long ms);
    }

    /**
     * The interface On position update listener.
     */
    public interface OnPositionUpdateListener {
        /**
         * On position update.
         *
         * @param audioFileCapture the AudioFileCapture instance
         * @param pos current position in ms
         */
        void onPositionUpdate(AudioFileCapture audioFileCapture, long pos);
    }

    /**
     * The interface On completion listener.
     */
    public interface OnCompletionListener {
        /**
         * On completion.
         *
         * @param audioFileCapture the AudioFileCapture instance
         */
        void onCompletion(AudioFileCapture audioFileCapture);
    }

    /**
     * The interface on audio play error listener.
     */
    public interface OnErrorListener {
        /**
         * On error.
         *
         * @param audioFileCapture  the AudioFileCapture instance
         * @param type              the error type
         * @param msg               the extra msg
         */
        void onError(AudioFileCapture audioFileCapture, int type, long msg);
    }

    /**
     * Instantiates a new Audio player capture.
     *
     * @param context the context
     */
    public AudioFileCapture(Context context) {
        mState = STATE_IDLE;
        mEnableAccurateSeek = false;
        mContext = context;
        mSrcPin = new SrcPin<>();
        mMainHandler = new Handler(Looper.getMainLooper());
        initDecodeThread();
    }

    /**
     * Gets src pin.
     *
     * @return the src pin
     */
    public SrcPin<AudioBufFrame> getSrcPin() {
        return mSrcPin;
    }

    /**
     * Get current state.
     *
     * @return current state
     */
    public int getState() {
        return mState;
    }

    /**
     * Get work handler.
     *
     * @return work handler
     */
    public Handler getWorkHandler() {
        return mDecodeHandler;
    }

    /**
     * Sets volume.
     *
     * @param volume the volume, should be 0.0f-1.0f
     */
    public void setVolume(float volume) {
        mVolume = volume;
    }

    /**
     * Gets volume.
     *
     * @return the volume, should in 0.0f-1.0f
     */
    public float getVolume() {
        return mVolume;
    }

    /**
     * Sets on prepared listener.
     *
     * @param listener the listener
     */
    public void setOnPreparedListener(OnPreparedListener listener) {
        mOnPreparedListener = listener;
    }

    /**
     * Sets on first audio frame decoded listener.
     *
     * @param listener the listener
     */
    public void setOnFirstAudioFrameDecodedListener(OnFirstAudioFrameDecodedListener listener) {
        mOnFirstAudioFrameDecodedListener = listener;
    }

    /**
     * Sets on seek completion listener.
     *
     * @param listener the listener
     */
    public void setOnSeekCompletionListener(OnSeekCompletionListener listener) {
        mOnSeekCompletionListener = listener;
    }

    /**
     * Sets on position update listener.
     *
     * @param listener the listener
     * @param interval update interval
     */
    public void setOnPositionUpdateListener(OnPositionUpdateListener listener, long interval) {
        mPositionUpdateInterval = interval;
        mOnPositionUpdateListener = listener;
    }

    /**
     * Sets on completion listener.
     *
     * @param listener the listener
     */
    public void setOnCompletionListener(OnCompletionListener listener) {
        mOnCompletionListener = listener;
    }

    /**
     * Sets on error listener.
     *
     * @param listener the listener
     */
    public void setOnErrorListener(OnErrorListener listener) {
        mOnErrorListener = listener;
    }

    /**
     * Set if enable accurate seek
     *
     * @param enableAccurateSeek if enable accurate seek
     */
    public void setEnableAccurateSeek(boolean enableAccurateSeek) {
        mEnableAccurateSeek = enableAccurateSeek;
    }

    /**
     * Get is accurate seek enabled
     *
     * @return is accurate seek enabled
     */
    public boolean isEnableAccurateSeek() {
        return mEnableAccurateSeek;
    }

    /**
     * Set data source.
     *
     * @param url  the url.
     *             prefix "file://" for absolute path,
     *             and prefix "assets://" for resource in assets folder,
     *             also prefix "http://", "https://"  supported.
     */
    public void setDataSource(String url) {
        setDataSource(url, 0, -1);
    }

    /**
     * Set data source with specified playback range.
     *
     * @param url  the url.
     *             prefix "file://" for absolute path,
     *             and prefix "assets://" for resource in assets folder,
     *             also prefix "http://", "https://"  supported.
     * @param offset 配置当前音频文件的实际播放开始时间，小于0时按0计算
     * @param end 配置当前音频文件的实际播放完成时间，小于0或者大于音频长度时，按音频长度计算
     */
    public void setDataSource(String url, long offset, long end) {
        mUrl = url;
        mOffset = offset;
        mEnd = end;
    }

    /**
     * Prepare async.
     */
    public void prepareAsync() {
        prepareAsync(null);
    }

    public void prepareAsync(Runnable r) {
        Message msg = mDecodeHandler.obtainMessage(CMD_PREPARE);
        mDecodeHandler.sendMessage(msg);
    }

    /**
     * Start.
     */
    public void start() {
        start(null);
    }

    public void start(Runnable r) {
        Message msg = mDecodeHandler.obtainMessage(CMD_PAUSE, 0, 0, r);
        mDecodeHandler.sendMessage(msg);
    }

    /**
     * Stop.
     */
    public void stop() {
        stop(null);
    }

    public void stop(Runnable r) {
        Message msg = mDecodeHandler.obtainMessage(CMD_STOP, r);
        mDecodeHandler.sendMessage(msg);
    }

    /**
     * Pause.
     */
    public void pause() {
        pause(null);
    }

    public void pause(Runnable r) {
        Message msg = mDecodeHandler.obtainMessage(CMD_PAUSE, 1, 0, r);
        mDecodeHandler.sendMessage(msg);
    }

    /**
     * Seek.
     *
     * @param ms the time seek to, in miliseconds
     */
    public void seek(long ms) {
        seek(ms, null);
    }

    public void seek(long ms, Runnable r) {
        Message msg = mDecodeHandler.obtainMessage(CMD_SEEK, (int) ms, 0, r);
        mDecodeHandler.sendMessage(msg);
    }

    /**
     * Release.
     */
    public void release() {
        stop();
        mDecodeHandler.sendEmptyMessage(CMD_RELEASE);
        mOnPreparedListener = null;
        mOnFirstAudioFrameDecodedListener = null;
        mOnPositionUpdateListener = null;
        mOnCompletionListener = null;
        mOnErrorListener = null;
        mOnSeekCompletionListener = null;
    }

    /**
     * Gets duration.
     *
     * @return the duration in milisenconds
     */
    public long getDuration() {
        return mOffsetDuration;
    }

    /**
     * Gets position.
     *
     * @return current position in miliseconds
     */
    public long getPosition() {
        return mOffsetCurrentPosition;
    }

    /**
     * Gets base position.
     *
     * @return base position in miliseconds
     */
    public long getBasePosition() {
        return mOffsetBasePosition;
    }

    private void initDecodeThread() {
        mDecodeThread = new HandlerThread("AudioDecode");
        mDecodeThread.start();
        mDecodeHandler = new Handler(mDecodeThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int err;
                switch (msg.what) {
                    case CMD_PREPARE:
                        if (mState != STATE_IDLE) {
                            break;
                        }
                        mState = STATE_PREPARING;
                        err = doStart();
                        if (msg.obj != null) {
                            ((Runnable) msg.obj).run();
                        }
                        if (err != 0) {
                            mState = STATE_IDLE;
                            postError(err, 0);
                        } else {
                            if (mOffset >= OFFSET_IGNORE) {
                                Log.d(TAG, "seek on start with: " + mOffset);
                                Message sendMsg = mDecodeHandler.obtainMessage(CMD_SEEK, 0, 0);
                                mDecodeHandler.sendMessage(sendMsg);
                            } else {
                                // trigger format changed
                                mSrcPin.onFormatChanged(mOutFormat);
                                mState = STATE_PREPARED;
                                postOnPrepared();
                            }
                        }
                        break;
                    case CMD_LOOP:
                        if (mState == STATE_PREPARED) {
                            mState = STATE_STARTED;
                        }
                        if (mState != STATE_STARTED) {
                            break;
                        }
                        if (mPaused) {
                            break;
                        }
                        boolean eos = false;
                        try {
                            eos = doLoop();
                        } catch (Exception e) {
                            Log.e(TAG, "decode frame failed!");
                            e.printStackTrace();
                            mDecodeHandler.sendEmptyMessage(CMD_STOP);
                            postError(ERROR_UNKNOWN, 0);
                            break;
                        }
                        if (!eos) {
                            mDecodeHandler.sendEmptyMessage(CMD_LOOP);
                        } else {
                            postOnCompletion();
                        }
                        break;
                    case CMD_PAUSE:
                        if (mState != STATE_PREPARED && mState != STATE_STARTED) {
                            break;
                        }
                        if (msg.arg1 != 0 && !mPaused) {
                            mPaused = true;
                        } else if (msg.arg1 == 0 && (mPaused || mState == STATE_PREPARED)) {
                            mPaused = false;
                            mDecodeHandler.sendEmptyMessage(CMD_LOOP);
                        }
                        if (msg.obj != null) {
                            ((Runnable) msg.obj).run();
                        }
                        break;
                    case CMD_STOP:
                        if (mState != STATE_STARTED) {
                            break;
                        }
                        mState = STATE_STOPPING;
                        doStop();
                        if (msg.obj != null) {
                            ((Runnable) msg.obj).run();
                        }
                        mState = STATE_IDLE;
                        break;
                    case CMD_SEEK:
                        if (mState != STATE_PREPARING && mState != STATE_STARTED) {
                            break;
                        }
                        if (msg.obj != null) {
                            ((Runnable) msg.obj).run();
                        }
                        doSeek(msg.arg1);
                        if (mState == STATE_PREPARING) {
                            mState = STATE_PREPARED;
                            postOnPrepared();
                        } else {
                            postOnSeekCompletion(msg.arg1);
                            mDecodeHandler.sendEmptyMessage(CMD_LOOP);
                        }
                        break;
                    case CMD_RELEASE:
                        mSrcPin.disconnect(true);
                        mDecodeThread.quit();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void postOnPrepared() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mOnPreparedListener != null) {
                    mOnPreparedListener.onPrepared(AudioFileCapture.this);
                }
            }
        });
    }

    private void postOnFirstAudioFrameDecoded(final long time) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mOnFirstAudioFrameDecodedListener != null) {
                    mOnFirstAudioFrameDecodedListener.onFirstAudioFrameDecoded(AudioFileCapture.this, time);
                }
            }
        });
    }

    private void postOnSeekCompletion(final long ms) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mOnSeekCompletionListener != null) {
                    mOnSeekCompletionListener.onSeekCompletion(AudioFileCapture.this, ms);
                }
            }
        });
    }

    private void postOnPositionUpdate(final long pos) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mOnPositionUpdateListener != null) {
                    mOnPositionUpdateListener.onPositionUpdate(AudioFileCapture.this, pos);
                }
            }
        });
    }

    private void postOnCompletion() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mOnCompletionListener != null) {
                    mOnCompletionListener.onCompletion(AudioFileCapture.this);
                }
            }
        });
    }

    private void postError(final int err, final long msg) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mOnErrorListener != null) {
                    mOnErrorListener.onError(AudioFileCapture.this, err, msg);
                }
            }
        });
    }

    private boolean isHttpDataSource(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    private void doSetDataSource(String url) throws IOException {
        final String filePrefix = "file://";
        final String assetsPrefix = "assets://";

        if (url == null || url.isEmpty()) {
            throw new IOException("url is empty!");
        }
        Log.d(TAG, "Try to open url " + mUrl);
        if (url.startsWith(filePrefix)) {
            String path = url.substring(filePrefix.length());
            mMediaExtractor.setDataSource(path);
        } else if (url.startsWith(assetsPrefix)) {
            String path = url.substring(assetsPrefix.length());
            final AssetFileDescriptor afd = mContext.getAssets().openFd(path);
            mMediaExtractor.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
        } else if (ENABLE_MEDIA_DATA_SOURCE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isHttpDataSource(url)) {
            // use custom data source to deal with network issue
            // but caused much slower prepare time, disabled for now
            HttpMediaDataSource httpMediaDataSource = new HttpMediaDataSource(mUrl);
            mMediaExtractor.setDataSource(httpMediaDataSource);
        } else {
            mMediaExtractor.setDataSource(mUrl);
        }
    }

    private int doStart() {
        mFirstPts = Long.MIN_VALUE;
        mFirstFrameDecoded = false;
        mPaused = false;
        mLastUpdateTime = 0;
        mIsSeeking = false;
        mAccurateSeekPosition = Long.MIN_VALUE;
        mDuration = 0;
        mOffsetDuration = 0;
        mBasePosition = 0;
        mOffsetBasePosition = 0;
        mCurrentPosition = 0;
        mOffsetCurrentPosition = 0;
        mSamplesWritten = 0;
        mMediaExtractor = new MediaExtractor();
        try {
            doSetDataSource(mUrl);
        } catch (IOException e) {
            Log.e(TAG, "Open " + mUrl + " failed");
            e.printStackTrace();
            return ERROR_IO;
        }

        Log.i(TAG, "getCachedDuration0: " + mMediaExtractor.getCachedDuration());
        int numTracks = mMediaExtractor.getTrackCount();
        for (int i = 0; i< numTracks; i++) {
            MediaFormat mediaFormat = mMediaExtractor.getTrackFormat(i);
            String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                mMediaExtractor.selectTrack(i);
                mDuration = mediaFormat.getLong(MediaFormat.KEY_DURATION) / 1000;
                mFirstPts = mMediaExtractor.getSampleTime();
                long end = (mEnd < 0 || mEnd > mDuration) ? mDuration : mEnd;
                long dur = end - mOffset;
                mOffsetDuration = dur < 0 ? 0 : dur;
                Log.d(TAG, "duration: " + mDuration + " offsetDuration: " + mOffsetDuration +
                        " first pts: " + mFirstPts);

                if ((mOffset + OFFSET_IGNORE > mDuration) ||
                        (mOffset + OFFSET_IGNORE > end)) {
                    Log.e(TAG, "playback duration " + mOffsetDuration + "ms too short! " +
                            "off: " + mOffset + " end: " + mEnd + " duration: " + mDuration);
                    return ERROR_UNSUPPORTED;
                }

                // audio format
                int sampleRate = mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                int channels = mediaFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                mOutFormat = new AudioBufFormat(AVConst.AV_SAMPLE_FMT_S16, sampleRate, channels);

                // open decoder
                try {
                    mMediaCodec = MediaCodec.createDecoderByType(mime);
                    mMediaCodec.configure(mediaFormat, null, null, 0);
                } catch (Exception e) {
                    Log.e(TAG, "init decoder " + mime + " failed!");
                    e.printStackTrace();
                    return ERROR_UNSUPPORTED;
                }
                mMediaCodec.start();
                mBufferInfo = new MediaCodec.BufferInfo();
                Log.i(TAG, "getCachedDuration1: " + mMediaExtractor.getCachedDuration());
                return 0;
            }
        }

        return ERROR_UNSUPPORTED;
    }

    private void doStop() {
        try {
            mMediaCodec.stop();
            mMediaCodec.release();
            mMediaExtractor.release();
        } catch (Exception e) {
            Log.w(TAG, "stop failed!");
            e.printStackTrace();
        }
    }

    private boolean doLoop() {
        boolean eos = fillDecoder();
        drainDecoder(eos);
        return eos;
    }

    private void doSeek(long ms) {
        mMediaCodec.flush();
        mOffsetCurrentPosition = ms;
        mCurrentPosition = ms + mOffset;
        if (mEnableAccurateSeek) {
            mAccurateSeekPosition = mCurrentPosition;
        }
        mSamplesWritten = 0;
        mIsSeeking = true;
        mMediaExtractor.seekTo(mCurrentPosition * 1000, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);

        long seeked = mMediaExtractor.getSampleTime() / 1000;
        Log.d(TAG, "doSeek pos: " + mCurrentPosition + " seeked: " + seeked);

        // seek后发送flush和onFormatChanged事件
        AudioBufFrame frame = new AudioBufFrame(mOutFormat, null, 0);
        frame.flags |= AVConst.FLAG_FLUSH_OF_STREAM;
        mSrcPin.onFrameAvailable(frame);
        mSrcPin.onFormatChanged(mOutFormat);
    }

    private boolean fillDecoder() {
        boolean eos = false;
        ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();
        int inputBufferIndex = mMediaCodec.dequeueInputBuffer(TIMEOUT_US);
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
            if (VERBOSE) {
                Log.d(TAG, "getCachedDuration loop: " + mMediaExtractor.getCachedDuration() +
                        " " + mMediaExtractor.hasCacheReachedEndOfStream());
            }
            int sampleSize = mMediaExtractor.readSampleData(inputBuffer, 0);
            long pts = (sampleSize >= 0) ? mMediaExtractor.getSampleTime() : 0;
            if (sampleSize >= 0 && (mEnd < 0 || mEnd * 1000 > pts)) {
                if (mIsSeeking) {
                    mBasePosition = (pts - mFirstPts) / 1000;
                    long pos = mBasePosition - mOffset;
                    mOffsetBasePosition = pos > 0 ? pos : 0;
                    mIsSeeking = false;
                }
                if (VERBOSE) Log.d(TAG, "fill decoder " + sampleSize + " pts: " + pts / 1000);
                mMediaCodec.queueInputBuffer(inputBufferIndex, 0, sampleSize, pts, 0);
                mMediaExtractor.advance();
            } else {
                Log.d(TAG, "EOS got, flush decoder");
                eos = true;
                mMediaCodec.queueInputBuffer(inputBufferIndex, 0, 0, 0,
                        MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            }
        }
        return eos;
    }

    private short clip_short(int val) {
        if (((val + 0x8000) & ~0xFFFF) != 0) {
            return (short) ((val >> 31) ^ 0x7FFF);
        } else {
            return (short) val;
        }
    }

    private ByteBuffer applyVolume(ByteBuffer inBuffer) {
        if (mOutBuffer != null && mOutBuffer.capacity() < inBuffer.limit()) {
            mOutBuffer = null;
        }
        if (mOutBuffer == null) {
            int size = 8 * 1024;
            while (size < inBuffer.limit()) {
                size *= 2;
            }
            mOutBuffer = ByteBuffer.allocateDirect(size);
            mOutBuffer.order(ByteOrder.nativeOrder());
        }
        mOutBuffer.clear();
        mOutBuffer.put(inBuffer);
        mOutBuffer.flip();
        inBuffer.rewind();
        if (mVolume != 1.0f) {
            ShortBuffer shortBuffer = mOutBuffer.asShortBuffer();
            int shortSize = mOutBuffer.limit() / 2;
            for (int i = 0; i < shortSize; i++) {
                shortBuffer.put(i, clip_short((int) (shortBuffer.get(i) * mVolume)));
            }
        }
        return mOutBuffer;
    }

    private void drainDecoder(boolean eos) {
        ByteBuffer[] outputBuffers = mMediaCodec.getOutputBuffers();
        mEosSpinCount = 0;
        long timeoutUs = TIMEOUT_US;
        while (true) {
            int outputBufferIndex;
            try {
                outputBufferIndex = mMediaCodec.dequeueOutputBuffer(mBufferInfo, timeoutUs);
            } catch (Exception e) {
                // Exynos socs may report invalid state exception even on a valid state,
                // when no input frame filled but eos signaled.
                Log.e(TAG, "dequeueOutputBuffer failed");
                break;
            }
            if (outputBufferIndex >= 0) {
                if (VERBOSE) Log.d(TAG, "drain decoder size: " + mBufferInfo.size +
                        " pts: " + mBufferInfo.presentationTimeUs / 1000);

                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                // accurate seek
                int offSize = 0;
                if (mAccurateSeekPosition != Long.MIN_VALUE) {
                    long curPts = (mBufferInfo.presentationTimeUs - mFirstPts) / 1000;
                    long durSamples = mBufferInfo.size / mOutFormat.channels / 2;
                    long dur = durSamples * 1000 / mOutFormat.sampleRate;
                    long diff = mAccurateSeekPosition - curPts;
                    long diffSamples = diff * mOutFormat.sampleRate / 1000;
                    Log.d(TAG, "[Accurate seek] seek: " + mAccurateSeekPosition +
                            " pts: " + curPts + " dur: " + dur + " diff: " + diff +
                            " oriSize: " + mBufferInfo.size);
                    if (diff <= 0) {
                        mAccurateSeekPosition = Long.MIN_VALUE;
                    } else if (diffSamples >= durSamples) {
                        Log.d(TAG, "[Accurate seek] drop current frame with samples: " + durSamples);
                        mMediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                        continue;
                    } else {
                        Log.d(TAG, "[Accurate seek] drop samples: " + diffSamples);
                        offSize = (int)diffSamples * mOutFormat.channels * 2;
                        mAccurateSeekPosition = Long.MIN_VALUE;
                    }
                }
                outputBuffer.position(mBufferInfo.offset + offSize);
                outputBuffer.limit(mBufferInfo.size + mBufferInfo.offset);

                ByteBuffer buffer = applyVolume(outputBuffer);
                if (offSize > 0) {
                    Log.d(TAG, "[Accurate seek] outBuffer: " + buffer);
                }
                AudioBufFrame frame = new AudioBufFrame(mOutFormat, buffer,
                        mBufferInfo.presentationTimeUs / 1000);
                if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    frame.flags |= AVConst.FLAG_END_OF_STREAM;
                    Log.d(TAG, "eos frame got, size = " + mBufferInfo.size);
                }
                mSrcPin.onFrameAvailable(frame);
                mMediaCodec.releaseOutputBuffer(outputBufferIndex, false);

                if (!mFirstFrameDecoded) {
                    mFirstFrameDecoded = true;
                    long time = System.nanoTime() / 1000 / 1000;
                    postOnFirstAudioFrameDecoded(time);
                }

                mSamplesWritten += frame.buf.limit() / 2 / mOutFormat.channels;
                mCurrentPosition = mBasePosition + mSamplesWritten * 1000 / mOutFormat.sampleRate;
                long tmpPos = mCurrentPosition - mOffset;
                mOffsetCurrentPosition = tmpPos > 0 ? tmpPos : 0;

                // update position
                long curTime = System.nanoTime() / 1000 / 1000;
                if (curTime - mLastUpdateTime >= mPositionUpdateInterval) {
                    mLastUpdateTime = curTime;
                    postOnPositionUpdate(mOffsetCurrentPosition);
                }

                if (VERBOSE) {
                    long pos = (mBufferInfo.presentationTimeUs - mFirstPts) / 1000;
                    Log.i(TAG, "pos: " + pos + " position: " + mCurrentPosition +
                            " samples: " + mSamplesWritten);
                }

                if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    break;
                }
                timeoutUs = 0;
            } else if (outputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                if (VERBOSE) Log.d(TAG, "INFO_TRY_AGAIN_LATER");
                if (!eos) {
                    break;
                } else {
                    mEosSpinCount++;
                    if (mEosSpinCount > MAX_EOS_SPINS) {
                        Log.i(TAG, "Force shutting down decoder for MAX_EOS_SPINS reached");
                        AudioBufFrame frame = new AudioBufFrame(mOutFormat, null, 0);
                        frame.flags |= AVConst.FLAG_END_OF_STREAM;
                        mSrcPin.onFrameAvailable(frame);
                        break;
                    }
                    if (VERBOSE) Log.d(TAG, "no output available, spinning to await EOS");
                }
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                Log.d(TAG, "INFO_OUTPUT_FORMAT_CHANGED");
            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                outputBuffers = mMediaCodec.getOutputBuffers();
                Log.d(TAG, "INFO_OUTPUT_BUFFERS_CHANGED");
            } else {
                Log.w(TAG, "unexpected result to dequeueOutputBuffer: " + outputBufferIndex);
                break;
            }
        }
    }
}
