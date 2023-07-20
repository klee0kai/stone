package com.github.klee0kai.stone.closed.types.holders;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.closed.types.SwitchCacheParam;
import com.github.klee0kai.stone.types.wrappers.Ref;

import java.lang.ref.Reference;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.klee0kai.stone.closed.types.holders.ItemRefType.ListObject;
import static com.github.klee0kai.stone.closed.types.holders.ItemRefType.StrongObject;

/**
 * Stone Private class
 */
public class SingleItemHolder<T> {

    private final ItemRefType defType;
    private ItemRefType curRefType;

    private Object refHolder = null;
    private final AtomicInteger shedTaskCount = new AtomicInteger(0);

    public SingleItemHolder(ItemRefType defType) {
        this.defType = defType;
        this.curRefType = defType;
    }

    public synchronized T get() {
        switch (curRefType) {
            case StrongObject:
                return (T) refHolder;
            case WeakObject:
            case SoftObject:
                Reference<T> ref = ((Reference<T>) refHolder);
                return ref != null ? ref.get() : null;
            default:
                return null;
        }
    }

    public synchronized List<T> getList() {
        switch (curRefType) {
            case ListObject:
                return (List<T>) refHolder;
            case ListWeakObject:
            case ListSoftObject:
                return ListUtils.format((List<Reference<T>>) refHolder, Reference::get);
            default:
                return null;
        }
    }


    public synchronized void set(Ref<T> creator, boolean onlyIfNull) {
        if (Objects.equals(curRefType, StrongObject)) {
            if (refHolder != null && onlyIfNull) return;
            refHolder = creator.get();
            return;
        }
        ListUtils.IFormat<T, Reference<T>> formatter = curRefType.formatter();
        if (formatter == null) return;
        if (!onlyIfNull) {
            //switch ref type case
            refHolder = formatter.format(creator.get());
            return;
        }

        Reference<T> ref = (Reference<T>) refHolder;
        if (ref == null || ref.get() == null) {
            refHolder = formatter.format(creator.get());
        }
    }

    public synchronized void setList(Ref<List<T>> creator, boolean onlyIfNull) {
        if (Objects.equals(curRefType, ListObject)) {
            if (!onlyIfNull || refHolder == null) {
                refHolder = creator.get();
                return;
            }

            // init nulls if needed
            List<T> created = null;
            List<T> list = (List<T>) refHolder;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null) {
                    if (created == null) created = creator.get();
                    list.set(i, created.get(i));
                }
            }
            return;
        }
        ListUtils.IFormat<T, Reference<T>> formatter = curRefType.formatter();
        if (formatter == null) return;
        List<Reference<T>> refList = (List<Reference<T>>) refHolder;
        if (refList == null || !onlyIfNull) {
            refHolder = ListUtils.format(creator.get(), formatter);
            return;
        }

        // init nulls if needed
        List<T> created = null;
        for (int i = 0; i < refList.size(); i++) {
            if (refList.get(i) == null || refList.get(i).get() == null) {
                if (created == null) created = creator.get();
                refList.set(i, formatter.format(created.get(i)));
            }
        }
    }

    public synchronized void setRefType(ItemRefType refType) {
        if (curRefType == refType) return;
        if (defType.isList()) {
            List<T> ob = getList();
            curRefType = refType.forList();
            setList(() -> ob, false);
        } else {
            T ob = get();
            curRefType = refType.forSingle();
            set(() -> ob, false);
        }
    }


    public synchronized void reset() {
        curRefType = defType;
        refHolder = null;
    }


    public synchronized void switchCache(SwitchCacheParam args) {
        switch (args.cache) {
            case Default:
                setRefType(defType);
                return;
            case Reset:
                reset();
                return;
            case Weak:
                setRefType(ItemRefType.WeakObject);
                break;
            case Soft:
                setRefType(ItemRefType.SoftObject);
                break;
            case Strong:
                setRefType(ItemRefType.StrongObject);
                break;
        }

        if (args.time > 0) {
            shedTaskCount.incrementAndGet();
            args.scheduler.schedule(new ScheduleTask(args.time) {
                @Override
                public void run() {
                    if (shedTaskCount.decrementAndGet() <= 0) setRefType(defType);
                }
            });
        }
    }

}
