package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall

import com.github.klee0kai.stone.__hidden__.provide.ProvideBuilder
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model.FieldDetail
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model.MethodDetail
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapHelper
import com.github.klee0kai.thekey.stone.ksp.utils.LocalFieldName
import com.google.devtools.ksp.symbol.KSAnnotation
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
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
    var wrapHelper: WrapHelper,
    val invokeSequenceVariants: List<List<MethodDetail>>,
    val flags: InvokeProvideFlags = InvokeProvideFlags(),
) {

    /**
     * Create new invoke sequence
     *
     * @param callSequence ordered methods in invoke sequence
     */
    constructor(
        wrapHelper: WrapHelper,
        callSequence: List<MethodDetail>,
        flags: InvokeProvideFlags = InvokeProvideFlags()
    ) : this(
        wrapHelper = wrapHelper,
        invokeSequenceVariants = listOf(callSequence),
        flags = flags,
    )

    /**
     * Merge variants. All should return same type
     *
     * @param variants all variants from best to worse
     */
    constructor(
        wrapHelper: WrapHelper,
        variants: List<InvokeCall>,
    ) : this(
        wrapHelper = wrapHelper,
        invokeSequenceVariants = variants.flatMap { it.invokeSequenceVariants },
        flags = variants.fold(InvokeProvideFlags()) { acc, value -> acc.merge(value.flags) }
    )

    fun bestSequence(): List<MethodDetail> = invokeSequenceVariants[0]

    fun qualifierAnnotations(
        crossing: Boolean,
    ): Set<KSAnnotation> {
        val allQualifiersLists = LinkedList<MutableSet<KSAnnotation>>()
        for (variant in invokeSequenceVariants) {
            val qualifiers = HashSet<KSAnnotation>()
            for (m in variant) qualifiers.addAll(m.qualifierAnns)
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
                    m.args.map {
                        ProvideDep(
                            methodName = null,
                            typeName = wrapHelper.listWrapTypeIfNeed(it.type),
                            qualifierAnns = m.qualifierAnns.toSet(),
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
    fun resultType(): TypeName = wrapHelper.nonWrappedType(rawReturnType())

    fun rawReturnType(): TypeName {
        val invokeSequence = bestSequence()
        return invokeSequence[invokeSequence.size - 1].returnType
    }

    fun best() = InvokeCall(wrapHelper, bestSequence())

    /**
     * Generate invoke code bloke
     *
     * @param envFields predefined arguments in generated code
     * @param argGen    argument generator, if non found in envFields
     * @return new code block without semicolon
     */
    fun invokeCode(
        envFields: List<FieldDetail>,
        vararg argGen: (FieldDetail) -> CodeBlock?,
    ): CodeBlock {
        val argGens = LinkedList<(FieldDetail) -> CodeBlock?>()
        argGens.add(unwrapArgument(envFields))
        argGens.addAll(argGen)

        val invokeBuilder = CodeBlock.builder()
        var invokeCount = 0
        for (m in bestSequence()) {
            var argCount = 0
            val argsCodeBuilder = CodeBlock.builder()
            for (arg in m.args) {
                if (argCount++ > 0) argsCodeBuilder.add(",")
                val argCode = argGens.firstNotNullOfOrNull { it.invoke(arg) }
                argsCodeBuilder.add(argCode ?: CodeBlock.of("null"))
            }

            if (invokeCount++ > 0) invokeBuilder.add(".")
            invokeBuilder.add("%L(%L)", m.methodName, argsCodeBuilder.build())
        }

        return invokeBuilder.build()
    }

    fun invokeAllToList(
        declaredFields: List<FieldDetail>,
    ): CodeBlock {
        val provType = List::class.asClassName().parameterizedBy(resultType())
        val listFieldName: String = LocalFieldName.genLocalFieldName()

        val builder = CodeBlock.builder()

        builder.add(
            "%T{ %L -> \n",
            ProvideBuilder::class.asClassName().parameterizedBy(resultType()),
            listFieldName,
        )

        builder.add(CodeBlock.of("buildList<%T>{  \n", resultType()))
        for (sequence in invokeSequenceVariants) {
            val invokeCall = InvokeCall(wrapHelper, sequence)
            val seqCodeBlock = invokeCall.invokeCode(declaredFields)

            if (wrapHelper.isList(invokeCall.rawReturnType())) {
                builder
                    .add(listFieldName)
                    .add(".addAll(")
                    .add(
                        wrapHelper.transform(
                            invokeCall.rawReturnType(),
                            provType,
                            seqCodeBlock
                        )
                    )
                    .add(");\n")
            } else {
                builder
                    .add(listFieldName)
                    .add(".add(")
                    .add(
                        wrapHelper.transform(
                            invokeCall.rawReturnType(),
                            resultType(),
                            seqCodeBlock
                        )
                    )
                    .add(");\n")
            }
        }
        builder.add(" }.all() ")
        return builder.build()
    }


    private fun unwrapArgument(
        envFields: List<FieldDetail>,
    ): (FieldDetail) -> CodeBlock? {
        return { arg ->
            val isWannaList = wrapHelper.isList(arg.type)
            val typeFields = envFields.filter { f ->
                wrapHelper.nonWrappedType(f.type) == wrapHelper.nonWrappedType(arg.type)
            }
            var field = if (isWannaList) typeFields.firstOrNull { f ->
                wrapHelper.isList(f.type) && f.qualifierAnns == arg.qualifierAnns
            } else null

            if (field == null) {
                //non list
                field = typeFields.firstOrNull { f -> f.qualifierAnns == arg.qualifierAnns }
            }

            if (field != null) {
                wrapHelper.transform(field.type, arg.type, CodeBlock.of(field.name))
            } else {
                null
            }
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        if (invokeSequenceVariants.size <= 1) {
            for (qualifierAnn in qualifierAnnotations(false)) {
                builder.append(qualifierAnn.toString())
                    .append("    ")
            }
        }
        var variantIndx = 0
        for (variant in invokeSequenceVariants) {
            if (variantIndx++ > 0) builder.append(";\n")
            var secIndx = 0
            for (m in variant) {
                if (secIndx++ > 0) builder.append(".")
                builder.append(m.methodName)
                    .append("(")
                    .append(m.args.joinToString(", ") { it.type.toString() })
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
