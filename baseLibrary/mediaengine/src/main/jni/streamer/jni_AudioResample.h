/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_zq_mediaengine_filter_audio_AudioResample */

#ifndef _Included_com_zq_mediaengine_filter_audio_AudioResample
#define _Included_com_zq_mediaengine_filter_audio_AudioResample
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_zq_mediaengine_filter_audio_AudioResample
 * Method:    _init
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_zq_mediaengine_filter_audio_AudioResample__1init
        (JNIEnv *, jobject);

/*
 * Class:     com_zq_mediaengine_filter_audio_AudioResample
 * Method:    _setOutputFormat
 * Signature: (JIII)V
 */
JNIEXPORT void JNICALL Java_com_zq_mediaengine_filter_audio_AudioResample__1setOutputFormat
        (JNIEnv *, jobject, jlong, jint, jint, jint, jboolean);

/*
 * Class:     com_zq_mediaengine_filter_audio_AudioResample
 * Method:    _config
 * Signature: (JII)I
 */
JNIEXPORT jint JNICALL Java_com_zq_mediaengine_filter_audio_AudioResample__1config
        (JNIEnv *, jobject, jlong, jint, jint, jint);

/*
 * Class:     com_zq_mediaengine_filter_audio_AudioResample
 * Method:    _attachTo
 * Signature: (JIJZ)V
 */
JNIEXPORT void JNICALL Java_com_zq_mediaengine_filter_audio_AudioResample__1attachTo
        (JNIEnv *, jobject, jlong, jint, jlong, jboolean);

/*
 * Class:     com_zq_mediaengine_filter_audio_AudioResample
 * Method:    _read
 * Signature: (JLjava/nio/ByteBuffer;I)I
 */
JNIEXPORT jint JNICALL Java_com_zq_mediaengine_filter_audio_AudioResample__1read
        (JNIEnv *, jobject, jlong, jobject, jint);

/*
 * Class:     com_zq_mediaengine_filter_audio_AudioResample
 * Method:    _resample
 * Signature: (JLjava/nio/ByteBuffer;I)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_com_zq_mediaengine_filter_audio_AudioResample__1resample
        (JNIEnv *, jobject, jlong, jobject, jint);

/*
 * Class:     com_zq_mediaengine_filter_audio_AudioResample
 * Method:    _release
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_zq_mediaengine_filter_audio_AudioResample__1release
        (JNIEnv *, jobject, jlong);

#ifdef __cplusplus
}
#endif
#endif
