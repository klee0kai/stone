package com.github.klee0kai.stone.closed.types.single;

import java.util.List;

/**
 * Stone Private class
 */
public class WeakItemHolder<T> extends SingleItemHolder<T> {

    @Override
    public T set(T ob) {
        setWeak(ob);
        return ob;
    }

    @Override
    public List<T> setList(List<T> ob) {
        setWeakList(ob);
        return ob;
    }

    public void defRef() {
        weak();
    }

}
