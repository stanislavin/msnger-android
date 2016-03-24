//
// Created by Stanislav Slavin on 24/03/16.
//

#include <android/log.h>

#include "com_stanislavin_msnger_JNIMsnger.h"

#define LOGI(TAG,...) __android_log_print(ANDROID_LOG_INFO   , TAG,__VA_ARGS__)

JNIEXPORT void JNICALL Java_com_stanislavin_msnger_JNIMsnger_sendMessage
(JNIEnv *env, jobject object, jcharArray number, jcharArray message, jdouble lat, jdouble lon)
{
    LOGI("JNI", "sendMessage called");
}