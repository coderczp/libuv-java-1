# libuv-java

[![Build](https://github.com/webfolderio/libuv-java/workflows/libuv-java/badge.svg)](https://github.com/webfolderio/libuv-java/actions?query=workflow%3Alibuv-java)

Java binding for [libuv](https://github.com/libuv/libuv).

Supported Java Versions
-----------------------

Oracle & OpenJDK Java 8, 11.

Both the JRE and the JDK are suitable for use with this library.

Stability
---------
This library is suitable for use in production systems.

Supported Platforms
-------------------
* Windows 8+
* Ubuntu & CentOS
* macOS

How it is tested
----------------
libuv-java is regularly tested on [github actions](https://github.com/webfolderio/libuv-java/actions?query=workflow%3Alibuv-java).

Download
--------

[libuv-java-1.0.1.jar](https://github.com/webfolderio/libuv-java/releases/download/1.0.1/libuv-java-1.0.1.jar) - 291 KB

[libuv-java-1.0.1-sources.jar](https://github.com/webfolderio/libuv-java/releases/download/1.0.1/libuv-java-1.0.1-sources.jar) - 302 KB

[libuv-java-1.0.1-javadoc.jar](https://github.com/webfolderio/libuv-java/releases/download/1.0.1/libuv-java-1.0.1-javadoc.jar) - 608 KB

Maven Integration
-----------------

To use the official release of libuv-java, please use the following snippet in your `pom.xml` file.

Add the following to your POM's `<dependencies>` tag:

```xml
<dependency>
    <groupId>io.webfolder</groupId>
    <artifactId>libuv-java</artifactId>
    <version>1.0.1</version>
</dependency>
```

License
-------
Licensed under the [GNU General Public License v2.0](https://github.com/webfolderio/libuv-java/blob/master/LICENSE).
