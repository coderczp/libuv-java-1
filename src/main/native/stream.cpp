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
#include <stdlib.h>

#include "uv.h"
#include "throw.h"
#include "stream.h"
#include "udp.h"
#include "net_java_libuv_handles_StreamHandle.h"

jclass StreamCallbacks::_integer_cid = NULL;
jclass StreamCallbacks::_long_cid = NULL;
jclass StreamCallbacks::_object_cid = NULL;
jclass StreamCallbacks::_buffer_cid = NULL;
jclass StreamCallbacks::_address_cid = NULL;
jclass StreamCallbacks::_stream_handle_cid = NULL;

jmethodID StreamCallbacks::_integer_valueof_mid = NULL;
jmethodID StreamCallbacks::_long_valueof_mid = NULL;
jmethodID StreamCallbacks::_buffer_wrap_mid = NULL;
jmethodID StreamCallbacks::_address_init_mid = NULL;
jmethodID StreamCallbacks::_callback_1arg_mid = NULL;
jmethodID StreamCallbacks::_callback_narg_mid = NULL;

JNIEnv* StreamCallbacks::_env = NULL;

void StreamCallbacks::static_initialize(JNIEnv* env, jclass cls) {
  _env = env;
  assert(_env);

  _integer_cid = env->FindClass("java/lang/Integer");
  assert(_integer_cid);
  _integer_cid = (jclass) env->NewGlobalRef(_integer_cid);
  assert(_integer_cid);

  _long_cid = env->FindClass("java/lang/Long");
  assert(_long_cid);
  _long_cid = (jclass) env->NewGlobalRef(_long_cid);
  assert(_long_cid);

  _object_cid = env->FindClass("java/lang/Object");
  assert(_object_cid);
  _object_cid = (jclass) env->NewGlobalRef(_object_cid);
  assert(_object_cid);

  _buffer_cid = env->FindClass("java/nio/ByteBuffer");
  assert(_buffer_cid);
  _buffer_cid = (jclass) env->NewGlobalRef(_buffer_cid);
  assert(_buffer_cid);

  _integer_valueof_mid = env->GetStaticMethodID(_integer_cid, "valueOf", "(I)Ljava/lang/Integer;");
  assert(_integer_valueof_mid);

  _long_valueof_mid = env->GetStaticMethodID(_long_cid, "valueOf", "(J)Ljava/lang/Long;");
  assert(_long_valueof_mid);

  _buffer_wrap_mid = env->GetStaticMethodID(_buffer_cid, "wrap", "([B)Ljava/nio/ByteBuffer;");
  assert(_buffer_wrap_mid);

  _stream_handle_cid = (jclass) env->NewGlobalRef(cls);
  assert(_stream_handle_cid);

  _callback_1arg_mid = env->GetMethodID(_stream_handle_cid, "callback", "(ILjava/lang/Object;)V");
  assert(_callback_1arg_mid);
  _callback_narg_mid = env->GetMethodID(_stream_handle_cid, "callback", "(I[Ljava/lang/Object;)V");
  assert(_callback_narg_mid);

  static_initialize_address(env);
}

void StreamCallbacks::static_initialize_address(JNIEnv* env) {
  if (!_address_cid) {
      _address_cid = env->FindClass("net/java/libuv/handles/Address");
      assert(_address_cid);
      _address_cid = (jclass) env->NewGlobalRef(_address_cid);
      assert(_address_cid);
  }

  if (!_address_init_mid) {
    _address_init_mid = env->GetMethodID(_address_cid, "<init>", "(Ljava/lang/String;ILjava/lang/String;)V");
    assert(_address_init_mid);
  }
}

void StreamCallbacks::initialize(jobject instance) {
  assert(_env);
  assert(instance);
  _instance = _env->NewGlobalRef(instance);
}

StreamCallbacks::StreamCallbacks() {
}

StreamCallbacks::~StreamCallbacks() {
  _env->DeleteGlobalRef(_instance);
}

void StreamCallbacks::throw_exception(int code, const char* syscall) {
  assert(_env);
  ThrowException(_env, code, syscall);
}

