package com.github.klee0kai.stone.test

import com.tschuchort.compiletesting.SourceFile


fun SourceFile.Companion.fromResources(path: String): SourceFile {
    val content = object {}::class.java.classLoader
        .getResourceAsStream(path)
        ?.bufferedReader()
        ?.readText()
        ?: error("Resource not found: $path")

    return SourceFile.kotlin(path.substringAfterLast('/'), content)
}