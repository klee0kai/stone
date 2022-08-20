package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.container.ItemsWeakContainer;
import com.github.klee0kai.stone.interfaces.IModule;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.SingletonAnnotation;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.annotation.Generated;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;

public class ModuleGen {

    private static int incrementRefId = 1;
    private ProcessingEnvironment env;
    private Messager messager;

    private final List<ClassDetail> classes = new LinkedList<>();

    public void init(ProcessingEnvironment env) {
        this.env = env;
        messager = env.getMessager();
    }

    public void addModule(ClassDetail... modules) {
        this.classes.addAll(Arrays.asList(modules));
    }

    public void genCode() {
        AnnotationSpec codeGenAnnot = AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", ModuleGen.class.getCanonicalName())
                .addMember("comments", "$S", AnnotationProcessor.PROJECT_URL)
                .addMember("date", "$S", new SimpleDateFormat().format(Calendar.getInstance().getTime()))
                .build();
        ClassName itemsWeakContainerClass = ClassName.get(ItemsWeakContainer.class);
        String orClassFactoryFieldName = "factory";
        String prefixFieldName = "prefix";
        String iModuleInit = "init";
        String iModuleSetPrefix = "setPrefix";

        for (ClassDetail cl : classes) {
            TypeSpec.Builder moduleClBuilder = TypeSpec.classBuilder(ClassNameUtils.genClassNameMirror(cl.classType))
                    .superclass(cl.classType)
                    .addSuperinterface(ClassName.get(IModule.class))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addField(FieldSpec.builder(cl.classType, orClassFactoryFieldName, Modifier.PRIVATE)
                            .build())
                    .addField(FieldSpec.builder(String.class, prefixFieldName, Modifier.PRIVATE)
                            .initializer("$S", "1")
                            .build())
                    .addAnnotation(codeGenAnnot);


            moduleClBuilder.addMethod(MethodSpec.methodBuilder(iModuleInit)
                    .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                    .addAnnotation(Override.class)
                    .addParameter(Object.class, "or")
                    .beginControlFlow("if (or instanceof $T) ", cl.classType)
                    .addStatement("this.$L = ($T) or", orClassFactoryFieldName, cl.classType)
                    .beginControlFlow("if (or instanceof $T)", IModule.class)
                    .addStatement("(($T)or).$L($T.valueOf($T.parseInt($L)+1))", IModule.class, iModuleSetPrefix, String.class, Integer.class, prefixFieldName)
                    .endControlFlow()
                    .endControlFlow()
                    .returns(void.class)
                    .build());

            moduleClBuilder.addMethod(MethodSpec.methodBuilder(iModuleSetPrefix)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(String.class, "prefix")
                    .addStatement("this.$L = prefix", prefixFieldName)
                    .build());

            for (MethodDetail m : cl.methods) {
                if (Objects.equals(m.methodName, "<init>"))
                    continue;
                if (m.singletonAnn == null)
                    m.singletonAnn = new SingletonAnnotation();

                int argIndex = 0;
                StringBuilder argsSum = new StringBuilder();
                StringBuilder argsComma = new StringBuilder();
                MethodSpec.Builder itemMethodBuilder = MethodSpec.methodBuilder(m.methodName)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                        .returns(m.returnType);
                for (TypeName t : m.argTypes) {
                    if (argsComma.length() > 0)
                        argsComma.append(", ");
                    argsSum.append(" + ").append("arg").append(argIndex).append(".toString()");
                    argsComma.append("arg").append(argIndex);
                    itemMethodBuilder.addParameter(t, "arg" + (argIndex++));
                }

                itemMethodBuilder
                        .addStatement("$T cache = $T.get($L+$L $L)", m.returnType, itemsWeakContainerClass, prefixFieldName, incrementRefId, argsSum)
                        .addStatement("if (cache != null) return cache")
                        .addStatement("if ($L == null) return null", orClassFactoryFieldName)
                        .addStatement("return $T.putRef($L + $L $L,$L,$L.$L($L))", itemsWeakContainerClass,
                                prefixFieldName, incrementRefId++, argsSum, m.singletonAnn.cacheType, orClassFactoryFieldName, m.methodName, argsComma);

                moduleClBuilder.addMethod(itemMethodBuilder
                        .build());

            }


            CodeFileUtil.writeToJavaFile(cl.classType.packageName(), moduleClBuilder.build());
        }
    }

}
