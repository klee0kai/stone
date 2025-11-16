package com.github.klee0kai.thekey.stone.ksp.poet.smartcode

import com.squareup.kotlinpoet.ClassName

fun interface SimpleCodeBuilder {

    fun collect(
        declaredVariables: Map<String, ClassName>
    ): CollectedSmartCode

}

