package net.hyperj.jhell.core;

import net.hyperj.jhell.priority.*;
import net.hyperj.jhell.*;

import net.hyperj.misc.JVoid;
import net.hyperj.result.*;

public class DefaultInjection<I> implements PriorityInjection {
    @DoNotReveal(priority = 1000)
    private int priority = Priority.DEFAULT.anInt();
    @DoNotReveal(priority = 1000)
    private final Class<I> clazz;
    @DoNotReveal(priority = 1000)
    private final I obj;

    public DefaultInjection(Class<I> clazz, I obj) {
        this.clazz = clazz;
        this.obj = obj;
    }

    public DefaultInjection(int priority, Class<I> clazz, I obj) {
        this.priority = priority;
        this.clazz = clazz;
        this.obj = obj;
    }

    @Override
    public Result<Object> read(String fieldName) {
        return read$(fieldName);
    }

    @Override
    public Result<Object> write(String fieldName, Object value) {
        return write$(fieldName, value);
    }

    @Override
    public Result<Object> call(String methodName, Object... params) {
        return call$(methodName, params);
    }

    public Result<Object> read$(String fieldName) {
        return ReflectInjector.readPrivateFieldWithPriority(
            this.priority, this.clazz, this.obj, fieldName
        );
    }

    public Result<Object> write$(String fieldName, Object value) {
        return ReflectInjector.writePrivateFieldWithPriority(
            this.priority, this.clazz, this.obj, fieldName, value
        );
    }

    public Result<Object> call$(String methodName, Object... params) {
        Class<?>[] reflectionParams = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof Byte) {
                reflectionParams[i] = Byte.TYPE;
            } else if (params[i] instanceof Short) {
                reflectionParams[i] = Short.TYPE;
            } else if (params[i] instanceof Integer) {
                reflectionParams[i] = Integer.TYPE;
            } else if (params[i] instanceof Long) {
                reflectionParams[i] = Long.TYPE;
            } else if (params[i] instanceof Float) {
                reflectionParams[i] = Float.TYPE;
            } else if (params[i] instanceof Double) {
                reflectionParams[i] = Double.TYPE;
            } else if (params[i] instanceof Character) {
                reflectionParams[i] = Character.TYPE;
            } else if (params[i] instanceof Boolean) {
                reflectionParams[i] = Boolean.TYPE;
            } else {
                reflectionParams[i] = params[i].getClass();
            }
        }
        return JResult.runUnchecked(() -> {
            Object res = ReflectInjector.getPrivateMethodWithPriority(
                this.priority, this.clazz, this.obj, methodName, reflectionParams
            ).getOrThrow().call(params).getOrThrow();
            if (res == null) return JVoid.get();
            return res;
        });
    }
}
