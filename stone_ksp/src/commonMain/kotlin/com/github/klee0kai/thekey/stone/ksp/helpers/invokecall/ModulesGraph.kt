package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall

import com.github.klee0kai.stone.__hidden__.provide.ProvideBuilder
import com.github.klee0kai.thekey.stone.ksp.helpers.isListType
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapHelper
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.add
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.declareLocalVariable
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.smartCode
import com.github.klee0kai.thekey.stone.ksp.utils.LocalFieldName.genLocalFieldName
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

class ModulesGraph {
    private val provideTypeCodes = HashMap<TypeName, Set<InvokeCall>>()
    private val cacheControlTypeCodes = HashMap<TypeName, Set<InvokeCall>>()
    private val wrapHelper = WrapHelper()

    /**
     * Methods graph build.
     *
     * @param provideModuleMethod module's provide method
     * @param module              module's class
     */
    fun collectFromModule(
//        provideModuleMethod: MethodDetail?,
//        module: ClassDetail,
    ) {
//        val iModuleInterface: ClassDetail = AnnotationProcessor.allClassesHelper.iModule
//        for (m in module.getAllMethods(false, true, "<init>")) {
//            val provTypeName: TypeName = nonWrappedType(m.returnType)
//            if (provTypeName.isPrimitive() || provTypeName === TypeName.VOID) continue
//            if (iModuleInterface.findMethod(m, false) != null) continue
//            val isCached =
//                !m.hasAnnotations(ProvideAnn::class.java) || m.ann(ProvideAnn::class.java).isCachingProvideType()
//            val isBindInstance: Boolean = m.hasAnnotations(BindInstanceAnn::class.java)
//            var invokeProvideFlags = if (isCached) INVOKE_PROVIDE_OBJECT_CACHED else 0
//            invokeProvideFlags = invokeProvideFlags or if (isBindInstance) INVOKE_PROVIDE_BIND_INSTANCE else 0
//
//            provideTypeCodes.putIfAbsent(provTypeName, HashSet<InvokeCall?>())
//            provideTypeCodes.get(provTypeName)!!.add(InvokeCall(invokeProvideFlags, provideModuleMethod, m))
//
//            val cacheControlMethod: MethodDetail = MethodDetail()
//            cacheControlMethod.methodName = cacheControlMethodName(m.methodName)
//            cacheControlMethod.args.add(FieldDetail.simple("__action", ClassName.get(CacheAction::class.java)))
//            for (it in m.args) {
//                if (!((it.type is ClassName) && allClassesHelper.allIdentifiers.contains(it.type))) continue
//                cacheControlMethod.args.add(it)
//            }
//            cacheControlMethod.returnType = listWrapTypeIfNeed(m.returnType)
//            cacheControlMethod.qualifierAnns = m.qualifierAnns
//
//            cacheControlTypeCodes.putIfAbsent(provTypeName, HashSet<InvokeCall?>())
//            cacheControlTypeCodes.get(provTypeName)!!.add(InvokeCall(provideModuleMethod, cacheControlMethod))
//        }
    }

