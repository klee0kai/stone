package com.github.klee0kai.thekey.stone.ksp.target.wrapper

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.thekey.stone.ksp.exceptions.forEachType
import com.github.klee0kai.thekey.stone.ksp.helpers.allParentDeclarations
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.findComponentAnnotation
import com.github.klee0kai.thekey.stone.ksp.helpers.wrapperStoneClName
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.poet.genFileSpec
import com.github.klee0kai.thekey.stone.ksp.poet.genLibComment
import com.github.klee0kai.thekey.stone.ksp.poet.genObject
import com.github.klee0kai.thekey.stone.ksp.poet.genProperty
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.google.devtools.ksp.processing.Dependencies as KspDependencies


class GenWrappersSupportProcessor : TargetFileProcessor {

    companion object {
        val provideWrappersGlFieldPrefixName = "__wrapperCreator"


        val allReserveMethodNames = listOf<String>(
            provideWrappersGlFieldPrefixName,
        )
    }

    override suspend fun findSymbolsToProcess(
        resolver: Resolver
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
        val wrapperProviders = componentCl.allParentDeclarations
            .filter { it.findComponentAnnotation().any() }
            .flatMap { parentComponentCl ->
                parentComponentCl.findComponentAnnotation().firstOrNull()?.wrapperProviders ?: emptyList()
            }

        val wrapperCreatorClName = componentCl.wrapperStoneClName
        val fileSpec = genFileSpec(wrapperCreatorClName.packageName, wrapperCreatorClName.simpleName) {
            genLibComment()

            genObject(wrapperCreatorClName) {
                wrapperProviders.forEachType { index, provideWrappersCl ->
                    val name = provideWrappersGlFieldPrefixName + index

                    genProperty(name, provideWrappersCl.toClassName()) {
                        initializer("%T()", provideWrappersCl.toClassName())
                    }
                }
            }
        }

        return GenSpec(
            fileSpec = fileSpec,
            // https://kotlinlang.org/docs/ksp-incremental.html
            dependencies = KspDependencies(aggregating = false, fileOwner),
        )
    }
}