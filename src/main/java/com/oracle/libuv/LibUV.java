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

import java.util.Objects;

public final class LibUV {

    static {
        System.load("C:\\libuv-java\\src\\main\\build\\Release\\libuv-java.dll");
    }

    private LibUV() {
    }

    // misc

    public static String version() {
        return _version();
    }

    public static void disableStdioInheritance() {
        _disable_stdio_inheritance();
    }

    // process

    public static String exePath() {
        return _exe_path();
    }

    public static String cwd() {
        return _cwd();
    }

    public static void chdir(final String dir) {
        _chdir(dir);
    }

    public static String getTitle() {
        return _getTitle();
    }

    public static void setTitle(final String value) {
        Objects.requireNonNull(value);
        _setTitle(value);
    }

    public static int kill(final int pid, final int signal) {
        return _kill(pid, signal);
    }

    public static int rss() {
        return _rss();
    }

    // os

    public static double getUptime() {
        return _getUptime();
    }

    public static double[] getLoadAvg() {
        return _getLoadAvg();
    }

    public static double getTotalMem() {
        return _getTotalMem();
    }

    public static double getFreeMem() {
        return _getFreeMem();
    }

    public static Object[] getCPUs() {
        return _getCPUs();
    }

    public static boolean isIPv6(final String ip) {
        Objects.requireNonNull(ip);
        return _isIPv6(ip);
    }

    // misc

    private static native String _version();

    private static native void _disable_stdio_inheritance();

    // process

    private static native String _exe_path();

    private static native String _cwd();

    private static native void _chdir(String dir);

    private static native String _getTitle();

    private static native void _setTitle(String value);

    private static native int _kill(int pid, int signal);

    private static native int _rss();

    // os

    private static native double _getUptime();

    private static native double[] _getLoadAvg();

    private static native double _getTotalMem();

    private static native double _getFreeMem();

    private static native Object[] _getCPUs();

    // dns

    private static native boolean _isIPv6(String ip);

}
