package com.github.klee0kai.thekey.stone.ksp.poet.smartcode

import com.github.klee0kai.thekey.stone.ksp.property.Property
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName

class SmartCode(
    val parent: SmartCode? = null,
) {

    private val codes = Property<List<SmartCodeBlock>>(emptyList())

    val declareLocalVariable = Property<DeclareLocalVariable?>(null)
    val providingType = Property<TypeName?>(null)

    val availableVariables = Property<Map<String, TypeName>>(emptyMap())
    val usedVariables = Property<Set<String>>(emptySet())

    fun add(codeBlock: CodeBlock) {
        codes.value += SmartCodeBlock(codeBlock = codeBlock)
        parent?.providingType?.source = null
    }

    fun add(codeBlock: SmartCode) {
        codes.value += SmartCodeBlock(smartCode = codeBlock)
        parent?.providingType?.source = codeBlock.providingType
    }

    fun collect(
    ): CodeBlock {
        val allUsedVariables = usedVariables.value + codes.value
            .flatMap { childCode -> childCode.smartCode?.usedVariables?.value ?: emptySet<String>() }

        val optimizedCode = codes.value.filter { childCode ->
            childCode.smartCode == null
                    || childCode.smartCode.declareLocalVariable.value == null
                    || childCode.smartCode.declareLocalVariable.value?.variableName in allUsedVariables
        }

        val codeBlock = CodeBlock.Builder()
        optimizedCode.forEach { childCode ->
            when {
                childCode.codeBlock != null -> codeBlock.add(childCode.codeBlock)
                childCode.smartCode != null -> codeBlock.add(childCode.smartCode.collect())
            }
        }
        return codeBlock.build()
    }

}






