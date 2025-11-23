package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.squareup.kotlinpoet.TypeName

class WrapType(
    val typeName: TypeName,
    val unwrap: FormatSimple,
    val wrap: FormatSimple,
    val isAsyncProvider: Boolean = true,
    val isNoCachingWrapper: Boolean = true,
    val inListFormat: FormatInList? = null,
) {
    val isList: Boolean get() = inListFormat != null
}
