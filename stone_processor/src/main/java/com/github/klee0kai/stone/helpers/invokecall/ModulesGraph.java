package com.github.klee0kai.stone.helpers.invokecall;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.closed.provide.ProvideBuilder;
import com.github.klee0kai.stone.closed.types.CacheAction;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.exceptions.ObjectNotProvidedException;
import com.github.klee0kai.stone.exceptions.RecurciveProviding;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.helpers.wrap.WrapHelper;
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
import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.nonWrappedType;
import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.transform;
import static java.util.Collections.singleton;

public class ModulesGraph {

    public static boolean SIMPLE_PROVIDE_OPTIMIZING = false;

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
            TypeName provTypeName = WrapHelper.nonWrappedType(m.returnType);
            if (provTypeName.isPrimitive() || provTypeName == TypeName.VOID)
                continue;
            if (iModuleInterface.findMethod(m, false) != null)
                continue;
            boolean isCached = !m.hasAnnotations(ProvideAnn.class) || m.ann(ProvideAnn.class).isCachingProvideType();
            int invokeProvideFlags = isCached ? INVOKE_PROVIDE_OBJECT_CACHED : 0;

            provideTypeCodes.putIfAbsent(provTypeName, new LinkedList<>());
            provideTypeCodes.get(provTypeName).add(new InvokeCall(invokeProvideFlags, provideModuleMethod, m));

            MethodDetail cacheControlMethod = new MethodDetail();
            cacheControlMethod.methodName = cacheControlMethodName(m.methodName);
            cacheControlMethod.args.add(FieldDetail.simple("__action", ClassName.get(CacheAction.class)));
            for (FieldDetail it : m.args) {
                if (!((it.type instanceof ClassName) && allQualifiers.contains(it.type)))
                    continue;
                cacheControlMethod.args.add(it);
            }
            cacheControlMethod.returnType = provTypeName;

            cacheControlTypeCodes.putIfAbsent(provTypeName, new LinkedList<>());
            cacheControlTypeCodes.get(provTypeName).add(new InvokeCall(provideModuleMethod, cacheControlMethod));
        }
    }

    public SmartCode codeProvideType(String provideMethodName, TypeName returnType, List<FieldDetail> qualifiers) {
        boolean isWrappedReturn = WrapHelper.isSupport(returnType);
        boolean isListReturn = WrapHelper.isList(returnType);
        TypeName providingType = isWrappedReturn ? nonWrappedType(returnType) : returnType;

        List<InvokeCall> provideTypeInvokes = provideInvokesWithDeps(provideMethodName, providingType, qualifiers);
        if (provideTypeInvokes == null || provideTypeInvokes.isEmpty()) {
            return null;
        }

        if (SIMPLE_PROVIDE_OPTIMIZING && provideTypeInvokes.size() == 1 && !isListReturn) {
            InvokeCall invokeCall = provideTypeInvokes.get(0);
            return WrapHelper.transform(
                    SmartCode.builder()
                            .add(invokeCall.invokeBest())
                            .providingType(invokeCall.resultType()),
                    returnType);
        }

        SmartCode builder = SmartCode.builder();
        TypeName provideBuilder = ParameterizedTypeName.get(ClassName.get(ProvideBuilder.class), providingType);
        TypeName provideBuilderList = ParameterizedTypeName.get(ClassName.get(Collection.class), providingType);
        builder.add(CodeBlock.of("new $T( ( single, list ) -> { \n", provideBuilder), null);

        for (InvokeCall inv : provideTypeInvokes) {
            boolean isCacheProvide = (inv.flags & INVOKE_PROVIDE_OBJECT_CACHED) != 0;
            FieldDetail singleDepField = FieldDetail.simple(builder.genLocalFieldName(), null);
            FieldDetail listDepField = FieldDetail.simple(builder.genLocalFieldName(), null);

            // provide single objects
            builder.withLocals(localBuilder -> {
                if (isCacheProvide) {
                    localBuilder.localVariable(singleDepField.name, inv.invokeBest());
                    singleDepField.type = inv.resultType();
                } else {
                    singleDepField.type = ParameterizedTypeName.get(ClassName.get(Ref.class), inv.resultType());
                    localBuilder.localVariable(singleDepField.name, SmartCode.builder()
                            .providingType(singleDepField.type)
                            .add("() -> ")
                            .add(inv.invokeBest())
                    );
                }
                return localBuilder;
            });

            // provide objects to lists
            builder.withLocals(localBuilder -> {
                listDepField.type = ParameterizedTypeName.get(ClassName.get(Ref.class),
                        ParameterizedTypeName.get(ClassName.get(List.class), inv.resultType())
                );
                localBuilder.localVariable(listDepField.name, SmartCode.builder()
                        .providingType(listDepField.type)
                        .add("() -> ")
                        .add(inv.invokeAllToList())
                );

                return localBuilder;
            });

            if (Objects.equals(inv.resultType(), providingType)) {
                builder.withLocals(localBuilder -> {
                    if (isListReturn) {
                        localBuilder.add("list.addAll( ")
                                .add(
                                        transform(
                                                SmartCode.of(listDepField.name, singleton(singleDepField.name))
                                                        .providingType(listDepField.type),
                                                provideBuilderList
                                        )
                                ).add(");\n");
                    } else {
                        localBuilder.add("list.add( ")
                                .add(
                                        transform(
                                                SmartCode.of(singleDepField.name, singleton(singleDepField.name))
                                                        .providingType(singleDepField.type),
                                                providingType
                                        )
                                ).add(");\n");

                    }
                    return localBuilder;
                });
                if (!isListReturn)
                    break;
            }
        }



        builder.add("\n  })");
        if (WrapHelper.isList(returnType)) {
            builder.add(".all() ")
                    .providingType(ParameterizedTypeName.get(ClassName.get(List.class), providingType));
        } else {
            builder.add(".first() ")
                    .providingType(providingType);
        }
        return WrapHelper.transform(builder, returnType);
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
                if (Objects.equals(dep, typeName)) {
                    return null;
                }
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
