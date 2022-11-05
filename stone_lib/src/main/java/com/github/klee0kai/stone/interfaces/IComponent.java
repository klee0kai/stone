package com.github.klee0kai.stone.interfaces;

public interface IComponent {

    /**
     * init modules.
     * @param modules can be:
     *                - a factory instance
     *                - a factory class
     *                - An instance of bindable objects
     */
    void init(Object... modules);

    /**
     * this component extends of other
     */
    void extOf(IComponent components);


}
