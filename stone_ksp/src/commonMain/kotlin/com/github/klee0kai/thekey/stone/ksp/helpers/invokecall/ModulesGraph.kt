@file:OptIn(KspExperimental::class)

package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall

import com.github.klee0kai.stone.__hidden__.CacheAction
import com.github.klee0kai.stone.__hidden__.provide.ProvideBuilder
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.thekey.stone.ksp.exceptions.IncorrectSignatureException
import com.github.klee0kai.thekey.stone.ksp.exceptions.ObjectNotProvidedException
import com.github.klee0kai.thekey.stone.ksp.exceptions.RecursiveProviding
import com.github.klee0kai.thekey.stone.ksp.exceptions.StoneException
import com.github.klee0kai.thekey.stone.ksp.helpers.cacheControlMethodName
import com.github.klee0kai.thekey.stone.ksp.helpers.identifierParameters
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model.*
import com.github.klee0kai.thekey.stone.ksp.helpers.qualifierAnnotations
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapHelper
import com.github.klee0kai.thekey.stone.ksp.ksp.getAllMethods
import com.github.klee0kai.thekey.stone.ksp.ksp.isNotPrimitive
import com.github.klee0kai.thekey.stone.ksp.utils.LocalFieldName.genLocalFieldName
import com.github.klee0kai.thekey.stone.ksp.utils.RecursiveDetector
import com.github.klee0kai.thekey.stone.ksp.utils.removeDoubles
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import java.util.*

