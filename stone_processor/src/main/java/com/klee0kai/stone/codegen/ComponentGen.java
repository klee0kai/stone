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
        String iModuleInit = "init";

        for (ClassDetail cl : classes) {
            TypeSpec.Builder compBuilder = TypeSpec.classBuilder(ClassNameUtils.genClassNameMirror(cl.classType))
                    .addAnnotation(codeGenAnnot)
                    .addSuperinterface(cl.classType)
                    .addSuperinterface(IComponent.class)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addField(FieldSpec.builder(String.class, prefixFieldName, Modifier.PROTECTED)
                            .initializer("$S", 1)
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
                    .beginControlFlow("if (modules != null) for (Object m :modules)");
            for (MethodDetail m : cl.methods)
                initMethodBuilder.addStatement("if ($L != null ) $L.init(m)", m.methodName, m.methodName);
            initMethodBuilder
                    .endControlFlow();

            compBuilder.addMethod(initMethodBuilder.build());

            MethodSpec.Builder initByOtherDI = MethodSpec.methodBuilder(iModuleInit)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(IComponent.class, "c")
                    .addStatement("if (!(c instanceof $T)) return", cl.classType);
            for (MethodDetail m : cl.methods)
                initByOtherDI.addStatement("$L.init((($T)c).$L())", m.methodName, cl.classType, m.methodName);
            compBuilder.addMethod(initByOtherDI.build());


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
