package com.github.klee0kai.test.tech.components;

import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsVersion;

import java.util.UUID;

public class OperationSystem {

    public final UUID uuid = UUID.randomUUID();
    public final PhoneOsType phoneOsType;
    public final PhoneOsVersion version;


    public OperationSystem(PhoneOsType phoneOsType) {
        this.phoneOsType = phoneOsType;
        this.version = null;
    }

    public OperationSystem(PhoneOsType phoneOsType, PhoneOsVersion version) {
        this.phoneOsType = phoneOsType;
        this.version = version;
    }
}
