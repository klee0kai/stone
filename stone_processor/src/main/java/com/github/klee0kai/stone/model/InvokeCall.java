package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.types.wrappers.PhantomProvide;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.*;

public class InvokeCall {

    public static final int INVOKE_PROVIDE_OBJECT_CACHED = 0x1;

    public final List<MethodDetail> invokeSequence = new LinkedList<>();
    public final int flags;

    public InvokeCall(MethodDetail... callSequence) {
        this.invokeSequence.addAll(Arrays.asList(callSequence));
        this.flags = 0;
    }

    public InvokeCall(int flags, MethodDetail... callSequence) {
        this.invokeSequence.addAll(Arrays.asList(callSequence));
        this.flags = flags;
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
        List<Pair<TypeName, String>> provideFields = ListUtils.format(envFields, it -> {
            if (it.type instanceof ParameterizedTypeName) {
                ParameterizedTypeName type = (ParameterizedTypeName) it.type;
                TypeName orType = type.typeArguments.get(type.typeArguments.size() - 1);
                if (Objects.equals(type.rawType, ClassName.get(PhantomProvide.IProvide.class)))
                    return new Pair<>(orType, it.name + ".provide()");
                if (Objects.equals(type.rawType, ClassName.get(PhantomProvide.class)))
                    return new Pair<>(orType, it.name + ".get()");
            }
            return new Pair<>(it.type, it.name);
        });


        int i = 0;
        for (MethodDetail m : invokeSequence) {
            LinkedList<String> argNames = new LinkedList<>();
            for (FieldDetail arg : m.args) {
                Pair<TypeName, String> evField = ListUtils.first(provideFields, (inx, it) -> Objects.equals(it.first, arg.type));
                argNames.add(evField != null ? evField.second : "null");
            }
            if (i++ > 0) invokeBuilder.add(".");
            invokeBuilder.add("$L($L)", m.methodName, String.join(",", argNames));
        }
        return invokeBuilder.build();
    }

}
