package stone.annotations.component


/**
 * Initializing a module or dependency in a component.
 *
 *
 * An initialization method is declared in a component that accepts an instance of the module or its class.
 *
 * <pre>`ㅤ@Component
 * interface AppComponent() {
 *
 * ㅤ@Init
 * void initRepositoriesModule(RepositoriesModule module);
 *
 * ㅤ@Init
 * void initRepositoriesModule(Class<? extends RepositoriesModule> module);
 *
 * }
 *
 * ㅤㅤ@Module
 * interface RepositoriesModule{
 * // some code
 * }
`</pre> *
 *
 *
 * Dependencies can only be initialized on an instance.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Init 
