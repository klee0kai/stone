package com.github.klee0kai.stone.helpers.invokecall;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone._hidden_.provide.ProvideBuilder;
import com.github.klee0kai.stone._hidden_.types.StCacheAction;
import com.github.klee0kai.stone._hidden_.types.StListUtils;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.exceptions.ObjectNotProvidedException;
import com.github.klee0kai.stone.exceptions.RecurciveProviding;
import com.github.klee0kai.stone.exceptions.StoneException;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.helpers.wrap.WrapHelper;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.ProvideAnn;
import com.github.klee0kai.stone.model.annotations.QualifierAnn;
import com.github.klee0kai.stone.utils.RecursiveDetector;
import com.github.klee0kai.stone.wrappers.Ref;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.*;

import static com.github.klee0kai.stone.codegen.ModuleCacheControlInterfaceBuilder.cacheControlMethodName;
import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;
import static com.github.klee0kai.stone.helpers.invokecall.InvokeCall.INVOKE_PROVIDE_OBJECT_CACHED;
import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.*;
import static com.github.klee0kai.stone.utils.LocalFieldName.genLocalFieldName;
import static java.util.Collections.singleton;

public class ModulesGraph {

    public static boolean SIMPLE_PROVIDE_OPTIMIZING = true;
    public static int MAX_PROVIDE_RESOLVE_COUNT = 10_000;

    public final Set<ClassName> allQualifiers = new HashSet<>();
    private final HashMap<TypeName, Set<InvokeCall>> provideTypeCodes = new HashMap<>();
    private final HashMap<TypeName, Set<InvokeCall>> cacheControlTypeCodes = new HashMap<>();


    /**
     * Methods graph build.
     *
     * @param provideModuleMethod module's provide method
     * @param module              module's class
     */
    public void collectFromModule(MethodDetail provideModuleMethod, ClassDetail module) {
        ClassDetail iModuleInterface = AnnotationProcessor.allClassesHelper.iModule;
        for (MethodDetail m : module.getAllMethods(false, true, "<init>")) {
            TypeName provTypeName = nonWrappedType(m.returnType);
            if (provTypeName.isPrimitive() || provTypeName == TypeName.VOID)
                continue;
            if (iModuleInterface.findMethod(m, false) != null)
                continue;
            boolean isCached = !m.hasAnnotations(ProvideAnn.class) || m.ann(ProvideAnn.class).isCachingProvideType();
            int invokeProvideFlags = isCached ? INVOKE_PROVIDE_OBJECT_CACHED : 0;

            provideTypeCodes.putIfAbsent(provTypeName, new HashSet<>());
            provideTypeCodes.get(provTypeName).add(new InvokeCall(invokeProvideFlags, provideModuleMethod, m));

            MethodDetail cacheControlMethod = new MethodDetail();
            cacheControlMethod.methodName = cacheControlMethodName(m.methodName);
            cacheControlMethod.args.add(FieldDetail.simple("__action", ClassName.get(StCacheAction.class)));
            for (FieldDetail it : m.args) {
                if (!((it.type instanceof ClassName) && allQualifiers.contains(it.type)))
                    continue;
                cacheControlMethod.args.add(it);
            }
            cacheControlMethod.returnType = listWrapTypeIfNeed(m.returnType);
            cacheControlMethod.qualifierAnns = m.qualifierAnns;

            cacheControlTypeCodes.putIfAbsent(provTypeName, new HashSet<>());
            cacheControlTypeCodes.get(provTypeName).add(new InvokeCall(provideModuleMethod, cacheControlMethod));
        }
    }

