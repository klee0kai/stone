package com.github.klee0kai.stone.helpers.invokecall;

import com.github.klee0kai.stone._hidden_.provide.ProvideBuilder;
import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.helpers.wrap.WrapHelper;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.model.annotations.QualifierAnn;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.*;
import java.util.function.Function;

import static com.github.klee0kai.stone.helpers.invokecall.GenArgumentFunctions.unwrapArgument;
import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.nonWrappedType;
import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.transform;
import static com.github.klee0kai.stone.utils.LocalFieldName.genLocalFieldName;

/**
 * Invoke sequence or call sequence.
 * Like someMethod1(arg1,arg2).someMethod2(arg3).someMethod3()
 * <p>
 * Describe method names and using arguments for code generation.
 * Arguments for chaining are reused by type.
 */
public class InvokeCall {

    /**
     * Invoke sequence provides the object, which caching in DI.
     */
    public static final int INVOKE_PROVIDE_OBJECT_CACHED = 0x1;
    /**
     * ignore recursive providing for bind instances
     */
    public static final int INVOKE_PROVIDE_BIND_INSTANCE = 0x2;

    public final List<List<MethodDetail>> invokeSequenceVariants = new LinkedList<>();
    public final int flags;

    /**
     * Create new invoke sequence
     *
     * @param callSequence ordered methods in invoke sequence
     */
    public InvokeCall(
            MethodDetail... callSequence
    ) {
        this.flags = 0;
        this.invokeSequenceVariants.add(Arrays.asList(callSequence));
    }

    public InvokeCall(
            List<MethodDetail> callSequence
    ) {
        this.flags = 0;
        this.invokeSequenceVariants.add(callSequence);
    }

    /**
     * Create new invoke sequence
     *
     * @param flags        available flags:
     *                     {@code INVOKE_PROVIDE_OBJECT_CACHED} providing cached object
     * @param callSequence ordered methods in invoke sequence
     */
    public InvokeCall(
            int flags,
            MethodDetail... callSequence
    ) {
        this.flags = flags;
        this.invokeSequenceVariants.add(Arrays.asList(callSequence));
    }

    /**
     * Merge variants. All should return same type
     *
     * @param variants all variants from best to worse
     */
    public InvokeCall(
            Collection<InvokeCall> variants
    ) {
        int mergeflag = 0;
        for (InvokeCall v : variants) {
            mergeflag |= v.flags;
            this.invokeSequenceVariants.addAll(v.invokeSequenceVariants);
        }
        this.flags = mergeflag;
    }


    public List<MethodDetail> bestSequence() {
        return invokeSequenceVariants.get(0);
    }

    public Set<QualifierAnn> qualifierAnnotations(
            boolean crossing
    ) {
        List<Set<QualifierAnn>> allQualifiersLists = new LinkedList<>(new HashSet<>());
        for (List<MethodDetail> variant : invokeSequenceVariants) {
            Set<QualifierAnn> qualifiers = new HashSet<>();
            for (MethodDetail m : variant) qualifiers.addAll(m.qualifierAnns);
            allQualifiersLists.add(qualifiers);
        }
        if (allQualifiersLists.isEmpty()) return Collections.emptySet();

        Set<QualifierAnn> allQualifiers = allQualifiersLists.get(0);
        if (crossing) {
            for (Set<QualifierAnn> q : allQualifiersLists) allQualifiers.retainAll(q);
        } else {
            for (Set<QualifierAnn> q : allQualifiersLists) allQualifiers.addAll(q);
        }
        return allQualifiers;
    }

    /**
     * Using arguments in invoke sequence
     *
     * @return collection of all argument's types
     */
    public Set<ProvideDep> argDeps() {
        Set<ProvideDep> argsTypes = new HashSet<>();
        for (List<MethodDetail> invokeSequence : invokeSequenceVariants)
            for (MethodDetail m : invokeSequence) {
                List<ProvideDep> types = ListUtils.format(m.args, (it) -> new ProvideDep(it.type, it.qualifierAnns));
                argsTypes.addAll(types);
            }
        return argsTypes;
    }

