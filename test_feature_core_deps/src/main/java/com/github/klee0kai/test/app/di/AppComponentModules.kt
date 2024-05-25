package com.github.klee0kai.test.app.di

import com.github.klee0kai.stone.annotations.component.Init
import com.github.klee0kai.stone.annotations.component.ModuleOriginFactory
import com.github.klee0kai.test.app.di.module.MushroomsModule

interface AppComponentModules {

    fun mushroomsModule(): MushroomsModule

    @ModuleOriginFactory
    fun mushroomModuleFactory(): MushroomsModule

    @Init
    fun initMushroomsModule(module: MushroomsModule)

}