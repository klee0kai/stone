package com.github.klee0kai.stone.helpers.wrap;

import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.squareup.javapoet.TypeName;

public interface FormatSimple {

    /**
     * @param code code witch return original type
     * @return code witch return wanna type
     */
    SmartCode formatCode(TypeName providingType, SmartCode code);

}
