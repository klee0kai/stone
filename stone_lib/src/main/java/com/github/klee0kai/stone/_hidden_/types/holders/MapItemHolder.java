package com.github.klee0kai.stone._hidden_.types.holders;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone._hidden_.types.SwitchCacheParam;
import com.github.klee0kai.stone.wrappers.Ref;

import java.lang.ref.Reference;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stone Private class
 */
public class MapItemHolder<Key, T> {

    private final StoneRefType defType;

    private StoneRefType curRefType;


    private final HashMap<Key, Object> refMap = new HashMap<>();
    private final AtomicInteger shedTaskCount = new AtomicInteger(0);


    public MapItemHolder(StoneRefType defType) {
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
        Object refHolder = refMap.get(key);
        if (Objects.equals(curRefType, StoneRefType.StrongObject)) {
            if (onlyIfNull && refHolder != null) return;
            refMap.put(key, creator.get());
            return;
        }
        ListUtils.IFormat<T, Reference<T>> formatter = curRefType.formatter();
        if (formatter == null) return;
        if (!onlyIfNull) {
            //switch ref type case
            refMap.put(key, formatter.format(creator.get()));
            return;
        }

        Reference<T> ref = (Reference<T>) refHolder;
        if (ref == null || ref.get() == null) {
            refMap.put(key, formatter.format(creator.get()));
        }
    }

    public void setList(Key key, Ref<List<T>> creator, boolean onlyIfNull) {
        Object refHolder = refMap.get(key);
        if (Objects.equals(curRefType, StoneRefType.ListObject)) {
            if (!onlyIfNull || refHolder == null) {
                refMap.put(key, creator.get());
                return;
            }

            // init nulls if needed
            List<T> created = null;
            List<T> refList = (List<T>) refHolder;
            for (int i = 0; i < refList.size(); i++) {
                if (refList.get(i) == null) {
                    if (created == null) created = creator.get();
                    refList.set(i, created.get(i));
                }
            }
            return;
        }
        ListUtils.IFormat<T, Reference<T>> formatter = curRefType.formatter();
        if (formatter == null) return;
        if (!onlyIfNull || refHolder == null) {
            //switch ref type case
            refMap.put(key, ListUtils.format(creator.get(), formatter));
            return;
        }

        List<Reference<T>> refList = (List<Reference<T>>) refHolder;
        List<T> created = null;
        for (int i = 0; i < refList.size(); i++) {
            if (refList.get(i) == null || refList.get(i).get() == null) {
                if (created == null) created = creator.get();
                refList.set(i, formatter.format(created.get(i)));
            }
        }
    }


    public void setRefType(StoneRefType refType) {
        if (curRefType == refType) return;
        if (defType.isList()) {
            HashMap<Key, List<T>> listMap = new HashMap<>();
            for (Key key : refMap.keySet()) listMap.put(key, getList(key));
            curRefType = refType.forList();
            for (Key key : listMap.keySet()) setList(key, () -> listMap.get(key), false);
        } else {
            HashMap<Key, T> itemMap = new HashMap<>();
            for (Key key : refMap.keySet()) itemMap.put(key, get(key));
            curRefType = refType.forSingle();
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
        if (!curRefType.isList()) {
            for (Key key : keys) {
                if (get(key) == null) {
                    refMap.remove(key);
                }
            }
        } else {
            for (Key key : keys) {
                List<T> list = getList(key);
                if (!ListUtils.contains(list, (i, it) -> it != null)) {
                    refMap.remove(key);
                }
            }
        }

    }


    public void switchCache(SwitchCacheParam args) {
        switch (args.cache) {
            case Default:
                setRefType(defType);
                return;
            case Reset:
                reset();
                return;
            case Weak:
                setRefType(StoneRefType.WeakObject);
                break;
            case Soft:
                setRefType(StoneRefType.SoftObject);
                break;
            case Strong:
                setRefType(StoneRefType.StrongObject);
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
