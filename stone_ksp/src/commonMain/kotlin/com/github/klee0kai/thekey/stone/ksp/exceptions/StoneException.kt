package com.github.klee0kai.thekey.stone.ksp.exceptions

import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.NonExistLocation


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
        if (sourceElement == null || sourceElement.location is NonExistLocation) {
            sourceElement = element
        }
        return sourceElement
    }

}
