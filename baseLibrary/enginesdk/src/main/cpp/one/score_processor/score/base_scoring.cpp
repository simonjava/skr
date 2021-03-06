#include "base_scoring.h"
#include <sys/time.h>

#define LOG_TAG "BaseScoring"

#define LOGOPEN 0

BaseScoring::BaseScoring() {
    //LOGI("enter BaseScoring::BaseScoring()");
    UNIT_LEN_IN_MS = 60;
//    currentTimeMills = 0;
    sampleCursor = 0;
    samples = NULL;
    isNeedDestroy = false;
    recordRawQueue = new RecordLevelQueue("record raw Queue");
    recordLevelQueue = new RecordLevelQueue("record level Queue");
    mPushTime = 0;
    mMaxT1 = 0;
    mRearLevelMills = 0;
}

BaseScoring::~BaseScoring() {
    if (NULL != samples) {
        delete[] samples;
        samples = NULL;
    }
}

/**
 * 44100 1 16 512
 */
int BaseScoring::getMinBufferSize(int sampleRate, int channelNum, int bit,
                                  int bufferSizeInShorts) {

    LOGI("sampleRate=%d channelNum=%d bit=%d bufferSizeInShorts=%d",
         sampleRate, channelNum, bit,
         bufferSizeInShorts);
    // 44100 * 16 ／8 * 1
    int mByteCntPerSecond = sampleRate * bit / 8 * channelNum;// 采样率乘以采样位数 等于每秒的采样位数
    // mByteCntPerSecond = 11025 个字节
    mShortCntPerMs = mByteCntPerSecond / (float) 2000.0; // 每毫秒的 short 个数 44.099998
    int cntPerUnit = (int) (mShortCntPerMs * UNIT_LEN_IN_MS); // 最少40毫秒
    //  cntPerUnit=1764 mShortCntPerMs=44.099998 UNIT_LEN_IN_MS=40
    LOGI("cntPerUnit=%d mShortCntPerMs=%f UNIT_LEN_IN_MS=%d", cntPerUnit, mShortCntPerMs,
         UNIT_LEN_IN_MS);
    int mActualBufferSize = sampleRate
                            / 10 + MAX(cntPerUnit, bufferSizeInShorts);
    //mActualBufferSize=6174
    LOGI("mActualBufferSize=%d", mActualBufferSize);
    return mActualBufferSize;
}

//
int BaseScoring::getLatency(long currentTimeMills) {
    int latency = 0;
    if (!isNeedDestroy) {
        latency = currentTimeMills - mRearLevelMills;
        long t = getRealTime();
        long t1 = currentTimeMills - ((t - mPushTime) + mRearLevelMills);
        if (mPushTime == 0) { //表示还没有psuh数据
            return mMaxT1;
        }
        if (t1 > mMaxT1) {
            mMaxT1 = t1;
        }
    }
    //LOGI("mMaxT1 is %ld  latency is %d", mMaxT1,latency);
    return mMaxT1;
}

long BaseScoring::getCurTime() {
    long t = getRealTime();
    return (t - mPushTime) + recordLevelQueue->getRear()->getTimeMills();
}

long BaseScoring::getRealTime() {
    struct timeval tv;
    gettimeofday(&tv, NULL);
    long t = tv.tv_sec * 1000 + tv.tv_usec / 1000;
    return t;
}

// 44100 1 2 /storage/emulated/0/ZQ_LIVE/midi/23f4fb31a48f7826.melp
void BaseScoring::init(int sampleRate, int channelNum, int bit, char *melFilePath) {
    LOGI("enter BaseScoring::init()");

    AUDIO_DATA_SAMPLE_FOR_SCORE = sampleRate * UNIT_LEN_IN_MS / 1000; // 44100/25 = 1764
    isRunning = true;
    pthread_create(&scoringThread, NULL, startScoringThread, this);
    onInit(sampleRate, channelNum, bit, melFilePath);
    LOGI("enter BaseScoring::init() over");
}

void *BaseScoring::startScoringThread(void *ptr) {
    //LOGI("enter BaseScoring::startScoringThread()");
    BaseScoring *scoring = (BaseScoring *) ptr;
    scoring->consumeRecordRawQueue();
    return NULL;
}

void BaseScoring::consumeRecordRawQueue() {
    //LOGI("enter BaseScoring::consumeRecordRawQueue() isRunning=%d", isRunning);
    RecordLevel *recordLevel = NULL;
    while (isRunning) {
        int resultCode = recordRawQueue->poll(&recordLevel, true);
        if (resultCode > 0) {
            if (NULL != recordLevel) {
                //LOGI("running in process ");
                mPushTime = getRealTime();
                mRearLevelMills = recordLevel->getTimeMills();
                processRecordLevel(recordLevel);
                if (NULL != recordLevel) {
                    delete recordLevel;
                    recordLevel = NULL;
                }
            }
        }
    }
}

