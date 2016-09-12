
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := fastdfsuploader
LOCAL_SRC_FILES := fastdfs_uploader.c

LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog -lz -lm

LOCAL_C_INCLUDES := -L$(SYSROOT)/usr/include

include $(BUILD_SHARED_LIBRARY)