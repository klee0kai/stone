package com.github.klee0kai.test.di.base_forest;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner;
import com.github.klee0kai.test.mowgli.School;
import com.github.klee0kai.test.mowgli.animal.Horse;
import com.github.klee0kai.test.mowgli.animal.Mowgli;
import com.github.klee0kai.test.mowgli.animal.Snake;

@Component
public interface ForestComponent {

    UnitedModule united();

    IdentityModule identity();

    void inject(Horse horse, StoneLifeCycleOwner stoneLifeCycleOwner);

    void inject(Horse horse);


    void inject(Mowgli mowgli);

    void inject(Snake snake);

    void inject(School school);

    @Init
    void initUnitedModule(UnitedModule unitedModule);

    @Init
    void initUnitedModule(Class<? extends UnitedModule> unitedModule);

    @Init
    void iniAllModules(UnitedModule unitedModule, IdentityModule identityModule);

    @RunGc
    @GcAllScope
    void gcAll();

    @ProtectInjected(timeMillis = 30)
    void protectInjected(Horse horse);


    @ProtectInjected(timeMillis = 30)
    void protectInjected(Mowgli horse);

    @ProtectInjected(timeMillis = 30)
    void protectInjected(School school);

}
