mkdir -p build
cd build
cmake .. -DCMAKE_TOOLCHAIN_FILE=/home/appveyor/projects/libuv-java/vcpkg/scripts/buildsystems/vcpkg.cmake -DVCPKG_TARGET_TRIPLET=x64-linux
make
strip libuv-java.so
mkdir -p ../resources/META-INF
cp libuv-java.so ../resources/META-INF/libuv-java.so
cd ..