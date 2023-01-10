package com.github.klee0kai.test_kotlin.di.compfactory

import com.github.klee0kai.stone.KotlinWrappersStone
import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsVersion
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize

@Component(
    qualifiers = [
        RamSize::class,
        PhoneOsType::class, PhoneOsVersion::class
    ],
    wrapperProviders = [KotlinWrappersStone::class]
)
interface CompFactoryComponent : ICompFactoryWrappersComponent {

    fun factory(): CompFactoryModule

}