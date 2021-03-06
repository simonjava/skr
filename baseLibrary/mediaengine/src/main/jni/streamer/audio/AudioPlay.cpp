//
// Created by 乐 程 on 17/03/2017.
//

#include <assert.h>
#include <string.h>
#include <math.h>
#include <include/log.h>
#include "AudioPlay.h"
#include "thread_util.h"

#undef LOG_TAG
#define LOG_TAG "AudioPlay"

#undef VERBOSE
#define VERBOSE 0

AudioPlay::AudioPlay():
    mMute(false),
    mVolume(1.0f),
    mNonBlock(false),
    mTuneLatency(false),
    mFirstFrame(true),
    mBuffer(NULL),
    mFifoBuffer(NULL),
    mWriteCond(NULL),
    mState(STATE_IDLE),
    mStart(false),
    mSamplesWritten(0),
    mZeroSamplesEnqueue(0),
    mSamplesEnqueue(0){
    pthread_mutex_init(&mPlayerLock, NULL);
    memset(&mSLPlayer, 0, sizeof(mSLPlayer));
}

AudioPlay::~AudioPlay() {
    LOGD("~AudioPlay");
    pthread_mutex_lock(&mPlayerLock);
    release();
    pthread_mutex_unlock(&mPlayerLock);
    pthread_mutex_destroy(&mPlayerLock);
}

int AudioPlay::config(int sampleFmt, int sampleRate, int channels, int bufferSamples, int fifoSizeInMs) {
    LOGD("config %dHz %d channels, bufferSamples: %d, fifoSizeInMs: %d", sampleRate, channels, bufferSamples, fifoSizeInMs);
    int ret = 0;
    int threshold;
    pthread_mutex_lock(&mPlayerLock);
    // destroy previous instance
    release();

    if (sampleFmt != SAMPLE_FMT_S16) {
        LOGE("AudioPlay only support SAMPLE_FMT_S16!");
        ret = -1;
        goto EXIT;
    }

    mSampleFmt = sampleFmt;
    mSampleRate = sampleRate;
    mChannels = channels;
    mBufferSamples = bufferSamples;
    mFifoSizeInMs = fifoSizeInMs;
    mFrameSize = 2 * channels;

    mSamplesWritten = 0;
    mZeroSamplesEnqueue = 0;
    mSamplesEnqueue = 0;

    mLastWriteTime = 0;
    mWriteInterval = mBufferSamples * 1000000 / mSampleRate;
    mLastLogTime = 0;

    // create engineObject and engineEngine
    if (createEngine() != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay] CreateEngine failed");
        destroyEngine();
        ret = -1;
        goto EXIT;
    }

    if (openPlayer() != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay] openPlayer failed");
        closePlayer();
        destroyEngine();
        ret = -1;
        goto EXIT;
    }

    mBuffer = (uint8_t*) malloc((size_t) (bufferSamples * mFrameSize));
    assert(mBuffer);

    threshold = mSampleRate * mFifoSizeInMs / 1000;
    mFifoSamples = mBufferSamples * 3;
    while (mFifoSamples < threshold) {
        mFifoSamples += mBufferSamples;
    }
    LOGD("FIFO size: %d count: %d", mFifoSamples, mFifoSamples / mBufferSamples);
    mFifoBuffer = (uint8_t*) malloc((size_t) (mFifoSamples * mFrameSize));
    assert(mFifoBuffer);
    audio_utils_fifo_init(&mFifo, (size_t) mFifoSamples, (size_t) mFrameSize, mFifoBuffer);
    mWriteCond = createThreadLock();
    waitThreadLock(mWriteCond);

    mState = STATE_INITIALIZED;

    if (mStart) {
        if (startPlayer() != SL_RESULT_SUCCESS) {
            LOGE("Auto start player failed!");
            ret = -1;
            goto EXIT;
        }
        setVolumeNoLock(mVolume);
        if (mMute) {
            mutePlayer(mMute);
        }
    }

EXIT:
    pthread_mutex_unlock(&mPlayerLock);
    return ret;
}

void AudioPlay::setMute(bool mute) {
    LOGD("setMute: %d", mute);
    pthread_mutex_lock(&mPlayerLock);
    mMute = mute;
    if (mState == STATE_PLAYING || mState == STATE_PAUSE) {
        mutePlayer(mute);
    }
    pthread_mutex_unlock(&mPlayerLock);
}

