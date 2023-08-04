package dev.matveit.hyperj;

public final class JVoid {
    public static JVoid get() {
        return new JVoid();
    }
    public JVoid() { }
    @Override
    public String toString() {
        return "Void";
    }
}
