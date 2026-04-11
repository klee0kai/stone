package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.github.klee0kai.stone.Provider
import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.weakref.SoftRef
import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.stone.wrappers.AsyncLazy
import com.github.klee0kai.stone.wrappers.AsyncProvider
import com.github.klee0kai.stone.wrappers.LazyProvider
import com.github.klee0kai.thekey.stone.ksp.exceptions.StoneException
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.ClassNameUtils.noWildCardType
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.ClassNameUtils.rawTypeOf
import com.github.klee0kai.thekey.stone.ksp.poet.codeBlock
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.lang.ref.Reference
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.util.*


class WrapHelper {

    private var wrapTypes = HashMap<TypeName, WrapType>()

    init {
        std()
    }

    fun support(wrapType: WrapType) {
        wrapTypes.putIfAbsent(wrapType.typeName, wrapType)
    }

    fun isSupport(typeName: TypeName): Boolean = wrapTypes.containsKey(rawTypeOf(typeName))

    fun isNonCachingWrapper(typeName: TypeName): Boolean {
        for (t in allParamTypes(typeName)) {
            val wrapType = wrapTypes.get(rawTypeOf(t))
            if (wrapType != null && wrapType.isNoCachingWrapper) return true
        }
        return false
    }

    fun isList(typeName: TypeName): Boolean = allParamTypes(typeName).any {
        val wrapType = wrapTypes.get(rawTypeOf(it))
        wrapType != null && wrapType.isList
    }

    fun paramType(typeName: TypeName): TypeName {
        if (typeName is ParameterizedTypeName) {
            if (isSupport(typeName.rawType) && !typeName.typeArguments.isEmpty()) {
                return typeName.typeArguments.first()
            }
        }
        return typeName
    }

    /**
     * com.github.klee0kai.stone.wrappers.LazyProvide<com.github.klee0kai.test.tech.components.Battery> -> com.github.klee0kai.test.tech.components.Battery
     * ? extends java.lang.ref.WeakReference<com.github.klee0kai.test.car.model.Window> -> com.github.klee0kai.test.car.model.Window
    </com.github.klee0kai.test.car.model.Window></com.github.klee0kai.test.tech.components.Battery> */
    fun nonWrappedType(typeName: TypeName): TypeName {
        if (typeName is ParameterizedTypeName) {
            if (isSupport(typeName.rawType) && !typeName.typeArguments.isEmpty()) {
                return nonWrappedType(typeName.typeArguments.first())
            }
        }
        if (typeName is WildcardTypeName) {
            if (!typeName.outTypes.isEmpty()) return nonWrappedType(typeName.outTypes.first())
        }
        return typeName.copy(nullable = false)
    }

    /**
     * java.util.List<com.github.klee0kai.stone.wrappers.Ref></com.github.klee0kai.stone.wrappers.Ref><com.github.klee0kai.test.boxed.model.CarBox></com.github.klee0kai.test.boxed.model.CarBox><com.github.klee0kai.test.car.model.Window>>> ->
     * java.util.List<com.github.klee0kai.test.boxed.model.CarBox></com.github.klee0kai.test.boxed.model.CarBox><com.github.klee0kai.test.car.model.Window>>
    </com.github.klee0kai.test.car.model.Window></com.github.klee0kai.test.car.model.Window> */
    fun listWrapTypeIfNeed(typeName: TypeName): TypeName {
        if (isList(typeName)) return List::class.asClassName().parameterizedBy(nonWrappedType(typeName))
        return nonWrappedType(typeName)
    }

    fun allParamTypes(typeName: TypeName): List<TypeName> {
        var typeName = noWildCardType(typeName)
        val allParams = LinkedList<TypeName>()
        while (true) {
            allParams.add(typeName)
            val paramType = typeName as? ParameterizedTypeName
            if (paramType == null || paramType.typeArguments.isEmpty()) break
            typeName = noWildCardType(paramType.typeArguments[0])
        }
        return allParams
    }


