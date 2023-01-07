package com.github.klee0kai.test;

import java.util.UUID;

/**
 * @deprecated Develop models centrally.
 * Available forest, tech models
 */
@Deprecated
public class Context {

    public UUID uuid = UUID.randomUUID();


    public static Context create() {
        return new Context();
    }

    protected Context() {

    }

}
