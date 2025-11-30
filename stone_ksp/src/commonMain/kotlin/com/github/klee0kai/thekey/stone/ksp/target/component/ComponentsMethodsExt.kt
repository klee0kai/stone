package com.github.klee0kai.thekey.stone.ksp.target.component

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.stone.annotations.dependencies.Dependencies
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.thekey.stone.ksp.exceptions.IncorrectSignatureException
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.annotations
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.anyAnnotation
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.hasOnlyAnnotation
import com.github.klee0kai.thekey.stone.ksp.helpers.scopeAnnotations
import com.github.klee0kai.thekey.stone.ksp.ksp.isClassReturn
import com.github.klee0kai.thekey.stone.ksp.ksp.isNotPrimitive
import com.github.klee0kai.thekey.stone.ksp.ksp.isUnit
import com.github.klee0kai.thekey.stone.ksp.target.GenModuleProcessor
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

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
            throw IncorrectSignatureException("${simpleName.asString()} must no have arguments")
        }
        if (!hasOnlyAnnotation(ModuleOriginFactory::class.asClassName())) {
            throw IncorrectSignatureException("${simpleName.asString()} must have only one annotation ${ModuleOriginFactory::class.simpleName}")
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
            throw IncorrectSignatureException("${simpleName.asString()} must no have arguments")
        }
        if (annotations.any()) {
            throw IncorrectSignatureException("${simpleName.asString()} must no have annotations")
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
            throw IncorrectSignatureException("${simpleName.asString()} must no have arguments")
        }
        if (annotations.any()) {
            throw IncorrectSignatureException("${simpleName.asString()} must no have annotations")
        }
        checkMethodNameBusy()
        return true
    }

val KSFunctionDeclaration.isObjectProvideMethod: Boolean
    get() = isProvideMethodSimple && !isModuleProvideMethod && !isDepsProvideMethod && !isModuleFactoryProvideMethod

val KSFunctionDeclaration.isModuleInitMethod: Boolean
    get() {
        if (!annotations(Init::class.asClassName()).any()) return false

        if (!hasOnlyAnnotation(Init::class.asClassName())) {
            throw IncorrectSignatureException("${simpleName.asString()} must have only one annotation ${Init::class.simpleName}")
        }
        if (parameters.size != 1) {
            throw IncorrectSignatureException("${simpleName.asString()} must have only one parameter of Dependency or Module instance")
        }
        if (returnType?.resolve()?.isUnit == false) {
            throw IncorrectSignatureException("${simpleName.asString()} must return unit")
        }

        parameters.forEach {
            val clDeclaration = it.type
                .resolve()
                .declaration as? KSClassDeclaration
                ?: throw IncorrectSignatureException("${simpleName.asString()} must have only one parameter of Dependency or Module instance")

            if (!clDeclaration.anyAnnotation(Module::class.asClassName(), Dependencies::class.asClassName()).any()) {
                throw IncorrectSignatureException("${simpleName.asString()} must have only one parameter of Dependency or Module instance")
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
        throw IncorrectSignatureException("${simpleName.asString()} must have only one annotation ${ExtendOf::class.simpleName}")
    }
    if (parameters.size != 1) {
        throw IncorrectSignatureException("${simpleName.asString()} must have only one parameter of Component instance")
    }
    if (returnType?.resolve()?.isUnit == false) {
        throw IncorrectSignatureException("${simpleName.asString()} must return unit")
    }
    val argumentType = parameters.firstOrNull()?.type
        ?.resolve() as? KSClassDeclaration
        ?: throw IncorrectSignatureException("${simpleName.asString()} must have only one parameter of Component instance")

    if (!argumentType.annotations(Component::class.asClassName()).any()) {
        throw IncorrectSignatureException("${argumentType.simpleName.asString()} must have @Component annotation")
    }
    if (!clOwner.getAllSuperTypes().any { parent -> parent.toClassName() == argumentType.toClassName() }) {
        throw IncorrectSignatureException(
            message = "The argument for the method ${simpleName.asString()} must be the parent class of the class ${clOwner.toClassName()}. " +
                    "The class ${argumentType.toClassName()} is not a parent to the class ${clOwner.toClassName()}."
        )
    }

    checkMethodNameBusy()
    return true
}


val KSFunctionDeclaration.isBindInstanceMethod: BindInstanceType?
    get() {
        if (!annotations(BindInstance::class.asClassName()).any()) return null

        if (!hasOnlyAnnotation(BindInstance::class.asClassName())) {
            throw IncorrectSignatureException("${simpleName.asString()} must have only one annotation ${BindInstance::class.simpleName}")
        }
        if (parameters.size != 1) {
            throw IncorrectSignatureException("${simpleName.asString()} must have only one parameter of binding type")
        }
        checkMethodNameBusy()

        when {
            returnType?.resolve()?.isNotPrimitive == true -> {
                return BindInstanceType.BindInstance
            }

            returnType?.resolve()?.toClassName() == parameters.first().type.resolve().toClassName() -> {
                return BindInstanceType.BindInstanceAndProvide
            }
        }

        throw IncorrectSignatureException("${simpleName.asString()} has incorrect signature")
    }

val KSFunctionDeclaration.isGcMethod: Boolean
    get() {
        if (!annotations(RunGc::class.asClassName()).any()) return false
        if (!scopeAnnotations.any()) {
            throw IncorrectSignatureException("${simpleName.asString()} must use GC scope annotation")
        }

        if (returnType?.resolve()?.isUnit == false) {
            throw IncorrectSignatureException("${simpleName.asString()} must have return type is Unit")
        }

        checkMethodNameBusy()
        return true
    }


val KSFunctionDeclaration.isSwitchCacheMethod: Boolean
    get() {
        if (!annotations(SwitchCache::class.asClassName()).any()) return false

        if (!hasOnlyAnnotation(SwitchCache::class.asClassName())) {
            throw IncorrectSignatureException("${simpleName.asString()} must use only ${SwitchCache::class.simpleName} annotation")
        }
        if (parameters.isNotEmpty()) {
            throw IncorrectSignatureException("${simpleName.asString()} must no have arguments")
        }
        if (returnType?.resolve()?.isUnit == false) {
            throw IncorrectSignatureException("${simpleName.asString()} must have return type is Unit")
        }
        checkMethodNameBusy()
        return true
    }

val KSFunctionDeclaration.isInjectMethod: Boolean
    get() {
        if (annotations.any()) return false
        if (returnType?.resolve()?.isUnit == false) return false
        if (parameters.size != 1) return false
        checkMethodNameBusy()
        return true
    }

val KSFunctionDeclaration.isProtectInjectedMethod: Boolean
    get() {
        if (!annotations(ProtectInjected::class.asClassName()).any()) return false
        if (returnType?.resolve()?.isUnit == false) {
            throw IncorrectSignatureException("${simpleName.asString()} must have return type is Unit")
        }
        if (parameters.size != 1) {
            throw IncorrectSignatureException("${simpleName.asString()} must have only one parameter")
        }
        checkMethodNameBusy()
        return true
    }


fun KSFunctionDeclaration.checkMethodNameBusy() {
    val reserved =
        simpleName.asString() in GenModuleProcessor.allReserveMethodNames + GenComponentProcessor.allReserveMethodNames
    if (reserved) throw IncorrectSignatureException("Function name ${simpleName.asString()} is reserved by stone library")
}


private val KSFunctionDeclaration.isProvideMethodSimple get() = isClassReturn()


