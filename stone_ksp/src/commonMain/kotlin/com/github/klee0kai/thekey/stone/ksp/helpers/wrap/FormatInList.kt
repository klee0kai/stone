package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.SmartCode

fun interface FormatInList {

    /**
     * @param itemTransformFun format each type
     * @return
     */
    fun formatCode(
        or: SmartCode,
        itemTransformFun: FormatSimple,
    ): SmartCode

}
