@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.tech.components

import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion
import kotlin.jvm.JvmField
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class OperationSystem {

    @JvmField
    var uuid: String = Uuid.random().toString()

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
