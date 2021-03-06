/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <assert.h>
#include "android_nio_utils.h"

struct NioJNIData {
    jclass nioAccessClass;

    jmethodID getBasePointerID;
    jmethodID getBaseArrayID;
    jmethodID getBaseArrayOffsetID;
};

static NioJNIData gNioJNI;

void* nio_getPointer(JNIEnv *_env, jobject buffer, jarray *array) {
    assert(array);

    jlong pointer;
    jint offset;
    void *data;

    if (buffer == NULL) {
        return NULL;
    }

    pointer = _env->CallStaticLongMethod(gNioJNI.nioAccessClass,
                                         gNioJNI.getBasePointerID, buffer);
    if (pointer != 0L) {
        *array = NULL;
        return reinterpret_cast<void *>(pointer);
    }

    *array = (jarray) _env->CallStaticObjectMethod(gNioJNI.nioAccessClass,
                                                   gNioJNI.getBaseArrayID, buffer);
    offset = _env->CallStaticIntMethod(gNioJNI.nioAccessClass,
                                       gNioJNI.getBaseArrayOffsetID, buffer);
    data = _env->GetPrimitiveArrayCritical(*array, (jboolean *) 0);

    return (void *) ((char *) data + offset);
}


void nio_releasePointer(JNIEnv *_env, jarray array, void *data,
                                 jboolean commit) {
    _env->ReleasePrimitiveArrayCritical(array, data,
                                        commit ? 0 : JNI_ABORT);
}

///////////////////////////////////////////////////////////////////////////////

AutoBufferPointer::AutoBufferPointer(JNIEnv* env, jobject nioBuffer,
                                              jboolean commit) {
    fEnv = env;
    fCommit = commit;
    fPointer = nio_getPointer(env, nioBuffer, &fArray);
}

AutoBufferPointer::~AutoBufferPointer() {
    if (NULL != fArray) {
        nio_releasePointer(fEnv, fArray, fPointer, fCommit);
    }
}

///////////////////////////////////////////////////////////////////////////////

int register_android_nio_utils(JNIEnv* env) {
    jclass localClass = env->FindClass("java/nio/NIOAccess");
    gNioJNI.getBasePointerID = env->GetStaticMethodID(localClass, "getBasePointer", "(Ljava/nio/Buffer;)J");
    gNioJNI.getBaseArrayID = env->GetStaticMethodID(localClass, "getBaseArray", "(Ljava/nio/Buffer;)Ljava/lang/Object;");
    gNioJNI.getBaseArrayOffsetID = env->GetStaticMethodID(localClass, "getBaseArrayOffset", "(Ljava/nio/Buffer;)I");

    // now record a permanent version of the class ID
    gNioJNI.nioAccessClass = static_cast<jclass>(env->NewGlobalRef(localClass));

    return 0;
}