    fun codeProvideType(
        methodName: String,
        returnType: KSType,
        qualifierAnns: List<KSAnnotation> = emptyList(),
    ) = smartCode {
        val isWrappedReturn = wrapHelper.isSupport(returnType.toClassName())
        val isListReturn = returnType.isListType()
        val providingType = if (isWrappedReturn) {
            wrapHelper.nonWrappedType(returnType.toClassName())
        } else {
            returnType.toClassName()
        }
        this.providingType.value = providingType

        val provideTypeInvokes = provideInvokesWithDeps(ProvideDep(methodName, returnType, qualifierAnns))
        if (provideTypeInvokes.isEmpty()) {
            // TODO throw error
            return@smartCode
        }

        if (SIMPLE_PROVIDE_OPTIMIZING && provideTypeInvokes.size == 1 && !isListReturn) {
            val invokeCall = provideTypeInvokes.first()
            add(
                wrapHelper.transform(
                    code = smartCode {
                        add(invokeCall.invokeBest())
                        this.providingType.value = invokeCall.resultType()
                    },
                    wannaType = returnType.toClassName(),
                )
            )
            return@smartCode
        }


        val provideBuilder = ProvideBuilder::class.asClassName().parameterizedBy(providingType)
        val provideBuilderList = Collection::class.asClassName().parameterizedBy(providingType)
        val listFieldName = genLocalFieldName()
        add("%T( ( %L ) -> { \n", provideBuilder, listFieldName)


        for (inv in provideTypeInvokes) {
            val depFieldName = genLocalFieldName()
//            val isCacheProvide = (inv.flags and INVOKE_PROVIDE_OBJECT_CACHED) !== 0
//            val singleDepField: FieldDetail = FieldDetail.simple(genLocalFieldName(), null)
//            val listDepField: FieldDetail = FieldDetail.simple(genLocalFieldName(), null)
//            val isListInv = inv.invokeSequenceVariants.size() > 1

            add {
                if (inv.flags.provideObjectCached) {
//                    declareLocalVariable()
                } else {

                }


            }

//
//            builder.withLocals({ localBuilder ->
//                // provide single objects
//                if (isCacheProvide) {
//                    localBuilder.localVariable(singleDepField.name, inv.qualifierAnnotations(true), inv.invokeBest())
//                    singleDepField.type = inv.resultType()
//                } else {
//                    singleDepField.type = ParameterizedTypeName.get(ClassName.get(Ref::class.java), inv.resultType())
//                    localBuilder.localVariable(
//                        singleDepField.name, inv.qualifierAnnotations(true), SmartCode.builder()
//                            .add("() -> ")
//                            .add(inv.invokeBest())
//                            .providingType(singleDepField.type)
//                    )
//                }
//                localBuilder
//            })
//
//            builder.withLocals({ localBuilder ->
//                // provide list objects
//                listDepField.type = ParameterizedTypeName.get(
//                    ClassName.get(Ref::class.java),
//                    ParameterizedTypeName.get(
//                        ClassName.get(MutableList::class.java),
//                        inv.resultType()
//                    )
//                )
//                localBuilder.localVariable(
//                    listDepField.name, inv.qualifierAnnotations(true), SmartCode.builder()
//                        .add("() -> ")
//                        .add(inv.invokeAllToList())
//                        .providingType(listDepField.type)
//                )
//                localBuilder
//            })
//
//            if (inv.resultType() == providingType) {
//                builder.withLocals({ localBuilder ->
//                    if (isListReturn) {
//                        localBuilder
//                            .add(listFieldName)
//                            .add(".addAll( ")
//                            .add(
//                                transform(
//                                    SmartCode.of(
//                                        listDepField.name,
//                                        mutableSetOf<T?>(listDepField.name)
//                                    )
//                                        .providingType(listDepField.type),
//                                    provideBuilderList
//                                )
//                            ).add(");\n")
//                    } else {
//                        localBuilder
//                            .add(listFieldName)
//                            .add(".add( ")
//                            .add(
//                                transform(
//                                    SmartCode.of(
//                                        singleDepField.name,
//                                        mutableSetOf<T?>(singleDepField.name)
//                                    )
//                                        .providingType(singleDepField.type),
//                                    providingType
//                                )
//                            ).add(");\n")
//                    }
//                    localBuilder
//                })
//                if (!isListReturn) break
//            }
        }
//
//
//        builder.add("\n  })")
//        if (WrapHelper.isList(returnType)) {
//            builder.add(".all() ")
//                .providingType(
//                    ParameterizedTypeName.get(
//                        ClassName.get(MutableList::class.java),
//                        providingType
//                    )
//                )
//        } else {
//            builder.add(".first() ")
//                .providingType(providingType)
//        }
//        return WrapHelper.transform(builder, returnType)
    }


