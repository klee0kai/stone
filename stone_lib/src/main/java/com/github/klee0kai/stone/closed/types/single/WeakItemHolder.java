package com.github.klee0kai.stone.closed.types.single;

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
    public void setIfNull(T ob) {
        if (get() == null) {
            setWeak(ob);
        }
    }

    public void defRef() {
        weak();
    }

}
