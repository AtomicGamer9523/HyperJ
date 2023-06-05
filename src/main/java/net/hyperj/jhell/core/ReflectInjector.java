package net.hyperj.jhell.core;

import net.hyperj.injections.*;
import net.hyperj.result.*;
import net.hyperj.jhell.*;

import java.lang.reflect.*;

public class ReflectInjector {
    public static Result<MethodInject> getPrivateMethodWithPriority(
        int priority, Class<?> clazz, Object invoker, String name, Class<?>... reflectionParams
    ) {
        Result<Object> output = JResult.runUnchecked(() -> {
            Method method = clazz.getDeclaredMethod(name, reflectionParams);
            DummyObject dummyObject = DummyObject.getInstance();
            boolean accessibility = method.canAccess(dummyObject);
            if (!isValid(method, priority)) return JResult.fail(
                new IllegalAccessException("Your Priority '" + priority + "' isn't high enough!")
            );
            Result<MethodInject> res = SecurityManagerInjection.getInstance().runBypass(() -> {
                if (!method.trySetAccessible()) throw new SecurityException(
                    "The request was denied by the security manager"
                );
                return new MethodInject(method, invoker, accessibility);
            });
            return res.getOrThrow();
        });
        return JResult.asNewType(output);
    }

    public static Result<Object> readPrivateFieldWithPriority(
        int priority, Class<?> clazz, Object invoker, String name
    ) {
        return JResult.runUnchecked(() -> {
            Field field = clazz.getDeclaredField(name);
            DummyObject dummyObject = DummyObject.getInstance();
            boolean accessibility = field.canAccess(dummyObject);
            if (!isValid(field, priority)) return JResult.fail(
                new IllegalAccessException("Your Priority '" + priority + "' isn't high enough!")
            );
            Result<Object> res = SecurityManagerInjection.getInstance().runBypass(() -> {
                if (!field.trySetAccessible()) throw new SecurityException(
                    "The request was denied by the security manager"
                );
                return field.get(invoker);
            });
            field.setAccessible(accessibility);
            return res.getOrThrow();
        });
    }

    public static Result<Object> writePrivateFieldWithPriority(
            int priority, Class<?> clazz, Object invoker, String name, Object value
    ) {
        return JResult.runUnchecked(() -> {
            Field field = clazz.getDeclaredField(name);
            DummyObject dummyObject = DummyObject.getInstance();
            boolean accessibility = field.canAccess(dummyObject);
            if (!isValid(field, priority)) return JResult.fail(
                new IllegalAccessException("Your Priority '" + priority + "' isn't high enough!")
            );
            Result<Object> result = readPrivateFieldWithPriority(priority, clazz, invoker, name);
            Result<Integer> res = SecurityManagerInjection.getInstance().runBypass(() -> {
                if (!field.trySetAccessible()) throw new SecurityException(
                    "The request was denied by the security manager"
                );
                field.set(invoker, value);
                // Because this function is required to return.
                // return value ignored, will still error though
                return -1;
            });
            field.setAccessible(accessibility);
            if (res.isError()) throw res.getError();
            return result.getOrThrow();
        });
    }


    @DoNotReveal(priority = 1000)
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isValid(AccessibleObject member, int priority) {
        if (!member.isAnnotationPresent(DoNotReveal.class)) return true;
        DoNotReveal annotation = member.getAnnotation(DoNotReveal.class);
        return priority >= annotation.priority();
    }
}
