package com.github.klee0kai.stone.annotations;


import java.lang.annotation.*;

@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.METHOD)
@Inherited
public @interface Singleton {

    enum CacheType {
        WEAK,
        SOFT,
        STRONG
    }

    CacheType cache() default CacheType.SOFT;

}
