package com.oracle.libuv;

@FunctionalInterface
public interface DnsCallback {

    void onAddress(Address address, int status) throws Exception;
}