    public SmartCode codeProvideType(String methodName, TypeName returnType, Set<QualifierAnn> qualifierAnns) {
        boolean isWrappedReturn = isSupport(returnType);
        boolean isListReturn = isList(returnType);
        TypeName providingType = isWrappedReturn ? nonWrappedType(returnType) : returnType;

        List<InvokeCall> provideTypeInvokes = provideInvokesWithDeps(new ProvideDep(methodName, returnType, qualifierAnns));
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
        String listFieldName = genLocalFieldName();
        builder.add(CodeBlock.of("new $T( ( $L ) -> { \n", provideBuilder, listFieldName), null);

        for (InvokeCall inv : provideTypeInvokes) {
            boolean isCacheProvide = (inv.flags & INVOKE_PROVIDE_OBJECT_CACHED) != 0;
            FieldDetail singleDepField = FieldDetail.simple(genLocalFieldName(), null);
            FieldDetail listDepField = FieldDetail.simple(genLocalFieldName(), null);
            boolean isListInv = inv.invokeSequenceVariants.size() > 1;

            builder.withLocals(localBuilder -> {
                // provide single objects
                if (isCacheProvide) {
                    localBuilder.localVariable(singleDepField.name, inv.qualifierAnnotations(true), inv.invokeBest());
                    singleDepField.type = inv.resultType();
                } else {
                    singleDepField.type = ParameterizedTypeName.get(ClassName.get(Ref.class), inv.resultType());
                    localBuilder.localVariable(singleDepField.name, inv.qualifierAnnotations(true), SmartCode.builder()
                            .add("() -> ")
                            .add(inv.invokeBest())
                            .providingType(singleDepField.type)
                    );
                }
                return localBuilder;
            });

            builder.withLocals(localBuilder -> {
                // provide list objects
                listDepField.type = ParameterizedTypeName.get(ClassName.get(Ref.class),
                        ParameterizedTypeName.get(ClassName.get(List.class), inv.resultType())
                );
                localBuilder.localVariable(listDepField.name, inv.qualifierAnnotations(true), SmartCode.builder()
                        .add("() -> ")
                        .add(inv.invokeAllToList())
                        .providingType(listDepField.type)
                );
                return localBuilder;
            });

            if (Objects.equals(inv.resultType(), providingType)) {
                builder.withLocals(localBuilder -> {
                    if (isListReturn) {
                        localBuilder
                                .add(listFieldName)
                                .add(".addAll( ")
                                .add(
                                        transform(
                                                SmartCode.of(listDepField.name, singleton(listDepField.name))
                                                        .providingType(listDepField.type),
                                                provideBuilderList
                                        )
                                ).add(");\n");
                    } else {
                        localBuilder
                                .add(listFieldName)
                                .add(".add( ")
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


    public List<InvokeCall> provideInvokesWithDeps(ProvideDep provideDep) {
        LinkedList<InvokeCall> provideTypeInvokes = new LinkedList<>();
        LinkedList<ProvideDep> needProvideDeps = new LinkedList<>();
        RecursiveDetector<Integer> needProvideDepsRecursiveDetector = new RecursiveDetector<>();
        needProvideDeps.add(provideDep);
        int loopCount = 0;

        // provide dependencies while not provide all
        while (!needProvideDeps.isEmpty()) {
            ProvideDep rawDep = needProvideDeps.pollFirst();
            TypeName dep = nonWrappedType(rawDep.typeName);
            InvokeCall invokeCall = provideTypeInvokeCall(provideTypeCodes, dep, rawDep.qualifierAnns, rawDep.methodName, isList(rawDep.typeName));
            if (invokeCall == null) {
                if (Objects.equals(provideDep, rawDep)) {
                    return null;
                }

                throw new ObjectNotProvidedException(
                        createErrorMes()
                                .errorProvideType(dep.toString())
                                .build(),
                        null
                );
            }

            List<ProvideDep> newDeps = StListUtils.filter(invokeCall.argDeps(), (i, it) -> {
                if (Objects.equals(provideDep.typeName, it.typeName) && Objects.equals(rawDep.typeName, it.typeName)) {
                    // bind instance case. Argument and return type are equals
                    return false;
                }
                // qualifies not need to provide
                TypeName argNonWrapped = nonWrappedType(it.typeName);
                return argNonWrapped instanceof ClassName && !allQualifiers.contains(argNonWrapped);
            });

            needProvideDeps.addAll(newDeps);
            needProvideDeps = StListUtils.removeDoublesRight(needProvideDeps, Objects::equals);
            boolean recursiveDetected = !newDeps.isEmpty() && needProvideDepsRecursiveDetector.next(needProvideDeps.hashCode());
            if (recursiveDetected) {
                throw new RecurciveProviding(
                        createErrorMes()
                                .errorProvideType(provideDep.typeName.toString())
                                .recursiveProviding()
                                .build()
                );
            }
            if (loopCount++ > MAX_PROVIDE_RESOLVE_COUNT) {
                throw new StoneException(
                        createErrorMes()
                                .errorProvideType(provideDep.typeName.toString())
                                .add("long providing loop for type. Stone library Error.")
                                .build(),
                        null
                );
            }

            provideTypeInvokes.add(invokeCall);
            provideTypeInvokes = StListUtils.removeDoublesRight(provideTypeInvokes, (it1, it2) -> {
                return Objects.equals(it1.resultType(), it2.resultType())
                        && Objects.equals(it1.qualifierAnnotations(true), it2.qualifierAnnotations(true));
            });

        }
        Collections.reverse(provideTypeInvokes);
        return provideTypeInvokes;
    }

    /**
     * Generate cache control method invoke. Clean refs, change ref type and other
     *
     * @param provideMethodName predefined method name
     * @param typeName          the name of the type whose cache needs to be changed
     * @return cache control invoke call
     */
    public InvokeCall invokeControlCacheForType(String provideMethodName, TypeName typeName, Set<QualifierAnn> qualifierAnns) {
        String cacheControlMethodName = cacheControlMethodName(provideMethodName);
        return provideTypeInvokeCall(cacheControlTypeCodes, typeName, qualifierAnns, cacheControlMethodName, false);
    }

    private InvokeCall provideTypeInvokeCall(
            HashMap<TypeName, Set<InvokeCall>> provideTypeCodes,
            TypeName typeName,
            Set<QualifierAnn> qualifierAnns,
            String provideMethodName,
            boolean listVariants
    ) {
        Set<InvokeCall> invokeCalls = provideTypeCodes.getOrDefault(typeName, null);
        if (invokeCalls == null || invokeCalls.isEmpty())
            return null;
        List<InvokeCall> filtered = !listVariants || qualifierAnns != null && !qualifierAnns.isEmpty()
                ? StListUtils.filter(invokeCalls, (i, it) -> Objects.equals(it.qualifierAnnotations(false), qualifierAnns))
                : new LinkedList<>(invokeCalls);

        filtered = provideMethodName != null ? StListUtils.filter(filtered, (i, it) -> {
            int len = it.bestSequence().size();
            String mName = it.bestSequence().get(len - 1).methodName;
            return Objects.equals(provideMethodName, mName);
        }) : filtered;

        if (!listVariants && filtered.size() > 1) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .errorProvideType(typeName.toString())
                            .add(": is bound multi times.\n")
                            .add(String.join(" and \n", StListUtils.format(filtered, InvokeCall::toString)))
                            .build());
        }
        return !filtered.isEmpty() ? new InvokeCall(filtered) : null;
    }

}
