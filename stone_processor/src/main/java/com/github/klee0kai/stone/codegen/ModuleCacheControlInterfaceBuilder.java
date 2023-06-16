package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.closed.types.CacheAction;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.closed.types.SwitchCacheParam;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.*;

import static com.github.klee0kai.stone.codegen.ModuleBuilder.bindMethodName;
import static com.github.klee0kai.stone.codegen.ModuleBuilder.switchRefMethodName;
import static com.github.klee0kai.stone.utils.StoneNamingUtils.genCacheControlInterfaceModuleNameMirror;

public class ModuleCacheControlInterfaceBuilder {

    public final ClassDetail orModuleCl;

    public ClassName className;


    public final Set<TypeName> interfaces = new HashSet<>();

    public Set<ClassName> qualifiers = new HashSet<>();
    public final HashMap<String, MethodSpec.Builder> iModuleMethodBuilders = new HashMap<>();

    // ---------------------- provide fields and method  ----------------------------------
    public final List<MethodSpec.Builder> methodBuilders = new LinkedList<>();


    public static ModuleCacheControlInterfaceBuilder from(ModuleFactoryBuilder factoryBuilder, List<ClassName> allQualifiers) {
        ModuleCacheControlInterfaceBuilder builder = new ModuleCacheControlInterfaceBuilder(factoryBuilder.orFactory);
        builder.qualifiers.addAll(allQualifiers);

        builder.bindMethod()
                .switchRefMethod();
        for (MethodDetail m : factoryBuilder.orFactory.getAllMethods(false, false)) {
            if (Objects.equals(m.methodName, "<init>"))
                continue;
            builder.provideMethod(m.methodName, m.returnType, m.args)
                    .cacheControlMethod(m.methodName, m.returnType, m.args);
        }
        return builder;
    }


    public ModuleCacheControlInterfaceBuilder(ClassDetail orModuleCl) {
        this.orModuleCl = orModuleCl;
        this.className = genCacheControlInterfaceModuleNameMirror(orModuleCl.className);
    }

    public ModuleCacheControlInterfaceBuilder bindMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(bindMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(Object.class, "or")
                .returns(boolean.class);

        iModuleMethodBuilders.put(bindMethodName, builder);

        return this;
    }

    public ModuleCacheControlInterfaceBuilder switchRefMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(switchRefMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(Set.class, Class.class), "scopes").build())
                .addParameter(ParameterSpec.builder(SwitchCacheParam.class, "__params").build())
                .returns(void.class);

        iModuleMethodBuilders.put(switchRefMethodName, builder);
        return this;
    }


    public ModuleCacheControlInterfaceBuilder provideMethod(String name, TypeName typeName, List<FieldDetail> args) {
        MethodSpec.Builder provideMethodBuilder = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(typeName);

        if (args != null) for (FieldDetail p : args) {
            provideMethodBuilder.addParameter(p.type, p.name);
        }

        methodBuilders.add(provideMethodBuilder);
        return this;
    }

    public ModuleCacheControlInterfaceBuilder cacheControlMethod(String name, TypeName typeName, List<FieldDetail> args) {
        String cacheControlMethodName = cacheControlMethodName(name);
        List<FieldDetail> qFields = ListUtils.filter(args,
                (inx, it) -> (it.type instanceof ClassName) && qualifiers.contains(it.type)
        );

        MethodSpec.Builder cacheControldMethodBuilder = MethodSpec.methodBuilder(cacheControlMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(typeName)
                .addParameter(ParameterSpec.builder(CacheAction.class, "__action").build());
        for (FieldDetail q : qFields) {
            cacheControldMethodBuilder.addParameter(q.type, q.name);
        }

        methodBuilders.add(cacheControldMethodBuilder);
        return this;
    }

    public TypeSpec build() {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.interfaceBuilder(className);

        typeSpecBuilder.addModifiers(Modifier.PUBLIC);

        for (TypeName supInterface : interfaces)
            typeSpecBuilder.addSuperinterface(supInterface);

        for (MethodSpec.Builder iModuleMethod : iModuleMethodBuilders.values())
            typeSpecBuilder.addMethod(iModuleMethod.build());

        for (MethodSpec.Builder provideMethod : methodBuilders)
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


    public static String cacheControlMethodName(String factoryMethodName) {
        return "__" + factoryMethodName + "_cache";
    }

}
