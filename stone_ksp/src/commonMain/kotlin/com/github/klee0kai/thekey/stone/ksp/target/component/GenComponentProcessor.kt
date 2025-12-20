@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.target.component

import com.github.klee0kai.stone.__hidden__.*
import com.github.klee0kai.stone.__hidden__.collections.RefCollection
import com.github.klee0kai.stone.__hidden__.types.WeakList
import com.github.klee0kai.stone.__hidden__.types.holders.TimeHolder
import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.ProtectInjected
import com.github.klee0kai.stone.annotations.component.SwitchCache
import com.github.klee0kai.stone.annotations.dependencies.Dependencies
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.weakref.Inject
import com.github.klee0kai.stone.weakref.Memory
import com.github.klee0kai.thekey.stone.ksp.exceptions.IncorrectSignatureException
import com.github.klee0kai.thekey.stone.ksp.exceptions.ObjectNotProvidedException
import com.github.klee0kai.thekey.stone.ksp.exceptions.forEachFun
import com.github.klee0kai.thekey.stone.ksp.helpers.*
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.anyAnnotation
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.ModulesGraph
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model.toFieldDetail
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model.toQualifierAnn
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapHelper
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.github.klee0kai.thekey.stone.ksp.ksp.resolveAlias
import com.github.klee0kai.thekey.stone.ksp.poet.*
import com.github.klee0kai.thekey.stone.ksp.target.module.GenModuleProcessor
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass
import com.google.devtools.ksp.processing.Dependencies as KspDependencies

class GenComponentProcessor : TargetFileProcessor {

    companion object {
        val refCollectionGlFieldName = "__refCollection"
        val scopeFieldName = "__scope"
        val hiddenModuleFieldName = "__hiddenModule"
        val relatedComponentsListFieldName = "__related"
        val protectRecursiveField = "__protectRecursive"
        val eachModuleMethodName = "__eachModule"
        val initMethodName = "__init"
        val initDepsMethodName = "__initDependencies"
        val bindMethodName = "__bind"
        val extOfMethodName = "__extOf"

        val allReserveMethodNames = listOf<String>(
            refCollectionGlFieldName,
            scopeFieldName,
            hiddenModuleFieldName,
            relatedComponentsListFieldName,
            protectRecursiveField,
            eachModuleMethodName,
            initMethodName,
            initDepsMethodName,
            bindMethodName,
            extOfMethodName,
        )
    }

