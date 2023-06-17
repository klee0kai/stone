package com.github.klee0kai.stone;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.checks.ComponentChecks;
import com.github.klee0kai.stone.codegen.ComponentBuilder;
import com.github.klee0kai.stone.codegen.ModuleBuilder;
import com.github.klee0kai.stone.codegen.ModuleCacheControlInterfaceBuilder;
import com.github.klee0kai.stone.codegen.ModuleFactoryBuilder;
import com.github.klee0kai.stone.exceptions.CreateStoneComponentException;
import com.github.klee0kai.stone.exceptions.CreateStoneModuleException;
import com.github.klee0kai.stone.helpers.AllClassesHelper;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.ComponentClassDetails;
import com.github.klee0kai.stone.model.annotations.ComponentAnn;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;

/**
 * Stone's Annotation processor
 * Entry Point of the lib.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"*"})
public class AnnotationProcessor extends AbstractProcessor {

    public static final String PROJECT_URL = "https://github.com/klee0kai/stone";
    public static final AllClassesHelper allClassesHelper = new AllClassesHelper();


    public static ProcessingEnvironment env;
    public static Messager messager;


    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        AnnotationProcessor.env = env;
        messager = env.getMessager();
        allClassesHelper.init(env.getElementUtils());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        List<ClassName> allQualifiers = new LinkedList<>();
        for (Element componentEl : roundEnv.getElementsAnnotatedWith(Component.class)) {
            try {
                ClassDetail component = new ClassDetail((TypeElement) componentEl);
                allClassesHelper.deepExtractGcAnnotations(component);

                for (ClassDetail componentParentCl : component.getAllParents(false)) {
                    ComponentAnn parentCompAnn = componentParentCl.ann(ComponentAnn.class);
                    if (parentCompAnn != null) allQualifiers.addAll(parentCompAnn.qualifiers);
                }
            } catch (Throwable cause) {
                throw new CreateStoneComponentException(
                        createErrorMes()
                                .cannotCreateComponent(componentEl.getSimpleName().toString())
                                .collectCauseMessages(cause)
                                .build(),
                        cause
                );
            }
        }

        for (Element moduleEl : roundEnv.getElementsAnnotatedWith(Module.class)) {
            try {
                ClassDetail module = new ClassDetail((TypeElement) moduleEl);

                ModuleFactoryBuilder factoryBuilder = ModuleFactoryBuilder.fromModule(module, allQualifiers);
                factoryBuilder.buildAndWrite();

                ModuleCacheControlInterfaceBuilder moduleCacheControlInterfaceBuilder = ModuleCacheControlInterfaceBuilder.from(factoryBuilder, allQualifiers);
                moduleCacheControlInterfaceBuilder.buildAndWrite();


                ModuleBuilder moduleBuilder = ModuleBuilder.from(factoryBuilder, allQualifiers);
                moduleBuilder.buildAndWrite();
            } catch (Throwable e) {
                throw new CreateStoneModuleException(
                        createErrorMes()
                                .cannotCreateModule(moduleEl.toString())
                                .collectCauseMessages(e)
                                .build(),
                        e);
            }
        }

        //create components
        for (Element componentEl : roundEnv.getElementsAnnotatedWith(Component.class)) {
            try {
                ComponentClassDetails component = new ComponentClassDetails((TypeElement) componentEl);
                ComponentChecks.checkComponentClass(component);

                ComponentBuilder.from(component)
                        .buildAndWrite();

            } catch (Throwable cause) {
                throw new CreateStoneComponentException(
                        createErrorMes()
                                .cannotCreateComponent(componentEl.getSimpleName().toString())
                                .collectCauseMessages(cause)
                                .build(),
                        cause
                );
            }

        }
        return false;
    }
}

