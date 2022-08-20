package com.klee0kai.stone.codegen;

import com.klee0kai.stone.AnnotationProcessor;
import com.klee0kai.stone.annotations.Component;
import com.klee0kai.stone.container.ItemsWeakContainer;
import com.klee0kai.stone.interfaces.IComponent;
import com.klee0kai.stone.model.ClassDetail;
import com.klee0kai.stone.model.MethodDetail;
import com.klee0kai.stone.utils.ClassNameUtils;
import com.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.annotation.processing.Generated;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ComponentGen {

    private ProcessingEnvironment env;
    private Messager messager;

    private final List<ClassDetail> classes = new LinkedList<>();

    public void init(ProcessingEnvironment env) {
        this.env = env;
        messager = env.getMessager();
    }

    public void addComponent(ClassDetail... classDetails) {
        this.classes.addAll(Arrays.asList(classDetails));

    }

    public void genCode() {
        AnnotationSpec codeGenAnnot = AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", ModuleGen.class.getCanonicalName())
                .addMember("comments", "$S", AnnotationProcessor.PROJECT_URL)
                .addMember("date", "$S", new SimpleDateFormat().format(Calendar.getInstance().getTime()))
                .build();
        String prefixFieldName = "prefix";
        String recursiveFieldName = "recursiveBlock";
        String relatedFieldName = "related";
        String iModuleInit = "init";
        String iModulePrefix = "prefix";
        String iModuleRelateTo = "relateTo";

        for (ClassDetail cl : classes) {
            TypeSpec.Builder compBuilder = TypeSpec.classBuilder(ClassNameUtils.genClassNameMirror(cl.classType))
                    .addAnnotation(codeGenAnnot)
                    .addSuperinterface(cl.classType)
                    .addSuperinterface(IComponent.class)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            compBuilder.addField(FieldSpec.builder(String.class, prefixFieldName, Modifier.PRIVATE)
                    .initializer("$S", "1")
                    .build());
            compBuilder.addField(FieldSpec.builder(boolean.class, recursiveFieldName, Modifier.PRIVATE)
                    .initializer("false").build());
            compBuilder.addField(FieldSpec.builder(
                            ParameterizedTypeName.get(LinkedList.class, IComponent.class), relatedFieldName, Modifier.PRIVATE)
                    .initializer("new $T<$T>()", LinkedList.class, IComponent.class)
                    .build());


            for (MethodDetail m : cl.methods) {
                compBuilder.addField(
                        FieldSpec.builder(ClassNameUtils.genClassNameMirror(m.returnType), m.methodName, Modifier.PRIVATE, Modifier.FINAL)
                                .initializer("new $T()", ClassNameUtils.genClassNameMirror(m.returnType))
                                .build());
            }


            compBuilder.addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("super()")
                    .build());

            MethodSpec.Builder initMethodBuilder = MethodSpec.methodBuilder(iModuleInit)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(Object[].class, "modules")
                    .varargs(true)
                    .addStatement("if ($L) return", recursiveFieldName)
                    .addStatement("$L = true", recursiveFieldName)
                    .beginControlFlow("if (modules != null) for (Object m :modules)");
            for (MethodDetail m : cl.methods)
                initMethodBuilder.addStatement("if ($L!=null) $L.init(m)", m.methodName, m.methodName);
            initMethodBuilder
                    .endControlFlow()
                    .beginControlFlow("if ($L != null ) for ($T r: $L) ", relatedFieldName, IComponent.class, relatedFieldName)
                    .addStatement("r.init(modules)")
                    .endControlFlow()
                    .addStatement("$L = false", recursiveFieldName);
            compBuilder.addMethod(initMethodBuilder.build());

            compBuilder.addMethod(MethodSpec.methodBuilder(iModulePrefix)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return $L", prefixFieldName)
                    .build());

            compBuilder.addMethod(MethodSpec.methodBuilder(iModuleRelateTo)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(IComponent[].class, "components")
                    .varargs(true)
                    .addStatement("if ($L) return", recursiveFieldName)
                    .addStatement("$L = true", recursiveFieldName)
                    .addStatement("int relateId = $T.parseInt($L) ", Integer.class, prefixFieldName)
                    .beginControlFlow("if (components!=null) for ($T c :components)", IComponent.class)
                    .beginControlFlow("if (this.$L.contains(c))", relatedFieldName)
                    .addStatement("$L = false", recursiveFieldName)
                    .addStatement("return")
                    .endControlFlow()
                    .addStatement("if ($T.valueOf(relateId).equals(c.prefix())) relateId++", String.class)
                    .addStatement("this.$L.add(c)", relatedFieldName)
                    .addStatement("c.init($L)", cl.methods.stream().map(m -> m.methodName)
                            .collect(Collectors.joining(",")))
                    .endControlFlow()
                    .addStatement("this.$L = $T.valueOf(relateId)", prefixFieldName, String.class)
                    .beginControlFlow("if (components!=null) for ($T c :components)", IComponent.class)
                    .addStatement("c.relateTo(this)")
                    .endControlFlow()
                    .addStatement("$L = false", recursiveFieldName)
                    .build());


            for (MethodDetail m : cl.methods) {
                if (Objects.equals(m.methodName, "<init>"))
                    continue;

                compBuilder.addMethod(
                        MethodSpec.methodBuilder(m.methodName)
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(m.returnType)
                                .addStatement("return $L", m.methodName)
                                .build());
            }

            CodeFileUtil.writeToJavaFile(cl.classType.packageName(), compBuilder.build());

        }
    }

}
