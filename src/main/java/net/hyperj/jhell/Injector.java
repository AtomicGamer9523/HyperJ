package net.hyperj.jhell;

import net.hyperj.jhell.core.DefaultInjection;
import net.hyperj.jhell.priority.Priority;
import net.hyperj.logging.LogFactory;

import org.slf4j.Logger;

@SuppressWarnings({"unused", "unchecked"})
public class Injector {
    public static final Logger LOG = LogFactory.getLogger("JHell");
    public static <T> T $(UncheckedFunction<T> f, T backup) {
        LOG.trace("Called '$' with backup: '" + backup.toString() + "'");
        try {
            T t = f.get();
            LOG.trace("f.get succeeded; t: '" + t.toString() + "'");
            return t;
        } catch (Exception e) {
            LOG.trace("f.get failed! returning backup", e);
            return backup;
        }
    }

    public static <T> Injection inject(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        LOG.trace("Injecting into: '" + clazz.getName() + "'");
        return inject(clazz, obj);
    }

    public static <T> Injection inject(Class<T> clazz) {
        LOG.trace("Injecting into: '" + clazz.getName() + "'");
        return inject(clazz, null);
    }

    public static <T> Injection inject$(int priority, T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        LOG.trace("Injecting into: '" + clazz.getName() + "'");
        return injectPriority(priority, clazz, obj);
    }

    public static <T> Injection inject$(int priority, Class<T> clazz) {
        LOG.trace("Injecting into: '" + clazz.getName() + "'");
        return injectPriority(priority, clazz, null);
    }

    public static <T> Injection inject$(Priority priority, T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        LOG.trace("Injecting into: '" + clazz.getName() + "'");
        return injectPriority(priority.anInt(), clazz, obj);
    }

    public static <T> Injection inject$(Priority priority, Class<T> clazz) {
        LOG.trace("Injecting into: '" + clazz.getName() + "'");
        return injectPriority(priority.anInt(), clazz, null);
    }

    @DoNotReveal(priority = 1000)
    private static <T> Injection inject(Class<T> clazz, T instance) {
        return new DefaultInjection<>(clazz, instance);
    }

    @DoNotReveal(priority = 1000)
    private static <T> Injection injectPriority(int priority, Class<T> clazz, T instance) {
        return new DefaultInjection<>(priority, clazz, instance);
    }

    @FunctionalInterface
    interface UncheckedFunction<R> {
        R get() throws Exception;
    }
}
