# libuv-java

[![Build](https://github.com/webfolderio/libuv-java/workflows/libuv-java/badge.svg)](https://github.com/webfolderio/libuv-java/actions?query=workflow%3Alibuv-java)

libuv bindings for Java.

[Libuv](https://github.com/libuv/libuv) is a cross platform asynchronous IO implementation that powers NodeJS. It supports sockets, both UDP and TCP, filesystem watch, TTY, 
Pipes and other asynchronous primitives like timer, check, prepare and idle.

Supported Java Versions
-----------------------

Oracle & OpenJDK Java 8, 11, & GraalVM native-image.

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

[libuv-java-1.2.0.jar](https://repo1.maven.org/maven2/io/webfolder/libuv-java/1.2.0/libuv-java-1.2.0.jar) - 300KB

[libuv-java-1.2.0-sources.jar](https://repo1.maven.org/maven2/io/webfolder/libuv-java/1.2.0/libuv-java-1.2.0-sources.jar) - 310 KB

[libuv-java-1.2.0-javadoc.jar](https://repo1.maven.org/maven2/io/webfolder/libuv-java/1.2.0/libuv-java-1.2.0-javadoc.jar) - 252 KB

Maven Integration
-----------------

To use the official release of libuv-java, please use the following snippet in your `pom.xml` file.

Add the following to your POM's `<dependencies>` tag:

```xml
<dependency>
    <groupId>io.webfolder</groupId>
    <artifactId>libuv-java</artifactId>
    <version>1.2.0</version>
</dependency>
```

License
-------
Licensed under the [GNU General Public License v2.0](https://github.com/webfolderio/libuv-java/blob/master/LICENSE).
