package com.github.klee0kai.stone.exceptions;

public class StoneExceptionStrings {

    public static String cannotCreateComponent = "Cannot create component: %s";
    public static String cannotCreateModule = "Cannot create module: %s";
    public static String classNonFoundMes = "Class not found: %s\nTry import class directly";
    public static String primitiveTypeNonSupported = "Primitive type non supported: %s";
    public static String methodPurposeNonDetected = "What is purpose for Method '%s'. Declared in %s";
    public static String errorProvideTypeRequiredIn = "Error provide type %s. Required in %s.%s";
    public static String errorProvideType = "Error provide type %s";
    public static String errorImplementMethod = "Error to implement method: '%s'";

    public static String componentsClass = "Component's class %s ";
    public static String moduleClass = "Module's class %s ";
    public static String dependencyClass = "Dependency's class %s ";
    public static String wrappersProviderClass = "WrappersProvider's class %s ";
    public static String method = "Method '%s' ";
    public static String hasIncorrectSignature = "has incorrect signature";
    public static String shouldNoHaveAnnotation = "should not have @%s annotation";
    public static String shouldNoHaveQualifier = "should not have %s qualifier";
    public static String shouldHaveOnlySingleModuleMethod = "should have only one module or dependency method. %s has duplicate";
    public static String shouldNoHaveFields = "should not have fields";
    public static String shouldImplementInterface = "should implement %s";
    public static String shouldHaveConstructorWithoutArgs = "should have public constructor without parameters";
    public static String shouldHaveInjectableClassAsParameter = "should have an injection class as a parameter";
    public static String shouldProvideNonPrimitiveObjects = "should provide non primitive objects";
    public static String shouldNoHavePrimitiveArguments = "should no have primitive arguments";
    public static String recursiveProviding = "Recursive providing detected";
    public static String constructorNonFound = "No found public constructor for class: %s with args: %s";

    public static String componentInitMethodSignatureIncorrect = "Component's init method have incorrect signature: '%s'. "
            + "Method Should have only one %s annotation. "
            + "Should have arguments of module or dependencies. "
            + "Should be void.";

    public static String componentExtOfMethodSignatureIncorrect = "Component's extOf method have incorrect signature: '%s'. "
            + "Method Should have only one %s annotation. "
            + "Should have only one argument of parent component. "
            + "Should be void.";

    public static String componentBindInstanceMethodSignatureIncorrect = "Component's bindInstance method have incorrect signature: '%s'. "
            + "Method Should have only %s annotation and GC scope annotations. "
            + "Should have only one argument of providing object. "
            + "Providing object should not be primitive or boxed primitive. "
            + "Can return only providing object or should be void. ";

    public static String componentGCMethodSignatureIncorrect = "Component's gc method have incorrect signature: '%s'. "
            + "Should be void.";

    public static String componentSwitchCacheMethodSignatureIncorrect = "Component's SwitchCache method have incorrect signature: '%s'. "
            + "Method Should have only %s annotation and GC scope annotations. "
            + "Should no have arguments. "
            + "Should be void.";

    public static String componentProtectInjectedMethodSignatureIncorrect = "Component's ProtectInjected method have incorrect signature: '%s'. "
            + "Method Should have only %s annotation. "
            + "Should have only one argument (non primitive and non boxed primitive). "
            + "Should be void.";

    public static String componentMethodNameBusy = "Component's method name busy by lib's private interfaces";

    public static String collectCauseMessages(String mes, Throwable cause) {
        StringBuilder sb = new StringBuilder(mes);
        while (cause != null) {
            if (sb.length() > 0) sb.append("\nCaused by: ");
            sb.append(cause.getMessage());
            cause = cause.getCause();
        }
        return sb.toString();
    }

}
