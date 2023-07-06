package com.github.klee0kai.stone.closed.types.map;

import java.util.List;

/**
 * Stone Private class
 */
public class WeakMapItemHolder<Key, T> extends MapItemHolder<Key, T> {


    @Override
    public T set(Key key, T ob) {
        setWeak(key, ob);
        return ob;
    }

    @Override
    public List<T> setList(Key key, List<T> ob) {
        setWeakList(key, ob);
        return ob;
    }

    @Override
    public void defRef() {
        weak();
    }


}
