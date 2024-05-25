package com.github.klee0kai.test.core.di

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.Init
import com.github.klee0kai.stone.annotations.component.ModuleOriginFactory
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.test.core.di.dependecies.CoreDepependenciesProvider
import com.github.klee0kai.test.core.di.modules.BirdsModule
import com.github.klee0kai.test.core.di.modules.TreesModule
import com.github.klee0kai.test.core.di.wrapper.CustomWrappersStone
import com.github.klee0kai.test.core.forest.Alder
import com.github.klee0kai.test.core.forest.Ash
import com.github.klee0kai.test.core.forest.Beech

@Component(
    wrapperProviders = [
        CustomWrappersStone::class,
    ],
)
interface CoreComponent : CoreDepependenciesProvider {

    fun birdsModule(): BirdsModule

    fun treesModule(): TreesModule

    @ModuleOriginFactory
    fun birdsModuleFactory(): BirdsModule

    @ModuleOriginFactory
    fun treesModuleFactory(): TreesModule

    @Init
    fun initBirdsModule(module: BirdsModule)

    @Init
    fun initTreesModule(module: TreesModule)

    @BindInstance(cache = BindInstance.CacheType.Strong)
    fun alder(alder: Alder): Alder

    @BindInstance(cache = BindInstance.CacheType.Soft)
    fun ash(alder: Ash): Ash

    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun beech(alder: Beech): Beech

}