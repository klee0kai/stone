package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.InvokeCall;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.types.ListUtils;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.*;

public class ComponentInjectGraph {


    private final HashMap<TypeName, List<InvokeCall>> provideTypeCodes = new HashMap<>();

    public void addModule(MethodDetail provideModuleMethod, ClassDetail module) {
        for (MethodDetail m : module.getAllMethods(false, "<init>")) {
            provideTypeCodes.putIfAbsent(m.returnType, new LinkedList<>());
            provideTypeCodes.get(m.returnType).add(new InvokeCall(provideModuleMethod, m));
        }
    }

    public CodeBlock codeProvideType(TypeName typeName, List<FieldDetail> qualifiers) {
        List<InvokeCall> invokeCalls = provideTypeCodes.getOrDefault(typeName, null);
        if (invokeCalls == null || invokeCalls.isEmpty())
            return null;
        Set<TypeName> qualifiersTypes = new HashSet<>(ListUtils.format(qualifiers, (it) -> it.type));
        int bestUsedTypeCount = 0;
        InvokeCall bestInvokeCall = null;
        for (InvokeCall invokeCall : invokeCalls) {
            if (bestInvokeCall == null) {
                bestInvokeCall = invokeCall;
                bestUsedTypeCount = invokeCall.argTypes(qualifiersTypes).size();
            } else {
                int usedTypes = invokeCall.argTypes(qualifiersTypes).size();
                if (bestUsedTypeCount < usedTypes) {
                    bestUsedTypeCount = usedTypes;
                    bestInvokeCall = invokeCall;
                }

            }
        }


        return bestInvokeCall != null ? bestInvokeCall.invokeCode(qualifiers) : null;

    }

}
