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

import java.util.concurrent.atomic.AtomicBoolean;

import com.oracle.libuv.cb.AsyncCallback;
import com.oracle.libuv.cb.CloseCallback;

/**
 * Async handles allow the user to <strong>wakeup</strong> the event loop and
 * get a callback called from another thread.
 * <p>
 * See <a href="http://docs.libuv.org/en/v1.x/async.html">Async handle</a>
 */
public class AsyncHandle extends Handle {

    private final AtomicBoolean closed = new AtomicBoolean();

    private AsyncCallback onSend;

    static {
        _static_initialize();
    }

    /**
     * Attach a {@link AsyncCallback}.
     * 
     * @param callback A callback, which will be executed after a loop will receive
     *                 an async signal from this handle.
     *
     * @throws IllegalStateException if this method called more than once.
     */
    public void setAsyncCallback(final AsyncCallback callback) {
        if (onSend != null) {
            throw new IllegalStateException();
        }
        onSend = callback;
    }

    protected AsyncHandle(final LoopHandle loop) {
        super(_new(loop.pointer()), loop);
        _initialize(pointer);
    }

    /**
     * Wake up the event loop and call the specified callback.
     * <p>
     * It's safe to call this function from <strong>any thread</strong>. The
     * callback will be called on the <strong>loop thread</strong>.
     * 
     * @see {@link #setAsyncCallback(AsyncCallback)}
     * 
     * @return {@code 0} on success, or an error {@code code < 0} on failure.
     */
    public int send() {
        return closed.get() ? -1 : _send(pointer);
    }

    /**
     * Close the handle and free up any resources that may be held by it.
     * <p>
     * {@link CloseCallback} callback will be invoked right before the close.
     * 
     * @see {@link #setCloseCallback(CloseCallback)}
     */
    public void close() {
        if (closed.compareAndSet(false, true)) {
            _close(pointer);
        }
    }

    // ------------------------------------------------------------------------
    // ~ Private
    // ------------------------------------------------------------------------

    private void callSend(final int status) {
        if (onSend != null) {
            loop.getCallbackHandler().handleAsyncCallback(onSend, status);
        }
    }

    // ------------------------------------------------------------------------
    // ~ Native
    // ------------------------------------------------------------------------

    private static native long _new(final long loop);

    private static native void _static_initialize();

    private native void _initialize(final long ptr);

    private native int _send(final long ptr);

    private native void _close(final long ptr);
}
