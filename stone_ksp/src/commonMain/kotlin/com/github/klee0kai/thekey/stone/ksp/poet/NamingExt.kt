package com.github.klee0kai.thekey.stone.ksp.poet

val String.crossboxPackageName: String
    get() {
        return if (endsWith(".crossbox")) this
        else "$this.crossbox"
    }