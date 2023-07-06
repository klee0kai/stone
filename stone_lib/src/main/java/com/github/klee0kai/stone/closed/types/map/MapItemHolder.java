package com.github.klee0kai.stone.closed.types.map;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.closed.types.ScheduleTask;
import com.github.klee0kai.stone.closed.types.SwitchCacheParam;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stone Private class
 */
public abstract class MapItemHolder<Key, T> {


    private static final int TYPE_OBJECT = 756;
    private static final int TYPE_REF_OBJECT = 23;
    private static final int TYPE_LIST_OBJECT = 432;
    private static final int TYPE_LIST_REF_OBJECT = 2;

    private int refType = 0;

    private final HashMap<Key, Object> refMap = new HashMap<>();
    private final AtomicInteger shedTaskCount = new AtomicInteger(0);


    abstract public T set(Key key, T ob);

    abstract public List<T> setList(Key key, List<T> ob);


    abstract public void defRef();

    public T get(Key key) {
        Object holder = refMap.get(key);
        if (holder == null) return null;
        switch (refType) {
            case TYPE_OBJECT:
                return (T) holder;
            case TYPE_REF_OBJECT:
                return ((Reference<T>) holder).get();
        }
        return null;
    }

    public List<T> getList(Key key) {
        Object holder = refMap.get(key);
        if (holder == null) return null;
        switch (refType) {
            case TYPE_LIST_OBJECT:
                return (List<T>) holder;
            case TYPE_LIST_REF_OBJECT:
                return ListUtils.format((List<Reference<T>>) holder, Reference::get);
        }
        return null;
    }

    public void setIfNull(Key key, T ob) {
        if (get(key) == null) {
            set(key, ob);
        }
    }

    public void setListIfNull(Key key, List<T> ob) {
        if (get(key) == null) {
            setList(key, ob);
        }
    }

    public void setStrong(Key key, T ob) {
        refType = TYPE_OBJECT;
        refMap.put(key, ob);
    }

    public void setSoft(Key key, T ob) {
        refType = TYPE_REF_OBJECT;
        refMap.put(key, new SoftReference<>(ob));
    }

    public void setWeak(Key key, T ob) {
        refType = TYPE_REF_OBJECT;
        refMap.put(key, new WeakReference<>(ob));
    }


    public void setStrongList(Key key, List<T> ob) {
        refType = TYPE_LIST_OBJECT;
        refMap.put(key, ob);
    }

    public void setSoftList(Key key, List<T> ob) {
        refType = TYPE_LIST_REF_OBJECT;
        refMap.put(key, ListUtils.format(ob, SoftReference::new));
    }

    public void setWeakList(Key key, List<T> ob) {
        refType = TYPE_LIST_REF_OBJECT;
        refMap.put(key, ListUtils.format(ob, WeakReference::new));
    }

    public void remove(Key key) {
        refType = 0;
        refMap.remove(key);
    }

    public void strong() {
        switch (refType) {
            case TYPE_OBJECT:
            case TYPE_REF_OBJECT:
                for (Key k : refMap.keySet())
                    setStrong(k, get(k));
                break;
            case TYPE_LIST_OBJECT:
            case TYPE_LIST_REF_OBJECT:
                for (Key k : refMap.keySet())
                    setStrongList(k, getList(k));
        }
    }

    public void soft() {
        switch (refType) {
            case TYPE_OBJECT:
            case TYPE_REF_OBJECT:
                for (Key k : refMap.keySet())
                    setSoft(k, get(k));
                break;
            case TYPE_LIST_OBJECT:
            case TYPE_LIST_REF_OBJECT:
                for (Key k : refMap.keySet())
                    setSoftList(k, getList(k));
        }
    }

    public void weak() {
        switch (refType) {
            case TYPE_OBJECT:
            case TYPE_REF_OBJECT:
                for (Key k : refMap.keySet())
                    setWeak(k, get(k));
                break;
            case TYPE_LIST_OBJECT:
            case TYPE_LIST_REF_OBJECT:
                for (Key k : refMap.keySet())
                    setWeakList(k, getList(k));
        }
    }

    public void reset() {
        refType = 0;
        refMap.clear();
    }


    public void clearNulls() {
        Set<Key> keys = new HashSet<>(refMap.keySet());
        for (Key key : keys) {
            if (get(key) == null)
                remove(key);
        }
    }


    public void switchCache(SwitchCacheParam params) {
        switch (params.cache) {
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

        if (params.time > 0) {
            shedTaskCount.incrementAndGet();
            params.scheduler.schedule(new ScheduleTask(params.time) {
                @Override
                public void run() {
                    if (shedTaskCount.decrementAndGet() <= 0)
                        defRef();
                }
            });
        }
    }

}
