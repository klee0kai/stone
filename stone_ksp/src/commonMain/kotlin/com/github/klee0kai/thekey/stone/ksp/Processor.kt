package com.github.klee0kai.thekey.stone.ksp

import com.github.klee0kai.thekey.stone.ksp.coroutines.LaunchConductor
import com.github.klee0kai.thekey.stone.ksp.exceptions.StoneException
import com.github.klee0kai.thekey.stone.ksp.exceptions.wrapKsNoteInfo
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.GenSpec
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.filter
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.forceProcess
import com.github.klee0kai.thekey.stone.ksp.ksp.arch.nowTakeOnly
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleCacheControlProcessor
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleFactoryProcessor
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor
import com.github.klee0kai.thekey.stone.ksp.target.component.GenComponentProcessor
import com.github.klee0kai.thekey.stone.ksp.target.hiddenmodule.GenHiddenModuleCacheControlProcessor
import com.github.klee0kai.thekey.stone.ksp.target.hiddenmodule.GenHiddenModuleProcessor
import com.google.devtools.ksp.containingFile
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

        var oneRunSymbolsCount = 4
            private set

        var multithread = false
            private set

        var debug = false
            private set

        var debugPkgFilter: String? = null
            private set
    }

    init {
        oneRunSymbolsCount = options["oneRunSymbolsCount"]?.toInt()
            ?: max(Runtime.getRuntime().availableProcessors(), 4)

        multithread = options["multithread"]?.toBoolean() ?: false
        debug = options["debug"]?.toBoolean() ?: false
        debugPkgFilter = options["debugPkgFilter"]

        // force changes
//        debug = true
//        debugPkgFilter = "com.github.klee0kai.test.di.base_phone"
    }


    val dispatcher by lazy { if (multithread) Dispatchers.Default else Dispatchers.Unconfined }
    val targetProcessors = arrayOf(
        GenModuleFactoryProcessor(),
        GenModuleCacheControlProcessor(),
        GenModuleProcessor(),
        GenHiddenModuleProcessor(),
        GenHiddenModuleCacheControlProcessor(),
        GenComponentProcessor(),
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

                        if (debug && debugPkgFilter != null) {
                            symbols = symbols
                                .filter {
                                    it.containingFile?.packageName
                                        ?.asString()?.startsWith(debugPkgFilter!!) ?: true
                                }
                        }

                        var takeSymbolsCount = 0
                        processSymbolsCounter.updateAndGet { totalCount ->
                            takeSymbolsCount = min(
                                symbols.symbolsForProcessing.size,
                                oneRunSymbolsCount - totalCount
                            )

                            takeSymbolsCount = max(takeSymbolsCount, 0)
                            totalCount + takeSymbolsCount
                        }


                        // skip to next run
                        symbols = symbols.nowTakeOnly(takeSymbolsCount)

                        globalSymbolsForProcessing.addAll(symbols.symbolsForProcessing)

                        symbols
                    }

                    symbols = symbols.forceProcess { it in globalSymbolsForProcessing }


                    globalSymbolsForReprocessing.addAll(symbols.symbolsForReprocessing)

                    try {
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
                    } catch (e: StoneException) {
                        logger.error(e.toString(), e.findLastErrorElement())
                    }

                }
            }
        }

        // join generate code
        // we provide separate file recording
        // with symbol resolution so that the processor can link the input and output of generation
        generateCodeJob.join()

        try {
            genSpecs.forEach { genSpec ->
                wrapKsNoteInfo(genSpec.dependencies.originatingFiles.firstOrNull()) {
                    genSpec?.fileSpec?.writeTo(
                        codeGenerator = codeGenerator,
                        dependencies = genSpec.dependencies
                    )
                }
            }
        } catch (e: StoneException) {
            logger.error(e.toString(), e.findLastErrorElement())
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