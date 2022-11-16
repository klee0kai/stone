package com.github.klee0kai.stone.types;

public class PhantomProvide<T> implements IRef<T> {

    public interface IProvide<T> {
        T provide();
    }

    private IProvide<T> call;

    public PhantomProvide(IProvide<T> call) {
        this.call = call;
    }

    @Override
    public T get() {
        return call.provide();
    }
}
