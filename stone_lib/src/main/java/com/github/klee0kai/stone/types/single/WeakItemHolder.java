package com.github.klee0kai.stone.types.single;

public class WeakItemHolder<T> extends SingleItemHolder<T> {

    public T set(T ob) {
        setWeak(ob);
        return ob;
    }

    public void defRef() {
        weak();
    }

}