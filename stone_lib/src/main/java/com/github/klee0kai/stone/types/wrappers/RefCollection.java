package com.github.klee0kai.stone.types.wrappers;

import com.github.klee0kai.stone.closed.types.ListUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RefCollection<T> {

    private final List<IRef<T>> refs = new LinkedList<>();

    public void add(IRef<T> iRef) {
        clearNulls();
        refs.add(iRef);
    }

    public List<IRef<T>> getAllRefs() {
        clearNulls();
        return refs;
    }

    public List<T> getAll() {
        clearNulls();
        return ListUtils.format(refs, IRef::get);
    }

    public void clearNulls() {
        Iterator<IRef<T>> it = refs.iterator();
        while (it.hasNext()) {
            IRef<T> ref = it.next();
            if (ref == null || ref.get() == null)
                it.remove();
        }
    }


}
