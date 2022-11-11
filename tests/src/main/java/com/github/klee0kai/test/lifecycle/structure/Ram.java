package com.github.klee0kai.test.lifecycle.structure;

import com.github.klee0kai.test.lifecycle.di.qualifiers.RamSize;

import java.util.UUID;

public class Ram {

    public final UUID uuid = UUID.randomUUID();

    public final String size;

    public Ram() {
        size = null;
    }

    public Ram(RamSize ramSize) {
        this.size = ramSize.size;
    }
}
