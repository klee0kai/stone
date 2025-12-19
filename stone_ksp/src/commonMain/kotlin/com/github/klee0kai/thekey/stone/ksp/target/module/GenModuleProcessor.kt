@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.target.module

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
import com.github.klee0kai.thekey.stone.ksp.exceptions.forEachFun
import com.github.klee0kai.thekey.stone.ksp.helpers.*
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.annotations
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.anyAnnotation
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
import com.squareup.kotlinpoet.ksp.toTypeName
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

        val allReserveMethodNames = listOf(
            initMethodName,
            initCachesFromMethodName,
            updateBindInstancesFrom,
            bindMethodName,
            switchRefMethodName,
            clearNullsMethodName,
        )
    }

    private class DelayedCodeBlocks(
        val bindMethodBody: CodeBlock.Builder = CodeBlock.builder(),
        val clearNullsMethodBody: CodeBlock.Builder = CodeBlock.builder(),
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

        val wrapHelper = WrapHelper()

        val fileSpec = genFileSpec(genModuleClassName.packageName, genModuleClassName.simpleName) {
            genLibComment()

            genClass(genModuleClassName) {
                if (moduleCl.classKind == ClassKind.INTERFACE) {
                    addSuperinterface(moduleCl.toClassName())
                } else {
                    superclass(moduleCl.toClassName())
                }
                addSuperinterface(IModule::class)

                moduleCl.allParentDeclarations
                    .filter { it.getAnnotationsByType(Module::class).any() }
                    .forEach { parentModuleCl -> addSuperinterface(parentModuleCl.toClassName().cacheControlStoneClName) }

                addModifiers(KModifier.OPEN)
                val codeBlocks = DelayedCodeBlocks()

                validSymbol.getAllMethods(false, false, "<init>")
                    .forEachFun { funIdx, function ->
                        val holderIdx = funIdx + 1
                        val bindAnn = function.getAnnotationsByType(BindInstance::class).firstOrNull()
                        val provideAnn = function.getAnnotationsByType(Provide::class).firstOrNull()
                        val idArguments = function.parameters.identifierParameters(identifierTypes)

                        val returnType = function.returnType?.resolve()?.toTypeName() ?: return@forEachFun
                        val nonWrappedType = wrapHelper.nonWrappedType(returnType)
                        val isListReturnType = wrapHelper.isList(returnType)
                        val gcScopes = (function.scopeAnnotations
                            .map { it.annotationType.resolve().toTypeName() }
                            .toSet() + GcAllScope::class.asClassName()).toMutableSet()


                        when {
                            bindAnn != null -> {
                                val itemHolderCodeHelper = ItemHolderCodeHelper.of(
                                    fieldName = "${function.simpleName.asString()}$holderIdx",
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

                            provideAnn == null || provideAnn.cache == Provide.CacheType.Factory -> {
                                genOverrideFun(function) {
                                    addStatement(
                                        "return %L.%L(%L)", factoryFieldName,
                                        function.simpleName.asString(),
                                        function.parameters.joinToString(", ") { it.name!!.asString() })
                                }
                                genFun(function.cacheControlMethodName) {
                                    modifiers.add(KModifier.OVERRIDE)
                                    val returnType = function.returnType?.resolve()?.toTypeName()
                                    returnType?.let { returns(returnType.copy(nullable = true)) }
                                    addParameter("__action", CacheAction::class)
                                    idArguments.forEach {
                                        addParameter(it.name!!.asString(), it.type.resolve().toTypeName())
                                    }
                                    addStatement("return null")
                                }
                            }

                            else -> {
                                val itemHolderCodeHelper = ItemHolderCodeHelper.of(
                                    fieldName = "${function.simpleName.asString()}$holderIdx",
                                    returnType = returnType,
                                    idArguments = idArguments,
                                    cacheType = provideAnn.cache.toItemCacheType() ?: return@forEachFun,
                                    wrapHelper = wrapHelper,
                                )
                                gcScopes += provideAnn.cache.toItemCacheType()!!.gcScopeClassName
                                codeBlocks.switchRefStatementBuilders.getOrPut(gcScopes) { CodeBlock.builder() }
                                    .add(itemHolderCodeHelper.statementSwitchRef(CodeBlock.of("__params")))
                                codeBlocks.clearNullsMethodBody.add(itemHolderCodeHelper.clearNullsStatement())
                                with(itemHolderCodeHelper) {
                                    genCacheField()
                                }
                                genProvideCachedFun(
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

    private fun TypeSpec.Builder.genProvideCachedFun(
        function: KSFunctionDeclaration,
        idArguments: List<KSValueParameter>,
        itemHolderCodeHelper: ItemHolderCodeHelper,
        wrapHelper: WrapHelper,
    ) {
        val returnType = function.returnType?.resolve()?.toTypeName() ?: return
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
                wrapHelper.transform(
                    wrapHelper.listWrapTypeIfNeed(returnType),
                    returnType,
                    CodeBlock.of("cached"),
                )
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
                itemHolderCodeHelper.codeSetCachedValue(
                    wrapHelper.transform(
                        returnType,
                        wrapHelper.listWrapTypeIfNeed(returnType),
                        CodeBlock.of("creator.get()"),
                    ),
                    onlyIfNull = true,
                )
            )
            addCode("\n")
            addCode("return ")
            addCode(
                wrapHelper.transform(
                    wrapHelper.listWrapTypeIfNeed(returnType),
                    returnType,
                    itemHolderCodeHelper.codeGetCachedValue(),
                )
            )
            addCode(" as %T", returnType)
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
                    onlyIfNull = false
                )
            )
            endControlFlow()
            //set if null value
            beginControlFlow("%T.SET_IF_NULL ->", CacheAction.ActionType::class)
            addCode("if ( __action.value != null ) ")
            addCode(
                codeBlock = itemHolderCodeHelper.codeSetCachedValue(
                    CodeBlock.of("__action.value as? %T", cacheControlType),
                    onlyIfNull = true
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
            .parameterizedBy(moduleCl.toClassName().cacheControlStoneClName)
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

            // check module class
            beginControlFlow(
                "if ( (or is %T) ) ",
                moduleCl.toClassName().cacheControlStoneClName,
            )
            addStatement(
                "%L.set(onlyIfNull = false) { or }",
                overridedModuleFieldName,
            )
            addStatement(
                "%L = (or as %T).%L as %T",
                factoryFieldName,
                IModule::class.asClassName(),
                factoryFieldName,
                moduleCl.toClassName(),
            )
            addStatement("%L = true", appliedLocalFieldName)
            endControlFlow() // check factory class
            beginControlFlow("else if (or is %T) ", moduleCl.toClassName())
            addStatement(
                "%L = or as %T",
                factoryFieldName,
                moduleCl.toClassName(),

                )
            addStatement("%L = true", appliedLocalFieldName)
            endControlFlow() // get module factory by module class

            addStatement("return %L", appliedLocalFieldName)
        }



        genFun(bindMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("or", Any::class)
            returns(BOOLEAN)
            addStatement("%L.get()?.%L(or)", overridedModuleFieldName, bindMethodName)

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
            (sequenceOf(moduleCl) + moduleCl.getAllSuperTypes().map { it.declaration })
                .filter { it.annotations(Module::class.asClassName()).any() }
                .mapNotNull { it as? KSClassDeclaration }
                .forEach { cl ->
                    val cacheControlCl = cl.toClassName().cacheControlStoneClName
                    beginControlFlow("if ( m is %T )", cacheControlCl)
                    addStatement("val module = m as %T", cacheControlCl)

                    cl.getAllMethods(
                        includeObjectMethods = false,
                        allowDoubles = false,
                        exceptNames = arrayOf("<init>"),
                    ).forEach { protoProvideMethod ->
                        val cacheControlMethod = protoProvideMethod.cacheControlMethodName
                        val idArguments = protoProvideMethod.parameters.identifierParameters(identifierTypes)
                        if (idArguments.isNotEmpty()) {
                            // TODO https://github.com/klee0kai/stone/issues/42
                            return@forEach
                        }

                        addStatement(
                            "%L( %T.setIfNullValueAction( module.%L( %T.getValueAction ) ) )",
                            cacheControlMethod, CacheAction::class,
                            cacheControlMethod, CacheAction::class,
                        );
                    }
                    endControlFlow()
                }
        }

        genFun(updateBindInstancesFrom) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("m", IModule::class)
            addStatement("if (m == this) return")
            moduleCl.getAllSuperTypes()

            (sequenceOf(moduleCl) + moduleCl.getAllSuperTypes().map { it.declaration })
                .filter { it.annotations(Module::class.asClassName()).any() }
                .mapNotNull { it as? KSClassDeclaration }
                .forEach { cl ->
                    val cacheControlCl = cl.toClassName().cacheControlStoneClName
                    beginControlFlow("if ( m is %T )", cacheControlCl)
                    addStatement("val module = m as %T", cacheControlCl)

                    cl.getAllMethods(
                        includeObjectMethods = false,
                        allowDoubles = false,
                        exceptNames = arrayOf("<init>"),
                    ).forEach { protoProvideMethod ->
                        if (protoProvideMethod.anyAnnotation(BindInstance::class.asClassName()).none()) {
                            return@forEach
                        }

                        val cacheControlMethod = protoProvideMethod.cacheControlMethodName
                        val idArguments = protoProvideMethod.parameters.identifierParameters(identifierTypes)
                        if (idArguments.isNotEmpty()) {
                            // TODO https://github.com/klee0kai/stone/issues/42
                            return@forEach
                        }

                        addStatement(
                            "%L( %T.setValueAction( module.%L( %T.getValueAction ) ) )",
                            cacheControlMethod, CacheAction::class,
                            cacheControlMethod, CacheAction::class,
                        );
                    }
                    endControlFlow()
                }
        }

        genFun(clearNullsMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addCode(codeBlocks.clearNullsMethodBody.build())
        }

    }


}