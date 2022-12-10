package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * protect injected components in class.
 *
 */

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
public @interface ProtectInjected {

    /**
     * protect tim millis.
     * Default protect 5 sec.
     *
     */
    long timeMillis() default 5000L;

}
