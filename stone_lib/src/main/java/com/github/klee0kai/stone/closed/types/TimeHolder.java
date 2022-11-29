package com.github.klee0kai.stone.closed.types;

import com.github.klee0kai.stone.types.IRef;

/**
 * Stone Private class
 */
public class TimeHolder<T> implements IRef<T> {

    private final TimeScheduler timer;
    private T ob = null;
    private ScheduleTask scheduleTask = null;

    public TimeHolder(TimeScheduler timer) {
        this.timer = timer;
    }

    public TimeHolder(TimeScheduler timer, T ob, long holdTime) {
        this.timer = timer;
        hold(ob, holdTime);
    }

    public synchronized T hold(T ob, long holdTime) {
        clearRef();
        this.ob = ob;
        timer.schedule(scheduleTask = new ScheduleTask(holdTime) {
            @Override
            public void run() {
                clearRef();
            }
        });
        return ob;
    }

    public synchronized T get() {
        return ob;
    }

    private synchronized void clearRef() {
        ob = null;
        if (scheduleTask != null) scheduleTask.cancel();
    }

}
