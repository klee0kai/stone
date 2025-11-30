package com.github.klee0kai.thekey.stone.ksp.target.hiddenmodule

import com.github.klee0kai.stone.__hidden__.IModule
import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.thekey.stone.ksp.helpers.hiddenModuleStoneClName
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

class GenHiddenModuleProcessor : TargetFileProcessor {
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

        val fileSpec = genFileSpec(genHiddenModuleCl.packageName, genHiddenModuleCl.simpleName) {
            genLibComment()

            genClass(genHiddenModuleCl) {
                addSuperinterface(IModule::class)

            }

        }

        return GenSpec(
            fileSpec = fileSpec,
            // https://kotlinlang.org/docs/ksp-incremental.html
            dependencies = Dependencies(aggregating = false, fileOwner),
        )
    }
}
