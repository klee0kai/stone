package com.github.klee0kai.stone.codegen.helpers;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.closed.types.CacheAction;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.codegen.ModuleCacheControlInterfaceBuilder;
import com.github.klee0kai.stone.exceptions.ObjectNotProvidedException;
import com.github.klee0kai.stone.exceptions.RecurciveProviding;
import com.github.klee0kai.stone.model.*;
import com.github.klee0kai.stone.types.wrappers.PhantomProvide;
import com.github.klee0kai.stone.utils.RecursiveDetector;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.*;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.errorProvideType;
import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.recursiveProviding;
import static com.github.klee0kai.stone.model.InvokeCall.INVOKE_PROVIDE_OBJECT_CACHED;

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
            if (m.returnType.isPrimitive() || m.returnType == TypeName.VOID)
                continue;
            if (iModuleInterface.findMethod(m, false) != null)
                continue;
            boolean isCached = m.provideAnnotation == null || m.provideAnnotation.isCachingProvideType();
            int invokeProvideFlags = isCached ? INVOKE_PROVIDE_OBJECT_CACHED : 0;

            provideTypeCodes.putIfAbsent(m.returnType, new LinkedList<>());
            provideTypeCodes.get(m.returnType).add(new InvokeCall(invokeProvideFlags, provideModuleMethod, m));

            MethodDetail cacheControlMethod = new MethodDetail();
            cacheControlMethod.methodName = ModuleCacheControlInterfaceBuilder.cacheControlMethodName(m.methodName);
            cacheControlMethod.args.add(FieldDetail.simple("__action", ClassName.get(CacheAction.class)));
            for (FieldDetail it : m.args) {
                if (!((it.type instanceof ClassName) && allQualifiers.contains(it.type)))
                    continue;
                cacheControlMethod.args.add(it);
            }
            cacheControlMethod.returnType = m.returnType;

            cacheControlTypeCodes.putIfAbsent(m.returnType, new LinkedList<>());
            cacheControlTypeCodes.get(m.returnType).add(new InvokeCall(provideModuleMethod, cacheControlMethod));
        }
    }

    /**
     * Generate object's provide code from module or dependency.
     * Provide via {@link com.github.klee0kai.stone.types.wrappers.PhantomProvide.IProvide}
     *
     * @param localVariable     {@link com.github.klee0kai.stone.types.wrappers.PhantomProvide.IProvide} local variable which provide the type
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
        RecursiveDetector<Integer> needProvideDepsRecursiveDetector = new RecursiveDetector<>();
        needProvideDeps.add(typeName);

        // provide dependencies while not provide all
        while (!needProvideDeps.isEmpty()) {
            TypeName dep = needProvideDeps.pollFirst();
            InvokeCall invokeCall = provideTypeInvokeCall(provideTypeCodes, provideMethodName, dep, qualifiers);
            if (invokeCall == null) {
                if (Objects.equals(dep, typeName)) return null;
                throw new ObjectNotProvidedException(String.format(errorProvideType, dep.toString()));
            }
            provideMethodName = null;

            List<TypeName> newDeps = ListUtils.filter(new ArrayList<>(invokeCall.argTypes()), (indx, it) -> {
                if (Objects.equals(dep, it)) return false; // bind instance case
                // qualifies not need to provide
                return it instanceof ClassName && !allQualifiers.contains(it) && !argTypes.contains(it);
            });

            needProvideDeps.addAll(newDeps);
            needProvideDeps = ListUtils.removeDoublesRight(needProvideDeps, Objects::equals);
            boolean recursiveDetected = !newDeps.isEmpty() && needProvideDepsRecursiveDetector.next(needProvideDeps.hashCode());
            if (recursiveDetected) throw new RecurciveProviding(recursiveProviding);

            provideTypeInvokes.add(invokeCall);
            provideTypeInvokes = ListUtils.removeDoublesRight(provideTypeInvokes,
                    (it1, it2) -> Objects.equals(it1.resultType(), it2.resultType()));

        }

        int varIndex = 1;
        LinkedList<Pair<FieldDetail, InvokeCall>> depsProvide = new LinkedList<>();
        for (InvokeCall it : provideTypeInvokes) {
            String name = Objects.equals(it.resultType(), typeName) ? "result" : ("dep" + varIndex++);
            ParameterizedTypeName phProvideDep = ParameterizedTypeName.get(ClassName.get(PhantomProvide.IProvide.class), it.resultType());
            TypeName depProvideType = (it.flags & INVOKE_PROVIDE_OBJECT_CACHED) != 0 ? it.resultType() : phProvideDep;
            FieldDetail f = FieldDetail.simple(name, depProvideType);
            depsProvide.add(new Pair<>(f, it));
        }

        ParameterizedTypeName phantomVariableType = ParameterizedTypeName.get(ClassName.get(PhantomProvide.IProvide.class), typeName);
        CodeBlock.Builder provideCodeStatement = CodeBlock.builder()
                .beginControlFlow("$T $L = () -> ", phantomVariableType, localVariable);
        List<FieldDetail> vars = new LinkedList<>(qualifiers);
        while (!depsProvide.isEmpty()) {
            Pair<FieldDetail, InvokeCall> invokeCall = depsProvide.pollLast();
            boolean isLambdaProvide = invokeCall.first.type instanceof ParameterizedTypeName;

            if (!depsProvide.isEmpty()) {
                provideCodeStatement.addStatement(
                        isLambdaProvide ? "$T $L = () -> $L " : "$T $L =  $L ",
                        invokeCall.first.type, invokeCall.first.name,
                        invokeCall.second.invokeCode(vars)
                );
                vars.add(invokeCall.first);
            } else {
                provideCodeStatement.addStatement("return $L", invokeCall.second.invokeCode(vars));
            }
        }

        return provideCodeStatement
                .endControlFlow("")
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
        InvokeCall invokeCall = provideTypeInvokeCall(cacheControlTypeCodes, cacheControlMethodName, typeName, qualifiers);
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
            HashMap<TypeName, List<InvokeCall>> provideTypeCodes,
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
