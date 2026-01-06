package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.squareup.kotlinpoet.TypeName

data class WrapType(
    val typeName: TypeName,
    val unwrap: UnwrapFun,
    val wrap: WrapFun,
    val isNoCachingWrapper: Boolean = true,
    val inListFormat: FormatInList? = null,
) {
    val isList: Boolean get() = inListFormat != null
}

