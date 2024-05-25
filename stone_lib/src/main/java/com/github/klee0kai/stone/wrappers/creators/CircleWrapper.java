package com.github.klee0kai.stone.wrappers.creators;

public interface CircleWrapper extends ProviderWrapper {

    /**
     * UnProvide wrapped object.
     *
     * @param wrapperCl  type of wrapper
     * @param objectType original provide object type
     * @param wrapper    wrapper to unwrap
     * @param <Wr>       type of wrapper
     * @param <T>        type of providing original object
     * @return wrapped object provider
     */
    <Wr, T> T unwrap(Class<Wr> wrapperCl, Class<T> objectType, Wr wrapper);

}
