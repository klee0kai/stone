package com.github.klee0kai.stone._hidden_.types.holders;

import com.github.klee0kai.stone._hidden_.types.StListUtils;
import com.github.klee0kai.stone._hidden_.types.StThr;

import java.util.LinkedList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Stone Private class
 */
public class StTimeScheduler {

    private static final int MAX_VALUE_AWAIT_TIME = 30;
    private static final int MIN_AWAIT_TIME = 3;

    private final ThreadPoolExecutor secThread = StThr.singleThreadExecutor("stone time scheduler");

    private final StDataAwait<StScheduleTask> timeTaskAwait = new StDataAwait<>();

    public synchronized void schedule(StScheduleTask timerTask) {
        timeTaskAwait.trySend(timerTask);
        if (secThread.getQueue().isEmpty())
            secThread.submit(() -> {
                LinkedList<StScheduleTask> timers = new LinkedList<>();

                //get first task
                StScheduleTask task = timeTaskAwait.await(MAX_VALUE_AWAIT_TIME);
                if (task != null) timers.add(task);

                while (timers.size() > 0) {
                    long now = System.currentTimeMillis();
                    while (!timers.isEmpty() && now >= timers.get(0).scheduledExecutionTime()) {
                        timers.get(0).run();
                        timers.removeFirst();
                    }

                    long awaitTime = !timers.isEmpty() ?
                            Math.max(timers.get(0).scheduledExecutionTime() - now, MIN_AWAIT_TIME)
                            : MAX_VALUE_AWAIT_TIME;

                    task = timeTaskAwait.await(awaitTime);
                    if (task != null)
                        StListUtils.orderedAdd(timers, task, (ob1, ob2) ->
                                (int) (ob1.scheduledExecutionTime() - ob2.scheduledExecutionTime())
                        );
                }

            });
    }


}
