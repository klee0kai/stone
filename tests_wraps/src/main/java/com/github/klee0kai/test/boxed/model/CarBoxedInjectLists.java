package com.github.klee0kai.test.boxed.model;

import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import javax.inject.Inject;
import java.util.List;

public class CarBoxedInjectLists {


    @Inject
    public List<CarBox<Bumper>> bumpers;

    @Inject
    public List<CarBox<Wheel>> wheels;

    @Inject
    public List<CarBox<Window>> windows;

    public List<CarBox<Bumper>> bumpersMethodFrom;

    public List<CarBox<Wheel>> wheelsMethodFrom;

    public List<CarBox<Window>> windowsMethodFrom;

    @Inject
    public void init(List<CarBox<Bumper>> bumpers, List<CarBox<Wheel>> wheels, List<CarBox<Window>> windows) {
        bumpersMethodFrom = bumpers;
        wheelsMethodFrom = wheels;
        windowsMethodFrom = windows;
    }


}
