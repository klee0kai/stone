package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.checks.ComponentMethods;
import com.github.klee0kai.stone.closed.IModule;
import com.github.klee0kai.stone.closed.IPrivateComponent;
import com.github.klee0kai.stone.closed.types.*;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.exceptions.ObjectNotProvidedException;
import com.github.klee0kai.stone.helpers.SetFieldHelper;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.helpers.invokecall.InvokeCall;
import com.github.klee0kai.stone.helpers.wrap.WrapHelper;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.ComponentClassDetails;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.*;
import com.github.klee0kai.stone.types.wrappers.RefCollection;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.github.klee0kai.stone.utils.ImplementMethodCollection;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.*;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;
import static com.github.klee0kai.stone.checks.ComponentMethods.BindInstanceType.BindInstanceAndProvide;
import static com.github.klee0kai.stone.checks.ComponentMethods.*;
import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;
import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.*;
import static com.github.klee0kai.stone.utils.StoneNamingUtils.*;
import static com.squareup.javapoet.MethodSpec.methodBuilder;

public class ComponentBuilder {

    public final ComponentClassDetails orComponentCl;

    public ClassName className;

    public static final String refCollectionGlFieldName = "__refCollection";
    public static final String scheduleGlFieldName = "__scheduler";
    public static final String hiddenModuleFieldName = "__hiddenModule";
    public static final String relatedComponentsListFieldName = "__related";
    public static final String protectRecursiveField = "__protectRecursive";
    public static final String hiddenModuleMethodName = "__hidden";
    public static final String eachModuleMethodName = "__eachModule";
    public static final String initMethodName = "__init";
    public static final String initDepsMethodName = "__initDependencies";
    public static final String bindMethodName = "__bind";
    public static final String extOfMethodName = "__extOf";

    public final Set<TypeName> interfaces = new HashSet<>();

    // ---------------------- common fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> fields = new HashMap<>();
    public final List<MethodSpec.Builder> iComponentMethods = new LinkedList<>();

    public final CodeBlock.Builder initDepsCode = CodeBlock.builder();
    public final CodeBlock.Builder bindModuleCode = CodeBlock.builder();


    // ---------------------- provide fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> modulesFields = new HashMap<>();
    public final HashMap<String, FieldSpec.Builder> depsFields = new HashMap<>();
    public final HashMap<String, MethodSpec.Builder> modulesMethods = new HashMap<>();
    public final HashMap<String, MethodSpec.Builder> depsMethods = new HashMap<>();
    public final List<MethodSpec.Builder> provideObjMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> bindInstanceMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> injectMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> protectInjectedMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> gcMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> switchRefMethods = new LinkedList<>();


    private final ImplementMethodCollection collectRuns = new ImplementMethodCollection();

    public static ComponentBuilder from(ComponentClassDetails component) {
        ComponentBuilder componentBuilder = new ComponentBuilder(component, genComponentNameMirror(component.className))
                .implementIComponentMethods();


        for (MethodDetail m : component.getAllMethods(false, false, "<init>")) {
            if (allClassesHelper.iComponentClassDetails.findMethod(m, false) != null)
                continue;

            if (isModuleProvideMethod(m)) {
                ClassDetail moduleCl = allClassesHelper.findForType(m.returnType);
                componentBuilder.provideModuleMethod(m.methodName, moduleCl);
            } else if (isDepsProvide(m)) {
                ClassDetail dependencyCl = allClassesHelper.findForType(m.returnType);
                componentBuilder.provideDependenciesMethod(m.methodName, dependencyCl);
            } else if (isInitModuleMethod(m)) {
                componentBuilder.initMethod(m);
            } else if (isExtOfMethod(component, m)) {
                componentBuilder.extOfMethod(m);
            } else if (isObjectProvideMethod(m)) {
                componentBuilder.provideObjMethod(m);
            } else if (isBindInstanceMethod(m) != null) {
                componentBuilder.bindInstanceMethod(m);
            } else if (isGcMethod(m)) {
                componentBuilder.gcMethod(m);
            } else if (isSwitchCacheMethod(m)) {
                componentBuilder.switchRefMethod(m);
            } else if (isInjectMethod(m)) {
                componentBuilder.injectMethod(m);
            } else if (isProtectInjectedMethod(m)) {
                componentBuilder.protectInjectedMethod(
                        m.methodName,
                        allClassesHelper.findForType(m.args.get(0).type),
                        m.ann(ProtectInjectedAnn.class).timeMillis
                );
            } else if (component.isInterfaceClass() || m.isAbstract()) {
                //non implemented method
                throw new IncorrectSignatureException(
                        createErrorMes()
                                .methodPurposeNonDetected(m.methodName, component.className.toString())
                                .build()
                );
            }
        }
        return componentBuilder;
    }

