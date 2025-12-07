package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.codegen.model.WrapperCreatorField;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.helpers.wrap.WrapHelper;
import com.github.klee0kai.stone.helpers.wrap.WrapType;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.annotations.WrapperCreatorsAnn;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.LinkedList;
import java.util.Objects;

import static com.github.klee0kai.stone.checks.WrappersCreatorChecks.*;
import static com.github.klee0kai.stone.exceptions.ExceptionStringBuilder.createErrorMes;

public class WrappersSupportBuilder {

    public ClassName className;

    public static final String provideWrappersGlFieldPrefixName = "__wrapperCreator";
    public final LinkedList<WrapperCreatorField> wrapperCreatorFields = new LinkedList<>();

    public WrappersSupportBuilder(ClassName className) {
        this.className = className;
    }

    public WrappersSupportBuilder addProvideWrapperField(ClassDetail provideWrappersCl) {
        String name = provideWrappersGlFieldPrefixName + wrapperCreatorFields.size();
        wrapperCreatorFields.add(new WrapperCreatorField(
                name,
                provideWrappersCl.ann(WrapperCreatorsAnn.class).wrappers,
                FieldSpec.builder(provideWrappersCl.className, name, Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                        .initializer("new $T()", provideWrappersCl.className)
        ));

        boolean isSimpleWrapper = ListUtils.first(provideWrappersCl.getAllParents(false),
                (i, it) -> Objects.equals(it.className, wrapperClName)) != null;

        boolean isAsyncWrapper = ListUtils.first(provideWrappersCl.getAllParents(false),
                (i, it) -> Objects.equals(it.className, asyncWrapperClName)) != null;

        boolean isCycleWrapper = ListUtils.first(provideWrappersCl.getAllParents(false),
                (i, it) -> Objects.equals(it.className, circleWrapperClName)) != null;

        for (ClassName wrapper : provideWrappersCl.ann(WrapperCreatorsAnn.class).wrappers) {
            WrapType wrapType = new WrapType();
            wrapType.isNoCachingWrapper = !isAsyncWrapper;
            wrapType.typeName = wrapper;
            wrapType.wrap = (providingType, or) -> {
                CodeBlock.Builder builder = CodeBlock.builder();

                if (isSimpleWrapper) {
                    builder.add(CodeBlock.of("$T.$L.wrap( $T.class , ", className, name, wrapper))
                            .add(or)
                            .add(")");
                } else if (isAsyncWrapper || isCycleWrapper) {
                    builder.add(CodeBlock.of("$T.$L.wrap( $T.class , () -> ", className, name, wrapper))
                            .add(or)
                            .add(")");
                } else {
                    throw new IncorrectSignatureException(createErrorMes()
                            .typeTransformNonSupport(providingType, wrapper)
                            .build());
                }
                return builder.build();
            };
            wrapType.unwrap = (providingType, or) -> {
                CodeBlock.Builder builder = CodeBlock.builder();
                TypeName paramType = WrapHelper.paramType(providingType);
                if (isCycleWrapper) {

                    builder.add("$T.$L.unwrap( $T.class , $T.class, ", className, name, wrapper, paramType)
                            .add(or)
                            .add(")");
                } else {
                    throw new IncorrectSignatureException(createErrorMes()
                            .typeTransformNonSupport(providingType, wrapper)
                            .build());
                }

                return builder.build();
            };

            WrapHelper.support(wrapType);
        }
        return this;
    }

    public boolean isEmpty() {
        return wrapperCreatorFields.isEmpty();
    }

    public TypeSpec build() {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        for (WrapperCreatorField field : wrapperCreatorFields)
            typeSpecBuilder.addField(field.fieldBuilder.build());

        return typeSpecBuilder.build();
    }


    public TypeSpec buildAndWrite() {
        TypeSpec typeSpec = build();
        if (typeSpec != null) {
            CodeFileUtil.writeToJavaFile(className.packageName(), typeSpec);
        }

        return typeSpec;
    }

}
