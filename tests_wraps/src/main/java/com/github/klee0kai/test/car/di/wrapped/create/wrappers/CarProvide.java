package com.github.klee0kai.test.car.di.wrapped.create.wrappers;

import com.github.klee0kai.stone.types.wrappers.Ref;

public class CarProvide<T> {

    private Ref<T> call;

    public CarProvide(Ref<T> call) {
        this.call = call;
    }

    public T getValue() {
        return call.get();
    }

}
