package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.annotations.component.GcAllScope;
import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.closed.IModule;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.closed.types.TimeScheduler;
import com.github.klee0kai.stone.codegen.helpers.ItemHolderCodeHelper;
import com.github.klee0kai.stone.model.annotations.BindInstanceAnnotation;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.*;

import static com.github.klee0kai.stone.codegen.ModuleBuilder.*;

/**
 * hidden component's module.
 * There we provide bindinstance objects which are declared only in component
 */
public class ModuleHiddenBuilder {

    private static final String appliedLocalFieldName = "applied";


    public ClassName className;

    public final Set<TypeName> interfaces = new HashSet<>();
    public final Set<ClassName> qualifiers = new HashSet<>();


    // ---------------------- common fields and method  ----------------------------------
    public final HashMap<String, FieldSpec.Builder> fields = new HashMap<>();

    public final HashMap<String, MethodSpec.Builder> iModuleMethodBuilders = new HashMap<>();


    // ---------------------- provide fields and method  ----------------------------------
    public final List<FieldSpec.Builder> cacheFields = new LinkedList<>();
    public final List<MethodSpec.Builder> provideMethodBuilders = new LinkedList<>();

    public final HashMap<Set<TypeName>, CodeBlock.Builder> switchRefStatementBuilders = new HashMap<>();

    private final LinkedList<Runnable> collectRuns = new LinkedList<>();

    public ModuleHiddenBuilder(ClassName className) {
        this.className = className;
    }

    public ModuleHiddenBuilder implementIModule() {
        interfaces.add(ClassName.get(IModule.class));

        initMethod();
        bindMethod();
        getFactoryMethod();
        switchRefMethod();
        return this;
    }


    public ModuleHiddenBuilder initMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(initMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addAnnotation(Override.class)
                .returns(boolean.class)
                .addParameter(Object.class, "or")
                .addStatement("return false");

        iModuleMethodBuilders.put(initMethodName, builder);
        return this;
    }

    public ModuleHiddenBuilder bindMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(bindMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addAnnotation(BindInstance.class)
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


    public ModuleHiddenBuilder getFactoryMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(getFactoryMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(Object.class)
                .addStatement("return null");
        iModuleMethodBuilders.put(getFactoryMethodName, builder);
        return this;
    }


    public ModuleHiddenBuilder switchRefMethod() {
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

    public ModuleHiddenBuilder switchRefFor(ItemHolderCodeHelper fieldHelper, Set<TypeName> scopes) {
        switchRefStatementBuilders.putIfAbsent(scopes, CodeBlock.builder());
        switchRefStatementBuilders.get(scopes).add(
                fieldHelper.statementSwitchRef(
                        "cache",
                        "scheduler",
                        "time"
                ));
        return this;
    }

    public ModuleHiddenBuilder bindInstanceAndSwitchRef(
            String methodName,
            TypeName typeName,
            BindInstanceAnnotation bindInstanceAnnotation,
            List<TypeName> scopes
    ) {
        ItemHolderCodeHelper.ItemCacheType cacheType = ItemHolderCodeHelper.cacheTypeFrom(bindInstanceAnnotation.cacheType);
        ItemHolderCodeHelper itemHolderCodeHelper = ItemHolderCodeHelper.of(methodName + provideMethodBuilders.size(), typeName, null, cacheType);
        bindInstance(methodName, typeName, itemHolderCodeHelper);

        switchRefFor(itemHolderCodeHelper, ListUtils.setOf(
                scopes,
                ClassName.get(GcAllScope.class),
                cacheType.getGcScopeClassName()
        ));
        return this;
    }

    public ModuleHiddenBuilder bindInstance(String name, TypeName typeName, ItemHolderCodeHelper itemHolderCodeHelper) {
        cacheFields.add(itemHolderCodeHelper.cachedField());

        //provide method
        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addAnnotation(BindInstance.class)
                .returns(typeName)
                .addStatement("return $L", itemHolderCodeHelper.codeGetCachedValue()));

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
                .returns(typeName)
                .addStatement("return $L", itemHolderCodeHelper.codeGetCachedValue());

        provideMethodBuilders.add(getCachedMethodBuilder);

        // set cached method
        String setCachedMethodName = setBindInstanceCachedMethodName(name);
        MethodSpec.Builder setCachedMethodBuilder = MethodSpec.methodBuilder(setCachedMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addParameter(ParameterSpec.builder(typeName, "arg").build())
                .addStatement(itemHolderCodeHelper.codeSetCachedValue(CodeBlock.of("arg")));
        provideMethodBuilders.add(setCachedMethodBuilder);

        // set cache type  method
        String setCacheTypeMethodName = ModuleInterfaceBuilder.getSwitchCacheMethodName(name);
        MethodSpec.Builder setCacheTypeMethodBuilder = MethodSpec.methodBuilder(setCacheTypeMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                .addParameter(ParameterSpec.builder(SwitchCache.CacheType.class, "cache").build())
                .addParameter(ParameterSpec.builder(TimeScheduler.class, "scheduler").build())
                .addParameter(ParameterSpec.builder(long.class, "time").build())
                .addCode(itemHolderCodeHelper.statementSwitchRef("cache", "scheduler", "time"));
        provideMethodBuilders.add(setCacheTypeMethodBuilder);
        return this;
    }

    public ModuleHiddenBuilder collect() {
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

    public TypeSpec buildAndWrite() {
        TypeSpec typeSpec = build(true);
        if (typeSpec != null) {
            CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
        }
        return typeSpec;
    }

}
