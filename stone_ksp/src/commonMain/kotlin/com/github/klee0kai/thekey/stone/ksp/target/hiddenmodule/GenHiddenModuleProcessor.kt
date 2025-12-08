package com.github.klee0kai.thekey.stone.ksp.target.hiddenmodule

import com.github.klee0kai.stone.__hidden__.IModule
import com.github.klee0kai.stone.__hidden__.SwitchCacheParam
import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.thekey.stone.ksp.helpers.allIdentifierTypes
import com.github.klee0kai.thekey.stone.ksp.helpers.hiddenModuleStoneClName
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.poet.*
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor.Companion.appliedLocalFieldName
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor.Companion.bindMethodName
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor.Companion.clearNullsMethodName
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor.Companion.factoryFieldName
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor.Companion.initCachesFromMethodName
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor.Companion.initMethodName
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor.Companion.switchRefMethodName
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor.Companion.updateBindInstancesFrom
import com.github.klee0kai.thekey.stone.ksp.target.component.collectComponentGraph
import com.github.klee0kai.thekey.stone.ksp.target.component.collectWrapHelper
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlin.reflect.KClass

class GenHiddenModuleProcessor : TargetFileProcessor {


    private class DelayedCodeBlocks(
        val bindMethodBody: CodeBlock.Builder = CodeBlock.builder(),
        val clearNullsMethodBody: CodeBlock.Builder = CodeBlock.builder(),
        val switchRefStatementBuilders: MutableMap<Set<TypeName>, CodeBlock.Builder> = mutableMapOf()
    )

    override suspend fun findSymbolsToProcess(
        resolver: Resolver,
    ) = SymbolsToProcess(
        symbolsForProcessing = resolver
            .getSymbolsWithAnnotation(Component::class.asClassName().canonicalName)
            .toList(),
        symbolsForReprocessing = emptyList(),
    )


    override suspend fun process(
        validSymbol: KSAnnotated,
        resolver: Resolver,
        options: Map<String, String>,
        logger: KSPLogger
    ): GenSpec? {
        val fileOwner = validSymbol.containingFile ?: return null
        val componentCl = validSymbol as? KSClassDeclaration ?: return null

        val genHiddenModuleCl = componentCl.hiddenModuleStoneClName
        val identifierTypes = componentCl.allIdentifierTypes.toList()
        val wrapHelper = componentCl.collectWrapHelper()
        val modulesGraph = componentCl.collectComponentGraph()
        val delayedCodeBlocks = DelayedCodeBlocks()

        val fileSpec = genFileSpec(genHiddenModuleCl.packageName, genHiddenModuleCl.simpleName) {
            genLibComment()

            genClass(genHiddenModuleCl) {
                addSuperinterface(IModule::class)
                genIModelMethods(
                    codeBlocks = delayedCodeBlocks,
                )
            }

        }

        return GenSpec(
            fileSpec = fileSpec,
            // https://kotlinlang.org/docs/ksp-incremental.html
            dependencies = Dependencies(aggregating = false, fileOwner),
        )
    }

    private fun TypeSpec.Builder.genIModelMethods(
        codeBlocks: DelayedCodeBlocks,
    ) {
        genProperty(
            name = factoryFieldName,
            type = ANY.copy(nullable = true),
        ) {
            addModifiers(KModifier.OVERRIDE)
            initializer("null")
        }

        genFun(initMethodName) {
            addModifiers(KModifier.OVERRIDE)
            returns(BOOLEAN)
            addParameter("or", Any::class)
            addStatement("if (or === this) return false")
            addStatement("var %L = false", appliedLocalFieldName)

            addStatement("return %L", appliedLocalFieldName)
        }

        genFun(initCachesFromMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("m", IModule::class)
            addStatement("if (m == this) return")
        }

        genFun(bindMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("or", Any::class)
            returns(BOOLEAN)
            addStatement("var %L = false", appliedLocalFieldName)
            addCode(codeBlocks.bindMethodBody.build())
            addStatement("return %L", appliedLocalFieldName)
        }

        genFun(switchRefMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addParameter(
                "scopes",
                Set::class.asClassName()
                    .parameterizedBy(
                        KClass::class.asClassName()
                            .parameterizedBy(STAR)
                    )
            )
            addParameter("__params", SwitchCacheParam::class)

            codeBlocks.switchRefStatementBuilders.forEach { (key, value) ->
                addCode("if (listOf(")
                key.forEachIndexed { idx, scope ->
                    if (idx > 0) addCode(", ")
                    addCode("%T::class", scope)
                }
                beginControlFlow(").containsAll(scopes))")
                addCode(value.build())
                endControlFlow()
            }
        }

        genFun(updateBindInstancesFrom) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("m", IModule::class)
            addStatement("if (m == this) return")
        }

        genFun(clearNullsMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addCode(codeBlocks.clearNullsMethodBody.build())
        }

    }


}
