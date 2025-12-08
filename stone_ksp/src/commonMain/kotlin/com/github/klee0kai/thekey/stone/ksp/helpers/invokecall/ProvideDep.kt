package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall

import com.google.devtools.ksp.symbol.KSAnnotation
import com.squareup.kotlinpoet.TypeName

data class ProvideDep(
    val methodName: String?,
    val typeName: TypeName,
    val qualifierAnns: Set<KSAnnotation>,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProvideDep

        if (methodName != other.methodName) return false
        if (typeName != other.typeName) return false
        if (qualifierAnns != other.qualifierAnns) return false

        return true
    }

    override fun hashCode(): Int {
        var result = methodName?.hashCode() ?: 0
        result = 31 * result + typeName.hashCode()
        result = 31 * result + qualifierAnns.hashCode()
        return result
    }
}

