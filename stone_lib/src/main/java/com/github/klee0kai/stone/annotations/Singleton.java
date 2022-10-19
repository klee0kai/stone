package com.github.klee0kai.stone.annotations;


import java.lang.annotation.*;

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
@Inherited
public @interface Singleton {

    enum CacheType {
        WEAK,
        SOFT,
        STRONG
    }

    CacheType cache() default CacheType.SOFT;

    String scope() default "";

}
