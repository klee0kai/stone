package com.github.klee0kai.stone.annotations.component


/**
 * A component extends a parent component.
 * With the help of such an annotation, an extension method is declared on the parent component.
 * <pre>`ㅤ@Component
 * public interface AppExtendComponent extends AppComponent {
 *
 * ㅤ@ExtendOf
 * void extOf(AppComponent parent);
 *
 * }
`</pre> *
 *
 *
 * For the parent component, all object creation factories are replaced with new ones,
 * from the child extending component.
 * Components become interconnected, all cleanups,
 * caching type changes are performed simultaneously for both components.
 *
 *
 * The replacement of generated and provided objects is not done immediately, but gradually.
 * As they are cleared from memory.
 * Be careful, the new objects provided should extend the functionality of the previous ones, not break the old logic.
 * In some cases, interaction between objects of different versions is possible.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class ExtendOf 
