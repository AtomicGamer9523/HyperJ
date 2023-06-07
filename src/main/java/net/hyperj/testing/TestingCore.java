package net.hyperj.testing;

import net.hyperj.logging.LogFactory;
import org.slf4j.Logger;

import java.lang.reflect.*;
import java.util.*;

public final class TestingCore {
    static final Logger LOG = LogFactory.getLogger(HyperTesting.class);
    private record Test(Method method, String description) {}
    static Test[] getTestingMethods(Class<?> clazz) {
        LOG.trace("Adding Tests from class '" + clazz.getName() + "'");
        Method[] methods = clazz.getDeclaredMethods();
        ArrayList<Test> res = new ArrayList<>();
        for (Method method : methods) {
            Optional<String> m = isMethodOk(method);
            m.ifPresent(s -> res.add(new Test(method, s)));
        }
        Test[] output = new Test[res.size()];
        res.toArray(output);
        return output;
    }
    static Optional<String> isMethodOk(Method method) {
        if (!method.isAnnotationPresent(HyperTesting.Test.class)) return Optional.empty();
        if (method.getParameters().length != 0) return Optional.empty();
        String description = method.getAnnotation(HyperTesting.Test.class).value();
        return Optional.of(description);
    }
    static HyperTesting.TestingResult runTests(ArrayList<Class<?>> classQueue) {
        ArrayList<HyperTesting.FailedTest> errorsQueue = new ArrayList<>();
        for (Class<?> clazz : classQueue) {
            LOG.debug("Testing: '" + clazz.getName() + "'");
            Test[] methods = getTestingMethods(clazz);
            LOG.debug("Number of Tests: " + methods.length);
            for (Test f : methods) {
                LOG.debug("Testing '" + clazz.getName() + "." + f.method.getName() + "'");
                try {
                    f.method.setAccessible(true);
                    f.method.invoke(null);
                } catch (Exception e) {
                    HyperTesting.FailedTest fail = new HyperTesting.FailedTest(
                        clazz.getName(), f.description, f.method.getName(), e
                    );
                    LOG.warn("Test: '"+ fail.full()+"' raised an exception!");
                    errorsQueue.add(fail);
                }
            }
        }
        HyperTesting.FailedTest[] fails = new HyperTesting.FailedTest[errorsQueue.size()];
        classQueue.clear();
        errorsQueue.toArray(fails);
        return new HyperTesting.TestingResult(fails);
    }
}
