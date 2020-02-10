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

/**
 * Pipe handles provide an abstraction over streaming files on Unix (including
 * local domain sockets, pipes, and FIFOs) and named pipes on Windows.
 * <p>
 * See <a href="http://docs.libuv.org/en/v1.x/pipe.html">Pipe handle</a>
 */
public class PipeHandle extends StreamHandle {

    protected PipeHandle(final LoopHandle loop, final boolean ipc) {
        super(_new(loop.pointer(), ipc), loop);
    }

    /**
     * Bind the pipe to a file path (Unix) or a name (Windows).
     * 
     * @param name socket, pipe or FIFO name.
     * 
     * @return {@code 0} on success, or an error {@code code < 0} on failure.
     */
    public int bind(final String name) {
        Objects.requireNonNull(name);
        return _bind(pointer, name);
    }

    /**
     * Accepts connections.
     * <p>
     * This call is used in conjunction with {@link #listen(int)} to accept incoming
     * connections.
     * <p>
     * Call this function after receiving a {@link StreamConnectionCallback} to accept
     * the connection. Before calling this function the client handle must be
     * initialized.
     * 
     * @return {@code 0} on success, or an error {@code code < 0} on failure.
     */
    @Override
    public int accept(final StreamHandle client) {
        return super.accept(client);
    }

    public void connect(final String name) {
        Objects.requireNonNull(name);
        _connect(pointer, name, loop.getContext());
    }

    // ------------------------------------------------------------------------
    // ~ Private
    // ------------------------------------------------------------------------

    private static native long _new(final long    loop,
                                    final boolean ipc);

    private native int _bind(final long   ptr,
                             final String name);

    private native void _connect(final long   ptr,
                                 final String name,
                                 final Object context);
}
