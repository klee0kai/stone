package com.github.klee0kai.stone.holder;

public class StrongMapItemHolder<Key, T> extends MapItemHolder<Key, T> {


    public T set(Key key, T ob) {
        return setStrong(key, ob);
    }

    public void defRef() {
        strong();
    }


}
