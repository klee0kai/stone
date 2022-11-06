package com.github.klee0kai.stone.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.TYPE)
public @interface Component {

    /**
     * Qualifiers for providing components.
     * Control building components and cached components.
     * Can use as id class wrapper for component's constructor
     *
     * @return
     */
    Class[] qualifiers() default {};


}
