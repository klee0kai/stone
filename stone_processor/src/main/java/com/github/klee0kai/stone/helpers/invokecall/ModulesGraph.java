package com.github.klee0kai.stone.helpers.invokecall;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.closed.provide.ProvideBuilder;
import com.github.klee0kai.stone.closed.types.CacheAction;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.exceptions.ObjectNotProvidedException;
import com.github.klee0kai.stone.exceptions.RecurciveProviding;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.ProvideAnn;
import com.github.klee0kai.stone.types.wrappers.Ref;
import com.github.klee0kai.stone.utils.RecursiveDetector;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.*;

import static com.github.klee0kai.stone.codegen.ModuleCacheControlInterfaceBuilder.cacheControlMethodName;
import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;
import static com.github.klee0kai.stone.helpers.invokecall.InvokeCall.INVOKE_PROVIDE_OBJECT_CACHED;
import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.transform;
import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.paramType;
import static java.util.Collections.singleton;

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
            boolean isCached = !m.hasAnnotations(ProvideAnn.class) || m.ann(ProvideAnn.class).isCachingProvideType();
            int invokeProvideFlags = isCached ? INVOKE_PROVIDE_OBJECT_CACHED : 0;

            provideTypeCodes.putIfAbsent(m.returnType, new LinkedList<>());
            provideTypeCodes.get(m.returnType).add(new InvokeCall(invokeProvideFlags, provideModuleMethod, m));

            MethodDetail cacheControlMethod = new MethodDetail();
            cacheControlMethod.methodName = cacheControlMethodName(m.methodName);
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

    public SmartCode codeProvideType(String provideMethodName, TypeName typeName, List<FieldDetail> qualifiers) {
        List<InvokeCall> provideTypeInvokes = provideInvokesWithDeps(provideMethodName, typeName, qualifiers);
        if (provideTypeInvokes == null || provideTypeInvokes.isEmpty()) {
            return null;
        }
        SmartCode builder = SmartCode.builder()
                .declared(qualifiers)
                .providingType(typeName);

        if (provideTypeInvokes.size() == 1) {
            return builder.add(provideTypeInvokes.get(0).invokeBest());
        }


        TypeName provideBuilder = ParameterizedTypeName.get(ClassName.get(ProvideBuilder.class), typeName);
        builder.add(CodeBlock.of("new $T( ( single, list ) -> { \n", provideBuilder), null);

        for (InvokeCall inv : provideTypeInvokes) {
            boolean isCacheProvide = (inv.flags & INVOKE_PROVIDE_OBJECT_CACHED) != 0;
            builder.withLocals(localBuilder -> {
                if (isCacheProvide) {
                    localBuilder.localVariable(inv.invokeBest());
                } else {
                    TypeName provDep = ParameterizedTypeName.get(ClassName.get(Ref.class), inv.resultType());
                    localBuilder.localVariable(SmartCode.builder()
                            .providingType(provDep)
                            .add("() -> ")
                            .add(inv.invokeBest())
                    );
                }
            });
        }

        builder.withLocals(localBuilder -> {
            for (FieldDetail f : localBuilder.getDeclaredFields()) {
                if (!Objects.equals(paramType(f.type), paramType(typeName)))
                    continue;

                localBuilder.add("list.add( ")
                        .add(
                                transform(
                                        SmartCode.of(f.name, singleton(f.name))
                                                .providingType(f.type),
                                        typeName
                                )
                        ).add(");\n");
            }
        });

        builder.add("\n  }).first() ");
        return builder;
    }


    public List<InvokeCall> provideInvokesWithDeps(String provideMethodName, TypeName typeName, List<FieldDetail> qualifiers) {
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
                throw new ObjectNotProvidedException(
                        createErrorMes()
                                .errorProvideType(dep.toString())
                                .build(),
                        null
                );
            }
            provideMethodName = null;

            List<TypeName> newDeps = ListUtils.filter(invokeCall.argTypes(true, null), (indx, it) -> {
                if (Objects.equals(dep, it)) return false; // bind instance case
                // qualifies not need to provide
                return it instanceof ClassName && !allQualifiers.contains(it) && !argTypes.contains(it);
            });

            needProvideDeps.addAll(newDeps);
            needProvideDeps = ListUtils.removeDoublesRight(needProvideDeps, Objects::equals);
            boolean recursiveDetected = !newDeps.isEmpty() && needProvideDepsRecursiveDetector.next(needProvideDeps.hashCode());
            if (recursiveDetected) {
                throw new RecurciveProviding(
                        createErrorMes()
                                .recursiveProviding()
                                .build(),
                        null
                );
            }

            provideTypeInvokes.add(invokeCall);
            provideTypeInvokes = ListUtils.removeDoublesRight(provideTypeInvokes,
                    (it1, it2) -> Objects.equals(it1.resultType(), it2.resultType()));

        }
        Collections.reverse(provideTypeInvokes);
        return provideTypeInvokes;
    }

    /**
     * Generate cache control method invoke. Clean refs, change ref type and other
     *
     * @param provideMethodName predefined method name
     * @param typeName          the name of the type whose cache needs to be changed
     * @param qualifiers        method's arguments
     * @return cache control invoke call
     */
    public InvokeCall invokeControlCacheForType(String provideMethodName, TypeName typeName, List<FieldDetail> qualifiers) {
        String cacheControlMethodName = cacheControlMethodName(provideMethodName);
        return provideTypeInvokeCall(cacheControlTypeCodes, cacheControlMethodName, typeName, qualifiers);
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
        invokeCalls.sort((inv1, inv2) -> {
            // first compare uses qualifiers
            int usedQualifiers1 = inv1.argTypes(true, qualifiersTypes).size();
            int usedQualifiers2 = inv2.argTypes(true, qualifiersTypes).size();
            int qCompare = Integer.compare(usedQualifiers2, usedQualifiers1);
            if (qCompare != 0) return qCompare; // more used qualifies

            // second compare name equals
            int len1 = inv1.bestSequence().size();
            int name1 = Objects.equals(provideMethodName, inv1.bestSequence().get(len1 - 1).methodName) ? 1 : 0;
            int len2 = inv2.bestSequence().size();
            int name2 = Objects.equals(provideMethodName, inv2.bestSequence().get(len2 - 1).methodName) ? 1 : 0;
            int nameCompare = Integer.compare(name2, name1);
            if (nameCompare != 0) return nameCompare; // better with name equals

            // low null's using
            int qualifiers1 = inv1.argTypes(true, null).size();
            int qualifiers2 = inv2.argTypes(true, null).size();
            qCompare = Integer.compare(qualifiers1, qualifiers2);
            return qCompare; // less all qualifies
        });
        return !invokeCalls.isEmpty() ? new InvokeCall(invokeCalls) : null;
    }

}