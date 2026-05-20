#include <jni.h>
#include <GLES2/gl2.h>
#include <stdlib.h>
#include <string.h>

JNIEXPORT void JNICALL
Java_org_andengine_opengl_GLES20Fix_glVertexAttribPointer(
        JNIEnv   *env,
        jclass    clazz,
        jint      indx,
        jint      size,
        jint      type,
        jboolean  normalized,
        jint      stride,
        jobject   buffer)
{
    glVertexAttribPointer(
        (GLuint)   indx,
        (GLint)    size,
        (GLenum)   type,
        (GLboolean)normalized,
        (GLsizei)  stride,
        (*env)->GetDirectBufferAddress(env, buffer));
}

JNIEXPORT void JNICALL
Java_org_andengine_opengl_GLES20Fix_glDrawElements(
        JNIEnv  *env,
        jclass   clazz,
        jint     mode,
        jint     count,
        jint     type,
        jobject  indices)
{
    glDrawElements(
        (GLenum) mode,
        (GLsizei)count,
        (GLenum) type,
        (*env)->GetDirectBufferAddress(env, indices));
}

JNIEXPORT void JNICALL
Java_org_andengine_opengl_util_BufferUtils_jniPut(
        JNIEnv      *env,
        jclass       clazz,
        jobject      dstBuffer,
        jfloatArray  src,
        jint         length,
        jint         dstOffset)
{
    void   *dst  = (*env)->GetDirectBufferAddress(env, dstBuffer);
    jfloat *data = (*env)->GetFloatArrayElements(env, src, NULL);
    memcpy((char *)dst + dstOffset * sizeof(jfloat),
           data,
           (size_t)length * sizeof(jfloat));
    (*env)->ReleaseFloatArrayElements(env, src, data, JNI_ABORT);
}

JNIEXPORT jobject JNICALL
Java_org_andengine_opengl_util_BufferUtils_jniAllocateDirect(
        JNIEnv *env,
        jclass  clazz,
        jint    capacity)
{
    void *buf = malloc((size_t)capacity);
    if (buf == NULL) return NULL;
    memset(buf, 0, (size_t)capacity);
    return (*env)->NewDirectByteBuffer(env, buf, (jlong)capacity);
}

JNIEXPORT void JNICALL
Java_org_andengine_opengl_util_BufferUtils_jniFreeDirect(
        JNIEnv  *env,
        jclass   clazz,
        jobject  buffer)
{
    void *buf = (*env)->GetDirectBufferAddress(env, buffer);
    if (buf != NULL) {
        free(buf);
    }
}
