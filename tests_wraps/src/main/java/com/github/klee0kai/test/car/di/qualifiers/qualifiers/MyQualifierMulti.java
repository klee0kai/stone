package com.github.klee0kai.test.car.di.qualifiers.qualifiers;


import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyQualifierMulti {

    String id() default "";

    int indx() default 0;

    Type type() default Type.SIMPLE;

    enum Type {
        SIMPLE,
        MIDDLE,
        HARD,
    }
}

