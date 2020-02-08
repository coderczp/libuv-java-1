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
#include "header/jni/com_oracle_libuv_handles_PrepareHandle.h"

class PrepareCallbacks {
private:
  static jclass _prepare_handle_cid;

  static jmethodID _callback_mid;

  JNIEnv* _env;
  jobject _instance;

public:
  static void static_initialize(JNIEnv* env, jclass cls);

  PrepareCallbacks();
  ~PrepareCallbacks();

  void initialize(JNIEnv* env, jobject instance);

  void on_prepare(int status);
  void on_close();
};

typedef enum {
  PREPARE_CALLBACK = 1,
  PREPARE_CLOSE_CALLBACK
} PrepareHandleCallbackType;

jclass PrepareCallbacks::_prepare_handle_cid = NULL;

jmethodID PrepareCallbacks::_callback_mid = NULL;

void PrepareCallbacks::static_initialize(JNIEnv* env, jclass cls) {
  _prepare_handle_cid = (jclass) env->NewGlobalRef(cls);
  assert(_prepare_handle_cid);

  _callback_mid = env->GetMethodID(_prepare_handle_cid, "callback", "(II)V");
  assert(_callback_mid);
}

void PrepareCallbacks::initialize(JNIEnv* env, jobject instance) {
  _env = env;
  assert(_env);
  assert(instance);
  _instance = _env->NewGlobalRef(instance);
}

PrepareCallbacks::PrepareCallbacks() {
  _env = NULL;
}

PrepareCallbacks::~PrepareCallbacks() {
  _env->DeleteGlobalRef(_instance);
}

void PrepareCallbacks::on_prepare(int status) {
  assert(_env);
  _env->CallVoidMethod(
      _instance,
      _callback_mid,
      PREPARE_CALLBACK,
      status);
}

void PrepareCallbacks::on_close() {
  assert(_env);
  _env->CallVoidMethod(
      _instance,
      _callback_mid,
      PREPARE_CLOSE_CALLBACK,
      0);
}

static void _prepare_cb(uv_prepare_t* handle) {
  assert(handle);
  assert(handle->data);
  PrepareCallbacks* cb = reinterpret_cast<PrepareCallbacks*>(handle->data);
  cb->on_prepare(0);
}

static void _close_cb(uv_handle_t* handle) {
  assert(handle);
  assert(handle->data);
  PrepareCallbacks* cb = reinterpret_cast<PrepareCallbacks*>(handle->data);
  cb->on_close();
  delete cb;
  delete handle;
}

JNIEXPORT jlong JNICALL Java_com_oracle_libuv_handles_PrepareHandle__1new
  (JNIEnv *env, jclass cls, jlong loop) {
  assert(loop);
  uv_loop_t* lp = reinterpret_cast<uv_loop_t*>(loop);
  uv_prepare_t* prepare = new uv_prepare_t();
  int r = uv_prepare_init(lp, prepare);
  if (r) {
    ThrowException(env, r, "uv_prepare_init");
  } else {
    prepare->data = new PrepareCallbacks();
  }
  return reinterpret_cast<jlong>(prepare);
}

JNIEXPORT void JNICALL Java_com_oracle_libuv_handles_PrepareHandle__1static_1initialize
  (JNIEnv *env, jclass cls) {
  PrepareCallbacks::static_initialize(env, cls);
}

JNIEXPORT void JNICALL Java_com_oracle_libuv_handles_PrepareHandle__1initialize
  (JNIEnv *env, jobject that, jlong prepare) {
  assert(prepare);
  uv_prepare_t* handle = reinterpret_cast<uv_prepare_t*>(prepare);
  assert(handle->data);
  PrepareCallbacks* cb = reinterpret_cast<PrepareCallbacks*>(handle->data);
  cb->initialize(env, that);
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_handles_PrepareHandle__1start
  (JNIEnv *env, jobject that, jlong prepare) {
  assert(prepare);
  uv_prepare_t* handle = reinterpret_cast<uv_prepare_t*>(prepare);
  int r = uv_prepare_start(handle, _prepare_cb);
  if (r) {
    ThrowException(env, r, "uv_prepare_start");
  }
  return r;
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_handles_PrepareHandle__1stop
  (JNIEnv *env, jobject that, jlong prepare) {
  assert(prepare);
  uv_prepare_t* handle = reinterpret_cast<uv_prepare_t*>(prepare);
  int r = uv_prepare_stop(handle);
  if (r) {
    ThrowException(env, r, "uv_prepare_stop");
  }
  return r;
}

JNIEXPORT void JNICALL Java_com_oracle_libuv_handles_PrepareHandle__1close
  (JNIEnv *env, jobject that, jlong prepare) {
  assert(prepare);
  uv_handle_t* handle = reinterpret_cast<uv_handle_t*>(prepare);
  uv_close(handle, _close_cb);
}
