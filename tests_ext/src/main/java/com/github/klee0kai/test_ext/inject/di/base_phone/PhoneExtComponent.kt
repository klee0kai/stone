package com.github.klee0kai.test_ext.inject.di.base_phone

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.ExtendOf
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.test.di.base_phone.PhoneComponent
import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.tech.phone.GoodPhone
import com.github.klee0kai.test.tech.phone.OnePhone

@Component
interface PhoneExtComponent : PhoneComponent {

    override fun components(): TechExtModule?

    @ExtendOf
    fun extOf(parent: PhoneComponent?)

    fun injectExt(onePhone: OnePhone?)

    fun injectExt(goodPhone: GoodPhone?, dataStorageSize: DataStorageSize?, ramSize: RamSize?)

    fun injectExt(
        goodPhone: GoodPhone?,
        lifeCycleOwner: StoneLifeCycleOwner?,
        dataStorageSize: DataStorageSize?,
        ramSize: RamSize?
    )
}
