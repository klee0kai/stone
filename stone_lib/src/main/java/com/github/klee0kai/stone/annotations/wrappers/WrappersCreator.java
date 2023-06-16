package com.github.klee0kai.stone.annotations.wrappers;


import com.github.klee0kai.stone.types.wrappers.WrapperCreator;

import java.lang.annotation.*;

/**
 * Provide custom wrappers creator, class
 * <p>
 * Should implement {@link WrapperCreator}
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

}
