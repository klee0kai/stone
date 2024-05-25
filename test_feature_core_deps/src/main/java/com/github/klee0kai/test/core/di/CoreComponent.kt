package com.github.klee0kai.test.core.di

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.test.core.di.dependecies.CoreDepependenciesProvider
import com.github.klee0kai.test.core.di.wrapper.CustomWrappersStone
import com.github.klee0kai.test.core.forest.Alder
import com.github.klee0kai.test.core.forest.Ash
import com.github.klee0kai.test.core.forest.Beech

@Component(
    wrapperProviders = [
        CustomWrappersStone::class,
    ],
)
interface CoreComponent : CoreComponentModules, CoreDepependenciesProvider {

    @BindInstance(cache = BindInstance.CacheType.Strong)
    fun alder(alder: Alder): Alder

    @BindInstance(cache = BindInstance.CacheType.Soft)
    fun ash(alder: Ash): Ash

    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun beech(alder: Beech): Beech

}