package com.github.klee0kai.thekey.stone.ksp.ksp.arch

import com.google.devtools.ksp.processing.Dependencies
import com.squareup.kotlinpoet.FileSpec

data class GenSpec(
    val fileSpec: FileSpec,
    val dependencies: Dependencies,
)
