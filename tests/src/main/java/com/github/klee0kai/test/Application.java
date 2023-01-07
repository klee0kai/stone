package com.github.klee0kai.test;

import java.util.UUID;


/**
 * @deprecated Develop models centrally.
 * Available forest, tech models
 */
@Deprecated
public class Application extends Context {

    public UUID uuid = UUID.randomUUID();

    public static Application create() {
        return new Application();
    }

    protected Application() {
        super();
    }

}