    fun transform(
        providingType: TypeName,
        wannaType: TypeName,
        code: CodeBlock
    ): CodeBlock {
        if (providingType == wannaType) return code

        var codeBuilder = CodeBlock.builder().add(code)
        val wrapPathNames = LinkedList(allParamTypes(wannaType))
        val unwrapPathNames = LinkedList(allParamTypes(providingType))
        wrapPathNames.reverse()

        while (!wrapPathNames.isEmpty() && !unwrapPathNames.isEmpty()
            && unwrapPathNames.last().rawType() == wrapPathNames.first().rawType()
        ) {
            unwrapPathNames.pollLast()
            wrapPathNames.pollFirst()
        }

        val wrapTypeFormat: (TypeName) -> WrapType = { it: TypeName ->
            wrapTypes[it.rawType()]
                ?: throw StoneException(message = "Type Transform non support $providingType -> $wannaType")
        }

        val unwrapPath = LinkedList(unwrapPathNames.map { wrapTypeFormat(it).copy(typeName = it) })
        val wrapPath = LinkedList(wrapPathNames.map { wrapTypeFormat(it).copy(typeName = it) })

        var currentNullable = providingType.isNullable

        while (!unwrapPath.isEmpty()) {
            val unwrapType = unwrapPath.first()
            if (unwrapType.isList) {
                val wrapListIndex = wrapPath.indexOfFirst { it.isList }
                if (wrapListIndex >= 0) {
                    val unWrapItemType = paramType(unwrapPathNames[0])
                    val wrapItemType = paramType(wrapPathNames[wrapListIndex])
                    val wrapListType = wrapPath[wrapListIndex]

                    codeBuilder = wrapListType.inListFormat!!.formatCode(
                        originalListType = unwrapType.typeName,
                        or = codeBuilder.build(),
                        itemTransformFun = { listItemCode ->
                            transform(unWrapItemType, wrapItemType, listItemCode)
                        }
                    ).toBuilder()

                    for (i in 0..wrapListIndex) {
                        wrapPath.pollFirst()
                        wrapPathNames.pollFirst()
                    }
                    unwrapPath.clear()
                    unwrapPathNames.clear()
                    break
                }
            }
            unwrapPath.pollFirst()
            unwrapPathNames.pollFirst()

            currentNullable = (
                    wrapPath.firstOrNull()?.typeName?.let { paramType(it) }
                        ?: wannaType)
                .isNullable

            codeBuilder = unwrapType.unwrap.formatCode(
                or = codeBuilder.build(),
                srcNullable = unwrapType.typeName.isNullable,
                targetNullable = currentNullable
            ).toBuilder()
        }

        while (!wrapPath.isEmpty()) {
            val wrapType = wrapPath.first()

            wrapPath.pollFirst()
            wrapPathNames.pollFirst()

            codeBuilder = wrapType.wrap.formatCode(
                codeBuilder.build(),
                srcNullable = currentNullable,
                targetNullable = wrapType.typeName.isNullable,
                targetArgTypeNullable = paramType(wrapType.typeName).isNullable,
            ).toBuilder()

            currentNullable = wrapType.typeName.isNullable
        }

        if (currentNullable && !wannaType.isNullable) {
            codeBuilder.add("!!")
        }

        return codeBuilder.build()
    }

