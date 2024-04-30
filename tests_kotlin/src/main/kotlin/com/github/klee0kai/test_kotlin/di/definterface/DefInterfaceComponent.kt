package com.github.klee0kai.test_kotlin.di.definterface

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.ModuleOriginFactory
import com.github.klee0kai.test_kotlin.tech.components.Keyboard
import com.github.klee0kai.test_kotlin.tech.components.Mouse

@Component
interface DefInterfaceComponent {

    fun factory(): DefInterfaceModule

    @ModuleOriginFactory
    fun factoryOrigin(): DefInterfaceModule

    fun keyBoard(): Keyboard

    fun mouse(): Mouse

}