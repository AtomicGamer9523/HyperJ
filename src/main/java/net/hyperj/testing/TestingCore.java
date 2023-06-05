package net.hyperj.testing;

import net.hyperj.logging.LogFactory;
import org.slf4j.Logger;

import java.lang.reflect.*;
import java.util.*;

public class TestingCore {
    static final Logger LOG = LogFactory.getLogger(Testing.class);
    static Method[] getTestingMethods(Class<?> clazz) {
        LOG.trace("Adding Tests from class '" + clazz.getName() + "'");
        Method[] methods = clazz.getDeclaredMethods();
        ArrayList<Method> res = new ArrayList<>();
        for (Method method : methods) {
            if (isMethodOk(method)) {
                res.add(method);
            }
        }
        Method[] output = new Method[res.size()];
        res.toArray(output);
        return output;
    }
    static boolean isMethodOk(Method method) {
        if (!method.isAnnotationPresent(Testing.Test.class)) return false;
        return method.getParameters().length == 0;
    }
    static Testing.TestingResult runTests(ArrayList<Class<?>> classQueue) {
        ArrayList<Testing.FailedTest> errorsQueue = new ArrayList<>();
        for (Class<?> clazz : classQueue) {
            LOG.debug("Testing: '" + clazz.getName() + "'");
            Method[] methods = getTestingMethods(clazz);
            LOG.debug("Number of Tests: " + methods.length);
            for (Method f : methods) {
                LOG.debug("Testing '" + clazz.getName() + "." + f.getName() + "'");
                try {
                    f.setAccessible(true);
                    f.invoke(null);
                } catch (Exception e) {
                    Testing.FailedTest fail = new Testing.FailedTest(clazz.getName(), f.getName(), e);
                    LOG.warn("Test: '"+ fail.fullName()+"' raised an exception!");
                    errorsQueue.add(fail);
                }
            }
            LOG.trace("Done testing: '" + clazz.getName() + "'");
        }
        Testing.FailedTest[] fails = new Testing.FailedTest[errorsQueue.size()];
        classQueue.clear();
        errorsQueue.toArray(fails);
        return new Testing.TestingResult(fails);
    }
}