    public ComponentBuilder(ComponentClassDetails orComponentCl, ClassName className) {
        this.orComponentCl = orComponentCl;
        this.className = className;
    }

    /**
     * Call first
     */
    public ComponentBuilder implementIComponentMethods() {
        interfaces.add(ClassName.get(IPrivateComponent.class));
        ParameterizedTypeName weakComponentsList = ParameterizedTypeName.get(WeakLinkedList.class, IPrivateComponent.class);
        fields.put(
                relatedComponentsListFieldName,
                FieldSpec.builder(weakComponentsList, relatedComponentsListFieldName, Modifier.FINAL, Modifier.PRIVATE)
                        .initializer("new $T()", weakComponentsList)
        );
        fields.put(
                protectRecursiveField,
                FieldSpec.builder(boolean.class, protectRecursiveField, Modifier.PRIVATE)
                        .initializer("false")
        );


        // IComponent
        initMethod(true);
        initDependenciesMethod(true);
        bindMethod(true);
        extOfMethod(true);
        // IPrivateComponent
        hiddenModuleMethod(true);
        eachModuleMethod(true);
        return this;
    }


    public ComponentBuilder timeHolderFields() {
        if (!fields.containsKey(scheduleGlFieldName))
            fields.put(
                    scheduleGlFieldName,
                    FieldSpec.builder(TimeScheduler.class, scheduleGlFieldName, Modifier.PRIVATE, Modifier.FINAL)
                            .initializer("new $T()", TimeScheduler.class)
            );
        if (!fields.containsKey(refCollectionGlFieldName))
            fields.put(
                    refCollectionGlFieldName,
                    FieldSpec.builder(RefCollection.class, refCollectionGlFieldName, Modifier.PRIVATE, Modifier.FINAL)
                            .initializer("new $T()", RefCollection.class)
            );
        return this;
    }

    public ComponentBuilder initMethod(boolean override) {
        MethodSpec.Builder builder = methodBuilder(initMethodName)
                .addModifiers(Modifier.SYNCHRONIZED, Modifier.PUBLIC)
                .addParameter(Object[].class, "modules")
                .varargs(true);
        if (override) builder.addAnnotation(Override.class);

        iComponentMethods.add(builder);

        collectRuns.execute(null, () -> {
            builder.beginControlFlow("for (Object m : modules)")
                    .beginControlFlow("if (m instanceof $T)", IPrivateComponent.class)
                    .addComment("related component")
                    .addStatement("$L.add( ( $T ) m)", relatedComponentsListFieldName, IPrivateComponent.class)
                    .endControlFlow()
                    .beginControlFlow("else")
                    .addComment("init modules")
                    .addStatement(
                            "$L( (module) -> { module.$L(m); } )",
                            eachModuleMethodName, ModuleBuilder.initMethodName
                    )
                    .endControlFlow()
                    .endControlFlow();
        });
        return this;
    }


    public ComponentBuilder initDependenciesMethod(boolean override) {
        MethodSpec.Builder builder = methodBuilder(initDepsMethodName)
                .addModifiers(Modifier.SYNCHRONIZED, Modifier.PUBLIC)
                .addParameter(Object[].class, "deps")
                .varargs(true);
        if (override) builder.addAnnotation(Override.class);

        iComponentMethods.add(builder);

        collectRuns.execute(null, () -> {
            builder.beginControlFlow("for (Object m : deps)")
                    .addComment("init dependencies")
                    .addCode(initDepsCode.build())
                    .endControlFlow();
        });
        return this;
    }

