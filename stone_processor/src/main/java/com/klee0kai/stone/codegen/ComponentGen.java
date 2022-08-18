package com.klee0kai.stone.codegen;

import com.klee0kai.stone.AnnotationProcessor;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

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
        ClassName itemsWeakContainerClass = ClassName.get(ItemsWeakContainer.class);
        String orClassFieldName = "originalClass";
        String iModuleInit = "init";

        for (ClassDetail cl : classes) {
            TypeSpec.Builder compBuilder = TypeSpec.classBuilder(ClassNameUtils.genClassNameMirror(cl.classType))
                    .addAnnotation(codeGenAnnot)
                    .addSuperinterface(cl.classType)
                    .addSuperinterface(IComponent.class)
                    .addModifiers(Modifier.PUBLIC);

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
                initMethodBuilder.addStatement("if ($L!=null) $L.init(m)", m.methodName, m.methodName);
            initMethodBuilder
                    .endControlFlow();

            compBuilder.addMethod(initMethodBuilder.build());

            for (MethodDetail m : cl.methods) {
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
