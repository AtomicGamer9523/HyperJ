package dev.matveit.hyperj;

import static dev.matveit.hyperj.testing.HyperTesting.*;
import dev.matveit.hyperj.injections.*;
import dev.matveit.hyperj.util.*;

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
