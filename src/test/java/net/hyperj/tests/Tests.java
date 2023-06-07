package net.hyperj.tests;

import static net.hyperj.testing.HyperTesting.*;

import net.hyperj.tests.result.*;
import net.hyperj.tests.jhell.*;

public class Tests {
    private static final Testing TESTS = initTesting();
    public static void main(String[] args) {
        // Result
        TESTS.add(JResultTest.class);

        // JHell
        TESTS.add(InjectorTest.class);
        TESTS.add(PriorityInjectorTest.class);

        // End
        TESTS.run().finish();
    }
}
