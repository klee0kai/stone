package com.github.klee0kai.stone.interfaces;

public interface IModule {

    /**
     * Init module
     * @param ob - binding object or factory of objects
     */
    void init(Object ob);


    /**
     * this module extents of other module
     * @param superStoneModule - super stone module which this module extending for
     */
    void extOf(IModule superStoneModule);

}
