package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model

import com.github.klee0kai.stone.annotations.qualifier.IgnoreQualifier
import com.google.devtools.ksp.symbol.KSAnnotation
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName

data class QualifierAnn(
    var typeName: TypeName,
    var values: Map<String, Any?> = mapOf(),
) {
    companion object;
}

fun QualifierAnn.Companion.ignoreQualifier(
) = QualifierAnn(
    typeName = IgnoreQualifier::class.asTypeName(),
    values = emptyMap(),
)


fun KSAnnotation.toQualifierAnn(
) = QualifierAnn(
    typeName = annotationType.resolve().toClassName(),
    values = arguments.map { it.name?.asString()!! to it.value }.groupBy { it.first },
)

fun QualifierAnn.logString(): String {
    val type = (typeName as? ClassName)?.simpleName ?: typeName
    val value = values.values.joinToString(",")
    return "${type}( $values )"
}