    public ComponentBuilder initMethod(MethodDetail m) {
        MethodSpec.Builder builder = methodBuilder(m.methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);
        for (FieldDetail arg : m.args) {
            builder.addParameter(ParameterSpec.builder(arg.type, arg.name).build());
            ClassDetail iniCl = allClassesHelper.findForType(arg.type);
            if (Objects.equals(ClassNameUtils.rawTypeOf(arg.type), ClassName.get(Class.class))) {
                TypeName initType = ((ParameterizedTypeName) arg.type).typeArguments.get(0);
                iniCl = allClassesHelper.findForType(initType);
            }

            if (iniCl.hasAnyAnnotation(ModuleAnn.class, ComponentAnn.class)) {
                builder.addStatement("$L( $L )", initMethodName, arg.name);
            } else if (iniCl.hasAnyAnnotation(DependenciesAnn.class)) {
                builder.addStatement("$L( $L )", initDepsMethodName, arg.name);
            } else {
                throw new IncorrectSignatureException(
                        createErrorMes()
                                .method(m.methodName)
                                .hasIncorrectSignature()
                                .build()
                );
            }
        }

        iComponentMethods.add(builder);
        return this;
    }

    public ComponentBuilder bindMethod(boolean override) {
        MethodSpec.Builder builder = methodBuilder(bindMethodName)
                .addModifiers(Modifier.SYNCHRONIZED, Modifier.PUBLIC)
                .addParameter(Object[].class, "objects")
                .varargs(true);
        if (override) builder.addAnnotation(Override.class);

        iComponentMethods.add(builder);
        collectRuns.execute(null, () -> {
            builder.beginControlFlow("for (Object ob : objects)")
                    .addStatement(
                            "$L( (m) -> {  m.$L(ob); } )",
                            eachModuleMethodName, ModuleBuilder.bindMethodName
                    )
                    .endControlFlow();
        });
        return this;
    }

