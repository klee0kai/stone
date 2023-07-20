package com.github.klee0kai.stone.closed.types;

import java.util.*;

/**
 * Stone Private class
 */

public class StMultiKey {

    private final List<Object> subKeys = new LinkedList<>();

    public StMultiKey(Object... subKeys){
        this.subKeys.addAll(Arrays.asList(subKeys));
    }

    @Override
    public int hashCode() {
        //AbstractList hash code
        int hashCode = 1;

        Object e;
        for(Iterator<Object> var2 = subKeys.iterator(); var2.hasNext();
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode())) {
            e = var2.next();
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        //AbstractList equals check
        if (obj == this) {
            return true;
        } else if (!(obj instanceof StMultiKey)) {
            return false;
        } else {
            ListIterator<Object> e1 = this.subKeys.listIterator();
            ListIterator<?> e2 = ((StMultiKey)obj).subKeys.listIterator();

            while(true) {
                if (e1.hasNext() && e2.hasNext()) {
                    Object o1 = e1.next();
                    Object o2 = e2.next();
                    if (o1 == null) {
                        if (o2 == null) {
                            continue;
                        }
                    } else if (o1.equals(o2)) {
                        continue;
                    }

                    return false;
                }
                return !e1.hasNext() && !e2.hasNext();
            }
        }
    }
}
