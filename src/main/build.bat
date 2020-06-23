curl -L https://github.com/libuv/libuv/archive/v1.38.0.tar.gz --output v1.38.0.tar.gz
tar xfz v1.38.0.tar.gz
cd libuv-1.38.0/
mkdir -p build
cd build
cmake .. -DCMAKE_INSTALL_PREFIX=../../install -DCMAKE_BUILD_TYPE=Release -G "Visual Studio 16 2019" -A x64
cmake --build . --config Release
cd ..
cd ..
mkdir build 2> NUL
cd build
cmake .. -DCMAKE_INSTALL_PREFIX=../../install -DCMAKE_BUILD_TYPE=Release -G "Visual Studio 16 2019" -A x64
cmake --build . --config Release
cd ..
mkdir ..\main\resources\META-INF 2> NUL
copy build\libuv-java.so ..\main\resources\META-INF\libuv-java.dll
cd ..
