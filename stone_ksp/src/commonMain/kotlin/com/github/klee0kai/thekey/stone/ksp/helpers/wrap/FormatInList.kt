package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName

fun interface FormatInList {

    /**
     * @param itemTransformFun format each type
     * @return
     */
    fun formatCode(
        originalListType: TypeName,
        or: CodeBlock,
        itemTransformFun: FormatSimple,
    ): CodeBlock

}
