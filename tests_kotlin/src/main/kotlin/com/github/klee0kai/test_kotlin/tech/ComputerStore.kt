package com.github.klee0kai.test_kotlin.tech

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test_kotlin.di.base_comp.CompComponent

object ComputerStore {
    var DI = Stone.createComponent(CompComponent::class.java)

    fun recreate() {
        DI = Stone.createComponent(CompComponent::class.java)
    }
}