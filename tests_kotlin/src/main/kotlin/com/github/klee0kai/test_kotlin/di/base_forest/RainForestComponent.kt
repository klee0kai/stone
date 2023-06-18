package com.github.klee0kai.test_kotlin.di.base_forest

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.GcAllScope
import com.github.klee0kai.stone.annotations.component.ProtectInjected
import com.github.klee0kai.stone.closed.IPrivateComponent
import com.github.klee0kai.test.di.base_forest.IdentityModule
import com.github.klee0kai.test.di.base_forest.UnitedModule
import com.github.klee0kai.test_kotlin.mowgli.University
import com.github.klee0kai.test_kotlin.mowgli.animal.Cougar
import com.github.klee0kai.test_kotlin.mowgli.animal.Gorilla

@Component
interface RainForestComponent : IPrivateComponent {

    fun united(): UnitedModule

    fun identity(): IdentityModule

    @GcAllScope
    fun gcAll()

    fun inject(university: University)
    fun inject(gorilla: Gorilla)
    fun inject(cougar: Cougar)

    @ProtectInjected(timeMillis = 50)
    fun protectInjected(gorrila: Gorilla)

    @ProtectInjected(timeMillis = 50)
    fun protectInjected(gorrila: University)

}