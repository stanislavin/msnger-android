//
// Created by Stanislav Slavin on 24/03/16.
//

#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>

#include "com_stanislavin_msnger_JNIMsnger.h"
#include "Msnger.h"

#define LOGI(TAG,...) __android_log_print(ANDROID_LOG_INFO, TAG,__VA_ARGS__)

struct ReceiverEntry;
typedef struct ReceiverEntry
{
    jobject object;
    struct ReceiverEntry* next;
} ReceiverEntry;

Msnger         gMsnger = NULL;
ReceiverEntry* gReceivers = NULL;
JavaVM*        gJvm = NULL;

void popFrontReceiverInvokeCallback(int code)
{
    JNIEnv *env;
    jobject object;
    jmethodID callback;
    jclass class;

    ReceiverEntry* e = gReceivers;
    gReceivers = gReceivers->next;
    object = e->object;
    free(e);

    if (gJvm == NULL) return;
    (*gJvm)->AttachCurrentThread(gJvm, &env, NULL );
    if (NULL == (class = (*env)->GetObjectClass(env, object)))
    {
        LOGI("JNI", "class == NULL");
    }
    else if(NULL == (callback = (*env)->GetMethodID(env, class, "onMessageSent", "(I)V")))
    {
        LOGI("JNI", "callback == NULL");
    }
    else
    {
        (*env)->CallVoidMethod(env, object, callback, code);
    }
    (*env)->DeleteGlobalRef(env, object);
    (*gJvm)->DetachCurrentThread(gJvm);
}

void addReceiver(JNIEnv* env, jobject jobject)
{
    ReceiverEntry *cur = NULL;
    ReceiverEntry* e = calloc(1,sizeof(ReceiverEntry));
    e->object = (*env)->NewGlobalRef(env, jobject);
    e->next = NULL;

    if (gReceivers == NULL)
    {
        gReceivers = e;
        return;
    }
    cur = gReceivers;
    while (cur->next != NULL)
    {
        cur = cur->next;
    }
    cur->next = e;
}

/*
 * Class:     com_stanislavin_msnger_JNIMsnger
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_stanislavin_msnger_JNIMsnger_init
(JNIEnv *env, jobject object)
{
    LOGI("JNI", "init()");
    if (gMsnger == NULL)
    {
        gMsnger = createMsnger();
    }
    (*env)->GetJavaVM(env, &gJvm);
}

/*
 * Class:     com_stanislavin_msnger_JNIMsnger
 * Method:    deinit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_stanislavin_msnger_JNIMsnger_deinit
(JNIEnv *env, jobject object)
{
    LOGI("JNI", "deinit()");
    if (gMsnger != NULL)
    {
        releaseMsnger(gMsnger);
        gMsnger = NULL;
    }
    while (gReceivers != NULL)
    {
        ReceiverEntry* e = gReceivers;
        gReceivers = gReceivers->next;
        free(e);
    }
}

void onMessageSentByCore(int code)
{
    popFrontReceiverInvokeCallback(code);
}

/*
 * Class:     com_stanislavin_msnger_JNIMsnger
 * Method:    sendMessage
 * Signature: ([B[BDD)V
 */
JNIEXPORT void JNICALL Java_com_stanislavin_msnger_JNIMsnger_sendMessage
(JNIEnv *env, jobject object, jbyteArray number, jbyteArray message, jdouble lat, jdouble lon)
{
    char *n, *m;
    n = (*env)->GetByteArrayElements(env, number, NULL);
    m = (*env)->GetByteArrayElements(env, message, NULL);
    LOGI("JNI", "sendMessage called: %s %s", n, m);
    if (gMsnger != NULL)
    {
        addReceiver(env, object);
        sendMessage(gMsnger, n, m, lat, lon, onMessageSentByCore);
    }
    (*env)->ReleaseByteArrayElements(env, number, n, 0);
    (*env)->ReleaseByteArrayElements(env, message, m, 0);
}