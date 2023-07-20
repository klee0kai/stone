package com.github.klee0kai.stone.closed.provide;

import com.github.klee0kai.stone.closed.types.StListUtils;

import java.util.Collection;
import java.util.LinkedList;

public class ProvideConsumer<T> {

    private LinkedList<T> list = new LinkedList<>();

    public void addAll(Collection<T> collection) {
        if (collection != null) {
            list.addAll(StListUtils.filter(collection, (i, it) -> it != null));
        }
    }

    public void add(T element) {
        if (element != null) {
            list.add(element);
        }
    }

    public LinkedList<T> getList() {
        return list;
    }

    public T getFirst() {
        return list.isEmpty() ? null : list.getFirst();
    }


}
