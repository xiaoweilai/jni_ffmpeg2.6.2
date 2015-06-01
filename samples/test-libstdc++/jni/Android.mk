# A simple test for the minimal standard C++ library
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := maexec
LOCAL_SRC_FILES := maexec.c
include $(BUILD_EXECUTABLE)
