package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType


data class ProvideDep(
    val methodName: String,
    val type: KSType,
    val qualifiers: List<KSAnnotation>,
)

