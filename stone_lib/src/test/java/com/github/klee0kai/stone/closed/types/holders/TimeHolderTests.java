package com.github.klee0kai.stone.closed.types.holders;

import com.github.klee0kai.stone._hidden_.types.holders.StTimeHolder;
import com.github.klee0kai.stone._hidden_.types.holders.StTimeScheduler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class TimeHolderTests {


    @Test
    public void simpleTest() throws InterruptedException {
        StTimeScheduler timer = new StTimeScheduler();

        StTimeHolder<String> holder1 = new StTimeHolder<>(timer, "1", 100);
        StTimeHolder<String> holder2 = new StTimeHolder<>(timer, "2", 100);
        StTimeHolder<String> holder3 = new StTimeHolder<>(timer, "3", 120);
        StTimeHolder<String> holder4 = new StTimeHolder<>(timer, "4", 120);
        StTimeHolder<String> holder5 = new StTimeHolder<>(timer, "5", 150);
        StTimeHolder<String> holder6 = new StTimeHolder<>(timer, "5", 50);


        Thread.sleep(100);

        assertNull(holder6.get());

        Thread.sleep(500);

        assertNull(holder1.get());
        assertNull(holder2.get());
        assertNull(holder3.get());
        assertNull(holder4.get());
        assertNull(holder5.get());

    }
}
