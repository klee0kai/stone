package com.github.klee0kai.stone;


import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import org.jetbrains.annotations.NotNull;

public class AndroidStone {

    private static final long PROTECT_TIME_MILLIS = 500;

    public static IStoneLifeCycleOwner lifeCycleOwner(LifecycleOwner owner, long protectTimeMillis) {
        return lifeCycleOwner(owner.getLifecycle(), protectTimeMillis);
    }

    public static IStoneLifeCycleOwner lifeCycleOwner(Lifecycle lifecycle, long protectTimeMillis) {
        return listener -> lifecycle.addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onPause(@NonNull @NotNull LifecycleOwner owner1) {
                DefaultLifecycleObserver.super.onPause(owner1);
                listener.protectForInjected(protectTimeMillis);
            }
        });
    }

    public static IStoneLifeCycleOwner lifeCycleOwner(LifecycleOwner owner) {
        return lifeCycleOwner(owner.getLifecycle(), PROTECT_TIME_MILLIS);
    }

    public static IStoneLifeCycleOwner lifeCycleOwner(Lifecycle lifecycle) {
        return lifeCycleOwner(lifecycle, PROTECT_TIME_MILLIS);
    }


}
