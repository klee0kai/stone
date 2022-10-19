package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.container.ItemsWeakContainer;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.stone.interfaces.IModule;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.org.apache.xml.internal.utils.ObjectVector;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
//        AnnotationSpec codeGenAnnot = AnnotationSpec.builder(Generated.class)
//                .addMember("value", "$S", ModuleGen.class.getCanonicalName())
//                .addMember("comments", "$S", AnnotationProcessor.PROJECT_URL)
//                .addMember("date", "$S", new SimpleDateFormat().format(Calendar.getInstance().getTime()))
//                .build();
        String superStoneComponentFieldName = "superStoneComponent";
        String initMethodName = "init";
        String extOfMethodName = "extOf";
        String removeScopeMethodName = "removeScope";

        for (ClassDetail cl : classes) {
            TypeSpec.Builder compBuilder = TypeSpec.classBuilder(ClassNameUtils.genClassNameMirror(cl.classType))
//                    .addAnnotation(codeGenAnnot)
                    .addSuperinterface(IComponent.class)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addField(FieldSpec.builder(IComponent.class, superStoneComponentFieldName, Modifier.PROTECTED)
                            .initializer("null")
                            .build());

            if (cl.isInterfaceClass)
                compBuilder.addSuperinterface(cl.classType);
            else compBuilder.superclass(cl.classType);


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

            MethodSpec.Builder initMethodBuilder = MethodSpec.methodBuilder(initMethodName)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(Object[].class, "modules")
                    .varargs(true)
                    .addStatement("if ($L != null) $L.$L(modules)", superStoneComponentFieldName, superStoneComponentFieldName, initMethodName)
                    .beginControlFlow("if (modules != null) for (Object m :modules)");
            for (MethodDetail m : cl.methods)
                initMethodBuilder.addStatement("if ($L != null ) $L.init(m)", m.methodName, m.methodName);
            initMethodBuilder
                    .endControlFlow();

            compBuilder.addMethod(initMethodBuilder.build());

            MethodSpec.Builder metBuilderExtOfOtherDI = MethodSpec.methodBuilder(extOfMethodName)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(IComponent.class, "c")
                    .addStatement("this.$L = c", superStoneComponentFieldName);
            for (MethodDetail m : cl.methods)
                metBuilderExtOfOtherDI.addStatement("c.init($L.getFactory())", m.methodName);
            for (MethodDetail m : cl.methods) {
                ClassDetail interfaceOrigin = cl.findInterfaceOverride(m);
                if (interfaceOrigin != null)
                    metBuilderExtOfOtherDI.addStatement("$L.extOf( ($T )  ( ( $T ) c).$L())", m.methodName, IModule.class, interfaceOrigin.classType, m.methodName);
            }
            compBuilder.addMethod(metBuilderExtOfOtherDI.build());


            compBuilder.addMethod(MethodSpec.methodBuilder(removeScopeMethodName)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(String.class, "scope")
                    .addStatement("$T.removeScope(scope)", ItemsWeakContainer.class)
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
