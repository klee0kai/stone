package stone.__hidden__

/**
 * Stone Private class
 */
interface IModule {
    /**
     * Init module
     *
     * @param ob can be:
     * - a factory instance
     * - a factory class
     */
    fun __init(ob: Any?): Boolean

    /**
     * Init caches from module prototype.
     * using in extOf method
     */
    fun __initCachesFrom(module: IModule?)

    /**
     * Update values of bindInstance variables
     *
     * @param module related module, source to update
     */
    fun __updateBindInstancesFrom(module: IModule?)

    /**
     * bind instance objects
     *
     * @param object - An instance of bindable objects
     */
    fun __bind(`object`: Any?): Boolean

    /**
     * get component's factory
     *
     * @return
     */
    fun __getFactory(): Any?

    /**
     * TODO Switch cache type for scope
     */
//    fun __switchRef(scopes: MutableSet<Class<*>?>?, param: SwitchCacheParam?)

    /**
     * Clear null refs.
     * Useful after gc
     */
    fun __clearNulls()
}
