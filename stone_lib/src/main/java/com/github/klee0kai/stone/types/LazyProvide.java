package com.github.klee0kai.stone.types;

public class LazyProvide<T> implements IRef<T>{

    private T value = null;
    private PhantomProvide.IProvide<T> call;

    public LazyProvide(PhantomProvide.IProvide<T> call) {
        this.call = call;
    }

    @Override
    public T get() {
        if (value != null)
            return value;
        return value = call.provide();
    }
}
