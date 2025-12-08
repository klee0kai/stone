package com.github.klee0kai.test.tech.components;

import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion;

import java.util.UUID;

public class OperationSystem {

    public final UUID uuid = UUID.randomUUID();
    public final PhoneOsType phoneOsType;
    public final PhoneOsVersion version;


    public OperationSystem(PhoneOsType phoneOsType) {
        this.phoneOsType = phoneOsType;
        this.version = new PhoneOsVersion("default");
    }

    public OperationSystem(PhoneOsType phoneOsType, PhoneOsVersion version) {
        this.phoneOsType = phoneOsType;
        this.version = version;
    }
}
