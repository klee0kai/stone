package com.github.klee0kai.test.di.base_forest

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.test.mowgli.School
import com.github.klee0kai.test.mowgli.animal.Horse
import com.github.klee0kai.test.mowgli.animal.Mowgli
import com.github.klee0kai.test.mowgli.animal.Snake

@Component
interface ForestComponent {
    fun united(): UnitedModule?

    fun identity(): IdentityModule?

    fun inject(horse: Horse?, stoneLifeCycleOwner: StoneLifeCycleOwner?)

    fun inject(horse: Horse?)


    fun inject(mowgli: Mowgli?)

    fun inject(snake: Snake?)

    fun inject(school: School?)

    @Init
    fun initUnitedModule(unitedModule: UnitedModule?)

    @Init
    fun iniAllModules(unitedModule: UnitedModule?, identityModule: IdentityModule?)

    @RunGc
    @GcAllScope
    fun gcAll()

    @ProtectInjected(timeMillis = 30)
    fun protectInjected(horse: Horse?)


    @ProtectInjected(timeMillis = 30)
    fun protectInjected(horse: Mowgli?)

    @ProtectInjected(timeMillis = 30)
    fun protectInjected(school: School?)
}
