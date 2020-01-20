mkdir -p build
cd build
cmake .. -DCMAKE_BUILD_TYPE=Release -DCMAKE_TOOLCHAIN_FILE=$HOME/vcpkg/scripts/buildsystems/vcpkg.cmake
make
strip libuv-java.so
mkdir -p ../resources/META-INF
cp libuv-java.so ../resources/META-INF/libuv-java.so
cd ..