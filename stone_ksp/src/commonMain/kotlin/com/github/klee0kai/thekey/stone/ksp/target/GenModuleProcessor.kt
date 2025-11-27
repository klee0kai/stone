@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.target

import com.github.klee0kai.stone.__hidden__.CacheAction
import com.github.klee0kai.stone.__hidden__.IModule
import com.github.klee0kai.stone.__hidden__.SwitchCacheParam
import com.github.klee0kai.stone.__hidden__.types.holders.SingleItemHolder
import com.github.klee0kai.stone.__hidden__.types.holders.StoneRefType
import com.github.klee0kai.stone.annotations.component.GcAllScope
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.thekey.stone.ksp.helpers.*
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.annotations
import com.github.klee0kai.thekey.stone.ksp.helpers.itemholder.ItemHolderHelper
import com.github.klee0kai.thekey.stone.ksp.helpers.itemholder.of
import com.github.klee0kai.thekey.stone.ksp.helpers.itemholder.toItemCacheType
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.ClassNameUtils.rawTypeOf
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
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import kotlin.reflect.KClass

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
        val initMethodBody: SmartCode = SmartCode(),
        val initCachesFromMethodBody: SmartCode = SmartCode(),
        val bindMethodBody: SmartCode = SmartCode(),
        val getFactoryMethodBody: SmartCode = SmartCode(),
        val switchRefMethodBody: SmartCode = SmartCode(),
        val updateBindInstancesFromBody: SmartCode = SmartCode(),
        val clearNullsMethodBody: SmartCode = SmartCode(),
        val switchRefStatementBuilders: MutableMap<Set<TypeName>, CodeBlock.Builder> = mutableMapOf()
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
                        val gcScopes = (function.scopeAnnotations
                            .map { it.annotationType.resolve().toClassName() }
                            .toSet() + GcAllScope::class.asClassName()).toMutableSet()


                        when {
                            bindAnn != null -> {
                                val itemHolderHelper = ItemHolderHelper.of(
                                    fieldName = "${function.simpleName.asString()}$funIdx",
                                    returnType = returnType,
                                    idArguments = idArguments,
                                    cacheType = bindAnn.cache.toItemCacheType(),
                                )
                                gcScopes += bindAnn.cache.toItemCacheType().gcScopeClassName
                                codeBlocks.switchRefStatementBuilders.getOrPut(gcScopes) { CodeBlock.builder() }
                                    .add(itemHolderHelper.statementSwitchRef(CodeBlock.of("__params")))

                                codeBlocks.clearNullsMethodBody.add(itemHolderHelper.clearNullsStatement())

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
                                gcScopes += provideAnn.cache.toItemCacheType()!!.gcScopeClassName
                                codeBlocks.switchRefStatementBuilders.getOrPut(gcScopes) { CodeBlock.builder() }
                                    .add(itemHolderHelper.statementSwitchRef(CodeBlock.of("__params")))
                                codeBlocks.clearNullsMethodBody.add(itemHolderHelper.clearNullsStatement())
                                with(itemHolderHelper) {
                                    genCacheField()
                                }
                                genProvideCachedFun(
                                    function = function,
                                    idArguments = idArguments,
                                    itemHolderHelper = itemHolderHelper,
                                    wrapperHelper = wrapperHelper,
                                )
                                genCacheControlFun(
                                    function = function,
                                    idArguments = idArguments,
                                    itemHolderHelper = itemHolderHelper,
                                )
                            }
                        }

                    }


                genIModelMethods(
                    moduleCl = moduleCl,
                    identifierTypes = identifierTypes,
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

    private fun TypeSpec.Builder.genProvideCachedFun(
        function: KSFunctionDeclaration,
        idArguments: List<KSValueParameter>,
        itemHolderHelper: ItemHolderHelper,
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

    private fun TypeSpec.Builder.genCacheControlFun(
        function: KSFunctionDeclaration,
        idArguments: List<KSValueParameter>,
        itemHolderHelper: ItemHolderHelper,
    ) {
        val returnType = function.returnType?.resolve()?.toClassName() ?: return
        genFun(function.cacheControlMethodName) {
            modifiers.add(KModifier.OVERRIDE)
            returns(returnType.copy(nullable = true))
            addParameter("__action", CacheAction::class)
            idArguments.forEach {
                addParameter(it.name!!.asString(), it.type.resolve().toClassName())
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
            addCode("(__action.value as? %T)?.let { ", rawTypeOf(returnType))
            addCode(codeBlock = itemHolderHelper.codeSetCachedValue(CodeBlock.of("it"), onlyIfNull = false))
            addCode("}")
            endControlFlow()
            //set if null value
            beginControlFlow("%T.SET_IF_NULL ->", CacheAction.ActionType::class)
            addCode("(__action.value as? %T)?.let { ", rawTypeOf(returnType))
            addCode(codeBlock = itemHolderHelper.codeSetCachedValue(CodeBlock.of("it"), onlyIfNull = true))
            addCode("}")
            endControlFlow()
            // switch cache type
            beginControlFlow("%T.SWITCH_CACHE ->", CacheAction.ActionType::class)
            addCode(codeBlock = itemHolderHelper.statementSwitchRef(CodeBlock.of("__action.swCacheParams!!")))
            endControlFlow()

            addStatement("null -> Unit")
            endControlFlow()

            addCode("return ")
            addCode(codeBlock = itemHolderHelper.codeGetCachedValue().collect())
        }
    }

    private fun TypeSpec.Builder.genIModelMethods(
        moduleCl: KSClassDeclaration,
        identifierTypes: List<KSType>,
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
            addModifiers(KModifier.OVERRIDE)
            returns(BOOLEAN)
            addParameter("or", Any::class)
            //TODO
        }

        genFun(initCachesFromMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("m", IModule::class)
            addStatement("if (m == this) return")
            (sequenceOf(moduleCl) + moduleCl.getAllSuperTypes().map { it.declaration })
                .filter { it.annotations(Module::class.asClassName()).any() }
                .mapNotNull { it as? KSClassDeclaration }
                .forEach { cl ->
                    val cacheControlCl = cl.cacheControlStoneClName
                    beginControlFlow("if ( m is %T )", cacheControlCl)
                    addStatement("val module = m as %T", cacheControlCl)

                    cl.getAllMethods(
                        includeObjectMethods = false,
                        allowDoubles = false,
                        exceptNames = arrayOf("<init>"),
                    ).forEach { protoProvideMethod ->
                        val cacheControlMethod = protoProvideMethod.cacheControlMethodName
                        val idArguments = protoProvideMethod.parameters
                            .filter { it.type.resolve() in identifierTypes }
                        if (!idArguments.isEmpty()) {
                            // TODO https://github.com/klee0kai/stone/issues/42
                            return@forEach
                        }

                        addStatement(
                            "%L( %T.setIfNullValueAction( module.%L( %T.getValueAction ) ) )",
                            cacheControlMethod, CacheAction::class,
                            protoProvideMethod, CacheAction::class,
                        );
                    }
                    endControlFlow()
                }
        }

        genFun(bindMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("or", Any::class)
            returns(BOOLEAN)

            addStatement("var %L = false", appliedLocalFieldName)
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
            addCode(codeBlocks.clearNullsMethodBody.collect())
        }

    }


}