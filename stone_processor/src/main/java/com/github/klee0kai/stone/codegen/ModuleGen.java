package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.container.ItemsWeakContainer;
import com.github.klee0kai.stone.interfaces.IModule;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.SingletonAnnotation;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
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
//        AnnotationSpec codeGenAnnot = AnnotationSpec.builder(Generated.class)
//                .addMember("value", "$S", ModuleGen.class.getCanonicalName())
//                .addMember("comments", "$S", AnnotationProcessor.PROJECT_URL)
//                .addMember("date", "$S", new SimpleDateFormat().format(Calendar.getInstance().getTime()))
//                .build();
        ClassName itemsWeakContainerClass = ClassName.get(ItemsWeakContainer.class);
        String orClassFactoryFieldName = "factory";
        String prefixFieldName = "prefix";
        String superDIModuleStoneFieldName = "superModuleStone";
        String initMethodName = "init";
        String extOfMethodName = "extOf";
        String getFactoryMethodName = "getFactory";

        for (ClassDetail cl : classes) {
            boolean hasSupperStoneModule = cl.superClass != null && cl.superClass.moduleAnn != null;


            TypeSpec.Builder moduleClBuilder = TypeSpec.classBuilder(ClassNameUtils.genClassNameMirror(cl.classType))
                    .addSuperinterface(ClassName.get(IModule.class))
                    .addModifiers(Modifier.PUBLIC)
                    .addField(FieldSpec.builder(cl.classType, orClassFactoryFieldName, Modifier.PRIVATE)
                            .build())
                    .addField(FieldSpec.builder(String.class, prefixFieldName, Modifier.PRIVATE)
                            .initializer("$S", cl.superClassesDeep(false))
                            .build());
//                    .addAnnotation(codeGenAnnot);
            if (cl.isInterfaceClass)
                moduleClBuilder.addSuperinterface(cl.classType);
            else moduleClBuilder.superclass(cl.classType);

            if (hasSupperStoneModule)
                moduleClBuilder.addField(
                        FieldSpec.builder(ClassNameUtils.genClassNameMirror(cl.superClass.classType), superDIModuleStoneFieldName, Modifier.PRIVATE)
                                .build());


            MethodSpec.Builder initMethodBuilder = MethodSpec.methodBuilder(initMethodName)
                    .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                    .addAnnotation(Override.class)
                    .addParameter(Object.class, "or")
                    .beginControlFlow("if (or instanceof $T) ", cl.classType)
                    .addStatement("this.$L = ($T) or", orClassFactoryFieldName, cl.classType)
                    .endControlFlow()
                    .returns(void.class);

            MethodSpec.Builder extOfMethodBuilder = MethodSpec.methodBuilder(extOfMethodName)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(IModule.class, "superStoneModule")
                    .returns(void.class);

            if (hasSupperStoneModule)
                extOfMethodBuilder.beginControlFlow("if (superStoneModule instanceof $T)", ClassNameUtils.genClassNameMirror(cl.superClass.classType))
                        .addStatement("this.$L = ($T) superStoneModule", superDIModuleStoneFieldName, ClassNameUtils.genClassNameMirror(cl.superClass.classType))
                        .endControlFlow()
                        .beginControlFlow("else if (superStoneModule == null)")
                        .addStatement("this.$L = null ", superDIModuleStoneFieldName)
                        .endControlFlow();


            moduleClBuilder.addMethod(extOfMethodBuilder.build());

            for (MethodDetail m : cl.getAllMethods(false)) {
                if (Objects.equals(m.methodName, "<init>"))
                    continue;
                incrementRefId++;
                if (m.changeableAnn != null) {
                    String scope = (m.changeableAnn.scope != null ? m.changeableAnn.scope : "") + ".";
                    moduleClBuilder.addMethod(MethodSpec.methodBuilder(m.methodName)
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED)
                            .returns(m.returnType)
                            .addStatement("return  $T.get($S + $L+$L)", itemsWeakContainerClass, scope, prefixFieldName, incrementRefId)
                            .build());
                    initMethodBuilder.addStatement("if (or instanceof $T) $T.putRef($S + $L + $L,$L,or)", m.returnType,
                            itemsWeakContainerClass, scope, prefixFieldName, incrementRefId, m.changeableAnn.cacheType);
                    continue;
                }


                if (m.singletonAnn == null)
                    m.singletonAnn = new SingletonAnnotation();
                String scope = (m.singletonAnn.scope != null ? m.singletonAnn.scope : "") + ".";


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

                if (hasSupperStoneModule && !cl.haveMethod(m, false)) {
                    // this module does override this component. We can use old
                    itemMethodBuilder.addStatement("if ($L != null) return  $L.$L($L)", superDIModuleStoneFieldName, superDIModuleStoneFieldName, m.methodName, argsComma);
                }

                itemMethodBuilder
                        .addStatement("$T cache = $T.get($S + $L+$L+ \".\" $L)", m.returnType, itemsWeakContainerClass, scope, prefixFieldName, incrementRefId, argsSum)
                        .addStatement("if (cache != null) return cache")
                        .addStatement("if ($L == null) return null", orClassFactoryFieldName)
                        .addStatement("return $T.putRef($S + $L + $L + \".\" $L,$L,$L.$L($L))", itemsWeakContainerClass,
                                scope, prefixFieldName, incrementRefId, argsSum, m.singletonAnn.cacheType, orClassFactoryFieldName, m.methodName, argsComma);


                moduleClBuilder.addMethod(itemMethodBuilder
                        .build());
            }

            moduleClBuilder.addMethod(initMethodBuilder.build());

            moduleClBuilder.addMethod(MethodSpec.methodBuilder(getFactoryMethodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(Object.class)
                    .addStatement("return this.$L", orClassFactoryFieldName)
                    .build());


            CodeFileUtil.writeToJavaFile(cl.classType.packageName(), moduleClBuilder.build());
        }
    }

}
