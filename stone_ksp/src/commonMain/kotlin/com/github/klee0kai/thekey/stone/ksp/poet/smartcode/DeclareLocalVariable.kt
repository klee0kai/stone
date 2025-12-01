package com.github.klee0kai.thekey.stone.ksp.poet.smartcode

import com.google.devtools.ksp.symbol.KSAnnotation
import com.squareup.kotlinpoet.TypeName

data class DeclareLocalVariable(
    val variableName: String,
    val type: TypeName,
    val qualifierAnnotations: List<KSAnnotation> = emptyList(),
)
