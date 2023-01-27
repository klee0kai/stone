package com.github.klee0kai.stone.closed.types.single;

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
    public void setIfNull(T ob) {
        if (get() == null) {
            setSoft(ob);
        }
    }

    public void defRef() {
        soft();
    }

}
