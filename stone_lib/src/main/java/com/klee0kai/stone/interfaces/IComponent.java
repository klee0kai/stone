package com.klee0kai.stone.interfaces;

public interface IComponent {

    /**
     * init modules
     */
    void init(Object... modules);

    /**
     * init by other DI
     */
    void init(IComponent components);


}