void BaseScoring::reset() {
    LOGI("BaseScoring::reset()....");
//    currentTimeMills = 0;
    recordRawQueue->flush();
    recordLevelQueue->flush();
}

void BaseScoring::pushSamplesToRecordRawQueue(long endCurrentTimeMills) {
    if(LOGOPEN) {
        LOGI("pushSamplesToRecordRawQueue endCurrentTimeMills=%ld", endCurrentTimeMills);
    }
    RecordLevel *recordLevel = new RecordLevel();
    recordLevel->setTimeMills(endCurrentTimeMills-UNIT_LEN_IN_MS/2);
    recordLevel->setSamples(samples);
    recordRawQueue->push(recordLevel);
//    currentTimeMills += UNIT_LEN_IN_MS;
    sampleCursor = 0;
    samples = NULL;
}

/**
 * 512 512
 * sampleCursor 当前buffer的index
 * */
void BaseScoring::mergeRecordRaw(short *buffer, int bufferSize, long currentTime) {
    if (currentTime == 0) {
        return;
    }
    int bufferCursor = 0;
    // 每次都是512
    if (LOGOPEN) {
        // AUDIO_DATA_SAMPLE_FOR_SCORE = 1764 每 40ms 44100采样率 字节为
        LOGI("mergeRecordRaw currentTime=%ld AUDIO_DATA_SAMPLE_FOR_SCORE=%d sampleCursor=%d ",
             currentTime,
             AUDIO_DATA_SAMPLE_FOR_SCORE,
             sampleCursor
        );
    }
    if (sampleCursor > 0 && NULL != samples) {
        // 处理残留数据
        if (bufferSize + sampleCursor >= AUDIO_DATA_SAMPLE_FOR_SCORE) {
            if (LOGOPEN) {
                LOGI("mergeRecordRaw sampleCursor=%d", sampleCursor);
            }
            int subSampleSize = AUDIO_DATA_SAMPLE_FOR_SCORE - sampleCursor;
            memcpy(samples + sampleCursor, buffer, subSampleSize * 2);
            // 满足一个单位了，丢
            this->pushSamplesToRecordRawQueue(currentTime);
            bufferSize -= subSampleSize;
            bufferCursor += subSampleSize;
            if (bufferSize <= 0) {
                return;
            }
        } else {
            if (LOGOPEN) {
                LOGI("mergeRecordRaw 12 sampleCursor=%d", sampleCursor);
            }
            memcpy(samples + sampleCursor, buffer, bufferSize * 2);
            sampleCursor += bufferSize;
            return;
        }
    }
    if (bufferSize >= AUDIO_DATA_SAMPLE_FOR_SCORE) {
        // 如果传进来的buffer size 大于计算得分的最小单位的大小，则进行拆分
        while (bufferSize > AUDIO_DATA_SAMPLE_FOR_SCORE) {
            if (LOGOPEN) {
                LOGI("mergeRecordRaw 21");
            }
            samples = new short[AUDIO_DATA_SAMPLE_FOR_SCORE];
            memcpy(samples, buffer + bufferCursor, AUDIO_DATA_SAMPLE_FOR_SCORE * 2);
            this->pushSamplesToRecordRawQueue(currentTime);
            bufferSize -= AUDIO_DATA_SAMPLE_FOR_SCORE;
            bufferCursor += AUDIO_DATA_SAMPLE_FOR_SCORE;
        }
        if (bufferSize > 0) {
            samples = new short[AUDIO_DATA_SAMPLE_FOR_SCORE];
            memcpy(samples, buffer + bufferCursor, bufferSize * 2);
            sampleCursor += bufferSize;
        } else {
            sampleCursor = 0;
            samples = NULL;
        }
    } else {
        // 如果传进来的buffer size 小于计算得分的最小单位的大小，则进行等待合并
        samples = new short[AUDIO_DATA_SAMPLE_FOR_SCORE];
        memcpy(samples, buffer + bufferCursor, bufferSize * 2);
        sampleCursor += bufferSize;
    }
}

int BaseScoring::destroy() {
    //LOGI("enter BaseScoring::destroy()");
    //结束计算线程
    isRunning = false;
    //销毁接受源数据的队列
    if (NULL != recordRawQueue) {
        recordRawQueue->abort();
    }
    void *status;
    pthread_join(scoringThread, &status);
    //销毁计算结果队列
    if (NULL != recordLevelQueue) {
        recordLevelQueue->abort();
        delete recordLevelQueue;
        recordLevelQueue = NULL;
    }
    if (NULL != recordRawQueue) {
        delete recordRawQueue;
        recordRawQueue = NULL;
    }
    return 0;
}
