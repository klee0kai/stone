package com.klee0kai.stone.annotations;


import java.lang.annotation.*;

@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.METHOD)
@Inherited
public @interface Item {

    enum CacheType {
        WEAK,
        SOFT,
        STRONG
    }

    CacheType cache() default CacheType.SOFT;

}
