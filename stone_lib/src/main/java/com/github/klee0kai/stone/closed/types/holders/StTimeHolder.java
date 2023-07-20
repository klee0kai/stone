package com.github.klee0kai.stone.closed.types.holders;

import com.github.klee0kai.stone.types.wrappers.Ref;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

/**
 * Stone Private class
 */
public class StTimeHolder<T> implements Ref<T> {

    private final StTimeScheduler timer;
    private Reference<T> ref = null;
    private StScheduleTask scheduleTask = null;

    public StTimeHolder(StTimeScheduler timer) {
        this.timer = timer;
    }

    public StTimeHolder(StTimeScheduler timer, T ref, long holdTime) {
        this.timer = timer;
        hold(ref, holdTime);
    }

    public synchronized T hold(T ob, long holdTime) {
        clearRef();
        this.ref = new SoftReference<>(ob);
        timer.schedule(scheduleTask = new StScheduleTask(holdTime) {
            @Override
            public void run() {
                clearRef();
            }
        });
        return ob;
    }

    public synchronized T get() {
        return ref != null ? ref.get() : null;
    }

    private synchronized void clearRef() {
        ref = null;
        if (scheduleTask != null) scheduleTask.cancel();
    }

}
