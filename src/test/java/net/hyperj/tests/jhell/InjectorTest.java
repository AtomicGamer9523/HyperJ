package net.hyperj.tests.jhell;

import static net.hyperj.testing.HyperTesting.*;

import net.hyperj.jhell.*;

@SuppressWarnings("unused")
public class InjectorTest {
    private static final DummyObject object = DummyObject.getInstance();

    @Test("Reads a private field from an object")
    private static void readTest() {
        DummyObject.resetInstance();
        object.increment();
        int value = (int) Injector.inject(object).read("state").get();
    }

    @Test("Write to a private field of an object")
    private static void writeTest() {
        DummyObject.resetInstance();
        object.increment();
        Injector.inject(object).write("state", 10).get();
    }
}
