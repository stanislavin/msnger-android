LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := curl
LOCAL_SRC_FILES := ../jniLibs/$(TARGET_ARCH_ABI)/libcurl.a
LOCAL_EXPORT_C_INCLUDES := jni/curl

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := msnger-jni

LOCAL_SRC_FILES := \
message/Message.cpp \
message/Messenger.cpp \
wrapper/Msnger.cpp \
gis/GisTransaction.cpp \
infobip/InfobipTransaction.cpp \
http/HttpClient.cpp \
jsmn/jsmn.c \
jsmn/jsmntools.c \
core/MessageLoop.cpp \
com_stanislavin_msnger_JNIMsnger.c

LOCAL_C_INCLUDES := \
jni/message \
jni/wrapper \
jni/gis \
jni/infobip \
jni/curl \
jni/http \
jni/jsmn \
jni/core \
jni

LOCAL_STATIC_LIBRARIES += curl
LOCAL_LDLIBS := -lz -latomic

include $(BUILD_SHARED_LIBRARY)