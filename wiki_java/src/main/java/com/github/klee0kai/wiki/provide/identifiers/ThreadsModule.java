package com.github.klee0kai.wiki.provide.identifiers;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Module
public class ThreadsModule {

    @ThreadQualifier(type = ThreadQualifier.ThreadType.Main)
    @Provide(cache = Provide.CacheType.Strong)
    public ThreadPoolExecutor mainThreadPoolExecutor() {
        return new ThreadPoolExecutor(0, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), runnable -> new Thread(runnable));
    }

}