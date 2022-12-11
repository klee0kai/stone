package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.closed.IModule;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.closed.types.TimeScheduler;
import com.github.klee0kai.stone.codegen.helpers.ItemHolderCodeHelper;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ModuleBuilder {

    public final ClassDetail orModuleCl;

    public ClassName className;

    public static final String overridedModuleFieldName = "overridedModule";
    public static final String factoryFieldName = "factory";

    private static final String appliedLocalFieldName = "applied";

    public static final String initMethodName = "init";

    public static final String bindMethodName = "bind";
    public static final String getFactoryMethodName = "getFactory";

    public static final String switchRefMethodName = "switchRef";


    public final Set<TypeName> interfaces = new HashSet<>();

    // ---------------------- common fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> fields = new HashMap<>();

    public final HashMap<String, MethodSpec.Builder> iModuleMethodBuilders = new HashMap<>();


    // ---------------------- provide fields and method  ----------------------------------
    public final List<FieldSpec.Builder> cacheFields = new LinkedList<>();
    public final List<MethodSpec.Builder> provideMethodBuilders = new LinkedList<>();

    public final HashMap<TypeName, CodeBlock.Builder> switchRefStatementBuilders = new HashMap<>();

    private final LinkedList<Runnable> collectRuns = new LinkedList<>();

    public static ModuleBuilder from(ModuleFactoryBuilder factoryBuilder) {
        ModuleBuilder builder = new ModuleBuilder(factoryBuilder.orFactory)
                .factoryField(factoryBuilder.orFactory.className, factoryBuilder.className)
                .overridedField(ClassNameUtils.genInterfaceModuleNameMirror(factoryBuilder.orFactory.className))
                .implementIModule();

        int fieldId = 1;
        for (MethodDetail m : factoryBuilder.orFactory.getAllMethods(false)) {
            if (Objects.equals(m.methodName, "<init>"))
                continue;
            if (m.bindInstanceAnnotation != null) {
                ItemHolderCodeHelper.ItemCacheType cacheType = ItemHolderCodeHelper.cacheTypeFrom(m.bindInstanceAnnotation.cacheType);
                ItemHolderCodeHelper itemHolderCodeHelper = ItemHolderCodeHelper.of(m.methodName + fieldId++, m.returnType, m.args, cacheType);
                builder.bindInstance(m.methodName, m.returnType, itemHolderCodeHelper);
                builder.switchRefFor(itemHolderCodeHelper, ListUtils.setOf(m.gcScopeAnnotations, ClassName.get(GcAllScope.class)));
                switch (m.bindInstanceAnnotation.cacheType) {
                    case Weak:
                        builder.switchRefFor(itemHolderCodeHelper, ListUtils.setOf(m.gcScopeAnnotations, ClassName.get(GcWeakScope.class)));
                        break;
                    case Soft:
                        builder.switchRefFor(itemHolderCodeHelper, ListUtils.setOf(m.gcScopeAnnotations, ClassName.get(GcSoftScope.class)));
                        break;
                    case Strong:
                        builder.switchRefFor(itemHolderCodeHelper, ListUtils.setOf(m.gcScopeAnnotations, ClassName.get(GcStrongScope.class)));
                        break;
                }
            } else if (m.provideAnnotation != null && m.provideAnnotation.cacheType == Provide.CacheType.Factory) {
                builder.provideFactory(m.methodName, m.returnType, m.args);
            } else {
                ItemHolderCodeHelper.ItemCacheType cacheType = ItemHolderCodeHelper.cacheTypeFrom(
                        m.provideAnnotation != null ? m.provideAnnotation.cacheType : Provide.CacheType.Soft);
                ItemHolderCodeHelper itemHolderCodeHelper = ItemHolderCodeHelper.of(m.methodName + fieldId++, m.returnType, m.args, cacheType);
                builder.provideCached(m.methodName, m.returnType, itemHolderCodeHelper, m.args);
                builder.switchRefFor(itemHolderCodeHelper, ListUtils.setOf(m.gcScopeAnnotations, ClassName.get(GcAllScope.class)));
                switch (cacheType) {
                    case Weak:
                        builder.switchRefFor(itemHolderCodeHelper, ListUtils.setOf(m.gcScopeAnnotations, ClassName.get(GcWeakScope.class)));
                        break;
                    case Soft:
                        builder.switchRefFor(itemHolderCodeHelper, ListUtils.setOf(m.gcScopeAnnotations, ClassName.get(GcSoftScope.class)));
                        break;
                    case Strong:
                        builder.switchRefFor(itemHolderCodeHelper, ListUtils.setOf(m.gcScopeAnnotations, ClassName.get(GcStrongScope.class)));
                        break;
                }
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

    public ModuleBuilder overridedField(TypeName typeName) {
        FieldSpec.Builder builder = FieldSpec.builder(typeName, overridedModuleFieldName, Modifier.PRIVATE)
                .initializer("null");
        fields.put(overridedModuleFieldName, builder);
        return this;
    }


    /**
     * Call first
     * add IModule methods.
     *
     * @return
     */
    public ModuleBuilder implementIModule() {
        interfaces.add(ClassName.get(IModule.class));
        for (ClassDetail parentModule : orModuleCl.getAllParents(false)) {
            if (parentModule.moduleAnn!=null)
                interfaces.add(ClassNameUtils.genInterfaceModuleNameMirror(parentModule.className));
        }

        initMethod();
        bindMethod();
        getFactoryMethod();
        switchRefMethod();
        return this;
    }

    public ModuleBuilder initMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(initMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addAnnotation(Override.class)
                .returns(boolean.class)
                .addParameter(Object.class, "or")
                .addStatement("boolean $L = false", appliedLocalFieldName)
                // check module class
                .beginControlFlow("if ( (or instanceof $T) ) ", ClassNameUtils.genInterfaceModuleNameMirror(orModuleCl.className))
                .addStatement("$L = ($T) or", overridedModuleFieldName, ClassNameUtils.genInterfaceModuleNameMirror(orModuleCl.className))
                .addStatement("$L = ($T) (($T) or).getFactory() ", factoryFieldName, orModuleCl.className, IModule.class)
                .addStatement("$L = true", appliedLocalFieldName)
                .endControlFlow()
                // check factory class
                .beginControlFlow("else if (or instanceof $T) ", orModuleCl.className)
                .addStatement("$L = ($T) or", factoryFieldName, orModuleCl.className)
                .addStatement("$L = true", appliedLocalFieldName)
                .endControlFlow()
                // get module factory by module class
                .beginControlFlow("else if (or instanceof Class<?> && ((Class<?>) or).isInstance($T.class))", orModuleCl.className)
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

        iModuleMethodBuilders.put(initMethodName, builder);
        return this;
    }

    public ModuleBuilder bindMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(bindMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addAnnotation(Override.class)
                .addParameter(Object.class, "or")
                .addStatement("boolean $L = false", appliedLocalFieldName)
                .returns(boolean.class);

        iModuleMethodBuilders.put(bindMethodName, builder);

        collectRuns.add(() -> {
            builder.addStatement("return $L", appliedLocalFieldName);
        });
        return this;
    }


    public ModuleBuilder getFactoryMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(getFactoryMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(Object.class)
                .addStatement("return $L", factoryFieldName);
        iModuleMethodBuilders.put(getFactoryMethodName, builder);
        return this;
    }

    public ModuleBuilder switchRefMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(switchRefMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(Set.class, Class.class), "scopes").build())
                .addParameter(ParameterSpec.builder(SwitchCache.CacheType.class, "cache").build())
                .addParameter(ParameterSpec.builder(TimeScheduler.class, "scheduler").build())
                .addParameter(ParameterSpec.builder(long.class, "time").build())
                .returns(void.class);
        iModuleMethodBuilders.put(switchRefMethodName, builder);

        collectRuns.add(() -> {
            builder.beginControlFlow("for ($T sc:scopes)", Class.class);
            for (TypeName gcScope : switchRefStatementBuilders.keySet()) {
                builder.beginControlFlow("if ($T.equals($T.class,sc))", Objects.class, gcScope)
                        .addCode(switchRefStatementBuilders.get(gcScope).build())
                        .endControlFlow();
            }
            builder.endControlFlow();
        });
        return this;
    }


    public ModuleBuilder switchRefFor(ItemHolderCodeHelper fieldHelper, Set<TypeName> scopes) {
        for (TypeName sc : scopes) {
            switchRefStatementBuilders.putIfAbsent(sc, CodeBlock.builder());
            switchRefStatementBuilders.get(sc).add(
                    fieldHelper.statementSwitchRef(
                            "cache",
                            "scheduler",
                            "time"
                    ));
        }
        return this;
    }

    public ModuleBuilder bindInstance(String name, TypeName typeName, ItemHolderCodeHelper itemHolderCodeHelper) {
        cacheFields.add(itemHolderCodeHelper.cachedField());

        //provide method
        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addCode("return ")
                .addCode(itemHolderCodeHelper.codeGetCachedValue())
                .addStatement(""));

        //bind item code
        MethodSpec.Builder bindMethodBuilder = iModuleMethodBuilders.get(bindMethodName);
        if (bindMethodBuilder != null) {
            bindMethodBuilder.beginControlFlow("if ($T.equals(or.getClass(), $T.class)) ", Objects.class, typeName)
                    .addStatement(itemHolderCodeHelper.codeSetCachedValue(
                            CodeBlock.of("($T) or", typeName)
                    ))
                    .addStatement("$L = true", appliedLocalFieldName)
                    .endControlFlow();
        }

        // get cached method
        String getCachedMethodName = ModuleInterfaceBuilder.getCachedMethodName(name);
        MethodSpec.Builder getCachedMethodBuilder = MethodSpec.methodBuilder(getCachedMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addAnnotation(Override.class)
                .returns(typeName)
                .addCode("return ")
                .addStatement(itemHolderCodeHelper.codeGetCachedValue());
        provideMethodBuilders.add(getCachedMethodBuilder);

        // set cache type  method
        String setCacheTypeMethodName = ModuleInterfaceBuilder.getSwitchCacheMethodName(name);
        MethodSpec.Builder setCacheTypeMethodBuilder = MethodSpec.methodBuilder(setCacheTypeMethodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addParameter(ParameterSpec.builder(SwitchCache.CacheType.class, "cache").build())
                .addParameter(ParameterSpec.builder(TimeScheduler.class, "scheduler").build())
                .addParameter(ParameterSpec.builder(long.class, "time").build())
                .addCode(itemHolderCodeHelper.statementSwitchRef("cache", "scheduler", "time"));
        provideMethodBuilders.add(setCacheTypeMethodBuilder);
        return this;
    }


    public ModuleBuilder provideFactory(String name, TypeName typeName, List<FieldDetail> args) {
        MethodSpec.Builder provideMethodBuilder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                .addStatement("return  $L.$L($L)", factoryFieldName, name,
                        String.join(",", ListUtils.format(args, (it) -> it.name)));


        // get cached method
        MethodSpec.Builder getCachedMethodBuilder = MethodSpec.methodBuilder(ModuleInterfaceBuilder.getCachedMethodName(name))
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addAnnotation(Override.class)
                .returns(typeName)
                .addStatement("return null");

        // set cache type  method
        MethodSpec.Builder setCacheTypeMethodBuilder = MethodSpec.methodBuilder(ModuleInterfaceBuilder.getSwitchCacheMethodName(name))
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED);

        if (args != null) for (FieldDetail p : args) {
            provideMethodBuilder.addParameter(p.type, p.name);
            getCachedMethodBuilder.addParameter(p.type, p.name);
            setCacheTypeMethodBuilder.addParameter(p.type, p.name);
        }
        setCacheTypeMethodBuilder
                .addParameter(ParameterSpec.builder(SwitchCache.CacheType.class, "cache").build())
                .addParameter(ParameterSpec.builder(TimeScheduler.class, "scheduler").build())
                .addParameter(ParameterSpec.builder(long.class, "time").build());


        provideMethodBuilders.add(provideMethodBuilder);
        provideMethodBuilders.add(getCachedMethodBuilder);
        provideMethodBuilders.add(setCacheTypeMethodBuilder);

        return this;
    }

    public ModuleBuilder provideCached(String name, TypeName typeName, ItemHolderCodeHelper itemHolderCodeHelper, List<FieldDetail> args) {
        String getCachedMethodName = ModuleInterfaceBuilder.getCachedMethodName(name);
        String argsString = String.join(",", ListUtils.format(args, (it) -> it.name));

        cacheFields.add(itemHolderCodeHelper.cachedField());
        MethodSpec.Builder provideMethodBuilder = MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(typeName)
                // check cached from overrided module
                .beginControlFlow("if ($L != null && $L.$L(  $L  ) != null ) ", overridedModuleFieldName, overridedModuleFieldName, getCachedMethodName, argsString)
                .addStatement("return $L.$L( $L ) ", overridedModuleFieldName, getCachedMethodName, argsString)
                .endControlFlow()
                //  check cached
                .addCode("if ( ").addCode(itemHolderCodeHelper.codeGetCachedValue()).addCode(" != null )\n")
                .addCode("\t\treturn ").addStatement(itemHolderCodeHelper.codeGetCachedValue())
                // return from overrided module
                .beginControlFlow("if ($L != null) ", overridedModuleFieldName)
                .addStatement("return $L.$L( $L ) ", overridedModuleFieldName, name, argsString)
                .endControlFlow()
                // gen new and return
                .addCode("return ").addCode(itemHolderCodeHelper.codeSetCachedValue(
                        CodeBlock.of("$L.$L($L)", factoryFieldName, name, argsString)
                )).addStatement("");

        // get cached method
        MethodSpec.Builder getCachedMethodBuilder = MethodSpec.methodBuilder(getCachedMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addAnnotation(Override.class)
                .returns(typeName)
                .addCode("return ")
                .addStatement(itemHolderCodeHelper.codeGetCachedValue());

        // get cache type  method
        String setCacheTypeMethodName = ModuleInterfaceBuilder.getSwitchCacheMethodName(name);
        MethodSpec.Builder setCacheTypeMethodBuilder = MethodSpec.methodBuilder(setCacheTypeMethodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED);

        if (args != null) for (FieldDetail p : args) {
            provideMethodBuilder.addParameter(p.type, p.name);
            getCachedMethodBuilder.addParameter(p.type, p.name);
            setCacheTypeMethodBuilder.addParameter(p.type, p.name);
        }

        setCacheTypeMethodBuilder
                .addParameter(ParameterSpec.builder(SwitchCache.CacheType.class, "cache").build())
                .addParameter(ParameterSpec.builder(TimeScheduler.class, "scheduler").build())
                .addParameter(ParameterSpec.builder(long.class, "time").build())
                .addCode(itemHolderCodeHelper.statementSwitchRef("cache", "scheduler", "time"));


        provideMethodBuilders.add(provideMethodBuilder);
        provideMethodBuilders.add(getCachedMethodBuilder);
        provideMethodBuilders.add(setCacheTypeMethodBuilder);
        return this;
    }


    public ModuleBuilder collect() {
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


}
