package com.github.klee0kai.stone.types;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeScheduler {

    private final ThreadPoolExecutor secThread = new ThreadPoolExecutor(0, 1, 0,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(), runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.setName("stone time scheduler");
        return thread;
    });

    private final DataAwait<ScheduleTask> timeTaskAwait = new DataAwait<>();

    public synchronized void schedule(ScheduleTask timerTask) {
        timeTaskAwait.trySend(timerTask);
        if (secThread.getQueue().isEmpty())
            secThread.submit(() -> {
                LinkedList<ScheduleTask> timers = new LinkedList<>();
                {
                    ScheduleTask task = timeTaskAwait.await(100);
                    if (task != null) timers.add(task);
                }

                while (timers.size() > 0) {
                    ScheduleTask curTimer = timers.get(0);
                    long now = System.currentTimeMillis();
                    if (now >= curTimer.scheduledExecutionTime()) {
                        curTimer.run();
                        timers.removeFirst();
                        continue;
                    }
                    long awaitTime = curTimer.scheduledExecutionTime() - now;
                    ScheduleTask task = timeTaskAwait.await(awaitTime);
                    if (task != null)
                        ListUtils.orderedAdd(timers, task, (ob1, ob2) -> (int) (ob1.scheduledExecutionTime() - ob2.scheduledExecutionTime()));
                }

            });
    }

}
