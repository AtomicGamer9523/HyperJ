package net.hyperj;

import static net.hyperj.testing.HyperTesting.*;

import net.hyperj.injections.ReflectInjectorTest;
import net.hyperj.tests.util.*;
import net.hyperj.tests.injections.*;
import net.hyperj.util.JResultTest;

public class Tests {
    private static final Testing TESTS = initTesting();
    public static void main(String[] args) {
        // Result
        TESTS.add(JResultTest.class);

        // JHell
        TESTS.add(ReflectInjectorTest.class);

        // End
        TESTS.run().finishVerbose();
    }
}
