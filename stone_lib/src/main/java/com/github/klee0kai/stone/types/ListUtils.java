package com.github.klee0kai.stone.types;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ListUtils {

    public interface IFormat<Tin, Tout> {
        Tout format(Tin ob);
    }

    public interface ICompare<T> {
        int compare(T ob1, T ob2);

    }

    public static <Tin, Tout> List<Tout> format( List<Tin> list, IFormat<Tin, Tout> format) {
        LinkedList<Tout> touts = new LinkedList<>();
        if (list != null) for (Tin it : list) {
            touts.add(format.format(it));
        }
        return touts;
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


}
