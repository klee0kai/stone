package com.github.klee0kai.test.car.di.wrapped.custom.wrappers;

public class CarRef<T> {

    private T value = null;

    public CarRef(T v) {
        this.value = v;
    }

    public T getValue() {
        return value;
    }

}