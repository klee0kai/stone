package com.github.klee0kai.test_ext.inject.di.techfactory.identifiers;

import java.util.Objects;

public class Frequency {

    public String frequency;

    public Frequency(String frequency) {
        this.frequency = frequency;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Frequency frequency = (Frequency) o;
        return Objects.equals(this.frequency, frequency.frequency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(frequency);
    }
}
