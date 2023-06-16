package com.github.klee0kai.stone.exceptions;

import java.util.List;
import java.util.Locale;

/**
 * Build error message by masks
 * Pro-StringBuilder implementation for exceptions
 */
public class ExceptionStringBuilder {

    private final StringBuilder sb = new StringBuilder();

    public static ExceptionStringBuilder createErrorMes() {
        return new ExceptionStringBuilder();
    }

    public ExceptionStringBuilder cannotCreateComponent(String componentName) {
        if (sb.length() > 0) sb.append(" ");
        sb.append("Cannot create component: ");
        sb.append(componentName);
        return this;
    }

    public ExceptionStringBuilder cannotCreateModule(String moduleName) {
        if (sb.length() > 0) sb.append(" ");
        sb.append("Cannot create component: ");
        sb.append(moduleName);
        return this;
    }

    public ExceptionStringBuilder componentsClass(String className) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Component's class %s",
                className));
        return this;
    }


    public ExceptionStringBuilder moduleClass(String className) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Module's class %s",
                className));
        return this;
    }

    public ExceptionStringBuilder dependencyClass(String className) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Dependency's class %s",
                className));
        return this;
    }


    public ExceptionStringBuilder wrappersCreatorClass(String className) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "WrappersCreator's class %s",
                className));
        return this;
    }

    public ExceptionStringBuilder method(String methodName) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "method '%s'",
                methodName));
        return this;
    }


    public ExceptionStringBuilder classNonFound(String className) {
        if (sb.length() > 0) sb.append(" ");
        sb.append("Class not found: ");
        sb.append(className);
        sb.append("Try import class directly.");
        return this;
    }

    public ExceptionStringBuilder primitiveTypesNonSupported(String typeName) {
        if (sb.length() > 0) sb.append(" ");
        sb.append("Primitive type non supported: ");
        sb.append(typeName);
        return this;
    }

    public ExceptionStringBuilder methodPurposeNonDetected(String methodName, String className) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "What is purpose for Method '%s'. Declared in %s",
                methodName, className));
        return this;
    }

    public ExceptionStringBuilder errorProvideTypeRequiredIn(String providingTypeName, String className, String method) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Error provide type %s. Required in %s.%s",
                providingTypeName, className, method));
        return this;
    }

    public ExceptionStringBuilder errorProvideType(String providingTypeName) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Error provide type %s",
                providingTypeName));
        return this;
    }

    public ExceptionStringBuilder errorImplementMethod(String method) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Error to implement method: '%s'",
                method));
        return this;
    }

    public ExceptionStringBuilder hasIncorrectSignature() {
        if (sb.length() > 0) sb.append(" ");
        sb.append("has incorrect signature");
        return this;
    }

    public ExceptionStringBuilder shouldNoHaveAnnotation(String annotation) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "should not have @%s annotation",
                annotation));
        return this;
    }

    public ExceptionStringBuilder shouldHaveOnlyAnnotations(String... annotations) {
        if (sb.length() > 0) sb.append(" ");
        String annList = String.join(", @", annotations);
        sb.append(String.format(Locale.ROOT,
                "should have only @%s annotation",
                annList));
        return this;
    }


    public ExceptionStringBuilder shouldNoHaveQualifier(String qualifier) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "should not have %s qualifier",
                qualifier));
        return this;
    }

    public ExceptionStringBuilder shouldHaveOnlySingleModuleMethod(String moduleType) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "should have only one module or dependency method. %s has duplicate",
                moduleType));
        return this;
    }

    public ExceptionStringBuilder shouldNoHaveFields() {
        if (sb.length() > 0) sb.append(" ");
        sb.append("should not have fields");
        return this;
    }


    public ExceptionStringBuilder shouldImplementInterface(String interfaceType) {
        if (sb.length() > 0) sb.append(" ");
        sb.append("should implement");
        sb.append(interfaceType);
        return this;
    }

    public ExceptionStringBuilder shouldHaveConstructorWithoutArgs() {
        if (sb.length() > 0) sb.append(" ");
        sb.append("should have public constructor without arguments");
        return this;
    }

    public ExceptionStringBuilder shouldHaveInjectableClassAsParameter() {
        if (sb.length() > 0) sb.append(" ");
        sb.append("should have an injection class as a parameter");
        return this;
    }

    public ExceptionStringBuilder shouldProvideNonPrimitiveObjects() {
        if (sb.length() > 0) sb.append(" ");
        sb.append("should provide non primitive objects");
        return this;
    }


    public ExceptionStringBuilder shouldNoHavePrimitiveArguments() {
        if (sb.length() > 0) sb.append(" ");
        sb.append("should no have primitive arguments");
        return this;
    }


    public ExceptionStringBuilder recursiveProviding() {
        if (sb.length() > 0) sb.append(" ");
        sb.append("Recursive providing detected");
        return this;
    }

    public ExceptionStringBuilder constructorNonFound(String className, List<String> argTypes) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "No found public constructor for class: %s with args: %s",
                className, String.join(", ", argTypes)));
        return this;
    }


    public ExceptionStringBuilder componentInitMethodSignatureIncorrect(String className, String annotation) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Component's init method have incorrect signature: '%s'. "
                        + "Method Should have only one %s annotation. "
                        + "Should have arguments of module or dependencies. "
                        + "Should be void.",
                className, annotation));
        return this;
    }


    public ExceptionStringBuilder componentExtOfMethodSignatureIncorrect(String className, String annotation) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Component's bindInstance method have incorrect signature: '%s'. "
                        + "Method Should have only %s annotation and GC scope annotations. "
                        + "Should have only one argument of providing object. "
                        + "Providing object should not be primitive or boxed primitive. "
                        + "Can return only providing object or should be void. ",
                className, annotation));
        return this;
    }

    public ExceptionStringBuilder componentBindInstanceMethodSignatureIncorrect(String className, String annotation) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Component's bindInstance method have incorrect signature: '%s'. "
                        + "Method Should have only %s annotation and GC scope annotations. "
                        + "Should have only one argument of providing object. "
                        + "Providing object should not be primitive or boxed primitive. "
                        + "Can return only providing object or should be void. ",
                className, annotation));
        return this;
    }

    public ExceptionStringBuilder componentGCMethodSignatureIncorrect(String className) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Component's gc method have incorrect signature: '%s'. "
                        + "Should be void.",
                className));
        return this;
    }

    public ExceptionStringBuilder componentSwitchCacheMethodSignatureIncorrect(String className, String annotation) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Component's SwitchCache method have incorrect signature: '%s'. "
                        + "Method Should have only %s annotation and GC scope annotations. "
                        + "Should no have arguments. "
                        + "Should be void.",
                className, annotation));
        return this;
    }

    public ExceptionStringBuilder componentProtectInjectedMethodSignatureIncorrect(String className, String annotation) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Component's ProtectInjected method have incorrect signature: '%s'. "
                        + "Method Should have only %s annotation. "
                        + "Should have only one argument (non primitive and non boxed primitive). "
                        + "Should be void.",
                className, annotation));
        return this;
    }


    public ExceptionStringBuilder componentMethodNameBusy(String methodName) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(String.format(Locale.ROOT,
                "Component's method name '%s' busy by lib's private interfaces",
                methodName));
        return this;
    }

    public ExceptionStringBuilder add(String mes) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(mes);
        return this;
    }


    public ExceptionStringBuilder collectCauseMessages(Throwable cause) {
        while (cause != null) {
            if (sb.length() > 0) sb.append("\nCaused by: ");
            sb.append(cause.getMessage());
            cause = cause.getCause();
        }
        return this;
    }

    public String build() {
        return sb.toString();
    }

}
