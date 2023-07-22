package com.github.klee0kai.stone;

import com.github.klee0kai.stone._hidden_.types.NullGet;
import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.checks.ComponentChecks;
import com.github.klee0kai.stone.checks.ModuleChecks;
import com.github.klee0kai.stone.codegen.*;
import com.github.klee0kai.stone.exceptions.StoneException;
import com.github.klee0kai.stone.helpers.AllClassesHelper;
import com.github.klee0kai.stone.helpers.wrap.WrapHelper;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.ComponentClassDetails;
import com.github.klee0kai.stone.model.annotations.ComponentAnn;
import com.github.klee0kai.stone.utils.StoneNamingUtils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;
import static com.github.klee0kai.stone.utils.StoneNamingUtils.genCacheControlInterfaceModuleNameMirror;
import static com.github.klee0kai.stone.utils.StoneNamingUtils.genModuleNameMirror;
import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * Stone's Annotation processor
 * Entry Point of the lib.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"*"})
public class AnnotationProcessor extends AbstractProcessor {

    public static final String PROJECT_URL = "https://github.com/klee0kai/stone";
    public static final AllClassesHelper allClassesHelper = new AllClassesHelper();
    private static final String DEBUG_PKG = null;

    public static ProcessingEnvironment env;
    public static Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        AnnotationProcessor.env = env;
        messager = env.getMessager();
        allClassesHelper.reInit(env.getElementUtils());
        WrapHelper.reInit();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        for (Element componentEl : roundEnv.getElementsAnnotatedWith(Component.class)) {
            try {
                ClassDetail component = new ClassDetail((TypeElement) componentEl);
                allClassesHelper.deepExtractAdditionalClasses(component);
            } catch (StoneException cause) {
                processingEnv.getMessager().printMessage(ERROR,
                        createErrorMes()
                                .cannotCreateComponent(componentEl.getSimpleName().toString())
                                .collectCauseMessages(cause)
                                .build(),
                        NullGet.first(cause.findErrorElement(), componentEl)
                );
            }
        }
        for (Element moduleEl : roundEnv.getElementsAnnotatedWith(Module.class)) {
            try {
                ClassDetail module = new ClassDetail((TypeElement) moduleEl);
                allClassesHelper.deepExtractAdditionalClasses(module);
            } catch (StoneException cause) {
                processingEnv.getMessager().printMessage(ERROR,
                        createErrorMes()
                                .cannotCreateModule(moduleEl.toString())
                                .collectCauseMessages(cause)
                                .build(),
                        NullGet.first(cause.findErrorElement(), moduleEl)
                );
            }
        }


        try {
            WrappersSupportBuilder wrappersBuilder = null;
            for (Element componentEl : roundEnv.getElementsAnnotatedWith(Component.class)) {
                ClassDetail component = new ClassDetail((TypeElement) componentEl);
                if (wrappersBuilder == null) {
                    ClassName wrapperHelper = StoneNamingUtils.typeWrappersClass(component.className);
                    wrappersBuilder = new WrappersSupportBuilder(wrapperHelper);
                }

                for (ClassName wrappedProvider : component.ann(ComponentAnn.class).wrapperProviders) {
                    ClassDetail wrappedProviderCl = allClassesHelper.findForType(wrappedProvider);
                    if (wrappedProviderCl != null) wrappersBuilder.addProvideWrapperField(wrappedProviderCl);
                }
            }

            if (wrappersBuilder != null && !wrappersBuilder.isEmpty()) {
                wrappersBuilder.buildAndWrite();
            }
        } catch (StoneException cause) {
            processingEnv.getMessager().printMessage(ERROR,
                    createErrorMes()
                            .cannotCreateWrappersHelper()
                            .collectCauseMessages(cause)
                            .build(),
                    cause.findErrorElement()
            );
        }


        for (Element moduleEl : roundEnv.getElementsAnnotatedWith(Module.class)) {
            try {
                ClassDetail module = new ClassDetail((TypeElement) moduleEl);
                if (DEBUG_PKG != null && !((ClassName) module.className).packageName().contains(DEBUG_PKG)) {
                    System.out.println("module skipped for debug " + module.className);
                    continue;
                }
                ModuleChecks.checkModuleClass(module);

                ModuleFactoryBuilder factoryBuilder = ModuleFactoryBuilder.fromModule(module);
                factoryBuilder.buildAndWrite();

                ModuleCacheControlInterfaceBuilder.from(module, genCacheControlInterfaceModuleNameMirror(module.className))
                        .buildAndWrite();

                ModuleBuilder.from(module, genModuleNameMirror(module.className), factoryBuilder.className)
                        .buildAndWrite();
            } catch (StoneException cause) {
                processingEnv.getMessager().printMessage(ERROR,
                        createErrorMes()
                                .cannotCreateModule(moduleEl.toString())
                                .collectCauseMessages(cause)
                                .build(),
                        NullGet.first(cause.findErrorElement(), moduleEl)
                );
            }
        }

        //create components
        for (Element componentEl : roundEnv.getElementsAnnotatedWith(Component.class)) {
            try {
                ComponentClassDetails component = new ComponentClassDetails((TypeElement) componentEl);
                if (DEBUG_PKG != null && !((ClassName) component.className).packageName().contains(DEBUG_PKG)) {
                    System.out.println("Component skipped for debug " + component.className);
                    continue;
                }

                ComponentChecks.checkComponentClass(component);

                ComponentBuilder.from(component)
                        .buildAndWrite();

                ModuleCacheControlInterfaceBuilder.from(component.hiddenModule, component.hiddenModuleCacheControlInterface)
                        .buildAndWrite();

                ModuleBuilder.from(component.hiddenModule, (ClassName) component.hiddenModule.className, null)
                        .buildAndWrite();

            } catch (StoneException cause) {
                processingEnv.getMessager().printMessage(ERROR,
                        createErrorMes()
                                .cannotCreateComponent(componentEl.getSimpleName().toString())
                                .collectCauseMessages(cause)
                                .build(),
                        NullGet.first(cause.findErrorElement(), componentEl)
                );
            }

        }
        return false;
    }
}

