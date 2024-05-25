package com.github.klee0kai.test.app.di

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.ExtendOf
import com.github.klee0kai.test.app.di.dependencies.AppDependencyProvider
import com.github.klee0kai.test.core.di.CoreComponent
import com.github.klee0kai.test.core.di.wrapper.CustomWrappersStone


@Component(
    wrapperProviders = [
        CustomWrappersStone::class,
    ],
)
interface AppComponent : CoreComponent, AppComponentModules, AppDependencyProvider {

    @ExtendOf
    fun ext(core: CoreComponent)

}