package com.github.klee0kai.test.core.di

import com.github.klee0kai.stone.annotations.component.Init
import com.github.klee0kai.stone.annotations.component.ModuleOriginFactory
import com.github.klee0kai.test.core.di.modules.BirdsModule
import com.github.klee0kai.test.core.di.modules.TreesModule

interface CoreComponentModules {

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


}