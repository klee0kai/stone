package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.holder.SingleItemHolder;
import com.github.klee0kai.stone.holder.SoftItemHolder;
import com.github.klee0kai.stone.holder.StrongItemHolder;
import com.github.klee0kai.stone.holder.WeakItemHolder;
import com.github.klee0kai.stone.interfaces.IModule;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ModuleBuilder {

    public final ClassDetail orModuleCl;

    public ClassName className;

    public static final String factoryFieldName = "factory";

    private static final String appliedLocalFieldName = "applied";

    public static final String initMethodName = "init";

    public static final String bindMethodName = "bind";
    public static final String extOfMethodName = "extOf";
    public static final String getFactoryMethodName = "getFactory";

    public static final String allWeakMethodName = "allWeak";

    public static final String restoreRefsMethodName = "restoreRefs";

    public static final ClassName refClassName = ClassName.get(SingleItemHolder.class);

    public static final ClassName strongRefClassName = ClassName.get(StrongItemHolder.class);
    public static final ClassName softRefClassName = ClassName.get(SoftItemHolder.class);
    public static final ClassName weakRefClassName = ClassName.get(WeakItemHolder.class);

    public final Set<TypeName> interfaces = new HashSet<>();

    // ---------------------- common fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> fields = new HashMap<>();

    public final HashMap<String, MethodSpec.Builder> iModuleMethodBuilders = new HashMap<>();


    // ---------------------- provide fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> cacheFields = new HashMap<>();
    public final List<MethodSpec.Builder> provideMethodBuilders = new LinkedList<>();

    private final LinkedList<Runnable> collectRuns = new LinkedList<>();

    private boolean collected = false;

    public static ModuleBuilder from(ModuleFactoryBuilder factoryBuilder) {
        ModuleBuilder builder = new ModuleBuilder(factoryBuilder.orFactory)
                .factoryField(factoryBuilder.orFactory.className, factoryBuilder.className)
                .implementIModule();

        for (MethodDetail m : factoryBuilder.orFactory.getAllMethods(false)) {
            if (Objects.equals(m.methodName, "<init>"))
                continue;
            if (m.bindInstanceAnnotation != null)
                switch (m.bindInstanceAnnotation.cacheType) {
                    case WEAK:
                        builder.bindInstanceRef(m.methodName, m.returnType, weakRefClassName);
                        break;
                    case SOFT:
                        builder.bindInstanceRef(m.methodName, m.returnType, softRefClassName)
                                .allWeakFor(m.methodName);
                        break;
                    case STRONG:
                        builder.bindInstanceRef(m.methodName, m.returnType, strongRefClassName)
                                .allWeakFor(m.methodName);
                        break;
                }
            else if (m.provideAnnotation != null) {
                switch (m.provideAnnotation.cacheType) {
                    case FACTORY:
                        builder.provideFactory(m.methodName, m.returnType);
                        break;
                    case WEAK:
                        builder.provideRefCached(m.methodName, m.returnType, weakRefClassName);
                        break;
                    case SOFT:
                        builder.provideRefCached(m.methodName, m.returnType, softRefClassName)
                                .allWeakFor(m.methodName);
                        break;
                    case STRONG:
                        builder.provideRefCached(m.methodName, m.returnType, strongRefClassName)
                                .allWeakFor(m.methodName);
                        break;
                }
            } else
                builder.provideRefCached(m.methodName, m.returnType, softRefClassName)
                        .allWeakFor(m.methodName);
        }
        return builder;
    }


    public ModuleBuilder(ClassDetail orModuleCl) {
        this.orModuleCl = orModuleCl;
        this.className = ClassNameUtils.genModuleNameMirror(orModuleCl.className);
    }

    public ModuleBuilder factoryField(TypeName typeName, TypeName initTypeName) {
        FieldSpec.Builder builder = FieldSpec.builder(typeName, factoryFieldName, Modifier.PRIVATE);
        if (initTypeName != null)
            builder.initializer("new $T()", initTypeName);
        fields.put(factoryFieldName, builder);
        return this;
    }


    /**
     * Call first
     * add IModule methods.
     *
     * @return
     */
    public ModuleBuilder implementIModule() {
        boolean hasSupperStoneModule = orModuleCl.superClass != null && orModuleCl.superClass.moduleAnn != null;
        interfaces.add(ClassName.get(IModule.class));
        initMethod(true);
        bindMethod(true);
        extOfMethod(hasSupperStoneModule ? ClassNameUtils.genModuleNameMirror(orModuleCl.superClass.className) : null, true);
        getFactoryMethod(true);
        allWeakMethod(true);
        restoreRefsMethod(true);
        return this;
    }

    public ModuleBuilder initMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(initMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(boolean.class)
                .addParameter(Object.class, "or")
                .addStatement("boolean $L = false", appliedLocalFieldName)
                .beginControlFlow("if (or instanceof $T) ", orModuleCl.className)
                .addStatement("$L = ($T) or", factoryFieldName, orModuleCl.className)
                .addStatement("$L = true", appliedLocalFieldName)
                .endControlFlow()
                // get module factory by module class
                .beginControlFlow("if (or instanceof Class<?> && ((Class<?>) or).isInstance($T.class))", orModuleCl.className)
                .beginControlFlow("try")
                .addComment("looking for generated factory for module class")
                .addStatement("Class<?> gennedClass = Class.forName(((Class) or).getCanonicalName() + \"StoneFactory\")")
                .addStatement("$L = ($T) gennedClass.getConstructors()[0].newInstance()", factoryFieldName, orModuleCl.className)
                .addStatement("$L = true", appliedLocalFieldName)
                .endControlFlow()
                .beginControlFlow("catch ( $T | $T | $T | $T e)", ClassNotFoundException.class,
                        InstantiationException.class, IllegalAccessException.class, InvocationTargetException.class)

                .beginControlFlow("try")
                .addComment("we don't got stone factory. We suppose class have constructor")
                .addStatement("$L = ($T) ((Class<?>) or).getConstructors()[0].newInstance()", factoryFieldName, orModuleCl.className)
                .addStatement("$L = true", appliedLocalFieldName)
                .endControlFlow()
                .beginControlFlow("catch ($T | $T | $T ex)",
                        InstantiationException.class, IllegalAccessException.class, InvocationTargetException.class)
                .endControlFlow()

                .endControlFlow()
                .endControlFlow()
                .addStatement("return $L", appliedLocalFieldName);

        if (override) builder.addAnnotation(Override.class);

        iModuleMethodBuilders.put(initMethodName, builder);
        return this;
    }

    public ModuleBuilder bindMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(bindMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addParameter(Object.class, "or")
                .addStatement("boolean $L = false", appliedLocalFieldName)
                .returns(boolean.class);

        if (override) builder.addAnnotation(Override.class);

        iModuleMethodBuilders.put(bindMethodName, builder);

        collectRuns.add(() -> {
            builder.addStatement("return $L", appliedLocalFieldName);
        });
        return this;
    }


    /**
     * If superClass is module. We should implement extOf method.
     *
     * @param superStoneModule
     * @return
     */
    public ModuleBuilder extOfMethod(TypeName superStoneModule, boolean override) {
        MethodSpec.Builder extOfMethodBuilder = MethodSpec.methodBuilder(extOfMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(IModule.class, "superStoneModule")
                .returns(void.class);

        if (override) extOfMethodBuilder.addAnnotation(Override.class);

        if (superStoneModule != null) {
            String superDIModuleStoneFieldName = "superStone";
            fields.put(superDIModuleStoneFieldName,
                    FieldSpec.builder(superStoneModule, superDIModuleStoneFieldName, Modifier.PRIVATE));

            extOfMethodBuilder.beginControlFlow("if (superStoneModule instanceof $T)", superStoneModule)
                    .addStatement("$L = ($T) superStoneModule", superDIModuleStoneFieldName, superStoneModule)
                    .endControlFlow()
                    .beginControlFlow("else if (superStoneModule == null)")
                    .addStatement("$L = null ", superDIModuleStoneFieldName)
                    .endControlFlow();
        }
        iModuleMethodBuilders.put(extOfMethodName, extOfMethodBuilder);
        return this;
    }

    public ModuleBuilder getFactoryMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(getFactoryMethodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(Object.class)
                .addStatement("return $L", factoryFieldName);
        if (override) builder.addAnnotation(Override.class);
        iModuleMethodBuilders.put(getFactoryMethodName, builder);
        return this;
    }

    public ModuleBuilder allWeakMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(allWeakMethodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        if (override) builder.addAnnotation(Override.class);
        iModuleMethodBuilders.put(allWeakMethodName, builder);
        return this;
    }


    public ModuleBuilder allWeakFor(String fieldName) {
        MethodSpec.Builder allWeakMethod = iModuleMethodBuilders.get(allWeakMethodName);
        MethodSpec.Builder restoreFefMethod = iModuleMethodBuilders.get(restoreRefsMethodName);
        if (allWeakMethod != null && restoreFefMethod != null) {
            allWeakMethod.addStatement("$L.weak()", fieldName);

            restoreFefMethod.addStatement("$L.defRef()", fieldName);
        }
        return this;
    }

    public ModuleBuilder restoreRefsMethod(boolean override) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(restoreRefsMethodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        if (override) builder.addAnnotation(Override.class);
        iModuleMethodBuilders.put(restoreRefsMethodName, builder);
        return this;
    }


    public ModuleBuilder bindInstanceRef(String name, TypeName typeName, ClassName javaRef) {
        ParameterizedTypeName cacheType = ParameterizedTypeName.get(javaRef, typeName);
        MethodSpec.Builder bindMethodBuilder = iModuleMethodBuilders.get(bindMethodName);

        cacheFields.put(name, FieldSpec.builder(cacheType, name, Modifier.PRIVATE, Modifier.FINAL).initializer("new $T()", cacheType));

        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("return $L != null ? $L.get() : null", name, name));

        if (bindMethodBuilder != null)
            bindMethodBuilder.beginControlFlow("  if ($T.equals(or.getClass(), $T.class)) ", Objects.class, typeName)
                    .addStatement("$L.set(($T) or)", name, typeName)
                    .addStatement("$L = true", appliedLocalFieldName)
                    .endControlFlow();
        return this;
    }

    public ModuleBuilder provideFactory(String name, TypeName typeName) {
        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("return  $L.$L()", factoryFieldName, name));
        return this;
    }


    public ModuleBuilder provideRefCached(String name, TypeName typeName, ClassName javaRef) {
        String getCachedMethodName = getCachedMethodName(name);
        ParameterizedTypeName cacheType = ParameterizedTypeName.get(javaRef, typeName);

        cacheFields.put(name, FieldSpec.builder(cacheType, name, Modifier.PRIVATE, Modifier.FINAL).initializer("new $T()", cacheType));
        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("if ($L != null && $L.get() != null) return $L.get()", name, name, name)
                .addStatement("return $L.set($L.$L())", name, factoryFieldName, name));
        provideMethodBuilders.add(MethodSpec.methodBuilder(getCachedMethodName)
                .addModifiers(Modifier.PROTECTED, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("return $L != null ? $L.get() : null", name, name));
        return this;
    }


    public ModuleBuilder collect() {
        if (collected)
            return this;
        collected = true;
        for (Runnable r : collectRuns)
            r.run();
        collectRuns.clear();
        return this;
    }

    public TypeSpec build() {
        collect();

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className);
        typeSpecBuilder.addModifiers(Modifier.PUBLIC);
        if (orModuleCl.isInterfaceClass())
            typeSpecBuilder.addSuperinterface(orModuleCl.className);
        else typeSpecBuilder.superclass(orModuleCl.className);

        for (TypeName supInterface : interfaces)
            typeSpecBuilder.addSuperinterface(supInterface);

        for (FieldSpec.Builder fieldBuilder : fields.values())
            typeSpecBuilder.addField(fieldBuilder.build());
        for (FieldSpec.Builder cacheFieldBuilder : cacheFields.values())
            typeSpecBuilder.addField(cacheFieldBuilder.build());

        for (MethodSpec.Builder iModuleMethod : iModuleMethodBuilders.values())
            typeSpecBuilder.addMethod(iModuleMethod.build());

        for (MethodSpec.Builder provideMethod : provideMethodBuilders)
            typeSpecBuilder.addMethod(provideMethod.build());

        return typeSpecBuilder.build();
    }

    public void writeTo(Filer filer) {
        TypeSpec typeSpec = build();
        if (typeSpec != null)
            CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
    }


    private static String getCachedMethodName(String factoryMethodName) {
        return "__" + factoryMethodName + "_cache";
    }
}
