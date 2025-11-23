@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.target

import com.github.klee0kai.stone.__hidden__.IModuleFactory
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.thekey.stone.ksp.helpers.factoryStoneClName
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.ksp.findConstructor
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.github.klee0kai.thekey.stone.ksp.ksp.joinInvokeArguments
import com.github.klee0kai.thekey.stone.ksp.poet.genClass
import com.github.klee0kai.thekey.stone.ksp.poet.genFileSpec
import com.github.klee0kai.thekey.stone.ksp.poet.genLibComment
import com.github.klee0kai.thekey.stone.ksp.poet.genOverrideFun
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

class GenModuleFactoryProcessor : TargetFileProcessor {

    override suspend fun findSymbolsToProcess(
        resolver: Resolver,
    ) = SymbolsToProcess(
        symbolsForProcessing = resolver
            .getSymbolsWithAnnotation(Module::class.asClassName().canonicalName)
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
        val moduleCl = validSymbol as? KSClassDeclaration ?: return null

        val moduleAnn = moduleCl.getAnnotationsByType(Module::class)
            .firstOrNull() ?: return null

        val genFactoryClassName = moduleCl.factoryStoneClName

        val fileSpec = genFileSpec(genFactoryClassName.packageName, fileName = genFactoryClassName.simpleName) {
            genLibComment()

            genClass(genFactoryClassName) {
                if (moduleCl.classKind == ClassKind.INTERFACE) {
                    addSuperinterface(moduleCl.toClassName())
                } else {
                    superclass(moduleCl.toClassName())
                }
                addSuperinterface(IModuleFactory::class)
                addModifiers(KModifier.OPEN)

                validSymbol.getAllMethods(false, false, "<init>")
                    .forEach { function ->
                        if (!function.modifiers.contains(Modifier.ABSTRACT) && moduleCl.classKind != ClassKind.INTERFACE) return@forEach
                        val returnCl = function.returnType?.resolve()
                            ?.declaration as? KSClassDeclaration ?: return@forEach
                        val bindInstanceAnn = function.getAnnotationsByType(BindInstance::class)
                            .firstOrNull()

                        val constructorFun by lazy {
                            returnCl.findConstructor(
                                parameters = function.parameters.map { it.type.resolve() })
                        }

                        genOverrideFun(function) {
                            when {
                                bindInstanceAnn != null -> {
                                    addStatement("return null")
                                }

                                constructorFun != null -> {
                                    addStatement(
                                        "return %T( %L )",
                                        returnCl.toClassName(),
                                        constructorFun!!.joinInvokeArguments(function.parameters),
                                    )
                                }

                                else -> {
                                    addStatement(
                                        "return super.%L( %L )",
                                        function.simpleName.asString(),
                                        function.parameters.joinToString(", ") { it.name?.asString() ?: "it" },
                                    )
                                }
                            }
                        }
                    }
            }
        }

        return GenSpec(
            fileSpec = fileSpec,
            // https://kotlinlang.org/docs/ksp-incremental.html
            dependencies = Dependencies(aggregating = false, fileOwner),
        )
    }
}