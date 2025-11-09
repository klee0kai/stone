package com.github.klee0kai.thekey.stone.ksp.poet

val String.stonePackageName: String
    get() {
        return if (endsWith(".stone")) this
        else "$this.stone"
    }