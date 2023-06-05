package net.hyperj.logging;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class LogFactory {
    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getSimpleName());
    }
    public static Logger getLogger(String name) {
        return LoggerFactory.getLogger("HyperJ.");
    }
    public static Logger getLogger() {
        return LoggerFactory.getLogger("HyperJ");
    }
}