    fun provideInvokesWithDeps(
        provideDep: ProvideDep,
    ): List<InvokeCall> {
        TODO()
//        var provideTypeInvokes: LinkedList<InvokeCall> = LinkedList<InvokeCall>()
//        var needProvideDeps: LinkedList<ProvideDep> = LinkedList<ProvideDep>()
//        val needProvideDepsRecursiveDetector: RecursiveDetector<Int?> = RecursiveDetector()
//        needProvideDeps.add(provideDep)
//        var loopCount = 0
//
//        // provide dependencies while not provide all
//        while (!needProvideDeps.isEmpty()) {
//            val rawDep: ProvideDep = needProvideDeps.pollFirst()
//            val dep: TypeName = nonWrappedType(rawDep.typeName)
//            val invokeCall: InvokeCall? = provideTypeInvokeCall(
//                provideTypeCodes,
//                dep,
//                rawDep.qualifierAnns,
//                rawDep.methodName,
//                isList(rawDep.typeName)
//            )
//            if (invokeCall == null) {
//                if (provideDep == rawDep) {
//                    return null
//                }
//
//                throw ObjectNotProvidedException(
//                    createErrorMes()
//                        .errorProvideType(dep.toString())
//                        .build()
//                )
//            }
//
//            val isBindInstanceInvoke = (invokeCall.flags and INVOKE_PROVIDE_BIND_INSTANCE) !== 0
//            val newDeps: MutableList<ProvideDep?> = ListUtils.filter(invokeCall.argDeps(), { i, it ->
//                if (isBindInstanceInvoke && rawDep.typeName == it.typeName) {
//                    // bind instance case. Argument and return type are equals
//                    return@filter false
//                }
//                // qualifies not need to provide
//                val argNonWrapped: TypeName? = nonWrappedType(it.typeName)
//                argNonWrapped is ClassName && !allClassesHelper.allIdentifiers.contains(argNonWrapped)
//            })
//
//            needProvideDeps.addAll(newDeps)
//            needProvideDeps = ListUtils.removeDoublesRight(needProvideDeps, Objects::equals)
//            val recursiveDetected =
//                !newDeps.isEmpty() && needProvideDepsRecursiveDetector.next(needProvideDeps.hashCode())
//            if (recursiveDetected) {
//                throw RecursiveProviding(
//                    createErrorMes()
//                        .errorProvideType(provideDep.typeName.toString())
//                        .recursiveProviding()
//                        .build()
//                )
//            }
//            if (loopCount++ > MAX_PROVIDE_RESOLVE_COUNT) {
//                throw StoneException(
//                    createErrorMes()
//                        .errorProvideType(provideDep.typeName.toString())
//                        .add("long providing loop for type. Stone library Error.")
//                        .build(),
//                    null
//                )
//            }
//
//            provideTypeInvokes.add(invokeCall)
//            provideTypeInvokes = ListUtils.removeDoublesRight(provideTypeInvokes, { it1, it2 ->
//                it1.resultType() == it2.resultType()
//                        && it1.qualifierAnnotations(true) == it2.qualifierAnnotations(true)
//            })
//        }
//        Collections.reverse(provideTypeInvokes)
//        return provideTypeInvokes
    }

    /**
     * Generate cache control method invoke. Clean refs, change ref type and other
     *
     * @param provideMethodName predefined method name
     * @param typeName          the name of the type whose cache needs to be changed
     * @return cache control invoke call
     */
    fun invokeControlCacheForType(
        provideMethodName: String?,
        typeName: TypeName,
        qualifierAnns: Set<KSAnnotation>
    ): InvokeCall? {
        TODO()
//        val cacheControlMethodName: String? = cacheControlMethodName(provideMethodName)
//        return provideTypeInvokeCall(cacheControlTypeCodes, typeName, qualifierAnns, cacheControlMethodName, false)
    }

    private fun provideTypeInvokeCall(
        provideTypeCodes: Map<TypeName, Set<InvokeCall>>,
        typeName: TypeName,
        qualifierAnns: Set<KSAnnotation>,
        provideMethodName: String?,
        listVariants: Boolean
    ): InvokeCall? {
        TODO()
//        val invokeCalls: MutableSet<InvokeCall?>? = provideTypeCodes.getOrDefault(typeName, null)
//        if (invokeCalls == null || invokeCalls.isEmpty()) return null
//        var filtered: MutableList<InvokeCall?> =
//            if (!listVariants || qualifierAnns != null && !qualifierAnns.isEmpty())
//                ListUtils.filter(invokeCalls, { i, it -> it.qualifierAnnotations(false) == qualifierAnns })
//            else
//                LinkedList<InvokeCall?>(invokeCalls)
//
//        filtered = if (provideMethodName != null) ListUtils.filter(filtered, { i, it ->
//            val len: Int = it.bestSequence().size()
//            val mName: String? = it.bestSequence().get(len - 1).methodName
//            provideMethodName == mName
//        }) else filtered
//
//        if (!listVariants && filtered.size > 1) {
//            throw IncorrectSignatureException(
//                createErrorMes()
//                    .errorProvideType(typeName.toString())
//                    .add(": is bound multi times.\n")
//                    .add(java.lang.String.join(" and \n", ListUtils.format(filtered, InvokeCall::toString)))
//                    .build()
//            )
//        }
//        return if (!filtered.isEmpty()) InvokeCall(filtered) else null
    }

    companion object {
        var SIMPLE_PROVIDE_OPTIMIZING: Boolean = true
        var MAX_PROVIDE_RESOLVE_COUNT: Int = 10000
    }
}
