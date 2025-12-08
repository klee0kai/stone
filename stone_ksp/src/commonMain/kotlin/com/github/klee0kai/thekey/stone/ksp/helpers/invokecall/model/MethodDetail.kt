package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model

import com.github.klee0kai.thekey.stone.ksp.helpers.qualifierAnnotations
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

class MethodDetail(
    val methodName: String,
    val returnType: TypeName,
    val args: List<FieldDetail> = emptyList(),
    val qualifierAnns: List<KSAnnotation> = emptyList(),
    val kSValueParameter: KSFunctionDeclaration? = null,
) {
    companion object

    override

    fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MethodDetail

        if (methodName != other.methodName) return false
        if (returnType != other.returnType) return false
        if (args != other.args) return false

        return true
    }

    override fun hashCode(): Int {
        var result = methodName.hashCode()
        result = 31 * result + returnType.hashCode()
        result = 31 * result + args.hashCode()
        return result
    };


}

fun KSFunctionDeclaration.toMethodDetail() = MethodDetail(
    methodName = simpleName.asString(),
    returnType = returnType?.resolve()?.toClassName() ?: Unit::class.asClassName(),
    args = parameters.map { it.toFieldDetail() },
    qualifierAnns = qualifierAnnotations.toList(),
    kSValueParameter = this,
)

