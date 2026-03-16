package com.github.klee0kai.thekey.stone.ksp.target.component

import com.github.klee0kai.thekey.stone.ksp.exceptions.forEachFun
import com.github.klee0kai.thekey.stone.ksp.helpers.allIdentifierTypes
import com.github.klee0kai.thekey.stone.ksp.helpers.allParentDeclarations
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.findComponentAnnotation
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.ModulesGraph
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapHelper
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapType
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.github.klee0kai.thekey.stone.ksp.ksp.resolveNotNullable
import com.github.klee0kai.thekey.stone.ksp.poet.codeBlock
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toClassName

fun KSClassDeclaration.collectWrapHelper(
): WrapHelper {
    val wrapHelper = WrapHelper()
    val wrapperHelpers = allParentDeclarations
        .filter { it.findComponentAnnotation().any() }
        .flatMap { parentComponentCl ->
            parentComponentCl.findComponentAnnotation().firstOrNull()?.wrapperHelpers ?: emptyList()
        }

    wrapperHelpers.forEachIndexed { _, wrapperHelperCl ->
        val wrapperHelperClDec = wrapperHelperCl.declaration as? KSClassDeclaration ?: return@forEachIndexed
        val methods = wrapperHelperClDec.getAllMethods(false, false, "<init>")
        methods.forEachFun { funIdx, m ->
            val inputType = (m.parameters.firstOrNull()
                ?.type?.resolveNotNullable()
                ?.declaration as? KSClassDeclaration)
                ?.toClassName() ?: return@forEachFun

            val outputType = (m.returnType?.resolveNotNullable()
                ?.declaration as? KSClassDeclaration)
                ?.toClassName() ?: return@forEachFun

            wrapHelper.support(
                WrapType(
                    isNoCachingWrapper = false,
                    typeName = outputType,
                    wrap = { or, srcNullable, targetNullable, argTypeNullable ->
                        codeBlock {
                            add(
                                "%T.%L{ %L!! }",
                                wrapperHelperClDec.toClassName(),
                                m.simpleName.asString(),
                                or,
                            )
                        }
                    },
                    unwrap = { or, srcNullable, targetNullable ->
                        codeBlock {

                        }
                    }
                )
            )
        }
    }

    return wrapHelper
}


fun KSClassDeclaration.collectComponentGraph(

): ModulesGraph {
    val modulesGraph = ModulesGraph(
        wrapHelper = collectWrapHelper(),
        identifierTypes = allIdentifierTypes.toList(),
    )
    modulesGraph.collectFromComponent(this)
    return modulesGraph
}