void StreamCallbacks::on_read(uv_buf_t* buf, jsize nread) {
  assert(_env);
  if (nread < 0) {
    _env->CallVoidMethod(
        _instance,
        _callback_1arg_mid,
        STREAM_READ_CALLBACK,
        NULL);
  } else if (nread > 0) {
    jbyteArray bytes = _env->NewByteArray(nread);
    _env->SetByteArrayRegion(bytes, 0, nread, reinterpret_cast<signed char const*>(buf->base));
    jobject arg = _env->CallStaticObjectMethod(_buffer_cid, _buffer_wrap_mid, bytes);
    _env->CallVoidMethod(
        _instance,
        _callback_1arg_mid,
        STREAM_READ_CALLBACK,
        arg);
    free(buf->base);
  }
}

void StreamCallbacks::on_read2(uv_buf_t* buf, jsize nread, long ptr, uv_handle_type pending) {
  assert(_env);
  if (nread < 0) {
    _env->CallVoidMethod(
        _instance,
        _callback_1arg_mid,
        STREAM_READ_CALLBACK,
        NULL);
  } else if (nread > 0) {
    jbyteArray bytes = _env->NewByteArray(nread);
    _env->SetByteArrayRegion(bytes, 0, nread, reinterpret_cast<signed char const*>(buf->base));
    jobject array = _env->CallStaticObjectMethod(_buffer_cid, _buffer_wrap_mid, bytes);
    jobject handle = _env->CallStaticObjectMethod(_long_cid, _long_valueof_mid, ptr);
    jobject type = _env->CallStaticObjectMethod(_integer_cid, _integer_valueof_mid, pending);
    jobjectArray args = _env->NewObjectArray(3, _object_cid, 0);
    _env->SetObjectArrayElement(args, 0, array);
    _env->SetObjectArrayElement(args, 1, handle);
    _env->SetObjectArrayElement(args, 2, type);
    _env->CallVoidMethod(
        _instance,
        _callback_narg_mid,
        STREAM_READ_CALLBACK,
        args);
    free(buf->base);
  }
}

void StreamCallbacks::on_write(int status) {
  assert(_env);
  jobject arg = _env->CallStaticObjectMethod(_integer_cid, _integer_valueof_mid, status);
  assert(arg);
  _env->CallVoidMethod(
      _instance,
      _callback_1arg_mid,
      STREAM_WRITE_CALLBACK,
      arg);
}

void StreamCallbacks::on_write(int status, int error_code) {
  assert(_env);
  assert(status < 0);

  jobject status_value = _env->CallStaticObjectMethod(_integer_cid, _integer_valueof_mid, status);
  jthrowable exception = NewException(_env, error_code);
  jobjectArray args = _env->NewObjectArray(2, _object_cid, 0);
  assert(args);
  _env->SetObjectArrayElement(args, 0, status_value);
  _env->SetObjectArrayElement(args, 1, exception);
  _env->CallVoidMethod(
      _instance,
      _callback_narg_mid,
      STREAM_WRITE_CALLBACK,
      args);
}

void StreamCallbacks::on_connect(int status) {
  assert(_env);
  jobject arg = _env->CallStaticObjectMethod(_integer_cid, _integer_valueof_mid, status);
  assert(arg);
  _env->CallVoidMethod(
      _instance,
      _callback_1arg_mid,
      STREAM_CONNECT_CALLBACK,
      arg);
}

void StreamCallbacks::on_connect(int status, int error_code) {
  assert(_env);
  assert(status < 0);

  jobject status_value = _env->CallStaticObjectMethod(_integer_cid, _integer_valueof_mid, status);
  jthrowable exception = NewException(_env, error_code);
  jobjectArray args = _env->NewObjectArray(2, _object_cid, 0);
  assert(args);
  _env->SetObjectArrayElement(args, 0, status_value);
  _env->SetObjectArrayElement(args, 1, exception);
  _env->CallVoidMethod(
      _instance,
      _callback_narg_mid,
      STREAM_CONNECT_CALLBACK,
      args);
}

void StreamCallbacks::on_connection(int status) {
  assert(_env);
  jobject arg = _env->CallStaticObjectMethod(_integer_cid, _integer_valueof_mid, status);
  assert(arg);
  _env->CallVoidMethod(
      _instance,
      _callback_1arg_mid,
      STREAM_CONNECTION_CALLBACK,
      arg);
}

void StreamCallbacks::on_connection(int status, int error_code) {
  assert(_env);
  assert(status < 0);

  jobject status_value = _env->CallStaticObjectMethod(_integer_cid, _integer_valueof_mid, status);
  jthrowable exception = NewException(_env, error_code);
  jobjectArray args = _env->NewObjectArray(2, _object_cid, 0);
  assert(args);
  _env->SetObjectArrayElement(args, 0, status_value);
  _env->SetObjectArrayElement(args, 1, exception);
  _env->CallVoidMethod(
      _instance,
      _callback_narg_mid,
      STREAM_CONNECTION_CALLBACK,
      args);
}

