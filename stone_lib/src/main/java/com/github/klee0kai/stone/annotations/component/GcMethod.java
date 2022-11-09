package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.*;


/**
 * GC method in component
 */

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
@Inherited
public @interface GcMethod {

}
