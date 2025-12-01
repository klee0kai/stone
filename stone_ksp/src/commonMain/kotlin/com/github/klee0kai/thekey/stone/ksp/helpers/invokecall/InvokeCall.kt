package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall

import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.GenArgumentFunctions.unwrapArgument
import com.github.klee0kai.thekey.stone.ksp.helpers.qualifierAnnotations
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.ClassNameUtils
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.SmartCode
import com.github.klee0kai.thekey.stone.ksp.poet.smartcode.smartCode
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName
import java.util.*

/**
 * Invoke sequence or call sequence.
 * Like someMethod1(arg1,arg2).someMethod2(arg3).someMethod3()
 *
 *
 * Describe method names and using arguments for code generation.
 * Arguments for chaining are reused by type.
 */
class InvokeCall(
    val invokeSequenceVariants: List<List<KSFunctionDeclaration>>,
    val flags: InvokeProvideFlags = InvokeProvideFlags(),
) {

    /**
     * Create new invoke sequence
     *
     * @param callSequence ordered methods in invoke sequence
     */
    constructor(
        vararg callSequence: KSFunctionDeclaration,
        flags: InvokeProvideFlags = InvokeProvideFlags()
    ) : this(
        invokeSequenceVariants = listOf(callSequence.toList()),
        flags = flags,
    )

    /**
     * Merge variants. All should return same type
     *
     * @param variants all variants from best to worse
     */
    constructor(variants: MutableCollection<InvokeCall>) : this(
        invokeSequenceVariants = variants.flatMap { it.invokeSequenceVariants },
        flags = variants.fold(InvokeProvideFlags()) { acc, value -> acc.merge(value.flags) }
    )

    fun bestSequence(): List<KSFunctionDeclaration> = invokeSequenceVariants[0]


    fun qualifierAnnotations(
        crossing: Boolean,
    ): Set<KSAnnotation> {
        val allQualifiersLists = LinkedList<MutableSet<KSAnnotation>>()
        for (variant in invokeSequenceVariants) {
            val qualifiers = HashSet<KSAnnotation>()
            for (m in variant) qualifiers.addAll(m.qualifierAnnotations)
            allQualifiersLists.add(qualifiers)
        }
        if (allQualifiersLists.isEmpty()) return mutableSetOf()

        val allQualifiers = allQualifiersLists[0]
        if (crossing) {
            for (q in allQualifiersLists) allQualifiers.retainAll(q)
        } else {
            for (q in allQualifiersLists) allQualifiers.addAll(q)
        }
        return allQualifiers
    }

    /**
     * Using arguments in invoke sequence
     *
     * @return collection of all argument's types
     */
    fun argDeps(): Set<ProvideDep> {
        val argsTypes = HashSet<ProvideDep>()
        for (invokeSequence in invokeSequenceVariants) {
            for (m in invokeSequence) {
                argsTypes.addAll(
                    m.parameters.map {
                        ProvideDep(
                            type = it.type.resolve(),
                            qualifiers = m.qualifierAnnotations.toList(),
                        )
                    })
            }
        }
        return argsTypes
    }

    /**
     * Providing type in this invoke sequence
     *
     * @return return type
     */
    fun resultType(): TypeName {
        return ClassNameUtils.rawTypeOf(rawReturnType())
    }

    fun rawReturnType(): TypeName {
        val invokeSequence = bestSequence()
        return invokeSequence[invokeSequence.size - 1].returnType?.resolve()!!.toClassName()
    }

    /**
     * Generate invoke code bloke
     *
     * @param envFields predefined arguments in generated code
     * @param argGen    argument generator, if non found in envFields
     * @return new code block without semicolon
     */
    fun invokeCode(
        envFields: List<KSValueParameter>,
        vararg argGen: (TypeName) -> CodeBlock,
    ): CodeBlock {
        val argGens = LinkedList<(TypeName) -> CodeBlock>()
        argGens.add(unwrapArgument(envFields))
        argGens.addAll(argGen)

        val invokeBuilder = CodeBlock.builder()
        var invokeCount = 0
        for (m in bestSequence()) {
            var argCount = 0
            val argsCodeBuilder: CodeBlock.Builder = CodeBlock.builder()
            for (arg in m.parameters) {
                if (argCount++ > 0) argsCodeBuilder.add(",")
                val argCode = argGens.firstNotNullOf { it.invoke(arg.type.resolve().toClassName()) }
                argsCodeBuilder.add(argCode)
            }

            if (invokeCount++ > 0) invokeBuilder.add(".")
            invokeBuilder.add("%L(%L)", m.simpleName.asString(), argsCodeBuilder.build())
        }
        return invokeBuilder.build()
    }

    fun invokeBest(): SmartCode = smartCode {
        providingType.value = resultType()
// TODO        transform(invokeSequence(bestSequence()), resultType())
    }

//        return SmartCode
//            .builder()
//            .providingType(resultType())
//            .withLocals({ builder -})
//    }

    fun invokeAllToList() = smartCode {
//        TODO
//        val provType: TypeName? = ParameterizedTypeName.get(ClassName.get(MutableList::class.java), resultType())
//        return SmartCode
//            .builder()
//            .providingType(provType)
//            .withLocals({ builder ->
//                val listFieldName: String? = genLocalFieldName()
//                builder.add(
//                    CodeBlock.of(
//                        "new \$T( ( \$L ) -> { \n",
//                        ParameterizedTypeName.get(ClassName.get(ProvideBuilder::class.java), resultType()),
//                        listFieldName
//                    )
//                )
//                for (sequence in invokeSequenceVariants) {
//                    val seqCode: SmartCode = invokeSequence(sequence)
//                    if (WrapHelper.isList(seqCode.providingType)) {
//                        builder.add(listFieldName)
//                            .add(".addAll(")
//                            .add(transform(seqCode, provType))
//                            .add(");\n")
//                    } else {
//                        builder.add(listFieldName)
//                            .add(".add(")
//                            .add(transform(seqCode, resultType()))
//                            .add(");\n")
//                    }
//                }
//                builder.add(" }).all() ")
//                builder
//            })
    }


    private fun invokeSequence(
        sequence: List<KSFunctionDeclaration>,
    ): SmartCode {
        TODO()
//        return SmartCode.builder().withLocals({ builder ->
//            var invokeCount = 0
//            for (m in sequence) {
//                if (invokeCount++ > 0) builder.add(".")
//                builder.add(m.methodName)
//                    .add("(")
//
//                var argCount = 0
//                for (arg in m.args) {
//                    if (argCount++ > 0) builder.add(", ")
//                    val isList: Boolean = isList(arg.type)
//                    val typeFields: MutableList<FieldDetail?>? = ListUtils.filter(
//                        builder.getDeclaredFields(),
//                        { i, f -> nonWrappedType(f.type) == nonWrappedType(arg.type) })
//
//                    var field: FieldDetail? = if (isList) ListUtils.first(
//                        typeFields,
//                        { i, f -> isList(f.type) && f.qualifierAnns == arg.qualifierAnns }
//                    ) else null
//                    if (field == null) {
//                        //non list
//                        field = ListUtils.first(typeFields, { i, f -> f.qualifierAnns == arg.qualifierAnns })
//                    }
//
//                    if (field == null) {
//                        builder.add("null", null)
//                    } else {
//                        // unwrap type
//                        builder.add(
//                            transform(
//                                SmartCode.of(field.name, mutableSetOf<T?>(field.name))
//                                    .providingType(field.type),
//                                arg.type
//                            )
//                        )
//                    }
//                }
//
//                builder.add(")")
//            }
//            builder
//        })
//            .providingType(sequence.get(sequence.size - 1).returnType)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        if (invokeSequenceVariants.size <= 1) for (qualifierAnn in qualifierAnnotations(false)) {
            builder.append(qualifierAnn.toString())
                .append("    ")
        }
        var variantIndx = 0
        for (variant in invokeSequenceVariants) {
            if (variantIndx++ > 0) builder.append(";\n")
            var secIndx = 0
            for (m in variant) {
                if (secIndx++ > 0) builder.append(".")
                builder.append(m.simpleName.asString())
                    .append("(")
                    .append(m.parameters.joinToString(", ") { it.type.resolve().toClassName().simpleName })
                    .append(")")
            }
        }
        return builder.toString()
    }


    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as InvokeCall
        return invokeSequenceVariants == that.invokeSequenceVariants
    }

    override fun hashCode(): Int {
        return Objects.hash(invokeSequenceVariants)
    }
}
