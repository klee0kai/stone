package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.wrappers.AsyncCoroutineProvide
import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.stone.wrappers.PhantomProvide
import com.github.klee0kai.thekey.stone.ksp.exceptions.ExceptionStringBuilder
import com.github.klee0kai.thekey.stone.ksp.exceptions.StoneException
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.ClassNameUtils.rawTypeOf
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.SmartCode
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.add
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.smartCode
import com.github.klee0kai.thekey.stone.ksp.property.map
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
import java.lang.ref.Reference
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import javax.inject.Provider

class WrapHelper {

    private var wrapTypes = HashMap<TypeName, WrapType>()

    init {
        fillStdWrappers()
    }

    fun isList(typeName: TypeName): Boolean = allParamTypes(typeName).any {
        val wrapType = wrapTypes[rawTypeOf(it)]
        wrapType != null && wrapType.isList
    }


    fun listWrapTypeIfNeed(typeName: TypeName): TypeName {
        if (isList(typeName)) List::class
            .asClassName()
            .parameterizedBy(nonWrappedType(typeName))

        return nonWrappedType(typeName);
    }

    fun transform(code: SmartCode, wannaType: TypeName): SmartCode {
        if (code.providingType.value == null || code.providingType.value == wannaType) {
            return code
        }
        var smartCode = SmartCode().apply { add(code) }

        var wrapPathNames = allParamTypes(wannaType).reversed()
        var unwrapPathNames = allParamTypes(code.providingType.value!!)
        while (
            !wrapPathNames.isEmpty() && !unwrapPathNames.isEmpty()
            && rawTypeOf(unwrapPathNames.last()) == rawTypeOf(wrapPathNames.first())
        ) {
            unwrapPathNames = unwrapPathNames.dropLast(1)
            wrapPathNames = wrapPathNames.drop(1)
        }

        val wrapTypeFormat: (TypeName) -> WrapType = {
            wrapTypes.getOrElse(rawTypeOf(it)) {
                throw StoneException(
                    ExceptionStringBuilder.createErrorMes()
                        .typeTransformNonSupport(
                            ClassNameUtils.noWildCardType(code.providingType.value!!),
                            wannaType
                        )
                        .classNonFound(it.toString())
                        .build(),
                )
            }
        }

        var unwrapPath = unwrapPathNames.map(wrapTypeFormat)
        var wrapPath = wrapPathNames.map(wrapTypeFormat)

        while (!unwrapPath.isEmpty()) {
            val unwrapType = unwrapPath.first()
            if (unwrapType.isList) {
                val wrapListIndex = wrapPath.indexOfFirst { it.isList }
                if (wrapListIndex >= 0) {
                    val unWrapItemType = paramType(unwrapPathNames.first())
                    val wrapItemType = paramType(wrapPathNames[wrapListIndex])
                    val wrapListType = wrapPath[wrapListIndex]
                    smartCode = wrapListType.inListFormat!!.formatCode(
                        smartCode,
                        FormatSimple { listItemCode ->
                            listItemCode.providingType.value = unWrapItemType
                            transform(listItemCode, wrapItemType)
                        })

                    for (i in 0..wrapListIndex) {
                        wrapPath = wrapPath.drop(1)
                        wrapPathNames.drop(1)
                    }
                    unwrapPath = emptyList()
                    unwrapPathNames = emptyList()
                    break
                }
            }
            smartCode = unwrapType.unwrap.formatCode(smartCode)
            unwrapPath = unwrapPath.drop(1)
            unwrapPathNames = unwrapPathNames.drop(1)
        }

        while (!wrapPath.isEmpty()) {
            smartCode = wrapPath[0].wrap.formatCode(smartCode)
            wrapPath = wrapPath.drop(1)
            wrapPathNames = wrapPathNames.drop(1)
        }

        smartCode.providingType.value = wannaType
        return smartCode
    }

    fun nonWrappedType(typeName: TypeName): TypeName {
        if (typeName is ParameterizedTypeName) {
            if (isSupport(typeName.rawType) && !typeName.typeArguments.isEmpty()) {
                return nonWrappedType(typeName.typeArguments.first())
            }
        }
        if (typeName is WildcardTypeName) {
            if (!typeName.outTypes.isEmpty()) {
                return nonWrappedType(typeName.outTypes.first())
            }
        }
        return typeName
    }

