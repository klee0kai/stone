package com.github.klee0kai.stone.closed.types.single;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.closed.types.ScheduleTask;
import com.github.klee0kai.stone.closed.types.SwitchCacheParam;
import com.github.klee0kai.stone.types.wrappers.Ref;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stone Private class
 */
public abstract class SingleItemHolder<T> implements Ref<T> {

    private static final int TYPE_OBJECT = 756;
    private static final int TYPE_REF_OBJECT = 23;
    private static final int TYPE_LIST_OBJECT = 432;
    private static final int TYPE_LIST_REF_OBJECT = 2;

    private int refType = 0;
    private Object refHolder = null;
    private final AtomicInteger shedTaskCount = new AtomicInteger(0);


    abstract public T set(T ob);

    abstract public List<T> setList(List<T> ob);


    abstract public void defRef();

    @Override
    public T get() {
        switch (refType) {
            case TYPE_OBJECT:
                return (T) refHolder;
            case TYPE_REF_OBJECT:
                return ((Reference<T>) refHolder).get();
            default:
                return null;
        }
    }

    public List<T> getList() {
        switch (refType) {
            case TYPE_LIST_OBJECT:
                return (List<T>) refHolder;
            case TYPE_LIST_REF_OBJECT:
                return ListUtils.format((List<Reference<T>>) refHolder, Reference::get);
            default:
                return null;
        }
    }

    public void setIfNull(T ob) {
        if (get() == null) {
            set(ob);
        }
    }

    public void setStrong(T ob) {
        refType = TYPE_OBJECT;
        refHolder = ob;
    }

    public void setSoft(T ob) {
        refType = TYPE_REF_OBJECT;
        refHolder = new SoftReference<>(ob);
    }

    public void setWeak(T ob) {
        refType = TYPE_REF_OBJECT;
        refHolder = new WeakReference<>(ob);
    }


    public void setStrongList(List<T> ob) {
        refType = TYPE_LIST_OBJECT;
        refHolder = ob;
    }

    public void setSoftList(List<T> ob) {
        refType = TYPE_LIST_REF_OBJECT;
        refHolder = ListUtils.format(ob, SoftReference::new);
    }

    public void setWeakList(List<T> ob) {
        refType = TYPE_LIST_REF_OBJECT;
        refHolder = ListUtils.format(ob, WeakReference::new);
    }

    public void strong() {
        switch (refType) {
            case TYPE_OBJECT:
            case TYPE_REF_OBJECT:
                setStrong(get());
                break;
            case TYPE_LIST_OBJECT:
            case TYPE_LIST_REF_OBJECT:
                setStrongList(getList());
        }
    }

    public void soft() {
        switch (refType) {
            case TYPE_OBJECT:
            case TYPE_REF_OBJECT:
                setSoft(get());
                break;
            case TYPE_LIST_OBJECT:
            case TYPE_LIST_REF_OBJECT:
                setSoftList(getList());
        }
    }

    public void weak() {
        switch (refType) {
            case TYPE_OBJECT:
            case TYPE_REF_OBJECT:
                setWeak(get());
                break;
            case TYPE_LIST_OBJECT:
            case TYPE_LIST_REF_OBJECT:
                setWeakList(getList());
        }
    }


    public void reset() {
        refType = 0;
        refHolder = null;
    }

    public boolean isList() {
        return refType == TYPE_LIST_OBJECT || refType == TYPE_LIST_REF_OBJECT;
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
