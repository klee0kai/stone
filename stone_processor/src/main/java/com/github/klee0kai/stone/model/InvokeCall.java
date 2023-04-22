package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.*;

public class InvokeCall {

    public final List<MethodDetail> invokeSequence = new LinkedList<>();

    public InvokeCall(MethodDetail... callSequence) {
        this.invokeSequence.addAll(Arrays.asList(callSequence));
    }


    public Set<TypeName> argTypes() {
        Set<TypeName> argsTypes = new HashSet<>();
        for (MethodDetail m : invokeSequence) {
            argsTypes.addAll(ListUtils.format(m.args, (it) -> it.type));
        }
        return argsTypes;
    }

    public Set<TypeName> argTypes(Set<TypeName> filter) {
        Set<TypeName> argsTypes = new HashSet<>();
        for (MethodDetail m : invokeSequence) {
            List<TypeName> types = ListUtils.format(m.args, (it) -> it.type);
            types = ListUtils.filter(types, (inx, it) -> filter.contains(it));
            argsTypes.addAll(types);
        }
        return argsTypes;
    }

    public TypeName resultType() {
        return invokeSequence.get(invokeSequence.size() - 1).returnType;
    }

    public CodeBlock invokeCode(List<FieldDetail> envFields) {
        CodeBlock.Builder invokeBuilder = CodeBlock.builder();
        int i = 0;
        for (MethodDetail m : invokeSequence) {
            LinkedList<String> argNames = new LinkedList<>();
            for (FieldDetail arg : m.args) {
                FieldDetail evField = ListUtils.first(envFields, (inx, it) -> Objects.equals(it.type, arg.type));
                argNames.add(evField != null ? evField.name : "null");
            }
            if (i++ > 0)
                invokeBuilder.add(".");
            invokeBuilder.add("$L($L)", m.methodName, String.join(",", argNames));
        }
        return invokeBuilder.build();
    }

}
