package stone.__hidden__


/**
 * Private Stone class
 * Each Stone component implement this interface.
 */
interface IPrivateComponent {
    /**
     * init modules.
     *
     * @param modules can be:
     * - a factory instance
     * - a factory class
     */
    @Deprecated("Create init method with module type as argument")
    fun __init(vararg modules: Any?)

    /**
     * init dependencies
     *
     * @param dependencies - An instance of dependencies
     */
    @Deprecated("Create init method with dependency type as argument")
    fun __initDependencies(vararg dependencies: Any?)

    /**
     * bind instance objects
     *
     * @param objects - An instance of bindable objects
     */
    fun __bind(vararg objects: Any?)

    /**
     * this component extends of other
     */
    fun __extOf(components: stone.__hidden__.IPrivateComponent?)

    /**
     * hidden module
     */
    fun __hidden(): stone.__hidden__.IModule?

    /**
     *
     * @param callback
     */
    fun __eachModule(callback: (stone.__hidden__.IModule) -> Unit)
}
