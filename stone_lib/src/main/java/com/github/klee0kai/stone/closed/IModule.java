package com.github.klee0kai.stone.closed;

import com.github.klee0kai.stone.closed.types.SwitchCacheParam;

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
     * Init caches from module prototype.
     * using in extOf method
     *
     * @param onlyBindInstance update cached only for bindInstance variables
     */
    void initCachesFrom(IModule module, boolean onlyBindInstance);

    /**
     * bind instance objects
     *
     * @param object - An instance of bindable objects
     */
    boolean bind(Object object);

    /**
     * get component's factory
     *
     * @return
     */
    Object getFactory();

    /**
     * Switch cache type for scope
     */
    void switchRef(Set<Class> scopes, SwitchCacheParam param);

}
