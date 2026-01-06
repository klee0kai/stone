package com.github.klee0kai.thekey.stone.ksp.helpers.invokecall

import com.github.klee0kai.stone.__hidden__.provide.ProvideBuilder
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model.FieldDetail
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model.MethodDetail
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model.QualifierAnn
import com.github.klee0kai.thekey.stone.ksp.helpers.invokecall.model.anyIgnoreQualifier
import com.github.klee0kai.thekey.stone.ksp.helpers.wrap.WrapHelper
import com.github.klee0kai.thekey.stone.ksp.poet.codeBlock
import com.github.klee0kai.thekey.stone.ksp.utils.LocalFieldName
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

    companion object;

    fun bestSequence(): List<MethodDetail> = invokeSequenceVariants[0]

    fun qualifierAnnotations(
        crossing: Boolean,
    ): Set<QualifierAnn> {
        val allQualifiersLists = LinkedList<MutableSet<QualifierAnn>>()
        for (variant in invokeSequenceVariants) {
            val qualifiers = HashSet<QualifierAnn>()
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
                    m.args.map { arg ->
                        ProvideDep(
                            methodName = null,
                            typeName = wrapHelper.listWrapTypeIfNeed(arg.type),
                            qualifierAnns = arg.qualifierAnns.toSet(),
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

    fun best() = InvokeCall.fromSequence(wrapHelper, bestSequence())

    /**
     * Generate invoke code bloke
     *
     * @param envFields predefined arguments in generated code
     * @param argGen    argument generator, if non found in envFields
     * @return new code block without semicolon
     */
    fun invokeCode(
        envFields: List<FieldDetail>,
        argGen: (FieldDetail) -> CodeBlock? = { null },
    ): CodeBlock {
        val argGens = LinkedList<(FieldDetail) -> CodeBlock?>()
        argGens.add(unwrapArgument(envFields))
        argGens.add(argGen)

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
            if (m.isProperty) {
                invokeBuilder.add("%L", m.methodName)
            } else {
                invokeBuilder.add("%L(%L)", m.methodName, argsCodeBuilder.build())
            }
        }

        return invokeBuilder.build()
    }

    fun invokeAllToList(
        declaredFields: List<FieldDetail>,
    ): CodeBlock = codeBlock {
        val provType = List::class.asClassName().parameterizedBy(resultType())
        val listFieldName: String = LocalFieldName.genLocalFieldName()
        add(
            "%T{ %L -> \n",
            ProvideBuilder::class.asClassName().parameterizedBy(resultType()),
            listFieldName,
        )
        for (sequence in invokeSequenceVariants) {
            val invokeCall = InvokeCall.fromSequence(wrapHelper, sequence)
            val seqCodeBlock = invokeCall.invokeCode(declaredFields)

            if (wrapHelper.isList(invokeCall.rawReturnType())) {
                add(
                    "%L.addAll( %L );\n",
                    listFieldName,
                    wrapHelper.transform(
                        invokeCall.rawReturnType(),
                        provType,
                        seqCodeBlock
                    )
                )
            } else {
                add(
                    "%L.add( %L );\n",
                    listFieldName,
                    wrapHelper.transform(
                        invokeCall.rawReturnType(),
                        resultType().copy(nullable = true),
                        seqCodeBlock
                    )
                )
            }
        }

        add(" }.all();\n")
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
                wrapHelper.isList(f.type)
                        && (arg.qualifierAnns.anyIgnoreQualifier() || arg.qualifierAnns == f.qualifierAnns)
            } else null

            if (field == null) {
                //non list
                field = typeFields.firstOrNull { f ->
                    (arg.qualifierAnns.anyIgnoreQualifier() || arg.qualifierAnns == f.qualifierAnns)
                }
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


fun InvokeCall.Companion.fromSequence(
    wrapHelper: WrapHelper,
    callSequence: List<MethodDetail>,
    flags: InvokeProvideFlags = InvokeProvideFlags(),
) = InvokeCall(
    wrapHelper = wrapHelper,
    invokeSequenceVariants = listOf(callSequence),
    flags = flags,
)


fun InvokeCall.Companion.fromVariants(
    wrapHelper: WrapHelper,
    variants: List<InvokeCall>,
) = InvokeCall(
    wrapHelper = wrapHelper,
    invokeSequenceVariants = variants.flatMap { it.invokeSequenceVariants },
    flags = variants.fold(InvokeProvideFlags()) { acc, value -> acc.merge(value.flags) }
)