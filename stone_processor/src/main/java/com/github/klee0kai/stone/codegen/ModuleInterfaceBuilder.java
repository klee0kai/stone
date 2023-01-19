package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.closed.IModule;
import com.github.klee0kai.stone.closed.types.TimeScheduler;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.*;

import static com.github.klee0kai.stone.codegen.ModuleBuilder.*;

public class ModuleInterfaceBuilder {

    public final ClassDetail orModuleCl;

    public ClassName className;


    public final Set<TypeName> interfaces = new HashSet<>();

    public final HashMap<String, MethodSpec.Builder> iModuleMethodBuilders = new HashMap<>();


    // ---------------------- provide fields and method  ----------------------------------
    public final List<MethodSpec.Builder> provideMethodBuilders = new LinkedList<>();


    public static ModuleInterfaceBuilder from(ModuleFactoryBuilder factoryBuilder) {
        ModuleInterfaceBuilder builder = new ModuleInterfaceBuilder(factoryBuilder.orFactory)
                .implementIModule();
        for (MethodDetail m : factoryBuilder.orFactory.getAllMethods(false)) {
            if (Objects.equals(m.methodName, "<init>"))
                continue;
            builder.provideMethod(m.methodName, m.returnType, m.args);
        }
        return builder;
    }


    public ModuleInterfaceBuilder(ClassDetail orModuleCl) {
        this.orModuleCl = orModuleCl;
        this.className = ClassNameUtils.genInterfaceModuleNameMirror(orModuleCl.className);
    }

    /**
     * Call first
     * add IModule methods.
     *
     * @return
     */
    public ModuleInterfaceBuilder implementIModule() {
        interfaces.add(ClassName.get(IModule.class));
        initMethod();
        bindMethod();
        getFactoryMethod();
        switchRefMethod();
        return this;
    }

    public ModuleInterfaceBuilder initMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(initMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(boolean.class)
                .addParameter(Object.class, "or");

        iModuleMethodBuilders.put(initMethodName, builder);
        return this;
    }

    public ModuleInterfaceBuilder bindMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(bindMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(Object.class, "or")
                .returns(boolean.class);


        iModuleMethodBuilders.put(bindMethodName, builder);
        return this;
    }


    public ModuleInterfaceBuilder getFactoryMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(getFactoryMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(Object.class);
        iModuleMethodBuilders.put(getFactoryMethodName, builder);
        return this;
    }

    public ModuleInterfaceBuilder switchRefMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(switchRefMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(Set.class, Class.class), "scopes").build())
                .addParameter(ParameterSpec.builder(SwitchCache.CacheType.class, "cache").build())
                .addParameter(ParameterSpec.builder(TimeScheduler.class, "scheduler").build())
                .addParameter(ParameterSpec.builder(long.class, "time").build())
                .returns(void.class);
        iModuleMethodBuilders.put(switchRefMethodName, builder);
        return this;
    }


    public ModuleInterfaceBuilder provideMethod(String name, TypeName typeName, List<FieldDetail> args) {
        String getCachedMethodName = getCachedMethodName(name);
        String setCacheTypeMethodName = getSwitchCacheMethodName(name);

        MethodSpec.Builder provideMethodBuilder = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(typeName);

        MethodSpec.Builder getCachedMethodBuilder = MethodSpec.methodBuilder(getCachedMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(typeName);

        MethodSpec.Builder setCacheTypeMethodBuilder = MethodSpec.methodBuilder(setCacheTypeMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

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

    public TypeSpec build() {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.interfaceBuilder(className);

        typeSpecBuilder.addModifiers(Modifier.PUBLIC);

        for (TypeName supInterface : interfaces)
            typeSpecBuilder.addSuperinterface(supInterface);

        for (MethodSpec.Builder iModuleMethod : iModuleMethodBuilders.values())
            typeSpecBuilder.addMethod(iModuleMethod.build());

        for (MethodSpec.Builder provideMethod : provideMethodBuilders)
            typeSpecBuilder.addMethod(provideMethod.build());

        return typeSpecBuilder.build();
    }

    public TypeSpec buildAndWrite() {
        TypeSpec typeSpec = build();
        if (typeSpec != null) {
            CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
        }
        return typeSpec;
    }


    public static String getCachedMethodName(String factoryMethodName) {
        return "__" + factoryMethodName + "_cache";
    }

    public static String getSwitchCacheMethodName(String factoryMethodName) {
        return "__" + factoryMethodName + "_switchCache";
    }
}
