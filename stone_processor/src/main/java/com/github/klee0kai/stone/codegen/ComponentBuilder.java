package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.annotations.component.GcAllScope;
import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.closed.IModule;
import com.github.klee0kai.stone.closed.types.*;
import com.github.klee0kai.stone.codegen.helpers.*;
import com.github.klee0kai.stone.codegen.model.WrapperCreatorField;
import com.github.klee0kai.stone.exceptions.ObjectNotProvided;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.SwitchCacheAnnotation;
import com.github.klee0kai.stone.types.wrappers.RefCollection;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.*;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;

public class ComponentBuilder {

    public final ClassDetail orComponentCl;

    public ClassName className;

    public static final String refCollectionGlFieldName = "__refCollection";
    public static final String scheduleGlFieldName = "__scheduler";
    public static final String provideWrappersGlFieldPrefixName = "__wrapperCreator";
    public static final String hiddenModuleFieldName = "__hiddenModule";

    public static final String hiddenModuleMethodName = "hidden";
    public static final String initMethodName = "init";
    public static final String bindMethodName = "bind";
    public static final String extOfMethodName = "extOf";

    public final Set<TypeName> interfaces = new HashSet<>();
    public final Set<ClassName> qualifiers = new HashSet<>();

    // ---------------------- common fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> fields = new HashMap<>();
    public final LinkedList<WrapperCreatorField> wrapperCreatorFields = new LinkedList<>();
    public final HashMap<String, MethodSpec.Builder> iComponentMethods = new HashMap<>();

    public final CodeBlock.Builder initModuleCode = CodeBlock.builder();
    public final CodeBlock.Builder bindModuleCode = CodeBlock.builder();


    // ---------------------- provide fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> modulesFields = new HashMap<>();
    public final HashMap<String, MethodSpec.Builder> modulesMethods = new HashMap<>();
    public final List<MethodSpec.Builder> provideObjMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> bindInstanceMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> injectMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> protectInjectedMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> gcMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> switchRefMethods = new LinkedList<>();


    private final LinkedList<Runnable> collectRuns = new LinkedList<>();
    private ModuleBuilder moduleHiddenBuilder = null;
    private final ModulesGraph modulesGraph = new ModulesGraph();
    private final LinkedList<ModuleFieldHelper> moduleFieldHelpers = new LinkedList<>();


    public static ComponentBuilder from(ClassDetail component) {
        ComponentBuilder componentBuilder = new ComponentBuilder(component, ClassNameUtils.genComponentNameMirror(component.className));
        componentBuilder.implementIComponentMethods();
        componentBuilder.qualifiers.addAll(component.componentAnn.qualifiers);
        return componentBuilder;
    }

    public ComponentBuilder(ClassDetail orComponentCl, ClassName className) {
        this.orComponentCl = orComponentCl;
        this.className = className;
    }

    /**
     * Call first
     */
    public ComponentBuilder implementIComponentMethods() {
        interfaces.add(ClassName.get(IComponent.class));
        initMethod(true);
        bindMethod(true);
        extOfMethod(true);
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

    public ComponentBuilder addProvideWrapperField(ClassDetail provideWrappersCl) {
        String name = provideWrappersGlFieldPrefixName + wrapperCreatorFields.size();
        wrapperCreatorFields.add(new WrapperCreatorField(
                name,
                provideWrappersCl.wrapperCreatorsAnn.wrappers,
                FieldSpec.builder(provideWrappersCl.className, name, Modifier.PRIVATE, Modifier.FINAL)
                        .initializer("new $T()", provideWrappersCl.className)
        ));
        return this;
    }

    public ComponentBuilder initMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(initMethodName)
                .addModifiers(Modifier.SYNCHRONIZED, Modifier.PUBLIC)
                .addParameter(Object[].class, "modules")
                .varargs(true);
        if (override) builder.addAnnotation(Override.class);

        iComponentMethods.put(initMethodName, builder);

        collectRuns.add(() -> {
            builder.beginControlFlow("for (Object m : modules)").addCode(initModuleCode.build()).endControlFlow();
        });
        return this;
    }


    public ComponentBuilder bindMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(bindMethodName)
                .addModifiers(Modifier.SYNCHRONIZED, Modifier.PUBLIC)
                .addParameter(Object[].class, "objects")
                .varargs(true);
        if (override) builder.addAnnotation(Override.class);

        iComponentMethods.put(bindMethodName, builder);

