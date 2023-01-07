package com.github.klee0kai.test.bindinstance.di

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.interfaces.IComponent

@Component(qualifiers = [String::class])
abstract class BindInstanceTextComponent : IComponent {
    abstract fun appModule(): AppModule
}