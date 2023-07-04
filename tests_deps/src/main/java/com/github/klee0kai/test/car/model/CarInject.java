package com.github.klee0kai.test.car.model;

import javax.inject.Inject;

public class CarInject {


    @Inject
    public Bumper bumper;

    @Inject
    public Wheel wheel;

    @Inject
    public Window window;

    public Bumper bumperFromMethod;

    public Wheel wheelFromMethod;

    public Window windowFromMethod;


    @Inject
    public void init(Bumper bumper, Wheel wheel, Window window) {
        bumperFromMethod = bumper;
        wheelFromMethod = wheel;
        windowFromMethod = window;
    }

}
