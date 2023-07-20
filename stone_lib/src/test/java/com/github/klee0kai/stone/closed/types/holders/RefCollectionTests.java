package com.github.klee0kai.stone.closed.types.holders;

import com.github.klee0kai.stone.types.wrappers.RefCollection;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RefCollectionTests {

    @Test
    public void clearNullTest() throws InterruptedException {
        StTimeScheduler timeScheduler = new StTimeScheduler();
        RefCollection refCollection = new RefCollection();

        refCollection.add(new StTimeHolder(timeScheduler, 10, 40));
        refCollection.add(new StTimeHolder(timeScheduler, 12, 100));
        refCollection.add(new StTimeHolder(timeScheduler, null, 100));
        refCollection.add(new StTimeHolder(timeScheduler, null, 100));
        refCollection.add(new StTimeHolder(timeScheduler, 1, 100));
        refCollection.add(new StTimeHolder(timeScheduler, 3, 100));


        refCollection.clearNulls();
        assertEquals(4, refCollection.getAll().size());
        assertEquals(Arrays.asList(10, 12, 1, 3), refCollection.getAll());

        Thread.sleep(50);

        assertEquals(3, refCollection.getAll().size());
        assertEquals(Arrays.asList(12, 1, 3), refCollection.getAll());

        Thread.sleep(60);

        assertEquals(0, refCollection.getAll().size());

    }


    @Test
    public void clearNullTest2() throws InterruptedException {
        StTimeScheduler timeScheduler = new StTimeScheduler();
        RefCollection refCollection = new RefCollection();

        refCollection.add(new StTimeHolder(timeScheduler, 10, 50));
        refCollection.add(new StTimeHolder(timeScheduler, 12, 10));
        refCollection.add(new StTimeHolder(timeScheduler, 1, 50));
        refCollection.add(new StTimeHolder(timeScheduler, 3, 100));
        refCollection.add(new StTimeHolder(timeScheduler, null, 100));
        refCollection.add(new StTimeHolder(timeScheduler, null, 100));


        refCollection.clearNulls();
        assertEquals(4, refCollection.getAll().size());
        assertEquals(Arrays.asList(10, 12, 1, 3), refCollection.getAll());


        Thread.sleep(20);

        assertEquals(3, refCollection.getAll().size());
        assertEquals(Arrays.asList(10, 1, 3), refCollection.getAll());


        Thread.sleep(50);

        assertEquals(Arrays.asList(3), refCollection.getAll());


        Thread.sleep(50);

        assertEquals(0, refCollection.getAll().size());

    }

}
