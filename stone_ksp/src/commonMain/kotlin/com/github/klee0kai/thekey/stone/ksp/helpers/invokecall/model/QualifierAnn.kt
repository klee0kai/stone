package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model

import com.google.devtools.ksp.symbol.KSAnnotation
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName

data class QualifierAnn(
    var typeName: TypeName,
    var values: Map<String, Any?> = mapOf(),
)

fun KSAnnotation.toQualifierAnn(
) = QualifierAnn(
    typeName = annotationType.resolve().toClassName(),
    values = arguments.map { it.name?.asString()!! to it.value }.groupBy { it.first },
)
