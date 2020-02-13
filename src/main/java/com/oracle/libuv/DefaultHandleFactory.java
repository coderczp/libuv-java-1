/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
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

public class DefaultHandleFactory implements HandleFactory {

    private LoopHandle loop;

    public DefaultHandleFactory(final LoopHandle loop) {
        this.loop = loop;
    }

    @Override
    public LoopHandle getLoopHandle() {
        return loop;
    }

    @Override
    public AsyncHandle newAsyncHandle() {
        return new AsyncHandle(loop);
    }

    @Override
    public CheckHandle newCheckHandle() {
        return new CheckHandle(loop);
    }


    @Override
    public PrepareHandle newPrepareHandle() {
        return new PrepareHandle(loop);
    }

    @Override
    public IdleHandle newIdleHandle() {
        return new IdleHandle(loop);
    }

    @Override
    public PipeHandle newPipeHandle(final boolean ipc) {
        return new PipeHandle(loop, ipc);
    }

    @Override
    public ProcessHandle newProcessHandle() {
        return new ProcessHandle(loop);
    }

    @Override
    public TCPHandle newTCPHandle() {
        return new TCPHandle(loop);
    }

    @Override
    public TimerHandle newTimerHandle() {
        return new TimerHandle(loop);
    }

    @Override
    public UDPHandle newUDPHandle() {
        return new UDPHandle(loop);
    }

    @Override
    public DNSHandle newDnsHandle(String host, int port) {
        return new DNSHandle(loop, host, port);
    }

    @Override
    public DNSHandle newDnsHandle(String host) {
        return new DNSHandle(loop, host, 0);
    }
}
