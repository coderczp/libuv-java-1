mkdir -p install
wget https://github.com/libuv/libuv/archive/v1.38.0.tar.gz
tar xfz v1.38.0.tar.gz
cd libuv-1.38.0/
mkdir -p build
cd build
cmake .. -DCMAKE_C_FLAGS=-fPIC -DCMAKE_INSTALL_LIBDIR=lib -DCMAKE_INSTALL_PREFIX=../../install -DCMAKE_BUILD_TYPE=Release -GNinja
ninja
ninja install
cd ..
cd ..
mkdir -p build
cd build
cmake .. -DCMAKE_INSTALL_PREFIX=../../install -DCMAKE_BUILD_TYPE=Release -GNinja
ninja
cd ..
mkdir -p ../main/resources/META-INF
cp build/libuv-java.so ../main/resources/META-INF/libuv-java.so
cd ..
