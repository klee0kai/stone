package com.github.klee0kai.wiki.provide.identifiers;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThreadQualifier {
    ThreadType type() default ThreadType.Main;

    enum ThreadType {
        Main,
        Default,
        IO
    }

}