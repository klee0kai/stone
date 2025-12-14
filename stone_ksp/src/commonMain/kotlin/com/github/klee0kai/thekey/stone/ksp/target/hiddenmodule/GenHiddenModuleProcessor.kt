@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.target.hiddenmodule

import com.github.klee0kai.stone.__hidden__.CacheAction
import com.github.klee0kai.stone.__hidden__.IModule
import com.github.klee0kai.stone.__hidden__.SwitchCacheParam
import com.github.klee0kai.stone.__hidden__.types.holders.SingleItemHolder
import com.github.klee0kai.stone.__hidden__.types.holders.StoneRefType
import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.GcAllScope
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.thekey.stone.ksp.exceptions.forEachFun
import com.github.klee0kai.thekey.stone.ksp.helpers.*
import com.github.klee0kai.thekey.stone.ksp.helpers.itemholder.ItemHolderCodeHelper
import com.github.klee0kai.thekey.stone.ksp.helpers.itemholder.of
import com.github.klee0kai.thekey.stone.ksp.helpers.itemholder.toItemCacheType
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.ClassNameUtils.rawTypeOf
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapHelper
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.github.klee0kai.thekey.stone.ksp.poet.*
import com.github.klee0kai.thekey.stone.ksp.target.module.GenModuleProcessor.Companion.appliedLocalFieldName
import com.github.klee0kai.thekey.stone.ksp.target.module.GenModuleProcessor.Companion.bindMethodName
import com.github.klee0kai.thekey.stone.ksp.target.module.GenModuleProcessor.Companion.clearNullsMethodName
import com.github.klee0kai.thekey.stone.ksp.target.module.GenModuleProcessor.Companion.factoryFieldName
import com.github.klee0kai.thekey.stone.ksp.target.module.GenModuleProcessor.Companion.initCachesFromMethodName
import com.github.klee0kai.thekey.stone.ksp.target.module.GenModuleProcessor.Companion.initMethodName
import com.github.klee0kai.thekey.stone.ksp.target.module.GenModuleProcessor.Companion.overridedModuleFieldName
import com.github.klee0kai.thekey.stone.ksp.target.module.GenModuleProcessor.Companion.switchRefMethodName
import com.github.klee0kai.thekey.stone.ksp.target.module.GenModuleProcessor.Companion.updateBindInstancesFrom
import com.github.klee0kai.thekey.stone.ksp.target.component.BindInstanceType
import com.github.klee0kai.thekey.stone.ksp.target.component.collectComponentGraph
import com.github.klee0kai.thekey.stone.ksp.target.component.collectWrapHelper
import com.github.klee0kai.thekey.stone.ksp.target.component.isBindInstanceMethod
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toTypeName
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
        val codeBlocks = DelayedCodeBlocks()

        val genCacheControlClassName = genHiddenModuleCl.cacheControlStoneClName
        val fileSpec = genFileSpec(genHiddenModuleCl.packageName, genHiddenModuleCl.simpleName) {
            genLibComment()

            genClass(genHiddenModuleCl) {
                addSuperinterface(IModule::class)
                addSuperinterface(genCacheControlClassName)

                val functions = validSymbol.getAllMethods(false, false, "<init>")
                functions.forEachFun { funIdx, function ->

                    val bindAnn = function.getAnnotationsByType(BindInstance::class).firstOrNull()
                    val idArguments = function.parameters.identifierParameters(identifierTypes)

                    val returnType = function.returnType?.resolve()?.toTypeName() ?: return@forEachFun
                    val nonWrappedType = wrapHelper.nonWrappedType(returnType)
                    val gcScopes = (function.scopeAnnotations
                        .map { it.annotationType.resolve().toTypeName() }
                        .toSet() + GcAllScope::class.asClassName()).toMutableSet()

                    if (bindAnn == null || function.isBindInstanceMethod != BindInstanceType.BindInstanceAndProvide)
                        return@forEachFun

                    val itemHolderCodeHelper = ItemHolderCodeHelper.of(
                        fieldName = "${function.simpleName.asString()}$funIdx",
                        returnType = returnType,
                        idArguments = idArguments,
                        cacheType = bindAnn.cache.toItemCacheType(),
                        wrapHelper = wrapHelper,
                    )
                    gcScopes += bindAnn.cache.toItemCacheType().gcScopeClassName
                    codeBlocks.switchRefStatementBuilders.getOrPut(gcScopes) { CodeBlock.builder() }
                        .add(itemHolderCodeHelper.statementSwitchRef(CodeBlock.of("__params")))

                    codeBlocks.clearNullsMethodBody.add(itemHolderCodeHelper.clearNullsStatement())


                    with(itemHolderCodeHelper) {
                        genCacheField()

                        codeBlocks.bindMethodBody.apply {
                            add(
                                "if (or::class == %T::class) {\n",
                                rawTypeOf(nonWrappedType),
                            )
                            add(codeSetCachedValue(CodeBlock.of("or as? %T", nonWrappedType), false))
                            add("\n")
                            add("%L = true\n", appliedLocalFieldName)
                            add("}\n")
                        }
                    }

                    genBindInstance(
                        function = function,
                        idArguments = idArguments,
                        itemHolderCodeHelper = itemHolderCodeHelper,
                        wrapHelper = wrapHelper,
                    )
                    genCacheControlFun(
                        function = function,
                        idArguments = idArguments,
                        itemHolderCodeHelper = itemHolderCodeHelper,
                        wrapHelper = wrapHelper,
                    )
                }

                genIModelMethods(
                    componentCl = componentCl,
                    codeBlocks = codeBlocks,
                )
            }

        }

        return GenSpec(
            fileSpec = fileSpec,
            // https://kotlinlang.org/docs/ksp-incremental.html
            dependencies = Dependencies(aggregating = false, fileOwner),
        )
    }

    private fun TypeSpec.Builder.genBindInstance(
        function: KSFunctionDeclaration,
        idArguments: List<KSValueParameter>,
        itemHolderCodeHelper: ItemHolderCodeHelper,
        wrapHelper: WrapHelper,
    ) {
        val returnType = function.returnType?.resolve()?.toTypeName() ?: return
        val setValueArg = function.parameters.firstOrNull { it.type.resolve().toTypeName() == returnType }

        genOverrideFun(function) {
            addStatement(
                "val cached = %L.get()?.%L( %T.getValueAction, %L ) ",
                overridedModuleFieldName,
                function.cacheControlMethodName,
                CacheAction::class.asClassName(),
                idArguments.joinToString(", ") { it.name!!.asString() },
            )
            addCode("if ( cached != null ) return ")
            addCode(
                wrapHelper.transform(
                    providingType = wrapHelper.listWrapTypeIfNeed(returnType),
                    wannaType = returnType,
                    code = codeBlock { add("cached") },
                )
            )
            addStatement("")

            if (setValueArg != null) {
                beginControlFlow("if (%L != null)", setValueArg.name!!.asString())
                addCode(
                    itemHolderCodeHelper.codeSetCachedValue(
                        value = CodeBlock.of("%L", setValueArg.name!!.asString()),
                        onlyIfNull = false
                    )
                )
                endControlFlow();
            }


            addCode("return ")
            addCode(
                wrapHelper.transform(
                    wrapHelper.listWrapTypeIfNeed(returnType),
                    returnType,
                    itemHolderCodeHelper.codeGetCachedValue(),
                )
            )
            addStatement(" as %T", returnType)
        }
    }


    private fun TypeSpec.Builder.genCacheControlFun(
        function: KSFunctionDeclaration,
        idArguments: List<KSValueParameter>,
        itemHolderCodeHelper: ItemHolderCodeHelper,
        wrapHelper: WrapHelper,
    ) {
        val returnType = function.returnType?.resolve()?.toTypeName() ?: return
        val cacheControlType = wrapHelper.listWrapTypeIfNeed(returnType)
        genFun(function.cacheControlMethodName) {
            modifiers.add(KModifier.OVERRIDE)
            returns(returnType.copy(nullable = true))
            addParameter("__action", CacheAction::class)
            idArguments.forEach {
                addParameter(it.name!!.asString(), it.type.resolve().toTypeName())
            }

            addStatement(
                "%L.get()?.%L( __action, %L ) ",
                overridedModuleFieldName,
                function.cacheControlMethodName,
                idArguments.joinToString(", ") { it.name!!.asString() },
            )
            beginControlFlow("when (__action.type) {")
            addStatement("%T.GET_VALUE -> Unit", CacheAction.ActionType::class)
            //set value
            beginControlFlow("%T.SET_VALUE ->", CacheAction.ActionType::class)
            addCode("if ( __action.value != null ) ")
            addCode(
                codeBlock = itemHolderCodeHelper.codeSetCachedValue(
                    CodeBlock.of("__action.value as? %T", cacheControlType),
                    onlyIfNull = false,
                )
            )
            endControlFlow()
            //set if null value
            beginControlFlow("%T.SET_IF_NULL ->", CacheAction.ActionType::class)
            addCode("if ( __action.value != null ) ")
            addCode(
                codeBlock = itemHolderCodeHelper.codeSetCachedValue(
                    CodeBlock.of("__action.value as? %T", cacheControlType),
                    onlyIfNull = true,
                )
            )
            endControlFlow()
            // switch cache type
            beginControlFlow("%T.SWITCH_CACHE ->", CacheAction.ActionType::class)
            addCode(codeBlock = itemHolderCodeHelper.statementSwitchRef(CodeBlock.of("__action.swCacheParams!!")))
            endControlFlow()

            addStatement("null -> Unit")
            endControlFlow()

            addCode("return ")
            addCode(codeBlock = itemHolderCodeHelper.codeGetCachedValue())
        }
    }

    private fun TypeSpec.Builder.genIModelMethods(
        componentCl: KSClassDeclaration,

        codeBlocks: DelayedCodeBlocks,
    ) {
        genProperty(
            name = factoryFieldName,
            type = ANY.copy(nullable = true),
        ) {
            addModifiers(KModifier.OVERRIDE)
            initializer("null")
        }

        val cacheControlHolder = SingleItemHolder::class.asClassName()
            .parameterizedBy(componentCl.hiddenModuleStoneClName.cacheControlStoneClName)
        genProperty(
            name = overridedModuleFieldName,
            type = cacheControlHolder,
        ) {
            mutable(true)
            initializer("%T(%T.WeakObject)", cacheControlHolder, StoneRefType::class)
        }

        genFun(initMethodName) {
            addModifiers(KModifier.OVERRIDE)
            returns(BOOLEAN)
            addParameter("or", Any::class)
            addStatement("if (or === this) return false")
            addStatement("var %L = false", appliedLocalFieldName)

            addStatement("return %L", appliedLocalFieldName)
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

        genFun(initCachesFromMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("m", IModule::class)
            addStatement("if (m == this) return")

            val cacheControlCl = componentCl.hiddenModuleStoneClName.cacheControlStoneClName
            beginControlFlow("if ( m is %T )", cacheControlCl)
            addStatement("val module = m as %T", cacheControlCl)
            componentCl.getAllMethods(
                includeObjectMethods = false,
                allowDoubles = false,
                exceptNames = arrayOf("<init>"),
            ).forEach { protoProvideMethod ->
                if (protoProvideMethod.isBindInstanceMethod != BindInstanceType.BindInstanceAndProvide) {
                    return@forEach
                }

                val cacheControlMethod = protoProvideMethod.cacheControlMethodName

                addStatement(
                    "%L( %T.setIfNullValueAction( module.%L( %T.getValueAction ) ) )",
                    cacheControlMethod, CacheAction::class,
                    cacheControlMethod, CacheAction::class,
                )
            }
            endControlFlow()
        }

        genFun(updateBindInstancesFrom) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("m", IModule::class)
            addStatement("if (m == this) return")

            val cacheControlCl = componentCl.hiddenModuleStoneClName.cacheControlStoneClName
            beginControlFlow("if ( m is %T )", cacheControlCl)
            addStatement("val module = m as %T", cacheControlCl)
            componentCl.getAllMethods(
                includeObjectMethods = false,
                allowDoubles = false,
                exceptNames = arrayOf("<init>"),
            ).forEach { protoProvideMethod ->
                if (protoProvideMethod.isBindInstanceMethod != BindInstanceType.BindInstanceAndProvide) {
                    return@forEach
                }

                val cacheControlMethod = protoProvideMethod.cacheControlMethodName

                addStatement(
                    "%L( %T.setValueAction( module.%L( %T.getValueAction ) ) )",
                    cacheControlMethod, CacheAction::class,
                    cacheControlMethod, CacheAction::class,
                );
            }
            endControlFlow()
        }

        genFun(clearNullsMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addCode(codeBlocks.clearNullsMethodBody.build())
        }

    }


}
