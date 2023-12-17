package com.github.klee0kai.wiki.init

import com.github.klee0kai.stone.Stone

fun main(args: Array<String>) {
    val DI = Stone.createComponent(AppComponent::class.java)
    val DIPro = Stone.createComponent(AppProComponent::class.java)
    DIPro.extendComponent(DI)
}
