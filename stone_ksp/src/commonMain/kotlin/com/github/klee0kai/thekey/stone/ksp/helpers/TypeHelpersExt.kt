package com.github.klee0kai.thekey.stone.ksp.helpers

import com.github.klee0kai.thekey.stone.ksp.ksp.isAnyType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType

fun KSType.isListType(
): Boolean {
    if (
        declaration.isAnyType(
            Iterable::class,
            List::class,
            MutableList::class,
            Collection::class,
        )
    ) {
        return true
    }


    return (this.declaration as? KSClassDeclaration)
        ?.superTypes
        ?.any { it.resolve().isListType() } == true
}
