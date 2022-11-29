package com.github.klee0kai.stone.test.types;

import com.github.klee0kai.stone.types.RefCollection;
import com.github.klee0kai.stone.closed.types.TimeHolder;
import com.github.klee0kai.stone.closed.types.TimeScheduler;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RefCollectionTests {

    @Test
    public void clearNullTest() {
        TimeScheduler timeScheduler = new TimeScheduler();
        RefCollection refCollection = new RefCollection();

        refCollection.add(new TimeHolder(timeScheduler, 10, 40));
        refCollection.add(new TimeHolder(timeScheduler, 12, 100));
        refCollection.add(new TimeHolder(timeScheduler, null, 100));
        refCollection.add(new TimeHolder(timeScheduler, null, 100));
        refCollection.add(new TimeHolder(timeScheduler, 1, 100));
        refCollection.add(new TimeHolder(timeScheduler, 3, 100));


        refCollection.clearNulls();
        assertEquals(4, refCollection.getAll().size());
        assertEquals(Arrays.asList(10,12,1,3),refCollection.getAll());

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }

        assertEquals(3, refCollection.getAll().size());
        assertEquals(Arrays.asList(12,1,3),refCollection.getAll());

        try {
            Thread.sleep(60);
        } catch (InterruptedException e) {
        }

        assertEquals(0, refCollection.getAll().size());

    }



    @Test
    public void clearNullTest2() {
        TimeScheduler timeScheduler = new TimeScheduler();
        RefCollection refCollection = new RefCollection();

        refCollection.add(new TimeHolder(timeScheduler, 10, 50));
        refCollection.add(new TimeHolder(timeScheduler, 12, 10));
        refCollection.add(new TimeHolder(timeScheduler, 1, 50));
        refCollection.add(new TimeHolder(timeScheduler, 3, 100));
        refCollection.add(new TimeHolder(timeScheduler, null, 100));
        refCollection.add(new TimeHolder(timeScheduler, null, 100));


        refCollection.clearNulls();
        assertEquals(4, refCollection.getAll().size());
        assertEquals(Arrays.asList(10,12,1,3),refCollection.getAll());


        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
        }

        assertEquals(3, refCollection.getAll().size());
        assertEquals(Arrays.asList(10,1,3),refCollection.getAll());


        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }

        assertEquals(Arrays.asList(3),refCollection.getAll());


        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }

        assertEquals(0, refCollection.getAll().size());

    }

}