    private fun std() {
        for (cl in listOf(
            WeakReference::class,
            SoftReference::class,
            Reference::class,
        )) {
            val wrapper = cl.asClassName()
            val creator = if (cl != Reference::class) wrapper else WeakReference::class.asClassName()

            val wrapType = WrapType(
                typeName = wrapper,
                isNoCachingWrapper = false,
                wrap = { or, srcNullable, targetNullable, argTypeNullable ->
                    codeBlock {
                        when {
                            !srcNullable -> add("%T( %L )", creator, or)
                            targetNullable -> add("%L?.let{ %T( it ) }", or, creator)
                            else -> add("%T( %L!! )", creator, or)
                        }
                    }
                },
                unwrap = { or, srcNullable, targetNullable ->
                    codeBlock {
                        when {
                            targetNullable -> add("%L?.get()", or)
                            else -> add("%L!!.get()!!", or)
                        }
                    }
                }
            )
            support(wrapType)
        }


        for (cl in listOf(
            WeakRef::class,
            SoftRef::class,
        )) {
            val creator = cl.asClassName()

            val wrapType = WrapType(
                typeName = creator,
                isNoCachingWrapper = false,
                wrap = { or, srcNullable, targetNullable, argTypeNullable ->
                    codeBlock {
                        when {
                            !srcNullable -> add("%T( %L )", creator, or)
                            targetNullable -> add("%L?.let{ %T( it ) }", or, creator)
                            else -> add("%T( %L!! )", creator, or)
                        }
                    }
                },
                unwrap = { or, srcNullable, targetNullable ->
                    codeBlock {
                        when {
                            targetNullable -> add("%L?.get()", or)
                            else -> add("%L!!.get()!!", or)
                        }
                    }
                }
            )
            support(wrapType)
        }

        for (cl in listOf(
            Lazy::class,
        )) {
            val wrapper = cl.asClassName()
            val wrapType = WrapType(
                typeName = wrapper,
                isNoCachingWrapper = false,
                wrap = { or, srcNullable, targetNullable, argTypeNullable ->
                    codeBlock {
                        when {
                            !srcNullable || argTypeNullable -> add("lazy{ %L } ", or)
                            else -> add("lazy{ %L!! } ", or)
                        }
                    }
                },
                unwrap = { or, srcNullable, targetNullable ->
                    codeBlock {
                        when {
                            targetNullable -> add("%L?.value", or)
                            !srcNullable -> add("%L.value", or)
                            else -> add("%L!!.value", or)
                        }
                    }
                },
            )
            support(wrapType)
        }

        for (cl in listOf(
            Provider::class,
            Ref::class,
            javax.inject.Provider::class,
            LazyProvider::class,
            AsyncLazy::class,
            AsyncProvider::class,
        )) {
            val isNoCachingWrapper = cl != LazyProvider::class && cl != AsyncLazy::class

            val wrapper = cl.asClassName()
            val wrapType = WrapType(
                typeName = wrapper,
                isNoCachingWrapper = isNoCachingWrapper,
                wrap = { or, srcNullable, targetNullable, argTypeNullable ->
                    codeBlock {
                        when {
                            !srcNullable || argTypeNullable -> add("%T{ %L }", wrapper, or)
                            else -> add("%T{ %L!! } ", wrapper, or)
                        }
                    }
                },
                unwrap = { or, srcNullable, targetNullable ->
                    codeBlock {
                        when {
                            targetNullable -> add("%L?.get()", or)
                            !srcNullable -> add("%L.get()", or)
                            else -> add("%L!!.get()!!", or)
                        }
                    }
                },
            )
            support(wrapType)
        }



        for (cl in listOf(
            LinkedList::class,
            ArrayList::class,
            List::class,
            Collection::class,
        )) {
            val wrapper = cl.asClassName()
            val constructor = when (cl) {
                LinkedList::class, ArrayList::class -> wrapper
                else -> null
            }

            val wrapType = WrapType(
                typeName = wrapper,
                isNoCachingWrapper = false,
                wrap = { or, srcNullable, targetNullable, argTypeNullable ->
                    codeBlock {
                        when {
                            constructor != null && targetNullable -> add("%L?.let { %T(it) }", or, constructor)
                            targetNullable -> add("%L?.let { listOfNotNull( it ) }", or)
                            constructor != null && argTypeNullable -> add("%L!!.let { %T(it) }", or, constructor)
                            constructor != null -> add("%L!!.let { %T(it!!) }", or, constructor)
                            else -> add("listOfNotNull( %L )", or)
                        }

                    }
                },
                unwrap = { or, srcNullable, targetNullable ->
                    codeBlock {
                        when {
                            targetNullable -> add("%L?.firstOrNull()", or)
                            !srcNullable -> add("%L.first()", or)
                            else -> add("%L!!.first()", or)
                        }
                    }
                },
                inListFormat = { originalListType, originalListCode, itemTransformFun ->
                    codeBlock {
                        val itemTransform = itemTransformFun.formatCode(CodeBlock.of("it"))
                        if (itemTransform.toString() == "it") {
                            //no transforms
                            add(originalListCode)
                        } else {
                            add("( %L?.map{ it -> %L } ?: emptyList() )", originalListCode, itemTransform)
                        }

                        if (wrapper.rawType() != originalListType.rawType()) {
                            if (constructor != null) {
                                add("!!.let { %T(it) }", constructor)
                            } else {
                                add("!!.toList()")
                            }
                        }
                    }
                },
            )

            support(wrapType)
        }
    }
}