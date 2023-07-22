package com.github.klee0kai.stone.kotlin.test.utils

object KotlinUtils {

    /**
     * simple kotlin GC bug go-round
     *
     * [Research](https://github.com/klee0kai/KotlinMemLeakResearch)
     */
    inline fun resetKotlinRegisters() {
        var someVar: List<String>? = listOf(1, 2, 3).map { it.toString() }
        someVar = null
    }

}