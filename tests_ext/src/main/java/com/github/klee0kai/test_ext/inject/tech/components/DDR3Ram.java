package com.github.klee0kai.test_ext.inject.tech.components;

import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.components.Ram;
import com.github.klee0kai.test_ext.inject.di.techfactory.qualifiers.Frequency;

import java.util.UUID;

public class DDR3Ram extends Ram {

    public final UUID uuid = UUID.randomUUID();

    public final String size;
    public final String frequency;

    public DDR3Ram() {
        size = "default";
        frequency = "default";
    }

    public DDR3Ram(RamSize ramSize) {
        this.size = ramSize.size;
        this.frequency = "default";
    }

    public DDR3Ram(RamSize ramSize, Frequency frequency) {
        this.size = ramSize.size;
        this.frequency = frequency.frequency;
    }

}
