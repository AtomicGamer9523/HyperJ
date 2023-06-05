package net.hyperj.result;

@FunctionalInterface
public interface ThrowingSupplier<S> {
    S get() throws Exception;
}
