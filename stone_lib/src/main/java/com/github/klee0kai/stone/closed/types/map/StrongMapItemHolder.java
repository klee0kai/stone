package com.github.klee0kai.stone.closed.types.map;

/**
 * Stone Private class
 */
public class StrongMapItemHolder<Key, T> extends MapItemHolder<Key, T> {


    public T set(Key key, T ob) {
        return setStrong(key, ob);
    }

    public void defRef() {
        strong();
    }


}
