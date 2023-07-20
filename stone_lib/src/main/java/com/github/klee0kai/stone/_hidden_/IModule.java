package com.github.klee0kai.stone._hidden_;

import com.github.klee0kai.stone._hidden_.types.SwitchCacheParams;

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
    boolean __init(Object ob);

    /**
     * Init caches from module prototype.
     * using in extOf method
     */
    void __initCachesFrom(IModule module);

    /**
     * Update values of bindInstance variables
     *
     * @param module related module, source to update
     */
    void __updateBindInstancesFrom(IModule module);

    /**
     * bind instance objects
     *
     * @param object - An instance of bindable objects
     */
    boolean __bind(Object object);

    /**
     * get component's factory
     *
     * @return
     */
    Object __getFactory();

    /**
     * Switch cache type for scope
     */
    void __switchRef(Set<Class> scopes, SwitchCacheParams param);

    /**
     * Clear null refs.
     * Useful after gc
     */
    void __clearNulls();


}
