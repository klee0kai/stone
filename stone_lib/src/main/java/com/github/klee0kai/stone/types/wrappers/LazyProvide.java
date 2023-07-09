package com.github.klee0kai.stone.types.wrappers;

public class LazyProvide<T> implements Ref<T> {

    private T value = null;
    private Ref<T> call;

    public LazyProvide(Ref<T> call) {
        this.call = call;
    }

    @Override
    public T get() {
        if (value != null)
            return value;
        return value = call.get();
    }
}
