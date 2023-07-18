package com.github.klee0kai.test.car.di.qualifiers.qualifiers;


import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BumperQualifier {
    BumperType type() default BumperType.Simple;

    enum BumperType {
        Simple,
        Reinforced
    }


}
