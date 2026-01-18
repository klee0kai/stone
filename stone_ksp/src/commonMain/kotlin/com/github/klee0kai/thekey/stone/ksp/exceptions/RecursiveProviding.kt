package com.github.klee0kai.thekey.stone.ksp.exceptions

import com.google.devtools.ksp.symbol.KSNode

class RecursiveProviding(
    message: String? = null,
    cause: Throwable? = null,
    element: KSNode? = null,
) : StoneException(
    message = message,
    cause = cause,
    element = element,
)