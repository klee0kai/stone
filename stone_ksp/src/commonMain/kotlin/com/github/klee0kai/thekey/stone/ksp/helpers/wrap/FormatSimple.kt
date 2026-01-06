package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.squareup.kotlinpoet.CodeBlock


fun interface FormatSimple {

    /**
     * @param code code witch return original type
     * @return code witch return wanna type
     */
    fun formatCode(
        or: CodeBlock,
    ): CodeBlock


}


fun interface UnwrapFun {

    /**
     * @param code code witch return original type
     * @return code witch return wanna type
     */
    fun formatCode(
        or: CodeBlock,
        srcNullable: Boolean,
        targetNullable: Boolean,
    ): CodeBlock


}


fun interface WrapFun {

    /**
     * @param code code witch return original type
     * @return code witch return wanna type
     */
    fun formatCode(
        or: CodeBlock,
        srcNullable: Boolean,
        targetNullable: Boolean,
        targetArgTypeNullable: Boolean,
    ): CodeBlock


}


