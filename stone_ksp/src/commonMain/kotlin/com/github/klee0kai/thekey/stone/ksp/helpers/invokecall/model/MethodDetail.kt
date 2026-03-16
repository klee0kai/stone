package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model

import com.github.klee0kai.thekey.stone.ksp.helpers.qualifierAnnotations
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName

data class MethodDetail(
    val methodName: String,
    val returnType: TypeName,
    val args: List<FieldDetail> = emptyList(),
    val qualifierAnns: Set<QualifierAnn> = emptySet(),
    val isProperty: Boolean = false,
) {
    companion object;
}

fun KSFunctionDeclaration.toMethodDetail() = MethodDetail(
    methodName = simpleName.asString(),
    returnType = returnType?.resolve()?.toTypeName() ?: Unit::class.asClassName(),
    args = parameters.map { it.toFieldDetail() },
    qualifierAnns = qualifierAnnotations.map { it.toQualifierAnn() }.toSet(),
)