    private class DelayedCodeBlocks(
        val initDepsMethodBody: CodeBlock.Builder = CodeBlock.builder(),
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

        val identifierTypes = componentCl.allIdentifierTypes.toList()
        val wrapHelper = componentCl.collectWrapHelper()
        val modulesGraph = componentCl.collectComponentGraph()
        val delayedCodeBlocks = DelayedCodeBlocks()

        val genComponentClassName = componentCl.componentStoneClName

        val fileSpec = genFileSpec(genComponentClassName.packageName, genComponentClassName.simpleName) {
            genLibComment()

            genClass(genComponentClassName) {
                if (componentCl.classKind == ClassKind.INTERFACE) {
                    addSuperinterface(componentCl.toClassName())
                } else {
                    superclass(componentCl.toClassName())
                }
                addSuperinterface(IPrivateComponent::class.asClassName())

                val componentsAllMethods = componentCl
                    .getAllMethods(includeObjectMethods = false, allowDoubles = false, "<init>")

                componentsAllMethods.forEachFun { _, m ->
                    when {
                        m.isModuleProvideMethod -> {
                            val moduleCl = m.returnType?.resolve()?.declaration as? KSClassDeclaration
                                ?: throw IncorrectSignatureException(
                                    message = "wrong return type. Must by Module type",
                                    element = m,
                                )
                            genProperty(m.simpleName.asString(), moduleCl.moduleStoneClName) {
                                addModifiers(KModifier.PRIVATE)
                                initializer("%T()", moduleCl.moduleStoneClName)
                            }
                            genOverrideFun(m) {
                                returns(moduleCl.moduleStoneClName)
                                addStatement("return %L", m.simpleName.asString())
                            }
                        }

                        m.isModuleFactoryProvideMethod -> {
                            val providingModuleFun = componentsAllMethods
                                .filter { it.isModuleProvideMethod }
                                .firstOrNull {
                                    it.returnType?.resolve()?.toTypeName() == m.returnType?.resolve()?.toTypeName()
                                }
                                ?: throw IncorrectSignatureException(
                                    message = "Component must also have providing module simple method with same type",
                                    element = m.returnType,
                                )
                            genOverrideFun(m) {
                                addStatement(
                                    "return %L.%L",
                                    providingModuleFun.simpleName.asString(),
                                    GenModuleProcessor.factoryFieldName
                                )
                            }
                        }

                        m.isDepsProvideMethod -> {
                            //TODO
                        }

                        m.isModuleInitMethod -> {
                            genOverrideFun(m) {
                                m.parameters.forEach { param ->
                                    val paramType = param.type.resolve()
                                        .declaration as? KSClassDeclaration
                                        ?: throw IncorrectSignatureException(
                                            message = "wrong return type. Must by Module type",
                                            element = param,
                                        )
                                    when {
                                        paramType.anyAnnotation(
                                            Module::class.asClassName(),
                                            Component::class.asClassName()
                                        ).any() -> {
                                            addStatement(
                                                "%L?.let{ %L( %L ) }",
                                                param.name!!.asString(),
                                                initMethodName,
                                                param.name!!.asString()
                                            )
                                        }

                                        paramType.anyAnnotation(
                                            Dependencies::class.asClassName(),
                                        ).any() -> {
                                            addStatement(
                                                "%L?.let{ %L( %L ) }",
                                                param.name!!.asString(),
                                                initDepsMethodName,
                                                param.name!!.asString()
                                            )
                                        }

                                        else -> {
                                            throw IncorrectSignatureException(
                                                message = "wrong return type. Must by Module type",
                                                element = param,
                                            )
                                        }
                                    }

                                }
                            }
                        }

                        m.isExtOfMethod(componentCl) -> {
                            genOverrideFun(m) {
                                addCode(
                                    "(%L as? %T)?.let{ %L(it) }",
                                    parameters.first().name,
                                    IPrivateComponent::class.asClassName(),
                                    extOfMethodName
                                )
                            }
                        }

                        m.isObjectProvideMethod -> {
                            genProvideObjMethod(
                                componentCl = componentCl,
                                method = m,
                                modulesGraph = modulesGraph,
                            )
                        }

                        m.isBindInstanceMethod != null -> {
                            genBindInstanceMethod(
                                componentCl = componentCl,
                                method = m,
                                modulesGraph = modulesGraph,
                                wrapHelper = wrapHelper,
                            )
                        }

                        m.isGcMethod -> {
                            genGcMthod(
                                componentCl = componentCl,
                                method = m,
                                wrapHelper = wrapHelper,
                            )
                        }

                        m.isSwitchCacheMethod -> {
                            genSwitchRefMethod(
                                componentCl = componentCl,
                                method = m,
                            )
                        }

                        m.isInjectMethod -> {
                            genInjectMethod(
                                componentCl = componentCl,
                                method = m,
                                wrapHelper = wrapHelper,
                                modulesGraph = modulesGraph,
                            )
                        }

                        m.isProtectInjectedMethod -> {
                            genProtectInjected(
                                componentCl = componentCl,
                                method = m,
                                wrapHelper = wrapHelper,
                            )
                        }

                        m.isAbstract -> {
                            throw IncorrectSignatureException(
                                message = "What is purpose for Method ${m.simpleName.asString()}. " +
                                        "Declared in ${componentCl.simpleName.asString()} ",
                                element = m,
                            )
                        }
                    }
                }

                genIComponentMethods(
                    componentCl = componentCl,
                    delayedCodeBlocks = delayedCodeBlocks,

                    )
            }
        }

        return GenSpec(
            fileSpec = fileSpec,
            // https://kotlinlang.org/docs/ksp-incremental.html
            dependencies = KspDependencies(aggregating = false, fileOwner),
        )
    }

    private fun TypeSpec.Builder.genProvideObjMethod(
        componentCl: KSClassDeclaration,
        method: KSFunctionDeclaration,
        modulesGraph: ModulesGraph,
    ) {
        val returnType = method.returnType?.resolve()?.toTypeName() ?: return

        val codeBlock = modulesGraph.codeProvideType(
            methodName = null,
            returnType = returnType,
            qualifierAnns = method.qualifierAnnotations.map { it.toQualifierAnn() }.toSet(),
            declaredFields = method.parameters.map { it.toFieldDetail() },
        )

        if (codeBlock == null) {
            throw ObjectNotProvidedException(
                message = "Error provide type ${returnType}. " +
                        "Required in ${componentCl.toClassName()}.${method.simpleName.asString()}",
                element = method,
            )
        }

        genOverrideFun(method) {
            addStatement("return %L", codeBlock)
        }

    }


