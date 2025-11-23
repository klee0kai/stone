package com.github.klee0kai.thekey.stone.ksp.helpers

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ClassName

val String.componentClName: String get() = "${this}StoneComponent"

val String.factoryClName: String get() = "${this}_FStone"

val String.moduleClName: String get() = "${this}_MStone"

val String.cacheControlClName: String get() = "${this}_CCMStone"

val String.hiddenModuleClName: String get() = "${this}_HMStone"

val String.wrapperClName: String get() = "${this}_TWStone"

val KSFunctionDeclaration.cacheControlMethodName get() = "__" + simpleName.asString() + "_cache"

val KSClassDeclaration.componentStoneClName
    get() = ClassName(
        packageName.asString(),
        simpleName.getShortName().componentClName,
    )

val KSClassDeclaration.factoryStoneClName
    get() = ClassName(
        packageName.asString(),
        simpleName.getShortName().factoryClName,
    )

val KSClassDeclaration.moduleStoneClName
    get() = ClassName(
        packageName.asString(),
        simpleName.getShortName().moduleClName,
    )


val KSClassDeclaration.cacheControlStoneClName
    get() = ClassName(
        packageName.asString(),
        simpleName.getShortName().cacheControlClName,
    )


val KSClassDeclaration.hiddenModuleStoneClName
    get() = ClassName(
        packageName.asString(),
        simpleName.getShortName().hiddenModuleClName,
    )

val KSClassDeclaration.wrapperStoneClName
    get() = ClassName(
        packageName.asString(),
        simpleName.getShortName().wrapperClName,
    )