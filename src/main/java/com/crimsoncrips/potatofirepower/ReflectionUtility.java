//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.crimsoncrips.potatofirepower;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtility {
    public ReflectionUtility() {
    }

    public static Object createInstance(String className, Class<?>[] argTypes, Object[] args) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getDeclaredConstructor(argTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (Exception var5) {
            return null;
        }
    }

    public static void setField(Object object, String name, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception var4) {
            System.out.println("ERROR");
        }

    }

    public static Object getField(Object object, String name) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception var3) {
            return null;
        }
    }

    public static Object callMethod(Object object, String name, Class<?>[] argTypes, Object[] args) {
        try {
            Method method = object.getClass().getDeclaredMethod(name, argTypes);
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception var5) {
            return null;
        }
    }
}
