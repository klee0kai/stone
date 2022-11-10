package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.codegen.helpers.ComponentInjectGraph;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.types.ListUtils;
import com.github.klee0kai.stone.types.RefCollection;
import com.github.klee0kai.stone.types.TimeHolder;
import com.github.klee0kai.stone.types.TimeScheduler;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.util.*;

public class ComponentBuilder {

    public final ClassDetail orComponentCl;

    public ClassName className;

    public static String refCollectionGlFieldName = "__refCollection";

    public static String initMethodName = "init";
    public static String bindMethodName = "bind";
    public static String extOfMethodName = "extOf";

    public final Set<TypeName> interfaces = new HashSet<>();

    public final Set<ClassName> qualifiers = new HashSet<>();

    // ---------------------- common fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> fields = new HashMap<>();
    public final HashMap<String, MethodSpec.Builder> iComponentMethods = new HashMap<>();

    public final CodeBlock.Builder initModuleCode = CodeBlock.builder();
    public final CodeBlock.Builder bindModuleCode = CodeBlock.builder();

    public final CodeBlock.Builder weakAllModuleCode = CodeBlock.builder();
    public final CodeBlock.Builder restoreRefsModuleCode = CodeBlock.builder();


    // ---------------------- provide fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> modulesFields = new HashMap<>();
    public final HashMap<String, MethodSpec.Builder> modulesMethods = new HashMap<>();
    public final List<MethodSpec.Builder> injectMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> protectInjectedMethods = new LinkedList<>();
    public final List<MethodSpec.Builder> gcMethods = new LinkedList<>();


    private final LinkedList<Runnable> collectRuns = new LinkedList<>();
    private final ComponentInjectGraph injectGraph = new ComponentInjectGraph();


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

    public ComponentBuilder initMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(initMethodName)
                .addModifiers(Modifier.SYNCHRONIZED, Modifier.PUBLIC)
                .addParameter(Object[].class, "modules")
                .varargs(true);
        if (override)
            builder.addAnnotation(Override.class);

        iComponentMethods.put(initMethodName, builder);

        collectRuns.add(() -> {
            builder.beginControlFlow("for (Object m : modules)")
                    .addCode(initModuleCode.build())
                    .endControlFlow();
        });
        return this;
    }


    public ComponentBuilder bindMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(bindMethodName)
                .addModifiers(Modifier.SYNCHRONIZED, Modifier.PUBLIC)
                .addParameter(Object[].class, "objects")
                .varargs(true);
        if (override)
            builder.addAnnotation(Override.class);

        iComponentMethods.put(bindMethodName, builder);

        collectRuns.add(() -> {
            builder.beginControlFlow("for (Object ob : objects)")
                    .addCode(bindModuleCode.build())
                    .endControlFlow();
        });
        return this;
    }

    public ComponentBuilder extOfMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(extOfMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(IComponent.class, "c");
        if (override)
            builder.addAnnotation(Override.class);
        iComponentMethods.put(extOfMethodName, builder);
        return this;
    }

    public ComponentBuilder provideModuleMethod(String name, ClassDetail module) {
        ClassName moduleStoneMirror = ClassNameUtils.genModuleNameMirror(module.className);
        modulesFields.put(name, FieldSpec.builder(moduleStoneMirror, name, Modifier.PRIVATE)
                .initializer("new $T()", moduleStoneMirror));
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(moduleStoneMirror)
                .addStatement("return this.$L", name);
        modulesMethods.put(name, builder);

        initModuleCode.addStatement("this.$L.init(m)", name);
        bindModuleCode.addStatement("this.$L.bind(ob)", name);
        weakAllModuleCode.addStatement("this.$L.allWeak(scopes)", name);
        restoreRefsModuleCode.addStatement("this.$L.restoreRefs(scopes)", name);
        injectGraph.addModule(MethodDetail.simpleName(name), module);
        return this;
    }

    public ComponentBuilder injectMethod(String name, ClassDetail injectClass, List<FieldDetail> args) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        for (FieldDetail arg : args)
            builder.addParameter(ParameterSpec.builder(arg.type, arg.name).build());
        FieldDetail clField = ListUtils.first(args, (inx, it) -> Objects.equals(it.type, injectClass.className));
        if (clField==null)
            //todo throw error
            return this;


        injectMethods.add(builder);
        collectRuns.add(() -> {
            for (FieldDetail ijField : injectClass.fields) {
                if (!ijField.injectAnnotation)
                    continue;
                List<FieldDetail> qFields = ListUtils.filter(args,
                        (inx, it) -> (it.type instanceof ClassName) && qualifiers.contains(it.type));

                CodeBlock codeBlock = injectGraph.codeProvideType(ijField.type, qFields);
                if (codeBlock == null)
                    //todo throw errors
                    throw new RuntimeException("err inject " + ijField.name);
                builder.addCode("$L.$L = ", clField.name, ijField.name)
                        .addStatement(codeBlock);
            }
        });
        return this;
    }

    public ComponentBuilder protectInjected(String name, ClassDetail injectClass, long timeMillis) {
        String scheduleGlFieldName = "__scheduler";
        if (!fields.containsKey(scheduleGlFieldName))
            fields.put(scheduleGlFieldName, FieldSpec.builder(TimeScheduler.class, scheduleGlFieldName, Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("new $T()", TimeScheduler.class));
        if (!fields.containsKey(refCollectionGlFieldName))
            fields.put(refCollectionGlFieldName, FieldSpec.builder(RefCollection.class, refCollectionGlFieldName, Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("new $T()", RefCollection.class));


        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(injectClass.className, "cl").build())
                .returns(void.class);

        protectInjectedMethods.add(builder);
        collectRuns.add(() -> {
            for (FieldDetail ijField : injectClass.fields) {
                if (!ijField.injectAnnotation)
                    continue;
                builder.addStatement("$L.add(new $T($L, cl.$L, $L))",
                        refCollectionGlFieldName, TimeHolder.class,
                        scheduleGlFieldName, ijField.name, timeMillis);
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
            builder.addCode(weakAllModuleCode.build())
                    .addStatement("$T.gc()", System.class);
            if (fields.containsKey(refCollectionGlFieldName))
                builder.addStatement("$L.clearNulls()", refCollectionGlFieldName);
            builder.addCode(restoreRefsModuleCode.build());
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
        if (orComponentCl.isInterfaceClass())
            typeSpecBuilder.addSuperinterface(orComponentCl.className);
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
        methodBuilders.addAll(injectMethods);
        methodBuilders.addAll(protectInjectedMethods);
        methodBuilders.addAll(gcMethods);

        for (MethodSpec.Builder method : methodBuilders)
            typeSpecBuilder.addMethod(method.build());

        return typeSpecBuilder.build();
    }

    public void writeTo(Filer filer) {
        TypeSpec typeSpec = build();
        if (typeSpec != null)
            CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
    }

}
