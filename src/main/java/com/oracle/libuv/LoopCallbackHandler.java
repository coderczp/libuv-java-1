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

import java.nio.ByteBuffer;

public final class LoopCallbackHandler implements CallbackHandler {

    private final CallbackExceptionHandler exceptionHandler;

    LoopCallbackHandler(final CallbackExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void handleAsyncCallback(final AsyncCallback cb,
                                    final int           status) {
        try {
            cb.onSend(status);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleCheckCallback(final CheckCallback cb,
                                    final int           status) {
        try {
            cb.onCheck(status);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }


    @Override
    public void handleCheckCallback(CloseCallback cb, int status) {
        try {
            cb.onClose(status);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handlePrepareCallback(final PrepareCallback cb,
                                      final int             status) {
        try {
            cb.onPrepare(status);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }


    @Override
    public void handlePrepareCallback(final CloseCallback cb,
                                      final int           status) {
        try {
            cb.onClose(status);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleStreamReadCallback(final StreamReadCallback cb,
                                         final ByteBuffer         data) {
        try {
        	if (data != null) {
        		ByteBuffer buffer = clone(data);
        		cb.onRead(buffer);
        	} else {
        		cb.onRead(data);
        	}
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    private ByteBuffer clone(ByteBuffer original) {
        ByteBuffer clone = ByteBuffer.allocate(original.capacity());
        clone.put(original);
        clone.flip();
        return clone;
    }

    @Override
    public void handleStreamWriteCallback(final StreamWriteCallback cb,
                                          final int                 status,
                                          final Exception           error) {
        try {
            cb.onWrite(status, error);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleStreamConnectCallback(final StreamConnectCallback cb,
                                            final int                   status,
                                            final Exception             error) {
        try {
            cb.onConnect(status, error);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleStreamConnectionCallback(final StreamConnectionCallback cb,
                                               final int                      status,
            final Exception error) {
        try {
            cb.onConnection(status, error);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleStreamCloseCallback(final StreamCloseCallback cb) {
        try {
            cb.onClose();
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleStreamShutdownCallback(final StreamShutdownCallback cb,
                                             final int                    status,
                                             final Exception              error) {
        try {
            cb.onShutdown(status, error);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleProcessCloseCallback(final ProcessCloseCallback cb) {
        try {
            cb.onClose();
        } catch (Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleProcessExitCallback(final ProcessExitCallback cb,
                                          final int                  status,
                                          final int                  signal,
                                          final Exception            error) {
        try {
            cb.onExit(status, signal, error);
        } catch (Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleTimerCallback(final TimerCallback cb,
                                    final int           status) {
        try {
            cb.onTimer(status);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }


    @Override
    public void handleTimerCallback(final CloseCallback cb,
                                    final int           status) {
        try {
            cb.onClose(status);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleUDPRecvCallback(final     UDPRecvCallback cb,
                                      final int nread,
                                      final     ByteBuffer data,
            final Address address) {
        try {
            cb.onRecv(nread, data, address);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleUDPSendCallback(final     UDPSendCallback cb,
                                      final int status,
                                      final     Exception error) {
        try {
            cb.onSend(status, error);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleUDPCloseCallback(final UDPCloseCallback cb) {
        try {
            cb.onClose();
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleIdleCallback(final     IdleCallback cb,
                                   final int status) {
        try {
            cb.onIdle(status);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleIdleCallback(final CloseCallback cb,
                                   final int           status) {
        try {
            cb.onClose(status);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }

    @Override
    public void handleDnsCallback(final DnsCallback cb,
                                  final Address     address,
                                  final int         status) {
        try {
            cb.onAddress(address, status);
        } catch (final Exception ex) {
            exceptionHandler.handle(ex);
        }
    }
}
