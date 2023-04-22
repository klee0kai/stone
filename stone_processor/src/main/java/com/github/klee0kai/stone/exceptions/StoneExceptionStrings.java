package com.github.klee0kai.stone.exceptions;

public class StoneExceptionStrings {

    public static String cannotCreateComponent = "Cannot create component: %s";
    public static String cannotCreateModule = "Cannot create module: %s";
    public static String classNonFoundMes = "Class not found: %s\nTry import class directly";
    public static String primitiveTypeNonSupported = "Primitive type non supported: %s";
    public static String methodPurposeNonDetected = "What is purpose for Method '%s'. Declared in %s";
    public static String errorProvideType = "Error provide type %s. Required in %s.%s";

    public static String componentsClass = "Component's class %s ";
    public static String moduleClass = "Module's class %s ";
    public static String dependencyClass = "Dependency's class %s ";
    public static String wrappersProviderClass = "WrappersProvider's class %s ";
    public static String method = "Method '%s' ";
    public static String hasIncorrectSignature = "has incorrect signature";
    public static String shouldNoHaveAnnotation = "should not have @%s annotation";
    public static String shouldNoHaveQualifier = "should not have %s qualifier";
    public static String shouldNoHaveFields = "should not have fields";
    public static String shouldImplementInterface = "should implement %s";
    public static String shouldHaveConstructorWithoutArgs = "should have public constructor without parameters";
    public static String shouldHaveInjectableClassAsParameter = "should have an injection class as a parameter";
    public static String shouldProvideNonPrimitiveObjects = "should provide non primitive objects";
    public static String shouldNoHavePrimitiveArguments = "should no have primitive arguments";
    public static String recursiveProviding = "Recursive providing dependencies: %s";


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
