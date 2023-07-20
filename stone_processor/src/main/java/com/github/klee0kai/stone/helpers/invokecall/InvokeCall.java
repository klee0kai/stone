package com.github.klee0kai.stone.helpers.invokecall;

import com.github.klee0kai.stone.closed.provide.ProvideBuilder;
import com.github.klee0kai.stone.closed.types.StListUtils;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
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
import static com.github.klee0kai.stone.helpers.wrap.WrapHelper.*;
import static com.github.klee0kai.stone.utils.LocalFieldName.genLocalFieldName;
import static java.util.Collections.singleton;

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

    public final List<List<MethodDetail>> invokeSequenceVariants = new LinkedList<>();
    public final int flags;

    /**
     * Create new invoke sequence
     *
     * @param callSequence ordered methods in invoke sequence
     */
    public InvokeCall(MethodDetail... callSequence) {
        this.flags = 0;
        this.invokeSequenceVariants.add(Arrays.asList(callSequence));
    }

    /**
     * Create new invoke sequence
     *
     * @param flags        available flags:
     *                     {@code INVOKE_PROVIDE_OBJECT_CACHED} providing cached object
     * @param callSequence ordered methods in invoke sequence
     */
    public InvokeCall(int flags, MethodDetail... callSequence) {
        this.flags = flags;
        this.invokeSequenceVariants.add(Arrays.asList(callSequence));
    }

    /**
     * Merge variants. All should return same type
     *
     * @param variants all variants from best to worse
     */
    public InvokeCall(Collection<InvokeCall> variants) {
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

    public Set<QualifierAnn> qualifierAnnotations(boolean crossing) {
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
                List<ProvideDep> types = StListUtils.format(m.args, (it) -> new ProvideDep(it.type, it.qualifierAnns));
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

    /**
     * Generate invoke code bloke
     *
     * @param envFields predefined arguments in generated code
     * @param argGen    argument generator, if non found in envFields
     * @return new code block without semicolon
     */
    @SafeVarargs
    public final CodeBlock invokeCode(
            List<FieldDetail> envFields,
            Function<TypeName, CodeBlock>... argGen
    ) {
        List<Function<TypeName, CodeBlock>> argGens = new LinkedList<>();
        argGens.add(unwrapArgument(envFields));
        argGens.addAll(Arrays.asList(argGen));

        CodeBlock.Builder invokeBuilder = CodeBlock.builder();
        int invokeCount = 0;
        for (MethodDetail m : bestSequence()) {
            int argCount = 0;
            CodeBlock.Builder argsCodeBuilder = CodeBlock.builder();
            for (FieldDetail arg : m.args) {
                if (argCount++ > 0) argsCodeBuilder.add(",");
                CodeBlock argCode = StListUtils.firstNotNull(argGens, it -> it.apply(arg.type));
                argsCodeBuilder.add(argCode != null ? argCode : CodeBlock.of("null"));
            }

            if (invokeCount++ > 0) invokeBuilder.add(".");
            invokeBuilder.add("$L($L)", m.methodName, argsCodeBuilder.build());
        }
        return invokeBuilder.build();
    }

    public SmartCode invokeBest() {
        return SmartCode
                .builder()
                .providingType(resultType())
                .withLocals(builder -> transform(invokeSequence(bestSequence()), resultType()));
    }

    public SmartCode invokeAllToList() {
        TypeName provType = ParameterizedTypeName.get(ClassName.get(List.class), resultType());
        return SmartCode
                .builder()
                .providingType(provType)
                .withLocals(builder -> {
                    String listFieldName = genLocalFieldName();
                    builder.add(CodeBlock.of("new $T( ( $L ) -> { \n",
                            ParameterizedTypeName.get(ClassName.get(ProvideBuilder.class), resultType()), listFieldName
                    ));
                    for (List<MethodDetail> sequence : invokeSequenceVariants) {
                        SmartCode seqCode = invokeSequence(sequence);
                        if (WrapHelper.isList(seqCode.providingType)) {
                            builder.add(listFieldName)
                                    .add(".addAll(")
                                    .add(transform(seqCode, provType))
                                    .add(");\n");
                        } else {
                            builder.add(listFieldName)
                                    .add(".add(")
                                    .add(transform(seqCode, resultType()))
                                    .add(");\n");
                        }
                    }
                    builder.add(" }).all() ");
                    return builder;
                });
    }


    private SmartCode invokeSequence(List<MethodDetail> sequence) {
        return SmartCode.builder().withLocals(builder -> {
                    int invokeCount = 0;
                    for (MethodDetail m : sequence) {
                        if (invokeCount++ > 0) builder.add(".");
                        builder.add(m.methodName)
                                .add("(");

                        int argCount = 0;
                        for (FieldDetail arg : m.args) {
                            if (argCount++ > 0) builder.add(", ");
                            boolean isList = isList(arg.type);
                            List<FieldDetail> typeFields = StListUtils.filter(builder.getDeclaredFields(), (i, f) ->
                                    Objects.equals(nonWrappedType(f.type), nonWrappedType(arg.type)));

                            FieldDetail field = isList ? StListUtils.first(typeFields, (i, f) ->
                                    isList(f.type) && Objects.equals(f.qualifierAnns, arg.qualifierAnns)
                            ) : null;
                            if (field == null) {
                                //non list
                                field = StListUtils.first(typeFields, (i, f) -> Objects.equals(f.qualifierAnns, arg.qualifierAnns));
                            }

                            if (field == null) {
                                builder.add("null", null);
                            } else {
                                // unwrap type
                                builder.add(
                                        transform(SmartCode.of(field.name, singleton(field.name))
                                                        .providingType(field.type),
                                                arg.type
                                        ));
                            }
                        }

                        builder.add(")");
                    }
                    return builder;
                })
                .providingType(sequence.get(sequence.size() - 1).returnType);
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
                        .append(String.join(",", StListUtils.format(m.args, f -> f.type.toString())))
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
