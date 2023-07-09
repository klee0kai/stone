package com.github.klee0kai.test.boxed.model;

import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CarBoxed {

    public static int createCount = 0;
    public String uuid = UUID.randomUUID().toString();

    public List<CarBox<Bumper>> bumpers;
    public List<CarBox<Wheel>> wheels;
    public List<CarBox<Window>> windows;

    public CarBoxed(
            CarBox<Bumper> bumper,
            CarBox<Wheel> wheel,
            CarBox<Window> window
    ) {
        createCount++;
        bumpers = Collections.singletonList(bumper);
        wheels = Collections.singletonList(wheel);
        windows = Collections.singletonList(window);
    }


    public CarBoxed(List<CarBox<Bumper>> bumpers, List<CarBox<Wheel>> wheels, List<CarBox<Window>> windows) {
        createCount++;
        this.bumpers = bumpers;
        this.wheels = wheels;
        this.windows = windows;
    }
}
