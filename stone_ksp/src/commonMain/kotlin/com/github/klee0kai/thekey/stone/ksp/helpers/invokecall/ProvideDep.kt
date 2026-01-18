package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall

import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model.QualifierAnn
import com.squareup.kotlinpoet.TypeName

data class ProvideDep(
    val methodName: String?,
    val typeName: TypeName,
    val qualifierAnns: Set<QualifierAnn>,
) {
    companion object;
}

