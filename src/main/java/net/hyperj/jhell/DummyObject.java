package net.hyperj.jhell;

/**
 * A Dummy Object that is used purely for joking around ;)
 */
@SuppressWarnings("all")
public class DummyObject {
    @DoNotReveal(priority = 1000)
    private static DummyObject INSTANCE;

    public static DummyObject getInstance() {
        if (INSTANCE == null) INSTANCE = new DummyObject(0);
        return INSTANCE;
    }
    public static void resetInstance() {
        INSTANCE = new DummyObject(0);
    }

    @DoNotReveal(priority = 50)
    private int priorityState = 69420;
    @DoNotReveal(priority = 50)
    private int getPriorityState() {
        return priorityState;
    }
    private int state;

    private DummyObject(int state) {
        this.state = state;
    }

    public void increment() {
        this.add(1);
    }

    private void add(int number) {
        this.state += number;
    }

    public int getState() {
        return this.state;
    }
}
