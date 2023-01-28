package com.github.klee0kai.stone.closed.types.map;

/**
 * Stone Private class
 */
public class SoftMapItemHolder<Key, T> extends MapItemHolder<Key, T> {

    @Override
    public T set(Key key, T ob) {
        return setSoft(key, ob);
    }

    @Override
    public void setIfNull(Key key, T ob) {
        if (get(key) == null) {
            setSoft(key, ob);
        }
    }

    @Override
    public void defRef() {
        soft();
    }


}
