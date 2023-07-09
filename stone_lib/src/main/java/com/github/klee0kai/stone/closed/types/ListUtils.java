package com.github.klee0kai.stone.closed.types;

import java.util.*;

/**
 * Stone Private class
 */
public class ListUtils {

    public interface IFormat<Tin, Tout> {
        Tout format(Tin it);
    }

    public interface ICompare<T> {
        int compare(T it1, T it2);
    }


    public interface IFilter<T> {
        boolean filter(int inx, T it);
    }

    public interface IEq<T> {
        boolean eq(T it1, T it2);
    }

    public static <Tin, Tout> List<Tout> format(Iterable<Tin> list, IFormat<Tin, Tout> format) {
        if (list == null) return null;
        LinkedList<Tout> touts = new LinkedList<>();
        if (list != null) for (Tin it : list) {
            touts.add(format.format(it));
        }
        return touts;
    }

    public static <T> boolean contains(List<T> list, IFilter<T> filter) {
        int idx = 0;
        if (list != null) for (T it : list) {
            if (filter.filter(idx++, it))
                return true;
        }
        return false;
    }

    public static <T> T first(Iterable<T> list, IFilter<T> filter) {
        int idx = 0;
        if (list != null) for (T it : list) {
            if (filter == null || filter.filter(idx++, it))
                return it;
        }
        return null;
    }

    public static <T> T first(Iterable<T> list) {
        return first(list, null);
    }

    public static <T> int indexOf(List<T> list, IFilter<T> filter) {
        int idx = 0;
        if (list != null) for (T it : list) {
            if (filter.filter(idx, it))
                return idx;
            else idx++;
        }
        return -1;
    }

    public static <Tin, Tout> Tout firstNotNull(List<Tin> list, IFormat<Tin, Tout> format) {
        if (list != null) for (Tin it : list) {
            Tout out = format.format(it);
            if (out != null) return out;
        }
        return null;
    }

    public static <T> LinkedList<T> filter(Collection<T> list, IFilter<T> filter) {
        if (list == null) return null;
        LinkedList<T> touts = new LinkedList<>();
        int idx = 0;
        if (list != null) for (T it : list) {
            if (filter.filter(idx++, it))
                touts.add(it);
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

    public static <T> Set<T> setOf(List<T> list, T... items) {
        Set<T> set = new HashSet<>();
        set.addAll(list);
        set.addAll(Arrays.asList(items));
        return set;
    }

    public static <T> boolean endWith(List<T> parentList, List<T> childList) {
        if (parentList == null || childList == null) return false;
        if (childList.size() > parentList.size()) return false;
        int pSt = parentList.size() - childList.size();

        Iterator<T> ch = childList.listIterator();
        Iterator<T> p = parentList.listIterator(pSt);
        while (ch.hasNext() && p.hasNext()) {
            if (!Objects.equals(p.next(), ch.next()))
                return false;
        }
        return !ch.hasNext() && !p.hasNext();
    }

    public static <T> LinkedList<T> removeDoubles(List<T> list, IEq<T> eqHelper) {
        if (list == null) return null;
        LinkedList<T> out = new LinkedList<>();
        for (T item : list) {
            boolean contains = ListUtils.contains(out, (i, it) -> eqHelper.eq(item, it));
            if (!contains) out.add(item);
        }
        return out;
    }

    public static <T> LinkedList<T> removeDoublesRight(List<T> list, IEq<T> eqHelper) {
        if (list == null) return null;
        LinkedList<T> out = new LinkedList<>();
        for (T item : list) {
            out = ListUtils.filter(out, (i, it) -> !eqHelper.eq(item, it));
            out.add(item);
        }
        return out;
    }

    public static <T> boolean listAreSame(List<T> list1, List<T> list2, IEq<T> eqHelper) {
        if (list1 == list2) return true;
        if ((list2 == null) != (list1 == null)) return false;
        if (list1.size() != list2.size()) return false;
        for (T it1 : list1) if (!list2.contains(it1)) return false;
        return true;
    }


}
