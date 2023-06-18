package com.github.klee0kai.test.di.base_forest;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.GcAllScope;
import com.github.klee0kai.stone.annotations.component.Init;
import com.github.klee0kai.stone.annotations.component.ProtectInjected;
import com.github.klee0kai.stone.closed.IPrivateComponent;
import com.github.klee0kai.stone.types.lifecycle.StoneLifeCycleOwner;
import com.github.klee0kai.test.mowgli.School;
import com.github.klee0kai.test.mowgli.animal.Horse;
import com.github.klee0kai.test.mowgli.animal.Mowgli;
import com.github.klee0kai.test.mowgli.animal.Snake;

@Component
public interface ForestComponent extends IPrivateComponent {

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

    @GcAllScope
    void gcAll();

    @ProtectInjected(timeMillis = 30)
    void protectInjected(Horse horse);


    @ProtectInjected(timeMillis = 30)
    void protectInjected(Mowgli horse);

    @ProtectInjected(timeMillis = 30)
    void protectInjected(School school);

}
