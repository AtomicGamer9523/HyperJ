package dev.matveit.hyperj.injections;

import dev.matveit.hyperj.util.*;

import org.slf4j.Logger;

public class RuntimeInjector {
    private static final Logger LOG = LoggerUtil.getLogger(RuntimeInjector.class);
    private static final String METHOD = "main$";
    public static void launch(String appPackage, String[] args) {
        HyperThread thread = HyperThread.withHyper(() -> {
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                Class<?> clazz = loader.loadClass(appPackage);
                java.lang.reflect.Method method = clazz.getDeclaredMethod(METHOD, String[].class);
                method.invoke(null, (Object) args);
            } catch (Throwable e) {
                handleException(appPackage, e);
            }
        });
        thread.start();
    }

    private static void handleException(String pkg, Throwable e) {
        if (e instanceof ClassNotFoundException) {
            LOG.error("Class '" + pkg + "' not found!", e);
            return;
        }
        if (e instanceof NoSuchMethodException) {
            LOG.error("Method '" + METHOD + "' in class '" + pkg + "' not found!", e);
            return;
        }
        if (e instanceof SecurityException || e instanceof IllegalAccessException) {
            LOG.error("Illegal access to method '" + METHOD + "' in class '" + pkg + "' !", e);
            return;
        }
        if (e instanceof ExceptionInInitializerError) {
            LOG.error("Method '" + METHOD + "' raised an exception!", e);
            return;
        }
        if (e instanceof NullPointerException) {
            LOG.error("Method '" + METHOD + "' is not static", e);
            return;
        }
        if (e instanceof IllegalArgumentException) {
            LOG.error("Method '" + METHOD + "' doesn't accept 'String[]' !", e);
            return;
        }
        LOG.error("Method '" + METHOD + "' raised an exception!", e);
        throw new RuntimeException(e);
    }
}
