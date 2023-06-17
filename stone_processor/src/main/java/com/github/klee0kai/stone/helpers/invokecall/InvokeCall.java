package com.github.klee0kai.stone.helpers.invokecall;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.*;
import java.util.function.Function;

import static com.github.klee0kai.stone.helpers.invokecall.GenArgumentFunctions.unwrapArgument;

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

    public final List<MethodDetail> invokeSequence = new LinkedList<>();
    public final int flags;

    /**
     * Create new invoke sequence
     *
     * @param callSequence ordered methods in invoke sequence
     */
    public InvokeCall(MethodDetail... callSequence) {
        this.invokeSequence.addAll(Arrays.asList(callSequence));
        this.flags = 0;
    }

    /**
     * Create new invoke sequence
     *
     * @param flags        available flags:
     *                     {@code INVOKE_PROVIDE_OBJECT_CACHED} providing cached object
     * @param callSequence ordered methods in invoke sequence
     */
    public InvokeCall(int flags, MethodDetail... callSequence) {
        this.invokeSequence.addAll(Arrays.asList(callSequence));
        this.flags = flags;
    }

    /**
     * Using arguments in invoke sequence
     *
     * @param filter filter arguments by except list. Null of no filter
     * @return collection of all argument's types
     */
    public Set<TypeName> argTypes(Set<TypeName> filter) {
        Set<TypeName> argsTypes = new HashSet<>();
        for (MethodDetail m : invokeSequence) {
            List<TypeName> types = ListUtils.format(m.args, (it) -> it.type);
            if (filter != null) types = ListUtils.filter(types, (inx, it) -> filter.contains(it));
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
        for (MethodDetail m : invokeSequence) {

            int argCount = 0;
            CodeBlock.Builder argsCodeBuilder = CodeBlock.builder();
            for (FieldDetail arg : m.args) {
                if (argCount++ > 0) argsCodeBuilder.add(",");
                CodeBlock argCode = ListUtils.firstNotNull(argGens, it -> it.apply(arg.type));
                argsCodeBuilder.add(argCode != null ? argCode : CodeBlock.of("null"));
            }

            if (invokeCount++ > 0) invokeBuilder.add(".");
            invokeBuilder.add("$L($L)", m.methodName, argsCodeBuilder.build());
        }
        return invokeBuilder.build();
    }

}
