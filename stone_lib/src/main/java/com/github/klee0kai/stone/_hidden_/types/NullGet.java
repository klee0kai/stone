package com.github.klee0kai.stone._hidden_.types;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class NullGet {

    public static <T, R> R let(T v, Function<T, R> uncover) {
        return v != null ? uncover.apply(v) : null;
    }

    public static <T> List<T> list(T v) {
        return v != null ? Collections.singletonList(v) : null;
    }

    @SafeVarargs
    public static <T> T first(T... values) {
        for (T v : values) if (v != null) return v;
        return null;
    }

}
