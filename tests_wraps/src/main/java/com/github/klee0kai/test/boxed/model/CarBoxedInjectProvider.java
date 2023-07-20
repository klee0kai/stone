package com.github.klee0kai.test.boxed.model;

import com.github.klee0kai.stone.wrappers.LazyProvide;
import com.github.klee0kai.stone.wrappers.Ref;
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.CarLazy;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import javax.inject.Inject;

public class CarBoxedInjectProvider {

    @Inject
    public LazyProvide<CarBox<Bumper>> bumper;

    @Inject
    public Ref<CarBox<Wheel>> wheel;

    @Inject
    public Ref<CarLazy<CarBox<Window>>> window;

    public LazyProvide<CarBox<Bumper>> bumperFromMethod;

    public Ref<CarBox<Wheel>> wheelFromMethod;

    public Ref<CarLazy<CarBox<Window>>> windowFromMethod;

    @Inject
    public void init(LazyProvide<CarBox<Bumper>> bumper, Ref<CarBox<Wheel>> wheel, Ref<CarLazy<CarBox<Window>>> window) {
        bumperFromMethod = bumper;
        wheelFromMethod = wheel;
        windowFromMethod = window;
    }

}
