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
import com.github.klee0kai.thekey.stone.ksp.ksp.cacheProtected
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.github.klee0kai.thekey.stone.ksp.ksp.resolveNotNullable
import com.github.klee0kai.thekey.stone.ksp.poet.*
import com.github.klee0kai.thekey.stone.ksp.target.component.collectWrapHelper
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

        val wrapHelper = componentCl?.collectWrapHelper() ?: WrapHelper()

        val identifierTypes = componentCl
            ?.allIdentifierTypes?.toList()
            ?: emptyList()

        val genModuleClassName = moduleCl.moduleStoneClName

        val fileSpec = genFileSpec(genModuleClassName.packageName, genModuleClassName.simpleName) {
            genLibComment()

            addImport("com.github.klee0kai.stone.__hidden__.coroutines", "syncIfAvailable")

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
                                    cacheType = bindAnn.cacheProtected.toItemCacheType(),
                                    wrapHelper = wrapHelper,
                                )
                                gcScopes += bindAnn.cacheProtected.toItemCacheType().gcScopeClassName
                                codeBlocks.switchRefStatementBuilders.getOrPut(gcScopes) { CodeBlock.builder() }
                                    .add(itemHolderCodeHelper.statementSwitchRef(CodeBlock.of("__params")))

                                codeBlocks.clearNullsMethodBody.add(itemHolderCodeHelper.clearNullsStatement())

                                with(itemHolderCodeHelper) {
                                    genCacheField()

                                    if (!isListReturnType) {
                                        codeBlocks.bindMethodBody.controlFlow(
                                            "if (or::class == %T::class) {",
                                            rawTypeOf(nonWrappedType)
                                        ) {
                                            add(codeSetCachedValue(CodeBlock.of("or as? %T", nonWrappedType), false))
                                            addStatement("")
                                            addStatement("%L = true\n", appliedLocalFieldName)
                                        }
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

                            provideAnn == null || provideAnn.cacheProtected == Provide.CacheType.Factory -> {
                                genOverrideFun(function) {
                                    addStatement(
                                        "return %L.%L(%L)", factoryFieldName,
                                        function.simpleName.asString(),
                                        function.parameters.joinToString(", ") { it.name!!.asString() })
                                }
                                genFun(function.cacheControlMethodName) {
                                    modifiers.add(KModifier.OVERRIDE)
                                    val cacheControlType = function.returnType?.resolveNotNullable()
                                        ?.toTypeName()
                                        ?.let { wrapHelper.listWrapTypeIfNeed(it).copy(nullable = true) }
                                    cacheControlType?.let { returns(cacheControlType) }
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
                                    cacheType = provideAnn.cacheProtected.toItemCacheType() ?: return@forEachFun,
                                    wrapHelper = wrapHelper,
                                )
                                gcScopes += provideAnn.cacheProtected.toItemCacheType()!!.gcScopeClassName
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
        val setValueArg = function.parameters.firstOrNull {
            it.type.resolveNotNullable().toTypeName() == function.returnType?.resolveNotNullable()?.toTypeName()
        }

        genOverrideFun(function) {
            beginControlFlow("return syncIfAvailable(%L.mutex)", overridedModuleFieldName)
            addStatement(
                "val cached = %L.get()?.%L( %T.getValueAction, %L ) ",
                overridedModuleFieldName,
                function.cacheControlMethodName,
                CacheAction::class.asClassName(),
                idArguments.joinToString(", ") { it.name!!.asString() },
            )
            addCode("if ( cached != null ) return@syncIfAvailable ")
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

            addCode("return@syncIfAvailable ")
            addCode(
                wrapHelper.transform(
                    wrapHelper.listWrapTypeIfNeed(returnType).copy(nullable = true),
                    returnType,
                    itemHolderCodeHelper.codeGetCachedValue(),
                )
            )
            endControlFlow()
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
            beginControlFlow("return syncIfAvailable(%L.mutex)", overridedModuleFieldName)
            addStatement(
                "val cached = %L.get()?.%L( %T.getValueAction, %L ) ",
                overridedModuleFieldName,
                function.cacheControlMethodName,
                CacheAction::class.asClassName(),
                idArguments.joinToString(", ") { it.name!!.asString() },
            )
            addCode("if (cached != null ) return@syncIfAvailable ")
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
            addCode("return@syncIfAvailable ")
            addCode(
                wrapHelper.transform(
                    wrapHelper.listWrapTypeIfNeed(returnType).copy(nullable = true),
                    returnType,
                    itemHolderCodeHelper.codeGetCachedValue(),
                )
            )
            endControlFlow()
        }
    }

    private fun TypeSpec.Builder.genCacheControlFun(
        function: KSFunctionDeclaration,
        idArguments: List<KSValueParameter>,
        itemHolderCodeHelper: ItemHolderCodeHelper,
        wrapHelper: WrapHelper,
    ) {
        val returnType = function.returnType?.resolveNotNullable()?.toTypeName() ?: return
        val cacheControlType = wrapHelper.listWrapTypeIfNeed(returnType).copy(nullable = true)
        genFun(function.cacheControlMethodName) {
            modifiers.add(KModifier.OVERRIDE)
            returns(cacheControlType.copy(nullable = true))
            addParameter("__action", CacheAction::class)
            idArguments.forEach {
                addParameter(it.name!!.asString(), it.type.resolve().toTypeName())
            }

            beginControlFlow("return syncIfAvailable(%L.mutex)", overridedModuleFieldName)
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

            addCode("return@syncIfAvailable ")
            addCode(codeBlock = itemHolderCodeHelper.codeGetCachedValue())
            endControlFlow()
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