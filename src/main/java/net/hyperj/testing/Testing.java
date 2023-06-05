package net.hyperj.testing;

import net.hyperj.jhell.DoNotReveal;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;

/**
 * A simple testing thing I made
 * <p>
 * Example: <pre>{@code
 *
 * }</pre>
 */
public class Testing {
    /**
     * Asserts that the statement is true
     *
     * @param value the statement to check
     */
    public static void assert$(boolean value) {
        assertEQ(value, true);
    }

    /**
     * Asserts that the two statements are equal
     *
     * @param r   statement on the right
     * @param l   statement on the left
     * @param <T> Their common type
     */
    public static <T> void assertEQ(T r, T l) {
        if (r.equals(l)) {
            TestingCore.LOG.trace("r equals l");
        } else {
            throw new RuntimeException("'" + r + "' and '" + l + "' are NOT EQUAL");
        }
    }

    /**
     * Initializes the testing system.
     * <p>
     * For an example see {@link Testing this}
     *
     * @return a new {@link Testing Testing} instance
     */
    public static Testing initTesting() {
        return new Testing();
    }

    @DoNotReveal(priority = 100)
    private final ArrayList<Class<?>> classQueue = new ArrayList<>();

    /**
     * Adds a class that should be tested
     * <p>
     * For an example see {@link Testing this}
     *
     * @param clazz the class to test
     * @return {@link Testing the current instance}
     */
    public Testing add(Class<?> clazz) {
        TestingCore.LOG.trace("Added class'" + clazz.getName() + "' to queue");
        this.classQueue.add(clazz);
        return this;
    }

    /**
     * Runs the tests
     * @return the {@link TestingResult result of the tests}
     */
    public TestingResult run() {
        return TestingCore.runTests(this.classQueue);
    }

    /**
     *
     * @param failedTests
     */
    public record TestingResult(FailedTest[] failedTests) {
        public void finish() {
            for (FailedTest fail : this.failedTests) {
                fail.logAsError();
            }
            System.exit(this.failedTests.length);
        }
    }

    public record FailedTest(String className, String methodName, Exception reason) {
        public String fullName() {
            return this.className + "." + this.methodName;
        }

        public void logAsError() {
            TestingCore.LOG.error("Test: '" + this.fullName() + "' raised an exception!", this.reason);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Test {
    }
}
