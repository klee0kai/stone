package com.github.klee0kai.wiki.provide.identifiers

import com.github.klee0kai.stone.annotations.component.Component

@Component(identifiers = [ScreenId::class, LoginId::class])
interface AppComponent {
    fun planetsModule(): SevenPlanetModule

    fun threadsModule(): ThreadsModule

    fun presentersModule(): PresentersModule

    fun featurePresenter(loginId: LoginId, screenId: ScreenId): FeaturePresenter

    fun inject(screen: FeatureScreen, loginId: LoginId, screenId: ScreenId)
}