void AudioPlay::setVolume(float volume) {
    LOGD("setVolume: %f", volume);
    pthread_mutex_lock(&mPlayerLock);
    mVolume = volume;
    setVolumeNoLock(volume);
    pthread_mutex_unlock(&mPlayerLock);
}

SLresult AudioPlay::setVolumeNoLock(float volume) {
    if (mState == STATE_PLAYING || mState == STATE_PAUSE) {
        int mb = lroundf(2000.f * log10(volume));
        if (mb < SL_MILLIBEL_MIN) {
            mb = SL_MILLIBEL_MIN;
        } else if (mb > 0) {
            mb = 0;
        }
        SLresult result = (*mSLPlayer.volumeItf)->SetVolumeLevel(mSLPlayer.volumeItf, mb);
        if (result != SL_RESULT_SUCCESS) {
            LOGE("SetVolumeLevel %d failed:%d", mb, (int) result);
        }
    }
}

int AudioPlay::start() {
    LOGD("start in state: %d", mState);
    int ret = 0;
    pthread_mutex_lock(&mPlayerLock);
    if (mState == STATE_INITIALIZED) {
        if (startPlayer() != SL_RESULT_SUCCESS) {
            ret = -1;
            goto EXIT;
        }
        setVolumeNoLock(mVolume);
        if (mMute) {
            mutePlayer(mMute);
        }
    }
    mStart = true;
EXIT:
    pthread_mutex_unlock(&mPlayerLock);
    return ret;
}

int AudioPlay::pause() {
    LOGD("pause in state: %d", mState);
    int ret = 0;
    pthread_mutex_lock(&mPlayerLock);
    if (mState == STATE_PLAYING) {
        if (pausePlayer() != SL_RESULT_SUCCESS) {
            ret = -1;
            goto EXIT;
        }
    }
EXIT:
    pthread_mutex_unlock(&mPlayerLock);
    return ret;
}

int AudioPlay::resume() {
    LOGD("resume in state: %d", mState);
    int ret = 0;
    pthread_mutex_lock(&mPlayerLock);
    if (mState == STATE_PAUSE) {
        if (resumePlayer() != SL_RESULT_SUCCESS) {
            ret = -1;
            goto EXIT;
        }
    }
EXIT:
    pthread_mutex_unlock(&mPlayerLock);
    return ret;
}

int AudioPlay::stop() {
    LOGD("stop");
    int ret = 0;
    SLresult result;
    pthread_mutex_lock(&mPlayerLock);
    mStart = false;
    if ((mState != STATE_PLAYING) && (mState != STATE_PAUSE)) {
        ret = 0;
        goto EXIT;
    }

    // set the player's state to stopped
    result = (*mSLPlayer.playerPlay)->SetPlayState(mSLPlayer.playerPlay, SL_PLAYSTATE_STOPPED);
    if (result != SL_RESULT_SUCCESS) {
        LOGE("[stop] SetPlayState failed:%d", (int) result);
        ret = -1;
        goto EXIT;
    }

    mState = STATE_INITIALIZED;
    notifyThreadLock(mWriteCond);
    audio_utils_fifo_flush(&mFifo);
EXIT:
    pthread_mutex_unlock(&mPlayerLock);
    return ret;
}

int AudioPlay::write(uint8_t *inBuf, int inSize, bool nonBlock) {
    if (mState != STATE_PLAYING) {
        return filterProcess(mSampleFmt, mSampleRate, mChannels, mBufferSamples, inBuf, inSize);
    }

    mNonBlock = nonBlock;
    int inSamples = inSize / mFrameSize;
    int len = audio_utils_fifo_write(&mFifo, inBuf, (size_t) inSamples);
    if (nonBlock) {
        if (len < inSamples) {
            LOGD("fifo full, only write %d samples", len);
        }
        return filterProcess(mSampleFmt, mSampleRate, mChannels, mBufferSamples, inBuf, inSize);
    }
    while (len < inSamples) {
        waitThreadLock(mWriteCond);
        if (mState != STATE_PLAYING) {
            LOGD("write aborted!");
            break;
        }
        len += audio_utils_fifo_write(&mFifo, inBuf + len * mFrameSize,
                                      (size_t) (inSamples - len));
    }
    mSamplesWritten += len;
    return len * mFrameSize;
}

int AudioPlay::init(int idx, int sampleFmt, int sampleRate, int channels, int bufferSamples) {
    filterInit(sampleFmt, sampleRate, channels, bufferSamples);
    return config(sampleFmt, sampleRate, channels, bufferSamples);
}

