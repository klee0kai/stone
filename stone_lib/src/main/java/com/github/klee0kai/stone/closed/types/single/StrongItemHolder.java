package com.github.klee0kai.stone.closed.types.single;

import java.util.List;

/**
 * Stone Private class
 */
public class StrongItemHolder<T> extends SingleItemHolder<T> {

    @Override
    public T set(T ob) {
        setStrong(ob);
        return ob;
    }

    @Override
    public List<T> setList(List<T> ob) {
        setStrongList(ob);
        return ob;
    }

    public void defRef() {
        strong();
    }

}
