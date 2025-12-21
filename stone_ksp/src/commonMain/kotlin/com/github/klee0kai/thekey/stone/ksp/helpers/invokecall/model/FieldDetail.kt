package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model

import com.github.klee0kai.stone.annotations.qualifier.IgnoreQualifier
import com.github.klee0kai.thekey.stone.ksp.helpers.qualifierAnnotations
import com.github.klee0kai.thekey.stone.ksp.ksp.resolveAlias
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName

data class FieldDetail(
    val name: String,
    val type: TypeName,
    val qualifierAnns: Set<QualifierAnn> = emptySet(),
) {
    companion object;


}

fun FieldDetail.Companion.simple(name: String, type: TypeName) = FieldDetail(name, type)

fun KSValueParameter.toFieldDetail() = FieldDetail(
    name = name?.asString() ?: "it",
    type = type.resolveAlias().toTypeName().copy(nullable = false),
    qualifierAnns = qualifierAnnotations.map { it.toQualifierAnn() }.toSet(),
)

fun Set<QualifierAnn>.anyIgnoreQualifier(
): Boolean = any { it.typeName == IgnoreQualifier::class.asClassName() }


