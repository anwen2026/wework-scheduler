package de.robv.android.xposed;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public final class XposedHelpers {
    public static Object callMethod(Object obj, String methodName, Object... args) { return null; }
    public static Object callStaticMethod(Class<?> clazz, String methodName, Object... args) { return null; }
    public static Object getObjectField(Object obj, String fieldName) { return null; }
    public static void setObjectField(Object obj, String fieldName, Object value) {}
    public static void setIntField(Object obj, String fieldName, int value) {}
    public static void setLongField(Object obj, String fieldName, long value) {}
    public static Class<?> findClass(String className, ClassLoader classLoader) { return null; }
    public static Member findAndHookMethod(String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback) { return null; }
    public static Member findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) { return null; }
    public static Object newInstance(Class<?> clazz, Object... args) { return null; }
    public static Method findMethodExact(Class<?> clazz, String methodName, Class<?>... parameterTypes) { return null; }
    public static Field findField(Class<?> clazz, String fieldName) { return null; }
    
    public static class ClassNotFoundError extends Error {
        public ClassNotFoundError(Throwable cause) { super(cause); }
        public ClassNotFoundError(String detailMessage, Throwable cause) { super(detailMessage, cause); }
    }
}
