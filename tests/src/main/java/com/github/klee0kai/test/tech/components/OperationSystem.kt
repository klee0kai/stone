package com.github.klee0kai.test.tech.components

import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion
import java.util.*

class OperationSystem {
    @JvmField
    val uuid: UUID = UUID.randomUUID()
    @JvmField
    val phoneOsType: PhoneOsType?
    @JvmField
    val version: PhoneOsVersion?


    constructor(phoneOsType: PhoneOsType?) {
        this.phoneOsType = phoneOsType
        this.version = PhoneOsVersion("default")
    }

    constructor(phoneOsType: PhoneOsType?, version: PhoneOsVersion?) {
        this.phoneOsType = phoneOsType
        this.version = version
    }
}