    private fun TypeSpec.Builder.genBindInstanceMethod(
        componentCl: KSClassDeclaration,
        method: KSFunctionDeclaration,
        modulesGraph: ModulesGraph,
        wrapHelper: WrapHelper,
    ) {
        val returnType = method.returnType?.resolve()?.toTypeName() ?: return
        val identifierTypes = componentCl.allIdentifierTypes.toList()

        val setValueArg = method.parameters.firstOrNull { it.type.resolveAlias() !in identifierTypes }
            ?: throw IncorrectSignatureException(
                message = "Bind instance method must have bind instance arcgument",
                element = method,
            )

        val nonWrappedBindType = wrapHelper.nonWrappedType(setValueArg.type.resolve().toTypeName())
        val isProvideMethod = wrapHelper
            .nonWrappedType(method.returnType!!.resolve().toTypeName()) == nonWrappedBindType
        val hidingProvideName = if (isProvideMethod) method.simpleName.asString() else null

        // bind object declared in module
        val cacheControlInvoke = modulesGraph.invokeControlCacheForType(
            hidingProvideName,
            nonWrappedBindType,
            method.qualifierAnnotations.map { it.toQualifierAnn() }.toSet()
        )

        val isListCache = wrapHelper.isList(cacheControlInvoke!!.rawReturnType())
        val cacheControlType = if (isListCache) {
            List::class.asClassName().parameterizedBy(nonWrappedBindType).copy(nullable = true)
        } else {
            nonWrappedBindType.copy(nullable = true)
        }

        // bind object declared in module
        genOverrideFun(method) {
            addCode(
                cacheControlInvoke.invokeCode(
                    envFields = method.parameters.map { it.toFieldDetail() },
                    argGen = { _ ->
                        codeBlock {
                            add("%T.setValueAction(", CacheAction::class)
                            add(
                                wrapHelper.transform(
                                    setValueArg.type.resolve().toTypeName(),
                                    cacheControlType,
                                    CodeBlock.of(setValueArg.name!!.asString())
                                )
                            )
                            add(")")
                        }
                    }
                )
            )
            addStatement("")


            addStatement(
                "%L{ module -> module.%L( %L ); } ",
                eachModuleMethodName,
                GenModuleProcessor.updateBindInstancesFrom,
                cacheControlInvoke.bestSequence().first().methodName,
            )
            addStatement("")

            if (isProvideMethod) {
                addCode("return ")
                addCode(
                    modulesGraph.codeProvideType(
                        hidingProvideName,
                        returnType,
                        method.qualifierAnnotations.map { it.toQualifierAnn() }.toSet(),
                        method.parameters.map { it.toFieldDetail() },
                    )!!
                )
                addCode("\n")
            }
        }

    }


