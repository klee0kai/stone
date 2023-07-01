package com.github.klee0kai.stone.helpers.wrap;

import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;

public interface FormatSimple {

    /**
     * ForExample
     * new T() -> ( () -> new T() )
     *
     * @param code code witch return original type
     * @return code witch return wanna type
     */
    SmartCode formatCode(SmartCode code);

}
