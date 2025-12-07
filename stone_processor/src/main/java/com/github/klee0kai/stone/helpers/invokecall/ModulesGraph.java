package com.github.klee0kai.stone.helpers.invokecall;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone._hidden_.provide.ProvideBuilder;
import com.github.klee0kai.stone._hidden_.types.CacheAction;
import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.exceptions.ObjectNotProvidedException;
import com.github.klee0kai.stone.exceptions.RecursiveProviding;
import com.github.klee0kai.stone.exceptions.StoneException;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.BindInstanceAnn;
import com.github.klee0kai.stone.model.annotations.ProvideAnn;
import com.github.klee0kai.stone.model.annotations.QualifierAnn;
import com.github.klee0kai.stone.utils.RecursiveDetector;
import com.github.klee0kai.stone.wrappers.Ref;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.*;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;
import static com.github.klee0kai.stone.codegen.ModuleCacheControlInterfaceBuilder.cacheControlMethodName;
import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;
import static com.github.klee0kai.stone.helpers.invokecall.InvokeCall.INVOKE_PROVIDE_BIND_INSTANCE;
import static com.github.klee0kai.stone.helpers.invokecall.InvokeCall.INVOKE_PROVIDE_OBJECT_CACHED;
import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.*;
import static com.github.klee0kai.stone.utils.LocalFieldName.genLocalFieldName;

public class ModulesGraph {

    public static boolean SIMPLE_PROVIDE_OPTIMIZING = true;
    public static int MAX_PROVIDE_RESOLVE_COUNT = 10_000;

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
            boolean isBindInstance = m.hasAnnotations(BindInstanceAnn.class);
            int invokeProvideFlags = isCached ? INVOKE_PROVIDE_OBJECT_CACHED : 0;
            invokeProvideFlags |= isBindInstance ? INVOKE_PROVIDE_BIND_INSTANCE : 0;

            provideTypeCodes.putIfAbsent(provTypeName, new HashSet<>());
            provideTypeCodes.get(provTypeName).add(new InvokeCall(invokeProvideFlags, provideModuleMethod, m));

            MethodDetail cacheControlMethod = new MethodDetail();
            cacheControlMethod.methodName = cacheControlMethodName(m.methodName);
            cacheControlMethod.args.add(FieldDetail.simple("__action", ClassName.get(CacheAction.class)));
            for (FieldDetail it : m.args) {
                if (!((it.type instanceof ClassName) && allClassesHelper.allIdentifiers.contains(it.type)))
                    continue;
                cacheControlMethod.args.add(it);
            }
            cacheControlMethod.returnType = listWrapTypeIfNeed(m.returnType);
            cacheControlMethod.qualifierAnns = m.qualifierAnns;

