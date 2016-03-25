LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := curl
LOCAL_SRC_FILES := ../jniLibs/$(TARGET_ARCH_ABI)/libcurl.a
LOCAL_EXPORT_C_INCLUDES := jni/curl

include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE := msnger-jni

CORE_SRC_DIR := ../../../../../msnger-core
LOCAL_SRC_FILES := \
$(CORE_SRC_DIR)/message/Message.cpp \
$(CORE_SRC_DIR)/message/Messenger.cpp \
$(CORE_SRC_DIR)/wrapper/Msnger.cpp \
$(CORE_SRC_DIR)/gis/GisTransaction.cpp \
$(CORE_SRC_DIR)/infobip/InfobipTransaction.cpp \
$(CORE_SRC_DIR)/http/HttpClient.cpp \
$(CORE_SRC_DIR)/jsmn/jsmn.c \
$(CORE_SRC_DIR)/jsmn/jsmntools.c \
$(CORE_SRC_DIR)/core/MessageLoop.cpp \
com_stanislavin_msnger_JNIMsnger.c

CORE_INC_DIR := ../../../../msnger-core
LOCAL_C_INCLUDES := \
$(CORE_INC_DIR)/message \
$(CORE_INC_DIR)/wrapper \
$(CORE_INC_DIR)/gis \
$(CORE_INC_DIR)/infobip \
$(CORE_INC_DIR)/http \
$(CORE_INC_DIR)/jsmn \
$(CORE_INC_DIR)/core \
$(CORE_INC_DIR) \
jni

LOCAL_STATIC_LIBRARIES += curl
LOCAL_LDLIBS := -lz -latomic -llog
LOCAL_CPPFLAGS := -DMSNGER_ANDROID=1

include $(BUILD_SHARED_LIBRARY)