    /**
     * Providing type in this invoke sequence
     *
     * @return return type
     */
    public TypeName resultType() {
        return nonWrappedType(rawReturnType());
    }

    public TypeName rawReturnType() {
        List<MethodDetail> invokeSequence = bestSequence();
        return invokeSequence.get(invokeSequence.size() - 1).returnType;
    }

    public TypeName listResultType() {
        return ParameterizedTypeName.get(ClassName.get(List.class), resultType());
    }

    /**
     * Generate invoke code bloke
     *
     * @param envFields predefined arguments in generated code
     * @param argGen    argument generator, if non found in envFields
     * @return new code block without semicolon
     */
    @SafeVarargs
    public final CodeBlock invokeCode(
            Collection<FieldDetail> envFields,
            Function<FieldDetail, CodeBlock>... argGen
    ) {
        List<Function<FieldDetail, CodeBlock>> argGens = new LinkedList<>();
        argGens.add(unwrapArgument(envFields));
        argGens.addAll(Arrays.asList(argGen));

        CodeBlock.Builder invokeBuilder = CodeBlock.builder();
        int invokeCount = 0;
        for (MethodDetail m : bestSequence()) {
            int argCount = 0;
            CodeBlock.Builder argsCodeBuilder = CodeBlock.builder();
            for (FieldDetail arg : m.args) {
                if (argCount++ > 0) argsCodeBuilder.add(",");
                CodeBlock argCode = ListUtils.firstNotNull(argGens, it -> it.apply(arg));
                argsCodeBuilder.add(argCode != null ? argCode : CodeBlock.of("null"));
            }

            if (invokeCount++ > 0) invokeBuilder.add(".");
            invokeBuilder.add("$L($L)", m.methodName, argsCodeBuilder.build());
        }

        return invokeBuilder.build();
    }

    public InvokeCall best() {
        return new InvokeCall(bestSequence());
    }

    public CodeBlock invokeAllToList(
            Collection<FieldDetail> declaredFields
    ) {
        TypeName provType = ParameterizedTypeName.get(ClassName.get(List.class), resultType());
        String listFieldName = genLocalFieldName();
        CodeBlock.Builder builder = CodeBlock.builder();

        builder.add(CodeBlock.of("new $T( ( $L ) -> { \n",
                ParameterizedTypeName.get(ClassName.get(ProvideBuilder.class), resultType()), listFieldName
        ));
        for (List<MethodDetail> sequence : invokeSequenceVariants) {
            InvokeCall invokeCall = new InvokeCall(sequence);
            CodeBlock seqCodeBlock = invokeCall.invokeCode(declaredFields);

            if (WrapHelper.isList(invokeCall.rawReturnType())) {
                builder.add(listFieldName)
                        .add(".addAll(")
                        .add(transform(invokeCall.rawReturnType(), provType, seqCodeBlock))
                        .add(");\n");
            } else {
                builder.add(listFieldName)
                        .add(".add(")
                        .add(transform(invokeCall.rawReturnType(), resultType(), seqCodeBlock))
                        .add(");\n");
            }
        }
        builder.add(" }).all() ");
        return builder.build();
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (invokeSequenceVariants.size() <= 1)
            for (QualifierAnn qualifierAnn : qualifierAnnotations(false)) {
                builder.append(qualifierAnn.toString())
                        .append("    ");
            }
        int variantIndx = 0;
        for (List<MethodDetail> variant : invokeSequenceVariants) {
            if (variantIndx++ > 0) builder.append(";\n");
            int secIndx = 0;
            for (MethodDetail m : variant) {
                if (secIndx++ > 0) builder.append(".");
                builder.append(m.methodName)
                        .append("(")
                        .append(String.join(",", ListUtils.format(m.args, f -> f.type.toString())))
                        .append(")");
            }
        }
        return builder.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvokeCall that = (InvokeCall) o;
        return Objects.equals(invokeSequenceVariants, that.invokeSequenceVariants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invokeSequenceVariants);
    }
}
