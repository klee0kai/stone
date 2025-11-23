package com.github.klee0kai.thekey.stone.ksp.exceptions

import com.squareup.kotlinpoet.TypeName
import java.lang.String
import java.util.*
import kotlin.Deprecated
import kotlin.Throwable
import kotlin.text.format

/**
 * Build error message by masks
 * Pro-StringBuilder implementation for exceptions
 */
@Deprecated(message = "use kotlin string builder instead")
class ExceptionStringBuilder {
    private val sb = StringBuilder()

    fun cannotCreateComponent(componentName: String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("Cannot create component: ")
        sb.append(componentName)
        return this
    }

    fun cannotCreateWrappersHelper(): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("Cannot create wrappers helper ")
        return this
    }

    fun cannotCreateModule(moduleName: String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("Cannot create component: ")
        sb.append(moduleName)
        return this
    }

    fun componentsClass(className: String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            String.format(
                Locale.ROOT,
                "Component's class %s",
                className
            )
        )
        return this
    }


    fun moduleClass(className: String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            String.format(
                Locale.ROOT,
                "Module's class %s",
                className
            )
        )
        return this
    }

    fun dependencyClass(className: String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            String.format(
                Locale.ROOT,
                "Dependency's class %s",
                className
            )
        )
        return this
    }


    fun wrappersCreatorClass(className: String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            String.format(
                Locale.ROOT,
                "WrappersCreator's class %s",
                className
            )
        )
        return this
    }

    fun wrapperShouldBeGenericType1(className: TypeName): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            String.format(
                Locale.ROOT,
                "WrapperClass %s should be genericType with one type argument. Like SmthWrapper<T>.class ",
                className.toString()
            )
        )
        return this
    }

    fun method(methodName: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "method '%s'",
                methodName
            )
        )
        return this
    }


    fun classNonFound(className: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("Class not found: ")
        sb.append(className)
        sb.append(". Try import class directly.")
        return this
    }

    fun primitiveTypesNonSupported(typeName: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("Primitive type non supported: ")
        sb.append(typeName)
        return this
    }

    fun methodPurposeNonDetected(methodName: kotlin.String?, className: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "What is purpose for Method '%s'. Declared in %s",
                methodName, className
            )
        )
        return this
    }

    fun errorProvideModuleFactoryRequiredIn(
        providingTypeName: kotlin.String?,
        className: kotlin.String?,
        method: kotlin.String?
    ): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "Error provide module %s. Required in %s.%s",
                providingTypeName, className, method
            )
        )
        return this
    }

    fun errorProvideTypeRequiredIn(
        providingTypeName: kotlin.String?,
        className: kotlin.String?,
        method: kotlin.String?
    ): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "Error provide type %s. Required in %s.%s",
                providingTypeName, className, method
            )
        )
        return this
    }

    fun errorProvideType(providingTypeName: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "Error provide type %s",
                providingTypeName
            )
        )
        return this
    }

    fun shouldNoProvideIdentifierType(providingTypeName: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "Should no provide identifier %s",
                providingTypeName
            )
        )
        return this
    }

    fun errorImplementMethod(method: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "Error to implement method: '%s'",
                method
            )
        )
        return this
    }

    fun hasIncorrectSignature(): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("has incorrect signature")
        return this
    }

    fun shouldNoHaveAnnotation(annotation: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "should not have @%s annotation",
                annotation
            )
        )
        return this
    }

    fun shouldHaveOnlyAnnotations(vararg annotations: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        val annList = String.join(", @", *annotations)
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "should have only @%s annotation",
                annList
            )
        )
        return this
    }

    fun shouldHaveAnnotations(vararg annotations: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        val annList = String.join(", @", *annotations)
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "should have @%s annotation",
                annList
            )
        )
        return this
    }


    fun shouldNoHaveIdentifiers(identifier: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "should not have %s identifiers",
                identifier
            )
        )
        return this
    }

    fun shouldHaveOnlySingleModuleMethod(moduleType: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "should have only one module or dependency method. %s has duplicate",
                moduleType
            )
        )
        return this
    }

    fun shouldNoHaveFields(): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("should not have fields")
        return this
    }


    fun shouldImplementInterface(interfaceType: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("should implement ")
        sb.append(interfaceType)
        return this
    }

    fun shouldHaveConstructorWithoutArgs(): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("should have public constructor without arguments")
        return this
    }

    fun shouldHaveInjectableClassAsParameter(): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("should have an injection class as a parameter")
        return this
    }

    fun shouldProvideNonPrimitiveObjects(): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("should provide non primitive objects")
        return this
    }


    fun shouldNoHavePrimitiveArguments(): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("should no have primitive arguments")
        return this
    }


    fun recursiveProviding(): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append("Recursive providing detected")
        return this
    }

    fun constructorNonFound(className: kotlin.String?, argTypes: MutableList<kotlin.String?>): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "No found public constructor for class: %s with args: %s",
                className, String.join(", ", argTypes)
            )
        )
        return this
    }


    fun typeTransformNonSupport(or: TypeName, dest: TypeName): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            String.format(
                Locale.ROOT,
                "Type Transform non support %s -> %s",
                or.toString(), dest.toString()
            )
        )
        return this
    }


    fun componentInitMethodSignatureIncorrect(
        className: kotlin.String?,
        annotation: kotlin.String?
    ): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                ("Component's init method have incorrect signature: '%s'. "
                        + "Method Should have only one %s annotation. "
                        + "Should have arguments of module or dependencies. "
                        + "Should be void."),
                className, annotation
            )
        )
        return this
    }


    fun componentExtOfMethodSignatureIncorrect(
        className: kotlin.String?,
        annotation: kotlin.String?
    ): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                ("Component's bindInstance method have incorrect signature: '%s'. "
                        + "Method Should have only %s annotation and GC scope annotations. "
                        + "Should have only one argument of providing object. "
                        + "Providing object should not be primitive or boxed primitive. "
                        + "Can return only providing object or should be void. "),
                className, annotation
            )
        )
        return this
    }

    fun componentBindInstanceMethodSignatureIncorrect(
        className: kotlin.String?,
        annotation: kotlin.String?
    ): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                ("Component's bindInstance method have incorrect signature: '%s'. "
                        + "Method Should have only %s annotation and GC scope annotations. "
                        + "Should have only one argument of providing object. "
                        + "Providing object should not be primitive or boxed primitive. "
                        + "Can return only providing object or should be void. "),
                className, annotation
            )
        )
        return this
    }

    fun componentGCMethodSignatureIncorrect(
        className: kotlin.String?,
        annotation: kotlin.String?
    ): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                ("Component's gc method have incorrect signature: '%s'. "
                        + "Method Should have only %s annotation and GC scope annotations. "
                        + "Should no have arguments. "
                        + "Should be void."),
                className, annotation
            )
        )
        return this
    }

    fun componentSwitchCacheMethodSignatureIncorrect(
        className: kotlin.String?,
        annotation: kotlin.String?
    ): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                ("Component's SwitchCache method have incorrect signature: '%s'. "
                        + "Method Should have only %s annotation and GC scope annotations. "
                        + "Should no have arguments. "
                        + "Should be void."),
                className, annotation
            )
        )
        return this
    }

    fun componentProtectInjectedMethodSignatureIncorrect(
        className: kotlin.String?,
        annotation: kotlin.String?
    ): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                ("Component's ProtectInjected method have incorrect signature: '%s'. "
                        + "Method Should have only %s annotation. "
                        + "Should have only one argument (non primitive and non boxed primitive). "
                        + "Should be void."),
                className, annotation
            )
        )
        return this
    }


    fun componentMethodNameBusy(methodName: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(
            kotlin.String.format(
                Locale.ROOT,
                "Component's method name '%s' busy by lib's private interfaces",
                methodName
            )
        )
        return this
    }

    fun add(mes: kotlin.String?): ExceptionStringBuilder {
        if (sb.length > 0) sb.append(" ")
        sb.append(mes)
        return this
    }


    fun collectCauseMessages(cause: Throwable?): ExceptionStringBuilder {
        var cause = cause
        while (cause != null) {
            if (sb.length > 0) sb.append("\nCaused by: ")
            sb.append(cause.message)
            cause = cause.cause
        }
        return this
    }

    fun build(): kotlin.String {
        return sb.toString()
    }

    companion object {
        fun createErrorMes(): ExceptionStringBuilder {
            return ExceptionStringBuilder()
        }
    }
}
