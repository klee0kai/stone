@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.target.provider

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.thekey.stone.ksp.exceptions.forEachFun
import com.github.klee0kai.thekey.stone.ksp.helpers.allIdentifierTypes
import com.github.klee0kai.thekey.stone.ksp.helpers.findComponentForModuleOrDep
import com.github.klee0kai.thekey.stone.ksp.helpers.identifierParameters
import com.github.klee0kai.thekey.stone.ksp.helpers.qualifierAnnotations
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.ksp.fileText
import com.github.klee0kai.thekey.stone.ksp.ksp.genProviderNameProtected
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.github.klee0kai.thekey.stone.ksp.ksp.provideWrapperProtected
import com.github.klee0kai.thekey.stone.ksp.poet.*
import com.github.klee0kai.thekey.stone.ksp.psi.InMemoryPsiParser
import com.github.klee0kai.thekey.stone.ksp.psi.isSamePlace
import com.github.klee0kai.thekey.stone.ksp.target.isSuspend
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction

class GenProviderProcessor : TargetFileProcessor {

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

        val componentCl = resolver.findComponentForModuleOrDep(moduleCl.toClassName())
            .firstOrNull()

        val identifierTypes = componentCl
            ?.allIdentifierTypes?.toList()
            ?: emptyList()

        if (moduleAnn.genProviderNameProtected.isNullOrBlank()) return null


        val genProvideClName = ClassName(
            moduleCl.packageName.asString(),
            moduleAnn.genProviderName,
        )

        val filePsi = InMemoryPsiParser(moduleCl.location.fileText())

        val fileSpec = genFileSpec(moduleCl.packageName.asString(), genProvideClName.simpleName) {
            genLibComment()

            genInterface(genProvideClName) {
                validSymbol.getAllMethods(false, false, "<init>")
                    .forEachFun { funIdx, func ->
                        val idArguments = func.parameters.identifierParameters(identifierTypes)
                        val returnType = func.returnType?.resolve()?.toTypeName() ?: return@forEachFun


                        val provideWrapper = func.getAnnotationsByType(Provide::class)
                            .firstOrNull()
                            ?.provideWrapperProtected
                            ?.takeIf { it !in listOf(Nothing::class, Void::class) }

                        val providingType = provideWrapper?.asClassName()?.parameterizedBy(returnType)
                            ?: returnType

                        genFun(func.simpleName.asString()) {
                            if (func.isSuspend) addModifiers(KModifier.SUSPEND)
                            val psiFunc by lazy {
                                filePsi.ktFile.declarations
                                    .filterIsInstance<KtClass>()
                                    .firstOrNull()
                                    ?.declarations
                                    ?.filterIsInstance<KtNamedFunction>()
                                    ?.firstOrNull { it.isSamePlace(func) }
                            }
                            addModifiers(KModifier.ABSTRACT)
                            returns(providingType)
                            func.qualifierAnnotations.forEach {
                                addAnnotation(it)
                            }

                            idArguments.forEach { param ->
                                addParameter(
                                    ParameterSpec.builder(
                                        name = param.name?.asString() ?: "",
                                        type = param.type.resolve().toTypeName(),
                                    ).apply {
                                        if (param.isVararg) addModifiers(KModifier.VARARG)

                                        if (param.hasDefault && psiFunc != null) {
                                            val psiParam = psiFunc
                                                ?.valueParameters
                                                ?.firstOrNull { it.name == param.name?.asString() }

                                            if (!psiParam?.defaultValue?.text.isNullOrBlank()) {
                                                defaultValue("%L", psiParam.defaultValue?.text)
                                            }
                                        }
                                    }.build()
                                )
                            }
                        }
                    }


            }
        }


        filePsi.close()

        return GenSpec(
            fileSpec = fileSpec,
            // https://kotlinlang.org/docs/ksp-incremental.html
            dependencies = Dependencies(aggregating = false, fileOwner),
        )
    }

}