package com.github.klee0kai.stone.wrappers;

/**
 * Providing an object with lazy providing.
 * The object will be provided the first time it is used.
 * <pre>{@code
 *    ㅤ@Component
 *     interface Component {
 *
 *         LazyProvide<WelcomePresenter> presenter();
 *
 *     }
 * }</pre>
 */
public class LazyProvide<T> implements Ref<T> {

    private T value = null;
    private final Ref<T> call;

    public LazyProvide(Ref<T> call) {
        this.call = call;
    }

    @Override
    public T get() {
        if (value != null)
            return value;
        return value = call.get();
    }
}
