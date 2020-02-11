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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

public class IdleHandleTest extends TestBase {

    @Test
    public void testIdle() throws Throwable {
        final AtomicBoolean gotCallback = new AtomicBoolean(false);
        final AtomicBoolean gotClose = new AtomicBoolean(false);
        final AtomicInteger times = new AtomicInteger(0);

        final HandleFactory handleFactory = newFactory();
        final LoopHandle loop = handleFactory.getLoopHandle();
        final IdleHandle idleHandle = handleFactory.newIdleHandle();

        idleHandle.setCloseCallback(i -> {
            System.out.println("idle closed");
            gotClose.set(true);
        });

        idleHandle.setIdleCallback(status -> {
            gotCallback.set(true);
            System.out.println("idle!");
            times.incrementAndGet();
            idleHandle.close();
        });

        idleHandle.start();

        final long start = System.currentTimeMillis();
        while (!gotCallback.get() || !gotClose.get()) {
            if (System.currentTimeMillis() - start > TestBase.TIMEOUT) {
                Assert.fail("timeout waiting for idle");
            }
            loop.runNoWait();
        }

        Assert.assertTrue(gotCallback.get());
        Assert.assertTrue(gotClose.get());
        Assert.assertEquals(times.get(), 1);
    }
}
