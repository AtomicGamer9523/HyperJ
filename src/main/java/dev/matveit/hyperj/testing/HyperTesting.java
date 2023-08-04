package dev.matveit.hyperj.testing;

import java.lang.annotation.*;
import java.util.*;

/**
 * A simple testing thing I made
 * <p>
 * Example: <pre>{@code
 * import static net.hyperj.testing.HyperTesting.*;
 *
 * public class Tests {
 *     private static final Testing TESTS = initTesting();
 *     public static void main(String[] args) {
 *         TESTS.add(Tests.class);
 *         TESTS.run().finish();
 *     }
 *     @Test("Ensures that 5 + 5 = 10")
 *     static void myTest() {
 *         assertEQ(5 + 5, 10);
 *     }
 * }
 * }</pre>
 */
@SuppressWarnings("unused")
public class HyperTesting {
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
     * For an example see {@link HyperTesting this}
     *
     * @return a new {@link HyperTesting Testing} instance
     */
    public static Testing initTesting() {
        return new Testing();
    }

    /**
     * The Testing Infrastructure. For example see {@link HyperTesting this}
     */
    public static final class Testing {
        private final ArrayList<Class<?>> classQueue = new ArrayList<>();

        private Testing() {
            TestingCore.LOG.debug(
                "Initialized a new testing environment! Thread: '" +
                Thread.currentThread().getName() + "'"
            );
        }

        /**
         * Adds a class that should be tested
         * <p>
         * For an example see {@link HyperTesting this}
         *
         * @param clazz the class to test
         */
        public void add(Class<?> clazz) {
            TestingCore.LOG.trace("Added class '" + clazz.getName() + "' to queue");
            this.classQueue.add(clazz);
        }

        /**
         * Runs the tests
         *
         * @return the {@link TestingResult result of the tests}
         */
        public TestingResult run() {
            TestingCore.LOG.info("Starting tests for " + this.classQueue.size() + " classes!");
            return TestingCore.runTests(this.classQueue);
        }
    }

    /**
     * A simple record to structure all the failed tests (array of {@link FailedTest FailedTest})
     *
     * @param failedTests automatically created via {@link Testing#run() Testing.run()}
     */
    public record TestingResult(FailedTest[] failedTests) {
        /**
         * Finishes printing all the failed tests as warnings.
         * Exit code is the amount of tests failed
         */
        public void finish() {
            finish(false);
        }

        /**
         * Finishes printing all the failed tests as warnings, and the descriptions with exceptions
         * Exit code is the amount of tests failed
         */
        public void finishVerbose() {
            for (FailedTest fail : this.failedTests) {
                fail.reason.printStackTrace();
            }
            finish(true);

        }

        /**
         * Finishes Silently.
         * Exit code is the amount of tests failed
         */
        public void silentFinish() {
            System.exit(this.failedTests.length);
        }

        private void finish(boolean verbose) {
            for (FailedTest fail : this.failedTests) {
                fail.logAsError();
                if(verbose) fail.logAsDescriptionError();
            }
            silentFinish();
        }
    }

    public record FailedTest(String className, String description, String methodName, Exception reason) {
        /**
         * Gets the full identification of the failed function
         *
         * @return full path of class AND the method name
         */
        public String full() {
            return this.className + "." + this.methodName;
        }

        /**
         * Logs the fail as an error
         */
        public void logAsError() {
            TestingCore.LOG.error(
                "Test: '" + this.full() + "' raised an exception!", this.reason
            );
        }

        /**
         * Logs the fail as a description of the test and the cause of the failure
         */
        public void logAsDescriptionError() {
            TestingCore.LOG.error(
                "'" + this.description + "', failed because: " + this.reason.getCause().getMessage()
            );
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Test {
        /**
         * Brief description of the test
         *
         * @return the description
         */
        String value() default "Default Test Description";
    }
}
