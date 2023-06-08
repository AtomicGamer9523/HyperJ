package net.hyperj.util;

import net.hyperj.HyperJ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggerUtil {
    private static final String NAME = HyperJ.class.getSimpleName();
    public static Logger getLogger(Class<?> clazz) {
        return getBaseLogger(clazz.getSimpleName());
    }
    public static Logger getLogger(Object obj) {
        return getBaseLogger(obj.getClass().getSimpleName());
    }
    public static Logger getBaseLogger(String val) {
        return LoggerFactory.getLogger(NAME+"."+val);
    }
    public static Logger getBaseLogger() {
        return LoggerFactory.getLogger(NAME);
    }
}
