package net.hyperj.injections;

import static net.hyperj.testing.HyperTesting.*;

import net.hyperj.util.*;
import net.hyperj.*;

@SuppressWarnings("unused")
public class ReflectInjectorTest {

    @Test("Reads a private field from an object")
    private static void readTest() {
        DummyObject.resetInstance();
        DummyObject object = DummyObject.getInstance();
        object.increment();
        assertEQ(object.getState(), 1);
        int value = (int) HyperJ.inject(object).read("state").get();
        assertEQ(object.getState(), value);
    }

    @Test("Write to a private field of an object")
    private static void writeTest() {
        DummyObject.resetInstance();
        DummyObject object = DummyObject.getInstance();
        object.increment();
        assertEQ(object.getState(), 1);
        HyperJ.inject(object).write("state", 10).get();
        assertEQ(object.getState(), 10);
    }

    @Test("Call a private method of an object")
    private static void callTest() {
        DummyObject.resetInstance();
        DummyObject object = DummyObject.getInstance();
        HyperJ.inject(object).call("add", 10).get();
        assertEQ(object.getState(), 10);
    }
}
