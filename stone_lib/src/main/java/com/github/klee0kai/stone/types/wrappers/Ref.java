package com.github.klee0kai.stone.types.wrappers;

import javax.inject.Provider;

public interface Ref<T> extends Provider<T> {

    T get();

}
