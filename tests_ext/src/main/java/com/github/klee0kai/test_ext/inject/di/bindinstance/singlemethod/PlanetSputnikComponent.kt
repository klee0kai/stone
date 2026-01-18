package com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.ExtendOf
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponent
import com.github.klee0kai.test_ext.inject.mowgli.galaxy.sputniks.Moon

@Component
interface PlanetSputnikComponent : PlanetComponent {

    override fun sunModule(): StarModule

    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun moon(moon: Moon?): Moon?

    @ExtendOf
    fun extendOf(parent: PlanetComponent?)

}
