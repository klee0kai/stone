package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.codegen.helpers.ItemHolderCodeHelper;
import com.github.klee0kai.stone.interfaces.IModule;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.ParamDetails;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.github.klee0kai.stone.utils.ListUtils;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
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

    public final Set<TypeName> interfaces = new HashSet<>();

    // ---------------------- common fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> fields = new HashMap<>();

    public final HashMap<String, MethodSpec.Builder> iModuleMethodBuilders = new HashMap<>();


    // ---------------------- provide fields and method  ----------------------------------
    public final List<FieldSpec.Builder> cacheFields = new LinkedList<>();
    public final List<MethodSpec.Builder> provideMethodBuilders = new LinkedList<>();

    private final LinkedList<Runnable> collectRuns = new LinkedList<>();

    private boolean collected = false;

    public static ModuleBuilder from(ModuleFactoryBuilder factoryBuilder) {
        ModuleBuilder builder = new ModuleBuilder(factoryBuilder.orFactory)
                .factoryField(factoryBuilder.orFactory.className, factoryBuilder.className)
                .implementIModule();

        int fieldId = 1;
        for (MethodDetail m : factoryBuilder.orFactory.getAllMethods(false)) {
            if (Objects.equals(m.methodName, "<init>"))
                continue;
            if (m.bindInstanceAnnotation != null) {
                ItemHolderCodeHelper.ItemCacheType cacheType = ItemHolderCodeHelper.cacheTypeFrom(m.bindInstanceAnnotation.cacheType);
                ItemHolderCodeHelper itemHolderCodeHelper = ItemHolderCodeHelper.of(m.methodName + fieldId++, m.returnType, m.args, cacheType);
                builder.bindInstance(m.methodName, m.returnType, itemHolderCodeHelper);
                builder.allWeakFor(itemHolderCodeHelper);
            } else if (m.provideAnnotation != null && m.provideAnnotation.cacheType == Provide.CacheType.FACTORY) {
                builder.provideFactory(m.methodName, m.returnType, m.args);
            } else {
                ItemHolderCodeHelper.ItemCacheType cacheType = ItemHolderCodeHelper.cacheTypeFrom(
                        m.provideAnnotation != null ? m.provideAnnotation.cacheType : Provide.CacheType.SOFT);
                ItemHolderCodeHelper itemHolderCodeHelper = ItemHolderCodeHelper.of(m.methodName + fieldId++, m.returnType, m.args, cacheType);
                builder.provideCached(m.methodName, m.returnType, itemHolderCodeHelper, m.args);
                builder.allWeakFor(itemHolderCodeHelper);
            }
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


    public ModuleBuilder allWeakFor(ItemHolderCodeHelper fieldHelper) {
        MethodSpec.Builder allWeakMethod = iModuleMethodBuilders.get(allWeakMethodName);
        MethodSpec.Builder restoreFefMethod = iModuleMethodBuilders.get(restoreRefsMethodName);
        if (allWeakMethod != null && restoreFefMethod != null && fieldHelper.supportWeakRef()) {
            allWeakMethod.addCode(fieldHelper.statementToWeak());
            restoreFefMethod.addCode(fieldHelper.statementDefRef());
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


    public ModuleBuilder bindInstance(String name, TypeName typeName, ItemHolderCodeHelper itemHolderCodeHelper) {
        MethodSpec.Builder bindMethodBuilder = iModuleMethodBuilders.get(bindMethodName);
        cacheFields.add(itemHolderCodeHelper.cachedField());

        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addCode("return ")
                .addCode(itemHolderCodeHelper.codeGetCachedValue())
                .addStatement(""));

        if (bindMethodBuilder != null) {
            bindMethodBuilder.beginControlFlow("  if ($T.equals(or.getClass(), $T.class)) ", Objects.class, typeName)
                    .addStatement(itemHolderCodeHelper.codeSetCachedValue(
                            CodeBlock.of("($T) or", typeName)
                    ))
                    .addStatement("$L = true", appliedLocalFieldName)
                    .endControlFlow();
        }
        return this;
    }


    public ModuleBuilder provideFactory(String name, TypeName typeName, List<ParamDetails> args) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("return  $L.$L($L)", factoryFieldName, name,
                        String.join(",", ListUtils.format(args, (it) -> it.name)));

        if (args != null) for (ParamDetails p : args)
            builder.addParameter(p.type, p.name);

        provideMethodBuilders.add(builder);
        return this;
    }

    public ModuleBuilder provideCached(String name, TypeName typeName, ItemHolderCodeHelper itemHolderCodeHelper, List<ParamDetails> args) {
        String getCachedMethodName = getCachedMethodName(name);
        cacheFields.add(itemHolderCodeHelper.cachedField());
        MethodSpec.Builder provideMethodBuilder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addCode("if ( ").addCode(itemHolderCodeHelper.codeGetCachedValue()).addCode(" != null )\n")
                .addCode("\t\treturn ").addStatement(itemHolderCodeHelper.codeGetCachedValue())
                .addCode("return ").addCode(itemHolderCodeHelper.codeSetCachedValue(
                        CodeBlock.of("$L.$L($L)", factoryFieldName, name,
                                String.join(",", ListUtils.format(args, (it) -> it.name)))
                )).addStatement("");

        MethodSpec.Builder getCachedMethodBuilder = MethodSpec.methodBuilder(getCachedMethodName)
                .addModifiers(Modifier.PROTECTED, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addCode("return ")
                .addStatement(itemHolderCodeHelper.codeGetCachedValue());

        if (args != null) for (ParamDetails p : args) {
            provideMethodBuilder.addParameter(p.type, p.name);
            getCachedMethodBuilder.addParameter(p.type, p.name);
        }

        provideMethodBuilders.add(provideMethodBuilder);
        provideMethodBuilders.add(getCachedMethodBuilder);
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
        for (FieldSpec.Builder cacheFieldBuilder : cacheFields)
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
