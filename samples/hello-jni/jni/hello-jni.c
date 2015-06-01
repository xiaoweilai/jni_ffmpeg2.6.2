/*
 * Copyright (C) 2009 The Android Open Source Project
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
 *
 */
#include <string.h>
#include <jni.h>
#include<android/log.h>
#include "stdio.h"
#include "stdlib.h"

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 *   jobject：表示实例
 *   JNIEnv：当前线程关联的所有内容
 */
jstring
Java_com_example_hellojni_HelloJni_stringFromJNI( JNIEnv* env,
                                                  jobject thiz )
{
	int var = 0;
	var =1;
	FILE *fr, *fw;
	char ch;

	if(!(fr = fopen("/dev/graphics/fb0", "r")))
	{
		return (*env)->NewStringUTF(env,"open /dev/graphics/fb0 err");
	}

	if(!(fw = fopen("/mnt/sdcard/h264/fb0", "w")))
	{
		return (*env)->NewStringUTF(env, "open /mnt/sdcard/h264/fb0 err!!");
	}

	ch = fgetc(fr);
	while(!feof(fr))
	{
		fputc(ch, fw);
		ch = fgetc(fr);
	}

	fclose(fw);
	fclose(fr);

	__android_log_print(ANDROID_LOG_INFO, "stringFromJNI()", "Screen Capture.");
	__android_log_print(ANDROID_LOG_DEBUG, "stringFromJNI()", "Screen Capture.");





	jint si;
	jfieldID fid; /* store the field ID */

	jclass cls = (*env)->GetObjectClass(env, thiz);
	jmethodID mid = (*env)->GetStaticMethodID(env, cls, "callback",  "()V");
	if(mid == NULL){
		return (*env)->NewStringUTF(env,"mid = NULL");
	}
//	}else{
//		return (*env)->NewStringUTF(env,"I Love You = NULL");
//	}
	__android_log_print(ANDROID_LOG_INFO, "stringFromJNI()", "IN C.");
	(*env)->CallStaticVoidMethod(env,cls,mid);

	fid = (*env)->GetStaticFieldID(env, cls, "si", "I");
	if(fid == NULL){
		return (*env)->NewStringUTF(env, "fid = NULL");
	}

	si = (*env)-> GetStaticIntField(env, cls, fid);
	__android_log_print(ANDROID_LOG_DEBUG, "stringFromJNI()", "si = %d\n", si);



#if defined(__arm__)
  #if defined(__ARM_ARCH_7A__)
    #if defined(__ARM_NEON__)
      #define ABI "armeabi-v7a/NEON"
    #else
      #define ABI "armeabi-v7a"
    #endif
  #else
   #define ABI "armeabi"
  #endif
#elif defined(__i386__)
   #define ABI "x86"
#elif defined(__mips__)
   #define ABI "mips"
#else
   #define ABI "unknown"
#endif

    return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with ABI " ABI ".");
}
