package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.codegen.ModuleBuilder;
import com.github.klee0kai.stone.model.*;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.*;

public class ModulesGraph {


    private final HashMap<TypeName, List<InvokeCall>> provideTypeCodes = new HashMap<>();
    private final LinkedList<Pair<MethodDetail, ClassDetail>> modules = new LinkedList<>();

    public void addModule(MethodDetail provideModuleMethod, ClassDetail module) {
        modules.add(new Pair<>(provideModuleMethod, module));
        for (MethodDetail m : module.getAllMethods(false, "<init>")) {
            if (m.returnType.isPrimitive() || m.returnType == TypeName.VOID || m.returnType.isBoxedPrimitive())
                continue;
            ClassDetail rtClassDetails = AnnotationProcessor.allClassesHelper.findForType(m.returnType);
            if (rtClassDetails != null) for (ClassDetail cl : rtClassDetails.getAllParents(false)) {
                provideTypeCodes.putIfAbsent(cl.className, new LinkedList<>());
                provideTypeCodes.get(cl.className).add(new InvokeCall(provideModuleMethod, m));
            }
        }
    }

    /**
     * @param provideMethodName matching providing method name or null
     * @param typeName          providing type
     * @param qualifiers        qualifiers
     * @return
     */
    public CodeBlock codeProvideType(String provideMethodName, TypeName typeName, List<FieldDetail> qualifiers) {
        List<InvokeCall> invokeCalls = provideTypeCodes.getOrDefault(typeName, null);
        if (invokeCalls == null || invokeCalls.isEmpty())
            return null;
        Set<TypeName> qualifiersTypes = new HashSet<>(ListUtils.format(qualifiers, (it) -> it.type));
        int maxUsedQualifiersCount = -1;
        LinkedList<InvokeCall> bestInvokeCallVariants = new LinkedList<>();
        for (InvokeCall invokeCall : invokeCalls) {
            int usedQualifiers = invokeCall.argTypes(qualifiersTypes).size();
            if (maxUsedQualifiersCount < usedQualifiers) {
                bestInvokeCallVariants.clear();
                bestInvokeCallVariants.add(invokeCall);
                maxUsedQualifiersCount = usedQualifiers;
            }
            if (maxUsedQualifiersCount == usedQualifiers) {
                bestInvokeCallVariants.add(invokeCall);
            }
        }
        if (bestInvokeCallVariants.isEmpty()) {
            return null;
        }
        if (bestInvokeCallVariants.size() == 1) {
            return bestInvokeCallVariants.get(0).invokeCode(qualifiers);
        }
        InvokeCall nameMatchingInvokeCall = provideMethodName != null ? ListUtils.first(bestInvokeCallVariants, (inx, ob) -> {
            int len = ob.invokeSequence.size();
            return Objects.equals(provideMethodName, ob.invokeSequence.get(len - 1).methodName);
        }) : null;

        return (nameMatchingInvokeCall != null ? nameMatchingInvokeCall : bestInvokeCallVariants.get(0))
                .invokeCode(qualifiers);
    }

    /**
     * @param provideMethodName matching providing method name or null
     * @param typeName
     * @param valueCode
     * @return
     */
    public CodeBlock codeSetBindInstancesStatement(String provideMethodName, TypeName typeName, CodeBlock valueCode) {
        CodeBlock.Builder codeBuilder = CodeBlock.builder();
        for (Pair<MethodDetail, ClassDetail> m : modules) {
            MethodDetail method = m.first;
            ClassDetail module = m.second;
            List<MethodDetail> bindInstanceMethods = ListUtils.filter(module.getAllMethods(true, "<init>"),
                    (inx, bindInstMethod) -> bindInstMethod.bindInstanceAnnotation != null && Objects.equals(bindInstMethod.returnType, typeName)
            );
            if (bindInstanceMethods.isEmpty())
                continue;
            if (bindInstanceMethods.size() == 1 || provideMethodName == null) {
                String setBICacheMethodName = ModuleBuilder.setBindInstanceCachedMethodName(bindInstanceMethods.get(0).methodName);
                codeBuilder.addStatement(
                        "$L().$L( $L )",
                        method.methodName, setBICacheMethodName, valueCode
                );
                continue;
            }

            MethodDetail matchedMethod = ListUtils.first(bindInstanceMethods, (inx, m1) -> Objects.equals(provideMethodName, m1.methodName));
            matchedMethod = matchedMethod != null ? matchedMethod : bindInstanceMethods.get(0);
            String setBICacheMethodName = ModuleBuilder.setBindInstanceCachedMethodName(matchedMethod.methodName);
            codeBuilder.addStatement(
                    "$L().$L( $L )",
                    method.methodName, setBICacheMethodName, valueCode
            );
        }

        return codeBuilder.build();
    }

}