class ModulesGraph(
    val wrapHelper: WrapHelper,
    val identifierTypes: List<KSType>,
) {

    private val provideTypeCodes = HashMap<TypeName, HashSet<InvokeCall>>()
    private val cacheControlTypeCodes = HashMap<TypeName, HashSet<InvokeCall>>()

    /**
     * Methods graph build.
     *
     * @param provideModuleMethod module's provide method
     * @param module              module's class
     */
    fun collectFromModule(
        provideModuleMethod: KSFunctionDeclaration?,
    ) {
        val module = provideModuleMethod?.returnType?.resolve()?.declaration as? KSClassDeclaration ?: return
        for (m in module.getAllMethods(false, true, "<init>")) {
            if (m.returnType?.resolve()?.isNotPrimitive == false) continue
            val returnType = m.returnType?.resolve()?.toTypeName() ?: continue
            val provTypeName = wrapHelper.nonWrappedType(returnType)
            val isCached = m.getAnnotationsByType(Provide::class)
                .firstOrNull()?.cache !in listOf(Provide.CacheType.Factory, null)
            val isBindInstance = m.getAnnotationsByType(BindInstance::class).firstOrNull() != null

            provideTypeCodes.putIfAbsent(provTypeName, HashSet<InvokeCall>())
            provideTypeCodes[provTypeName]?.add(
                InvokeCall.fromSequence(
                    wrapHelper = wrapHelper,
                    callSequence = listOf(provideModuleMethod.toMethodDetail(), m.toMethodDetail()),
                    flags = InvokeProvideFlags(
                        provideObjectCached = isCached,
                        provideBindInstance = isBindInstance,
                    ),
                )
            )

            val cacheControlMethod = MethodDetail(
                methodName = m.cacheControlMethodName,
                returnType = wrapHelper.listWrapTypeIfNeed(returnType),
                qualifierAnns = m.qualifierAnnotations.map { it.toQualifierAnn() }.toSet(),
                args = buildList {
                    add(FieldDetail.simple("__action", CacheAction::class.asClassName()))
                    addAll(m.parameters.identifierParameters(identifierTypes).map { it.toFieldDetail() })
                },
            )

            cacheControlTypeCodes.putIfAbsent(provTypeName, HashSet<InvokeCall>())
            cacheControlTypeCodes.get(provTypeName)
                ?.add(
                    InvokeCall.fromSequence(
                        wrapHelper = wrapHelper,
                        callSequence = listOf(provideModuleMethod.toMethodDetail(), cacheControlMethod)
                    )
                )
        }
    }

    fun codeProvideType(
        methodName: String?,
        returnType: TypeName,
        qualifierAnns: Set<QualifierAnn>,
        declaredFields: List<FieldDetail>
    ): CodeBlock? {
        val isWrappedReturn = wrapHelper.isSupport(returnType)
        val providingType = if (isWrappedReturn) wrapHelper.nonWrappedType(returnType) else returnType
        val provideDeps = HashSet<ProvideDep>()
        provideDeps.add(
            ProvideDep(
                methodName = methodName,
                typeName = wrapHelper.listWrapTypeIfNeed(returnType),
                qualifierAnns = qualifierAnns,
            )
        )
        val provideTypeInvokes = provideInvokesWithDeps(provideDeps.iterator().next())
        if (provideTypeInvokes == null || provideTypeInvokes.isEmpty()) {
            return null
        }
        for (provideTypeInvoke in provideTypeInvokes) provideDeps.addAll(provideTypeInvoke.argDeps())
        if (provideTypeInvokes.size == 1 && !wrapHelper.isList(returnType)) {
            val invokeCall = provideTypeInvokes.first().best()
            return wrapHelper.transform(
                invokeCall.rawReturnType(),
                returnType,
                invokeCall.invokeCode(declaredFields)
            )
        }

        val provideBuilder = ProvideBuilder::class.asClassName().parameterizedBy(providingType)
        val provideBuilderList = Collection::class.asClassName().parameterizedBy(providingType)

        val listFieldName = genLocalFieldName()
        val localVariables = LinkedList<FieldDetail>(declaredFields)

        val codeBlock = CodeBlock.builder()
        codeBlock.add("%T{  %L -> \n", provideBuilder, listFieldName)

        for (inv in provideTypeInvokes) {
            val isCacheProvide = inv.flags.provideObjectCached
            val isSingleDepRequired = provideDeps.any {
                wrapHelper.nonWrappedType(it.typeName) == wrapHelper.nonWrappedType(inv.resultType())
                        && !wrapHelper.isList(it.typeName)
            }
            val isListDepRequired = provideDeps.any {
                wrapHelper.nonWrappedType(it.typeName) == wrapHelper.nonWrappedType(inv.resultType())
                        && wrapHelper.isList(it.typeName)
            }
            var singleDepField = FieldDetail(
                name = genLocalFieldName(),
                type = inv.resultType(),
                qualifierAnns = inv.qualifierAnnotations(true)
            )
            val listDepField = FieldDetail(
                name = genLocalFieldName(),
                type = Ref::class.asClassName().parameterizedBy(
                    List::class.asClassName().parameterizedBy(
                        inv.resultType()
                    )
                ),
                qualifierAnns = inv.qualifierAnnotations(true)
            )

            if (isSingleDepRequired) {
                if (isCacheProvide) {
                    codeBlock.add("val %L : %T = ", singleDepField.name, inv.resultType())
                        .add(
                            wrapHelper.transform(
                                inv.best().rawReturnType(),
                                inv.resultType(),
                                inv.best().invokeCode(localVariables)
                            )
                        )
                        .addStatement("")


                    localVariables.add(singleDepField)
                } else {
                    singleDepField = singleDepField
                        .copy(
                            type = Ref::class.asClassName().parameterizedBy(inv.resultType()),
                        )

                    codeBlock.add("val %L: %T = { ", singleDepField.name, singleDepField.type)
                        .add(
                            wrapHelper.transform(
                                inv.best().rawReturnType(),
                                inv.resultType(),
                                inv.best().invokeCode(localVariables),
                            )
                        )
                        .addStatement(" } ")

                    localVariables.add(singleDepField)
                }
            }

            if (isListDepRequired) {
                codeBlock.add("val %L : %T = { ", listDepField.name, listDepField.type)
                    .add(inv.invokeAllToList(localVariables))
                    .addStatement(" } ")

                localVariables.add(listDepField)
            }


            if (inv.resultType() == providingType) {
                if (wrapHelper.isList(returnType)) {
                    codeBlock.add(
                        "%L.addAll( %L )\n",
                        listFieldName,
                        wrapHelper.transform(
                            listDepField.type,
                            provideBuilderList,
                            CodeBlock.of(listDepField.name)
                        )
                    )
                } else {
                    codeBlock.add(
                        "%L.add( %L );\n",
                        listFieldName,
                        wrapHelper.transform(
                            singleDepField.type,
                            providingType,
                            CodeBlock.of(singleDepField.name)
                        )
                    )
                }
                if (!wrapHelper.isList(returnType)) break
            }
        }

        codeBlock.add("\n  })")
        if (wrapHelper.isList(returnType)) {
            codeBlock.add(".all() ")

            return wrapHelper.transform(
                List::class.asClassName().parameterizedBy(providingType),
                returnType,
                codeBlock.build()
            )

        } else {
            codeBlock.add(".first() ")

            return wrapHelper.transform(
                providingType,
                returnType,
                codeBlock.build()
            )
        }
    }


    fun provideInvokesWithDeps(provideDep: ProvideDep): List<InvokeCall>? {
        var provideTypeInvokes = LinkedList<InvokeCall>()
        var needProvideDeps = LinkedList<ProvideDep>()
        val needProvideDepsRecursiveDetector = RecursiveDetector<Int>()
        needProvideDeps.add(provideDep)
        var loopCount = 0

        // provide dependencies while not provide all
        while (!needProvideDeps.isEmpty()) {
            val rawDep = needProvideDeps.pollFirst()
            val dep = wrapHelper.nonWrappedType(rawDep.typeName)
            val invokeCall = provideTypeInvokeCall(
                provideTypeCodes,
                dep,
                rawDep.qualifierAnns,
                rawDep.methodName,
                wrapHelper.isList(rawDep.typeName)
            )
            if (invokeCall == null) {
                if (provideDep == rawDep) return null
                throw ObjectNotProvidedException("Error provide type ${rawDep.typeName}")
            }

            val isBindInstanceInvoke = invokeCall.flags.provideBindInstance
            val newDeps = invokeCall.argDeps().filter {
                if (isBindInstanceInvoke && rawDep.typeName == it.typeName) {
                    // bind instance case. Argument and return type are equals
                    return@filter false
                }
                // qualifies not need to provide
                val argNonWrapped = wrapHelper.nonWrappedType(it.typeName)
                argNonWrapped is ClassName && !identifierTypes.any { it.toTypeName() == argNonWrapped }
            }
            needProvideDeps.addAll(newDeps)

            needProvideDeps = LinkedList(needProvideDeps.removeDoubles { a, b -> a == b })

            val recursiveDetected = !newDeps.isEmpty()
                    && needProvideDepsRecursiveDetector.next(needProvideDeps.hashCode())
            if (recursiveDetected) {
                throw RecursiveProviding("Error provide type ${provideDep.typeName}. Recursive providing detected.")
            }

            if (loopCount++ > MAX_PROVIDE_RESOLVE_COUNT) {
                throw StoneException(
                    "Error provide type ${provideDep.typeName}. " +
                            "Long providing loop for type. Stone library Error. "
                )
            }

            provideTypeInvokes.add(invokeCall)
            provideTypeInvokes = LinkedList(
                provideTypeInvokes.removeDoubles { it1, it2 ->
                    it1.resultType() == it2.resultType()
                            && it1.qualifierAnnotations(true) == it2.qualifierAnnotations(true)
                }
            )
        }
        provideTypeInvokes.reverse()
        return provideTypeInvokes
    }

    /**
     * Generate cache control method invoke. Clean refs, change ref type and other
     *
     * @param provideMethodName predefined method name
     * @param typeName          the name of the type whose cache needs to be changed
     * @return cache control invoke call
     */
    fun invokeControlCacheForType(
        provideMethodName: String,
        typeName: TypeName,
        qualifierAnns: Set<QualifierAnn>
    ): InvokeCall? = provideTypeInvokeCall(
        cacheControlTypeCodes,
        typeName,
        qualifierAnns,
        provideMethodName.cacheControlMethodName,
        false
    )


    private fun provideTypeInvokeCall(
        provideTypeCodes: Map<TypeName, Set<InvokeCall>>,
        typeName: TypeName,
        qualifierAnns: Set<QualifierAnn>,
        provideMethodName: String?,
        listVariants: Boolean,
    ): InvokeCall? {
        val invokeCalls = provideTypeCodes.getOrDefault(typeName, null)
        if (invokeCalls == null || invokeCalls.isEmpty()) return null

        var filtered = if (!listVariants || !qualifierAnns.isEmpty()) {
            invokeCalls.filter {
                it.qualifierAnnotations(false) == qualifierAnns
            }
        } else {
            invokeCalls
        }

        filtered = if (provideMethodName != null) {
            filtered.filter { provideMethodName == it.bestSequence().last().methodName }
        } else filtered

        if (!listVariants && filtered.size > 1) {
            throw IncorrectSignatureException(
                "Error provide type $typeName " +
                        ": is bound multi times.\n " +
                        filtered.joinToString(" and ")
            )
        }
        return if (!filtered.isEmpty()) InvokeCall.fromVariants(wrapHelper, variants = filtered.toList()) else null
    }

    companion object {
        const val SIMPLE_PROVIDE_OPTIMIZING: Boolean = true
        const val MAX_PROVIDE_RESOLVE_COUNT: Int = 10000
    }

}
