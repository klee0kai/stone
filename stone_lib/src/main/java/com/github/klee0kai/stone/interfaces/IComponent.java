package com.github.klee0kai.stone.interfaces;

public interface IComponent {

    /**
     * init modules
     */
    void init(Object... modules);

    /**
     * this component extends of other
     */
    void extOf(IComponent components);


}
