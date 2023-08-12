package com.github.klee0kai.stone.wrappers;

/**
 * Providing an object without any deduction.
 * Each time you use this object through a provider,
 * the object is not cached in your class, but is taken from the component each time.
 * <p>
 * Together with the use of Kotlin delegates, you can fully use the objects from the
 * DI component directly without keeping them from being deleted in your class.
 * <pre>{@code
 *    ㅤ@Component
 *     interface Component {
 *
 *         PhantomProvide<WelcomePresenter> presenter();
 *
 *     }
 * }</pre>
 */
public class PhantomProvide<T> implements Ref<T> {

    private final Ref<T> call;

    public PhantomProvide(Ref<T> call) {
        this.call = call;
    }

    @Override
    public T get() {
        return call.get();
    }
}