void StreamCallbacks::on_shutdown(int status) {
  assert(_env);
  jobject arg = _env->CallStaticObjectMethod(_integer_cid, _integer_valueof_mid, status);
  assert(arg);
  _env->CallVoidMethod(
      _instance,
      _callback_1arg_mid,
      STREAM_SHUTDOWN_CALLBACK,
      arg);
}

void StreamCallbacks::on_shutdown(int status, int error_code) {
  assert(_env);
  assert(status < 0);

  jobject status_value = _env->CallStaticObjectMethod(_integer_cid, _integer_valueof_mid, status);
  jthrowable exception = NewException(_env, error_code);
  jobjectArray args = _env->NewObjectArray(2, _object_cid, 0);
  assert(args);
  _env->SetObjectArrayElement(args, 0, status_value);
  _env->SetObjectArrayElement(args, 1, exception);
  _env->CallVoidMethod(
      _instance,
      _callback_narg_mid,
      STREAM_SHUTDOWN_CALLBACK,
      args);
}

void StreamCallbacks::on_close() {
  assert(_env);
  _env->CallVoidMethod(
      _instance,
      _callback_1arg_mid,
      STREAM_CLOSE_CALLBACK,
      NULL);
}

// used in tcp.cpp and udp.cpp
jobject StreamCallbacks::_address_to_js(JNIEnv* env, const sockaddr* addr) {
  char ip[INET6_ADDRSTRLEN];
  const sockaddr_in *a4;
  const sockaddr_in6 *a6;
  int port;

  assert(addr);
  switch (addr->sa_family) {
  case AF_INET6:
    a6 = reinterpret_cast<const sockaddr_in6*>(addr);
    uv_inet_ntop(AF_INET6, &a6->sin6_addr, ip, sizeof ip);
    port = ntohs(a6->sin6_port);
    return env->NewObject(_address_cid,
      _address_init_mid,
      env->NewStringUTF(ip),
      port,
      env->NewStringUTF("IPv6"));

  case AF_INET:
    a4 = reinterpret_cast<const sockaddr_in*>(addr);
    uv_inet_ntop(AF_INET, &a4->sin_addr, ip, sizeof ip);
    port = ntohs(a4->sin_port);
    return env->NewObject(_address_cid,
      _address_init_mid,
      env->NewStringUTF(ip),
      port,
      env->NewStringUTF("IPv4"));
  }
  return NULL;
}

static uv_buf_t _alloc_cb(uv_handle_t* handle, size_t suggested_size) {
  // since we do not use buffer slabs, 64k buffers are too large
  if (suggested_size >= 64 * 1024) suggested_size = 16 * 1024;
  return uv_buf_init(new char[suggested_size], static_cast<unsigned int>(suggested_size));
}

static void _read_cb(uv_stream_t* stream, ssize_t nread, uv_buf_t buf) {
  StreamCallbacks* cb = reinterpret_cast<StreamCallbacks*>(stream->data);
  assert(cb);
  jsize size = static_cast<jsize>(nread);
  cb->on_read(&buf, size);
}

static void _shutdown_cb(uv_shutdown_t* req, int status) {
  assert(req);
  assert(req->handle);
  assert(req->handle->data);
  StreamCallbacks* cb = reinterpret_cast<StreamCallbacks*>(req->handle->data);
  if (status < 0) {
    int error_code = uv_last_error(req->handle->loop).code;
    cb->on_shutdown(status, error_code);
  } else {
    cb->on_shutdown(status);
  }
  delete req;
}

static void _close_cb(uv_handle_t* handle) {
  assert(handle);
  assert(handle->data);
  StreamCallbacks* cb = reinterpret_cast<StreamCallbacks*>(handle->data);
  cb->on_close();
  delete cb;
  delete handle;
}

