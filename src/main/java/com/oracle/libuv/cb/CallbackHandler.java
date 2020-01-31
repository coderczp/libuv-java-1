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

package com.oracle.libuv.cb;

import java.nio.ByteBuffer;

import com.oracle.libuv.Address;

public interface CallbackHandler {
    public void handleAsyncCallback(AsyncCallback cb, int status);
    public void handleCheckCallback(CheckCallback cb, int status);
    public void handleStreamReadCallback(StreamReadCallback cb, ByteBuffer data);
    public void handleStreamWriteCallback(StreamWriteCallback cb, int status, Exception error);
    public void handleStreamConnectCallback(StreamConnectCallback cb, int status, Exception error);
    public void handleStreamConnectionCallback(StreamConnectionCallback cb, int status, Exception error);
    public void handleStreamCloseCallback(StreamCloseCallback cb);
    public void handleStreamShutdownCallback(StreamShutdownCallback cb, int status, Exception error);
    public void handleProcessCloseCallback(ProcessCloseCallback cb);
    public void handleProcessExitCallback(ProcessExitCallback cb, int status, int signal, Exception error);
    public void handleTimerCallback(TimerCallback cb, int status);
    public void handleUDPRecvCallback(UDPRecvCallback cb, int nread, ByteBuffer data, Address address);
    public void handleUDPSendCallback(UDPSendCallback cb, int status, Exception error);
    public void handleUDPCloseCallback(UDPCloseCallback cb);
}
