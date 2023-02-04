package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.annotations.component.GcAllScope;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.closed.IModule;
import com.github.klee0kai.stone.closed.types.CacheAction;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.closed.types.SwitchCacheParam;
import com.github.klee0kai.stone.codegen.helpers.ItemHolderCodeHelper;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.github.klee0kai.stone.codegen.ModuleCacheControlInterfaceBuilder.cacheControlMethodName;

public class ModuleBuilder {

    public static final String overridedModuleFieldName = "overridedModule";
    public static final String factoryFieldName = "factory";
    private static final String appliedLocalFieldName = "applied";
    public static final String initMethodName = "init";
    public static final String initCachesFromMethodName = "initCachesFrom";
    public static final String bindMethodName = "bind";
    public static final String getFactoryMethodName = "getFactory";
    public static final String switchRefMethodName = "switchRef";

    public final ClassDetail orModuleCl;

    public ClassName className;


    public final Set<TypeName> interfaces = new HashSet<>();
    public final Set<ClassName> qualifiers = new HashSet<>();

    // ---------------------- common fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> fields = new HashMap<>();

    public final HashMap<String, MethodSpec.Builder> iModuleMethodBuilders = new HashMap<>();


    // ---------------------- provide fields and method  ----------------------------------
    public final List<FieldSpec.Builder> cacheFields = new LinkedList<>();
    public final List<MethodSpec.Builder> provideMethodBuilders = new LinkedList<>();
    public final List<MethodSpec.Builder> cacheControlMethodBuilders = new LinkedList<>();

    public final HashMap<Set<TypeName>, CodeBlock.Builder> switchRefStatementBuilders = new HashMap<>();

    private final LinkedList<Runnable> collectRuns = new LinkedList<>();

    public static ModuleBuilder from(ModuleFactoryBuilder factoryBuilder, List<ClassName> allQualifiers) {
        ModuleBuilder builder = new ModuleBuilder(factoryBuilder.orFactory, ClassNameUtils.genModuleNameMirror(factoryBuilder.orFactory.className))
                .factoryField(factoryBuilder.orFactory.className, factoryBuilder.className)
                .overridedField(ClassNameUtils.genCacheControlInterfaceModuleNameMirror(factoryBuilder.orFactory.className))
                .implementIModule();
        builder.qualifiers.addAll(allQualifiers);

        for (MethodDetail m : factoryBuilder.orFactory.getAllMethods(false, false)) {
            if (Objects.equals(m.methodName, "<init>"))
                continue;

            List<FieldDetail> qFields = ListUtils.filter(m.args,
                    (inx, it) -> (it.type instanceof ClassName) && allQualifiers.contains(it.type)
            );

            int cacheFieldsCount = builder.cacheFields.size();

            if (m.bindInstanceAnnotation != null) {
                ItemHolderCodeHelper.ItemCacheType cacheType = ItemHolderCodeHelper.cacheTypeFrom(m.bindInstanceAnnotation.cacheType);
                ItemHolderCodeHelper itemHolderCodeHelper = ItemHolderCodeHelper.of(m.methodName + cacheFieldsCount, m.returnType, qFields, cacheType);
                builder.bindInstance(m, itemHolderCodeHelper)
                        .cacheControl(m, itemHolderCodeHelper)
                        .switchRefFor(itemHolderCodeHelper,
                                ListUtils.setOf(
                                        m.gcScopeAnnotations,
                                        ClassName.get(GcAllScope.class),
                                        cacheType.getGcScopeClassName()
                                ));
            } else if (m.provideAnnotation != null && m.provideAnnotation.cacheType == Provide.CacheType.Factory) {
                builder.provideFactory(m)
                        .mockControl(m);
            } else {
                ItemHolderCodeHelper.ItemCacheType cacheType = ItemHolderCodeHelper.cacheTypeFrom(
                        m.provideAnnotation != null ? m.provideAnnotation.cacheType : Provide.CacheType.Soft
                );
                ItemHolderCodeHelper itemHolderCodeHelper = ItemHolderCodeHelper.of(m.methodName + cacheFieldsCount, m.returnType, qFields, cacheType);
                builder.provideCached(m, itemHolderCodeHelper)
                        .cacheControl(m, itemHolderCodeHelper)
                        .switchRefFor(itemHolderCodeHelper,
                                ListUtils.setOf(
                                        m.gcScopeAnnotations,
                                        ClassName.get(GcAllScope.class),
                                        cacheType.getGcScopeClassName()
                                ));
            }
        }
        return builder;
    }


