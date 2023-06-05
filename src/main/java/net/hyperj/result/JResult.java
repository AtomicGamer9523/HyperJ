package net.hyperj.result;

import net.hyperj.misc.JVoid;

import java.util.function.*;
import java.util.*;

public final class JResult<S> implements Result<S> {
    /**
     * Create a successful Result.
     *
     * @param s the result value, must be non-null.
     * @return the successful result.
     */
    public static <S> Result<S> success(S s) {
        return new JResult<>(null, Objects.requireNonNull(s));
    }

    /**
     * Create a failed Result.
     *
     * @param e the error, must be non-null.
     * @return the erroneous Result.
     */
    public static <S> Result<S> fail(Exception e) {
        return new JResult<>(Objects.requireNonNull(e), null);
    }

    /**
     * Creates a Result based on what happens when the given
     * supplier is called. If a value is returned, the Result
     * will be successful. If an exception is thrown, the
     * Result will be a failure.
     *
     * @param s the value supplier.
     * @return the Result.
     */
    public static <S> Result<S> from(ThrowingSupplier<S> s) {
        Objects.requireNonNull(s);
        try {
            return success(s.get());
        } catch (Exception t) {
            return fail(t);
        }
    }

    private final Exception e;
    private final S s;

    private JResult(Exception e, S s) {
        this.e = e;
        this.s = s;
    }

    @Override
    public boolean isError() {
        return e != null;
    }

    @Override
    public boolean isSuccess() {
        return s != null;
    }

    @Override
    public Exception getError() {
        if (!isError()) throw new NoSuchElementException("Attempted to retrieve error on non-erroneous result");
        return e;
    }

    @Override
    public S get() {
        if (isError()) throw new NoSuchElementException(e);
        return s;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean isVoid() {
        if (isError()) return false;
        S val = get();
        return val.getClass().equals(JVoid.class) || val.equals(JVoid.get()) || val == null;
    }

    @Override
    public S getOrElse(S def) {
        if (isError()) return def;
        return s;
    }

    @Override
    public S getOrElse(Function<Exception, S> f) {
        if (isError()) return f.apply(e);
        return s;
    }

    @Override
    public S getOrThrow() throws Exception {
        if (isError()) throw e;
        return s;
    }

    @Override
    public <N> Result<N> map(Function<S, N> f) {
        if (this.isError()) return JResult.fail(this.getError());
        return JResult.from(() -> f.apply(s));
    }

    @Override
    public Result<S> mapError(Function<Exception, Exception> f) {
        if (isError()) return JResult.fail(f.apply(e));
        return this;
    }

    @Override
    public Result<S> ifError(Consumer<Exception> consumer) {
        if (isError()) consumer.accept(e);
        return this;
    }

    @Override
    public Result<S> ifSuccess(Consumer<S> consumer) {
        if (isSuccess()) consumer.accept(s);
        return this;
    }

    @Override
    public Result<S> wrapError(BiFunction<String, Exception, Exception> f, String message) {
        if (isError()) return JResult.fail(f.apply(message, e));
        return this;
    }

    @Override
    public Result<S> wrapError(Function<Exception, Exception> f) {
        return mapError(f);
    }

    @Override
    public Optional<S> asOptional() {
        return Optional.ofNullable(s);
    }

    @Override
    public Result<S> handleErrorNow(Function<Exception, S> func) {
        if (isError()) return JResult.success(func.apply(e));
        assert isSuccess();
        return this;
    }

    @SuppressWarnings("unchecked")
    public static <Old, New> Result<New> asNewType(Result<Old> result) {
        if (result.isError()) return JResult.fail(result.getError());
        try {
            Old old = result.get();
            New nw = (New) old;
            return JResult.success(nw);
        } catch (Exception e) {
            return JResult.fail(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <R> Result<R> runUnchecked(UncheckedFunction<R> f) {
        try {
            R res = f.get();
            if (res == null) return JResult.success((R) JVoid.get());
            return JResult.success(res);
        } catch (Exception e) {
            return JResult.fail(e);
        }
    }

    @Override
    public String toString() {
        if (isSuccess()) {
            return "JResult; Ok: " + s;
        } else {
            return "JResult; Err: " + e;
        }
    }

    @FunctionalInterface
    public interface UncheckedFunction<R> {
        R get() throws Exception;
    }
}
