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
package com.oracle.libuv.handles;

import java.util.Objects;

import com.oracle.libuv.Address;

public class TCPHandle extends StreamHandle {

    protected TCPHandle(final LoopHandle loop) {
        super(_new(loop.pointer()), loop);
    }

    protected TCPHandle(final LoopHandle loop, final long socket) {
        super(_new(loop.pointer(), socket), loop);
    }

    protected TCPHandle(final LoopHandle loop, final long pointer, boolean dummy) {
        super(pointer, loop);
    }

    public int bind(final String address, final int port, boolean ipv6) {
        Objects.requireNonNull(address);
        return _bind(pointer, address, port, ipv6);
    }

    public int connect(final String address, final int port, boolean ipv6) {
        Objects.requireNonNull(address);
        return _connect(pointer, address, port, loop.getContext(), ipv6);
    }

    @Override
    public int listen(final int backlog) {
        return super.listen(backlog);
    }

    @Override
    public int accept(final StreamHandle client) {
        Objects.requireNonNull(client);
        assert client instanceof TCPHandle;
        final int accepted = super.accept(client);
        return accepted;
    }

    public Address getSocketName() {
        return _socket_name(pointer);
    }

    public Address getPeerName() {
        return _peer_name(pointer);
    }

    public int open(final long socket) {
        return _open(pointer, socket);
    }

    public int setNoDelay(final boolean enable) {
        return _no_delay(pointer, enable ? 1 : 0);
    }

    public int setKeepAlive(final boolean enable, final int delay) {
        return _keep_alive(pointer, enable ? 1 : 0, delay);
    }

    public int setSimultaneousAccepts(final boolean enable) {
        return _simultaneous_accepts(pointer, enable ? 1 : 0);
    }

    // ------------------------------------------------------------------------
    // ~ Native
    // ------------------------------------------------------------------------

    private static native long _new(final long loop);

    private static native long _new(final long loop, final long socket);

    private native int _bind(final long ptr, final String address, final int port, final boolean ipv6);

    private native int _connect(final long ptr, final String address, final int port, final Object context,
            final boolean ipv6);

    private native int _open(final long ptr, final long socket);

    private native Address _socket_name(final long ptr);

    private native Address _peer_name(final long ptr);

    private native int _no_delay(final long ptr, final int enable);

    private native int _keep_alive(final long ptr, final int enable, final int delay);

    private native int _simultaneous_accepts(final long ptr, final int enable);
}
