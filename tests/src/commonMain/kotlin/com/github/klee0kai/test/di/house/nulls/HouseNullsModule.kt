package com.github.klee0kai.test.di.house.nulls

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.house.House
import com.github.klee0kai.test.house.kitchen.Kichen

@Module
open class HouseNullsModule {
    open fun kichen(): Kichen? {
        return Kichen(null, null, null)
    }

    open fun house(kichen: Kichen?): House? {
        return House(kichen, null, null, null)
    }
}
