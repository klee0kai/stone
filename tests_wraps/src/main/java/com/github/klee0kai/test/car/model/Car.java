package com.github.klee0kai.test.car.model;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Car {

    public static int createCount = 0;
    public String uuid = UUID.randomUUID().toString();

    public List<Bumper> bumpers;
    public List<Wheel> wheels;
    public List<Window> windows;

    public Car(
            Bumper bumper,
            Wheel wheel,
            Window window
    ) {
        createCount++;
        bumpers = Collections.singletonList(bumper);
        wheels = Collections.singletonList(wheel);
        windows = Collections.singletonList(window);
    }


    public Car(List<Bumper> bumpers, List<Wheel> wheels, List<Window> windows) {
        createCount++;
        this.bumpers = bumpers;
        this.wheels = wheels;
        this.windows = windows;
    }
}