int AudioPlay::process(int idx, uint8_t *inBuf, int inSize) {
    return write(inBuf, inSize, true);
}

/*
 * Aquire current timestamp in milliseconds
 */
static inline int64_t getNsTimestamp() {
    struct timespec stamp;
    clock_gettime(CLOCK_MONOTONIC, &stamp);
    int64_t nsec = (int64_t) stamp.tv_sec*1000000000LL + stamp.tv_nsec;
    return nsec;
}

void AudioPlay::bqPlayerCallback(SLAndroidSimpleBufferQueueItf bq, void *context) {
    AudioPlay *thiz = (AudioPlay *) context;
    size_t size = (size_t) (thiz->mBufferSamples * thiz->mFrameSize);
    int64_t now = getNsTimestamp() / 1000;

    // Audio play jitter detect
    int readCount = 1;
    if (thiz->mLastWriteTime != 0 && thiz->mTuneLatency) {
        int64_t diff = now - thiz->mLastWriteTime;
        if (diff >= thiz->mWriteInterval * 7 / 4) {
            readCount = (int) ((diff + thiz->mWriteInterval / 4) / thiz->mWriteInterval);
            LOGW("write jitter: %d, dequeue count: %d", (int) diff, readCount);
        }
    }
    thiz->mLastWriteTime = now;

    // send data to next filter in blocking mode here
    if (!thiz->mFirstFrame && !thiz->mNonBlock) {
        thiz->filterProcess(thiz->mSampleFmt, thiz->mSampleRate, thiz->mChannels,
                            thiz->mBufferSamples, thiz->mBuffer, size);
    }

    int len = 0;
    int samplesWritten = 0;
    memset(thiz->mBuffer, 0, size);
    do {
        if (len > 0) {
            LOGD("[AudioPlay][Play] %d samples flushed!", len);
        }
        len = audio_utils_fifo_read(&thiz->mFifo, thiz->mBuffer, (size_t) thiz->mBufferSamples);
        samplesWritten += len;
        if (len < thiz->mBufferSamples) {
            LOGD("[AudioPlay][Play] fifo empty, enqueue %d samples", len);
        }
    } while(thiz->mNonBlock && (thiz->mFirstFrame || --readCount) && len == thiz->mBufferSamples);
    thiz->mFirstFrame = false;

    SLresult result = (*thiz->mSLPlayer.playerBufferQueue)->Enqueue(
            thiz->mSLPlayer.playerBufferQueue, thiz->mBuffer, size);
    if (result != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay][Play] Enqueue failed:%d",(int)result);
    } else {
        thiz->mSamplesEnqueue += samplesWritten;
        thiz->mZeroSamplesEnqueue += thiz->mBufferSamples - samplesWritten;
    }

    // measure fifo size
    if (thiz->mNonBlock && now - thiz->mLastLogTime >= 5000000) {
        LOGD("fifo remain: %d", (int) audio_utils_fifo_get_remain(&thiz->mFifo));
        thiz->mLastLogTime = now;
    }

    notifyThreadLock(thiz->mWriteCond);
}

SLresult AudioPlay::createEngine() {
    SLresult result;
    // create engine to state SL_OBJECT_STATE_UNREALIZED
    result = slCreateEngine(&(mSLPlayer.engineObject), 0, NULL, 0, NULL, NULL);
    if(result != SL_RESULT_SUCCESS) {
        LOGE("[audio_record][CreateEngine] slCreateEngine failed");
        return result;
    }

    // realize the engine to state SL_OBJECT_STATE_REALIZED
    result = (*mSLPlayer.engineObject)->Realize(mSLPlayer.engineObject, SL_BOOLEAN_FALSE);
    if(result != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay][CreateEngine] Realize failed");
        return result;
    }

    // get the engine interface, which is needed in order to create other objects
    result = (*mSLPlayer.engineObject)->GetInterface(mSLPlayer.engineObject, SL_IID_ENGINE,
                                                   &(mSLPlayer.engineEngine));
    if(result != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay][CreateEngine] GetInterface engineEngine failed");
        return result;
    }

    const SLInterfaceID ids[] = {};
    const SLboolean req[] = {};
    result = (*mSLPlayer.engineEngine)->CreateOutputMix(mSLPlayer.engineEngine,
                                                        &mSLPlayer.outputMixObject,
                                                        0, ids, req);
    if(result != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay][CreateEngine] CreateOutputMix failed");
        return result;
    }

    // realize the output mix
    result = (*mSLPlayer.outputMixObject)->Realize(mSLPlayer.outputMixObject, SL_BOOLEAN_FALSE);
    if(result != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay][CreateEngine] Realize outputMixObject failed");
        return result;
    }

    return result;
}

