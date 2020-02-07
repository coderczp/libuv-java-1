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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CWDTest extends TestBase {

    private String cwd;

    @Before
    public void before() {
        cwd = LibUV.cwd();
    }

    @After
    public void after() {
        LibUV.chdir(cwd);
    }

    @Test
    public void testCWD() {
        System.out.println("user.dir is " + cwd);
        Assert.assertEquals(System.getProperty("user.dir"), cwd);

        final String home = System.getProperty("user.home");
        System.out.println("user.home is " + home);
        LibUV.chdir(home);
        Assert.assertEquals(home, LibUV.cwd());

        final String java = System.getProperty("java.home");
        System.out.println("java.home is " + java);
        LibUV.chdir(java);
        Assert.assertEquals(java, LibUV.cwd());
    }

}