    public ComponentBuilder extOfMethod(boolean override) {
        MethodSpec.Builder builder = methodBuilder(extOfMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(IPrivateComponent.class, "c");
        if (override) builder.addAnnotation(Override.class);

        for (ClassDetail proto : orComponentCl.getAllParents(false)) {
            if (Objects.equals(proto.className, ClassName.get(IPrivateComponent.class))) continue;
            List<MethodDetail> provideModuleMethods = ListUtils.filter(proto.getAllMethods(false, false),
                    (i, m) -> ComponentMethods.isModuleProvideMethod(m)
            );
            List<MethodDetail> bindInstanceAndProvideMethods = ListUtils.filter(proto.getAllMethods(false, false),
                    (i, m) -> ComponentMethods.isBindInstanceMethod(m) == BindInstanceAndProvide
            );

            if (provideModuleMethods.isEmpty() && bindInstanceAndProvideMethods.isEmpty()) continue;

            List<String> provideFactories = ListUtils.format(provideModuleMethods, (it) -> it.methodName + "()");

            builder.beginControlFlow("if (c instanceof $T)", proto.className)
                    .addStatement("$T protoComponent = ($T) c", proto.className, proto.className);
            for (MethodDetail provideModule : provideModuleMethods) {
                builder.addStatement(
                        "$L().$L( ($T) protoComponent.$L())",
                        provideModule.methodName,
                        ModuleBuilder.initCachesFromMethodName,
                        IModule.class, provideModule.methodName
                );
            }
            for (MethodDetail bindInstMethod : bindInstanceAndProvideMethods) {
                builder.addStatement(
                        "$L(protoComponent.$L(null))",
                        bindInstMethod.methodName, bindInstMethod.methodName
                );
            }

            builder.addStatement("c.$L( $L ) ", ModuleBuilder.initMethodName, String.join(",", provideFactories))
                    .addStatement("c.$L(this)", ModuleBuilder.initMethodName)
                    .endControlFlow();

            builder.beginControlFlow("if (c instanceof $T)", IPrivateComponent.class)
                    .addStatement(
                            "$L.add( ($T) c)",
                            relatedComponentsListFieldName, IPrivateComponent.class)
                    .endControlFlow();

        }

        iComponentMethods.add(builder);
        return this;
    }

    public ComponentBuilder extOfMethod(MethodDetail m) {
        MethodSpec.Builder builder = methodBuilder(m.methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);
        for (FieldDetail arg : m.args) {
            builder.addParameter(ParameterSpec.builder(arg.type, arg.name).build());
            builder.addStatement("$L( ($T) $L )", extOfMethodName, IPrivateComponent.class, arg.name);
        }

        iComponentMethods.add(builder);
        return this;
    }

    public ComponentBuilder hiddenModuleMethod(boolean override) {
        ClassName tpName = genHiddenModuleNameMirror(orComponentCl.className);
        MethodSpec.Builder builder = methodBuilder(hiddenModuleMethodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(tpName)
                .addStatement("return this.$L", hiddenModuleFieldName);
        if (override) builder.addAnnotation(Override.class);

        String name = hiddenModuleFieldName;
        modulesFields.put(name, FieldSpec.builder(tpName, name, Modifier.PRIVATE, Modifier.FINAL)
                .initializer(CodeBlock.of("new $T()", tpName)));

        bindModuleCode.addStatement("this.$L.bind(ob)", name);
        iComponentMethods.add(builder);
        return this;
    }

    public ComponentBuilder eachModuleMethod(boolean override) {
        ParameterizedTypeName callbackType = ParameterizedTypeName.get(StoneCallback.class, IModule.class);
        MethodSpec.Builder builder = methodBuilder(eachModuleMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(callbackType, "callback").build())
                .addStatement("if ($L) return ", protectRecursiveField)
                .addStatement("$L = true", protectRecursiveField);
        if (override) builder.addAnnotation(Override.class);

        collectRuns.execute(null, () -> {
            for (String module : modulesMethods.keySet()) {
                builder.addStatement("callback.invoke($L())", module);
            }
            if (modulesFields.containsKey(hiddenModuleFieldName)) {
                builder.addStatement("callback.invoke($L())", hiddenModuleMethodName);
            }

            builder.beginControlFlow("for ($T c: $L.toList())", IPrivateComponent.class, relatedComponentsListFieldName)
                    .addStatement("c.$L(callback)", eachModuleMethodName)
                    .endControlFlow();


            builder.addStatement("$L = false", protectRecursiveField);
        });
        iComponentMethods.add(builder);
        return this;
    }


    public ComponentBuilder provideModuleMethod(String name, ClassDetail module) {
        ClassName moduleStoneMirror = genModuleNameMirror(module.className);
        modulesFields.put(name, FieldSpec.builder(moduleStoneMirror, name, Modifier.PRIVATE).initializer("new $T()", moduleStoneMirror));
        MethodSpec.Builder builder = methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(moduleStoneMirror)
                .addStatement("return this.$L", name);
        modulesMethods.put(name, builder);

        bindModuleCode.addStatement("this.$L.bind(ob)", name);
        return this;
    }

    public ComponentBuilder provideDependenciesMethod(String name, ClassDetail depsModule) {
        depsFields.put(name, FieldSpec.builder(depsModule.className, name, Modifier.PRIVATE));
        MethodSpec.Builder builder = methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(depsModule.className)
                .addStatement("return this.$L", name);
        depsMethods.put(name, builder);

        initDepsCode.addStatement("if (m instanceof $T) this.$L = ( $T ) m", depsModule.className, name, depsModule.className);
        return this;
    }

    public ComponentBuilder provideObjMethod(MethodDetail m) {
        MethodSpec.Builder builder = methodBuilder(m.methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(m.returnType);

        for (FieldDetail arg : m.args)
            builder.addParameter(ParameterSpec.builder(arg.type, arg.name).build());
        List<FieldDetail> qFields = ListUtils.filter(m.args,
                (inx, it) -> (it.type instanceof ClassName) && orComponentCl.qualifiers.contains(it.type)
        );

        provideObjMethods.add(builder);
        collectRuns.execute(createErrorMes().errorImplementMethod(m.methodName).build(), () -> {
            SmartCode smartCode = orComponentCl.modulesGraph.codeProvideType(
                    m.methodName,
                    m.returnType,
                    qFields);
            if (smartCode == null) {
                throw new ObjectNotProvidedException(
                        createErrorMes()
                                .errorProvideTypeRequiredIn(
                                        m.returnType.toString(),
                                        orComponentCl.className.toString(),
                                        m.methodName
                                )
                                .build(),
                        null
                );
            }
            builder.addCode("return ")
                    .addCode(smartCode.build(qFields))
                    .addCode(";\n");
        });
        return this;
    }


    public ComponentBuilder bindInstanceMethod(MethodDetail m) {
        List<FieldDetail> qFields = ListUtils.filter(m.args,
                (inx, it) -> (it.type instanceof ClassName) && orComponentCl.qualifiers.contains(it.type)
        );
        FieldDetail setValueArg = ListUtils.first(m.args, (inx, it) -> !(it.type instanceof ClassName) || !orComponentCl.qualifiers.contains(it.type));
        TypeName nonWrappedBindType = nonWrappedType(setValueArg.type);
        boolean isProvideMethod = Objects.equals(nonWrappedType(m.returnType), nonWrappedBindType);

        MethodSpec.Builder builder = methodBuilder(m.methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(m.returnType);
        if (m.args != null) for (FieldDetail p : m.args) {
            builder.addParameter(p.type, p.name);
        }

        collectRuns.execute(createErrorMes().errorImplementMethod(m.methodName).build(), () -> {
            // bind object declared in module
            InvokeCall cacheControlInvoke = orComponentCl.modulesGraph.invokeControlCacheForType(m.methodName, nonWrappedBindType, qFields);
            boolean isListCache = isList(cacheControlInvoke.rawReturnType());
            TypeName cacheControlType = isListCache ? ParameterizedTypeName.get(ClassName.get(List.class), nonWrappedBindType) : nonWrappedBindType;
            builder.addStatement(cacheControlInvoke.invokeCode(qFields,
                            typeName -> SmartCode.builder()
                                    .add(CodeBlock.of("$T.setValueAction(", CacheAction.class))
                                    .add(transform(
                                            SmartCode.of(setValueArg.name).providingType(setValueArg.type),
                                            cacheControlType
                                    ))
                                    .add(")")
                                    .build(null)
                    ))
                    .addStatement(
                            "$L( (module) -> { module.$L( $L() ); } )",
                            eachModuleMethodName,
                            ModuleBuilder.updateBindInstancesFrom,
                            cacheControlInvoke.bestSequence().get(0).methodName
                    );


            if (isProvideMethod) {
                builder.addCode("return ")
                        .addCode(
                                transform(
                                        orComponentCl.modulesGraph.codeProvideType(m.methodName, nonWrappedBindType, qFields),
                                        m.returnType
                                ).build(m.args))
                        .addCode(";\n");
            }

        });
        bindInstanceMethods.add(builder);
        return this;
    }

    public ComponentBuilder injectMethod(MethodDetail m) {
        MethodSpec.Builder builder = methodBuilder(m.methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        for (FieldDetail arg : m.args)
            builder.addParameter(ParameterSpec.builder(arg.type, arg.name).build());
        List<FieldDetail> qFields = ListUtils.filter(m.args, (inx, it) -> (it.type instanceof ClassName) && orComponentCl.qualifiers.contains(it.type));
        FieldDetail lifeCycleOwner = ListUtils.first(m.args, (inx, it) -> allClassesHelper.isLifeCycleOwner(it.type));


        List<FieldDetail> injectableFields = ListUtils.filter(m.args, (inx, it) -> (it.type instanceof ClassName) && !orComponentCl.qualifiers.contains(it.type));

        if (injectableFields.isEmpty()) {
            throw new IncorrectSignatureException(
                    createErrorMes()
                            .method(m.methodName)
                            .shouldHaveInjectableClassAsParameter()
                            .build());
        }

        if (lifeCycleOwner != null) timeHolderFields();

        injectMethods.add(builder);
        collectRuns.execute(createErrorMes().errorImplementMethod(m.methodName).build(), () -> {
            for (FieldDetail injectableField : injectableFields) {
                ClassDetail injectableCl = allClassesHelper.findForType(injectableField.type);

                for (FieldDetail injectField : injectableCl.getAllFields()) {
                    if (!injectField.injectAnnotation) continue;
                    SetFieldHelper setFieldHelper = new SetFieldHelper(injectField, injectableCl);
                    SmartCode provideCode = orComponentCl.modulesGraph.codeProvideType(null, injectField.type, qFields);
                    if (provideCode == null) {
                        throw new ObjectNotProvidedException(
                                createErrorMes()
                                        .errorProvideTypeRequiredIn(
                                                injectField.type.toString(),
                                                injectableCl.className.toString(),
                                                injectField.name
                                        )
                                        .build(),
                                null
                        );
                    }
                    builder.addCode(injectableField.name)
                            .addCode(".")
                            .addCode(setFieldHelper.codeSetField(provideCode.build(qFields)))
                            .addCode(";\n");
                }

                for (MethodDetail injectMethod : injectableCl.getAllMethods(false, false, "<init>")) {
                    if (!injectMethod.hasAnnotations(InjectAnn.class)) continue;

                    CodeBlock.Builder providingArgsCode = CodeBlock.builder();
                    for (FieldDetail injectField : injectMethod.args) {
                        SmartCode provideCode = orComponentCl.modulesGraph.codeProvideType(null, injectField.type, qFields);
                        if (provideCode == null) {
                            throw new ObjectNotProvidedException(
                                    createErrorMes()
                                            .errorProvideTypeRequiredIn(
                                                    injectField.type.toString(),
                                                    injectableCl.className.toString(),
                                                    injectField.name
                                            )
                                            .build(),
                                    null
                            );
                        }

                        if (!providingArgsCode.isEmpty()) providingArgsCode.add(", ");
                        providingArgsCode.add(provideCode.build(m.args));
                    }

                    builder.addCode("$L.$L( ", injectableField.name, injectMethod.methodName)
                            .addCode(providingArgsCode.build())
                            .addCode("); \n");
                }

            }

            //protect by lifecycle owner
            for (FieldDetail injectableField : injectableFields) {
                ClassDetail injectableCl = allClassesHelper.findForType(injectableField.type);

                CodeBlock.Builder subscrCode = CodeBlock.builder();
                boolean emptyCode = true;
                if (lifeCycleOwner != null) {

                    subscrCode.beginControlFlow("$L.subscribe( (timeMillis) -> ", lifeCycleOwner.name);
                    for (FieldDetail injectField : injectableCl.getAllFields()) {
                        if (!injectField.injectAnnotation) continue;
                        if (WrapHelper.isNonCachingWrapper(injectField.type))
                            //nothing to protect
                            continue;

                        SetFieldHelper getFieldHelper = new SetFieldHelper(injectField, injectableCl);

                        emptyCode = false;
                        subscrCode.addStatement(
                                "$L.add(new $T($L, $L.$L , timeMillis))",
                                refCollectionGlFieldName, TimeHolder.class, scheduleGlFieldName,
                                injectableField.name, getFieldHelper.codeGetField()
                        );
                    }
                    subscrCode.endControlFlow(")");

                    if (!emptyCode) builder.addCode(subscrCode.build());
                }
            }
        });
        return this;
    }

    public ComponentBuilder protectInjectedMethod(String name, ClassDetail injectableCl, long timeMillis) {
        timeHolderFields();

        MethodSpec.Builder builder = methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(injectableCl.className, "cl").build())
                .returns(void.class);

        protectInjectedMethods.add(builder);
        collectRuns.execute(createErrorMes().errorImplementMethod(name).build(), () -> {
            for (FieldDetail injectField : injectableCl.fields) {
                if (!injectField.injectAnnotation) continue;
                if (WrapHelper.isNonCachingWrapper(injectField.type))
                    //nothing to protect
                    continue;

                SetFieldHelper getFieldHelper = new SetFieldHelper(injectField, injectableCl);

                builder.addStatement(
                        "$L.add(new $T($L, cl.$L , $L))",
                        refCollectionGlFieldName, TimeHolder.class, scheduleGlFieldName,
                        getFieldHelper.codeGetField(),
                        timeMillis
                );
            }
        });
        return this;
    }

    public ComponentBuilder gcMethod(MethodDetail m) {
        CodeBlock.Builder scopesCode = CodeBlock.builder();
        int inx = 0;
        for (TypeName sc : m.gcScopeAnnotations) {
            if (inx++ > 0) scopesCode.add(", ");
            scopesCode.add("$T.class", sc);
        }

        MethodSpec.Builder builder = methodBuilder(m.methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);


        builder.addStatement(
                        "$T scopes = new $T($T.asList( $L ))",
                        ParameterizedTypeName.get(Set.class, Class.class),
                        ParameterizedTypeName.get(HashSet.class, Class.class),
                        Arrays.class,
                        scopesCode.build()
                )
                .addStatement("$T toWeak = $T.toWeak()", SwitchCacheParam.class, SwitchCacheParam.class)
                .addStatement("$T toDef = $T.toDef()", SwitchCacheParam.class, SwitchCacheParam.class);


        gcMethods.add(builder);
        collectRuns.execute(createErrorMes().errorImplementMethod(m.methodName).build(), () -> {
            builder.addStatement(
                            "$L( (m) -> {  m.$L(scopes, toWeak); } )",
                            eachModuleMethodName, ModuleBuilder.switchRefMethodName
                    )
                    .addStatement("$T.gc()", System.class)
                    .addStatement("$L.clearNulls()", relatedComponentsListFieldName)
                    .addStatement(

                            "$L( (m) -> {  m.__clearNulls(); m.$L(scopes, toDef); } )",
                            eachModuleMethodName, ModuleBuilder.switchRefMethodName
                    );

            if (fields.containsKey(refCollectionGlFieldName))
                builder.addStatement("$L.clearNulls()", refCollectionGlFieldName);
        });
        return this;
    }


    public ComponentBuilder switchRefMethod(MethodDetail m) {
        CodeBlock.Builder scopesCode = CodeBlock.builder();
        int inx = 0;
        for (TypeName sc : m.gcScopeAnnotations)
            if (inx++ <= 0) scopesCode.add("$T.class", sc);
            else scopesCode.add(", $T.class", sc);

        MethodSpec.Builder builder = methodBuilder(m.methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);

        builder
                .addStatement(
                        "$T scopes = new $T($T.asList( $L ))",
                        ParameterizedTypeName.get(Set.class, Class.class),
                        ParameterizedTypeName.get(HashSet.class, Class.class),
                        Arrays.class,
                        scopesCode.build()
                );


        CodeBlock.Builder schedulerInitCode = CodeBlock.builder();
        if (m.ann(SwitchCacheAnn.class).timeMillis > 0) {
            timeHolderFields();
            schedulerInitCode.add("this.$L", scheduleGlFieldName);
        } else {
            schedulerInitCode.add("null");
        }
        builder.addStatement(
                "$T switchCacheParams = new $T( $T.$L , $L, $L )",
                SwitchCacheParam.class, SwitchCacheParam.class,
                SwitchCache.CacheType.class, m.ann(SwitchCacheAnn.class).cache.name(),
                m.ann(SwitchCacheAnn.class).timeMillis,
                schedulerInitCode.build()
        ).addStatement(
                "$L( (m) -> {  m.$L(scopes, switchCacheParams); } )",
                eachModuleMethodName, ModuleBuilder.switchRefMethodName
        );

        switchRefMethods.add(builder);
        return this;
    }

    public TypeSpec build() {
        collectRuns.executeAll();

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className);
        typeSpecBuilder.addModifiers(Modifier.PUBLIC);
        if (orComponentCl.isInterfaceClass()) typeSpecBuilder.addSuperinterface(orComponentCl.className);
        else typeSpecBuilder.superclass(orComponentCl.className);

        for (TypeName supInterface : interfaces)
            typeSpecBuilder.addSuperinterface(supInterface);

        for (FieldSpec.Builder field : fields.values())
            typeSpecBuilder.addField(field.build());


        for (FieldSpec.Builder field : depsFields.values())
            typeSpecBuilder.addField(field.build());

        for (FieldSpec.Builder field : modulesFields.values())
            typeSpecBuilder.addField(field.build());


        List<MethodSpec.Builder> methodBuilders = new LinkedList<>();
        methodBuilders.addAll(iComponentMethods);
        methodBuilders.addAll(depsMethods.values());
        methodBuilders.addAll(modulesMethods.values());
        methodBuilders.addAll(provideObjMethods);
        methodBuilders.addAll(bindInstanceMethods);
        methodBuilders.addAll(injectMethods);
        methodBuilders.addAll(protectInjectedMethods);
        methodBuilders.addAll(switchRefMethods);
        methodBuilders.addAll(gcMethods);

        for (MethodSpec.Builder method : methodBuilders)
            typeSpecBuilder.addMethod(method.build());

        return typeSpecBuilder.build();
    }

    public TypeSpec buildAndWrite() {
        TypeSpec typeSpec = build();
        if (typeSpec != null) {
            CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
        }

        return typeSpec;
    }

}
