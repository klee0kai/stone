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
// TODO https://github.com/klee0kai/stone/issues/116
//  make the use of interfaces position independent
interface AppComponent : AppComponentModules, AppDependencyProvider, CoreComponent {

    @ExtendOf
    fun ext(core: CoreComponent)

}