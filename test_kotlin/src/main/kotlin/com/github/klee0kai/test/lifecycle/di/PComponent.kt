package com.github.klee0kai.test.lifecycle.di

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner
import com.github.klee0kai.test.lifecycle.GoodPhone
import com.github.klee0kai.test.lifecycle.OnePhone
import com.github.klee0kai.test.lifecycle.di.qualifier.DataStorageSize
import com.github.klee0kai.test.lifecycle.di.qualifier.RamSize

@Component(qualifiers = [DataStorageSize::class, RamSize::class])
interface PComponent {
    fun structure(): Structure?
    fun inject(onePhone: OnePhone?)
    fun inject(goodPhone: GoodPhone?, dataStorageSize: DataStorageSize?, ramSize: RamSize?)
    fun inject(
        goodPhone: GoodPhone?,
        lifeCycleOwner: IStoneLifeCycleOwner?,
        dataStorageSize: DataStorageSize?,
        ramSize: RamSize?,
    )
}