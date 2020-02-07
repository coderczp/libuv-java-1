/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

#include <string.h>
#include <assert.h>
#include <stdlib.h>
#include <jni.h>

#include "uv.h"
#include "header/private/exception.h"
#include "header/private/stream.h"
#include "header/jni/com_oracle_libuv_handles_CheckHandle.h"

class CheckCallbacks {
private:
  static jclass _check_handle_cid;

  static jmethodID _callback_mid;

  JNIEnv* _env;
  jobject _instance;

public:
  static void static_initialize(JNIEnv* env, jclass cls);

  CheckCallbacks();
  ~CheckCallbacks();

  void initialize(JNIEnv* env, jobject instance);

  void on_check(int status);
  void on_close();
};

typedef enum {
  CHECK_CALLBACK = 1,
  CHECK_CLOSE_CALLBACK
} CheckHandleCallbackType;

jclass CheckCallbacks::_check_handle_cid = NULL;

jmethodID CheckCallbacks::_callback_mid = NULL;

void CheckCallbacks::static_initialize(JNIEnv* env, jclass cls) {
  _check_handle_cid = (jclass) env->NewGlobalRef(cls);
  assert(_check_handle_cid);

  _callback_mid = env->GetMethodID(_check_handle_cid, "callback", "(II)V");
  assert(_callback_mid);
}

void CheckCallbacks::initialize(JNIEnv* env, jobject instance) {
  _env = env;
  assert(_env);
  assert(instance);
  _instance = _env->NewGlobalRef(instance);
}

CheckCallbacks::CheckCallbacks() {
  _env = NULL;
}

CheckCallbacks::~CheckCallbacks() {
  _env->DeleteGlobalRef(_instance);
}

void CheckCallbacks::on_check(int status) {
  assert(_env);
  _env->CallVoidMethod(
      _instance,
      _callback_mid,
      CHECK_CALLBACK,
      status);
}

void CheckCallbacks::on_close() {
  assert(_env);
  _env->CallVoidMethod(
      _instance,
      _callback_mid,
      CHECK_CLOSE_CALLBACK,
      0);
}

static void _check_cb(uv_check_t* handle) {
  assert(handle);
  assert(handle->data);
  CheckCallbacks* cb = reinterpret_cast<CheckCallbacks*>(handle->data);
  cb->on_check(0);
}

static void _close_cb(uv_handle_t* handle) {
  assert(handle);
  assert(handle->data);
  CheckCallbacks* cb = reinterpret_cast<CheckCallbacks*>(handle->data);
  cb->on_close();
  delete cb;
  delete handle;
}

JNIEXPORT jlong JNICALL Java_com_oracle_libuv_handles_CheckHandle__1new
  (JNIEnv *env, jclass cls, jlong loop) {
  assert(loop);
  uv_loop_t* lp = reinterpret_cast<uv_loop_t*>(loop);
  uv_check_t* check = new uv_check_t();
  int r = uv_check_init(lp, check);
  if (r) {
    ThrowException(env, r, "uv_check_init");
  } else {
    check->data = new CheckCallbacks();
  }
  return reinterpret_cast<jlong>(check);
}

JNIEXPORT void JNICALL Java_com_oracle_libuv_handles_CheckHandle__1static_1initialize
  (JNIEnv *env, jclass cls) {
  CheckCallbacks::static_initialize(env, cls);
}

JNIEXPORT void JNICALL Java_com_oracle_libuv_handles_CheckHandle__1initialize
  (JNIEnv *env, jobject that, jlong check) {

  assert(check);
  uv_check_t* handle = reinterpret_cast<uv_check_t*>(check);
  assert(handle->data);
  CheckCallbacks* cb = reinterpret_cast<CheckCallbacks*>(handle->data);
  cb->initialize(env, that);
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_handles_CheckHandle__1start
  (JNIEnv *env, jobject that, jlong check) {
  assert(check);
  uv_check_t* handle = reinterpret_cast<uv_check_t*>(check);
  int r = uv_check_start(handle, _check_cb);
  if (r) {
    ThrowException(env, r, "uv_check_start");
  }
  return r;
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_handles_CheckHandle__1stop
  (JNIEnv *env, jobject that, jlong check) {
  assert(check);
  uv_check_t* handle = reinterpret_cast<uv_check_t*>(check);
  int r = uv_check_stop(handle);
  if (r) {
    ThrowException(env, r, "uv_check_stop");
  }
  return r;
}

JNIEXPORT void JNICALL Java_com_oracle_libuv_handles_CheckHandle__1close
  (JNIEnv *env, jobject that, jlong check) {
  assert(check);
  uv_handle_t* handle = reinterpret_cast<uv_handle_t*>(check);
  uv_close(handle, _close_cb);
}
