package com.github.klee0kai.test.di.base_phone

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.tech.phone.GoodPhone
import com.github.klee0kai.test.tech.phone.OnePhone

@Component(
    identifiers = [
        DataStorageSize::class, RamSize::class, PhoneOsType::class, PhoneOsVersion::class
    ]
)
interface PhoneComponent {
    
    fun components(): TechModule?

    fun inject(onePhone: OnePhone?)

    fun inject(goodPhone: GoodPhone?, dataStorageSize: DataStorageSize?, ramSize: RamSize?)

    fun inject(
        goodPhone: GoodPhone?,
        lifeCycleOwner: StoneLifeCycleOwner?,
        dataStorageSize: DataStorageSize?,
        ramSize: RamSize?
    )
}
