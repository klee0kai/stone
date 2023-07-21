package com.github.klee0kai.test_ext.inject.tech.components;

import com.github.klee0kai.test.di.base_phone.identifiers.RamSize;
import com.github.klee0kai.test.tech.components.Ram;
import com.github.klee0kai.test_ext.inject.di.techfactory.identifiers.Frequency;

import java.util.UUID;

public class DDR3Ram extends Ram {

    public final UUID uuid = UUID.randomUUID();

    public final String frequency;

    public DDR3Ram() {
        super();
        frequency = "default";
    }

    public DDR3Ram(RamSize ramSize) {
        super(ramSize);
        this.frequency = "default";
    }

    public DDR3Ram(RamSize ramSize, Frequency frequency) {
        super(ramSize);
        this.frequency = frequency.frequency;
    }

}
