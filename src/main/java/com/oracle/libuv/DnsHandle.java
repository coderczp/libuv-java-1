package com.oracle.libuv;

import static java.util.Objects.requireNonNull;

public class DnsHandle {

    private final long pointer;

    private final LoopHandle loop;

    private DnsCallback onDns;

    static {
        _static_initialize();
    }

    DnsHandle(final LoopHandle loop) {
        requireNonNull(loop);
        this.loop = loop;
        this.pointer = _new(loop.pointer());
        _initialize(pointer);
    }

    public void setDnsCallback(final DnsCallback callback) {
        if (onDns != null) {
            throw new IllegalStateException();
        }
        onDns = callback;
    }

    public void getAddressInfo(String node, int service) {
        _uv_getaddrinfo(pointer, node, String.valueOf(service));
    }

    private void callback(final Address address,
                          final int     status) {
        if (onDns != null) {
            loop.getCallbackHandler()
                .handleDnsCallback(onDns, address, status);
        }

    }

    // ------------------------------------------------------------------------
    // ~ Native
    // ------------------------------------------------------------------------

    private native void _initialize(long ptr);

    private static native long _new(final long loop);

    private static native void _static_initialize();

    private native int  _uv_getaddrinfo(final long   ptr,
                                        final String node,
                                        final String service);
}
