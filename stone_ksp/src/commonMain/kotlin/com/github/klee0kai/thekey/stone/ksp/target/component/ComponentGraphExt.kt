package com.github.klee0kai.thekey.stone.ksp.target.component

import com.github.klee0kai.thekey.stone.ksp.helpers.allIdentifierTypes
import com.github.klee0kai.thekey.stone.ksp.helpers.annotations.findComponentAnnotation
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.ModulesGraph
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapHelper
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.google.devtools.ksp.symbol.KSClassDeclaration

fun KSClassDeclaration.collectWrapHelper(
): WrapHelper {
    val wrapHelper = WrapHelper()
    val wrapperProviders = findComponentAnnotation().flatMap { it.wrapperProviders }
    // TODO

    return wrapHelper
}


fun KSClassDeclaration.collectComponentGraph(

): ModulesGraph {
    val modulesGraph = ModulesGraph(
        wrapHelper = collectWrapHelper(),
        identifierTypes = allIdentifierTypes.toList(),
    )
    modulesGraph.collectFromComponent(this)
    return modulesGraph
}