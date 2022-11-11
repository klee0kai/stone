package com.github.klee0kai.test.lifecycle.structure;

import com.github.klee0kai.test.lifecycle.di.qualifiers.DataStorageSize;

import java.util.UUID;

public class DataStorage {

    public final UUID uuid = UUID.randomUUID();

    public final String size;

    public DataStorage() {
        size = null;
    }

    public DataStorage(DataStorageSize size) {
        this.size = size.size;
    }

}
