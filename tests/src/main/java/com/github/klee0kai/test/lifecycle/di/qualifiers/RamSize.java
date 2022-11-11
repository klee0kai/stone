package com.github.klee0kai.test.lifecycle.di.qualifiers;

import java.util.Objects;

public class RamSize {

    public String size;

    public RamSize(String size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RamSize that = (RamSize) o;
        return Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size);
    }

}
