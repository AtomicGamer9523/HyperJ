package net.hyperj;

import net.hyperj.injections.HyperThread;
import net.hyperj.logging.LogFactory;
import net.hyperj.jhell.DoNotReveal;

import org.slf4j.Logger;

public class HyperJ {
    @DoNotReveal(priority = 1000)
    private static final Logger LOGGER = LogFactory.getLogger();
    @DoNotReveal(priority = 1000)
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

    @DoNotReveal(priority = 1000)
    private static void handleException(String pkg, Throwable e) {
        if (e instanceof ClassNotFoundException) {
            LOGGER.error("Class '" + pkg + "' not found!", e);
            return;
        }
        if (e instanceof NoSuchMethodException) {
            LOGGER.error("Method '" + METHOD + "' in class '" + pkg + "' not found!", e);
            return;
        }
        if (e instanceof SecurityException || e instanceof IllegalAccessException) {
            LOGGER.error("Illegal access to method '" + METHOD + "' in class '" + pkg + "' !", e);
            return;
        }
        if (e instanceof ExceptionInInitializerError) {
            LOGGER.error("Method '" + METHOD + "' raised an exception!", e);
            return;
        }
        if (e instanceof NullPointerException) {
            LOGGER.error("Method '" + METHOD + "' is not static", e);
            return;
        }
        if (e instanceof IllegalArgumentException) {
            LOGGER.error("Method '" + METHOD + "' doesn't accept 'String[]' !", e);
            return;
        }
        LOGGER.error("Method '" + METHOD + "' raised an exception!", e);
        throw new RuntimeException(e);
    }
}