void AudioPlay::destroyEngine() {
    // destroy output mix object, and invalidate all associated interfaces
    if (mSLPlayer.outputMixObject != NULL) {
        (*mSLPlayer.outputMixObject)->Destroy(mSLPlayer.outputMixObject);
        mSLPlayer.outputMixObject = NULL;
    }

    // destroy engine object, and invalidate all associated interfaces
    if (mSLPlayer.engineObject != NULL) {
        (*mSLPlayer.engineObject)->Destroy(mSLPlayer.engineObject);
        mSLPlayer.engineObject = NULL;
        mSLPlayer.engineEngine = NULL;
    }
}

SLresult AudioPlay::openPlayer() {
    SLresult result;

    // configure audio source
    SLuint32 speakers;
    if(mChannels > 1) {
        speakers = SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT;
    } else {
        speakers = SL_SPEAKER_FRONT_CENTER;
    }
    SLDataLocator_AndroidSimpleBufferQueue loc_bq = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 1};
    SLDataFormat_PCM format_pcm = {SL_DATAFORMAT_PCM, (SLuint32) mChannels,
                                   (SLuint32) (mSampleRate * 1000),
                                   SL_PCMSAMPLEFORMAT_FIXED_16, SL_PCMSAMPLEFORMAT_FIXED_16,
                                   speakers, SL_BYTEORDER_LITTLEENDIAN};
    SLDataSource audioSrc = {&loc_bq, &format_pcm};

    // configure audio sink
    SLDataLocator_OutputMix loc_outmix = {SL_DATALOCATOR_OUTPUTMIX, mSLPlayer.outputMixObject};
    SLDataSink audioSnk = {&loc_outmix, NULL};

    // create audio player
    const SLInterfaceID ids[2] = {SL_IID_ANDROIDSIMPLEBUFFERQUEUE, SL_IID_VOLUME};
    const SLboolean req[2] = {SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE};
    result = (*mSLPlayer.engineEngine)->CreateAudioPlayer(mSLPlayer.engineEngine,
                                                          &(mSLPlayer.playerObject),
                                                          &audioSrc, &audioSnk,
                                                          2, ids, req);
    if(result != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay][PlayOpen] CreateAudioPlayer failed");
        return result;
    }

    // realize the player
    result = (*mSLPlayer.playerObject)->Realize(mSLPlayer.playerObject, SL_BOOLEAN_FALSE);
    if(result != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay][PlayOpen] Realize bqPlayerObject failed");
        return result;
    }

    // get the play interface
    result = (*mSLPlayer.playerObject)->GetInterface(mSLPlayer.playerObject, SL_IID_PLAY,
                                                     &(mSLPlayer.playerPlay));
    if(result != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay][PlayOpen] GetInterface playerPlay failed");
        return result;
    }

    // get the buffer queue interface
    result = (*mSLPlayer.playerObject)->GetInterface(mSLPlayer.playerObject,
                                                     SL_IID_ANDROIDSIMPLEBUFFERQUEUE,
                                                     &(mSLPlayer.playerBufferQueue));
    if(result != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay][PlayOpen] GetInterface bqPlayerBufferQueue failed");
        return result;
    }

    // register callback on the buffer queue
    result = (*mSLPlayer.playerBufferQueue)->RegisterCallback(mSLPlayer.playerBufferQueue,
                                                              bqPlayerCallback, this);
    if(result != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay][PlayOpen] RegisterCallback failed");
        return result;
    }

    result = (*mSLPlayer.playerObject)->GetInterface(mSLPlayer.playerObject, SL_IID_VOLUME,
                                                     &mSLPlayer.volumeItf);
    if(result != SL_RESULT_SUCCESS) {
        LOGE("[AudioPlay][PlayOpen] GetInterface muteSoloItf failed");
        return result;
    }

    return 0;
}

