package com.github.klee0kai.stone.interfaces;

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
    void allWeak();

    /**
     * restore weak Refs
     */
    void restoreRefs();

}
