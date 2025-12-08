package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model

import com.github.klee0kai.thekey.stone.ksp.helpers.qualifierAnnotations
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName

data class FieldDetail(
    val name: String,
    val type: TypeName,
    val qualifierAnns: Set<KSAnnotation> = emptySet(),
    val kSValueParameter: KSValueParameter? = null,
) {
    companion object;

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FieldDetail

        if (name != other.name) return false
        if (type != other.type) return false
        if (qualifierAnns != other.qualifierAnns) return false
        if (kSValueParameter != other.kSValueParameter) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + qualifierAnns.hashCode()
        result = 31 * result + (kSValueParameter?.hashCode() ?: 0)
        return result
    }

}

fun FieldDetail.Companion.simple(name: String, type: TypeName) = FieldDetail(name, type)


fun KSValueParameter.toFieldDetail() = FieldDetail(
    name = name?.asString() ?: "it",
    type = type.resolve().toClassName(),
    qualifierAnns = qualifierAnnotations.toSet(),
    kSValueParameter = this,
)

