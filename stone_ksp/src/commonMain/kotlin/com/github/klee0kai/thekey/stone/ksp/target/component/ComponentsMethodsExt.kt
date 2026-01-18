package com.github.klee0kai.thekey.stone.ksp.target.component

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.stone.annotations.dependencies.Dependencies
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.thekey.stone.ksp.exceptions.IncorrectSignatureException
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.annotations
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.anyAnnotation
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.hasOnlyAnnotation
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.stoneControlAnnotations
import com.github.klee0kai.thekey.stone.ksp.helpers.scopeAnnotations
import com.github.klee0kai.thekey.stone.ksp.ksp.isClassReturn
import com.github.klee0kai.thekey.stone.ksp.ksp.isNotPrimitive
import com.github.klee0kai.thekey.stone.ksp.ksp.isUnit
import com.github.klee0kai.thekey.stone.ksp.ksp.resolveNotNullable
import com.github.klee0kai.thekey.stone.ksp.target.module.GenModuleProcessor
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

enum class BindInstanceType {
    BindInstance,
    BindInstanceAndProvide
}

val KSFunctionDeclaration.isModuleFactoryProvideMethod: Boolean
    get() {
        if (!isProvideMethodSimple) return false
        val moduleCl = returnType?.resolve()?.declaration as? KSClassDeclaration ?: return false
        if (!moduleCl.annotations(Module::class.asClassName()).any()) return false
        if (!annotations(ModuleOriginFactory::class.asClassName()).any()) return false

        if (parameters.isNotEmpty()) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must no have arguments",
                element = this,
            )
        }
        if (!hasOnlyAnnotation(ModuleOriginFactory::class.asClassName())) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must have only one annotation ${ModuleOriginFactory::class.simpleName}",
                element = this,
            )
        }
        checkMethodNameBusy()

        return true
    }

val KSFunctionDeclaration.isModuleProvideMethod: Boolean
    get() {
        if (!isProvideMethodSimple) return false
        val moduleCl = returnType?.resolve()?.declaration as? KSClassDeclaration ?: return false
        if (!moduleCl.annotations(Module::class.asClassName()).any()) return false
        if (annotations(ModuleOriginFactory::class.asClassName()).any()) return false

        if (parameters.isNotEmpty()) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must no have arguments",
                element = this,
            )
        }
        if (annotations.any()) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must no have annotations",
                element = this,
            )
        }
        checkMethodNameBusy()

        return true
    }

val KSFunctionDeclaration.isDepsProvideMethod: Boolean
    get() {
        if (!isProvideMethodSimple) return false
        val depCl = returnType?.resolve()?.declaration as? KSClassDeclaration ?: return false
        if (!depCl.annotations(Dependencies::class.asClassName()).any()) return false
        if (parameters.isNotEmpty()) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must no have arguments",
                element = this,
            )
        }
        if (annotations.any()) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must no have annotations",
                element = this,
            )
        }
        checkMethodNameBusy()
        return true
    }

val KSFunctionDeclaration.isObjectProvideMethod: Boolean
    get() = isProvideMethodSimple
            && !isModuleProvideMethod
            && !isDepsProvideMethod
            && !isModuleFactoryProvideMethod
            && stoneControlAnnotations().none()

val KSFunctionDeclaration.isModuleInitMethod: Boolean
    get() {
        if (!annotations(Init::class.asClassName()).any()) return false

        if (!hasOnlyAnnotation(Init::class.asClassName())) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must have only one annotation ${Init::class.simpleName}",
                element = this,
            )
        }
        if (returnType?.resolve()?.isUnit == false) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must return unit",
                element = this,
            )
        }

        parameters.forEach {
            val clDeclaration = it.type
                .resolve()
                .declaration as? KSClassDeclaration
                ?: throw IncorrectSignatureException(
                    message = "${simpleName.asString()} must have only one parameter of Dependency or Module instance",
                    element = this,
                )


            if (!clDeclaration.anyAnnotation(Module::class.asClassName(), Dependencies::class.asClassName()).any()) {
                throw IncorrectSignatureException(
                    message = "${simpleName.asString()} must have only one parameter of Dependency or Module instance",
                    element = this,
                )
            }
        }
        checkMethodNameBusy()
        return true
    }

