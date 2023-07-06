package com.github.klee0kai.stone.closed.types.single;

import java.util.List;

/**
 * Stone Private class
 */
public class SoftItemHolder<T> extends SingleItemHolder<T> {

    @Override
    public T set(T ob) {
        setSoft(ob);
        return ob;
    }

    @Override
    public List<T> setList(List<T> ob) {
        setSoftList(ob);
        return ob;
    }

    public void defRef() {
        soft();
    }

}
