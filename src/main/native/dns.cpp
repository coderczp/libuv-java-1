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
#include <jni.h>

#include "uv.h"
#include "libuv-java/jni/com_oracle_libuv_DnsHandle.h"

class DnsCallbacks {
private:
  static jclass _dns_handle_cid;

  static jmethodID _callback_mid;

  JNIEnv* _env;
  jobject _instance;

public:
  static void static_initialize(JNIEnv* env, jclass cls);

  DnsCallbacks();
  ~DnsCallbacks();

  void initialize(JNIEnv* env, jobject instance);

  void on_dns(int status);
};

jclass DnsCallbacks::_dns_handle_cid = NULL;

jmethodID DnsCallbacks::_callback_mid = NULL;

void DnsCallbacks::static_initialize(JNIEnv* env, jclass cls) {
	_dns_handle_cid = (jclass)env->NewGlobalRef(cls);
	assert(_dns_handle_cid);

	_callback_mid = env->GetMethodID(_dns_handle_cid, "callback", "(Lcom/oracle/libuv/Address;I)V");
	assert(_callback_mid);
}

void DnsCallbacks::initialize(JNIEnv* env, jobject instance) {
	_env = env;
	assert(_env);
	assert(instance);
	_instance = _env->NewGlobalRef(instance);
}

DnsCallbacks::DnsCallbacks() {
	_env = NULL;
}

DnsCallbacks::~DnsCallbacks() {
	_env->DeleteGlobalRef(_instance);
}

void DnsCallbacks::on_dns(int status) {
	assert(_env);
	_env->CallVoidMethod(
		_instance,
		_callback_mid,
		NULL,
		0);
}

static void cb_getaddrinfo_cb(uv_getaddrinfo_t* req, int status, struct addrinfo* res) {
  assert(req);
  assert(req->data);
  DnsCallbacks* cb = reinterpret_cast<DnsCallbacks*>(req->data);
  cb->on_dns(0);

  if (status == 0) {
	  char addr[17] = { '\0' };
	  uv_ip4_name((struct sockaddr_in*) res->ai_addr, addr, 16);
	  fprintf(stdout, "%s\n", addr);
	  fflush(stdout);
  }
  else {
	  fprintf(stdout, "%s\n", uv_err_name(status));
	  fflush(stdout);
  }

  free(req);
  uv_freeaddrinfo(res);
}

JNIEXPORT void JNICALL Java_com_oracle_libuv_DnsHandle__1initialize(JNIEnv *env, jobject that, jlong dns) {
  assert(dns);
  uv_getaddrinfo_t* handle = reinterpret_cast<uv_getaddrinfo_t*>(dns);
  assert(handle->data);
  DnsCallbacks* cb = reinterpret_cast<DnsCallbacks*>(handle->data);
  cb->initialize(env, that);
}

JNIEXPORT jlong JNICALL Java_com_oracle_libuv_DnsHandle__1new(JNIEnv * env, jclass that, jlong loop) {
  assert(loop);
  uv_loop_t* lp = reinterpret_cast<uv_loop_t*>(loop);
  uv_getaddrinfo_t* req = new uv_getaddrinfo_t();
  req->loop = lp;
  req->data = new DnsCallbacks();
  return reinterpret_cast<jlong>(req);
}

JNIEXPORT void JNICALL Java_com_oracle_libuv_DnsHandle__1static_1initialize(JNIEnv *env, jclass cls) {
  DnsCallbacks::static_initialize(env, cls);
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_DnsHandle__1uv_1getaddrinfo(JNIEnv *env, jobject that, jlong dns, jstring node, jstring service) {
  assert(dns);
  uv_getaddrinfo_t* req = reinterpret_cast<uv_getaddrinfo_t*>(dns);
  const char* c_node = "localhost";
  const char* c_service = "80";
  int r = uv_getaddrinfo(req->loop, req, cb_getaddrinfo_cb, c_node, c_service, NULL);
  return r;
}
