package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.closed.types.TimeHolder;
import com.github.klee0kai.stone.closed.types.TimeScheduler;
import com.github.klee0kai.stone.codegen.helpers.ComponentInjectGraph;
import com.github.klee0kai.stone.codegen.helpers.IProvideTypeWrapperHelper;
import com.github.klee0kai.stone.codegen.helpers.ModuleFieldHelper;
import com.github.klee0kai.stone.codegen.helpers.SetFieldHelper;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.SwitchCacheAnnotation;
import com.github.klee0kai.stone.types.RefCollection;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.util.*;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;

public class ComponentBuilder {

    public final ClassDetail orComponentCl;

    public ClassName className;

    public static final String refCollectionGlFieldName = "__refCollection";
    public static final String scheduleGlFieldName = "__scheduler";

    public static final String initMethodName = "init";
    public static final String bindMethodName = "bind";
    public static final String extOfMethodName = "extOf";

    public final Set<TypeName> interfaces = new HashSet<>();

    public final Set<ClassName> qualifiers = new HashSet<>();

    // ---------------------- common fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> fields = new HashMap<>();
    public final HashMap<String, MethodSpec.Builder> iComponentMethods = new HashMap<>();

    public final CodeBlock.Builder initModuleCode = CodeBlock.builder();
    public final CodeBlock.Builder bindModuleCode = CodeBlock.builder();


    // ---------------------- provide fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> modulesFields = new HashMap<>();
    public final HashMap<String, MethodSpec.Builder> modulesMethods = new HashMap<>();
    public final List<MethodSpec.Builder> provideObjMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> injectMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> protectInjectedMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> gcMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> switchRefMethods = new LinkedList<>();


    private final LinkedList<Runnable> collectRuns = new LinkedList<>();
    private final ComponentInjectGraph injectGraph = new ComponentInjectGraph();
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

