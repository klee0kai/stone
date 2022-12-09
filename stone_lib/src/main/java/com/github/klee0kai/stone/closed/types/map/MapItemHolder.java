package com.github.klee0kai.stone.closed.types.map;

import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.closed.types.ScheduleTask;
import com.github.klee0kai.stone.closed.types.TimeScheduler;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stone Private class
 */
public abstract class MapItemHolder<Key, T> {

    private final HashMap<Key, T> strongMap = new HashMap<>();
    private final HashMap<Key, Reference<T>> refMap = new HashMap<>();
    private final AtomicInteger shedTaskCount = new AtomicInteger(0);


    abstract public T set(Key key, T ob);

    abstract public void defRef();

    public T get(Key key) {
        if (strongMap.containsKey(key))
            return strongMap.get(key);
        if (refMap.containsKey(key)) {
            Reference<T> ref = refMap.get(key);
            return ref != null ? ref.get() : null;
        }
        return null;
    }

    public T setStrong(Key key, T ob) {
        refMap.remove(key);
        strongMap.put(key, ob);
        return ob;
    }

    public T setSoft(Key key, T ob) {
        strongMap.remove(key);
        refMap.put(key, new SoftReference<>(ob));
        return ob;
    }

    public T setWeak(Key key, T ob) {
        strongMap.remove(key);
        refMap.put(key, new WeakReference<>(ob));
        return ob;
    }

    public void remove(Key key) {
        strongMap.remove(key);
        refMap.remove(key);
    }

    public void strong() {
        for (Key k : refMap.keySet())
            setStrong(k, get(k));
    }

    public void soft() {
        for (Key k : refMap.keySet())
            setSoft(k, get(k));
        for (Key k : strongMap.keySet())
            setSoft(k, get(k));
    }

    public void weak() {
        for (Key k : refMap.keySet())
            setWeak(k, get(k));
        for (Key k : strongMap.keySet())
            setWeak(k, get(k));
    }

    public void reset() {
        strongMap.clear();
        refMap.clear();
    }


    public void clearNulls() {
        Set<Key> keys = new HashSet<>();
        keys.addAll(strongMap.keySet());
        keys.addAll(refMap.keySet());

        for (Key key : keys) {
            if (get(key) == null)
                remove(key);
        }
    }


    public void switchCache(SwitchCache.CacheType cacheType, TimeScheduler scheduler, long time) {
        switch (cacheType) {
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

        if (time > 0) {
            shedTaskCount.incrementAndGet();
            scheduler.schedule(new ScheduleTask(time) {
                @Override
                public void run() {
                    if (shedTaskCount.decrementAndGet() <= 0)
                        defRef();
                }
            });
        }
    }

}
