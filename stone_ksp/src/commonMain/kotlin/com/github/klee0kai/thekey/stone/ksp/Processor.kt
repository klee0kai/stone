package com.github.klee0kai.thekey.stone.ksp

import com.github.klee0kai.thekey.stone.ksp.coroutines.LaunchConductor
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.TargetFileProcessor
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.forceProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.takeOnly
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleFactoryProcessor
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ksp.writeTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min


class Processor(
    private val options: Map<String, String>,
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {

    companion object {
        const val PROJECT_URL = "https://github.com/klee0kai/stone"

        val ONE_RUN_SYMBOLS_COUNT = max(Runtime.getRuntime().availableProcessors(), 4)
    }

    private val multithread = options["multithread"]?.toBoolean() ?: false

    val dispatcher by lazy { if (multithread) Dispatchers.Default else Dispatchers.Unconfined }
    val targetProcessors = arrayOf<TargetFileProcessor>(
        GenModuleFactoryProcessor(),
        GenModuleProcessor(),
    )

    override fun process(
        resolver: Resolver
    ): List<KSAnnotated> = runBlocking(dispatcher) {

        val processSymbolsCounter = AtomicInteger(0)
        val findSymbolsMutex = Mutex()
        val globalSymbolsForProcessing = ConcurrentLinkedQueue<KSAnnotated>()
        val globalSymbolsForReprocessing = ConcurrentLinkedQueue<KSAnnotated>()
        val genSpecs = ConcurrentLinkedQueue<GenSpec>()

        val launchConductor = LaunchConductor()

        val generateCodeJob = launch {
            targetProcessors.forEach { processor ->
                launch {
                    var symbols = launchConductor.finishTogether {
                        var symbols = findSymbolsMutex.withLock { processor.findSymbolsToProcess(resolver) }

                        if (multithread) {
                            // skip to next ksp run

                            var takeSymbolsCount = 0
                            processSymbolsCounter.updateAndGet { totalCount ->
                                takeSymbolsCount = min(
                                    symbols.symbolsForProcessing.size,
                                    ONE_RUN_SYMBOLS_COUNT - totalCount
                                )

                                takeSymbolsCount = max(takeSymbolsCount, 0)
                                totalCount + takeSymbolsCount
                            }
                            symbols = symbols.takeOnly(takeSymbolsCount)
                        }

                        globalSymbolsForProcessing.addAll(symbols.symbolsForProcessing)
                        symbols
                    }

                    symbols = symbols.forceProcess { it in globalSymbolsForProcessing }
                    globalSymbolsForReprocessing.addAll(symbols.symbolsForReprocessing)

                    genSpecs.addAll(
                        symbols.symbolsForProcessing
                            .mapNotNull { targetSymbol ->
                                processor.process(
                                    validSymbol = targetSymbol,
                                    resolver = resolver,
                                    options = options,
                                    logger = logger,
                                )
                            }
                    )
                }
            }
        }

        // join generate code
        // we provide separate file recording
        // with symbol resolution so that the processor can link the input and output of generation
        generateCodeJob.join()

        genSpecs.forEach { genSpec ->
            genSpec?.fileSpec?.writeTo(
                codeGenerator = codeGenerator,
                dependencies = genSpec.dependencies
            )
        }

        globalSymbolsForReprocessing.toList()
    }

    override fun finish() {
        super.finish()
    }

    override fun onError() {
        super.onError()
    }


}