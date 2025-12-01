package com.github.klee0kai.thekey.stone.ksp.utils

object LocalFieldName {
    private var localVariableIndx: Long = 0
    fun genLocalFieldName() = "_lc" + localVariableIndx++
}