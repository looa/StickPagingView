#include <jni.h>
#include <string>
#include "org_looa_ndkencode_SNNative.h"
#include "sha256.h"

JNIEXPORT jstring JNICALL Java_org_looa_ndkencode_SNNative_stringFromJNI
        (JNIEnv *env, jclass) {
    std::string hello = "hello world c+++++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jstring JNICALL Java_org_looa_ndkencode_SNNative_encode
        (JNIEnv *env, jclass, jstring, jstring, jstring, jobject) {
    std::string result = "result";
    return env->NewStringUTF(result.c_str());
}