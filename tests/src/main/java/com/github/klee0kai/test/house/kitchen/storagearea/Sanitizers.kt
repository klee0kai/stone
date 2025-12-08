package com.github.klee0kai.test.house.kitchen.storagearea;

import java.util.UUID;

public class Sanitizers {

    public static int createCount = 0;

    public UUID uuid = UUID.randomUUID();

    public Sanitizers() {
        createCount++;
    }

}
