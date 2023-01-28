package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.closed.types.CacheAction;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.codegen.ModuleCacheControlInterfaceBuilder;
import com.github.klee0kai.stone.model.*;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.*;

public class ModulesGraph {


    private final HashMap<TypeName, List<InvokeCall>> provideTypeCodes = new HashMap<>();
    private final HashMap<TypeName, List<InvokeCall>> cacheControlTypeCodes = new HashMap<>();
    private final LinkedList<Pair<MethodDetail, ClassDetail>> modules = new LinkedList<>();


    public void addModule(MethodDetail provideModuleMethod, ClassDetail module, Set<ClassName> qualifiers) {
        modules.add(new Pair<>(provideModuleMethod, module));
        ClassDetail iModuleInterface = AnnotationProcessor.allClassesHelper.iModule;
        for (MethodDetail m : module.getAllMethods(false, true, "<init>")) {
            if (m.returnType.isPrimitive() || m.returnType == TypeName.VOID || m.returnType.isBoxedPrimitive())
                continue;
            if (iModuleInterface.findMethod(m, false) != null)
                continue;

            ClassDetail rtClassDetails = AnnotationProcessor.allClassesHelper.findForType(m.returnType);
            provideTypeCodes.putIfAbsent(rtClassDetails.className, new LinkedList<>());
            provideTypeCodes.get(rtClassDetails.className).add(new InvokeCall(provideModuleMethod, m));

            MethodDetail cacheControlMethod = new MethodDetail();
            cacheControlMethod.methodName = ModuleCacheControlInterfaceBuilder.cacheControlMethodName(m.methodName);
            cacheControlMethod.args.add(FieldDetail.simple("__action", ClassName.get(CacheAction.class)));
            for (FieldDetail it : m.args) {
                if (!((it.type instanceof ClassName) && qualifiers.contains(it.type)))
                    continue;
                cacheControlMethod.args.add(it);
            }
            cacheControlMethod.returnType = m.returnType;

            cacheControlTypeCodes.putIfAbsent(rtClassDetails.className, new LinkedList<>());
            cacheControlTypeCodes.get(rtClassDetails.className).add(new InvokeCall(provideModuleMethod, cacheControlMethod));
        }
    }

    public InvokeCall provideTypeInvokeCall(HashMap<TypeName, List<InvokeCall>> provideTypeCodes, String provideMethodName, TypeName typeName, List<FieldDetail> qualifiers) {
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
            return bestInvokeCallVariants.get(0);
        }
        InvokeCall nameMatchingInvokeCall = provideMethodName != null ? ListUtils.first(bestInvokeCallVariants, (inx, ob) -> {
            int len = ob.invokeSequence.size();
            return Objects.equals(provideMethodName, ob.invokeSequence.get(len - 1).methodName);
        }) : null;

        return nameMatchingInvokeCall != null ? nameMatchingInvokeCall : bestInvokeCallVariants.get(0);
    }

    /**
     * @param provideMethodName matching providing method name or null
     * @param typeName          providing type
     * @param qualifiers        qualifiers
     * @return
     */
    public CodeBlock codeProvideType(String provideMethodName, TypeName typeName, List<FieldDetail> qualifiers) {
        InvokeCall invokeCall = provideTypeInvokeCall(provideTypeCodes, provideMethodName, typeName, qualifiers);
        return invokeCall != null ? invokeCall.invokeCode(qualifiers) : null;
    }


    public CodeBlock codeControlCacheForType(String provideMethodName, TypeName typeName, List<FieldDetail> qualifiers, CodeBlock actionParams) {
        InvokeCall invokeCall = provideTypeInvokeCall(cacheControlTypeCodes, provideMethodName, typeName, qualifiers);
        if (invokeCall == null || invokeCall.invokeSequence.isEmpty()) {
            return null;
        }
        CodeBlock.Builder invokeBuilder = CodeBlock.builder();
        int invokeCount = 0;
        for (MethodDetail m : invokeCall.invokeSequence) {
            if (invokeCount++ > 0)
                invokeBuilder.add(".");

            int argCount = 0;
            CodeBlock.Builder argsCodeBuilder = CodeBlock.builder();
            for (FieldDetail arg : m.args) {
                if (argCount > 0) argsCodeBuilder.add(",");
                if (Objects.equals(arg.type, ClassName.get(CacheAction.class))) {
                    argsCodeBuilder.add(actionParams);
                    argCount++;
                } else {
                    FieldDetail evField = ListUtils.first(qualifiers, (inx, it) -> Objects.equals(it.type, arg.type));
                    argsCodeBuilder.add(evField != null ? evField.name : "null");
                }
            }
            invokeBuilder.add("$L($L)", m.methodName, argsCodeBuilder.build());
        }
        return invokeBuilder.build();
    }


}
