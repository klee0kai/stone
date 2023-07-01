package com.github.klee0kai.stone.helpers.wrap;

import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;

public interface FormatInList {

    /**
     * @param originalListCode original list code
     * @param itemTransformFun format each type
     * @return
     */
    SmartCode formatCode(SmartCode originalListCode, FormatSimple itemTransformFun);

}
