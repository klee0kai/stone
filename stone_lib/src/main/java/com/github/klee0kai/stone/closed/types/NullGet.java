package com.github.klee0kai.stone.closed.types;

import java.util.function.Function;

public class NullGet {

    public static <T, R> R let(T v, Function<T, R> uncover) {
        return v != null ? uncover.apply(v) : null;
    }

}
