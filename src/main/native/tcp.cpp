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

#include <assert.h>

#include "uv.h"
#include "libuv-java/private/exception.h"
#include "libuv-java/private/stream.h"
#include "libuv-java/private/context.h"
#include "libuv-java/jni/com_oracle_libuv_TCPHandle.h"

static void _tcp_connect_cb(uv_connect_t* req, int status) {
  assert(req);
  assert(req->data);
  assert(req->handle);
  assert(req->handle->data);
  StreamCallbacks* cb = reinterpret_cast<StreamCallbacks*>(req->handle->data);
  ContextHolder* req_data = reinterpret_cast<ContextHolder*>(req->data);
  cb->on_connect(status, status < 0 ? status : 0, req_data->context());
  delete req;
  delete req_data;
}

JNIEXPORT jlong JNICALL Java_com_oracle_libuv_TCPHandle__1new
  (JNIEnv *env, jclass cls, jlong loop) {
  assert(loop);
  uv_tcp_t* tcp = new uv_tcp_t();
  uv_loop_t* lp = reinterpret_cast<uv_loop_t*>(loop);
  int r = uv_tcp_init(lp, tcp);
  if (r) {
    ThrowException(env, r, "uv_tcp_init");
    return (jlong) NULL;
  }
  assert(tcp);
  tcp->data = new StreamCallbacks();
  return reinterpret_cast<jlong>(tcp);
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_TCPHandle__1bind
  (JNIEnv *env, jobject that, jlong tcp, jstring host, jint port, jboolean ipv6, jint flags) {

  assert(tcp);
  uv_tcp_t* handle = reinterpret_cast<uv_tcp_t*>(tcp);
  const char* h = env->GetStringUTFChars(host, 0);
  sockaddr_in addrv4;
  sockaddr_in6 addrv6;
  if (ipv6 == JNI_TRUE) {
	  uv_ip6_addr(h, port, &addrv6);
  } else {
	  uv_ip4_addr(h, port, &addrv4);
  }
  const sockaddr* addr = (ipv6 == JNI_TRUE) ? (const struct sockaddr*) &addrv6 : (const struct sockaddr*) &addrv4;
  int r = uv_tcp_bind(handle, addr, (unsigned int) flags);
  if (r) {
    ThrowException(env, r, "uv_tcp_bind", h);
  }
  env->ReleaseStringUTFChars(host, h);
  return r;
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_TCPHandle__1connect
  (JNIEnv *env, jobject that, jlong tcp, jstring host, jint port, jobject context, jboolean ipv6) {
  assert(tcp);
  uv_tcp_t* handle = reinterpret_cast<uv_tcp_t*>(tcp);
  const char* h = env->GetStringUTFChars(host, 0);
  sockaddr_in addrv4;
  sockaddr_in6 addrv6;
  if (ipv6 == JNI_TRUE) {
	  uv_ip6_addr(h, port, &addrv6);
  } else {
	  uv_ip4_addr(h, port, &addrv4);
  }
  uv_connect_t* req = new uv_connect_t();
  req->handle = reinterpret_cast<uv_stream_t*>(handle);
  ContextHolder* req_data = new ContextHolder(env, context);
  req->data = req_data;
  const sockaddr* addr = (ipv6 == JNI_TRUE) ? (const struct sockaddr*) &addrv6 : (const struct sockaddr*) &addrv4;
  int r = uv_tcp_connect(req, handle, addr, _tcp_connect_cb);
  if (r) {
    delete req_data;
    delete req;
    ThrowException(env, r, "uv_tcp_connect", h);
  }
  env->ReleaseStringUTFChars(host, h);
  return r;
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_TCPHandle__1open
  (JNIEnv *env, jobject that, jlong tcp, jlong socket) {
  assert(tcp);
  uv_tcp_t* handle = reinterpret_cast<uv_tcp_t*>(tcp);
  int r = uv_tcp_open(handle, (uv_os_sock_t) socket);
  if (r) {
    ThrowException(env, r, "uv_tcp_open");
  }
  return r;
}

JNIEXPORT jobject JNICALL Java_com_oracle_libuv_TCPHandle__1socket_1name
  (JNIEnv *env, jobject that, jlong tcp) {

  assert(tcp);
  uv_tcp_t* handle = reinterpret_cast<uv_tcp_t*>(tcp);

  struct sockaddr_storage address;
  int addrlen = sizeof(address);
  int r = uv_tcp_getsockname(handle,
                             reinterpret_cast<sockaddr*>(&address),
                             &addrlen);
  if (r) {
    ThrowException(env, r, "uv_tcp_getsockname");
    return NULL;
  }
  const sockaddr* addr = reinterpret_cast<const sockaddr*>(&address);
  return StreamCallbacks::_address_to_js(env, addr);
}

JNIEXPORT jobject JNICALL Java_com_oracle_libuv_TCPHandle__1peer_1name
  (JNIEnv *env, jobject that, jlong tcp) {
  assert(tcp);
  uv_tcp_t* handle = reinterpret_cast<uv_tcp_t*>(tcp);

  struct sockaddr_storage address;
  int addrlen = sizeof(address);
  int r = uv_tcp_getpeername(handle,
                             reinterpret_cast<sockaddr*>(&address),
                             &addrlen);
  if (r) {
    ThrowException(env, r, "uv_tcp_getpeername");
    return NULL;
  }
  const sockaddr* addr = reinterpret_cast<const sockaddr*>(&address);
  return StreamCallbacks::_address_to_js(env, addr);
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_TCPHandle__1no_1delay
  (JNIEnv *env, jobject that, jlong tcp, jint enable) {

  assert(tcp);
  uv_tcp_t* handle = reinterpret_cast<uv_tcp_t*>(tcp);

  int r = uv_tcp_nodelay(handle, enable);
  if (r) {
    ThrowException(env, r, "uv_tcp_nodelay");
  }
  return r;
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_TCPHandle__1keep_1alive
  (JNIEnv *env, jobject that, jlong tcp, jint enable, jint delay) {

  assert(tcp);
  uv_tcp_t* handle = reinterpret_cast<uv_tcp_t*>(tcp);

  int r = uv_tcp_keepalive(handle, enable, delay);
  if (r) {
    ThrowException(env, r, "uv_tcp_keepalive");
  }
  return r;
}

JNIEXPORT jint JNICALL Java_com_oracle_libuv_TCPHandle__1simultaneous_1accepts
  (JNIEnv *env, jobject that, jlong tcp, jint enable) {

  assert(tcp);
  uv_tcp_t* handle = reinterpret_cast<uv_tcp_t*>(tcp);

  int r = uv_tcp_simultaneous_accepts(handle, enable);
  if (r) {
    // TODO: Node.js as of v0.10.23 ignores the error.
    // ThrowException(env, handle->loop, "uv_tcp_simultaneous_accepts");
  }
  return r;
}
