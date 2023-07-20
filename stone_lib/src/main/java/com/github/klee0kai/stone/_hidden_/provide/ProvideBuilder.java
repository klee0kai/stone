package com.github.klee0kai.stone._hidden_.provide;

import java.util.List;

public class ProvideBuilder<T> {

    public interface ProvideBody<T> {
        void provide(ProvideConsumer<T> consumer);
    }

    private ProvideBody<T> provideBody;

    public ProvideBuilder(ProvideBody<T> provideBody) {
        this.provideBody = provideBody;
    }

    public T first() {
        ProvideConsumer<T> consumer = new ProvideConsumer<>();
        provideBody.provide(consumer);
        return consumer.getFirst();
    }

    public List<T> all() {
        ProvideConsumer<T> consumer = new ProvideConsumer<>();
        provideBody.provide(consumer);
        return consumer.getList();
    }
}
