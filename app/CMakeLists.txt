cmake_minimum_required(VERSION 3.22.1)

project("andengine" C)

add_library(andengine SHARED src/main/cpp/andengine_jni.c)

target_link_libraries(andengine log GLESv2)
