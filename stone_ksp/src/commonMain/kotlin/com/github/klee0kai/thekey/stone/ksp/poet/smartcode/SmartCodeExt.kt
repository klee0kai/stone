package com.github.klee0kai.thekey.stone.ksp.poet.smartcode

import com.github.klee0kai.thekey.stone.ksp.poet.PoetDsl
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock


@PoetDsl
fun SmartCodeScopeBuilder.add(
    codeBlock: CodeBlock,
    usedVariables: Set<String> = emptySet(),
    providingType: ClassName? = null,
) {
    codes.add(
        SimpleCodeBuilder {
            CollectedSmartCode(
                codeBlock = codeBlock,
                usedVariables = usedVariables,
                providingType = providingType,
            )
        })
}

@PoetDsl
fun SmartCodeScopeBuilder.add(
    codeBlock: String,
    usedVariables: Set<String> = emptySet(),
    providingType: ClassName? = null,
) {
    codes.add(
        SimpleCodeBuilder {
            CollectedSmartCode(
                codeBlock = CodeBlock.Builder().apply {
                    add(codeBlock)
                }.build(),
                usedVariables = usedVariables,
                providingType = providingType,
            )
        })
}

@PoetDsl
fun SmartCodeScopeBuilder.add(
    codeBlock: CollectedSmartCode.ScopedBuilder.() -> Unit,
) {
    codes.add(
        SimpleCodeBuilder {
            CollectedSmartCode.ScopedBuilder(it).apply(codeBlock).build()
        }
    )
}

@PoetDsl
fun SmartCodeScopeBuilder.smartCode(
    codeBlock: SmartCodeScopeBuilder.() -> Unit,
) {
    codes.add(
        SmartCodeScopeBuilder()
            .apply(codeBlock)
    )
}


