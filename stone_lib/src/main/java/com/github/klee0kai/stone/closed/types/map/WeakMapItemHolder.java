package com.github.klee0kai.stone.closed.types.map;

/**
 * Stone Private class
 */
public class WeakMapItemHolder<Key, T> extends MapItemHolder<Key, T> {


    public T set(Key key, T ob) {
        return setWeak(key, ob);
    }

    public void defRef() {
        weak();
    }


}
