package com.github.klee0kai.stone.holder;

public class SoftItemHolder<T> extends SingleItemHolder<T> {

    public T set(T ob) {
        setSoft(ob);
        return ob;
    }

    public void defRef() {
        soft();
    }

}
