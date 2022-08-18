package com.klee0kai.stone.codegen;

import com.klee0kai.stone.AnnotationProcessor;
import com.klee0kai.stone.container.ItemsWeakContainer;
import com.klee0kai.stone.interfaces.IModule;
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
        String orClassFieldName = "originalClass";
        String iModuleInit = "init";

        for (ClassDetail cl : classes) {
            TypeSpec.Builder moduleClBuilder = TypeSpec.classBuilder(ClassNameUtils.genClassNameMirror(cl.classType))
                    .superclass(cl.classType)
                    .addSuperinterface(ClassName.get(IModule.class))
                    .addModifiers(Modifier.PUBLIC)
                    .addField(
                            FieldSpec.builder(cl.classType, orClassFieldName, Modifier.PRIVATE)
                                    .build())
                    .addAnnotation(codeGenAnnot);


            moduleClBuilder.addMethod(MethodSpec.methodBuilder(iModuleInit)
                    .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                    .addAnnotation(Override.class)
                    .addParameter(Object.class, "or")
                    .addStatement("if (or instanceof $T) this.$L = ($T) or", cl.classType, orClassFieldName, cl.classType)
                    .returns(void.class)
                    .build());

            for (MethodDetail m : cl.methods) {
                if (m.itemAnn == null)
                    continue;

                moduleClBuilder.addMethod(MethodSpec.methodBuilder(m.methodName)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                        .returns(m.returnType)
                        .addStatement("$T cache = $T.get($L)", m.returnType, itemsWeakContainerClass, incrementRefId)
                        .addStatement("if (cache != null) return cache")
                        .addStatement("if ($L == null) return null", orClassFieldName)
                        .addStatement("return $T.putRef($L,$L,$L.$L())", itemsWeakContainerClass,
                                incrementRefId++, m.itemAnn.cacheType, orClassFieldName, m.methodName)
                        .build());

            }


            CodeFileUtil.writeToJavaFile(cl.classType.packageName(), moduleClBuilder.build());
        }
    }

}
