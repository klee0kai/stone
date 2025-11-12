package com.github.klee0kai.thekey.stone.ksp.ksp.arch

import com.google.devtools.ksp.symbol.KSAnnotated

data class SymbolsToProcess(
    val symbolsForProcessing: List<KSAnnotated>,
    val symbolsForReprocessing: List<KSAnnotated>,
)

fun SymbolsToProcess.takeOnly(
    takeSymbolsCount: Int,
): SymbolsToProcess {
    return copy(
        symbolsForProcessing = symbolsForProcessing.take(takeSymbolsCount),
        symbolsForReprocessing = symbolsForReprocessing + symbolsForProcessing.drop(takeSymbolsCount),
    )
}

fun SymbolsToProcess.forceProcess(
    filter: (KSAnnotated) -> Boolean = { false },
): SymbolsToProcess {
    val symbolsForProcessing = (symbolsForProcessing + symbolsForReprocessing.filter(filter)).toSet()
    return copy(
        symbolsForProcessing = symbolsForProcessing.toList(),
        symbolsForReprocessing = symbolsForReprocessing.filter { it !in symbolsForProcessing }
    )
}