            cacheControlTypeCodes.putIfAbsent(provTypeName, new HashSet<>());
            cacheControlTypeCodes.get(provTypeName).add(new InvokeCall(provideModuleMethod, cacheControlMethod));
        }
    }

    public CodeBlock codeProvideType(
            String methodName,
            TypeName returnType,
            Set<QualifierAnn> qualifierAnns,
            Collection<FieldDetail> declaredFields
    ) {
        boolean isWrappedReturn = isSupport(returnType);
        TypeName providingType = isWrappedReturn ? nonWrappedType(returnType) : returnType;

        Set<ProvideDep> provideDeps = new HashSet<>();
        provideDeps.add(new ProvideDep(methodName, returnType, qualifierAnns));
        List<InvokeCall> provideTypeInvokes = provideInvokesWithDeps(provideDeps.iterator().next());
        if (provideTypeInvokes == null || provideTypeInvokes.isEmpty()) {
            return null;
        }
        for (InvokeCall provideTypeInvoke : provideTypeInvokes) provideDeps.addAll(provideTypeInvoke.argDeps());
        if (SIMPLE_PROVIDE_OPTIMIZING && provideTypeInvokes.size() == 1 && !isList(returnType)) {
            InvokeCall invokeCall = provideTypeInvokes.get(0).best();
            return transform(
                    invokeCall.rawReturnType(),
                    returnType,
                    invokeCall.invokeCode(declaredFields)
            );
        }

        TypeName provideBuilder = ParameterizedTypeName.get(ClassName.get(ProvideBuilder.class), providingType);
        TypeName provideBuilderList = ParameterizedTypeName.get(ClassName.get(Collection.class), providingType);
        String listFieldName = genLocalFieldName();
        List<FieldDetail> localVariables = new LinkedList<>(declaredFields);

        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.add("new $T( ( $L ) -> { \n", provideBuilder, listFieldName);

        for (InvokeCall inv : provideTypeInvokes) {
            boolean isCacheProvide = (inv.flags & INVOKE_PROVIDE_OBJECT_CACHED) != 0;
            boolean isSingleDepRequired = ListUtils.contains(provideDeps, (idx, it) ->
                    Objects.equals(nonWrappedType(it.typeName), nonWrappedType(inv.resultType()))
                            && !isList(it.typeName)
            );
            boolean isListDepRequired = ListUtils.contains(provideDeps, (idx, it) ->
                    Objects.equals(nonWrappedType(it.typeName), nonWrappedType(inv.resultType()))
                            && isList(it.typeName)
            );
            FieldDetail singleDepField = FieldDetail.simple(genLocalFieldName(), inv.resultType());
            singleDepField.qualifierAnns = inv.qualifierAnnotations(true);

            FieldDetail listDepField = FieldDetail.simple(
                    genLocalFieldName(),
                    ParameterizedTypeName.get(ClassName.get(Ref.class), ParameterizedTypeName.get(ClassName.get(List.class), inv.resultType())));
            listDepField.qualifierAnns = inv.qualifierAnnotations(true);


            if (isSingleDepRequired) {
                if (isCacheProvide) {
                    codeBlock.add("$T $L = ", inv.resultType(), singleDepField.name)
                            .addStatement(
                                    transform(
                                            inv.best().rawReturnType(),
                                            inv.resultType(),
                                            inv.best().invokeCode(localVariables))
                            );


                    localVariables.add(singleDepField);
                } else {
                    singleDepField.type = ParameterizedTypeName.get(ClassName.get(Ref.class), inv.resultType());
                    codeBlock.add("$T $L = () -> ", singleDepField.type, singleDepField.name)
                            .addStatement(transform(inv.best().rawReturnType(), inv.resultType(), inv.best().invokeCode(localVariables)));

                    localVariables.add(singleDepField);
                }
            }

            if (isListDepRequired) {
                codeBlock.add("$T $L = () -> ", listDepField.type, listDepField.name)
                        .addStatement(inv.invokeAllToList(localVariables));
                localVariables.add(listDepField);
            }


            if (Objects.equals(inv.resultType(), providingType)) {
                if (isList(returnType)) {
                    codeBlock.add("$L.addAll( $L );\n", listFieldName,
                            transform(
                                    listDepField.type,
                                    provideBuilderList,
                                    CodeBlock.of(listDepField.name)
                            )
                    );
                } else {
                    codeBlock.add("$L.add( $L );\n", listFieldName,
                            transform(
                                    singleDepField.type,
                                    providingType,
                                    CodeBlock.of(singleDepField.name)
                            )
                    );
                }
                if (!isList(returnType))
                    break;
            }
        }

        codeBlock.add("\n  })");
        if (isList(returnType)) {
            codeBlock.add(".all() ");

            return transform(
                    ParameterizedTypeName.get(ClassName.get(List.class), providingType),
                    returnType,
                    codeBlock.build()
            );
        } else {
            codeBlock.add(".first() ");

            return transform(
                    providingType,
                    returnType,
                    codeBlock.build()
            );
        }

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
                                .build()
                );
            }

            boolean isBindInstanceInvoke = (invokeCall.flags & INVOKE_PROVIDE_BIND_INSTANCE) != 0;
            List<ProvideDep> newDeps = ListUtils.filter(invokeCall.argDeps(), (i, it) -> {
                if (isBindInstanceInvoke && Objects.equals(rawDep.typeName, it.typeName)) {
                    // bind instance case. Argument and return type are equals
                    return false;
                }
                // qualifies not need to provide
                TypeName argNonWrapped = nonWrappedType(it.typeName);
                return argNonWrapped instanceof ClassName && !allClassesHelper.allIdentifiers.contains(argNonWrapped);
            });

            needProvideDeps.addAll(newDeps);
            needProvideDeps = ListUtils.removeDoublesRight(needProvideDeps, Objects::equals);
            boolean recursiveDetected = !newDeps.isEmpty() && needProvideDepsRecursiveDetector.next(needProvideDeps.hashCode());
            if (recursiveDetected) {
                throw new RecursiveProviding(
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
            provideTypeInvokes = ListUtils.removeDoublesRight(provideTypeInvokes, (it1, it2) -> {
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
                ? ListUtils.filter(invokeCalls, (i, it) -> Objects.equals(it.qualifierAnnotations(false), qualifierAnns))
                : new LinkedList<>(invokeCalls);

        filtered = provideMethodName != null ? ListUtils.filter(filtered, (i, it) -> {
            int len = it.bestSequence().size();
            String mName = it.bestSequence().get(len - 1).methodName;
            return Objects.equals(provideMethodName, mName);
        }) : filtered;

        if (!listVariants && filtered.size() > 1) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .errorProvideType(typeName.toString())
                            .add(": is bound multi times.\n")
                            .add(String.join(" and \n", ListUtils.format(filtered, InvokeCall::toString)))
                            .build());
        }
        return !filtered.isEmpty() ? new InvokeCall(filtered) : null;
    }

}
