package com.github.klee0kai.test.bindinstance;

import java.util.UUID;

public class Context {

    public UUID uuid = UUID.randomUUID();


    public static Context create() {
        return new Context();
    }

    protected Context() {

    }

}
