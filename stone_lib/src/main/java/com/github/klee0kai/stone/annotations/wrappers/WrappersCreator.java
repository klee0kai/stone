package com.github.klee0kai.stone.annotations.wrappers;


import com.github.klee0kai.stone.types.wrappers.Wrapper;

import java.lang.annotation.*;

/**
 * Provide custom wrappers creator, class
 * <p>
 * Should implement {@link Wrapper}
 * and declare custom wrappers in annotation
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.TYPE)
@Inherited
public @interface WrappersCreator {

    /**
     * Custom Wrappers, can be provided
     *
     * @return
     */
    Class[] wrappers() default {};


    /**
     * Providing without caching and object holding
     */
    boolean noCachesProviding() default true;

    boolean isAsyncProvider() default true;

}
