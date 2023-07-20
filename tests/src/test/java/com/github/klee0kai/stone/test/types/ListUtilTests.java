package com.github.klee0kai.stone.test.types;

import com.github.klee0kai.stone._hidden_.types.StListUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListUtilTests {

    @Test
    public void sortedAddTest() {
        LinkedList<Integer> sortedList = new LinkedList<>(Arrays.asList(4, 3, 4, 5, 10, 11));
        Collections.sort(sortedList);

        StListUtils.orderedAdd(sortedList, 6, (ob1, ob2) -> ob1 - ob2);
        StListUtils.orderedAdd(sortedList, 1, (ob1, ob2) -> ob1 - ob2);
        StListUtils.orderedAdd(sortedList, 2, (ob1, ob2) -> ob1 - ob2);
        StListUtils.orderedAdd(sortedList, 13, (ob1, ob2) -> ob1 - ob2);
        StListUtils.orderedAdd(sortedList, 13, (ob1, ob2) -> ob1 - ob2);
        StListUtils.orderedAdd(sortedList, 12, (ob1, ob2) -> ob1 - ob2);

        assertEquals(sortedList, Arrays.asList(1, 2, 3, 4, 4, 5, 6, 10, 11, 12, 13, 13));
        System.out.println(Arrays.toString(sortedList.toArray()));
    }


    @Test
    public void sortedAddTest2() {
        LinkedList<Integer> sortedList = new LinkedList<>();

        StListUtils.orderedAdd(sortedList, 6, (ob1, ob2) -> ob1 - ob2);
        StListUtils.orderedAdd(sortedList, 1, (ob1, ob2) -> ob1 - ob2);
        StListUtils.orderedAdd(sortedList, 2, (ob1, ob2) -> ob1 - ob2);
        StListUtils.orderedAdd(sortedList, 13, (ob1, ob2) -> ob1 - ob2);
        StListUtils.orderedAdd(sortedList, 13, (ob1, ob2) -> ob1 - ob2);
        StListUtils.orderedAdd(sortedList, 12, (ob1, ob2) -> ob1 - ob2);

        assertEquals(sortedList, Arrays.asList(1, 2, 6, 12, 13, 13));
        System.out.println(Arrays.toString(sortedList.toArray()));
    }
}
