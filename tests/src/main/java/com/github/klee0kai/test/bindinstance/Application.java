package com.github.klee0kai.test.bindinstance;

import java.util.UUID;

public class Application extends Context {

    public UUID uuid = UUID.randomUUID();

    public static Application create() {
        return new Application();
    }

    protected Application() {
        super();
    }

}
