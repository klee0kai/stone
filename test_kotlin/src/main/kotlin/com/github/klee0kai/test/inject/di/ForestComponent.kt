package com.github.klee0kai.test.inject.di

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.GcAllScope
import com.github.klee0kai.stone.annotations.component.ProtectInjected
import com.github.klee0kai.stone.interfaces.IComponent
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner
import com.github.klee0kai.test.inject.Horse
import com.github.klee0kai.test.inject.Mowgli
import com.github.klee0kai.test.inject.School
import com.github.klee0kai.test.inject.Snake

@Component
interface ForestComponent : IComponent {
    fun united(): UnitedModule
    fun identity(): IdentityModule
    fun inject(horse: Horse?, iStoneLifeCycleOwner: IStoneLifeCycleOwner?)
    fun inject(mowgli: Mowgli?)
    fun inject(snake: Snake?)
    fun inject(school: School?)

    @GcAllScope
    fun gcAll()

    @ProtectInjected(timeMillis = 50)
    fun protectInjected(horse: Horse?)

    @ProtectInjected(timeMillis = 50)
    fun protectInjected(horse: Mowgli?)

    @ProtectInjected(timeMillis = 50)
    fun protectInjected(school: School?)
}