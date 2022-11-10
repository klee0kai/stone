package com.github.klee0kai.stone.interfaces;

import java.util.List;
import java.util.Set;

public interface IModule {

    /**
     * Init module
     *
     * @param ob can be:
     *           - a factory instance
     *           - a factory class
     */
    boolean init(Object ob);


    /**
     * bind instance objects
     *
     * @param object - An instance of bindable objects
     */
    boolean bind(Object object);

    /**
     * this module extents of other module
     *
     * @param superStoneModule - super stone module which this module extending for
     */
    void extOf(IModule superStoneModule);

    /**
     * get component's factory
     *
     * @return
     */
    Object getFactory();

    /**
     * Switch soft refs to weak
     */
    void allWeak(Set<Class> scopes);

    /**
     * restore weak Refs
     */
    void restoreRefs(Set<Class> scopes);

}
