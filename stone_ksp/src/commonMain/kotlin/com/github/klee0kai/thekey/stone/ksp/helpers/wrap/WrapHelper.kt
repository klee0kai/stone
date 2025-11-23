package com.github.klee0kai.thekey.stone.ksp.helpers.wrap

import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.wrappers.AsyncCoroutineProvide
import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.stone.wrappers.PhantomProvide
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import java.lang.ref.Reference
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import javax.inject.Provider

class WrapHelper {

    var wrapTypes = HashMap<TypeName, WrapType>()

    init {
        fillStdWrappers()
    }

    private fun fillStdWrappers() {
        for (cl in listOf(
            WeakReference::class,
            SoftReference::class,
            Reference::class
        )) {
            val wrapper = cl.asClassName()
            val creator = if (cl != Reference::class) wrapper else WeakReference::class.asClassName()

            val wrapType = WrapType(
                isNoCachingWrapper = false,
                typeName = wrapper,
                wrap = FormatSimple { or ->
                    providingType = or.providingType?.let {
                        wrapper.parameterizedBy(it)
                    }
                    TODO()
                },
                unwrap = FormatSimple { or ->
//                    providingType = or.providingType?.let {
//
//                    }
                    TODO()
                },
            )
            wrapTypes.putIfAbsent(wrapType.typeName, wrapType)

        }

        for (cl in listOf(
            PhantomProvide::class,
            Ref::class,
            Provider::class,
            LazyProvide::class,
            AsyncCoroutineProvide::class,
        )) {
            val wrapper = cl.asClassName()

            val wrapType = WrapType(
                typeName = wrapper,
                isNoCachingWrapper = cl != LazyProvide::class.java && cl != AsyncCoroutineProvide::class.java,
                isAsyncProvider = true,
                wrap = FormatSimple { or ->
                    TODO()
                },
                unwrap = FormatSimple { or ->
                    TODO()
                }
            )

            wrapTypes.putIfAbsent(wrapType.typeName, wrapType)
        }

        for (cl in listOf(
            List::class,
            Array::class,
            MutableList::class,
            MutableCollection::class
        )) {

            val wrapper = cl.asClassName()

            val wrapType = WrapType(
                typeName = wrapper,
                wrap = FormatSimple { or ->
                    TODO()
                },
                unwrap = FormatSimple { or ->
                    TODO()
                },
                inListFormat = FormatInList { or ->
                    TODO()
                }
            )


        }

    }
}