package com.github.klee0kai.stone.interfaces;


/**
 * Each Stone component implement this interface.
 * Could be use
 */
@Deprecated
public interface IComponent {

    /**
     * init modules.
     *
     * @param modules can be:
     *                - a factory instance
     *                - a factory class
     * @deprecated Create init method with module type as argument
     */
    @Deprecated
    void init(Object... modules);

    /**
     * init dependencies
     *
     * @param dependencies - An instance of dependencies
     * @deprecated Create init method with dependency type as argument
     */
    @Deprecated
    void initDependencies(Object... dependencies);

    /**
     * bind instance objects
     *
     * @param objects - An instance of bindable objects
     */
    @Deprecated
    void bind(Object... objects);

    /**
     * this component extends of other
     */
    @Deprecated
    void extOf(IComponent components);

}
