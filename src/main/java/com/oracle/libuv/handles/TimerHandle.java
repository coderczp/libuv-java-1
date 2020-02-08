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
import com.oracle.libuv.cb.TimerCallback;

/**
 * Timer handles are used to schedule callbacks to be called in the future.
 * <p>
 * See <a href="http://docs.libuv.org/en/v1.x/timer.html">Timer handle</a>
 */
public class TimerHandle extends Handle {

    private boolean closed;

    private TimerCallback onTimerFired;

    private CloseCallback onClose;

    static {
        _static_initialize();
    }

    /**
     * Attach a {@link TimerCallback}.
     * 
     * @param callback A Callback, which will be invoked once <strong>per loop
     *                 iteration</strong>, right before the {@link PrepareCallback}
     *                 handles.
     */
    public void setTimerFiredCallback(final TimerCallback callback) {
        if (onTimerFired != null) {
            throw new IllegalStateException();
        }
        onTimerFired = callback;
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

    protected TimerHandle(final LoopHandle loop) {
        super(_new(loop.pointer()), loop);
        _initialize(pointer);
    }

    /**
     * Return the current timestamp in milliseconds.
     * <p>
     * The timestamp increases monotonically from some arbitrary point in time.
     * Don't make assumptions about the starting point, you will only get
     * disappointed.
     * 
     * @return current timestamp in milliseconds.
     */
    public static long now(final LoopHandle loop) {
        return _now(loop.pointer());
    }

    /**
     * Start the timer.
     * <p>
     * If timeout is zero, the callback fires on the next event loop iteration. If
     * repeat is non-zero, the callback fires first after timeout milliseconds and
     * then repeatedly after repeat milliseconds.
     * <p>
     * The timer will be scheduled to run on the given interval, regardless of the
     * callback execution duration, and will follow normal timer semantics in the
     * case of a time-slice overrun.
     * <p>
     * For example, if a {@code 50ms} repeating timer first runs for {@code 17ms},
     * it will be scheduled to run again {@code 33ms} later. If other tasks consume
     * more than the @{code 33ms} following the first timer callback, then the
     * callback will run as soon as possible.
     * <p>
     * <strong>Note</strong><br>
     * If the repeat value is set from a timer callback it does not immediately take
     * effect. If the timer was non-repeating before, it will have been stopped. If
     * it was repeating, then the old repeat value will have been used to schedule
     * the next timeout.
     * 
     * @param timeout timer's timeout value in milliseconds.
     * @param repeat  timer's timer repeat value in milliseconds.
     */
    public int start(final long timeout, final long repeat) {
        return _start(pointer, timeout, repeat);
    }

    /**
     * Stop the timer, and if it is repeating restart it using the repeat value as
     * the timeout.
     * <p>
     * If the timer has never been started before it returns UV_EINVAL.
     */
    public int again() {
        return _again(pointer);
    }

    /**
     * Get the timer repeat value.
     * 
     * @return timer value in milliseconds.
     */
    public long getRepeat() {
        return _get_repeat(pointer);
    }

    /**
     * Set the repeat interval value in milliseconds.
     * <p>
     * The timer will be scheduled to run on the given interval, regardless of the
     * callback execution duration, and will follow normal timer semantics in the
     * case of a time-slice overrun.
     * <p>
     * For example, if a 50ms repeating timer first runs for 17ms, it will be
     * scheduled to run again 33ms later. If other tasks consume more than the 33ms
     * following the first timer callback, then the callback will run as soon as
     * possible.
     * <p>
     * <strong>Note</strong><br>
     * If the repeat value is set from a timer callback it does not immediately take
     * effect. If the timer was non-repeating before, it will have been stopped. If
     * it was repeating, then the old repeat value will have been used to schedule
     * the next timeout.
     * 
     * @param repeat timer's repeat value in milliseconds.
     */
    public void setRepeat(final long repeat) {
        _set_repeat(pointer, repeat);
    }

    /**
     * Stop the timer, the callback will not be called anymore.
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

    private void callback(final int type, final int status) {
        switch (type) {
        case 1:
            if (onTimerFired != null) {
                loop.getCallbackHandler().handleTimerCallback(onTimerFired, status);
            }
            break;
        case 2:
            if (onClose != null) {
                loop.getCallbackHandler().handleTimerCallback(onClose, status);
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

    private static native long _now(final long loop);

    private static native void _static_initialize();

    private native void _initialize(final long ptr);

    private native int _start(final long ptr, final long timeout, final long repeat);

    private native int _again(final long ptr);

    private native long _get_repeat(final long ptr);

    private native void _set_repeat(final long ptr, final long repeat);

    private native int _stop(final long ptr);

    private native void _close(final long ptr);
}
