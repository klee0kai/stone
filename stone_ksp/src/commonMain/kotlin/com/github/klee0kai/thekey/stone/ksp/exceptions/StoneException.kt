package com.github.klee0kai.thekey.stone.ksp.exceptions

import com.google.devtools.ksp.symbol.KSNode


open class StoneException(
    message: String? = null,
    cause: Throwable? = null,
    val element: KSNode? = null,
) : IllegalStateException(message, cause) {

    fun findLastErrorElement(): KSNode? {
        var sourceElement: KSNode? = null
        if (cause is StoneException) {
            sourceElement = (cause as StoneException).findLastErrorElement()
        }
        if (sourceElement == null) sourceElement = element
        return sourceElement
    }

}
