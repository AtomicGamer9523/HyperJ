package dev.matveit.hyperj.util;

import static dev.matveit.hyperj.testing.HyperTesting.*;
import dev.matveit.hyperj.*;

@SuppressWarnings("unused")
public class JResultTest {
    private static final Exception EXCEPTION = new IllegalStateException("Illegal State ;)");
    private static final Object GOOD = 69;// ;)

    @Test("Make sure if an error is provided it will return true on 'isError'")
    private static void isErrorTest() {
        Result<Object> result = JResult.fail(EXCEPTION);
        assert$(result.isError());
    }

    @Test("Make sure if a good value is provided it will return true on 'isSuccess'")
    private static void isSuccessTest() {
        Result<Object> result = JResult.success(GOOD);
        assert$(result.isSuccess());
    }

    @Test("Make sure if an error is provided it will return the error via 'getError'")
    private static void getErrorTest() {
        Result<Object> result = JResult.fail(EXCEPTION);
        assertEQ(result.getError(), EXCEPTION);
    }

    @Test("Make sure get() actually returns the expected value")
    private static void getTest1() {
        Result<Object> result = JResult.success(GOOD);
        assertEQ(result.get(), GOOD);
    }

    @Test("Make sure get() throws an exception")
    private static void getTest2() {
        try {
            Result<Object> result = JResult.fail(EXCEPTION);
            result.get();
        } catch (Exception e) {
            return;
        }
        throw new IllegalStateException("Impossible to get a value from a result that was a fail");
    }
}
