package com.github.klee0kai.stone.utils;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class ListUtils {

    public interface IFormat<Tin, Tout> {
        Tout format(Tin ob);
    }

    public static <Tin, Tout> List<Tout> format(@Nullable List<Tin> list, IFormat<Tin, Tout> format) {
        LinkedList<Tout> touts = new LinkedList<>();
        if (list != null) for (Tin it : list) {
            touts.add(format.format(it));
        }
        return touts;
    }

}