    public ModuleBuilder(ClassDetail orModuleCl, ClassName className) {
        this.orModuleCl = orModuleCl;
        this.className = className;
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
        if (orModuleCl != null) for (ClassDetail parentModule : orModuleCl.getAllParents(false)) {
            if (parentModule.moduleAnn != null)
                interfaces.add(ClassNameUtils.genCacheControlInterfaceModuleNameMirror(parentModule.className));
        }

        initMethod();
        initCachesFromModule();
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
                .addStatement("boolean $L = false", appliedLocalFieldName);

        if (orModuleCl != null) {
            builder
                    // check module class
                    .beginControlFlow("if ( (or instanceof $T) ) ", ClassNameUtils.genCacheControlInterfaceModuleNameMirror(orModuleCl.className))
                    .addStatement("$L = ($T) or", overridedModuleFieldName, ClassNameUtils.genCacheControlInterfaceModuleNameMirror(orModuleCl.className))
                    .addStatement("$L = ($T) (($T) or).getFactory() ", factoryFieldName, orModuleCl.className, IModule.class)
                    .addStatement("$L = true", appliedLocalFieldName)
                    .endControlFlow()
                    // check factory class
                    .beginControlFlow("else if (or instanceof $T) ", orModuleCl.className)
                    .addStatement("$L = ($T) or", factoryFieldName, orModuleCl.className)
                    .addStatement("$L = true", appliedLocalFieldName)
                    .endControlFlow()
                    // get module factory by module class
                    .beginControlFlow("else if (or instanceof Class<?> &&  $T.class.isAssignableFrom((Class<?>) or))", orModuleCl.className)
                    .beginControlFlow("try")
                    .addComment("looking for generated factory for module class")
                    .addStatement("Class<?> gennedClass = Class.forName(((Class) or).getCanonicalName() + \"StoneFactory\")")
                    .addStatement("$L = ($T) gennedClass.getConstructors()[0].newInstance()", factoryFieldName, orModuleCl.className)
                    .addStatement("$L = true", appliedLocalFieldName)
                    .endControlFlow()
                    .beginControlFlow("catch ( $T | $T | $T | $T | $T e)",
                        ArrayIndexOutOfBoundsException.class,
                        ClassNotFoundException.class,
                            InstantiationException.class,
                        IllegalAccessException.class,
                        InvocationTargetException.class)

                    .beginControlFlow("try")
                    .addComment("we don't got stone factory. We suppose class have constructor")
                    .addStatement("$L = ($T) ((Class<?>) or).getConstructors()[0].newInstance()", factoryFieldName, orModuleCl.className)
                    .addStatement("$L = true", appliedLocalFieldName)
                    .endControlFlow()
                    .beginControlFlow("catch ($T | $T | $T | $T ex)",
                        ArrayIndexOutOfBoundsException.class,
                            InstantiationException.class,
                        IllegalAccessException.class,
                        InvocationTargetException.class
                )
                    .endControlFlow()

                    .endControlFlow()
                    .endControlFlow();
        }

        builder.addStatement("return $L", appliedLocalFieldName);

        iModuleMethodBuilders.put(initMethodName, builder);
        return this;
    }


    public ModuleBuilder initCachesFromModule() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(initCachesFromMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(IModule.class, "m");

        collectRuns.add(() -> {
            if (orModuleCl != null) for (ClassDetail cl : orModuleCl.getAllParents(false)) {
                if (cl.moduleAnn == null)
                    continue;
                ClassName cacheControlInterfaceCl = ClassNameUtils.genCacheControlInterfaceModuleNameMirror(cl.className);
                builder.beginControlFlow("if ( m instanceof $T )", cacheControlInterfaceCl)
                        .addStatement("$T module = ($T) m", cacheControlInterfaceCl, cacheControlInterfaceCl);

                for (MethodDetail protoProvideMethod : cl.getAllMethods(false, false, "<init>")) {
                    String protoCacheControlMethodName = cacheControlMethodName(protoProvideMethod.methodName);
                    List<FieldDetail> qFields = ListUtils.filter(protoProvideMethod.args,
                            (inx, it) -> (it.type instanceof ClassName) && qualifiers.contains(it.type)
                    );
                    if (!qFields.isEmpty()) {
                        // not support qualifiers
                        // TODO write ingore test
                        continue;
                    }

                    builder.addStatement(
                            "$L( $T.setIfNullValueAction( module.$L( null ) ) )",
                            protoCacheControlMethodName, CacheAction.class, protoCacheControlMethodName
                    );

                }

                builder.endControlFlow();
            }
        });
        iModuleMethodBuilders.put(initCachesFromMethodName, builder);
        return this;
    }

