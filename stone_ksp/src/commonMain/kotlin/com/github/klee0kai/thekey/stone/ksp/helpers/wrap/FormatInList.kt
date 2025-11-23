package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.SmartCodeScopeBuilder

fun interface FormatInList {

    /**
     * @param itemTransformFun format each type
     * @return
     */
    fun SmartCodeScopeBuilder.formatCode(itemTransformFun: FormatSimple)
}