fun KSFunctionDeclaration.isExtOfMethod(
    clOwner: KSClassDeclaration,
): Boolean {
    if (!annotations(ExtendOf::class.asClassName()).any()) return false

    if (!hasOnlyAnnotation(ExtendOf::class.asClassName())) {
        throw IncorrectSignatureException(
            message = "${simpleName.asString()} must have only one annotation ${ExtendOf::class.simpleName}",
            element = this,
        )
    }
    if (parameters.size != 1) {
        throw IncorrectSignatureException(
            message = "${simpleName.asString()} must have only one parameter of Component instance",
            element = this,
        )
    }
    if (returnType?.resolve()?.isUnit == false) {
        throw IncorrectSignatureException(
            message = "${simpleName.asString()} must return unit",
            element = this,
        )
    }
    val argumentType = parameters.firstOrNull()?.type
        ?.resolveNotNullable()?.declaration as? KSClassDeclaration
        ?: throw IncorrectSignatureException(
            message = "${simpleName.asString()} must have only one parameter of Component instance",
            element = this,
        )

    if (!argumentType.annotations(Component::class.asClassName()).any()) {
        throw IncorrectSignatureException(
            message = "${argumentType.simpleName.asString()} must have @Component annotation",
            element = this,
        )
    }
    if (!clOwner.getAllSuperTypes().any { parent -> parent.toTypeName() == argumentType.toClassName() }) {
        throw IncorrectSignatureException(
            message = "The argument for the method ${simpleName.asString()} must be the parent class of the class ${clOwner.toClassName()}. " +
                    "The class ${argumentType.toClassName()} is not a parent to the class ${clOwner.toClassName()}.",
            element = this,
        )
    }

    checkMethodNameBusy()
    return true
}


val KSFunctionDeclaration.isBindInstanceMethod: BindInstanceType?
    get() {
        if (!annotations(BindInstance::class.asClassName()).any()) return null

        if (parameters.size != 1) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must have only one parameter of binding type",
                element = this,
            )
        }
        checkMethodNameBusy()
        val parameterIsNotPrimitive = parameters.first().type.resolveNotNullable().isNotPrimitive

        when {
            parameterIsNotPrimitive
                    && returnType?.resolveNotNullable()?.toTypeName() == parameters.first().type.resolveNotNullable().toTypeName() -> {
                return BindInstanceType.BindInstanceAndProvide
            }

            parameterIsNotPrimitive -> {
                return BindInstanceType.BindInstance
            }


        }

        throw IncorrectSignatureException(
            message = "${simpleName.asString()} has incorrect signature",
            element = this,
        )
    }

val KSFunctionDeclaration.isGcMethod: Boolean
    get() {
        if (!annotations(RunGc::class.asClassName()).any()) return false
        if (!scopeAnnotations.any()) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must use GC scope annotation",
                element = this,
            )
        }

        if (returnType?.resolve()?.isUnit == false) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must have return type is Unit",
                element = this,
            )
        }

        checkMethodNameBusy()
        return true
    }


val KSFunctionDeclaration.isSwitchCacheMethod: Boolean
    get() {
        if (!annotations(SwitchCache::class.asClassName()).any()) return false

        if (parameters.isNotEmpty()) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must no have arguments",
                element = this,
            )
        }
        if (returnType?.resolve()?.isUnit == false) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must have return type is Unit",
                element = this,
            )
        }
        checkMethodNameBusy()
        return true
    }

val KSFunctionDeclaration.isInjectMethod: Boolean
    get() {
        if (annotations.any()) return false
        if (returnType?.resolve()?.isUnit == false) return false
        if (parameters.isEmpty()) return false
        checkMethodNameBusy()
        return true
    }

val KSFunctionDeclaration.isProtectInjectedMethod: Boolean
    get() {
        if (!annotations(ProtectInjected::class.asClassName()).any()) return false
        if (returnType?.resolve()?.isUnit == false) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must have return type is Unit",
                element = this,
            )
        }
        if (parameters.size != 1) {
            throw IncorrectSignatureException(
                message = "${simpleName.asString()} must have only one parameter",
                element = this,
            )
        }
        checkMethodNameBusy()
        return true
    }


fun KSFunctionDeclaration.checkMethodNameBusy() {
    val reserved =
        simpleName.asString() in GenModuleProcessor.allReserveMethodNames + GenComponentProcessor.allReserveMethodNames
    if (reserved) {
        throw IncorrectSignatureException(
            message = "Function name ${simpleName.asString()} is reserved by stone library",
            element = this,
        )
    }
}


private val KSFunctionDeclaration.isProvideMethodSimple get() = isClassReturn()
