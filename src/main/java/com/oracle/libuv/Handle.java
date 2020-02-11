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

import java.io.Closeable;
import java.util.Objects;

/**
 * The base class for handle.
 * 
 * See <a href="http://docs.libuv.org/en/v1.x/handle.html">Base handle</a>
 */
public abstract class Handle implements Closeable {

    protected final long pointer;

    protected final LoopHandle loop;

    protected Handle(final long       pointer,
                     final LoopHandle loop) {
        Objects.requireNonNull(loop);
        assert pointer != 0;
        this.pointer = pointer;
        this.loop = loop;
    }

    /**
     * Reference this handle.
     * <p>
     * References are idempotent, that is, if a handle is already referenced calling
     * this function again will have no effect.
     */
    public void ref() {
        _ref(pointer);
    }

    /**
     * Un-reference this handle.
     * <p>
     * References are idempotent, that is, if a handle is not referenced calling
     * this function again will have no effect.
     */
    public void unref() {
        _unref(pointer);
    }

    /**
     * Retrieves whether this handle has been closed.
     * <p>
     * This function should only be used between the initialization of the handle
     * and the arrival of the close callback.
     * 
     * @return {@code false} if the handle is closing or closed, {@code true}
     *         otherwise.
     */
    public boolean isClosing() {
        return _closing(pointer);
    }

    @Override
    public int hashCode() {
        return (int) ((pointer & 0xffffffffL) ^ ((pointer >> 32) & 0xffffffffL));
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof Handle && pointer == ((Handle) other).pointer;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "." + Long.toHexString(pointer);
    }

    // ------------------------------------------------------------------------
    // ~ Native
    // ------------------------------------------------------------------------

    private native void _ref(final long ptr);

    private native void _unref(final long ptr);

    private native boolean _closing(final long ptr);
}
