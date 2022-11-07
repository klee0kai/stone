package com.github.klee0kai.stone.types.single;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public abstract class SingleItemHolder<T> {

    private T strongHolder = null;
    private Reference<T> refHolder = null;


    abstract public T set(T ob);

    abstract public void defRef();

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
