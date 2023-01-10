package com.github.klee0kai.stone.types.wrappers;

import javax.inject.Provider;

public interface IWrapperCreator {

    /**
     * Provide wrapped object.
     *
     * @param wrapperCl        type of wrapper
     * @param originalProvider original object provider
     * @param <Wr>             type of wrapper
     * @param <T>              type of providing original object
     * @return wrapped object provider
     */
    <Wr, T> Wr provideWrapped(Class<Wr> wrapperCl, Provider<T> originalProvider);

}
