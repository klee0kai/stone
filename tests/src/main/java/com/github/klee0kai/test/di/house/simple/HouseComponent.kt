package com.github.klee0kai.test.di.house.simple

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.ModuleOriginFactory
import com.github.klee0kai.test.house.House
import com.github.klee0kai.test.house.InHouse
import com.github.klee0kai.test.house.identifiers.StoreAreaType
import com.github.klee0kai.test.house.kitchen.storagearea.Sanitizers

@Component(identifiers = [StoreAreaType::class])
interface HouseComponent {
    fun module(): HouseModule?

    @ModuleOriginFactory
    fun moduleFactory(): HouseModule?

    fun rooms(): RoomsModule?

    fun area(): AreasModule?

    fun tools(): ToolsModule?

    fun house(): House?

    fun sanitizers(): Sanitizers?

    fun house(type: StoreAreaType?): House?

    fun inject(storeAreaType: StoreAreaType?, inHouse: InHouse?)

    fun inject(inHouse: InHouse?)
}
