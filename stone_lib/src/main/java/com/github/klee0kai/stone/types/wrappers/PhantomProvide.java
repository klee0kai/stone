package com.github.klee0kai.stone.types.wrappers;

public class PhantomProvide<T> implements Ref<T> {

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
