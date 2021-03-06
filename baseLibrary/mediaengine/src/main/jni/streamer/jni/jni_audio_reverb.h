/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_zq_mediaengine_filter_audio_AudioReverbWrap */

#ifndef _Included_com_zq_mediaengine_filter_audio_AudioReverbWrap
#define _Included_com_zq_mediaengine_filter_audio_AudioReverbWrap
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_zq_mediaengine_filter_audio_AudioReverbWrap
 * Method:    create
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_zq_mediaengine_filter_audio_AudioReverbWrap_create
        (JNIEnv *, jobject);

/*
 * Class:     com_zq_mediaengine_filter_audio_AudioReverbWrap
 * Method:    config
 * Signature: (JII)V
 */
JNIEXPORT void JNICALL Java_com_zq_mediaengine_filter_audio_AudioReverbWrap_config
        (JNIEnv *, jobject, jlong, jint, jint, jint);

/*
 * Class:     com_zq_mediaengine_filter_audio_AudioReverbWrap
 * Method:    attachTo
 * Signature: (JIJZ)V
 */
JNIEXPORT void JNICALL Java_com_zq_mediaengine_filter_audio_AudioReverbWrap_attachTo
        (JNIEnv *, jobject, jlong, jint, jlong, jboolean);

/*
 * Class:     com_zq_mediaengine_filter_audio_AudioReverbWrap
 * Method:    setLevel
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_com_zq_mediaengine_filter_audio_AudioReverbWrap_setLevel
        (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     com_zq_mediaengine_filter_audio_AudioReverbWrap
 * Method:    read
 * Signature: (JLjava/nio/ByteBuffer;I)I
 */
JNIEXPORT jint JNICALL Java_com_zq_mediaengine_filter_audio_AudioReverbWrap_read
        (JNIEnv *, jobject, jlong, jobject, jint);

/*
 * Class:     com_zq_mediaengine_filter_audio_AudioReverbWrap
 * Method:    process
 * Signature: (JLjava/nio/ByteBuffer;I)I
 */
JNIEXPORT jint JNICALL Java_com_zq_mediaengine_filter_audio_AudioReverbWrap_process
        (JNIEnv *, jobject, jlong, jobject, jint);

/*
 * Class:     com_zq_mediaengine_filter_audio_AudioReverbWrap
 * Method:    delete
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_zq_mediaengine_filter_audio_AudioReverbWrap_delete
        (JNIEnv *, jobject, jlong);

#ifdef __cplusplus
}
#endif
#endif
