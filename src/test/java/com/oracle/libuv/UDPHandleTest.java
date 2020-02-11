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

import static com.oracle.libuv.DefaultHandleFactory.newFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

public class UDPHandleTest extends TestBase {

    private static final String HOST = "127.0.0.1";
    private static final String HOST6 = "::1";

    private static final int PORT = 34567;
    private static final int TIMES = 10;

    public void testConnection(boolean ipv6) throws Throwable {
        final AtomicInteger clientSendCount = new AtomicInteger(0);
        final AtomicInteger serverRecvCount = new AtomicInteger(0);

        final AtomicBoolean serverDone = new AtomicBoolean(false);
        final AtomicBoolean clientDone = new AtomicBoolean(false);

        final HandleFactory handleFactory = newFactory();
        final LoopHandle loop = handleFactory.getLoopHandle();
        final UDPHandle server = handleFactory.newUDPHandle();
        final UDPHandle client = handleFactory.newUDPHandle();

        server.setRecvCallback(new UDPRecvCallback() {
            @Override
            public void onRecv(int nread, ByteBuffer data, Address address) throws Exception {
                byte[] b = new byte[data.remaining()];
                data.get(b);
                Assert.assertTrue(new String(b, StandardCharsets.UTF_8).contains("PING"));
                if (serverRecvCount.incrementAndGet() < TIMES) {
                } else {
                    server.close();
                    serverDone.set(true);
                }
            }
        });

        client.setSendCallback(new UDPSendCallback() {
            @Override
            public void onSend(int status, Exception error) throws Exception {
                if (clientSendCount.incrementAndGet() < TIMES) {
                } else {
                    client.close();
                    clientDone.set(true);
                }
            }
        });

        server.bind(PORT, ipv6 ? HOST6 : HOST, ipv6);
        server.recvStart();

        for (int i = 0; i < TIMES; i++) {
            System.out.println("sending");
            client.send("PING." + i, PORT, ipv6 ? HOST6 : HOST, ipv6);
        }

        final long start = System.currentTimeMillis();
        while (!serverDone.get()) {
            if (System.currentTimeMillis() - start > TestBase.TIMEOUT) {
                Assert.fail("timeout");
            }
            loop.runNoWait();
        }

        Assert.assertEquals(clientSendCount.get(), TIMES);
        Assert.assertEquals(serverRecvCount.get(), TIMES);
    }

    @Test
    public void testConnectionIpv4() throws Throwable {
        testConnection(false);
    }

    @Test
    public void testConnectionIpv6() throws Throwable {
        testConnection(true);
    }
}
