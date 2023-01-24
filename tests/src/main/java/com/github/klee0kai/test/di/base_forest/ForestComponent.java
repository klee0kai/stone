package com.github.klee0kai.test.di.base_forest;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.GcAllScope;
import com.github.klee0kai.stone.annotations.component.ProtectInjected;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.github.klee0kai.test.mowgli.School;
import com.github.klee0kai.test.mowgli.animal.Horse;
import com.github.klee0kai.test.mowgli.animal.Mowgli;
import com.github.klee0kai.test.mowgli.animal.Snake;

@Component
public interface ForestComponent extends IComponent {

    UnitedModule united();

    IdentityModule identity();

    void inject(Horse horse, IStoneLifeCycleOwner iStoneLifeCycleOwner);

    void inject(Horse horse);


    void inject(Mowgli mowgli);

    void inject(Snake snake);

    void inject(School school);


    @GcAllScope
    void gcAll();

    @ProtectInjected(timeMillis = 30)
    void protectInjected(Horse horse);


    @ProtectInjected(timeMillis = 30)
    void protectInjected(Mowgli horse);

    @ProtectInjected(timeMillis = 30)
    void protectInjected(School school);

}
