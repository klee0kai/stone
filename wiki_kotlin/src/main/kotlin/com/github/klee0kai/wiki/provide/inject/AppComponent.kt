package com.github.klee0kai.wiki.provide.inject

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.wiki.provide.identifiers.LoginId
import com.github.klee0kai.wiki.provide.identifiers.PresentersModule
import com.github.klee0kai.wiki.provide.identifiers.ScreenId
import com.github.klee0kai.wiki.provide.identifiers.ThreadsModule

@Component(identifiers = [ScreenId::class, LoginId::class])
interface AppComponent {

    fun threadsModule(): ThreadsModule

    fun presentersModule(): PresentersModule

    fun inject(screen: FeatureScreen)

    fun inject(screen: FeatureScreen, owner: StoneLifeCycleOwner, loginId: LoginId, screenId: ScreenId)

}
