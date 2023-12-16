package com.github.klee0kai.wiki.provide.binding

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.mowgli.galaxy.Sun

fun main(args: Array<String>) {
    val DI = Stone.createComponent(GodWorkspaceComponent::class.java)
    val sun = Sun()
    DI.sunSystem().sun(sun)

    val sunFromDI = DI.sunSystem().sun(null)
    println("sunFromDI $sunFromDI")
}
