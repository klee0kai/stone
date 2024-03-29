package com.github.klee0kai.test.car.di.wrapped.custom.wrappers;

import com.github.klee0kai.stone.wrappers.Ref;

public class CarLazy<T> {

    private T value = null;
    private final Ref<T> call;

    public CarLazy(Ref<T> call) {
        this.call = call;
    }

    public T getValue() {
        if (value != null)
            return value;
        return value = call.get();
    }

}
