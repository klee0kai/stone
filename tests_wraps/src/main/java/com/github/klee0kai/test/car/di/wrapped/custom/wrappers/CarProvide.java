package com.github.klee0kai.test.car.di.wrapped.custom.wrappers;

import com.github.klee0kai.stone.wrappers.Ref;

public class CarProvide<T> {

    private final Ref<T> call;

    public CarProvide(Ref<T> call) {
        this.call = call;
    }

    public T getValue() {
        return call.get();
    }

}
