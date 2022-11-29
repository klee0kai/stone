package com.github.klee0kai.stone.types;

import javax.inject.Provider;

public interface IRef<T> extends Provider<T> {

    T get();

}
