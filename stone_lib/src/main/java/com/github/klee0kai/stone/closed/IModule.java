package com.github.klee0kai.stone.closed;

import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.closed.types.TimeScheduler;

import java.util.Set;

/**
 * Stone Private class
 */


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
     * Switch cache type for scope
     */
    void switchRef(Set<Class> scopes, SwitchCache.CacheType cacheType, TimeScheduler scheduler, long time);

}
