package com.github.klee0kai.test.car.model;

import java.util.UUID;

public class Bumper {

    public static int createCount = 0;
    public String uuid = UUID.randomUUID().toString();
    public String qualifier = null;

    public Bumper() {
        createCount++;
    }

}
