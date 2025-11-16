package com.github.klee0kai.thekey.stone.ksp.poet.smartcode

import com.github.klee0kai.thekey.stone.ksp.poet.PoetDsl
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock

open class SmartCodeScopeBuilder() : SimpleCodeBuilder {

    val codes = mutableListOf<SimpleCodeBuilder>()

    var declareLocalVariable: Pair<String, ClassName>? = null
    var providingType: ClassName? = null

    override fun collect(
        declaredVariables: Map<String, ClassName>,
    ): CollectedSmartCode {
        val declaredVariables = declaredVariables.toMutableMap()
        val usedVariables = mutableSetOf<String>()
        val collectedList = mutableListOf<CollectedSmartCode>()
        for (code in codes) {
            val collected = code.collect(declaredVariables)
            if (collected.declareLocalVariable != null) {
                declaredVariables += collected.declareLocalVariable
            }

            usedVariables += collected.usedVariables
            collectedList += collected
        }

        val optimized = collectedList.filter {
            it.declareLocalVariable == null
                    || it.declareLocalVariable.first in usedVariables
        }

        return CollectedSmartCode(
            codeBlock = CodeBlock.builder().also { builder ->
                optimized.forEach {
                    builder.add(it.codeBlock)
                }
            }.build(),
            declareLocalVariable = declareLocalVariable,
            usedVariables = usedVariables,
            providingType = providingType,
        )
    }

}

@PoetDsl
fun smartCode(
    block: SmartCodeScopeBuilder.() -> Unit,
) = SmartCodeScopeBuilder()
    .apply(block)
    .collect(declaredVariables = emptyMap())
    .codeBlock