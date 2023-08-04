package dev.matveit.hyperj.injections;

import java.util.concurrent.atomic.*;

public class HyperThread extends Thread {
    private static final AtomicInteger LATEST_USED = new AtomicInteger(-1);
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
