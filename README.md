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

[libuv-java-1.0.0.jar](https://github.com/webfolderio/libuv-java/releases/download/1.0.0/libuv-java-1.0.0.jar) - 291 KB

Maven Integration
-----------------
Install artifacts for Windows:
```sh
mvn deploy:deploy-file -DgroupId=io.webfolder -DartifactId=libuv-java -Dversion=1.0.0 -Dfile=libuv-java-1.0.0.jar -Dpackaging=jar -Durl=file://%USERPROFILE%\.m2
mvn deploy:deploy-file -DgroupId=io.webfolder -DartifactId=libuv-java -Dversion=1.0.0 -Dfile=libuv-java-1.0.0-sources.jar -Dpackaging=jar -Dclassifier=sources -Durl=file://%USERPROFILE%\.m2
mvn deploy:deploy-file -DgroupId=io.webfolder -DartifactId=libuv-java -Dversion=1.0.0 -Dfile=libuv-java-1.0.0-javadoc.jar -Dpackaging=jar -Dclassifier=javadoc -Durl=file://%USERPROFILE%\.m2
```

Install artifacts for Nix:

```sh
mvn deploy:deploy-file -DgroupId=io.webfolder -DartifactId=libuv-java -Dversion=1.0.0 -Dfile=libuv-java-1.0.0.jar -Dpackaging=jar -Durl=file://%HOME%\.m2
mvn deploy:deploy-file -DgroupId=io.webfolder -DartifactId=libuv-java -Dversion=1.0.0 -Dfile=libuv-java-1.0.0-sources.jar -Dpackaging=jar -Dclassifier=sources -Durl=file://%HOME%\.m2
mvn deploy:deploy-file -DgroupId=io.webfolder -DartifactId=libuv-java -Dversion=1.0.0 -Dfile=libuv-java-1.0.0-javadoc.jar -Dpackaging=jar -Dclassifier=javadoc -Durl=file://%HOME%\.m2
```

License
-------
Licensed under the [GNU General Public License v2.0](https://github.com/webfolderio/libuv-java/blob/master/LICENSE).
