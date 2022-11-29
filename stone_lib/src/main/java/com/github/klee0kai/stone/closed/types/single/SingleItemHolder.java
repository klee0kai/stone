package com.github.klee0kai.stone.closed.types.single;

import com.github.klee0kai.stone.types.IRef;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Stone Private class
 */
public abstract class SingleItemHolder<T> implements IRef<T> {

    private T strongHolder = null;
    private Reference<T> refHolder = null;


    abstract public T set(T ob);

    abstract public void defRef();

    @Override
    public T get() {
        if (strongHolder != null)
            return strongHolder;
        return refHolder != null ? refHolder.get() : null;
    }

    public void setStrong(T ob) {
        strongHolder = ob;
    }

    public void setSoft(T ob) {
        refHolder = new SoftReference<>(ob);
        strongHolder = null;
    }

    public void setWeak(T ob) {
        refHolder = new WeakReference<>(ob);
        strongHolder = null;
    }

    public void strong() {
        setStrong(get());
    }

    public void soft() {
        setSoft(get());
    }

    public void weak() {
        setWeak(get());
    }


}
