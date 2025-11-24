package com.github.klee0kai.stone.__hidden__

import kotlin.reflect.KClass

/**
 * Stone Private class
 */
interface IModule {

    /**
     * get component's factory
     *
     * @return
     */
    val factory: Any?

    /**
     * Init module
     *
     * @param ob can be:
     * - a factory instance
     * - a factory class
     */
    fun __init(ob: Any): Boolean

    /**
     * Init caches from module prototype.
     * using in extOf method
     */
    fun __initCachesFrom(module: IModule)

    /**
     * Update values of bindInstance variables
     *
     * @param module related module, source to update
     */
    fun __updateBindInstancesFrom(module: IModule)

    /**
     * bind instance objects
     *
     * @param object - An instance of bindable objects
     */
    fun __bind(`object`: Any?): Boolean


    /**
     * Switch cache type for scope
     */
    public fun __switchRef(scopes: Set<KClass<*>>, __params: SwitchCacheParam)

    /**
     * Clear null refs.
     * Useful after gc
     */
    fun __clearNulls()
}
