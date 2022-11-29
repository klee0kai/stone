package com.github.klee0kai.stone.closed.types.map;

/**
 * Stone Private class
 */
public class SoftMapItemHolder<Key, T> extends MapItemHolder<Key, T> {


    public T set(Key key, T ob) {
        return setSoft(key, ob);
    }

    public void defRef() {
        soft();
    }


}
