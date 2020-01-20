cmake_minimum_required(VERSION 3.8.0)

project(libuv-java CXX)

set(CMAKE_POSITION_INDEPENDENT_CODE ON)
set(CMAKE_SUPPRESS_REGENERATION ON)
set(CMAKE_CXX_STANDARD 11)
set(THREADS_PREFER_PTHREAD_FLAG ON)

if(MSVC)
    add_definitions(-D_WIN32_WINNT=0x0601)
endif()

find_package(JNI REQUIRED)
find_package(Threads REQUIRED)
find_package(unofficial-libuv CONFIG REQUIRED)

INCLUDE_DIRECTORIES(${JAVA_INCLUDE_PATH})
INCLUDE_DIRECTORIES(${JAVA_INCLUDE_PATH2})

file(GLOB_RECURSE sources "*native/.h" "native/*.cpp")

add_library(libuv-java SHARED ${sources})

target_link_libraries(libuv-java PRIVATE Threads::Threads unofficial::libuv::libuv)

set_property(DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR} PROPERTY VS_STARTUP_PROJECT libuv-java)