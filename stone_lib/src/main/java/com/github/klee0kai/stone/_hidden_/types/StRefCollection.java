package com.github.klee0kai.stone._hidden_.types;

import com.github.klee0kai.stone.types.wrappers.Ref;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StRefCollection<T> {

    private final List<Ref<T>> refs = new LinkedList<>();

    public void add(Ref<T> ref) {
        clearNulls();
        refs.add(ref);
    }

    public List<Ref<T>> getAllRefs() {
        clearNulls();
        return refs;
    }

    public List<T> getAll() {
        clearNulls();
        return StListUtils.format(refs, Ref::get);
    }

    public void clearNulls() {
        Iterator<Ref<T>> it = refs.iterator();
        while (it.hasNext()) {
            Ref<T> ref = it.next();
            if (ref == null || ref.get() == null)
                it.remove();
        }
    }


}
