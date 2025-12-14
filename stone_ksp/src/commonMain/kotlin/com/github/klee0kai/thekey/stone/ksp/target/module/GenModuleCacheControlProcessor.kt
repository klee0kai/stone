@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.target.module

import com.github.klee0kai.stone.__hidden__.CacheAction
import com.github.klee0kai.stone.__hidden__.SwitchCacheParam
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.thekey.stone.ksp.exceptions.forEachFun
import com.github.klee0kai.thekey.stone.ksp.helpers.*
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.github.klee0kai.thekey.stone.ksp.poet.*
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlin.reflect.KClass

class GenModuleCacheControlProcessor : TargetFileProcessor {

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

        val identifierTypes = componentCl
            ?.allIdentifierTypes?.toList()
            ?: emptyList()

        val genCacheControlClassName = moduleCl.toClassName().cacheControlStoneClName
        val fileSpec = genFileSpec(
            packageName = genCacheControlClassName.packageName,
            fileName = genCacheControlClassName.simpleName
        ) {
            genLibComment()

            genInterface(genCacheControlClassName) {

                genFun(GenModuleProcessor.bindMethodName) {
                    modifiers.add(KModifier.ABSTRACT)
                    addParameter("or", Any::class)
                    returns(Boolean::class)
                }

                genFun(GenModuleProcessor.switchRefMethodName) {
                    modifiers.add(KModifier.ABSTRACT)
                    addParameter(
                        "scopes",
                        Set::class.asClassName()
                            .parameterizedBy(
                                KClass::class.asClassName()
                                    .parameterizedBy(STAR)
                            )
                    )
                    addParameter("__params", SwitchCacheParam::class)
                }

                validSymbol.getAllMethods(false, false, "<init>")
                    .forEachFun { _, function ->
                        val idArguments = function.parameters.identifierParameters(identifierTypes)

                        genOverrideFun(function) {
                            modifiers.remove(KModifier.OVERRIDE)
                            modifiers.add(KModifier.ABSTRACT)
                        }
                        genFun(function.cacheControlMethodName) {
                            modifiers.add(KModifier.ABSTRACT)
                            returns(returnType = function.returnType!!.resolve().toTypeName().copy(nullable = true))
                            addParameter("__action", CacheAction::class)
                            idArguments.forEach {
                                addParameter(it.name!!.asString(), it.type.resolve().toTypeName())
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