@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.target.module

import com.github.klee0kai.stone.__hidden__.IModuleFactory
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.thekey.stone.ksp.exceptions.forEachFun
import com.github.klee0kai.thekey.stone.ksp.helpers.factoryStoneClName
import com.github.klee0kai.thekey.stone.ksp.helpers.findComponentForModuleOrDep
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapHelper
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.rawType
import com.github.klee0kai.thekey.stone.ksp.ksp.*
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.poet.genClass
import com.github.klee0kai.thekey.stone.ksp.poet.genFileSpec
import com.github.klee0kai.thekey.stone.ksp.poet.genLibComment
import com.github.klee0kai.thekey.stone.ksp.poet.genOverrideFun
import com.github.klee0kai.thekey.stone.ksp.psi.InMemoryPsiParser
import com.github.klee0kai.thekey.stone.ksp.psi.isSamePlace
import com.github.klee0kai.thekey.stone.ksp.target.component.collectWrapHelper
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction

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

        val componentCl = resolver.findComponentForModuleOrDep(moduleCl.toClassName())
            .firstOrNull()

        val wrapHelper = componentCl?.collectWrapHelper() ?: WrapHelper()
        val filePsi = InMemoryPsiParser(moduleCl.location.fileText())

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
                    .forEachFun { _, function ->
                        if (!function.modifiers.contains(Modifier.ABSTRACT) && moduleCl.classKind != ClassKind.INTERFACE) return@forEachFun
                        val returnType = function.returnType?.toTypeName() ?: return@forEachFun
                        val psiFunc by lazy {
                            filePsi.ktFile.declarations
                                .filterIsInstance<KtClass>()
                                .firstOrNull()
                                ?.declarations
                                ?.filterIsInstance<KtNamedFunction>()
                                ?.firstOrNull { it.isSamePlace(function) }
                        }

                        val nonWrappedType = wrapHelper.nonWrappedType(returnType)
                        val nonWrappedClDec = resolver.getClassDeclarationByName(nonWrappedType.rawType().toString())

                        val bindInstanceAnn = function.getAnnotationsByType(BindInstance::class)
                            .firstOrNull()

                        val constructorFun by lazy {
                            nonWrappedClDec?.findConstructor(
                                parameters = function.parameters.map { it.type.resolveNotNullable() })
                        }

                        genOverrideFun(function) {
                            when {
                                bindInstanceAnn != null -> {
                                    addStatement(
                                        "throw %T(%S)",
                                        NotImplementedError::class.asClassName(),
                                        "Object generation is not available for bind instance methods"
                                    )
                                }

                                psiFunc?.hasBody() == true -> {
                                    addStatement(
                                        "return super.%L( %L )",
                                        function.simpleName.asString(),
                                        function.parameters.joinToString(", ") { it.name?.asString() ?: "it" },
                                    )
                                }

                                constructorFun != null -> {
                                    addCode(
                                        "return %L",
                                        wrapHelper.transform(
                                            providingType = nonWrappedType,
                                            wannaType = returnType,
                                            CodeBlock.of(
                                                "%T( %L )",
                                                nonWrappedType,
                                                constructorFun!!.joinInvokeArguments(function.parameters),
                                            )
                                        )
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