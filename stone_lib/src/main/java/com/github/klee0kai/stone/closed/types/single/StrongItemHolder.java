package com.github.klee0kai.stone.closed.types.single;

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
    public void setIfNull(T ob) {
        if (get() == null) {
            setStrong(ob);
        }
    }

    public void defRef() {
        strong();
    }

}
