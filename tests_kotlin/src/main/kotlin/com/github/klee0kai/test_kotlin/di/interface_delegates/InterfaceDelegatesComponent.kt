package com.github.klee0kai.test_kotlin.di.interface_delegates

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.test_kotlin.tech.components.Keyboard
import com.github.klee0kai.test_kotlin.tech.components.Mouse

@Component
interface InterfaceDelegatesComponent {

    fun factory(): InterfaceDelegatesModule

    fun keyBoard(): Keyboard

    fun mouse(): Mouse

}