    fun allParamTypes(typeName: TypeName): List<TypeName> {
        var typeName = typeName
        typeName = ClassNameUtils.noWildCardType(typeName)
        val allParams = mutableListOf<TypeName>()
        while (true) {
            allParams.add(typeName)
            val paramType = typeName as? ParameterizedTypeName
            if (paramType == null || paramType.typeArguments.isEmpty()) break
            typeName = ClassNameUtils.noWildCardType(paramType.typeArguments.first())
        }
        return allParams
    }

    fun support(wrapType: WrapType) {
        wrapTypes.putIfAbsent(wrapType.typeName, wrapType)
    }

    fun isSupport(typeName: TypeName): Boolean = wrapTypes.containsKey(rawTypeOf(typeName))

    fun paramType(typeName: TypeName): TypeName {
        if (typeName is ParameterizedTypeName) {
            if (isSupport(typeName.rawType) && !typeName.typeArguments.isEmpty()) {
                return typeName.typeArguments.first()
            }
        }
        return typeName
    }

    private fun fillStdWrappers() {
        for (cl in listOf(
            WeakReference::class,
            SoftReference::class,
            Reference::class
        )) {
            val wrapper = cl.asClassName()
            val creator = if (cl != Reference::class) wrapper else WeakReference::class.asClassName()

            support(
                WrapType(
                    isNoCachingWrapper = false,
                    typeName = wrapper,
                    wrap = FormatSimple { or ->
                        smartCode {
                            add(or)
                            add("?.let{ %T( it ) }", creator)

                            providingType.source = or.providingType.map { orType ->
                                orType?.let { wrapper.parameterizedBy(it) }
                            }
                        }
                    },
                    unwrap = FormatSimple { or ->
                        smartCode {
                            add("or")
                            add("?.get()")

                            providingType.source = or.providingType.map { orType ->
                                orType?.let { paramType(it) }
                            }
                        }
                    },
                )
            )
        }

        for (cl in listOf(
            PhantomProvide::class,
            Ref::class,
            Provider::class,
            LazyProvide::class,
            AsyncCoroutineProvide::class,
        )) {
            val wrapper = cl.asClassName()

            val isNoCachingWrapper = cl !in listOf(LazyProvide::class, AsyncCoroutineProvide::class)
            val creator = if (isNoCachingWrapper) PhantomProvide::class.asClassName() else wrapper
            support(
                WrapType(
                    typeName = wrapper,
                    isNoCachingWrapper = cl != LazyProvide::class && cl != AsyncCoroutineProvide::class,
                    isAsyncProvider = true,
                    wrap = FormatSimple { or ->
                        smartCode {
                            add("%S{", creator)
                            add(or)
                            add("} ")

                            providingType.source = or.providingType.map { orType ->
                                orType?.let { wrapper.parameterizedBy(it) }
                            }
                        }
                    },
                    unwrap = FormatSimple { or ->
                        smartCode {
                            add("or")
                            add("?.get()")
                            providingType.source = or.providingType.map { orType ->
                                orType?.let { paramType(it) }
                            }
                        }
                    }
                )
            )
        }

        for (cl in listOf(
            List::class,
            Array::class,
            MutableList::class,
            MutableCollection::class
        )) {
            val wrapper = cl.asClassName()
            val creatorFun = when (cl) {
                List::class -> "listOf"
                Array::class -> "arrayOf"
                MutableList::class,
                MutableCollection::class -> "mutableListOf"

                else -> "listOf"
            }

            support(
                WrapType(
                    typeName = wrapper,
                    wrap = FormatSimple { or ->
                        smartCode {
                            add("%L(", creatorFun)
                            add(or)
                            add(")")

                            providingType.source = or.providingType.map { orType ->
                                orType?.let { wrapper.parameterizedBy(it) }
                            }
                        }
                    },
                    unwrap = FormatSimple { or ->
                        smartCode {
                            add("or")
                            add("?.firstOrNull()")
                            providingType.source = or.providingType.map { orType ->
                                orType?.let { paramType(it) }
                            }
                        }
                    },
                    inListFormat = FormatInList { originalListCode, itemTransformFun ->
                        smartCode {
                            add(originalListCode)
                            add(".map { ")
                            with(itemTransformFun) {
                                formatCode(smartCode("it"))
                            }
                            add(" }")
                        }
                    }
                ))

        }

    }
}