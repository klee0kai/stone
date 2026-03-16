package com.github.klee0kai.wiki.provide.inject

import com.github.klee0kai.wiki.provide.identifiers.FeaturePresenter
import javax.inject.Inject

class FeatureScreen {

    @Inject
    lateinit var presenter: FeaturePresenter

    @Inject
    fun init(presenter: FeaturePresenter) {
    }

    fun start() {
        val DI = AppComponentStoneComponent()
        DI.inject(this)
    }
}
