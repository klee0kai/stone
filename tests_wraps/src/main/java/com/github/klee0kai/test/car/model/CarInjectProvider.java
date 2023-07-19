package com.github.klee0kai.test.car.model;

import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.stone.types.wrappers.Ref;
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.CarLazy;

import javax.inject.Inject;

public class CarInjectProvider {

    @Inject
    public LazyProvide<Bumper> bumper;

    @Inject
    public Ref<Wheel> wheel;

    @Inject
    public Ref<CarLazy<Window>> window;

    public LazyProvide<Bumper> bumperFromMethod;

    public Ref<Wheel> wheelFromMethod;

    public Ref<CarLazy<Window>> windowFromMethod;

    @Inject
    public void init(LazyProvide<Bumper> bumper, Ref<Wheel> wheel, Ref<CarLazy<Window>> window) {
        bumperFromMethod = bumper;
        wheelFromMethod = wheel;
        windowFromMethod = window;
    }

}
