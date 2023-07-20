package com.github.klee0kai.stone.closed.types.holders;

import com.github.klee0kai.stone.closed.types.StListUtils;
import com.github.klee0kai.stone.closed.types.StSwitchCache;
import com.github.klee0kai.stone.types.wrappers.Ref;

import java.lang.ref.Reference;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.klee0kai.stone.closed.types.holders.StRefType.ListObject;
import static com.github.klee0kai.stone.closed.types.holders.StRefType.StrongObject;

/**
 * Stone Private class
 */
public class StMapItemHolder<Key, T> {

    private final StRefType defType;

    private StRefType curRefType;


    private final HashMap<Key, Object> refMap = new HashMap<>();
    private final AtomicInteger shedTaskCount = new AtomicInteger(0);


    public StMapItemHolder(StRefType defType) {
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
                return StListUtils.format((List<Reference<T>>) holder, Reference::get);
        }
        return null;
    }

    public void set(Key key, Ref<T> creator, boolean onlyIfNull) {
        Object refHolder = refMap.get(key);
        if (Objects.equals(curRefType, StrongObject)) {
            if (onlyIfNull && refHolder != null) return;
            refMap.put(key, creator.get());
            return;
        }
        StListUtils.IFormat<T, Reference<T>> formatter = curRefType.formatter();
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
        if (Objects.equals(curRefType, ListObject)) {
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
        StListUtils.IFormat<T, Reference<T>> formatter = curRefType.formatter();
        if (formatter == null) return;
        if (!onlyIfNull || refHolder == null) {
            //switch ref type case
            refMap.put(key, StListUtils.format(creator.get(), formatter));
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


    public void setRefType(StRefType refType) {
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
                if (!StListUtils.contains(list, (i, it) -> it != null)) {
                    refMap.remove(key);
                }
            }
        }

    }


    public void switchCache(StSwitchCache args) {
        switch (args.cache) {
            case Default:
                setRefType(defType);
                return;
            case Reset:
                reset();
                return;
            case Weak:
                setRefType(StRefType.WeakObject);
                break;
            case Soft:
                setRefType(StRefType.SoftObject);
                break;
            case Strong:
                setRefType(StRefType.StrongObject);
                break;
        }

        if (args.time > 0) {
            shedTaskCount.incrementAndGet();
            args.scheduler.schedule(new StScheduleTask(args.time) {
                @Override
                public void run() {
                    if (shedTaskCount.decrementAndGet() <= 0) setRefType(defType);
                }
            });
        }
    }

}
