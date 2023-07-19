package com.github.klee0kai.stone.types.wrappers;

public interface Wrapper {

    /**
     * Provide wrapped object.
     *
     * @param wrapperCl type of wrapper
     * @param original  original object
     * @param <Wr>      type of wrapper
     * @param <T>       type of providing original object
     * @return wrapped object provider
     */
    <Wr, T> Wr wrap(Class<Wr> wrapperCl, T original);

}
