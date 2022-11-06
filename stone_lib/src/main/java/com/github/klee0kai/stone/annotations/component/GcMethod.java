package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.*;


/**
 * GC method in component
 */

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
@Inherited
public @interface GcMethod {

    /**
     * collect soft cached components, which not use
     */
    boolean includeSoftRefs() default false;

    /**
     * collect strong cached components, which not use
     */
    boolean includeStrongRefs() default false;

}
