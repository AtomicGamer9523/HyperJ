package net.hyperj.tests.result;

import static net.hyperj.testing.Testing.*;

import net.hyperj.result.*;

@SuppressWarnings("unused")
public class JResultTest {
    private static final Exception EXCEPTION = new IllegalStateException("Illegal State ;)");
    private static final Object GOOD = 69;// ;)
    @Test
    private static void isErrorTest() {
        Result<Object> result = JResult.fail(EXCEPTION);
        assert$(result.isError());
    }
    @Test
    private static void isSuccessTest() {
        Result<Object> result = JResult.success(GOOD);
        assert$(result.isSuccess());
    }
    @Test
    private static void getErrorTest() {
        Result<Object> result = JResult.fail(EXCEPTION);
        assertEQ(result.getError(), EXCEPTION);
    }
    @Test
    private static void getTest1() {
        Result<Object> result = JResult.success(GOOD);
        assertEQ(result.get(), GOOD);
    }
    @Test
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
