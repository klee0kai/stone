package com.github.klee0kai.test.boxed.model;

public class CarBox<T> {

    public T val;

    public CarBox(T val) {
        this.val = val;
    }
}
