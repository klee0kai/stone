package com.github.klee0kai.thekey.stone.ksp.target.component

import com.github.klee0kai.stone.wrappers.creators.CircleWrapper
import com.github.klee0kai.stone.wrappers.creators.ProviderWrapper
import com.github.klee0kai.stone.wrappers.creators.Wrapper
import com.github.klee0kai.thekey.stone.ksp.exceptions.IncorrectSignatureException
import com.github.klee0kai.thekey.stone.ksp.helpers.allIdentifierTypes
import com.github.klee0kai.thekey.stone.ksp.helpers.allParentDeclarations
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.findComponentAnnotation
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.findWrapperCreatorAnnotation
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.ModulesGraph
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.ClassNameUtils
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapHelper
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapType
import com.github.klee0kai.thekey.stone.ksp.helpers.wrapperStoneClName
import com.github.klee0kai.thekey.stone.ksp.ksp.isType
import com.github.klee0kai.thekey.stone.ksp.poet.codeBlock
import com.github.klee0kai.thekey.stone.ksp.target.wrapper.GenWrappersSupportProcessor.Companion.provideWrappersGlFieldPrefixName
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toTypeName

fun KSClassDeclaration.collectWrapHelper(
): WrapHelper {
    val wrapHelper = WrapHelper()
    val wrapperProviders = allParentDeclarations
        .filter { it.findComponentAnnotation().any() }
        .flatMap { parentComponentCl ->
            parentComponentCl.findComponentAnnotation().firstOrNull()?.wrapperProviders ?: emptyList()
        }

    val wrapperCreatorClName = wrapperStoneClName
    wrapperProviders.forEachIndexed { index, provideWrappersCl ->
        val name = provideWrappersGlFieldPrefixName + index
        val provideWrappersClDecl = provideWrappersCl.declaration as? KSClassDeclaration ?: return@forEachIndexed
        val isSimpleWrapper = provideWrappersClDecl.allParentDeclarations.any { it.isType(Wrapper::class) }
        val isAsyncWrapper = provideWrappersClDecl.allParentDeclarations.any { it.isType(ProviderWrapper::class) }
        val isCycleWrapper = provideWrappersClDecl.allParentDeclarations.any { it.isType(CircleWrapper::class) }
        val wrappers = provideWrappersClDecl.findWrapperCreatorAnnotation()
            .firstOrNull()?.wrappers ?: return@forEachIndexed

        for (wrapper in wrappers) {
            val rawTypeName = ClassNameUtils.rawTypeOf(wrapper.toTypeName())
            wrapHelper.support(
                WrapType(
                    isNoCachingWrapper = !isAsyncWrapper,
                    typeName = rawTypeName,
                    wrap = { or, nullable ->
                        codeBlock {
                            if (isSimpleWrapper) {
                                add(
                                    "%T.%L.wrap( %T::class , %L )",
                                    wrapperCreatorClName,
                                    name,
                                    rawTypeName,
                                    or,
                                )
                            } else if (isAsyncWrapper || isCycleWrapper) {
                                add(
                                    "%T.%L.wrap( %T::class , { %L } )",
                                    wrapperCreatorClName,
                                    name,
                                    rawTypeName,
                                    or,
                                )
                            } else {
                                throw IncorrectSignatureException("Type Transform non support to ${wrapper.toTypeName()}")
                            }
                        }
                    },
                    unwrap = { or, nullable ->
                        codeBlock {
                            val paramType = wrapHelper.paramType(wrapper.toTypeName())

                            if (isCycleWrapper) {
                                add(
                                    "%T.%L.unwrap( %T::class , %T::class, %L )",
                                    wrapperCreatorClName,
                                    name,
                                    rawTypeName,
                                    paramType,
                                    or
                                )
                            } else {
                                throw IncorrectSignatureException("Type Transform non support to ${wrapper.toTypeName()}")
                            }
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