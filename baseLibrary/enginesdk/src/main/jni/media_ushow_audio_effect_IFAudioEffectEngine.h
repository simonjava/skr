/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class media_ushow_audio_effect_IFAudioEffectEngine */

#ifndef _Included_media_ushow_audio_effect_IFAudioEffectEngine
#define _Included_media_ushow_audio_effect_IFAudioEffectEngine
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     media_ushow_audio_effect_IFAudioEffectEngine
 * Method:    initAudioEffect
 * Signature: (Lcom/changba/songstudio/audioeffect/AudioEffect;)V
 */
JNIEXPORT void JNICALL Java_media_ushow_audio_1effect_IFAudioEffectEngine_initAudioEffect
  (JNIEnv *, jobject, jobject);

/*
 * Class:     media_ushow_audio_effect_IFAudioEffectEngine
 * Method:    setAudioEffect
 * Signature: (Lcom/changba/songstudio/audioeffect/AudioEffect;)V
 */
JNIEXPORT void JNICALL Java_media_ushow_audio_1effect_IFAudioEffectEngine_setAudioEffect
  (JNIEnv *, jobject, jobject);

/*
 * Class:     media_ushow_audio_effect_IFAudioEffectEngine
 * Method:    processAudioFrames
 * Signature: ([BIIII)V
 */
JNIEXPORT void JNICALL Java_media_ushow_audio_1effect_IFAudioEffectEngine_processAudioFrames
  (JNIEnv *, jobject, jbyteArray, jint, jint, jint, jint);

JNIEXPORT void JNICALL Java_media_ushow_audio_1effect_IFAudioEffectEngine_destroyAudioEffect
        (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
