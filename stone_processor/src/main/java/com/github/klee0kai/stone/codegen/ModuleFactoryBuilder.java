package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.util.LinkedList;
import java.util.List;

public class ModuleFactoryBuilder {

    public final ClassDetail orFactory;

    public ClassName className;

    public boolean needBuild = false;

    public final List<MethodSpec.Builder> provideMethodBuilders = new LinkedList<>();

    public static ModuleFactoryBuilder fromModule(ClassDetail module) {
        ModuleFactoryBuilder builder = new ModuleFactoryBuilder(module);
        builder.needBuild = module.isAbstractClass() || module.isInterfaceClass();
        if (builder.needBuild) {
            builder.className = ClassNameUtils.genFactoryNameMirror(module.className);
            for (MethodDetail m : module.getAllMethods(false, "<init>"))
                if (m.bindInstanceAnnotation != null)
                    builder.provideNullMethod(m.methodName, m.returnType);
                else builder.provideMethod(m.methodName, m.returnType);
        }
        return builder;
    }

    public ModuleFactoryBuilder(ClassDetail orFactory) {
        this.orFactory = orFactory;
        this.className = orFactory.className;
    }


    public ModuleFactoryBuilder fabricMethode(String name, TypeName typeName) {
        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(typeName)
                .addStatement("return new $T()", typeName));
        return this;
    }

    public ModuleFactoryBuilder provideMethod(String name, TypeName provideCl) {
        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(provideCl)
                .addStatement("return new $T()", provideCl));
        return this;
    }

    public ModuleFactoryBuilder provideNullMethod(String name, TypeName provideCl) {
        provideMethodBuilders.add(MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(provideCl)
                .addStatement("return null"));
        return this;
    }


    public TypeSpec build() {
        if (!needBuild)
            return null;
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className);
        typeSpecBuilder.addModifiers(Modifier.PUBLIC);
        if (orFactory.isInterfaceClass())
            typeSpecBuilder.addSuperinterface(orFactory.className);
        else typeSpecBuilder.superclass(orFactory.className);

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