    public ModuleBuilder bindMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(bindMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addAnnotation(BindInstance.class)
                .addAnnotation(Override.class)
                .addParameter(Object.class, "or")
                .addStatement("boolean $L = false", appliedLocalFieldName)
                .returns(boolean.class);

        if (fields.containsKey(overridedModuleFieldName))
            builder.beginControlFlow("if ( $L != null )", overridedModuleFieldName)
                    .addStatement("$L.bind(or)", overridedModuleFieldName)
                    .endControlFlow();

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
                .addStatement("return $L", fields.containsKey(factoryFieldName) ? factoryFieldName : "null");
        iModuleMethodBuilders.put(getFactoryMethodName, builder);
        return this;
    }

    public ModuleBuilder switchRefMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(switchRefMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(Set.class, Class.class), "scopes").build())
                .addParameter(ParameterSpec.builder(SwitchCacheParam.class, "__params").build())
                .returns(void.class);

        iModuleMethodBuilders.put(switchRefMethodName, builder);

        collectRuns.add(() -> {
            for (Set<TypeName> gcScopes : switchRefStatementBuilders.keySet()) {
                CodeBlock.Builder codeScopesList = CodeBlock.builder()
                        .add("$T.asList(", Arrays.class);
                int i = 0;
                for (TypeName sc : gcScopes) {
                    if (i++ > 0) codeScopesList.add(", ");
                    codeScopesList.add("$T.class", sc);
                }
                codeScopesList.add(")");

                builder.beginControlFlow("if ($L.containsAll(scopes))", codeScopesList.build())
                        .addCode(switchRefStatementBuilders.get(gcScopes).build())
                        .endControlFlow();
            }
        });
        return this;
    }


    public ModuleBuilder switchRefFor(ItemHolderCodeHelper fieldHelper, Set<TypeName> scopes) {
        switchRefStatementBuilders.putIfAbsent(scopes, CodeBlock.builder());
        switchRefStatementBuilders.get(scopes).add(fieldHelper.statementSwitchRef(CodeBlock.of("__params")));
        return this;
    }

    public ModuleBuilder bindInstance(MethodDetail m, ItemHolderCodeHelper itemHolderCodeHelper) {
        String cacheControlMethodName = cacheControlMethodName(m.methodName);
        cacheFields.add(itemHolderCodeHelper.cachedField());
        FieldDetail setValueArg = ListUtils.first(m.args, (inx, ob) -> Objects.equals(ob.type, m.returnType));
        List<FieldDetail> qFields = ListUtils.filter(m.args,
                (inx, it) -> (it.type instanceof ClassName) && qualifiers.contains(it.type)
        );

        //provide method
        MethodSpec.Builder provideMethodBuilder = MethodSpec.methodBuilder(m.methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(m.returnType);

        if (orModuleCl != null) provideMethodBuilder.addAnnotation(Override.class);


        if (m.args != null) for (FieldDetail p : m.args) {
            provideMethodBuilder.addParameter(p.type, p.name);
        }
        if (setValueArg != null) {
            provideMethodBuilder.beginControlFlow("if ($L != null)", setValueArg.name)
                    .addStatement(itemHolderCodeHelper.codeSetCachedValue(CodeBlock.of("$L", setValueArg.name)))
                    .endControlFlow();
        }
        provideMethodBuilder.addStatement("return $L", itemHolderCodeHelper.codeGetCachedValue());
        provideMethodBuilders.add(provideMethodBuilder);


        //bind item code
        MethodSpec.Builder bindMethodBuilder = iModuleMethodBuilders.get(bindMethodName);
        if (bindMethodBuilder != null) {
            bindMethodBuilder.beginControlFlow("if ($T.equals(or.getClass(), $T.class)) ", Objects.class, m.returnType)
                    .addStatement(itemHolderCodeHelper.codeSetCachedValue(
                            CodeBlock.of("($T) or", m.returnType)
                    ))
                    .addStatement("$L = true", appliedLocalFieldName)
                    .endControlFlow();
        }
        return this;
    }


    public ModuleBuilder provideFactory(MethodDetail m) {
        MethodSpec.Builder provideMethodBuilder = MethodSpec.methodBuilder(m.methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(m.returnType)
                .addStatement("return  $L.$L($L)", factoryFieldName, m.methodName,
                        String.join(",", ListUtils.format(m.args, (it) -> it.name)));
        if (orModuleCl != null) provideMethodBuilder.addAnnotation(Override.class);
        if (m.args != null) for (FieldDetail p : m.args) {
            provideMethodBuilder.addParameter(p.type, p.name);
        }

        //todo provide from overridedModuleFieldName

        provideMethodBuilders.add(provideMethodBuilder);
        return this;
    }

    public ModuleBuilder provideCached(MethodDetail m, ItemHolderCodeHelper itemHolderCodeHelper) {
        String cacheControlMethodName = cacheControlMethodName(m.methodName);
        List<FieldDetail> qFields = ListUtils.filter(m.args,
                (inx, it) -> (it.type instanceof ClassName) && qualifiers.contains(it.type)
        );

        cacheFields.add(itemHolderCodeHelper.cachedField());
        MethodSpec.Builder provideMethodBuilder = MethodSpec.methodBuilder(m.methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .returns(m.returnType);
        if (orModuleCl != null) provideMethodBuilder.addAnnotation(Override.class);

        if (m.args != null) for (FieldDetail p : m.args) {
            provideMethodBuilder.addParameter(p.type, p.name);
        }

        if (fields.containsKey(overridedModuleFieldName)) {
            // check cached from overrided module
            provideMethodBuilder
                    .beginControlFlow(
                            "if ($L != null && $L.$L( null  $L  ) != null ) ",
                            overridedModuleFieldName, overridedModuleFieldName,
                            cacheControlMethodName,
                            String.join("", ListUtils.format(qFields, (it) -> ", " + it.name))
                    )
                    .addStatement(
                            "return $L.$L( null  $L ) ",
                            overridedModuleFieldName,
                            cacheControlMethodName,
                            String.join("", ListUtils.format(qFields, (it) -> ", " + it.name))
                    )
                    .endControlFlow();
        }

        //  check cached
        provideMethodBuilder
                .addCode("if ( $L != null )", itemHolderCodeHelper.codeGetCachedValue())
                .addStatement("return $L ", itemHolderCodeHelper.codeGetCachedValue());

        if (fields.containsKey(overridedModuleFieldName)) {
            // return from overrided module
            provideMethodBuilder
                    .beginControlFlow("if ($L != null) ", overridedModuleFieldName)
                    .addStatement(
                            "return $L.$L( $L ) ",
                            overridedModuleFieldName, m.methodName,
                            String.join(",", ListUtils.format(m.args, (it) -> it.name))
                    )
                    .endControlFlow();
        }

        // gen new and return
        provideMethodBuilder.addStatement("return $L", itemHolderCodeHelper.codeSetCachedValue(
                CodeBlock.of(
                        "$L.$L($L)",
                        factoryFieldName, m.methodName,
                        String.join(",", ListUtils.format(m.args, (it) -> it.name))
                )
        ));


        provideMethodBuilders.add(provideMethodBuilder);
        return this;
    }


    public ModuleBuilder cacheControl(MethodDetail m, ItemHolderCodeHelper itemHolderCodeHelper) {
        String cacheControlMethodName = cacheControlMethodName(m.methodName);
        List<FieldDetail> qFields = ListUtils.filter(m.args,
                (inx, it) -> (it.type instanceof ClassName) && qualifiers.contains(it.type)
        );

        MethodSpec.Builder cacheControldMethodBuilder = MethodSpec.methodBuilder(cacheControlMethodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(m.returnType)
                .addParameter(ParameterSpec.builder(CacheAction.class, "__action").build());
        if (orModuleCl != null) cacheControldMethodBuilder.addAnnotation(Override.class);
        for (FieldDetail q : qFields) {
            cacheControldMethodBuilder.addParameter(q.type, q.name);
        }

        if (fields.containsKey(overridedModuleFieldName)) {
            cacheControldMethodBuilder
                    // invoke for overrided module
                    .beginControlFlow("if ($L != null)", overridedModuleFieldName)
                    .addStatement(
                            "$L.$L($L)",
                            overridedModuleFieldName,
                            cacheControlMethodName,
                            String.join(",", ListUtils.format(
                                    cacheControldMethodBuilder.parameters,
                                    (ListUtils.IFormat<ParameterSpec, CharSequence>) it -> it.name)
                            )
                    )
                    .endControlFlow();
        }

        cacheControldMethodBuilder
                .beginControlFlow("if (__action != null) switch (__action.type)")
                //set value
                .beginControlFlow("case SET_VALUE:")
                .beginControlFlow("if (__action.value instanceof $T)", m.returnType)
                .addStatement(itemHolderCodeHelper.codeSetCachedValue(
                        CodeBlock.of("( $T ) __action.value", m.returnType)
                ))
                .endControlFlow()
                .addStatement("break")
                .endControlFlow()
                //set if null value
                .beginControlFlow("case SET_IF_NULL:")
                .beginControlFlow("if (__action.value instanceof $T)", m.returnType)
                .addStatement(itemHolderCodeHelper.codeSetCachedIfNullValue(
                        CodeBlock.of("( $T ) __action.value", m.returnType)
                ))
                .endControlFlow()
                .addStatement("break")
                .endControlFlow()
                // switch cache type
                .beginControlFlow("case SWITCH_CACHE:")
                .addCode(itemHolderCodeHelper.statementSwitchRef(CodeBlock.of("__action.swCacheParams")))
                .addStatement("break")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return $L ", itemHolderCodeHelper.codeGetCachedValue());

        cacheControlMethodBuilders.add(cacheControldMethodBuilder);
        return this;
    }


    public ModuleBuilder mockControl(MethodDetail m) {
        String cacheControlMethodName = cacheControlMethodName(m.methodName);
        List<FieldDetail> qFields = ListUtils.filter(m.args,
                (inx, it) -> (it.type instanceof ClassName) && qualifiers.contains(it.type)
        );

        MethodSpec.Builder cacheControldMethodBuilder = MethodSpec.methodBuilder(cacheControlMethodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(m.returnType)
                .addParameter(ParameterSpec.builder(CacheAction.class, "__action").build())
                .addStatement("return null");
        if (orModuleCl != null) cacheControldMethodBuilder.addAnnotation(Override.class);

        for (FieldDetail q : qFields) {
            cacheControldMethodBuilder.addParameter(q.type, q.name);
        }
        provideMethodBuilders.add(cacheControldMethodBuilder);
        return this;
    }

    public ModuleBuilder collect() {
        for (Runnable r : collectRuns)
            r.run();
        collectRuns.clear();
        return this;
    }

    public TypeSpec build(boolean collectAll) {
        if (collectAll) {
            collect();
        }

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className);
        typeSpecBuilder.addModifiers(Modifier.PUBLIC);
        if (orModuleCl != null) {
            if (orModuleCl.isInterfaceClass())
                typeSpecBuilder.addSuperinterface(orModuleCl.className);
            else typeSpecBuilder.superclass(orModuleCl.className);
        }
        for (TypeName supInterface : interfaces)
            typeSpecBuilder.addSuperinterface(supInterface);

        for (FieldSpec.Builder fieldBuilder : fields.values())
            typeSpecBuilder.addField(fieldBuilder.build());
        for (FieldSpec.Builder cacheFieldBuilder : cacheFields)
            typeSpecBuilder.addField(cacheFieldBuilder.build());

        List<MethodSpec.Builder> methods = new LinkedList<>();
        methods.addAll(iModuleMethodBuilders.values());
        methods.addAll(provideMethodBuilders);
        methods.addAll(cacheControlMethodBuilders);

        for (MethodSpec.Builder provideMethod : methods)
            typeSpecBuilder.addMethod(provideMethod.build());

        return typeSpecBuilder.build();
    }

    public TypeSpec buildAndWrite() {
        TypeSpec typeSpec = build(true);
        if (typeSpec != null) {
            CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
        }
        return typeSpec;
    }


}
