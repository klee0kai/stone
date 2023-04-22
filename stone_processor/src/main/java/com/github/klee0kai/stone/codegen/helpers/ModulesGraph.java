package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.closed.types.CacheAction;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.codegen.ModuleCacheControlInterfaceBuilder;
import com.github.klee0kai.stone.exceptions.ObjectNotProvidedException;
import com.github.klee0kai.stone.exceptions.RecurciveProviding;
import com.github.klee0kai.stone.model.*;
import com.github.klee0kai.stone.types.wrappers.PhantomProvide;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.*;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.recursiveProviding;

public class ModulesGraph {

    public final Set<ClassName> allQualifiers = new HashSet<>();
    private final HashMap<TypeName, List<InvokeCall>> provideTypeCodes = new HashMap<>();
    private final HashMap<TypeName, List<InvokeCall>> cacheControlTypeCodes = new HashMap<>();


    /**
     * Methods graph build.
     *
     * @param provideModuleMethod module's provide method
     * @param module              module's class
     */
    public void collectFromModule(MethodDetail provideModuleMethod, ClassDetail module) {
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
                if (!((it.type instanceof ClassName) && allQualifiers.contains(it.type)))
                    continue;
                cacheControlMethod.args.add(it);
            }
            cacheControlMethod.returnType = m.returnType;

            cacheControlTypeCodes.putIfAbsent(rtClassDetails.className, new LinkedList<>());
            cacheControlTypeCodes.get(rtClassDetails.className).add(new InvokeCall(provideModuleMethod, cacheControlMethod));
        }
    }

    /**
     * Generate object's provide code from module or dependency.
     * Provide over crate new PhantomProvider variable
     *
     * @param localVariable     phantomProvider local variable which provide the type
     * @param provideMethodName predefined method name. Useful for bind instance names
     * @param typeName          providing type
     * @param qualifiers        using component's qualifiers
     * @return generated code.
     */
    public CodeBlock statementProvideType(String localVariable, String provideMethodName, TypeName typeName, List<FieldDetail> qualifiers) {
        if (localVariable == null) localVariable = "phProvider";
        Set<TypeName> argTypes = new HashSet<>(ListUtils.format(qualifiers, it -> it.type));
        LinkedList<InvokeCall> provideTypeInvokes = new LinkedList<>();
        LinkedList<TypeName> needProvideDeps = new LinkedList<>();
        needProvideDeps.add(typeName);

        // provide dependencies while not provide all
        while (!needProvideDeps.isEmpty()) {
            TypeName dep = needProvideDeps.pollFirst();
            InvokeCall invokeCall = provideTypeInvokeCall(provideMethodName, dep, qualifiers);
            if (invokeCall == null) {
                if (Objects.equals(dep, typeName)) return null;
                //todo correct throw error
                throw new ObjectNotProvidedException(dep, dep, provideMethodName);
            }
            provideMethodName = null;

            List<TypeName> newDeps = ListUtils.filter(new ArrayList<>(invokeCall.argTypes()), (indx, it) -> {
                if (Objects.equals(dep, it)) return false; // bind instance case
                for (InvokeCall f : provideTypeInvokes) {
                    //check already provided to local variable
                    if (Objects.equals(f.resultType(), it)) return false;
                }
                // qualifies not need to provide
                return it instanceof ClassName && !allQualifiers.contains(it) && !argTypes.contains(it);
            });
            if (!newDeps.isEmpty() && ListUtils.listAreSame(newDeps, needProvideDeps, Objects::equals)) {
                String recursiveDeps = String.join(", ", ListUtils.format(newDeps, TypeName::toString));
                throw new RecurciveProviding(String.format(recursiveProviding, recursiveDeps));
            }

            needProvideDeps.addAll(newDeps);
            needProvideDeps = ListUtils.removeDoublesRight(needProvideDeps, Objects::equals);
            provideTypeInvokes.add(invokeCall);
        }

        int varIndex = 1;
        LinkedList<Pair<FieldDetail, InvokeCall>> depsProvide = new LinkedList<>();
        for (InvokeCall it : provideTypeInvokes) {
            String name = Objects.equals(it.resultType(), typeName) ? "__result" : ("__dep" + varIndex++);
            FieldDetail f = FieldDetail.simple(name, it.resultType());
            depsProvide.add(new Pair<>(f, it));
        }

        ParameterizedTypeName phantomVariableType = ParameterizedTypeName.get(ClassName.get(PhantomProvide.class), typeName);
        CodeBlock.Builder provideCodeStatement = CodeBlock.builder()
                .beginControlFlow("$T $L = new $T( () -> ", phantomVariableType, localVariable, phantomVariableType);
        List<FieldDetail> vars = new LinkedList<>(qualifiers);
        while (!depsProvide.isEmpty()) {
            Pair<FieldDetail, InvokeCall> invokeCall = depsProvide.pollLast();
            provideCodeStatement.addStatement("$T $L = $L", invokeCall.first.type, invokeCall.first.name, invokeCall.second.invokeCode(vars));
            vars.add(invokeCall.first);
        }
        return provideCodeStatement.addStatement("return __result")
                .endControlFlow(")")
                .build();
    }


    /**
     * Generate cache control method invoke. Clean refs, change ref type and other
     *
     * @param provideMethodName predefined method name
     * @param typeName          the name of the type whose cache needs to be changed
     * @param qualifiers        method's arguments
     * @param actionParams      change cache action
     * @return generated code
     */
    public CodeBlock codeControlCacheForType(String provideMethodName, TypeName typeName, List<FieldDetail> qualifiers, CodeBlock actionParams) {
        String cacheControlMethodName = ModuleCacheControlInterfaceBuilder.cacheControlMethodName(provideMethodName);
        InvokeCall invokeCall = provideTypeInvokeCall(cacheControlMethodName, typeName, qualifiers);
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

    private InvokeCall provideTypeInvokeCall(
            String provideMethodName,
            TypeName typeName,
            List<FieldDetail> qualifiers
    ) {
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

}
