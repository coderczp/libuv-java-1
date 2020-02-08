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

import com.oracle.libuv.cb.CloseCallback;
import com.oracle.libuv.cb.PrepareCallback;

/**
 * Prepare handles will run the given callback once per loop iteration, right
 * before <strong>polling for i/o</strong>.
 * <p>
 * See <a href="http://docs.libuv.org/en/v1.x/prepare.html">Perpare handle</a>
 */
public class PrepareHandle extends Handle {

    private boolean closed;

    private PrepareCallback onPrepare;

    private CloseCallback onClose;

    static {
        _static_initialize();
    }

    /**
     * Attach a {@link PrepareCallback}.
     * 
     * @param callback A Callback, which will be invoked once per loop iteration,
     *                 right before <strong>polling for i/o</strong>.
     */
    public void setPrepareCallback(final PrepareCallback callback) {
        onPrepare = callback;
    }

    /**
     * Attach a {@link CloseCallback}.
     * 
     * @param callback A Callback, which will be invoked when check handle is
     *                 closed.
     */
    public void setCloseCallback(final CloseCallback callback) {
        onClose = callback;
    }

    protected PrepareHandle(final LoopHandle loop) {
        super(_new(loop.pointer()), loop);
        _initialize(pointer);
    }

    /**
     * Starts the prepare handle with the callback specified
     * {@link #setPrepareCallback(PrepareCallback)}.
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
     * 
     * @return {@code 0} on success, or an error {@code code < 0} on failure.
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

    private void callback(final int type, final int status) {
        switch (type) {
        case 1:
            if (onPrepare != null) {
                loop.getCallbackHandler().handlePrepareCallback(onPrepare, status);
            }
            break;
        case 2:
            if (onClose != null) {
                loop.getCallbackHandler().handlePrepareCallback(onClose, status);
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
