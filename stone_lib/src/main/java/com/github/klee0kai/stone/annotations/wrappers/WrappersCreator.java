package com.github.klee0kai.stone.annotations.wrappers;


import com.github.klee0kai.stone.wrappers.creators.CircleWrapper;
import com.github.klee0kai.stone.wrappers.creators.ProviderWrapper;
import com.github.klee0kai.stone.wrappers.creators.Wrapper;

import java.lang.annotation.*;

/**
 * Provide custom wrappers creator, class
 * <p>
 * Should implement {@link Wrapper} or {@link ProviderWrapper} or {@link CircleWrapper}
 * and declare custom wrappers in annotation
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.TYPE)
@Inherited
public @interface WrappersCreator {

    /**
     * Custom Wrappers, can be provided
     */
    Class[] wrappers() default {};

}
