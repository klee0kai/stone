package com.github.klee0kai.stone.closed.types.map;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.closed.types.ScheduleTask;
import com.github.klee0kai.stone.closed.types.SwitchCacheParam;
import com.github.klee0kai.stone.closed.types.single.ItemRefType;
import com.github.klee0kai.stone.types.wrappers.Ref;

import java.lang.ref.Reference;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.klee0kai.stone.closed.types.single.ItemRefType.ListObject;
import static com.github.klee0kai.stone.closed.types.single.ItemRefType.StrongObject;

/**
 * Stone Private class
 */
public class MapItemHolder<Key, T> {

    private final ItemRefType defType;

    private ItemRefType curRefType;


    private final HashMap<Key, Object> refMap = new HashMap<>();
    private final AtomicInteger shedTaskCount = new AtomicInteger(0);


    public MapItemHolder(ItemRefType defType) {
        this.defType = defType;
        this.curRefType = defType;
    }

    public T get(Key key) {
        Object holder = refMap.get(key);
        if (holder == null) return null;
        switch (curRefType) {
            case StrongObject:
                return (T) holder;
            case WeakObject:
            case SoftObject:
                Reference<T> ref = ((Reference<T>) holder);
                return ref != null ? ref.get() : null;
        }
        return null;
    }

    public List<T> getList(Key key) {
        Object holder = refMap.get(key);
        if (holder == null) return null;
        switch (curRefType) {
            case ListObject:
                return (List<T>) holder;
            case ListWeakObject:
            case ListSoftObject:
                return ListUtils.format((List<Reference<T>>) holder, Reference::get);
        }
        return null;
    }

    public void set(Key key, Ref<T> creator, boolean onlyIfNull) {
        if (Objects.equals(curRefType, StrongObject)) {
            if (onlyIfNull && refMap.get(key) != null) return;
            refMap.put(key, creator.get());
            return;
        }
        ListUtils.IFormat<T, Reference<T>> formatter = curRefType.formatter();
        if (formatter == null) return;
        if (!onlyIfNull) {
            //switch ref type case
            refMap.put(key, formatter.format(creator.get()));
        }

        Reference<T> ref = (Reference<T>) refMap.get(key);
        if (ref == null || ref.get() == null) {
            refMap.put(key, formatter.format(creator.get()));
        }
    }

    public void setList(Key key, Ref<List<T>> creator, boolean onlyIfNull) {
        if (Objects.equals(curRefType, ListObject)) {
            if (onlyIfNull && refMap.get(key) != null) return;
            refMap.put(key, creator.get());
            return;
        }
        ListUtils.IFormat<T, Reference<T>> formatter = curRefType.formatter();
        if (formatter == null) return;
        List<Reference<T>> refList = (List<Reference<T>>) refMap.get(key);
        if (refList == null || !onlyIfNull) {
            refMap.put(key, ListUtils.format(creator.get(), formatter));
            return;
        }
        List<T> created = null;
        for (int i = 0; i < refList.size(); i++) {
            if (refList.get(i) == null || refList.get(i).get() == null) {
                if (created == null) created = creator.get();
                refList.set(i, formatter.format(created.get(i)));
            }
        }
    }


    public void setRefType(ItemRefType refType) {
        if (curRefType == refType) return;
        if (defType.isList()) {
            HashMap<Key, List<T>> listMap = new HashMap<>();
            for (Key key : refMap.keySet()) listMap.put(key, getList(key));
            curRefType = refType.forList();
            for (Key key : listMap.keySet()) setList(key, () -> listMap.get(key), false);
        } else {
            HashMap<Key, T> itemMap = new HashMap<>();
            for (Key key : refMap.keySet()) itemMap.put(key, get(key));
            curRefType = refType.forList();
            for (Key key : itemMap.keySet()) set(key, () -> itemMap.get(key), false);
        }
    }


    public void remove(Key key) {
        refMap.remove(key);
    }


    public void reset() {
        curRefType = defType;
        refMap.clear();
    }


    public void clearNulls() {
        Set<Key> keys = new HashSet<>(refMap.keySet());
        for (Key key : keys) {
            if (get(key) == null)
                remove(key);
        }
    }


    public void switchCache(SwitchCacheParam args) {
        switch (args.cache) {
            case Default:
                setRefType(defType);
                break;
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
