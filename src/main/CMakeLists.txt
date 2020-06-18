cmake_minimum_required(VERSION 3.8.0)

project(uv-java CXX)

set(CMAKE_POSITION_INDEPENDENT_CODE ON)
set(CMAKE_SUPPRESS_REGENERATION ON)
set(CMAKE_CXX_STANDARD 11)
set(THREADS_PREFER_PTHREAD_FLAG ON)

find_package(JNI REQUIRED)
find_package(Threads REQUIRED)

INCLUDE_DIRECTORIES(${JAVA_INCLUDE_PATH})
INCLUDE_DIRECTORIES(${JAVA_INCLUDE_PATH2})

include_directories("${CMAKE_CURRENT_SOURCE_DIR}/install/include")

file(GLOB_RECURSE sources "*native/libuv-java/private/*.h" "*native/libuv-java/jni/*.h" "native/*.cpp")

add_library(uv-java SHARED ${sources})

if(WIN32)
  add_definitions(-D_WIN32_WINNT=0x0601)
  set_target_properties(uv-java PROPERTIES PREFIX "")
endif()

if (CMAKE_BUILD_TYPE MATCHES Release)
  set_target_properties(uv-java PROPERTIES LINK_FLAGS_RELEASE -s)
endif()

if(WIN32)
  target_link_libraries(uv-java PRIVATE "${CMAKE_CURRENT_SOURCE_DIR}/install/lib/libuv_a.lib")
else()
  target_link_libraries(uv-java PRIVATE "${CMAKE_CURRENT_SOURCE_DIR}/install/lib/libuv_a.a")
endif()