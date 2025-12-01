package com.github.klee0kai.thekey.stone.ksp.helpers

import com.github.klee0kai.thekey.stone.ksp.ksp.isAnyType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

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

fun KSType.noWrappedType(
    wrappedTypes: List<KSType>,
): KSType {
    if (arguments.isEmpty()) return this
    if (this in wrappedTypes) {
        return arguments
            .first().type?.resolve()
            ?.noWrappedType(wrappedTypes)
            ?: this
    }
    return this
}

fun KSType.listWrapTypeIfNeed(
    wrappedTypes: List<KSType>,
): TypeName {
    if (!isListType()) return noWrappedType(wrappedTypes).toClassName()
    return List::class.asClassName().parameterizedBy(noWrappedType(wrappedTypes).toClassName())
}