        collectRuns.add(() -> {
            builder.beginControlFlow("for (Object ob : objects)").addCode(bindModuleCode.build()).endControlFlow();
        });
        return this;
    }

    public ComponentBuilder extOfMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(extOfMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(IComponent.class, "c");
        if (override) builder.addAnnotation(Override.class);

        for (ClassDetail proto : orComponentCl.getAllParents(false)) {
            if (Objects.equals(proto.className, ClassName.get(IComponent.class))) continue;
            List<MethodDetail> provideModuleMethods = ListUtils.filter(proto.getAllMethods(false, false),
                    (i, m) -> ComponentMethods.isModuleProvideMethod(m)
            );
            if (provideModuleMethods.isEmpty()) continue;

            List<String> provideFactories = ListUtils.format(provideModuleMethods, (it) -> it.methodName + "()");

            builder.beginControlFlow("if (c instanceof $T)", proto.className)
                    .addStatement("$T protoComponent = ($T) c", proto.className, proto.className);
            for (MethodDetail provideModule : provideModuleMethods) {
                builder.addStatement(
                        "$L().initCachesFrom( ($T) protoComponent.$L() )",
                        provideModule.methodName, IModule.class, provideModule.methodName
                );
            }

            builder.addStatement("c.init( $L ) ", String.join(",", provideFactories))
                    .endControlFlow();
        }

        iComponentMethods.put(extOfMethodName, builder);
        return this;
    }

    public ComponentBuilder provideModuleMethod(String name, ClassDetail module) {
        ClassName moduleStoneMirror = ClassNameUtils.genModuleNameMirror(module.className);
        modulesFields.put(name, FieldSpec.builder(moduleStoneMirror, name, Modifier.PRIVATE).initializer("new $T()", moduleStoneMirror));
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(moduleStoneMirror)
                .addStatement("return this.$L", name);
        modulesMethods.put(name, builder);

        initModuleCode.addStatement("this.$L.init(m)", name);
        bindModuleCode.addStatement("this.$L.bind(ob)", name);
        modulesGraph.addModule(MethodDetail.simpleName(name), module, qualifiers);
        moduleFieldHelpers.add(new ModuleFieldHelper(name));
        return this;
    }

    public ComponentBuilder provideHiddenModuleMethod() {
        String name = hiddenModuleFieldName;

        ClassName tpName = ClassNameUtils.genHiddenModuleNameMirror(orComponentCl.className);
        moduleHiddenBuilder = new ModuleBuilder(null, tpName);
        moduleHiddenBuilder.qualifiers.addAll(qualifiers);
        moduleHiddenBuilder.implementIModule();
        modulesFields.put(name, FieldSpec.builder(tpName, name, Modifier.PROTECTED)
                .initializer(CodeBlock.of("new $T()", tpName)));
        modulesMethods.put(hiddenModuleMethodName,
                MethodSpec.methodBuilder(hiddenModuleMethodName)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(tpName)
                        .addStatement("return this.$L", name)
        );

        initModuleCode.addStatement("this.$L.init(m)", name);
        bindModuleCode.addStatement("this.$L.bind(ob)", name);
        moduleFieldHelpers.add(new ModuleFieldHelper(name));
        return this;
    }

    public ComponentBuilder provideObjMethod(String name, TypeName providingType, List<FieldDetail> args) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(providingType);

        for (FieldDetail arg : args)
            builder.addParameter(ParameterSpec.builder(arg.type, arg.name).build());
        List<FieldDetail> qFields = ListUtils.filter(args,
                (inx, it) -> (it.type instanceof ClassName) && qualifiers.contains(it.type)
        );

        provideObjMethods.add(builder);
        collectRuns.add(() -> {
            IProvideTypeWrapperHelper provideTypeWrapperHelper = IProvideTypeWrapperHelper.findHelper(providingType, wrapperCreatorFields);
            CodeBlock codeBlock = modulesGraph.codeProvideType(null, provideTypeWrapperHelper.providingType(), qFields);
            if (codeBlock == null) {
                throw new ObjectNotProvided(
                        provideTypeWrapperHelper.providingType(),
                        className,
                        name
                );
            }
            builder.addStatement(
                    "return $L",
                    provideTypeWrapperHelper.provideCode(codeBlock)
            );
        });
        return this;
    }


    public ComponentBuilder bindInstanceMethod(MethodDetail m) {
        List<FieldDetail> qFields = ListUtils.filter(m.args,
                (inx, it) -> (it.type instanceof ClassName) && qualifiers.contains(it.type)
        );
        boolean isProvideMethod = !(m.returnType.isPrimitive() || m.returnType.isBoxedPrimitive() || Objects.equals(m.returnType, TypeName.VOID));
        FieldDetail setValueArg = isProvideMethod ? ListUtils.first(m.args, (inx, ob) -> Objects.equals(ob.type, m.returnType))
                : ListUtils.first(m.args, (inx, it) -> (it.type instanceof ClassName) && !qualifiers.contains(it.type));

        MethodSpec.Builder builder = MethodSpec.methodBuilder(m.methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(m.returnType);
        if (m.args != null) for (FieldDetail p : m.args) {
            builder.addParameter(p.type, p.name);
        }

        if (isProvideMethod && modulesGraph.codeProvideType(m.methodName, m.returnType, qFields) == null) {
            //  bind object not declared in module
            ModuleBuilder moduleBuilder = getOrCreateHiddenModuleBuilder();
            ItemHolderCodeHelper.ItemCacheType cacheType = ItemHolderCodeHelper.cacheTypeFrom(m.bindInstanceAnnotation.cacheType);
            ItemHolderCodeHelper itemHolderCodeHelper = ItemHolderCodeHelper.of(m.methodName + moduleBuilder.cacheFields.size(), m.returnType, qFields, cacheType);
            moduleBuilder.bindInstance(m, itemHolderCodeHelper)
                    .cacheControl(m, itemHolderCodeHelper)
                    .switchRefFor(itemHolderCodeHelper,
                            ListUtils.setOf(
                                    m.gcScopeAnnotations,
                                    ClassName.get(GcAllScope.class),
                                    cacheType.getGcScopeClassName()
                            ));
        }

        collectRuns.add(() -> {
            // bind object declared in module
            if (setValueArg != null) {
                builder.addStatement(
                        modulesGraph.codeControlCacheForType(m.methodName, setValueArg.type, qFields,
                                CodeBlock.of(
                                        "$T.setIfNullValueAction( $L )",
                                        CacheAction.class, setValueArg.name
                                ))
                );
            }

            if (isProvideMethod) {
                CodeBlock provideCode = modulesGraph.codeProvideType(m.methodName, m.returnType, qFields);
                builder.addStatement("return $L", provideCode);
            }

        });
        bindInstanceMethods.add(builder);
        return this;
    }

    public ComponentBuilder injectMethod(String name, List<FieldDetail> args) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        for (FieldDetail arg : args)
            builder.addParameter(ParameterSpec.builder(arg.type, arg.name).build());
        List<FieldDetail> qFields = ListUtils.filter(args, (inx, it) -> (it.type instanceof ClassName) && qualifiers.contains(it.type));
        FieldDetail lifeCycleOwner = ListUtils.first(args, (inx, it) -> allClassesHelper.isLifeCycleOwner(it.type));


        List<FieldDetail> injectableFields = ListUtils.filter(args, (inx, it) -> (it.type instanceof ClassName) && !qualifiers.contains(it.type));

        if (injectableFields.isEmpty())
            //todo throw error
            return this;

        if (lifeCycleOwner != null) timeHolderFields();

        injectMethods.add(builder);
        collectRuns.add(() -> {
            for (FieldDetail injectableField : injectableFields) {
                ClassDetail injectableCl = allClassesHelper.findForType(injectableField.type);

                for (FieldDetail injectField : injectableCl.getAllFields()) {
                    if (!injectField.injectAnnotation) continue;

                    SetFieldHelper setFieldHelper = new SetFieldHelper(injectField, injectableCl);
                    IProvideTypeWrapperHelper provideTypeWrapperHelper = IProvideTypeWrapperHelper.findHelper(injectField.type, wrapperCreatorFields);
                    CodeBlock codeBlock = modulesGraph.codeProvideType(null, provideTypeWrapperHelper.providingType(), qFields);
                    if (codeBlock == null)
                        throw new ObjectNotProvided(
                                provideTypeWrapperHelper.providingType(),
                                injectableCl.className,
                                injectField.name
                        );

                    builder.addStatement(
                            "$L.$L",
                            injectableField.name,
                            setFieldHelper.codeSetField(provideTypeWrapperHelper.provideCode(codeBlock))
                    );
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
                        IProvideTypeWrapperHelper injectHelper = IProvideTypeWrapperHelper.findHelper(injectField.type, wrapperCreatorFields);
                        if (injectHelper.isGenerateWrapper())
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

        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(injectableCl.className, "cl").build())
                .returns(void.class);

        protectInjectedMethods.add(builder);
        collectRuns.add(() -> {
            for (FieldDetail injectField : injectableCl.fields) {
                if (!injectField.injectAnnotation) continue;
                IProvideTypeWrapperHelper injectHelper = IProvideTypeWrapperHelper.findHelper(injectField.type, wrapperCreatorFields);
                if (injectHelper.isGenerateWrapper())
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

    public ComponentBuilder gcMethod(String name, List<TypeName> gcScopes) {
        CodeBlock.Builder scopesCode = CodeBlock.builder();
        int inx = 0;
        for (TypeName sc : gcScopes) {
            if (inx++ > 0) scopesCode.add(", ");
            scopesCode.add("$T.class", sc);
        }

        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addStatement(
                        "$T scopes = new $T($T.asList( $L ))",
                        ParameterizedTypeName.get(Set.class, Class.class),
                        ParameterizedTypeName.get(HashSet.class, Class.class),
                        Arrays.class,
                        scopesCode.build()
                )
                .addStatement("$T toWeak = $T.toWeak()", SwitchCacheParam.class, SwitchCacheParam.class)
                .addStatement("$T toDef = $T.toDef()", SwitchCacheParam.class, SwitchCacheParam.class);


        gcMethods.add(builder);
        collectRuns.add(() -> {
            for (String module : modulesFields.keySet()) {
                builder.addStatement("this.$L.switchRef(scopes, toWeak)", module);
            }

            builder.addStatement("$T.gc()", System.class);

            if (fields.containsKey(refCollectionGlFieldName))
                builder.addStatement("$L.clearNulls()", refCollectionGlFieldName);
            for (String module : modulesFields.keySet()) {
                builder.addStatement("this.$L.switchRef(scopes, toDef)", module);
            }
        });
        return this;
    }


    public ComponentBuilder switchRefMethod(String name, SwitchCacheAnnotation switchCacheAnnotation, List<TypeName> gcScopes) {
        CodeBlock.Builder scopesCode = CodeBlock.builder();
        int inx = 0;
        for (TypeName sc : gcScopes)
            if (inx++ <= 0) scopesCode.add("$T.class", sc);
            else scopesCode.add(", $T.class", sc);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addStatement(
                        "$T scopes = new $T($T.asList( $L ))",
                        ParameterizedTypeName.get(Set.class, Class.class),
                        ParameterizedTypeName.get(HashSet.class, Class.class),
                        Arrays.class,
                        scopesCode.build()
                );


        CodeBlock.Builder schedulerInitCode = CodeBlock.builder();
        if (switchCacheAnnotation.timeMillis > 0) {
            timeHolderFields();
            schedulerInitCode.add("this.$L", scheduleGlFieldName);
        } else {
            schedulerInitCode.add("null");
        }
        builder.addStatement(
                "$T switchCacheParams = new $T( $T.$L , $L, $L )",
                SwitchCacheParam.class, SwitchCacheParam.class,
                SwitchCache.CacheType.class, switchCacheAnnotation.cache.name(),
                switchCacheAnnotation.timeMillis,
                schedulerInitCode.build()
        );


        switchRefMethods.add(builder);
        collectRuns.add(() -> {
            for (ModuleFieldHelper moduleFieldHelper : moduleFieldHelpers)
                builder.addCode(moduleFieldHelper.statementSwitchRefs(
                        "scopes", "switchCacheParams"
                ));
        });
        return this;
    }


    /**
     * Collect all params. prebuild
     *
     * @return
     */
    public ComponentBuilder collect() {
        for (Runnable r : collectRuns)
            r.run();

        collectRuns.clear();
        return this;
    }

    public TypeSpec build() {
        collect();

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className);
        typeSpecBuilder.addModifiers(Modifier.PUBLIC);
        if (orComponentCl.isInterfaceClass()) typeSpecBuilder.addSuperinterface(orComponentCl.className);
        else typeSpecBuilder.superclass(orComponentCl.className);

        for (TypeName supInterface : interfaces)
            typeSpecBuilder.addSuperinterface(supInterface);

        for (FieldSpec.Builder field : fields.values())
            typeSpecBuilder.addField(field.build());

        for (WrapperCreatorField field : wrapperCreatorFields)
            typeSpecBuilder.addField(field.fieldBuilder.build());

        for (FieldSpec.Builder field : modulesFields.values())
            typeSpecBuilder.addField(field.build());

        List<MethodSpec.Builder> methodBuilders = new LinkedList<>();
        methodBuilders.addAll(iComponentMethods.values());
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
        if (moduleHiddenBuilder != null) {
            TypeSpec typeSpec = moduleHiddenBuilder.buildAndWrite();
            modulesGraph.addModule(
                    MethodDetail.simpleName(hiddenModuleMethodName),
                    ClassDetail.of(moduleHiddenBuilder.className.packageName(), typeSpec),
                    qualifiers
            );
        }

        TypeSpec typeSpec = build();
        if (typeSpec != null) {
            CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
        }

        return typeSpec;
    }


    private ModuleBuilder getOrCreateHiddenModuleBuilder() {
        if (moduleHiddenBuilder == null) provideHiddenModuleMethod();


        return moduleHiddenBuilder;
    }

}
