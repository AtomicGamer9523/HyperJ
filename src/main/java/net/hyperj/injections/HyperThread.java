package net.hyperj.injections;

import net.hyperj.jhell.DoNotReveal;

import java.util.concurrent.atomic.AtomicInteger;

public class HyperThread extends Thread {
    @DoNotReveal(priority = 1000)
    private static final AtomicInteger LATEST_USED = new AtomicInteger(-1);
    @DoNotReveal(priority = 1000)
    private static int getNextAvailableID() {
        return LATEST_USED.addAndGet(1);
    }
    public static final ThreadGroup THREAD_GROUP = new ThreadGroup("HyperJ");
    public HyperThread(Runnable func) {
        super(THREAD_GROUP, func, "HyperJ-"+getNextAvailableID());
    }
    public static HyperThread withHyper(Runnable func) {
        HyperThread thread = new HyperThread(func);
        thread.setContextClassLoader(new HyperClassLoader(thread.getName()));
        return thread;
    }
}
