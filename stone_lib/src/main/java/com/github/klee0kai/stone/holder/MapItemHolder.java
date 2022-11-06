package com.github.klee0kai.stone.holder;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class MapItemHolder<Key, T> {

    private final HashMap<Key, T> strongMap = new HashMap<>();
    private final HashMap<Key, Reference<T>> refMap = new HashMap<>();


    public T get(Key key) {
        if (strongMap.containsKey(key))
            return strongMap.get(key);
        if (refMap.containsKey(key)) {
            Reference<T> ref = refMap.get(key);
            return ref != null ? ref.get() : null;
        }
        return null;
    }

    public void setStrong(Key key, T ob) {
        refMap.remove(key);
        strongMap.put(key, ob);
    }

    public void setSoft(Key key, T ob) {
        strongMap.remove(key);
        refMap.put(key, new SoftReference<>(ob));
    }

    public void setWeak(Key key, T ob) {
        strongMap.remove(key);
        refMap.put(key, new WeakReference<>(ob));
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


}
