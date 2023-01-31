package com.github.klee0kai.stone.closed.types;


import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class WeakLinkedList<T> {

    private final LinkedList<Reference<T>> list = new LinkedList<>();

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean add(T t) {
        clearNulls();
        return list.add(new WeakReference<>(t));
    }

    public boolean remove(Object o) {
        return clearNulls(o);
    }

    public void clear() {
        list.clear();
    }

    public boolean clearNulls() {
        return clearNulls(null);
    }

    public boolean clearNulls(Object ob) {
        ListIterator<Reference<T>> iter = list.listIterator();
        boolean removed = false;
        while (iter.hasNext()) {
            Reference<T> it = iter.next();
            if (it.get() == null || Objects.equals(it.get(), ob)) {
                iter.remove();
                removed = true;
            }
        }
        return removed;
    }


    public List<T> toList() {
        clearNulls();
        LinkedList<T> outList = new LinkedList<>();
        for (Reference<T> it : list) {
            outList.add(it.get());
        }
        return outList;
    }

    public T get(int i) {
        return list.get(i).get();
    }


    public void add(int i, T t) {
        list.add(i, new WeakReference<>(t));
    }

}
