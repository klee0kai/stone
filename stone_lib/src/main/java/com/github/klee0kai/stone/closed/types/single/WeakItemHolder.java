package com.github.klee0kai.stone.closed.types.single;

/**
 * Stone Private class
 */
public class WeakItemHolder<T> extends SingleItemHolder<T> {

    public T set(T ob) {
        setWeak(ob);
        return ob;
    }

    public void defRef() {
        weak();
    }

}
