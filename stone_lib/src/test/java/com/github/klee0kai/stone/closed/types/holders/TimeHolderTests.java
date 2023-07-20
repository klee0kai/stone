package com.github.klee0kai.stone.closed.types.holders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class TimeHolderTests {


    @Test
    public void simpleTest() throws InterruptedException {
        TimeScheduler timer = new TimeScheduler();

        TimeHolder<String> holder1 = new TimeHolder<>(timer, "1", 100);
        TimeHolder<String> holder2 = new TimeHolder<>(timer, "2", 100);
        TimeHolder<String> holder3 = new TimeHolder<>(timer, "3", 120);
        TimeHolder<String> holder4 = new TimeHolder<>(timer, "4", 120);
        TimeHolder<String> holder5 = new TimeHolder<>(timer, "5", 150);
        TimeHolder<String> holder6 = new TimeHolder<>(timer, "5", 50);


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
