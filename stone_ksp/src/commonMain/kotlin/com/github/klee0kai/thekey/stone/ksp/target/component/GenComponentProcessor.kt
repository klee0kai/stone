package com.github.klee0kai.thekey.stone.ksp.target.component

import com.github.klee0kai.stone.__hidden__.IPrivateComponent
import com.github.klee0kai.stone.__hidden__.types.WeakList
import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.thekey.stone.ksp.exceptions.IncorrectSignatureException
import com.github.klee0kai.thekey.stone.ksp.helpers.allIdentifierTypes
import com.github.klee0kai.thekey.stone.ksp.helpers.componentStoneClName
import com.github.klee0kai.thekey.stone.ksp.helpers.wrapperProviders
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.github.klee0kai.thekey.stone.ksp.poet.*
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

class GenComponentProcessor : TargetFileProcessor {

    companion object {
        val refCollectionGlFieldName = "__refCollection"
        val scheduleGlFieldName = "__scheduler"
        val hiddenModuleFieldName = "__hiddenModule"
        val relatedComponentsListFieldName = "__related"
        val protectRecursiveField = "__protectRecursive"
        val hiddenModuleMethodName = "__hidden"
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
            hiddenModuleMethodName,
            eachModuleMethodName,
            initMethodName,
            initDepsMethodName,
            bindMethodName,
            extOfMethodName,
        )
    }

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

                        }

                        m.isModuleFactoryProvideMethod -> {

                        }

                        m.isDepsProvideMethod -> {

                        }

                        m.isModuleInitMethod -> {

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

                    )
            }
        }

        return GenSpec(
            fileSpec = fileSpec,
            // https://kotlinlang.org/docs/ksp-incremental.html
            dependencies = Dependencies(aggregating = false, fileOwner),
        )
    }


    private fun TypeSpec.Builder.genIComponentMethods(
        componentCl: KSClassDeclaration,
    ) {
        val relatedListType = WeakList::class.asClassName().parameterizedBy(IPrivateComponent::class.asClassName())
        genProperty(
            name = relatedComponentsListFieldName,
            type = relatedListType,
        ) {
            initializer("%T()", relatedListType)
        }
        genProperty(
            name = protectRecursiveField,
            type = BOOLEAN,
        ) {
            initializer("false")
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

    }

}