package net.hyperj.tests;

import static net.hyperj.testing.Testing.*;

import net.hyperj.tests.result.*;

public class Tests {
    public static void main(String[] args) {
        initTesting()
            .add(JResultTest.class)
            .run()
            .finish();
    }
}
