package com.github.klee0kai.test.mowgli.body;

import java.awt.*;
import java.util.UUID;

public class Blood {

    public UUID uuid = UUID.randomUUID();

    public Color color = null;

    public Blood(Color color) {
        this.color = color;
    }
}
