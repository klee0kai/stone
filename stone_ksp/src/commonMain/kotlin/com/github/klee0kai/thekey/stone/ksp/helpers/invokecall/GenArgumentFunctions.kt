package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall

import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName

object GenArgumentFunctions {
    /**
     * Simple unwrap argument from wrapped type (WeakReference and other).
     * If you have arguments like <br></br>
     * `Provide<SomeClass> someVariable;`  <br></br>
     * unwrapArgument support to unwrap SomeClass type on invoke method, like <br></br>
     * `doSmth(someVariable.provide()) `
     *
     * @param envFields available fields
     * @return unwrapped field get code, or null
     */
    fun unwrapArgument(
        envFields: List<KSValueParameter>,
    ): (TypeName) -> CodeBlock {
        envFields.map {
            it.type.resolve().arguments


        }
        TODO()
//        val provideFields: MutableList<Pair<TypeName?, String?>> = ListUtils.format(envFields, { it ->
//            if (it.type is ParameterizedTypeName) {
//                val type: ParameterizedTypeName = it.type as ParameterizedTypeName
//                val orType: TypeName? = type.typeArguments.get(type.typeArguments.size() - 1)
//                if (type.rawType == ClassName.get(PhantomProvide::class.java)) return@format Pair(
//                    orType,
//                    it.name + ".get()"
//                )
//            }
//            Pair(it.type, it.name)
//        })
//
//        return Function { wannaType: TypeName? ->
//            for (provideField in provideFields) {
//                if (wannaType == provideField.first) return@Function CodeBlock.of(provideField.second)
//            }
//            null
//        }
    }
}
