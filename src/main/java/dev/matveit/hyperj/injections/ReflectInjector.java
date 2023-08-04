package dev.matveit.hyperj.injections;

import dev.matveit.hyperj.util.*;
import dev.matveit.hyperj.*;

import java.lang.reflect.*;

public class ReflectInjector<I> implements Injection {
    private final Class<I> clazz;

    private final I obj;
    public ReflectInjector(Class<I> clazz, I obj) {
        this.clazz = clazz;
        this.obj = obj;
    }

    @Override
    public Result<Object> read(String fieldName) {
        return JResult.from(() -> {
            Field field = this.clazz.getDeclaredField(fieldName);
            DummyObject dummyObject = DummyObject.getInstance();
            boolean accessibility = field.canAccess(dummyObject);
            Result<Object> res = SecurityManagerInjection.getInstance().runBypass(() -> {
                if (!field.trySetAccessible()) throw new SecurityException(
                    "The request was denied by the security manager"
                );
                return field.get(this.obj);
            });
            field.setAccessible(accessibility);
            return res.getOrThrow();
        });
    }

    @Override
    public Result<Object> write(String fieldName, Object value) {
        return JResult.from(() -> {
            Field field = this.clazz.getDeclaredField(fieldName);
            DummyObject dummyObject = DummyObject.getInstance();
            boolean accessibility = field.canAccess(dummyObject);
            Result<Object> result = read(fieldName);
            Result<Integer> res = SecurityManagerInjection.getInstance().runBypass(() -> {
                if (!field.trySetAccessible()) throw new SecurityException(
                    "The request was denied by the security manager"
                );
                field.set(this.obj, value);
                // Because this function is required to return.
                // return value ignored, will still error though
                return -1;
            });
            field.setAccessible(accessibility);
            if (res.isError()) throw res.getError();
            return result.getOrThrow();
        });
    }

    @Override
    public Result<Object> call(String methodName, Object... params) {
        return JResult.from(() -> {
            Class<?>[] reflectionParams = getReflectionParams(params);
            Method method = this.clazz.getDeclaredMethod(methodName, reflectionParams);
            DummyObject dummyObject = DummyObject.getInstance();
            boolean accessibility = method.canAccess(dummyObject);
            Object res = SecurityManagerInjection.getInstance().runBypass(() -> {
                if (!method.trySetAccessible()) throw new SecurityException(
                    "The request was denied by the security manager"
                );
                Object result = method.invoke(this.obj, params);
                if (result == null) return JVoid.get();
                method.setAccessible(accessibility);
                return result;
            }).getOrThrow();

            if (res == null) return JVoid.get();
            return res;
        });
    }

    private static Class<?>[] getReflectionParams(Object... params) {
        Class<?>[] res = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof Byte) {
                res[i] = Byte.TYPE;
            } else if (params[i] instanceof Short) {
                res[i] = Short.TYPE;
            } else if (params[i] instanceof Integer) {
                res[i] = Integer.TYPE;
            } else if (params[i] instanceof Long) {
                res[i] = Long.TYPE;
            } else if (params[i] instanceof Float) {
                res[i] = Float.TYPE;
            } else if (params[i] instanceof Double) {
                res[i] = Double.TYPE;
            } else if (params[i] instanceof Character) {
                res[i] = Character.TYPE;
            } else if (params[i] instanceof Boolean) {
                res[i] = Boolean.TYPE;
            } else {
                res[i] = params[i].getClass();
            }
        }
        return res;
    }
}
