package com.github.klee0kai.thekey.stone.ksp.target.component

import com.github.klee0kai.stone.__hidden__.IModule
import com.github.klee0kai.stone.__hidden__.IPrivateComponent
import com.github.klee0kai.stone.__hidden__.types.WeakList
import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.dependencies.Dependencies
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.thekey.stone.ksp.exceptions.IncorrectSignatureException
import com.github.klee0kai.thekey.stone.ksp.helpers.*
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.anyAnnotation
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.ModulesGraph
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.github.klee0kai.thekey.stone.ksp.ksp.isChildOf
import com.github.klee0kai.thekey.stone.ksp.poet.*
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.google.devtools.ksp.processing.Dependencies as KspDependencies

class GenComponentProcessor : TargetFileProcessor {

    companion object {
        val refCollectionGlFieldName = "__refCollection"
        val scheduleGlFieldName = "__scheduler"
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
            scheduleGlFieldName,
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
        val wrapperTypes = componentCl.wrapperProviders.toList()
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

                componentsAllMethods.forEach { m ->
                    when {
                        m.isModuleProvideMethod -> {
                            val moduleCl = m.returnType?.resolve()?.declaration as? KSClassDeclaration
                                ?: throw IncorrectSignatureException(
                                    message = "wrong return type. Must by Module type",
                                    element = m
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
                                    it.returnType?.resolve()?.toClassName() == m.returnType?.resolve()?.toClassName()
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
                                            addStatement("%L( %L )", initMethodName, param.name!!.asString());
                                        }

                                        paramType.anyAnnotation(
                                            Dependencies::class.asClassName(),
                                        ).any() -> {
                                            addStatement("%L( %L )", initDepsMethodName, param.name!!.asString());
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

                        }

                        m.isObjectProvideMethod -> {

                        }

                        m.isBindInstanceMethod != null -> {

                        }

                        m.isGcMethod -> {

                        }

                        m.isSwitchCacheMethod -> {

                        }

                        m.isInjectMethod -> {
                            genInjectMethod(
                                componentCl = componentCl,
                                method = m,
                            )
                        }

                        m.isProtectInjectedMethod -> {

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


    private fun TypeSpec.Builder.genInjectMethod(
        componentCl: KSClassDeclaration,
        method: KSFunctionDeclaration,
        modulesGraph: ModulesGraph,
    ) {
        val identifierTypes = componentCl.allIdentifierTypes.toList()
        val idArguments = method.parameters.filter { it.type.resolve() in identifierTypes }
        val lifeCycleOwnerArg = method.parameters.firstOrNull {
            (it.type.resolve().declaration as? KSClassDeclaration)
                ?.isChildOf(StoneLifeCycleOwner::class.asClassName()) == true
        }
        val injectableArguments = method.parameters.filter { it.type.resolve() !in identifierTypes }
        if (originatingElements.isEmpty()) {
            throw IncorrectSignatureException(
                message = "No injectable parameter at ${method.simpleName.asString()}",
                element = method,
            )
        }

        genOverrideFun(method) {
            for (injectableArgument in injectableArguments) {
                val injectableCl = injectableArgument.type.resolve().declaration as? KSClassDeclaration
                    ?: throw IncorrectSignatureException(
                        message = "parameter must be a class",
                        element = injectableArgument,
                    )

                for (injectField in injectableCl.getAllProperties()) {
                    val provideCode = modulesGraph.codeProvideType(
                        methodName = null,
                        returnType = injectField.type.resolve().toClassName(),
                        qualifierAnns = injectField.qualifierAnnotations.toList(),
                    )
                    // TODO
                }
            }

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

            for (proto in componentCl.getAllSuperTypes().mapNotNull { it.declaration as? KSClassDeclaration }) {
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