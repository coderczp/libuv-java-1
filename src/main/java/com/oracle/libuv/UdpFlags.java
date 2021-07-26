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

/**
 * Flags used in {@code UDPHandle#bind(int, String, boolean)}
 * 
 * @see http://docs.libuv.org/en/v1.x/udp.html#c.uv_udp_flags
 */
public enum UdpFlags {
    /**
     * Disables dual stack mode.
     */
    UV_UDP_IPV6ONLY(1),
    /**
     * Indicates message was truncated because read buffer was too small. The
     * remainder was discarded by the OS. Used in uv_udp_recv_cb.
     */
    UV_UDP_PARTIAL(2),
    /**
     * Indicates if SO_REUSEADDR will be set when binding the handle. This sets the
     * SO_REUSEPORT socket flag on the BSDs and OS X. On other Unix platforms, it
     * sets the SO_REUSEADDR flag. What that means is that multiple threads or
     * processes can bind to the same address without error (provided they all set
     * the flag) but only the last one to bind will receive any traffic, in effect
     * "stealing" the port from the previous listener.
     */
    UV_UDP_REUSEADDR(4),
    /**
     * Indicates that the message was received by recvmmsg, so the buffer provided
     * must not be freed by the recv_cb callback.
     */
    UV_UDP_MMSG_CHUNK(8),
    /**
     * Indicates that the buffer provided has been fully utilized by recvmmsg and
     * that it should now be freed by the recv_cb callback. When this flag is set in
     * uv_udp_recv_cb, nread will always be 0 and addr will always be NULL.
     */
    UV_UDP_MMSG_FREE(16),
    /**
     * Indicates if IP_RECVERR/IPV6_RECVERR will be set when binding the handle.
     * This sets IP_RECVERR for IPv4 and IPV6_RECVERR for IPv6 UDP sockets on Linux.
     * This stops the Linux kernel from suppressing some ICMP error messages and
     * enables full ICMP error reporting for faster failover. This flag is no-op on
     * platforms other than Linux.
     */
    UV_UDP_LINUX_RECVERR(32),
    /**
     * Indicates that recvmmsg should be used, if available.
     */
    UV_UDP_RECVMMSG(256);

    public final int value;

    private UdpFlags(int value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
