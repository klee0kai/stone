package com.github.klee0kai.stone.types.wrappers;

public class PhantomProvide<T> implements Ref<T> {

    private final Ref<T> call;

    public PhantomProvide(Ref<T> call) {
        this.call = call;
    }

    @Override
    public T get() {
        return call.get();
    }
}
