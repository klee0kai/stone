package com.github.klee0kai.wiki.provide.inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.wiki.provide.identifiers.FeaturePresenter
import javax.inject.Inject

class FeatureScreen {

    @Inject
    lateinit var presenter: FeaturePresenter

    @Inject
    fun init(presenter: FeaturePresenter) {
    }

    fun start() {
        val DI = Stone.createComponent(AppComponent::class.java)
        DI.inject(this)
    }
}
