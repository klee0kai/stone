package com.github.klee0kai.thekey.stone.ksp.poet.smartcode

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock

class CollectedSmartCode(
    val codeBlock: CodeBlock,
    val declareLocalVariable: Pair<String, ClassName>? = null,
    val usedVariables: Set<String> = emptySet(),
    val providingType: ClassName? = null,
) {

    class ScopedBuilder(
        val declaredVariables: Map<String, ClassName>,
    ) {
        val codeBuilder = CodeBlock.Builder()
        var declareLocalVariable: Pair<String, ClassName>? = null
        val usedVariables = mutableSetOf<String>()
        var providingType: ClassName? = null

        fun build() = CollectedSmartCode(
            codeBlock = codeBuilder.build(),
            declareLocalVariable = declareLocalVariable,
            usedVariables = usedVariables,
            providingType = providingType,
        )

    }

}

fun CollectedSmartCode.ScopedBuilder.add(
    string: String,
) {
    codeBuilder.add(string)
}

fun CollectedSmartCode.ScopedBuilder.add(
    codeBlock: CodeBlock,
) {
    codeBuilder.add(codeBlock)
}

