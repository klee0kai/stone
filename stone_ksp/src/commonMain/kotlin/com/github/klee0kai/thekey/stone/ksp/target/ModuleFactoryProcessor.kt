@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.target

import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.thekey.stone.ksp.ksp.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.poet.*
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
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

class ModuleFactoryProcessor : TargetFileProcessor {

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
        val classDeclaration = validSymbol as? KSClassDeclaration ?: return null

        val moduleAnn = classDeclaration.getAnnotationsByType(Module::class)
            .firstOrNull() ?: return null

        val genClassName = ClassName(
            fileOwner.packageName.asString().stonePackageName,
            "I${classDeclaration.simpleName.getShortName()}"
        )

        val fileSpec = genFileSpec(genClassName.packageName, genClassName.simpleName) {
            genLibComment()

            genClass(genClassName) {
                validSymbol.getAllFunctions().forEach { function ->
                    if (!function.isAbstract) return@genClass
                    val returnType = function.returnType?.resolve()?.toClassName() ?: return@genClass
                    val bindInstanceAnn = function.getAnnotationsByType(BindInstance::class)
                        .firstOrNull()

                    genFun(function.simpleName.asString()) {
                        declareSameParameters(function)
                        returns(returnType)
                        if (function.isSuspend) addModifiers(KModifier.SUSPEND)

                        if (bindInstanceAnn != null) {
                            addStatement("return null")
                        } else {

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