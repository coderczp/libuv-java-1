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
 * Check handles will run the given callback once per loop iteration, right
 * after <stop>polling for i/o</stop>.
 * 
 * See <a href="http://docs.libuv.org/en/v1.x/check.html">Check handle</a>
 */
public class CheckHandle extends Handle {

    private boolean closed;

    private CheckCallback onCheck;

    private CloseCallback onClose;

    static {
        _static_initialize();
    }

    /**
     * Attach a {@link CheckCallback}.
     * 
     * @param callback A Callback, which will be invoked once per loop iteration,
     *                 right after <stop>polling for i/o</stop>.
     * 
     * @throws IllegalStateException if this method called more than once.
     */
    public void setCheckCallback(final CheckCallback callback) {
        if (onCheck != null) {
            throw new IllegalStateException();
        }
        onCheck = callback;
    }

    /**
     * Attach a {@link CloseCallback}.
     * 
     * @param callback A Callback, which will be invoked when check handle is
     *                 closed.
     *
     * @throws IllegalStateException if this method called more than once.
     */
    public void setCloseCallback(final CloseCallback callback) {
        if (onClose != null) {
            throw new IllegalStateException();
        }
        onClose = callback;
    }

    protected CheckHandle(final LoopHandle loop) {
        super(_new(loop.pointer()), loop);
        _initialize(pointer);
    }

    /**
     * Starts the handle with the callback specified.
     * 
     * @see {@link #setCheckCallback(CheckCallback)}
     * 
     * @return {@code 0} on success, or an error {@code code < 0} on failure.
     */
    public int start() {
        return _start(pointer);
    }

    /**
     * Stop the handle, the callback will no longer be called.
     * 
     * @return {@code 0} on success, or an error {@code code < 0} on failure.
     */
    public int stop() {
        return _stop(pointer);
    }

    /**
     * Close the handle and free up any resources that may be held by it.
     * <p>
     * {@link CloseCallback} callback will be invoked right before the close.
     * 
     * @see {@link #setCloseCallback(CloseCallback)}
     */
    public void close() {
        if (!closed) {
            _close(pointer);
        }
        closed = true;
    }

    // ------------------------------------------------------------------------
    // ~ Private
    // ------------------------------------------------------------------------

    private void callback(final int type,
                          final int status) {
        switch (type) {
        case 1:
            if (onCheck != null) {
                loop.getCallbackHandler()
                    .handleCheckCallback(onCheck, status);
            }
            break;
        case 2:
            if (onClose != null) {
                loop.getCallbackHandler()
                    .handleCheckCallback(onClose, status);
            }
            break;
        default:
            assert false : "unsupported callback type " + type;
        }
    }

    // ------------------------------------------------------------------------
    // ~ Native
    // ------------------------------------------------------------------------

    private static native long _new(final long loop);

    private static native void _static_initialize();

    private native void _initialize(final long ptr);

    private native int _start(final long ptr);

    private native int _stop(final long ptr);

    private native void _close(final long ptr);
}
