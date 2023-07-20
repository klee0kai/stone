package com.github.klee0kai.stone._hidden_.types.holders;

/**
 * Stone Private class
 */

abstract class ScheduleTask {

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
