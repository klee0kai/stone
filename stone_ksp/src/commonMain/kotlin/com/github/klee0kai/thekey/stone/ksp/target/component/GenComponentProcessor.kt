package com.github.klee0kai.thekey.stone.ksp.target.component

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.thekey.stone.ksp.helpers.allIdentifierTypes
import com.github.klee0kai.thekey.stone.ksp.helpers.componentStoneClName
import com.github.klee0kai.thekey.stone.ksp.helpers.wrapperProviders
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.SymbolsToProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.poet.genClass
import com.github.klee0kai.thekey.stone.ksp.poet.genFileSpec
import com.github.klee0kai.thekey.stone.ksp.poet.genLibComment
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.asClassName

class GenComponentProcessor : TargetFileProcessor {

    companion object {
        val allReserveMethodNames = listOf<String>()
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

            }
        }

        return GenSpec(
            fileSpec = fileSpec,
            // https://kotlinlang.org/docs/ksp-incremental.html
            dependencies = Dependencies(aggregating = false, fileOwner),
        )
    }
}