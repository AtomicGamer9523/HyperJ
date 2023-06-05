package net.hyperj.jhell.core;

import net.hyperj.result.*;
import net.hyperj.jhell.*;

import java.lang.reflect.*;

public class MethodInject {
    @DoNotReveal(priority = 1000)
    private final boolean accessibility;
    @DoNotReveal(priority = 1000)
    private final Object invoker;
    @DoNotReveal(priority = 1000)
    private final Method method;

    @DoNotReveal(priority = 1000)
    MethodInject(Method method, Object invoker, boolean accessibility) {
        this.accessibility = accessibility;
        this.invoker = invoker;
        this.method = method;
    }

    @DoNotReveal(priority = 1000)
    Result<Object> call(Object... params) {
        return JResult.runUnchecked(() -> {
            Object res = this.method.invoke(this.invoker, params);
            this.method.setAccessible(this.accessibility);
            return res;
        });
    }
}
