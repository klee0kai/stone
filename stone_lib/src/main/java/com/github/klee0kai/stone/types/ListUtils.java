package com.github.klee0kai.stone.types;

import java.util.*;

public class ListUtils {

    public interface IFormat<Tin, Tout> {
        Tout format(Tin ob);
    }

    public interface ICompare<T> {
        int compare(T ob1, T ob2);

    }


    public interface IFilter<T> {
        boolean filter(int inx, T ob);

    }

    public static <Tin, Tout> List<Tout> format(List<Tin> list, IFormat<Tin, Tout> format) {
        LinkedList<Tout> touts = new LinkedList<>();
        if (list != null) for (Tin it : list) {
            touts.add(format.format(it));
        }
        return touts;
    }

    public static <T> boolean contains(List<T> list, IFilter<T> filter) {
        int idx = 0;
        for (T it : list) {
            if (filter.filter(idx++, it))
                return true;
        }
        return false;
    }

    public static <T> void orderedAdd(List<T> list, T item, ICompare<T> compare) {
        ListIterator<T> itr = list.listIterator();
        while (true) {
            if (!itr.hasNext()) {
                itr.add(item);
                return;
            }

            T elementInList = itr.next();
            if (compare.compare(elementInList, item) > 0) {
                itr.previous();
                itr.add(item);
                return;
            }
        }
    }

    public static <T> Set<T> setOf(List<T> list, T... items) {
        Set<T> set = new HashSet<>();
        set.addAll(list);
        set.addAll(Arrays.asList(items));
        return set;
    }


}
