package com.github.klee0kai.thekey.stone.ksp.target

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toTypeName
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job

val KSFunctionDeclaration.isSuspend: Boolean get() = modifiers.contains(Modifier.SUSPEND)

val KSFunctionDeclaration.asyncReturnType: TypeName?
    get() {
        val returnType = returnType?.resolve()?.toTypeName()
        return when {
            returnType != null && !isSuspend -> returnType
            returnType != null && returnType != Unit::class.asClassName() ->
                Deferred::class.asClassName().parameterizedBy(returnType)

            isSuspend -> Job::class.asClassName()
            else -> returnType
        }
    }

fun FunSpec.Builder.declareSameParameters(
    function: KSFunctionDeclaration,
) = apply {
    function.returnType?.resolve()?.toTypeName()?.let { returns(it) }
    function.extensionReceiver?.resolve()?.toTypeName()?.let { receiver(it) }

    function.parameters.forEach { param ->
        addParameter(
            ParameterSpec.builder(
                name = param.name?.asString() ?: "",
                type = param.type.resolve().toTypeName(),
            ).apply {
                if (param.isVararg) addModifiers(KModifier.VARARG)
            }.build()
        )
    }
}
