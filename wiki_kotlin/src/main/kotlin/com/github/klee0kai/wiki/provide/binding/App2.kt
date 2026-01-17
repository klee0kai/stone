package com.github.klee0kai.wiki.provide.binding

import com.github.klee0kai.test.mowgli.galaxy.Sun

fun main(args: Array<String>) {
    val DI = SunSystemComponentStoneComponent()
    val sun = Sun()
    DI.sun(sun)


    val sunFromDI = DI.sun(null)
    println("sunFromDI $sunFromDI")
}
