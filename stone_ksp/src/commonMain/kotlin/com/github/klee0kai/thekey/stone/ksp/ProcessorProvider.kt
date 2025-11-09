package com.github.klee0kai.thekey.stone.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class ProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Processor(
            options = environment.options,
            logger = environment.logger,
            codeGenerator = environment.codeGenerator,
        )
    }

}