    private fun TypeSpec.Builder.genInjectMethod(
        componentCl: KSClassDeclaration,
        method: KSFunctionDeclaration,
        wrapHelper: WrapHelper,
        modulesGraph: ModulesGraph,
    ) {
        val identifierTypes = componentCl.allIdentifierTypes.toList()
        val lifeCycleOwnerArg = method.parameters.lifeCycleParameter()
        val injectableArguments = method.parameters.notIdentifierParameters(identifierTypes)
        if (injectableArguments.isEmpty()) {
            throw IncorrectSignatureException(
                message = "No injectable parameter at ${method.simpleName.asString()}",
                element = method,
            )
        }

        genOverrideFun(method) {
            for (injectableField in injectableArguments) {
                val injectableCl = injectableField.type.resolve().declaration as? KSClassDeclaration
                    ?: throw IncorrectSignatureException(
                        message = "parameter must be a class",
                        element = injectableField,
                    )


                for (injectField in injectableCl.getAllProperties()) {
                    if (!injectField.anyAnnotation(Inject::class.asClassName()).any()) continue

                    val provideCode = modulesGraph.codeProvideType(
                        methodName = null,
                        returnType = injectField.type.resolve().toTypeName(),
                        qualifierAnns = injectField.qualifierAnnotations.map { it.toQualifierAnn() }.toSet(),
                        declaredFields = method.parameters.map { it.toFieldDetail() },
                    )

                    if (provideCode == null) {
                        wrapHelper.nonWrappedType(injectField.type.resolve().toTypeName())
                        throw ObjectNotProvidedException(
                            message = "Error provide type ${injectField.type.resolve().toTypeName()}. " +
                                    "Required in ${injectableCl.toClassName()}.${injectField.simpleName.asString()}",
                            element = method,
                        )
                    }

                    addCode(
                        "%L?.%L = ",
                        injectableField.name!!.asString(),
                        injectField.simpleName.asString(),
                    )
                    addCode(provideCode)
                    addStatement("")
                }

                for (injectMethod in injectableCl.getAllMethods(false, false, "<init>")) {
                    if (!injectMethod.anyAnnotation(Inject::class.asClassName()).any()) continue
                    val providingArgsCode = CodeBlock.builder()
                    for (injectField in injectMethod.parameters) {
                        val provideCode = modulesGraph.codeProvideType(
                            null,
                            injectField.type.resolve().toTypeName(),
                            injectField.qualifierAnnotations.map { it.toQualifierAnn() }.toSet(),
                            method.parameters.map { it.toFieldDetail() },
                        )

                        if (provideCode == null) {
                            throw ObjectNotProvidedException(
                                message = "Error provide type ${injectField.type.resolve().toTypeName()}. " +
                                        "Required in ${injectableCl.toClassName()}.${injectMethod.simpleName.asString()}",
                                element = method,
                            )
                        }

                        if (!providingArgsCode.isEmpty()) providingArgsCode.add(", ")
                        providingArgsCode.add(provideCode)
                    }

                    addCode("%L?.%L( ", injectableField.name!!.asString(), injectMethod.simpleName.asString())
                    addCode(providingArgsCode.build())
                    addStatement(")");
                }

            }


            //protect by lifecycle owner
            for (injectableField in injectableArguments) {
                val injectableCl = injectableField.type.resolve().declaration as? KSClassDeclaration
                    ?: throw IncorrectSignatureException(
                        message = "parameter must be a class",
                        element = injectableField,
                    )


                val subscrCode = CodeBlock.builder()
                var emptyCode = true
                if (lifeCycleOwnerArg != null) {
                    subscrCode.beginControlFlow(
                        "%L?.subscribe{ timeMillis -> ",
                        lifeCycleOwnerArg.name!!.asString(),
                    )
                    for (injectField in injectableCl.getAllProperties()) {
                        if (!injectField.anyAnnotation(Inject::class.asClassName()).any()) continue
                        if (wrapHelper.isNonCachingWrapper(
                                injectField.type.resolve().toClassName()
                            )
                        )  //nothing to protect
                            continue

                        emptyCode = false
                        subscrCode.addStatement(
                            "%L.add( %T( %L, %L?.%L , timeMillis) )",
                            refCollectionGlFieldName,
                            TimeHolder::class.asClassName(),
                            scopeFieldName,
                            injectableField.name!!.asString(),
                            injectField.simpleName.asString()
                        )
                    }

                    subscrCode
                        .endControlFlow()

                    if (!emptyCode) addCode(subscrCode.build())
                }
            }
        }
    }

    private fun TypeSpec.Builder.genProtectInjected(
        componentCl: KSClassDeclaration,
        method: KSFunctionDeclaration,
        wrapHelper: WrapHelper,
    ) {
        val protectTimeMillis = method.getAnnotationsByType(ProtectInjected::class)
            .firstOrNull()?.timeMillis
            ?: throw IncorrectSignatureException(
                message = "Use ProtectInjected annotation at method ${componentCl.simpleName.asString()}.${method.simpleName.asString()}",
                element = method,
            )
        val identifierTypes = componentCl.allIdentifierTypes.toList()
        val injectableArguments = method.parameters.notIdentifierParameters(identifierTypes)
        if (injectableArguments.isEmpty()) {
            throw IncorrectSignatureException(
                message = "No injectable parameter at ${method.simpleName.asString()}",
                element = method,
            )
        }


        genOverrideFun(method) {
            for (injectableField in injectableArguments) {
                val injectableCl = injectableField.type.resolve().declaration as? KSClassDeclaration
                    ?: throw IncorrectSignatureException(
                        message = "parameter must be a class",
                        element = injectableField,
                    )


                for (injectField in injectableCl.getAllProperties()) {
                    if (!injectField.anyAnnotation(Inject::class.asClassName()).any()) continue
                    if (wrapHelper.isNonCachingWrapper(injectField.type.resolve().toTypeName())) { //nothing to protect
                        continue
                    }

                    addStatement(
                        "%L.add( %T( %L, %L?.%L , %L ) )",
                        refCollectionGlFieldName,
                        TimeHolder::class.asClassName(),
                        scopeFieldName,
                        injectableField.name!!.asString(),
                        injectField.simpleName.asString(),
                        protectTimeMillis,
                    )
                }
            }
        }
    }

