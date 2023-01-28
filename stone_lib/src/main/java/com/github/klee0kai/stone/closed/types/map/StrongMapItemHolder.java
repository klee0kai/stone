package com.github.klee0kai.stone.closed.types.map;

/**
 * Stone Private class
 */
public class StrongMapItemHolder<Key, T> extends MapItemHolder<Key, T> {


    @Override
    public T set(Key key, T ob) {
        return setStrong(key, ob);
    }

    @Override
    public void setIfNull(Key key, T ob) {
        if (get(key) == null) {
            setStrong(key, ob);
        }
    }

    @Override
    public void defRef() {
        strong();
    }


}
