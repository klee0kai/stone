package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.SmartCodeScopeBuilder


fun interface FormatSimple {

    /**
     * @param code code witch return original type
     * @return code witch return wanna type
     */
    fun SmartCodeScopeBuilder.formatCode(or: SmartCodeScopeBuilder)
}