void AudioPlay::closePlayer() {
    // destroy buffer queue audio player object, and invalidate all associated interfaces
    if (mSLPlayer.playerObject != NULL) {
        (*mSLPlayer.playerObject)->Destroy(mSLPlayer.playerObject);
        mSLPlayer.playerObject = NULL;
        mSLPlayer.playerPlay = NULL;
        mSLPlayer.playerBufferQueue = NULL;
    }
}

SLresult AudioPlay::startPlayer() {
    // set player to playing state
    size_t size = (size_t) (mBufferSamples * mFrameSize);
    memset(mBuffer, 0, size);
    SLresult result = (*mSLPlayer.playerBufferQueue)->Enqueue(mSLPlayer.playerBufferQueue,
                                                              mBuffer, size);
    if (result != SL_RESULT_SUCCESS) {
        LOGE("[start] Enqueue failed:%d", (int) result);
        //return result;
    } else {
        mZeroSamplesEnqueue += mBufferSamples;
    }

    result = (*mSLPlayer.playerPlay)->SetPlayState(mSLPlayer.playerPlay,
                                                   SL_PLAYSTATE_PLAYING);

    if (result != SL_RESULT_SUCCESS) {
        LOGE("[start] SetPlayState failed:%d", (int) result);
    } else {
        mState = STATE_PLAYING;
    }
    mFirstFrame = true;

    return result;
}

SLresult AudioPlay::pausePlayer() {
    SLresult result = (*mSLPlayer.playerPlay)->SetPlayState(mSLPlayer.playerPlay,
                                                            SL_PLAYSTATE_PAUSED);
    if (result != SL_RESULT_SUCCESS) {
        LOGE("[pause] SetRecordState failed:%d", (int) result);
    } else {
        mState = STATE_PAUSE;
    }

    return result;
}

SLresult AudioPlay::resumePlayer() {
	SLresult result = (*mSLPlayer.playerPlay)->SetPlayState(mSLPlayer.playerPlay,
                                                            SL_PLAYSTATE_PLAYING);

    if (result != SL_RESULT_SUCCESS) {
		LOGE("[start] SetRecordState failed:%d", (int) result);
    } else {
		mState = STATE_PLAYING;
    }

    return result;
}

SLresult AudioPlay::mutePlayer(bool mute) {
    if ((mState != STATE_PLAYING) && (mState != STATE_PAUSE)) {
        return SL_RESULT_SUCCESS;
    }

    SLresult result = (*mSLPlayer.volumeItf)->SetMute(mSLPlayer.volumeItf,
                                             mute ? SL_BOOLEAN_TRUE : SL_BOOLEAN_FALSE);
    if (result != SL_RESULT_SUCCESS) {
        LOGE("SetMute %d failed:%d", (int) mute, (int) result);
    }
    return result;
}

int64_t AudioPlay::getPosition() {
    pthread_mutex_lock(&mPlayerLock);
    if ((mState != STATE_PLAYING) && (mState != STATE_PAUSE)) {
        pthread_mutex_unlock(&mPlayerLock);
        return 0;
    }

    SLuint32 slPlayPos = 0;
    SLresult result = (*mSLPlayer.playerPlay)->GetPosition(mSLPlayer.playerPlay, &slPlayPos);
    if (result != SL_RESULT_SUCCESS) {
        LOGE("GetPosition failed:%d", (int) result);
        slPlayPos = 0;
    }
    pthread_mutex_unlock(&mPlayerLock);

    int64_t writtenPos = mSamplesWritten * 1000 / mSampleRate;
    int64_t enqueuePos = mSamplesEnqueue * 1000 / mSampleRate;
    int64_t playPos = (int64_t) slPlayPos - mZeroSamplesEnqueue * 1000 / mSampleRate;

    if (VERBOSE) {
        LOGD("writtenPos %lld enqueuePos %lld slPlayPos %u zeroPos %lld playPos %lld",
             writtenPos, enqueuePos, slPlayPos, mZeroSamplesEnqueue * 1000 / mSampleRate, playPos);
    }

    return enqueuePos;
}

void AudioPlay::release() {
    // release audio engine
    closePlayer();
    destroyEngine();

    // release buffer
    if (mBuffer) {
        free(mBuffer);
        mBuffer = NULL;
    }

    // destroy fifo
    if (mFifoBuffer) {
        audio_utils_fifo_deinit(&mFifo);
        free(mFifoBuffer);
        mFifoBuffer = NULL;
    }
    if (mWriteCond) {
        destroyThreadLock(mWriteCond);
        mWriteCond = NULL;
    }
    mState = STATE_IDLE;
}
