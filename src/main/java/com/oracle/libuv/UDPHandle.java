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

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.EnumSet;

public class UDPHandle extends Handle {

    private boolean closed;

    private UDPRecvCallback onRecv;

    private UDPSendCallback onSend;

    private UDPCloseCallback onClose;

    public enum Membership {
        LEAVE_GROUP(0),
        JOIN_GROUP(1);

        final int value;

        Membership(final int value) {
            this.value = value;
        }
    }

    static {
        _static_initialize();
    }

    public void setRecvCallback(final UDPRecvCallback callback) {
        onRecv = callback;
    }

    public void setSendCallback(final UDPSendCallback callback) {
        onSend = callback;
    }

    public void setCloseCallback(final UDPCloseCallback callback) {
        onClose = callback;
    }

    UDPHandle(final LoopHandle loop) {
        super(_new(loop.pointer()), loop);
        this.closed = false;
        _initialize(pointer);
    }

    public void close() {
        if (!closed) {
            _close(pointer);
        }
        closed = true;
    }

    public Address address() {
        return _address(pointer);
    }

    public int bind(final int     port,
                    final String  address,
                    final boolean ipv6) {
        return bind(port, address,
                    ipv6, EnumSet.noneOf(UdpFlags.class));
    }

    public int bind(final int     port,
                    final String  address,
                    final boolean ipv6,
                    EnumSet<UdpFlags> flags) {
        requireNonNull(address);
        int flagValue = 0;
        for (UdpFlags next : flags) {
            flagValue |= next.value;
        }
        return bind(port, address,
                    ipv6, flagValue);
    }

    public int bind(final int     port,
                    final String  address,
                    final boolean ipv6,
                    final int     flags) {
        return _bind(pointer, port,
                     address, ipv6, flags);
    }

    public int send(final String  str,
                    final int     port,
                    final String  host,
                    final boolean ipv6) {
        requireNonNull(str);
        requireNonNull(host);
        final byte[] data = str.getBytes(UTF_8);
        return send(ByteBuffer.wrap(data), 0,
                    data.length, port,
                    host, ipv6);
    }

    public int send(final String  str,
                    final String  encoding,
                    final int     port,
                    final String  host,
                    final boolean ipv6)
            throws UnsupportedEncodingException {
        requireNonNull(str);
        requireNonNull(encoding);
        requireNonNull(host);
        final byte[] data = str.getBytes(encoding);
        return send(ByteBuffer.wrap(data), 0,
                    data.length, port,
                    host, ipv6);
    }

    public int send(final ByteBuffer buffer,
                    final int        port,
                    final String     host,
                    final boolean    ipv6) {
        requireNonNull(buffer);
        requireNonNull(host);
        return buffer.hasArray()
                ? _send(pointer, buffer,
                        buffer.array(),
                        0, buffer.capacity(),
                        port, host,
                        loop.getContext(), ipv6)
                : _send(pointer, buffer,
                        null, 0,
                        buffer.capacity(), port,
                        host, loop.getContext(),
                        ipv6);
    }

    public int send(final ByteBuffer buffer,
                    final int        offset,
                    final int        length,
                    final int        port,
                    final String     host,
                    final boolean    ipv6) {
        requireNonNull(buffer);
        requireNonNull(host);
        return buffer.hasArray()
                ? _send(pointer, buffer,
                        buffer.array(), offset,
                        length, port,
                        host, loop.getContext(),
                        ipv6)
                : _send(pointer, buffer,
                        null, offset,
                        length, port,
                        host, loop.getContext(),
                        ipv6);
    }

    public int recvStart() {
        return _recv_start(pointer);
    }

    public int recvStop() {
        return _recv_stop(pointer);
    }

    public int setTTL(final int ttl) {
        return _set_ttl(pointer, ttl);
    }

    public int setMembership(final String     multicastAddress,
                             final String     interfaceAddress,
                             final Membership membership) {
        return _set_membership(pointer, multicastAddress,
                               interfaceAddress, membership.value);
    }

    public int setMulticastLoop(final boolean on) {
        return _set_multicast_loop(pointer, on ? 1 : 0);
    }

    public int setMulticastTTL(final int ttl) {
        return _set_multicast_ttl(pointer, ttl);
    }

    public int setBroadcast(final boolean on) {
        return _set_broadcast(pointer, on ? 1 : 0);
    }

    // ------------------------------------------------------------------------
    // ~ Native
    // ------------------------------------------------------------------------

    private void callRecv(final int        nread,
                          final ByteBuffer data,
                          final Address    address) {
        if (onRecv != null) {
            loop.getCallbackHandler()
                .handleUDPRecvCallback(onRecv, nread,
                                       data, address);
        }
    }

    private void callSend(final int       status,
                          final Exception error,
                          final Object    context) {
        if (onSend != null) {
            loop.getCallbackHandler(context)
                .handleUDPSendCallback(onSend, status, error);
        }
    }

    private void callClose() {
        if (onClose != null) {
            loop.getCallbackHandler()
                .handleUDPCloseCallback(onClose);
        }
    }

    // ------------------------------------------------------------------------
    // ~ Native
    // ------------------------------------------------------------------------

    private static native long _new(long loop);

    private static native void _static_initialize();

    private native void _initialize(long ptr);

    private native Address _address(long ptr);

    private native int _bind(long    ptr,
                             int     port,
                             String  host,
                             boolean ipv6,
                             int     flags);

    private native int _send(long ptr,
                             ByteBuffer buffer,
                             byte[]     data,
                             int        offset,
                             int        length,
                             int        port,
                             String     host,
                             Object     context,
                             boolean    ipv6);

    private native int _recv_start(long ptr);

    private native int _recv_stop(long ptr);

    private native int _set_ttl(long ptr,
                                int  ttl);

    private native int _set_membership(long   ptr,
                                       String multicastAddress,
                                       String interfaceAddress,
                                       int    membership);

    private native int _set_multicast_loop(long ptr,
                                           int on);

    private native int _set_multicast_ttl(long ptr, int ttl);

    private native int _set_broadcast(long ptr, int on);

    private native void _close(long ptr);
}
