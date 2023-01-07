package com.github.klee0kai.test.di.base_phone.qualifiers;

import java.util.Objects;

public class DataStorageSize {

    public String size;

    public DataStorageSize(String size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataStorageSize that = (DataStorageSize) o;
        return Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size);
    }
}
