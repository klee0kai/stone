package com.github.klee0kai.test.boxed.model;

import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import javax.inject.Inject;

public class CarBoxedInject {


    @Inject
    public CarBox<Bumper> bumper;

    @Inject
    public CarBox<Wheel> wheel;

    @Inject
    public CarBox<Window> window;

    public CarBox<Bumper> bumperFromMethod;

    public CarBox<Wheel> wheelFromMethod;

    public CarBox<Window> windowFromMethod;


    @Inject
    public void init(CarBox<Bumper> bumper, CarBox<Wheel> wheel, CarBox<Window> window) {
        bumperFromMethod = bumper;
        wheelFromMethod = wheel;
        windowFromMethod = window;
    }

}
