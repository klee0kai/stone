package com.github.klee0kai.stone.utils;

import java.util.LinkedList;
import java.util.List;

public class ListUtils {

    public interface IFormat<Tin, Tout> {
        Tout format(Tin ob);
    }

    public static <Tin, Tout> List<Tout> format(List<Tin> list, IFormat<Tin, Tout> format) {
        LinkedList<Tout> touts = new LinkedList<>();
        for (Tin it : list) {
            touts.add(format.format(it));
        }
        return touts;
    }

}
