#include <stdint.h>
#include <include/android_nio_utils.h>
#include "jni_AudioPlay.h"
#include "audio/AudioPlay.h"

static inline AudioPlay* getInstance(jlong ptr) {
    return (AudioPlay*)(intptr_t) ptr;
}

jlong Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1init
        (JNIEnv *env, jobject obj) {
    AudioPlay* thiz = new AudioPlay();
    return (jlong)(intptr_t) thiz;
}

int Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1config
        (JNIEnv *env, jobject obj, jlong instance, jint sampleFmt, jint sampleRate, jint channels,
         jint bufferSamples, jint fifoSizeInMs) {
    return getInstance(instance)->config(sampleFmt, sampleRate, channels, bufferSamples, fifoSizeInMs);
}

void Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1attachTo
        (JNIEnv *env, jobject obj, jlong instance, jint idx, jlong ptr, jboolean detach) {
    getInstance(instance)->attachTo(idx, (AudioFilterBase*)(intptr_t) ptr, detach);
}

void Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1setTuneLatency
        (JNIEnv *env, jobject obj, jlong instance, jboolean tuneLatency) {
    getInstance(instance)->setTuneLatency(tuneLatency);
}

void Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1setMute
        (JNIEnv *env, jobject obj, jlong instance, jboolean mute) {
    getInstance(instance)->setMute(mute);
}

void Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1setVolume
        (JNIEnv *env, jobject obj, jlong instance, jfloat volume) {
    getInstance(instance)->setVolume(volume);
}

jlong Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1getPosition
        (JNIEnv *env, jobject obj, jlong instance) {
    return getInstance(instance)->getPosition();
}

jint Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1start
        (JNIEnv *env, jobject obj, jlong instance) {
    return getInstance(instance)->start();
}

jint Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1stop
        (JNIEnv *env, jobject obj, jlong instance) {
    return getInstance(instance)->stop();
}

jint Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1pause
        (JNIEnv *env, jobject obj, jlong instance) {
    return getInstance(instance)->pause();
}

jint Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1resume
        (JNIEnv *env, jobject obj, jlong instance) {
    return getInstance(instance)->resume();
}

jint JNICALL Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1read
        (JNIEnv *env, jobject obj, jlong instance, jobject byteBuffer, jint size) {
    if (byteBuffer == NULL) {
        return 0;
    }
    AutoBufferPointer abp(env, byteBuffer, JNI_TRUE);
    uint8_t *buf = (uint8_t*)abp.pointer();
    return getInstance(instance)->read(buf, size);
}

jint Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1write
        (JNIEnv *env, jobject obj, jlong instance, jobject byteBuffer,
         jint size, jboolean nonBlock) {
    AutoBufferPointer abp(env, byteBuffer, JNI_TRUE);
    uint8_t *buf = (uint8_t*)abp.pointer();
    return getInstance(instance)->write(buf, size, nonBlock);
}

void Java_com_zq_mediaengine_filter_audio_AudioSLPlayer__1release
        (JNIEnv *env, jobject obj, jlong instance) {
    delete getInstance(instance);
}