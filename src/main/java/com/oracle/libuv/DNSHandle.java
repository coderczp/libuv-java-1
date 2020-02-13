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

import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNull;

public class DNSHandle {

    private final long pointer;

    private final LoopHandle loop;

    private DnsCallback onDns;

    private final String host;

    private final int port;

    private boolean executed;

    static {
        _static_initialize();
    }

    DNSHandle(final LoopHandle loop,
              final String     host,
              final int        port) {
        requireNonNull(loop);
        requireNonNull(host);
        if (port > 65535 || port < 0) {
            throw new IllegalArgumentException("Invalid port number: [" + port + "]");
        }
        this.host = host;
        this.port = port;
        this.loop = loop;
        this.pointer = _new(loop.pointer());
        _initialize(pointer);
    }

    public void setDnsCallback(final DnsCallback callback) {
        if (onDns != null) {
            throw new IllegalStateException();
        }
        onDns = callback;
    }

    public int execute() {
        if (executed) {
            throw new IllegalStateException("DNS Handle can't be used more than once");
        }
        executed = true;
        return _uv_getaddrinfo(pointer, getHost(),
                               port == 0 ? null : valueOf(port));
    }

    private void callback(final Address address,
                          final int     status) {
        if (onDns != null) {
            loop.getCallbackHandler()
                .handleDnsCallback(onDns, address,
                                   status);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
    
    // ------------------------------------------------------------------------
    // ~ Native
    // ------------------------------------------------------------------------

    private native void _initialize(long ptr);

    private static native long _new(final long loop);

    private static native void _static_initialize();

    private native int  _uv_getaddrinfo(final long   ptr,
                                        final String node,
                                        final String service);
}
