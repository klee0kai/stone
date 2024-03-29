package com.github.klee0kai.stone.helpers.codebuilder;

import com.github.klee0kai.stone.model.FieldDetail;
import com.github.klee0kai.stone.model.annotations.QualifierAnn;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import java.util.*;

public class SmartCode implements ISmartCode {

    private static final boolean REMOVE_NON_USED = true;

    // atom meta info
    private CodeBlock code = null;
    public String fieldName;
    public Set<QualifierAnn> qualifierAnns = new HashSet<>();
    public TypeName providingType;

    public LinkedList<String> usedFields = new LinkedList<>();


    // code group info
    private Set<FieldDetail> declaredFields = Collections.emptySet();

    private final LinkedList<ISmartCode> codes = new LinkedList<>();

    private final LinkedList<SmartCode> collectedCode = new LinkedList<>();


    public static SmartCode of(CodeBlock code, Collection<String> usedFields) {
        SmartCode builder = new SmartCode();
        builder.code = code;
        if (usedFields != null) builder.usedFields.addAll(usedFields);
        return builder;
    }

    public static SmartCode of(String code, Collection<String> usedFields) {
        SmartCode builder = new SmartCode();
        builder.code = CodeBlock.of(code);
        if (usedFields != null) builder.usedFields.addAll(usedFields);
        return builder;
    }

    public static SmartCode of(CodeBlock code) {
        return of(code, null);
    }

    public static SmartCode of(String code) {
        return of(code, null);
    }

    // delayed code block
    private SmartCode() {

    }

    public static SmartCode builder() {
        return new SmartCode();
    }

    //meta info

    public SmartCode providingType(TypeName typeName) {
        providingType = typeName;
        return this;
    }

    // public methods

    public SmartCode withLocals(DelayedCode codeGen) {
        codes.add(codeGen);
        return this;
    }

    public SmartCode add(SmartCode code) {
        codes.add(code);
        if (providingType == null) providingType = code.providingType;
        return this;
    }


    public SmartCode add(String code, Collection<String> usedFields) {
        return add(SmartCode.of(code, usedFields));
    }

    public SmartCode add(String code) {
        return add(code, null);
    }

    public SmartCode add(CodeBlock codeBlock, Collection<String> usedFields) {
        return add(SmartCode.of(codeBlock, usedFields));
    }

    public SmartCode add(CodeBlock codeBlock) {
        return add(codeBlock, null);
    }


    public SmartCode localVariable(String fieldName, Set<QualifierAnn> qualifiers, SmartCode initVariable) {
        this.providingType = initVariable.providingType;
        this.fieldName = fieldName;
        this.qualifierAnns.addAll(qualifiers);

        add(CodeBlock.of("$T $L = ", providingType, fieldName), null);
        add(initVariable);
        add(";\n");
        return this;
    }

    public int getSize() {
        int size = 1;
        for (ISmartCode s : codes) {
            if (s instanceof SmartCode) size += ((SmartCode) s).getSize();
        }
        return size;
    }

    public List<FieldDetail> getDeclaredFields() {
        return new ArrayList<>(declaredFields);
    }

    public CodeBlock build(Collection<FieldDetail> declaredFields) {
        clearAllCollectedCodes();
        return internalBuild(declaredFields == null ? new HashSet<>() : new HashSet<>(declaredFields));
    }

    private void clearAllCollectedCodes() {
        collectedCode.clear();
        for (ISmartCode s : codes) {
            if (s instanceof SmartCode) ((SmartCode) s).clearAllCollectedCodes();
        }
    }

    private CodeBlock internalBuild(HashSet<FieldDetail> declaredFields) {
        collect(declaredFields);

        CodeBlock.Builder builder = CodeBlock.builder();
        if (code != null) builder.add(code);
        for (SmartCode c : collectedCode) {
            if (REMOVE_NON_USED && c.fieldName != null && !usedFields.contains(c.fieldName)) {
                continue;
            }

            builder.add(c.internalBuild(declaredFields));
        }

        return builder.build();
    }

    private SmartCode collect(HashSet<FieldDetail> declaredFields) {
        if (!collectedCode.isEmpty()) return this;

        this.declaredFields = declaredFields;

        for (ISmartCode c : codes) {
            SmartCode smartCode = null;
            if (c instanceof DelayedCode) {
                DelayedCode delayedCode = (DelayedCode) c;
                smartCode = SmartCode.builder();

                smartCode.declaredFields = declaredFields;
                smartCode = delayedCode.apply(smartCode);

                smartCode.declaredFields = declaredFields;
            } else if (c instanceof SmartCode) {
                smartCode = (SmartCode) c;
            }
            if (smartCode == null) continue;
            collectedCode.add(smartCode.collect(declaredFields));

            if (smartCode.fieldName != null && smartCode.providingType != null) {
                FieldDetail field = FieldDetail.simple(smartCode.fieldName, smartCode.providingType);
                field.qualifierAnns = smartCode.qualifierAnns;
                declaredFields.add(field);
            }
            usedFields.addAll(smartCode.usedFields);
        }

        this.declaredFields = Collections.emptySet();
        return this;
    }

}
