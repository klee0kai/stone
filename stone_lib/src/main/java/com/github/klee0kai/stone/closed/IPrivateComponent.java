package com.github.klee0kai.stone.closed;


import com.github.klee0kai.stone.closed.types.StoneCallback;

/**
 * Private Stone class
 * Each Stone component implement this interface.
 */
public interface IPrivateComponent {

    /**
     * init modules.
     *
     * @param modules can be:
     *                - a factory instance
     *                - a factory class
     * @deprecated Create init method with module type as argument
     */
    void __init(Object... modules);

    /**
     * init dependencies
     *
     * @param dependencies - An instance of dependencies
     * @deprecated Create init method with dependency type as argument
     */
    void __initDependencies(Object... dependencies);

    /**
     * bind instance objects
     *
     * @param objects - An instance of bindable objects
     */
    void __bind(Object... objects);

    /**
     * this component extends of other
     */
    void __extOf(IPrivateComponent components);

    /**
     * hidden module
     */
    IModule __hidden();

    /**
     *
     * @param callback
     */
    void __eachModule(StoneCallback<IModule> callback);

}
