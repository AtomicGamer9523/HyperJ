package net.hyperj;

import net.hyperj.injections.*;
import net.hyperj.util.*;

import org.slf4j.Logger;

public final class HyperJ {
    private static final Logger LOG = LoggerUtil.getBaseLogger();

    @SuppressWarnings("unchecked")
    public static <T> Injection inject(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        LOG.trace("Injecting into: '" + clazz.getName() + "'");
        return new ReflectInjector<>(clazz, obj);
    }

    public static <T> Injection inject(Class<T> clazz) {
        LOG.trace("Injecting into: '" + clazz.getName() + "'");
        return new ReflectInjector<>(clazz, null);
    }
    public static void launch(String appPackage, String[] args) {
        RuntimeInjector.launch(appPackage, args);
    }
}
