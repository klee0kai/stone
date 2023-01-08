package com.github.klee0kai.test.di.base_phone.qualifiers;

import java.util.Objects;

public class PhoneOsVersion {
    public String version;

    public PhoneOsVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneOsVersion that = (PhoneOsVersion) o;
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }
}
