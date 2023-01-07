package com.github.klee0kai.test.tech.components;

import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;

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
