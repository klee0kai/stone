package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.closed.IModuleFactory;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
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

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;

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
            for (MethodDetail m : module.getAllMethods(false, "<init>")) {
                if (!m.isAbstract() && !module.isInterfaceClass())
                    continue;
                ClassDetail providingClass = allClassesHelper.findForType(m.returnType);
                boolean hasConstructor = providingClass.findMethod(MethodDetail.constructorMethod(m.args), false) != null;
                if (m.bindInstanceAnnotation != null || !hasConstructor) {
                    builder.provideNullMethod(m.methodName, m.returnType, m.args);
                } else {
                    builder.provideMethod(m.methodName, m.returnType, m.args);
                }
            }
        }
        return builder;
    }

    public ModuleFactoryBuilder(ClassDetail orFactory) {
        this.orFactory = orFactory;
        this.className = orFactory.className;
    }


    public ModuleFactoryBuilder provideMethod(String name, TypeName provideCl, List<FieldDetail> args) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(provideCl);

        if (args == null || args.isEmpty()) {
            builder.addStatement("return new $T()", provideCl);
        } else {
            for (FieldDetail p : args)
                builder.addParameter(p.type, p.name);
            builder.addStatement("return new $T($L)", provideCl,
                    String.join(",", ListUtils.format(args, (it) -> it.name)));
        }


        provideMethodBuilders.add(builder);
        return this;
    }

    public ModuleFactoryBuilder provideNullMethod(String name, TypeName provideCl, List<FieldDetail> args) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(provideCl)
                .addStatement("return null");

        if (args != null) for (FieldDetail p : args)
            builder.addParameter(p.type, p.name);

        provideMethodBuilders.add(builder);
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
        typeSpecBuilder.addSuperinterface(IModuleFactory.class);

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
