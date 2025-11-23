@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.target

import com.github.klee0kai.stone.__hidden__.CacheAction
import com.github.klee0kai.stone.__hidden__.IModule
import com.github.klee0kai.stone.__hidden__.types.holders.SingleItemHolder
import com.github.klee0kai.stone.__hidden__.types.holders.StoneRefType
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.thekey.stone.ksp.helpers.*
import com.github.klee0kai.thekey.stone.ksp.helpers.itemholder.ItemHolderHelper
import com.github.klee0kai.thekey.stone.ksp.helpers.itemholder.of
import com.github.klee0kai.thekey.stone.ksp.helpers.itemholder.toItemCacheType
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapHelper
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.github.klee0kai.thekey.stone.ksp.poet.*
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.SmartCode
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.add
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.smartCode
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
        val initMethodCode: SmartCode = SmartCode(),
        val initCachesFromMethodName: SmartCode = SmartCode(),
        val bindMethodName: SmartCode = SmartCode(),
        val getFactoryMethodName: SmartCode = SmartCode(),
        val switchRefMethodName: SmartCode = SmartCode(),
        val updateBindInstancesFrom: SmartCode = SmartCode(),
        val clearNullsMethodName: SmartCode = SmartCode(),
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

        val wrapperHelper = WrapHelper()

        val fileSpec = genFileSpec(genModuleClassName.packageName, genModuleClassName.simpleName) {
            genLibComment()

            genClass(genModuleClassName) {
                if (moduleCl.classKind == ClassKind.INTERFACE) {
                    addSuperinterface(moduleCl.toClassName())
                } else {
                    superclass(moduleCl.toClassName())
                }
                addSuperinterface(IModule::class)
                addSuperinterface(moduleCl.cacheControlStoneClName)
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
                                genOverrideFun(function) {

                                }
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
                                }
                                genProvideCachedFun(
                                    function,
                                    idArguments,
                                    itemHolderHelper,
                                    wrapperTypes,
                                    wrapperHelper,
                                )
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
        itemHolderHelper: ItemHolderHelper,
        wrapperTypes: List<KSType>,
        wrapperHelper: WrapHelper,
    ) {
        val returnType = function.returnType?.resolve()?.toClassName() ?: return
        genOverrideFun(function) {
            addStatement(
                "val cached = %L.get()?.%L( %T.getValueAction, %L ) ",
                overridedModuleFieldName,
                function.cacheControlMethodName,
                CacheAction::class.asClassName(),
                idArguments.joinToString(", ") { it.name!!.asString() },
            )
            addCode("if (cached != null ) return ")
            addCode(
                wrapperHelper.transform(
                    code = smartCode {
                        providingType.value = wrapperHelper.listWrapTypeIfNeed(returnType)
                        add("cached")
                    },
                    wannaType = returnType,
                ).collect()
            )
            addStatement("")

            // set value if null
            val argStrList = function.parameters.joinToString(", ") { it.name!!.asString() }
            addCode("val creator = %T{ ", Ref::class.asClassName().parameterizedBy(returnType))
            addCode(
                "%L.get()?.%L(%L)",
                overridedModuleFieldName, function.simpleName.asString(), argStrList,
            )
            addCode(" ?: ")
            addCode("%L.%L(%L)", factoryFieldName, function.simpleName.asString(), argStrList)
            addCode("}\n")
            addCode(
                itemHolderHelper.codeSetCachedValue(
                    value = wrapperHelper.transform(
                        code = smartCode {
                            providingType.value = returnType
                            add("creator.get()")
                        },
                        wannaType = wrapperHelper.listWrapTypeIfNeed(returnType)
                    ).collect(),
                    onlyIfNull = true,
                )
            )
            addCode("\n")
            addCode("return ")
            addCode(
                wrapperHelper.transform(
                    code = itemHolderHelper.codeGetCachedValue(),
                    wannaType = returnType,
                ).collect()
            )
            addCode(" as %T", returnType)
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
        }

        genFun(initCachesFromMethodName) {
        }

        genFun(bindMethodName) {
        }

        genFun(switchRefMethodName) {
        }

        genFun(updateBindInstancesFrom) {
        }

        genFun(clearNullsMethodName) {
        }

    }


}