    private fun TypeSpec.Builder.genGcMthod(
        componentCl: KSClassDeclaration,
        method: KSFunctionDeclaration,
        wrapHelper: WrapHelper,
    ) {

        val scopesCode = codeBlock {
            method.scopeAnnotations.forEachIndexed { index, annotation ->
                if (index > 0) add(", ")
                add("%T::class", annotation.annotationType.resolve().toTypeName())
            }
        }


        genOverrideFun(method) {
            addStatement(
                "val scopes = setOf<%T>( %L )",
                KClass::class.asClassName().parameterizedBy(STAR), scopesCode
            )
            addStatement("val toWeak = %T.toWeak()", SwitchCacheParam::class)
            addStatement("val toDef = %T.toDef()", SwitchCacheParam::class)

            addStatement(
                "%L{ m -> m.%L(scopes, toWeak) } ",
                eachModuleMethodName,
                GenModuleProcessor.switchRefMethodName,
            )

            addStatement("%T.gc()", Memory::class)
            addStatement("%L.clearNulls()", relatedComponentsListFieldName)
            addStatement(

                "%L{ m ->  m.__clearNulls(); m.%L(scopes, toDef); }",
                eachModuleMethodName, GenModuleProcessor.switchRefMethodName,
            )

            addStatement("%L.clearNulls()", refCollectionGlFieldName);
        }
    }

    private fun TypeSpec.Builder.genSwitchRefMethod(
        componentCl: KSClassDeclaration,
        method: KSFunctionDeclaration,
    ) {
        val switchCacheAnn = method.getAnnotationsByType(SwitchCache::class).first()
        val scopesCode = codeBlock {
            method.scopeAnnotations.forEachIndexed { index, annotation ->
                if (index > 0) add(", ")
                add("%T::class", annotation.annotationType.resolve().toTypeName())
            }
        }

        genOverrideFun(method) {
            addStatement(
                "val scopes = setOf<%T>( %L )",
                KClass::class.asClassName().parameterizedBy(STAR), scopesCode
            )
            addStatement(
                "val switchCacheParams = %T( %T.%L , %L )",
                SwitchCacheParam::class,
                SwitchCache.CacheType::class, switchCacheAnn.cache.name,
                switchCacheAnn.timeMillis,
            )

            addStatement(
                "%L{ m ->   m.%L(scopes, switchCacheParams) } ",
                eachModuleMethodName, GenModuleProcessor.switchRefMethodName,
            )
        }
    }

