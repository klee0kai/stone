package com.github.klee0kai.stone.wrappers;

import javax.inject.Provider;

public interface Ref<T> extends Provider<T> {

    T get();

}
