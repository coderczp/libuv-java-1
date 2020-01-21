mkdir build 2> NUL
cd build
cmake .. -G"Visual Studio 15 2017 Win64" -DCMAKE_TOOLCHAIN_FILE=%VCPKG_ROOT%/scripts/buildsystems/vcpkg.cmake -DVCPKG_TARGET_TRIPLET=x64-windows-static
cmake --build . --config Release
cd ..