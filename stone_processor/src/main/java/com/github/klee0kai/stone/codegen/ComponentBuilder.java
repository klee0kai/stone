package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.codegen.helpers.AllClassesHelper;
import com.github.klee0kai.stone.codegen.helpers.ComponentInjectGraph;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.util.*;

public class ComponentBuilder {

    public final ClassDetail orComponentCl;

    public ClassName className;

    public static String initMethodName = "init";
    public static String bindMethodName = "bind";
    public static String extOfMethodName = "extOf";

    public static String gcMethodName = "gc";

    public final Set<TypeName> interfaces = new HashSet<>();

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


    private final LinkedList<Runnable> collectRuns = new LinkedList<>();
    private final ComponentInjectGraph injectGraph = new ComponentInjectGraph();


    public static ComponentBuilder from(ClassDetail component) {
        ComponentBuilder componentBuilder = new ComponentBuilder(component, ClassNameUtils.genComponentNameMirror(component.className));
        componentBuilder.implementIComponentMethods();
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
        gcMethod(true);
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

    public ComponentBuilder gcMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(gcMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(boolean.class, "includeSoftRefs");
        if (override)
            builder.addAnnotation(Override.class);

        iComponentMethods.put(gcMethodName, builder);
        collectRuns.add(() -> {
            builder.beginControlFlow("if (includeSoftRefs)")
                    .addCode(weakAllModuleCode.build())
                    .addStatement("$T.gc()", System.class)
                    .addCode(restoreRefsModuleCode.build())
                    .endControlFlow()
                    .beginControlFlow("else")
                    .addStatement("$T.gc()", System.class)
                    .endControlFlow();
        });

        return this;
    }

    public ComponentBuilder provideModule(String name, ClassDetail module) {
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
        weakAllModuleCode.addStatement("this.$L.allWeak()", name);
        restoreRefsModuleCode.addStatement("this.$L.restoreRefs()", name);
        injectGraph.addModule(name, module);
        return this;
    }

    /**
     * invoke after provideModule
     *
     * @param name
     * @param injectClass
     * @return
     */
    public ComponentBuilder provideInject(String name, ClassDetail injectClass) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(injectClass.className, "cl").build())
                .returns(void.class);
        for (FieldDetail ijField : injectClass.fields) {
            if (!ijField.injectAnnotation)
                continue;
            CodeBlock codeBlock = injectGraph.codeProvideType(ijField.type);
            if (codeBlock == null)
                //todo throw errors
                throw new RuntimeException("err inject " + ijField.name);
            builder
                    .addCode("cl.$L = ", ijField.name)
                    .addStatement(codeBlock);
        }

        injectMethods.add(builder);
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

        for (MethodSpec.Builder method : iComponentMethods.values())
            typeSpecBuilder.addMethod(method.build());

        for (MethodSpec.Builder method : modulesMethods.values())
            typeSpecBuilder.addMethod(method.build());

        for (MethodSpec.Builder method : injectMethods)
            typeSpecBuilder.addMethod(method.build());

        return typeSpecBuilder.build();
    }

    public void writeTo(Filer filer) {
        TypeSpec typeSpec = build();
        if (typeSpec != null)
            CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
    }

}