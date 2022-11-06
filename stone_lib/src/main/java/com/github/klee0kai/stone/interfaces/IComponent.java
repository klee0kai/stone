package com.github.klee0kai.stone.interfaces;

public interface IComponent {

    /**
     * init modules.
     * @param modules can be:
     *                - a factory instance
     *                - a factory class
     *
     */
    void init(Object... modules);

    /**
     * bind instance objects
     * @param objects - An instance of bindable objects
     */
    void bind(Object... objects);

    /**
     * this component extends of other
     */
    void extOf(IComponent components);


    /**
     * Run garbage collector
     * @param includeSoftRefs true - collect also soft cached components, which not use.
     */
    void gc(boolean includeSoftRefs);


}
