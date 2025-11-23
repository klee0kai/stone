package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.SmartCode


fun interface FormatSimple {

    /**
     * @param code code witch return original type
     * @return code witch return wanna type
     */
    fun formatCode(
        or: SmartCode,
    ): SmartCode
}
