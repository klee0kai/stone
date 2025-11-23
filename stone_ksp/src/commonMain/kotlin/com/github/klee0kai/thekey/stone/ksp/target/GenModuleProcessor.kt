@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.target

import com.github.klee0kai.stone.__hidden__.IModule
import com.github.klee0kai.stone.__hidden__.types.holders.SingleItemHolder
import com.github.klee0kai.stone.__hidden__.types.holders.StoneRefType
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.thekey.stone.ksp.helpers.*
import com.github.klee0kai.thekey.stone.ksp.helpers.itemholder.ItemHolderHelper
import com.github.klee0kai.thekey.stone.ksp.helpers.itemholder.of
import com.github.klee0kai.thekey.stone.ksp.helpers.itemholder.toItemCacheType
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.github.klee0kai.thekey.stone.ksp.poet.*
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.SmartCodeScopeBuilder
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

class GenModuleProcessor : TargetFileProcessor {

    companion object {
        const val overridedModuleFieldName: String = "overridedModule"
        const val factoryFieldName: String = "factory"
        const val appliedLocalFieldName: String = "applied"
        const val initMethodName: String = "__init"
        const val initCachesFromMethodName: String = "__initCachesFrom"
        const val updateBindInstancesFrom: String = "__updateBindInstancesFrom"
        const val bindMethodName: String = "__bind"
        const val switchRefMethodName: String = "__switchRef"
        const val clearNullsMethodName: String = "__clearNulls"
    }

    class DelayedCodeBlocks(
        val initMethodCode: SmartCodeScopeBuilder = SmartCodeScopeBuilder(),
        val initCachesFromMethodName: SmartCodeScopeBuilder = SmartCodeScopeBuilder(),
        val bindMethodName: SmartCodeScopeBuilder = SmartCodeScopeBuilder(),
        val getFactoryMethodName: SmartCodeScopeBuilder = SmartCodeScopeBuilder(),
        val switchRefMethodName: SmartCodeScopeBuilder = SmartCodeScopeBuilder(),
        val updateBindInstancesFrom: SmartCodeScopeBuilder = SmartCodeScopeBuilder(),
        val clearNullsMethodName: SmartCodeScopeBuilder = SmartCodeScopeBuilder(),
    )


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

        val wrapperTypes = componentCl
            ?.wrapperProviders?.toList()
            ?: emptyList()

        val genModuleClassName = moduleCl.moduleStoneClName


        val fileSpec = genFileSpec(genModuleClassName.packageName, genModuleClassName.simpleName) {
            genLibComment()

            genClass(genModuleClassName) {
                if (moduleCl.classKind == ClassKind.INTERFACE) {
                    addSuperinterface(moduleCl.toClassName())
                } else {
                    superclass(moduleCl.toClassName())
                }
                addSuperinterface(IModule::class)
                addModifiers(KModifier.OPEN)
                val codeBlocks = DelayedCodeBlocks()

                validSymbol.getAllMethods(false, false, "<init>")
                    .forEachIndexed { funIdx, function ->
                        val bindAnn = function.getAnnotationsByType(BindInstance::class).firstOrNull()
                        val provideAnn = function.getAnnotationsByType(Provide::class).firstOrNull()
                        val idArguments = function.parameters
                            .filter { it.type.resolve() in identifierTypes }

                        val returnType = function.returnType?.resolve() ?: return@forEachIndexed
                        val nonWrappedType = returnType.noWrappedType(wrapperTypes)
                        val isListReturnType = nonWrappedType.isListType()

                        when {
                            bindAnn != null -> {
                                val itemHolderHelper = ItemHolderHelper.of(
                                    fieldName = "${function.simpleName.asString()}$funIdx",
                                    returnType = returnType,
                                    idArguments = idArguments,
                                    cacheType = bindAnn.cache.toItemCacheType(),
                                )

                                with(itemHolderHelper) {
                                    genCacheField()
                                    genOverrideFun(function) {

                                    }
                                }
                            }

                            provideAnn == null || provideAnn.cache == Provide.CacheType.Factory -> {
                                genProvideCachedFun(
                                    function,
                                    idArguments,
                                    wrapperTypes,
                                )
                            }

                            else -> {
                                val itemHolderHelper = ItemHolderHelper.of(
                                    fieldName = "${function.simpleName.asString()}$funIdx",
                                    returnType = returnType,
                                    idArguments = idArguments,
                                    cacheType = provideAnn.cache.toItemCacheType() ?: return@forEachIndexed,
                                )
                                with(itemHolderHelper) {
                                    genCacheField()

                                    genOverrideFun(function) {

                                    }
                                }
                            }
                        }

                    }


                genIModelMethods(moduleCl, codeBlocks)
            }
        }

        return GenSpec(
            fileSpec = fileSpec,
            // https://kotlinlang.org/docs/ksp-incremental.html
            dependencies = Dependencies(aggregating = false, fileOwner),
        )
    }

    private fun TypeSpec.Builder.genProvideCachedFun(
        function: KSFunctionDeclaration,
        idArguments: List<KSValueParameter>,
        wrapperTypes: List<KSType>,
    ) {
        genOverrideFun(function) {
            controlFlow("if (%L.get() != null )", overridedModuleFieldName) {
                addStatement(
                    "%T cached = %L.get().%L( null %L ) ",
                    function.returnType!!.resolve().listWrapTypeIfNeed(wrapperTypes),
                    overridedModuleFieldName, function.cacheControlMethodName,
                    idArguments.joinToString { ", ${it.name}" },
                )
                add("if (cached != null ) return ")
                TODO()

            }
        }
    }

    private fun TypeSpec.Builder.genIModelMethods(
        moduleCl: KSClassDeclaration,
        codeBlocks: DelayedCodeBlocks,

        ) {
        genProperty(
            name = factoryFieldName,
            type = moduleCl.toClassName(),
        ) {
            addModifiers(KModifier.OVERRIDE)
            mutable(true)
            initializer("%T()", moduleCl.factoryStoneClName)
        }

        val cacheControlHolder = SingleItemHolder::class.asClassName()
            .parameterizedBy(moduleCl.cacheControlStoneClName)
        genProperty(
            name = overridedModuleFieldName,
            type = cacheControlHolder,
        ) {
            mutable(true)
            initializer("%T(%T.WeakObject)", cacheControlHolder, StoneRefType::class)
        }

        genFun(initMethodName) {
            codeBlocks.initMethodCode.collect(declaredVariables = emptyMap())
        }

        genFun(initCachesFromMethodName) {
            codeBlocks.initCachesFromMethodName.collect(declaredVariables = emptyMap())
        }

        genFun(bindMethodName) {
            codeBlocks.bindMethodName.collect(declaredVariables = emptyMap())
        }

        genFun(switchRefMethodName) {
            codeBlocks.switchRefMethodName.collect(declaredVariables = emptyMap())
        }

        genFun(updateBindInstancesFrom) {
            codeBlocks.updateBindInstancesFrom.collect(declaredVariables = emptyMap())
        }

        genFun(clearNullsMethodName) {
            codeBlocks.clearNullsMethodName.collect(declaredVariables = emptyMap())
        }

    }


}