/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_oracle_libuv_DNSHandle */

#ifndef _Included_com_oracle_libuv_DNSHandle
#define _Included_com_oracle_libuv_DNSHandle
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_oracle_libuv_DNSHandle
 * Method:    _initialize
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_oracle_libuv_DNSHandle__1initialize
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_oracle_libuv_DNSHandle
 * Method:    _new
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_oracle_libuv_DNSHandle__1new
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_oracle_libuv_DNSHandle
 * Method:    _static_initialize
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_oracle_libuv_DNSHandle__1static_1initialize
  (JNIEnv *, jclass);

/*
 * Class:     com_oracle_libuv_DNSHandle
 * Method:    _uv_getaddrinfo
 * Signature: (JLjava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_oracle_libuv_DNSHandle__1uv_1getaddrinfo
  (JNIEnv *, jobject, jlong, jstring, jstring);

#ifdef __cplusplus
}
#endif
#endif
