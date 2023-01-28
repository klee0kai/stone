package com.github.klee0kai.stone.closed.types.single;

import com.github.klee0kai.stone.closed.types.ScheduleTask;
import com.github.klee0kai.stone.closed.types.SwitchCacheParam;
import com.github.klee0kai.stone.types.wrappers.IRef;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stone Private class
 */
public abstract class SingleItemHolder<T> implements IRef<T> {

    private T strongHolder = null;
    private Reference<T> refHolder = null;
    private final AtomicInteger shedTaskCount = new AtomicInteger(0);


    abstract public T set(T ob);

    abstract public void setIfNull(T ob);

    abstract public void defRef();

    @Override
    public T get() {
        if (strongHolder != null)
            return strongHolder;
        return refHolder != null ? refHolder.get() : null;
    }

    public void setStrong(T ob) {
        strongHolder = ob;
    }

    public void setSoft(T ob) {
        refHolder = new SoftReference<>(ob);
        strongHolder = null;
    }

    public void setWeak(T ob) {
        refHolder = new WeakReference<>(ob);
        strongHolder = null;
    }

    public void strong() {
        setStrong(get());
    }

    public void soft() {
        setSoft(get());
    }

    public void weak() {
        setWeak(get());
    }


    public void reset() {
        strongHolder = null;
        refHolder = null;
    }


    public void switchCache(SwitchCacheParam args) {
        switch (args.cache) {
            case Default:
                defRef();
                break;
            case Reset:
                reset();
                break;
            case Weak:
                weak();
                break;
            case Soft:
                soft();
                break;
            case Strong:
                strong();
                break;
        }

        if (args.time > 0) {
            shedTaskCount.incrementAndGet();
            args.scheduler.schedule(new ScheduleTask(args.time) {
                @Override
                public void run() {
                    if (shedTaskCount.decrementAndGet() <= 0)
                        defRef();
                }
            });
        }
    }

}
