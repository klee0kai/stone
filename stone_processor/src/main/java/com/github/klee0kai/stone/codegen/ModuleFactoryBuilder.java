package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone._hidden_.IModuleFactory;
import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.exceptions.ObjectNotProvidedException;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.helpers.wrap.WrapHelper;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.BindInstanceAnn;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.LinkedList;
import java.util.List;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;
import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;
import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.nonWrappedType;
import static com.github.klee0kai.stone.utils.StoneNamingUtils.genFactoryNameMirror;
import static com.squareup.javapoet.MethodSpec.methodBuilder;

public class ModuleFactoryBuilder {

    public final ClassDetail orFactory;

    public ClassName className;

    public boolean needBuild = false;

    public final List<MethodSpec.Builder> provideMethodBuilders = new LinkedList<>();

    public static ModuleFactoryBuilder fromModule(ClassDetail module) {
        ModuleFactoryBuilder builder = new ModuleFactoryBuilder(module);
        builder.needBuild = module.isAbstractClass() || module.isInterfaceClass();
        ClassDetail defaultImpl = allClassesHelper.tryFindForType(((ClassName) module.className).nestedClass("DefaultImpls"));
        if (builder.needBuild) {
            builder.className = genFactoryNameMirror(module.className);
            for (MethodDetail m : module.getAllMethods(false, false, "<init>")) {
                if (!m.isAbstract() && !module.isInterfaceClass())
                    continue;
                boolean processed = false;
                if (m.hasAnnotations(BindInstanceAnn.class)) {
                    builder.provideNullMethod(m);
                    processed = true;
                }
                if (!processed && defaultImpl != null) {
                    MethodDetail defProvideMethod = MethodDetail.simpleName(m.methodName);
                    defProvideMethod.args.add(FieldDetail.simple("thhhis", module.className));
                    defProvideMethod.args.addAll(m.args);
                    if (defaultImpl.findMethod(defProvideMethod, false) != null) {
                        builder.provideMethodFrom(m, defaultImpl);
                        processed = true;
                    }
                }
                if (!processed) {
                    builder.provideMethod(m);
                }
            }
        }
        return builder;
    }

    public ModuleFactoryBuilder(ClassDetail orFactory) {
        this.orFactory = orFactory;
        this.className = (ClassName) orFactory.className;
    }

    public ModuleFactoryBuilder provideMethodFrom(MethodDetail m, ClassDetail defaultImpl) {
        ClassDetail providingClass = allClassesHelper.findForType(nonWrappedType(m.returnType));
        MethodSpec.Builder builder = methodBuilder(m.methodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(m.returnType);
        for (FieldDetail p : m.args)
            builder.addParameter(p.type, p.name);

        String argStr = m.args == null ? "" : String.join("", ListUtils.format(m.args, (it) -> ", " + it.name));
        SmartCode genCode = SmartCode.builder()
                .add(CodeBlock.of("$T.$L( null $L )", defaultImpl.className, m.methodName, argStr))
                .providingType(providingClass.className);

        builder.addCode(
                SmartCode.builder()
                        .add("return ")
                        .add(WrapHelper.transform(genCode, m.returnType))
                        .add(";\n")
                        .build(m.args)
        );


        provideMethodBuilders.add(builder);
        return this;
    }

    public ModuleFactoryBuilder provideMethod(MethodDetail m) {
        ClassDetail providingClass = allClassesHelper.findForType(nonWrappedType(m.returnType));
        boolean hasConstructor = providingClass.findMethod(MethodDetail.constructorMethod(m.args), false) != null;
        if (!hasConstructor) {
            List<String> argTypes = ListUtils.format(m.args, (it) -> it.type.toString());
            throw new ObjectNotProvidedException(
                    createErrorMes()
                            .constructorNonFound(providingClass.className.toString(), argTypes)
                            .build(),
                    m.sourceEl
            );
        }

        MethodSpec.Builder builder = methodBuilder(m.methodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(m.returnType);
        for (FieldDetail p : m.args)
            builder.addParameter(p.type, p.name);

        String argStr = m.args == null ? "" : String.join(",", ListUtils.format(m.args, (it) -> it.name));
        SmartCode genCode = SmartCode.builder()
                .add(CodeBlock.of("new $T( $L )", providingClass.className, argStr))
                .providingType(providingClass.className);

        builder.addCode(
                SmartCode.builder()
                        .add("return ")
                        .add(WrapHelper.transform(genCode, m.returnType))
                        .add(";\n")
                        .build(m.args)
        );


        provideMethodBuilders.add(builder);
        return this;
    }

    public ModuleFactoryBuilder provideNullMethod(MethodDetail m) {
        MethodSpec.Builder builder = methodBuilder(m.methodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(m.returnType)
                .addStatement("return null");

        if (m.args != null) for (FieldDetail p : m.args)
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

    public TypeSpec buildAndWrite() {
        TypeSpec typeSpec = build();
        if (typeSpec != null) {
            CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
        }
        return typeSpec;
    }


}
