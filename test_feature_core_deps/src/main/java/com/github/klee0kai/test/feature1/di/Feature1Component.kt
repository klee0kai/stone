package com.github.klee0kai.test.feature1.di

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.Init
import com.github.klee0kai.stone.annotations.component.ModuleOriginFactory
import com.github.klee0kai.test.core.di.dependecies.CoreDepependenciesProvider
import com.github.klee0kai.test.core.di.wrapper.CustomWrappersStone
import com.github.klee0kai.test.feature1.di.dependencies.Feature1DependencyProvider
import com.github.klee0kai.test.feature1.di.module.BerriesModule

@Component(
    wrapperProviders = [
        CustomWrappersStone::class,
    ],
)
interface Feature1Component : Feature1DependencyProvider, CoreDepependenciesProvider {

    fun berriesModule(): BerriesModule

    @ModuleOriginFactory
    fun berriesModuleFactory(): BerriesModule

    @Init
    fun initBerriesModule(module: BerriesModule)

    fun coreDependencies(): CoreDepependenciesProvider

    @Init
    fun initCoreDependencies(deps: CoreDepependenciesProvider)


}