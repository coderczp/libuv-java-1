/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_oracle_libuv_handles_TimerHandle */

#ifndef _Included_com_oracle_libuv_handles_TimerHandle
#define _Included_com_oracle_libuv_handles_TimerHandle
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_oracle_libuv_handles_TimerHandle
 * Method:    _new
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_oracle_libuv_handles_TimerHandle__1new
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_oracle_libuv_handles_TimerHandle
 * Method:    _now
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_oracle_libuv_handles_TimerHandle__1now
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_oracle_libuv_handles_TimerHandle
 * Method:    _static_initialize
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_oracle_libuv_handles_TimerHandle__1static_1initialize
  (JNIEnv *, jclass);

/*
 * Class:     com_oracle_libuv_handles_TimerHandle
 * Method:    _initialize
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_oracle_libuv_handles_TimerHandle__1initialize
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_oracle_libuv_handles_TimerHandle
 * Method:    _start
 * Signature: (JJJ)I
 */
JNIEXPORT jint JNICALL Java_com_oracle_libuv_handles_TimerHandle__1start
  (JNIEnv *, jobject, jlong, jlong, jlong);

/*
 * Class:     com_oracle_libuv_handles_TimerHandle
 * Method:    _again
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_oracle_libuv_handles_TimerHandle__1again
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_oracle_libuv_handles_TimerHandle
 * Method:    _get_repeat
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_oracle_libuv_handles_TimerHandle__1get_1repeat
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_oracle_libuv_handles_TimerHandle
 * Method:    _set_repeat
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_oracle_libuv_handles_TimerHandle__1set_1repeat
  (JNIEnv *, jobject, jlong, jlong);

/*
 * Class:     com_oracle_libuv_handles_TimerHandle
 * Method:    _stop
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_oracle_libuv_handles_TimerHandle__1stop
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_oracle_libuv_handles_TimerHandle
 * Method:    _close
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_oracle_libuv_handles_TimerHandle__1close
  (JNIEnv *, jobject, jlong);

#ifdef __cplusplus
}
#endif
#endif
