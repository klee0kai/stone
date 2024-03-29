package com.github.klee0kai.test.tech.components;

import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize;

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
