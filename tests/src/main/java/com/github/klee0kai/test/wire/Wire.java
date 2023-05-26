package com.github.klee0kai.test.wire;

import java.util.UUID;

public class Wire<Input, Output> {

    public final UUID uuid = UUID.randomUUID();

    public Input input;

    public Output output;

}
