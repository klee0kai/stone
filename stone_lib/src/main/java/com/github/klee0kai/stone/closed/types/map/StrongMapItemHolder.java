package com.github.klee0kai.stone.closed.types.map;

import java.util.List;

/**
 * Stone Private class
 */
public class StrongMapItemHolder<Key, T> extends MapItemHolder<Key, T> {


    @Override
    public T set(Key key, T ob) {
        setStrong(key, ob);
        return ob;
    }

    @Override
    public List<T> setList(Key key, List<T> ob) {
        setStrongList(key, ob);
        return ob;
    }

    @Override
    public void defRef() {
        strong();
    }


}
