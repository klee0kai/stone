package com.github.klee0kai.stone.annotations.module;


import java.lang.annotation.*;

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
@Inherited
public @interface BindInstance {

    enum CacheType {
        WEAK,
        SOFT,
        STRONG
    }

    BindInstance.CacheType cache() default BindInstance.CacheType.SOFT;

}