package com.github.klee0kai.test_kotlin.tech

import com.github.klee0kai.test_kotlin.di.base_comp.CompComponent
import com.github.klee0kai.test_kotlin.di.base_comp.CompComponentStoneComponent

object ComputerStore {
    var DI: CompComponent = CompComponentStoneComponent()

    fun recreate() {
        DI = CompComponentStoneComponent()
    }
}