mkdir -p install
wget https://github.com/libuv/libuv/archive/v1.40.0.tar.gz
tar xfz v1.40.0.tar.gz
cd libuv-1.40.0/
mkdir -p build
cd build
cmake .. -DBUILD_TESTING=OFF -DCMAKE_C_FLAGS=-fPIC -DCMAKE_INSTALL_LIBDIR=lib -DCMAKE_INSTALL_PREFIX=../../install -DCMAKE_BUILD_TYPE=Release
make
make install
cd ..
cd ..
mkdir -p build
cd build
cmake .. -DCMAKE_INSTALL_PREFIX=../../install -DCMAKE_BUILD_TYPE=Release
make
cd ..
mkdir -p ../main/resources/META-INF
cp build/libuv-java.{so,dylib} ../main/resources/META-INF
cd ..