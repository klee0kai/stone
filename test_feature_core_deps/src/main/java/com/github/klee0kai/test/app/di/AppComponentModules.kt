package com.github.klee0kai.test.app.di

import com.github.klee0kai.stone.annotations.component.Init
import com.github.klee0kai.stone.annotations.component.ModuleOriginFactory
import com.github.klee0kai.test.app.di.module.AppBirdsModule
import com.github.klee0kai.test.app.di.module.MushroomsModule
import com.github.klee0kai.test.core.di.CoreComponentModules

interface AppComponentModules : CoreComponentModules {

    override fun birdsModule(): AppBirdsModule

    fun mushroomsModule(): MushroomsModule

    @ModuleOriginFactory
    fun mushroomModuleFactory(): MushroomsModule

    @ModuleOriginFactory
    override fun birdsModuleFactory(): AppBirdsModule

    @Init
    fun initMushroomsModule(module: MushroomsModule)

    @Init
    fun initBirdsModule(module: AppBirdsModule)
}