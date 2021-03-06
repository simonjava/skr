//
// Created by 乐 程 on 15/03/2017.
//

#include <include/log.h>
#include <typeinfo>
#include "AudioFilterBase.h"
#include "thread_util.h"

#undef LOG_TAG
#define LOG_TAG "AudioFilterBase"

AudioBase::AudioBase() :
        mFilterIdx(0),
        mFilterInited(false),
        mFilter(NULL) {
    pthread_mutex_init(&mFilterLock, NULL);
}

AudioBase::~AudioBase() {
    pthread_mutex_destroy(&mFilterLock);
}

AudioFilterBase* AudioBase::setFilter(int idx, AudioFilterBase *filter) {
    AudioFilterBase* lastFilter = mFilter;
    pthread_mutex_lock(&mFilterLock);
    mFilterIdx = idx;
    mFilter = filter;
    mFilterInited = false;
    pthread_mutex_unlock(&mFilterLock);
    return lastFilter;
}

AudioFilterBase* AudioBase::getFilter() {
    return mFilter;
}

AudioFilterBase::AudioFilterBase() :
        mStopped(false),
        mFrameSize(0),
        mFifoBuffer(NULL),
        mFifoSize(0) {
    pthread_mutex_init(&mReadLock, NULL);
    pthread_cond_init(&mReadCond, NULL);
}

AudioFilterBase::~AudioFilterBase() {
    destroyFifo();
    pthread_cond_destroy(&mReadCond);
    pthread_mutex_destroy(&mReadLock);
}

void AudioFilterBase::initFifo(int sampleFmt, int sampleRate, int channels) {
    pthread_mutex_lock(&mReadLock);
    if (mFifoBuffer) {
        pthread_mutex_unlock(&mReadLock);
        return;
    }
    mFrameSize = channels * getBytesPerSample(sampleFmt);
    mFifoSize = sampleRate * 300 / 1000;     // 300ms
    mFifoBuffer = (uint8_t *) malloc((size_t) (mFrameSize * mFifoSize));
    audio_utils_fifo_init(&mFifo, (size_t) mFifoSize, (size_t) mFrameSize, mFifoBuffer);
    pthread_mutex_unlock(&mReadLock);
}

void AudioFilterBase::destroyFifo() {
    pthread_mutex_lock(&mReadLock);
    if (mFifoBuffer) {
        audio_utils_fifo_deinit(&mFifo);
        free(mFifoBuffer);
        mFifoBuffer = NULL;
    }
    pthread_cond_signal(&mReadCond);
    pthread_mutex_unlock(&mReadLock);
}

/**
 * Attach this filter to dst module
 *
 * @param dst dst audio module to attach to
 */
void AudioFilterBase::attachTo(int idx, AudioBase* dst, bool detach) {
    if (dst) {
        if (!detach) {
            dst->setFilter(idx, this);
        } else if (dst->getFilter() == this) {
            dst->setFilter(idx, NULL);
        }
    }
    if (detach) {
        pthread_mutex_lock(&mReadLock);
        mStopped = true;
        pthread_cond_signal(&mReadCond);
        if (mFifoBuffer) {
            audio_utils_fifo_flush(&mFifo);
        }
        pthread_mutex_unlock(&mReadLock);
    }
}

int AudioFilterBase::read(uint8_t* buf, int size) {
    pthread_mutex_lock(&mReadLock);
    if (mFifoBuffer == NULL || mStopped) {
        pthread_mutex_unlock(&mReadLock);
        return 0;
    }

    int count = size / mFrameSize;
    int read = audio_utils_fifo_read(&mFifo, buf, (size_t) count);
    while (read < count) {
        pthread_cond_wait(&mReadCond, &mReadLock);
        if (mStopped) {
            LOGD("read aborted!");
            break;
        }
        read += audio_utils_fifo_read(&mFifo, (buf + read * mFrameSize),
                                      (size_t) (count - read));
    }
    pthread_mutex_unlock(&mReadLock);
    return read * mFrameSize;
}

int AudioFilterBase::filterInit(int sampleFmt, int sampleRate, int channels, int bufferSamples) {
    // init fifo for java read
    initFifo(sampleFmt, sampleRate, channels);
    mStopped = false;

    int result = 0;
    pthread_mutex_lock(&mFilterLock);
    if (mFilter && !mFilterInited) {
        result = mFilter->init(mFilterIdx, sampleFmt, sampleRate, channels, bufferSamples);
        mFilterInited = true;
    }
    pthread_mutex_unlock(&mFilterLock);
    return result;
}

int AudioFilterBase::filterProcess(int sampleFmt, int sampleRate, int channels,
                                   int bufferSamples, uint8_t* inBuf, int inSize) {
    pthread_mutex_lock(&mReadLock);
    if (mFifoBuffer && !mStopped) {
        // write fifo
        int count = inSize / mFrameSize;
        int write = audio_utils_fifo_write(&mFifo, inBuf, (size_t) count);
        if (write < count) {
            LOGW("%s Fifo full, try to write %d, written %d", typeid(*this).name(),
                 inSize, write * mFrameSize);
        }
        if (write > 0) {
            pthread_cond_signal(&mReadCond);
        }
    }
    pthread_mutex_unlock(&mReadLock);

    int result = 0;
    pthread_mutex_lock(&mFilterLock);
    if (mFilter) {
        if (!mFilterInited) {
            mFilter->init(mFilterIdx, sampleFmt, sampleRate, channels, bufferSamples);
            mFilterInited = true;
        }
        result = mFilter->process(mFilterIdx, inBuf, inSize);
    }
    pthread_mutex_unlock(&mFilterLock);
    return result;
}