    private fun TypeSpec.Builder.genIComponentMethods(
        componentCl: KSClassDeclaration,
        delayedCodeBlocks: DelayedCodeBlocks,
    ) {
        val relatedListType = WeakList::class.asClassName().parameterizedBy(IPrivateComponent::class.asClassName())
        genProperty(
            name = relatedComponentsListFieldName,
            type = relatedListType,
        ) {
            addModifiers(KModifier.PRIVATE)
            initializer("%T()", relatedListType)
        }
        genProperty(
            name = protectRecursiveField,
            type = BOOLEAN,
        ) {
            addModifiers(KModifier.PRIVATE)
            mutable(true)
            initializer("false")
        }
        genProperty(
            name = refCollectionGlFieldName,
            type = RefCollection::class.asClassName().parameterizedBy(ANY),
        ) {
            addModifiers(KModifier.PRIVATE)
            mutable(true)
            initializer("%T()", RefCollection::class.asClassName().parameterizedBy(ANY))
        }

        genProperty(
            name = scopeFieldName,
            type = CoroutineScope::class.asClassName(),
        ) {
            addModifiers(KModifier.PRIVATE)
            mutable(true)
            initializer(" %T.stoneCoroutineScope ", StoneScope::class)
        }

        genProperty(
            name = hiddenModuleFieldName,
            type = componentCl.hiddenModuleStoneClName,
        ) {
            addModifiers(KModifier.OVERRIDE)
            initializer("%T()", componentCl.hiddenModuleStoneClName)
        }

        genFun(initMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("modules", Any::class, KModifier.VARARG)

            beginControlFlow("for (m in modules)")
            beginControlFlow("if (m is %T)", IPrivateComponent::class.asClassName())
            addComment("related component")
            addStatement("%L.add(m)", relatedComponentsListFieldName)
            endControlFlow()
            beginControlFlow("else")
            addComment("init modules")
            addStatement(
                "%L{ module -> module.%L(m) } ",
                eachModuleMethodName, GenModuleProcessor.initMethodName
            )
            endControlFlow()
            endControlFlow()
        }

        genFun(initDepsMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("deps", Any::class, KModifier.VARARG)
            beginControlFlow("for (m in deps)")
            addComment("init dependencies")
            addCode(delayedCodeBlocks.initDepsMethodBody.build())
            endControlFlow()
        }

        genFun(bindMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("objects", Any::class, KModifier.VARARG)
            beginControlFlow("for (ob in objects)")
            addStatement(
                "%L{ m -> m.%L(ob) }",
                eachModuleMethodName, GenModuleProcessor.bindMethodName
            )
            endControlFlow()
        }

        genFun(extOfMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addParameter("c", IPrivateComponent::class)

            for (proto in componentCl.allParentDeclarations) {
                if (proto.toClassName() == IPrivateComponent::class.asClassName()) continue
                val provideModuleMethods = proto
                    .getAllMethods(includeObjectMethods = false, allowDoubles = false)
                    .filter { it.isModuleProvideMethod }
                    .toList()

                val bindInstanceAndProvideMethods = proto
                    .getAllMethods(includeObjectMethods = false, allowDoubles = false)
                    .filter { it.isBindInstanceMethod == BindInstanceType.BindInstanceAndProvide }
                    .toList()

                if (provideModuleMethods.isEmpty() && bindInstanceAndProvideMethods.isEmpty()) continue

                beginControlFlow("if (c is %T)", proto.toClassName())
                addStatement("val protoComponent = c as %T", proto.toClassName())

                for (provideModule in provideModuleMethods) {
                    addStatement(
                        "%L().%L( protoComponent.%L() as %T )",
                        provideModule.simpleName.asString(),
                        GenModuleProcessor.initCachesFromMethodName,
                        provideModule.simpleName.asString(), IModule::class.asClassName(),
                    )
                }

                for (bindInstMethod in bindInstanceAndProvideMethods) {
                    addStatement(
                        "%L(protoComponent.%L(null))",
                        bindInstMethod.simpleName.asString(), bindInstMethod.simpleName.asString(),
                    )
                }
                addStatement(
                    "c.%L( %L ) ",
                    initMethodName,
                    provideModuleMethods.joinToString { "${it.simpleName.asString()}()" },
                )
                addStatement("c.%L(this)", initMethodName)
                endControlFlow()
            }

            addStatement("%L.add(c)", relatedComponentsListFieldName)
        }

        genFun(eachModuleMethodName) {
            addModifiers(KModifier.OVERRIDE)
            addParameter(
                "callback", LambdaTypeName.get(
                    receiver = null,
                    parameters = listOf(ParameterSpec.builder("it", IModule::class.asClassName()).build()),
                    returnType = UNIT,
                )
            )
            addStatement("if (%L) return ", protectRecursiveField)
            addStatement("%L = true", protectRecursiveField)

            val provideModuleMethods = componentCl
                .getAllMethods(includeObjectMethods = false, allowDoubles = false)
                .filter { it.isModuleProvideMethod }
                .toList()

            for (module in provideModuleMethods) {
                addStatement("callback(%L())", module.simpleName.asString())
            }
            addStatement("callback(%L)", hiddenModuleFieldName);

            beginControlFlow("for (c in %L.toList())", relatedComponentsListFieldName)
            addStatement("c.%L(callback)", eachModuleMethodName)
            endControlFlow();

            addStatement("%L = false", protectRecursiveField);
        }


    }

}