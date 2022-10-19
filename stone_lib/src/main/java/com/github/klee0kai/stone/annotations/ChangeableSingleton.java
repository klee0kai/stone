package com.github.klee0kai.stone.annotations;


import java.lang.annotation.*;

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
@Inherited
public @interface ChangeableSingleton {

    Singleton.CacheType cache() default Singleton.CacheType.SOFT;

}
