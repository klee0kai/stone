package com.github.klee0kai.stone.codegen;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.codegen.model.WrapperCreatorField;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.github.klee0kai.stone.helpers.wrap.WrapHelper;
import com.github.klee0kai.stone.helpers.wrap.WrapType;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.annotations.WrapperCreatorsAnn;
import com.github.klee0kai.stone.utils.CodeFileUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.LinkedList;
import java.util.Objects;

import static com.github.klee0kai.stone.checks.WrappersCreatorChecks.asyncWrapperClName;
import static com.github.klee0kai.stone.checks.WrappersCreatorChecks.wrapperClName;
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

        for (ClassName wrapper : provideWrappersCl.ann(WrapperCreatorsAnn.class).wrappers) {
            WrapType wrapType = new WrapType();
            wrapType.isNoCachingWrapper = !isAsyncWrapper;
            wrapType.typeName = wrapper;
            wrapType.wrap = or -> {
                SmartCode builder = SmartCode.builder();

                if (isSimpleWrapper) {
                    builder.add(CodeBlock.of("$T.$L.wrap( $T.class , ", className, name, wrapper))
                            .add(or)
                            .add(")");
                } else if (isAsyncWrapper) {
                    builder.add(CodeBlock.of("$T.$L.wrap( $T.class , () -> ", className, name, wrapper))
                            .add(or)
                            .add(")");
                } else {
                    throw new IncorrectSignatureException(createErrorMes()
                            .typeTransformNonSupport(or.providingType, wrapper)
                            .build());
                }
                if (or.providingType != null)
                    builder.providingType(ParameterizedTypeName.get(wrapper, or.providingType));
                return builder;
            };
            wrapType.unwrap = or -> {
                throw new IncorrectSignatureException(createErrorMes()
                        .typeTransformNonSupport(or.providingType, wrapper)
                        .build());
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
