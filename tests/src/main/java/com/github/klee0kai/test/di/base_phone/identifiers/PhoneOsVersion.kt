package com.github.klee0kai.test.di.base_phone.identifiers

import java.util.*

class PhoneOsVersion(@JvmField var version: String?) {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PhoneOsVersion
        return version == that.version
    }

    override fun hashCode(): Int {
        return Objects.hash(version)
    }
}
