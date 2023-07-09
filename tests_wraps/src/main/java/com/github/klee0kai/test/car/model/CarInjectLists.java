package com.github.klee0kai.test.car.model;

import javax.inject.Inject;
import java.util.List;

public class CarInjectLists {


    @Inject
    public List<Bumper> bumpers;

    @Inject
    public List<Wheel> wheels;

    @Inject
    public List<Window> windows;

    public List<Bumper> bumpersMethodFrom;

    public List<Wheel> wheelsMethodFrom;

    public List<Window> windowsMethodFrom;

    @Inject
    public void init(List<Bumper> bumpers, List<Wheel> wheels, List<Window> windows) {
        bumpersMethodFrom = bumpers;
        wheelsMethodFrom = wheels;
        windowsMethodFrom = windows;
    }


}
