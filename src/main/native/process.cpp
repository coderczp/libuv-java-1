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
#include <limits.h> /* PATH_MAX */
#include <assert.h>
#include <stdlib.h>
#include <errno.h>

#include "uv.h"
#include "header/private/exception.h"
#include "header/jni/com_oracle_libuv_LibUV.h"

#ifndef ARRAY_SIZE
# define ARRAY_SIZE(a) (sizeof((a)) / sizeof((a)[0]))
#endif


JNIEXPORT jstring JNICALL Java_com_oracle_libuv_LibUV__1exe_1path
  (JNIEnv *env, jclass cls) {

#ifdef _WIN32
  /* MAX_PATH is in characters, not bytes. Make sure we have enough headroom. */
  char buf[MAX_PATH * 4 + 1];
#else
  char buf[PATH_MAX + 1];
#endif

  size_t size = ARRAY_SIZE(buf);
  int r = uv_exepath(buf, &size);
  if (r) {
    ThrowException(env, r, "uv_exepath");
    return NULL;
  }
  return env->NewStringUTF(buf);
}

/*
 * Class:     com_oracle_libuv_LibUV
 * Method:    _cwd
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_oracle_libuv_LibUV__1cwd
  (JNIEnv *env, jclass cls) {

#ifdef _WIN32
  /* MAX_PATH is in characters, not bytes. Make sure we have enough headroom. */
  char buf[MAX_PATH * 4 + 1];
#else
  char buf[PATH_MAX + 1];
#endif

  size_t size = ARRAY_SIZE(buf) - 1;
  int r = uv_cwd(buf, &size);
  if (r) {
    ThrowException(env, r, "uv_cwd");
    return NULL;
  }
  buf[ARRAY_SIZE(buf) - 1] = '\0';
  return env->NewStringUTF(buf);
}

JNIEXPORT void JNICALL Java_com_oracle_libuv_LibUV__1chdir
  (JNIEnv *env, jclass cls, jstring arg) {

  const char* dir = env->GetStringUTFChars(arg, 0);
  int r = uv_chdir(dir);
  if (r) {
    ThrowException(env, r, "uv_chdir", NULL, dir);
  }
  env->ReleaseStringUTFChars(arg, dir);
}

JNIEXPORT jstring JNICALL Java_com_oracle_libuv_LibUV__1getTitle
  (JNIEnv *env, jclass cls) {
  char process_title[4096];
  int r = uv_get_process_title(process_title, 4096);
  return env->NewStringUTF(r == 0 ? process_title : "");
}

JNIEXPORT void JNICALL Java_com_oracle_libuv_LibUV__1setTitle
  (JNIEnv *env, jclass cls, jstring title) {
  const char* t = env->GetStringUTFChars(title, JNI_FALSE);
  uv_set_process_title(t);
  env->ReleaseStringUTFChars(title, t);
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_LibUV__1kill
  (JNIEnv *env, jclass cls, jint pid, jint signal) {
  int err = uv_kill(pid, signal);
  if (err) {
    return err;
  }
  return 0;
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_LibUV__1rss
  (JNIEnv *env, jclass cls) {
  size_t rss;
  int err = uv_resident_set_memory(&rss);
  if (err) {
    ThrowException(env, err, "uv_resident_set_memory", NULL, NULL);
  }
  return static_cast<jint>(rss);
}