        for (ClassDetail par : orComponentCl.getAllParents(false)) {
            if (Objects.equals(par.className, ClassName.get(IComponent.class))) continue;
            List<MethodDetail> provideModuleMethods = ListUtils.filter(par.getAllMethods(false), (i, it) -> it.returnType != TypeName.VOID && !it.returnType.isPrimitive());
            if (provideModuleMethods.isEmpty()) continue;

            List<String> provideFactories = ListUtils.format(provideModuleMethods, (it) -> it.methodName + "()");

            builder.beginControlFlow("if (c instanceof $T)", par.className).addStatement(" c.init( $L ) ", String.join(",", provideFactories)).endControlFlow();
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
        injectGraph.addModule(MethodDetail.simpleName(name), module);
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
        List<FieldDetail> qFields = ListUtils.filter(args, (inx, it) -> (it.type instanceof ClassName) && qualifiers.contains(it.type));

        provideObjMethods.add(builder);
        collectRuns.add(() -> {
            IProvideTypeWrapperHelper provideTypeWrapperHelper = IProvideTypeWrapperHelper.findHelper(providingType);
            CodeBlock codeBlock = injectGraph.codeProvideType(provideTypeWrapperHelper.providingType(), qFields);
            if (codeBlock == null)
                //todo throw errors
                throw new RuntimeException("err provide obj " + name + " " + providingType);

            builder.addStatement(
                    "return $L",
                    provideTypeWrapperHelper.provideCode(codeBlock)
            );
        });
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

                    SetFieldHelper setFieldHelper = new SetFieldHelper(injectField);
                    setFieldHelper.checkIsKotlinField(injectableCl);
                    IProvideTypeWrapperHelper provideTypeWrapperHelper = IProvideTypeWrapperHelper.findHelper(injectField.type);
                    CodeBlock codeBlock = injectGraph.codeProvideType(provideTypeWrapperHelper.providingType(), qFields);
                    if (codeBlock == null)
                        //todo throw errors
                        throw new RuntimeException("err inject " + injectField.name);

                    builder.addStatement(
                            setFieldHelper.codeSetField(
                                    injectableField.name,
                                    provideTypeWrapperHelper.provideCode(codeBlock)
                            )
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
                        IProvideTypeWrapperHelper injectHelper = IProvideTypeWrapperHelper.findHelper(injectField.type);
                        if (injectHelper.isGenerateWrapper())
                            //nothing to protect
                            continue;

                        SetFieldHelper getFieldHelper = new SetFieldHelper(injectField);
                        getFieldHelper.checkIsKotlinField(injectableCl);

                        emptyCode = false;
                        subscrCode.add("$L.add(new $T($L, ", refCollectionGlFieldName, TimeHolder.class, scheduleGlFieldName)
                                .add(getFieldHelper.codeGetField(injectableField.name))
                                .addStatement(", timeMillis))");
                    }
                    subscrCode.endControlFlow(")");

                    if (!emptyCode) builder.addCode(subscrCode.build());
                }
            }
        });
        return this;
    }

    public ComponentBuilder protectInjected(String name, ClassDetail injectableCl, long timeMillis) {
        timeHolderFields();

        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(injectableCl.className, "cl").build()
                ).returns(void.class);

        protectInjectedMethods.add(builder);
        collectRuns.add(() -> {
            for (FieldDetail injectField : injectableCl.fields) {
                if (!injectField.injectAnnotation) continue;
                IProvideTypeWrapperHelper injectHelper = IProvideTypeWrapperHelper.findHelper(injectField.type);
                if (injectHelper.isGenerateWrapper())
                    //nothing to protect
                    continue;

                SetFieldHelper getFieldHelper = new SetFieldHelper(injectField);
                getFieldHelper.checkIsKotlinField(injectableCl);

                builder.addCode("$L.add(new $T($L, ", refCollectionGlFieldName, TimeHolder.class, scheduleGlFieldName)
                        .addCode(getFieldHelper.codeGetField("cl"))
                        .addStatement(", $L))", timeMillis);
            }
        });
        return this;
    }

    public ComponentBuilder gcMethod(String name, List<TypeName> gcScopes) {
        CodeBlock.Builder scopesCode = CodeBlock.builder();
        int inx = 0;
        for (TypeName sc : gcScopes)
            if (inx++ <= 0) scopesCode.add("$T.class", sc);
            else scopesCode.add(", $T.class", sc);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addCode("$T scopes = new $T($T.asList(",
                        ParameterizedTypeName.get(Set.class, Class.class),
                        ParameterizedTypeName.get(HashSet.class, Class.class),
                        Arrays.class)
                .addCode(scopesCode.build())
                .addStatement("))");


        gcMethods.add(builder);
        collectRuns.add(() -> {
            for (ModuleFieldHelper moduleFieldHelper : moduleFieldHelpers)
                builder.addCode(moduleFieldHelper.statementAllWeak("scopes"));

            builder.addStatement("$T.gc()", System.class);

            if (fields.containsKey(refCollectionGlFieldName))
                builder.addStatement("$L.clearNulls()", refCollectionGlFieldName);
            for (ModuleFieldHelper moduleFieldHelper : moduleFieldHelpers)
                builder.addCode(moduleFieldHelper.statementAllDef("scopes"));
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
                .addCode("$T scopes = new $T($T.asList(",
                        ParameterizedTypeName.get(Set.class, Class.class),
                        ParameterizedTypeName.get(HashSet.class, Class.class),
                        Arrays.class).addCode(scopesCode.build())
                .addStatement("))");

        builder.addStatement("$T cache = $T.$L", SwitchCache.CacheType.class, SwitchCache.CacheType.class, switchCacheAnnotation.cache.name());
        builder.addStatement("$T time = $L", long.class, switchCacheAnnotation.timeMillis);
        if (switchCacheAnnotation.timeMillis > 0) {
            timeHolderFields();
            builder.addStatement("$T scheduler = this.$L", TimeScheduler.class, scheduleGlFieldName);
        } else {
            builder.addStatement("$T scheduler = null", TimeScheduler.class);
        }


        switchRefMethods.add(builder);
        collectRuns.add(() -> {
            for (ModuleFieldHelper moduleFieldHelper : moduleFieldHelpers)
                builder.addCode(moduleFieldHelper.statementSwitchRefs("scopes", "cache", "scheduler", "time"));
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

        for (FieldSpec.Builder field : modulesFields.values())
            typeSpecBuilder.addField(field.build());

        List<MethodSpec.Builder> methodBuilders = new LinkedList<>();
        methodBuilders.addAll(iComponentMethods.values());
        methodBuilders.addAll(modulesMethods.values());
        methodBuilders.addAll(provideObjMethods);
        methodBuilders.addAll(injectMethods);
        methodBuilders.addAll(protectInjectedMethods);
        methodBuilders.addAll(switchRefMethods);
        methodBuilders.addAll(gcMethods);

        for (MethodSpec.Builder method : methodBuilders)
            typeSpecBuilder.addMethod(method.build());

        return typeSpecBuilder.build();
    }

    public void writeTo(Filer filer) {
        TypeSpec typeSpec = build();
        if (typeSpec != null) CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
    }

}
