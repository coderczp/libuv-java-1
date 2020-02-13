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
package com.oracle.libuv;

import java.nio.ByteBuffer;

public interface CallbackHandler {

    void handleAsyncCallback(AsyncCallback cb, int status);

    void handleCheckCallback(CheckCallback cb, int status);

    void handleCheckCallback(CloseCallback cb, int status);

    void handlePrepareCallback(PrepareCallback cb, int status);

    void handlePrepareCallback(CloseCallback cb, int status);

    void handleIdleCallback(IdleCallback cb, int status);

    void handleIdleCallback(CloseCallback cb, int status);

    void handleStreamReadCallback(StreamReadCallback cb, ByteBuffer data);

    void handleStreamWriteCallback(StreamWriteCallback cb, int status, Exception error);

    void handleStreamConnectCallback(StreamConnectCallback cb, int status, Exception error);

    void handleStreamConnectionCallback(StreamConnectionCallback cb, int status, Exception error);

    void handleStreamCloseCallback(StreamCloseCallback cb);

    void handleStreamShutdownCallback(StreamShutdownCallback cb, int status, Exception error);

    void handleProcessCloseCallback(ProcessCloseCallback cb);

    void handleProcessExitCallback(ProcessExitCallback cb, int status, int signal, Exception error);

    void handleTimerCallback(TimerCallback cb, int status);

    void handleTimerCallback(CloseCallback cb, int status);

    void handleUDPRecvCallback(UDPRecvCallback cb, int nread, ByteBuffer data, Address address);

    void handleUDPSendCallback(UDPSendCallback cb, int status, Exception error);

    void handleUDPCloseCallback(UDPCloseCallback cb);

    void handleDnsCallback(DnsCallback cb, Address address, int status);
}