static void _read2_cb(uv_pipe_t* pipe, ssize_t nread, uv_buf_t buf, uv_handle_type pending) {
  int r;
  StreamCallbacks* cb = reinterpret_cast<StreamCallbacks*>(pipe->data);
  assert(cb);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(pipe);
  jsize size = static_cast<jsize>(nread);
  if (pending == UV_TCP) {
    uv_tcp_t* tcp = new uv_tcp_t();
    r = uv_tcp_init(handle->loop, tcp);
    if (r) {
      cb->throw_exception(r, "read2_cb.uv_tcp_init");
      return;
    }
    tcp->data = new StreamCallbacks();
    r = uv_accept(handle, reinterpret_cast<uv_stream_t*>(tcp));
    if (r) {
      cb->throw_exception(r, "read2_cb.uv_accept(tcp)");
      return;
    }
    cb->on_read2(&buf, size, reinterpret_cast<long>(tcp), pending);
  } else if (pending == UV_NAMED_PIPE) {
    uv_pipe_t* p = new uv_pipe_t();
    r = uv_pipe_init(handle->loop, p, 1);
    if (r) {
      cb->throw_exception(r, "read2_cb.uv_pipe_init");
      return;
    }
    r = uv_accept(handle, reinterpret_cast<uv_stream_t*>(p));
    if (r) {
      cb->throw_exception(r, "read2_cb.uv_accept(pipe)");
      return;
    }
    cb->on_read2(&buf, size, reinterpret_cast<long>(p), pending);
  } else if (pending == UV_UDP) {
    uv_udp_t* udp = new uv_udp_t();
    r = uv_udp_init(handle->loop, udp);
    if (r) {
      cb->throw_exception(r, "read2_cb.uv_udp_init");
      return;
    }
    udp->data = new UDPCallbacks();
    r = uv_accept(handle, reinterpret_cast<uv_stream_t*>(udp));
    if (r) {
      cb->throw_exception(r, "read2_cb.uv_accept(udp)");
      return;
    }
    cb->on_read2(&buf, size, reinterpret_cast<long>(udp), pending);
  } else {
    assert(pending == UV_UNKNOWN_HANDLE);
    cb->on_read(&buf, size);
  }
}

static void _write_cb(uv_write_t* req, int status) {
  assert(req->handle);
  assert(req->handle->data);
  assert(req->data);
  StreamCallbacks* cb = reinterpret_cast<StreamCallbacks*>(req->handle->data);
  if (status < 0) {
    int error_code = uv_last_error(req->handle->loop).code;
    cb->on_write(status, error_code);
  } else {
    cb->on_write(status);
  }
  delete[] reinterpret_cast<jbyte*>(req->data);
  delete req;
}

