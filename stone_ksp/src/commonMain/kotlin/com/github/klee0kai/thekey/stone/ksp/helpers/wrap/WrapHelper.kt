package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.wrappers.AsyncCoroutineProvide
import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.stone.wrappers.PhantomProvide
import com.github.klee0kai.thekey.stone.ksp.exceptions.StoneException
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.ClassNameUtils.noWildCardType
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.ClassNameUtils.rawTypeOf
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.lang.ref.Reference
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Provider


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

    fun isAsyncProvider(typeName: TypeName): Boolean {
        for (t in allParamTypes(typeName)) {
            val wrapType = wrapTypes.get(rawTypeOf(t))
            if (wrapType != null && wrapType.isAsyncProvider) return true
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
        return typeName
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
        val wrapPathNames = LinkedList<TypeName>(allParamTypes(wannaType))
        val unwrapPathNames = LinkedList<TypeName>(allParamTypes(providingType))
        wrapPathNames.reverse()

        while (!wrapPathNames.isEmpty() && !unwrapPathNames.isEmpty()
            && rawTypeOf(unwrapPathNames.last()) == rawTypeOf(wrapPathNames.first())
        ) {
            unwrapPathNames.pollLast()
            wrapPathNames.pollFirst()
        }

        val wrapTypeFormat: (TypeName) -> WrapType = { it: TypeName ->
            wrapTypes[rawTypeOf(it)]
                ?: throw StoneException(message = "Type Transform non support $providingType -> $wannaType")
        }

        val unwrapPath = LinkedList(unwrapPathNames.map(wrapTypeFormat))
        val wrapPath = LinkedList(wrapPathNames.map(wrapTypeFormat))

        while (!unwrapPath.isEmpty()) {
            val unwrapType = unwrapPath[0]
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
            codeBuilder = unwrapType.unwrap.formatCode(codeBuilder.build())
                .toBuilder()

            unwrapPath.pollFirst()
            unwrapPathNames.pollFirst()
        }

        while (!wrapPath.isEmpty()) {
            codeBuilder = wrapPath[0].wrap.formatCode(codeBuilder.build())
                .toBuilder()

            wrapPath.pollFirst()
            wrapPathNames.pollFirst()
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
            val creator = if (cl != Reference::class.java) wrapper else WeakReference::class.asClassName()

            val wrapType = WrapType(
                typeName = wrapper,
                isNoCachingWrapper = false,
                wrap = { or ->
                    CodeBlock.builder()
                        .add("%L?.let{ %T(it) }", or, creator)
                        .build()
                },
                unwrap = { or ->
                    CodeBlock.builder()
                        .add("%L?.get()", or)
                        .build()
                }
            )
            support(wrapType)
        }

        for (cl in listOf(
            PhantomProvide::class,
            Ref::class,
            Provider::class,
            LazyProvide::class,
            AsyncCoroutineProvide::class
        )) {
            val wrapper = cl.asClassName()
            val isNoCachingWrapper = cl != LazyProvide::class.java && cl != AsyncCoroutineProvide::class
            val wrapType = WrapType(
                typeName = wrapper,
                isNoCachingWrapper = isNoCachingWrapper,
                isAsyncProvider = true,
                wrap = { or ->
                    CodeBlock.of(
                        "%T{ %L } ",
                        if (isNoCachingWrapper) PhantomProvide::class.asClassName() else wrapper,
                        or,
                    )
                },
                unwrap = { or -> CodeBlock.of("%L?.get()", or) },
            )
            support(wrapType)
        }

        var index = 0
        for (cl in listOf(LinkedList::class, ArrayList::class, MutableList::class, MutableCollection::class)) {
            val wrapper = cl.asClassName()
            val needConstructor = listOf(LinkedList::class, ArrayList::class).contains(cl)
            val createType = if (index++ <= 0) wrapper else ArrayList::class.asClassName()

            val wrapType = WrapType(
                typeName = wrapper,
                wrap = { or ->
                    val builder = CodeBlock.builder()
                    builder.add("listOfNotNull( %L ) ", or)
                    if (needConstructor) builder.add(".let { %T(it) }", createType)
                    builder.build()
                },
                unwrap = { or ->
                    CodeBlock.builder()
                        .add("%L.first( %L )", or)
                        .build()
                },
                inListFormat = { originalListType, originalListCode, itemTransformFun ->
                    val builder = CodeBlock.builder()
                    val isListNeedConstructor =
                        needConstructor && rawTypeOf(wrapper) != rawTypeOf(originalListType)

                    val itemTransform = itemTransformFun.formatCode(CodeBlock.of("it"))
                    if (itemTransform.toString() == "it") {
                        //no transforms
                        builder.add(originalListCode)
                    } else {
                        builder.add("%L.map{ it -> %L }", originalListCode, itemTransform)
                    }

                    if (isListNeedConstructor) builder.add(".let { %T(it) }", createType)
                    builder.build()
                },
            )

            support(wrapType)
        }
    }
}