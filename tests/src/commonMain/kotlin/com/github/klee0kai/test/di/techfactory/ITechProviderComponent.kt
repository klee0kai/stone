package com.github.klee0kai.test.di.techfactory

import com.github.klee0kai.stone.Named
import com.github.klee0kai.stone.Provider
import com.github.klee0kai.stone.weakref.*
import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.stone.wrappers.PhantomProvide
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.tech.components.Battery
import com.github.klee0kai.test.tech.components.OperationSystem
import com.github.klee0kai.test.tech.components.Ram

interface ITechProviderComponent {

    fun batteryLazy(): LazyProvide<Battery?>?

    fun batteryProviderIRef(): Ref<Battery?>?

    fun batteryPhantomProvider(): PhantomProvide<Battery?>?

    fun batteryProvider(): Provider<Battery?>?

    fun batterySoft(): SoftRef<Battery?>?

    fun batteryWeak(): WeakRef<Battery?>?

    @Named("null_args")
    fun ram(): Ram?

    fun ram(ramSize: RamSize?): Ram?

    @Named("null_args")
    fun phoneOs(): OperationSystem?

    fun phoneOs(osType: PhoneOsType?): OperationSystem?

    fun phoneOs(phoneOsType: PhoneOsType?, version: PhoneOsVersion?): OperationSystem?
}