static void _connection_cb(uv_stream_t* stream, int status) {
  assert(stream);
  assert(stream->data);
  StreamCallbacks* cb = reinterpret_cast<StreamCallbacks*>(stream->data);
  if (status < 0) {
    int error_code = uv_last_error(stream->loop).code;
    cb->on_connection(status, error_code);
  } else {
    cb->on_connection(status);
  }
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _static_initialize
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_java_libuv_handles_StreamHandle__1static_1initialize
  (JNIEnv *env, jclass cls) {

  StreamCallbacks::static_initialize(env, cls);
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _initialize
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_java_libuv_handles_StreamHandle__1initialize
  (JNIEnv *env, jobject that, jlong stream) {

  assert(stream);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(stream);
  assert(handle->data);
  StreamCallbacks* cb = reinterpret_cast<StreamCallbacks*>(handle->data);
  cb->initialize(that);
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _read_start
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_java_libuv_handles_StreamHandle__1read_1start
  (JNIEnv *env, jobject that, jlong stream) {

  assert(stream);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(stream);

  bool ipc_pipe = handle->type == UV_NAMED_PIPE && ((uv_pipe_t*)handle)->ipc;
  if (!ipc_pipe) {
    int r = uv_read_start(handle, _alloc_cb, _read_cb);
    if (r) {
      ThrowException(env, handle->loop, "uv_read_start");
    }
  } else {
    Java_net_java_libuv_handles_StreamHandle__1read2_1start(env, that, stream);
  }
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _read2_start
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_java_libuv_handles_StreamHandle__1read2_1start
  (JNIEnv *env, jobject that, jlong stream) {

  assert(stream);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(stream);
  int r = uv_read2_start(handle, _alloc_cb, _read2_cb);
  if (r) {
    ThrowException(env, handle->loop, "uv_read2_start");
  }
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _read_stop
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_java_libuv_handles_StreamHandle__1read_1stop
  (JNIEnv *env, jobject that, jlong stream) {

  assert(stream);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(stream);
  int r = uv_read_stop(handle);
  if (r) {
    ThrowException(env, handle->loop, "uv_read_stop");
  }
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _readable
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_net_java_libuv_handles_StreamHandle__1readable
  (JNIEnv *env, jobject that, jlong stream) {

  assert(stream);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(stream);
  int r = uv_is_readable(handle);
  if (r) {
    ThrowException(env, handle->loop, "uv_is_readable");
  }
  return r == 0 ? JNI_TRUE : JNI_FALSE;
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _writable
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_net_java_libuv_handles_StreamHandle__1writable
  (JNIEnv *env, jobject that, jlong stream) {

  assert(stream);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(stream);
  int r = uv_is_writable(handle);
  if (r) {
    ThrowException(env, handle->loop, "uv_is_writable");
  }
  return r == 0 ? JNI_TRUE : JNI_FALSE;
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _write
 * Signature: (J[BII)I
 */
JNIEXPORT jint JNICALL Java_net_java_libuv_handles_StreamHandle__1write
  (JNIEnv *env, jobject that, jlong stream, jbyteArray data, jint offset, jint length) {

  jbyte* base = new jbyte[length - offset];
  env->GetByteArrayRegion(data, offset, length, base);
  uv_buf_t buf;
  buf.base = reinterpret_cast<char*>(base);
  buf.len = length - offset;
  assert(stream);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(stream);
  uv_write_t* req = new uv_write_t();
  req->handle = handle;
  req->data = base;
  int r = uv_write(req, handle, &buf, 1, _write_cb);
  if (r) {
    delete[] base;
    delete req;
    ThrowException(env, handle->loop, "uv_write");
  }
  return r;
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _write2
 * Signature: (J[BIIJ)I
 */
JNIEXPORT jint JNICALL Java_net_java_libuv_handles_StreamHandle__1write2
  (JNIEnv *env, jobject that, jlong stream, jbyteArray data, jint offset, jint length, jlong send_stream) {

  jbyte* base = new jbyte[length - offset];
  env->GetByteArrayRegion(data, offset, length, base);
  uv_buf_t buf;
  buf.base = reinterpret_cast<char*>(base);
  buf.len = length - offset;
  assert(stream);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(stream);
  uv_write_t* req = new uv_write_t();
  req->handle = handle;
  req->data = base;
  assert(send_stream);
  uv_stream_t* send_handle = reinterpret_cast<uv_stream_t*>(send_stream);
  int r = uv_write2(req, handle, &buf, 1, send_handle, _write_cb);
  if (r) {
    delete[] base;
    delete req;
    ThrowException(env, handle->loop, "uv_write2");
  }
  return r;
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _write_queue_size
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_net_java_libuv_handles_StreamHandle__1write_1queue_1size
  (JNIEnv *env, jobject that, jlong stream) {

  assert(stream);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(stream);
  return handle->write_queue_size;
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _close_write
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_net_java_libuv_handles_StreamHandle__1close_1write
  (JNIEnv *env, jobject that, jlong stream) {

  assert(stream);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(stream);
  uv_shutdown_t* req = new uv_shutdown_t();
  req->handle = handle;
  int r = uv_shutdown(req, handle, _shutdown_cb);
  if (r) {
    ThrowException(env, handle->loop, "uv_close_write");
  }
  return r;
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _close
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_java_libuv_handles_StreamHandle__1close
  (JNIEnv *env, jobject that, jlong stream) {

  assert(stream);
  uv_handle_t* handle = reinterpret_cast<uv_handle_t*>(stream);
  uv_close(handle, _close_cb);
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _listen
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_net_java_libuv_handles_StreamHandle__1listen
  (JNIEnv *env, jobject that, jlong ptr, jint backlog) {

  assert(ptr);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(ptr);
  int r = uv_listen(handle, backlog, _connection_cb);
  if (r) {
    ThrowException(env, handle->loop, "uv_listen");
  }
  return r;
}

/*
 * Class:     net_java_libuv_handles_StreamHandle
 * Method:    _accept
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_net_java_libuv_handles_StreamHandle__1accept
  (JNIEnv *env, jobject that, jlong ptr, jlong clientPtr) {

  assert(ptr);
  assert(clientPtr);
  uv_stream_t* handle = reinterpret_cast<uv_stream_t*>(ptr);
  uv_stream_t* client = reinterpret_cast<uv_stream_t*>(clientPtr);
  int r = uv_accept(handle, client);
  if (r) {
    ThrowException(env, handle->loop, "uv_accept");
  }
  return r;
}