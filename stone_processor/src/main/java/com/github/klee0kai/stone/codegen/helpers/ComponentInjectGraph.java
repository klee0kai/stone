package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;

public class ComponentInjectGraph {

    private final HashMap<TypeName, CodeBlock> provideTypeCodes = new HashMap<>();

    public void addModule(String provideModuleMethod, ClassDetail module) {
        for (MethodDetail m : module.getAllMethods(false, "<init>")) {
            provideTypeCodes.put(m.returnType,
                    CodeBlock.builder()
                            .add("$L().$L()", provideModuleMethod, m.methodName)
                            .build()
            );
        }
    }

    public CodeBlock codeProvideType(TypeName typeName) {
        return provideTypeCodes.getOrDefault(typeName, null);
    }

}
