package com.github.klee0kai.stone.closed.provide;

import java.util.LinkedList;
import java.util.List;

public class ProvideBuilder<T> {

    public interface ProvideBody<T> {
        void provide(LinkedList<T> consumer);

    }

    private ProvideBody<T> provideBody;

    public ProvideBuilder(ProvideBody<T> provideBody) {
        this.provideBody = provideBody;
    }


    public T first() {
        LinkedList<T> list = new LinkedList<>();
        provideBody.provide(list);
        return list.isEmpty() ? null : list.getFirst();
    }

    public List<T> all() {
        LinkedList<T> list = new LinkedList<>();
        provideBody.provide(list);
        return list;
    }
}
