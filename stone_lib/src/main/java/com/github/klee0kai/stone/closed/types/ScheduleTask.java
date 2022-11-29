package com.github.klee0kai.stone.closed.types;

/**
 * Stone Private class
 */

public abstract class ScheduleTask {

    private final long execTime;

    private boolean cancel = false;

    public ScheduleTask(long delay) {
        execTime = System.currentTimeMillis() + delay;
    }


    public abstract void run();

    public long scheduledExecutionTime() {
        return execTime;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void cancel() {
        cancel = true;
    }
}
