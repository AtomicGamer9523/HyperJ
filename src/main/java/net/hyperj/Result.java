package net.hyperj;

import java.util.function.*;
import java.util.*;

public interface Result<S> extends Supplier<S> {
    /**
     * Returns {@code true} if the Result is erroneous, {@code false}
     * otherwise.
     */
    boolean isError();

    /**
     * Returns {@code true} if the Result is successful, {@code false}
     * otherwise.
     */
    boolean isSuccess();

    /**
     * Returns the erroneous result, which is always some type of
     * {@code Exception}. If the Result is not erroneous, a
     * {@code NoSuchElementException} is thrown.
     * <p>
     * It is expected that you always check if the result is
     * erroneous before trying to get the error value.
     *
     * @see Result#isError()
     */
    Exception getError();

    /**
     * Returns the result value. If the Result is erroneous, a
     * {@code NoSuchElementException} is thrown.
     * <p>
     * It is expected that you always check if the result is
     * erroneous before trying to get the value.
     *
     * @see Result#isError()
     */
    @Override
    S get();

    /**
     * @return true if the returned value is void or instance of {@link JVoid Void}
     */
    boolean isVoid();

    /**
     * Returns the result value unless the Result is erroneous,
     * in which case a supplied default is returned.
     *
     * @param def the default to return if this Result is erroneous.
     */
    S getOrElse(S def);

    /**
     * Returns the result value unless the Result is erroneous,
     * in which case a function is called and the result of that
     * function is used.
     *
     * @param f a function that takes the error
     *          value and returns a suitable default.
     */
    S getOrElse(Function<Exception, S> f);

    /**
     * Returns the result value unless the Result is erroneous,
     * in which case the error value is thrown.
     */
    S getOrThrow() throws Exception;

    /**
     * Maps the result value to some other value.
     * <p>
     * If the Result is erroneous, this method returns the original
     * Result without calling the mapping function. The returned
     * value is cast, but this cast is safe because the value
     * being cast is guaranteed to be {@code null}.
     *
     * @param f mapping function.
     */
    <N> Result<N> map(Function<S, N> f);

    /**
     * Maps the error value to some other error value, which
     * must be of type {@code Exception}.
     * <p>
     * If the Result is successful, this method returns the
     * original Result without doing anything.
     *
     * @param f mapping function.
     */
    Result<S> mapError(Function<Exception, Exception> f);

    /**
     * If the Result is erroneous, calls the given {@code Consumer} on
     * the error result.
     *
     * @return this
     */
    Result<S> ifError(Consumer<Exception> consumer);

    /**
     * If the Result is successful, calls the given {@code Consumer} on
     * the result.
     *
     * @return this
     */
    Result<S> ifSuccess(Consumer<S> consumer);

    /**
     * A convenience method for wrapping an erroneous result object
     * with more information.
     *
     * <pre>
     *   Result.fail(new IOException()).wrapError(IllegalArgumentException::new, "invalid param");
     * </pre>
     * <p>
     * If the Result is successful, this method returns the original
     * Result without doing anything.
     */
    Result<S> wrapError(BiFunction<String, Exception, Exception> f, String message);

    /**
     * Alias for {@code Result#mapError(Function)}.
     *
     * @see Result#mapError(Function)
     */
    Result<S> wrapError(Function<Exception, Exception> f);

    /**
     * Converts this Result<S> to an Optional<S>, discarding
     * the error value in the process. If the Result is
     * erroneous, {@code Optional.empty()} is returned.
     */
    Optional<S> asOptional();

    /**
     * Throws the error if it exists,
     * otherwise returns a new result that will always be ok
     *
     * @return a new always-ok result
     */
    Result<S> handleErrorNow(Function<Exception, S> func);

    String toString();
}
